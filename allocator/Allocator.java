package allocator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.openqa.selenium.Platform;
import com.cognizant.framework.AzureIntegration;
import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.ExcelDataAccessforxlsm;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Util;
import com.cognizant.framework.selenium.Browser;
import com.cognizant.framework.selenium.ExecutionMode;
import com.cognizant.framework.selenium.MobileExecutionPlatform;
import com.cognizant.framework.selenium.MobileToolName;
import com.cognizant.framework.selenium.ResultSummaryManager;
import com.cognizant.framework.selenium.SeleniumTestParameters;


/**
 * Class to manage the batch execution of test scripts within the framework
 * 
 * @author Cognizant
 */
public class Allocator {
	private FrameworkParameters frameworkParameters = FrameworkParameters
			.getInstance();
	private Properties properties;
	public static Boolean blnTestSummaryFlag = false;
	public static int intTestsPassed, intTestsFailed, intTestsAborted = 0;
	private Boolean blnUpdatePropertiesFlag = false;
	private Properties testSummary = new Properties();
	private Properties mobileProperties;
	private ResultSummaryManager resultSummaryManager = ResultSummaryManager
			.getInstance();
	private int intTotalTests;
	int intRunID = 0;
	public static String strReportPath;
	private List<HashMap<String,Object>> testDetails; 
	private AzureIntegration azureIntegration;
	

	/**
	 * The entry point of the test batch execution <br>
	 * Exits with a value of 0 if the test passes and 1 if the test fails
	 * 
	 * @param args
	 *            Command line arguments to the Allocator (Not applicable)
	 */
	public static void main(String[] args) {
		Allocator allocator = new Allocator();
		allocator.driveBatchExecution(args);
	}

	private void driveBatchExecution(String[] args) {
		resultSummaryManager.setRelativePath(); //identify the working directory path
		properties = Settings.getInstance(); //get props from Global Settings.properties
		mobileProperties = Settings.getMobilePropertiesInstance(); // get props from Mobile Automation Settings.properties
		
		boolean blnUploadResultsToAzure;
		
		blnUploadResultsToAzure = Boolean.valueOf(properties.getProperty("UpdateResultsInAzure", "false")); // give value of UpdateResultsInAzure if not initialized
		
		if(blnUploadResultsToAzure) {
			azureIntegration = new AzureIntegration(args[0], args[1], args[2], args[3], args[4], args[5]);
			azureIntegration.createRun();
		}
		
		String runConfiguration;
		
		if (System.getProperty("RunConfiguration") != null) { // gets IDE RunConfiguration--> RunConfiguration-Arguments tab-VM arguments
			runConfiguration = System.getProperty("RunConfiguration");
		} else {
			runConfiguration = properties.getProperty("RunConfiguration");//set RunConfiguraion as Regression given in Global Settings.properties
		}
		resultSummaryManager.initializeTestBatch(runConfiguration); //set start time, props and configuration in resultSummaryManager

		int nThreads = Integer.parseInt(properties
				.getProperty("NumberOfThreads")); 
		resultSummaryManager.initializeSummaryReport(nThreads);

		resultSummaryManager.setupErrorLog();// create ErrorLog in report file
		
		strReportPath = resultSummaryManager.reportPath;
		
		int testBatchStatus = executeTestBatch(nThreads);

		resultSummaryManager.wrapUp(false);
		
		if(blnUpdatePropertiesFlag)
		{
			putDataInPropertiesFile("TotalTests", String.valueOf(intTotalTests));
			SimpleDateFormat objDateFormat = new SimpleDateFormat("hh:mm:ss a");
			putDataInPropertiesFile("StartTime", objDateFormat.format(resultSummaryManager.overallStartTime));
			putDataInPropertiesFile("EndTime", objDateFormat.format(resultSummaryManager.overallEndTime));
			putDataInPropertiesFile("OverAllExecutionTime", resultSummaryManager.overAllExecutionTime());
			putDataInPropertiesFile("SummaryPath", resultSummaryManager.reportPath+"\\HTML Results\\Summary.Html");
			SavePropertiesFile();
		}
		
		if(new File(System.getProperty("user.dir")+Util.getFileSeparator()+"Results.zip").exists()) {
			new File(System.getProperty("user.dir")+Util.getFileSeparator()+"Results.zip").delete();
		}
		
		String sourceFile = resultSummaryManager.reportPath;
        FileOutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+Util.getFileSeparator()+"Results.zip");
			ZipOutputStream zipOut = new ZipOutputStream(fos);
	        File fileToZip = new File(sourceFile);
	        zipFile(fileToZip, fileToZip.getName(), zipOut);
	        zipOut.close();
	        fos.close();
		} catch (IOException e) {
			System.out.println("Error in creating zip file for results upload");
		}
		
		resultSummaryManager.launchResultSummary();
		
		if(blnUploadResultsToAzure) {
			azureIntegration.addResultsToRun(testDetails);
			azureIntegration.addAttachmentToRun(System.getProperty("user.dir")+Util.getFileSeparator()+"Results.zip", 
					"Results.zip", "Execution Results");
			azureIntegration.updateRun();
		}
		
		System.exit(testBatchStatus);
	}
	/***** When working with SeeTest/Perfecto Parellel  *****/
		
	private int executeTestBatch(int nThreads) {
		intTotalTests = 0;
		List<SeleniumTestParameters> testInstancesToRun = getRunInfo(frameworkParameters.getRunConfiguration());
		intTotalTests = testInstancesToRun.size();
		ExecutorService parallelExecutor = Executors
				.newFixedThreadPool(nThreads);
		LinkedList<ParallelRunner> lnklstRunner = new LinkedList<>();
		
		
		if(intTotalTests==0) blnUpdatePropertiesFlag = false;

		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun
				.size(); currentTestInstance++) {
			ParallelRunner testRunner = new ParallelRunner(
					testInstancesToRun.get(currentTestInstance));
			lnklstRunner.add(testRunner);
			if((testInstancesToRun.get(currentTestInstance).getCurrentTestcase().equalsIgnoreCase("Test_Summary") && intTotalTests>1)) {
				blnTestSummaryFlag = true;
				blnUpdatePropertiesFlag = true;
				intTotalTests--;
			} else if((testInstancesToRun.get(currentTestInstance).getCurrentTestcase().equalsIgnoreCase("Test_Summary") && intTotalTests==1)) {
				blnTestSummaryFlag = true;
				blnUpdatePropertiesFlag = false;
			} else {
				blnUpdatePropertiesFlag = true;
				parallelExecutor.execute(testRunner);
			}
			
		}

		parallelExecutor.shutdown();
		while (!parallelExecutor.isTerminated()) {
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		boolean blnUploadResultsToAzure;
		
		blnUploadResultsToAzure = Boolean.valueOf(properties.getProperty("UpdateResultsInAzure", "false"));
		
		if(blnUploadResultsToAzure) {
			testDetails = new LinkedList<HashMap<String,Object>>();
			for(int i=0;i<lnklstRunner.size();i++) {
				HashMap<String,Object> hmTestDetails = lnklstRunner.get(i).hmTestDetails;
				hmTestDetails.put("state", "Completed");
				testDetails.add(hmTestDetails);
			}
		}	
		
		if(blnUpdatePropertiesFlag) {
			deleteTestSummaryPropFile();
		}
		
		if (lnklstRunner.isEmpty()) {
			if(blnUpdatePropertiesFlag) {
				putDataInPropertiesFile("PassedTestCasesCount",String.valueOf(0));
				putDataInPropertiesFile("FailedTestCasesCount",String.valueOf(0));
				putDataInPropertiesFile("AbortTestCasesCount",String.valueOf(0));
			}
			return 0; // All tests flagged as "No" in the Run Manager
		} else {
			if(blnUpdatePropertiesFlag) {
				putDataInPropertiesFile("PassedTestCasesCount",String.valueOf(intTestsPassed));
				putDataInPropertiesFile("FailedTestCasesCount",String.valueOf(intTestsFailed));
				putDataInPropertiesFile("AbortTestCasesCount",String.valueOf(intTestsAborted));
			}
	
			int intBatchStatus = 0;
			for(int i=0;i<lnklstRunner.size();i++) {
				if(lnklstRunner.get(i).getTestBatchStatus()==1) {
					intBatchStatus = 1;
				}
			}
			return intBatchStatus;
		}
	}

	private List<SeleniumTestParameters> getRunInfo(String sheetName) {
		ExcelDataAccessforxlsm runManagerAccess = new ExcelDataAccessforxlsm(
				frameworkParameters.getRelativePath(), "Run Manager");
		runManagerAccess.setDatasheetName(sheetName);

		int nTestInstances = runManagerAccess.getLastRowNum();
		List<SeleniumTestParameters> testInstancesToRun = new ArrayList<SeleniumTestParameters>();

		for (int currentTestInstance = 1; currentTestInstance <= nTestInstances; currentTestInstance++) {
			String executeFlag = runManagerAccess.getValue(currentTestInstance,"Execute");

			if ("Yes".equalsIgnoreCase(executeFlag)) {
				String currentScenario = runManagerAccess.getValue(
						currentTestInstance, "TestScenario");
				String currentTestcase = runManagerAccess.getValue(
						currentTestInstance, "TestCase");
				SeleniumTestParameters testParameters = new SeleniumTestParameters(
						currentScenario, currentTestcase);

				testParameters.setCurrentTestInstance("Instance"
						+ runManagerAccess.getValue(currentTestInstance,
								"TestInstance"));
				testParameters.setCurrentTestDescription(runManagerAccess
						.getValue(currentTestInstance, "Description"));

				String iterationMode = runManagerAccess.getValue(
						currentTestInstance, "IterationMode");
				if (!"".equals(iterationMode)) {
					testParameters.setIterationMode(IterationOptions
							.valueOf(iterationMode));
				} else {
					testParameters
							.setIterationMode(IterationOptions.RUN_ALL_ITERATIONS);
				}

				String startIteration = runManagerAccess.getValue(
						currentTestInstance, "StartIteration");
				if (!"".equals(startIteration)) {
					testParameters.setStartIteration(Integer
							.parseInt(startIteration));
				}
				String endIteration = runManagerAccess.getValue(
						currentTestInstance, "EndIteration");
				if (!"".equals(endIteration)) {
					testParameters.setEndIteration(Integer
							.parseInt(endIteration));
				}

				String executionMode = runManagerAccess.getValue(
						currentTestInstance, "ExecutionMode");
				if (!"".equals(executionMode)) {
					testParameters.setExecutionMode(ExecutionMode
							.valueOf(executionMode));
				} else {
					testParameters.setExecutionMode(ExecutionMode
							.valueOf(properties
									.getProperty("DefaultExecutionMode")));
				}

				String toolName = runManagerAccess.getValue(
						currentTestInstance, "MobileToolName");
				if (!"".equals(toolName)) {
					testParameters.setMobileToolName(MobileToolName
							.valueOf(toolName));
				} else {
					testParameters.setMobileToolName(MobileToolName
							.valueOf(mobileProperties
									.getProperty("DefaultMobileToolName")));
				}

				String executionPlatform = runManagerAccess.getValue(
						currentTestInstance, "MobileExecutionPlatform");
				if (!"".equals(executionPlatform)) {
					testParameters
							.setMobileExecutionPlatform(MobileExecutionPlatform
									.valueOf(executionPlatform));
				} else {
					testParameters
							.setMobileExecutionPlatform(MobileExecutionPlatform.valueOf(mobileProperties
									.getProperty("DefaultMobileExecutionPlatform")));
				}

				String mobileOSVersion = runManagerAccess.getValue(
						currentTestInstance, "MobileOSVersion");
				if (!"".equals(mobileOSVersion)) {
					testParameters.setmobileOSVersion(mobileOSVersion);
				}

				String deviceName = runManagerAccess.getValue(
						currentTestInstance, "DeviceName");
				if (!"".equals(deviceName)) {
					testParameters.setDeviceName(deviceName);
				} else {
					testParameters.setDeviceName(mobileProperties
							.getProperty("DefaultDevice"));
				}

				String browser = runManagerAccess.getValue(currentTestInstance,
						"Browser");
				if (!"".equals(browser)) {
					testParameters.setBrowser(Browser.valueOf(browser));
					
				} else {
					testParameters.setBrowser(Browser.valueOf(properties
							.getProperty("DefaultBrowser")));
					
				}
				String browserVersion = runManagerAccess.getValue(
						currentTestInstance, "BrowserVersion");
				if (!"".equals(browserVersion)) {
					testParameters.setBrowserVersion(browserVersion);
				}
				String platform = runManagerAccess.getValue(
						currentTestInstance, "Platform");
				if (!"".equals(platform)) {
					testParameters.setPlatform(Platform.valueOf(platform));
				} else {
					testParameters.setPlatform(Platform.valueOf(properties
							.getProperty("DefaultPlatform")));
				}
				String seeTestPort = runManagerAccess.getValue(
                        currentTestInstance, "SeeTestPort");
				if (!"".equals(seeTestPort)) {
					testParameters.setSeeTestPort(seeTestPort);
				} else {
					testParameters.setSeeTestPort(properties
                                        .getProperty("SeeTestDefaultPort"));
				}
				testInstancesToRun.add(testParameters);
			}
		}

		return testInstancesToRun;
	}
	
	private void putDataInPropertiesFile(String Property, String Value) {
		testSummary.setProperty(Property, Value);
	}
	
	private void SavePropertiesFile() {
		File FileTestSummaryProp = new File(System.getProperty("user.dir")+Util.getFileSeparator()+"//Test Summary.properties");
		try {
			testSummary.store(new FileWriter(FileTestSummaryProp),"Test Summary Details");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error - in saving the properties file");
		}
	}
	
	private void deleteTestSummaryPropFile() {
		File FileTestSummaryProp = new File(System.getProperty("user.dir")+Util.getFileSeparator()+"//Test Summary.properties");
		if(FileTestSummaryProp.exists()) {
			FileTestSummaryProp.delete();
		}
	}
	
	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) {
		try {
			if (fileToZip.isHidden()) {
	            return;
	        }
	        if (fileToZip.isDirectory()) {
	            if (fileName.endsWith("/")) {
	                zipOut.putNextEntry(new ZipEntry(fileName));
	                zipOut.closeEntry();
	            } else {
	                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
	                zipOut.closeEntry();
	            }
	            File[] children = fileToZip.listFiles();
	            for (File childFile : children) {
	                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
	            }
	            return;
	        }
	        FileInputStream fis = new FileInputStream(fileToZip);
	        ZipEntry zipEntry = new ZipEntry(fileName);
	        zipOut.putNextEntry(zipEntry);
	        byte[] bytes = new byte[1024];
	        int length;
	        while ((length = fis.read(bytes)) >= 0) {
	            zipOut.write(bytes, 0, length);
	        }
	        fis.close();
		} catch(Exception e) {
			System.out.println("Error in creating zip file for results upload");
		}
    }
	
	}