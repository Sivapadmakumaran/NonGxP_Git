package allocator;

import supportlibraries.DriverScript;
import com.cognizant.framework.selenium.*;

import java.util.HashMap;

import com.cognizant.framework.FrameworkParameters;


/**
 * Class to facilitate parallel execution of test scripts
 * @author Cognizant
 */
class ParallelRunner implements Runnable {
	private final SeleniumTestParameters testParameters;
	private int testBatchStatus = 0;
	public HashMap<String,Object> hmTestDetails = new HashMap<String,Object>();
	
	
	/**
	 * Constructor to initialize the details of the test case to be executed
	 * @param testParameters The {@link SeleniumTestParameters} object (passed from the {@link Allocator})
	 */
	ParallelRunner(SeleniumTestParameters testParameters) {
		super();
		this.testParameters = testParameters;
	}
	
	/**
	 * Function to get the overall test batch status
	 * @return The test batch status (0 = Success, 1 = Failure)
	 */
	public int getTestBatchStatus() {
		return testBatchStatus;
	}
	
	
	@Override
	public void run() {
		long lngExecutionTime = 0;
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		String testReportName, executionTime, testStatus;
		if(frameworkParameters.getStopExecution()) {
			testReportName = "N/A";
			executionTime = "N/A";
			testStatus = "Aborted";
			Allocator.intTestsAborted++;
			testBatchStatus = 1;	// Non-zero outcome indicates failure
		} else {
			DriverScript driverScript = new DriverScript(this.testParameters);
			driverScript.driveTestExecution();
			testReportName = driverScript.getReportName();
			executionTime = driverScript.getExecutionTime();
			testStatus = driverScript.getTestStatus();
			if ("failed".equalsIgnoreCase(testStatus)) {
				testBatchStatus = 1;	// Non-zero outcome indicates failure
				Allocator.intTestsFailed++;
			}
			else {
				Allocator.intTestsPassed++;
			}
			lngExecutionTime = driverScript.getExecutionTimeInMilliSecs();
		}
		ResultSummaryManager resultSummaryManager = ResultSummaryManager.getInstance();
		resultSummaryManager.updateResultSummary(testParameters, testReportName,
															executionTime, testStatus);
		hmTestDetails.put("testCaseTitle", testParameters.getCurrentTestcase());
		hmTestDetails.put("durationInMs", lngExecutionTime);
		hmTestDetails.put("outcome", testStatus);
		hmTestDetails.put("automatedTestName", testParameters.getCurrentTestcase());
	}
}