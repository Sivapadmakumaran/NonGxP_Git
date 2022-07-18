package allocator;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.channels.FileLock;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import supportlibraries.*;
import com.cognizant.framework.ExcelDataAccessforxlsm;
import com.cognizant.framework.FileLockMechanism;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Util;
import com.cognizant.framework.selenium.*;

/**
 * Class to manage the test execution from HP Quality Center
 * 
 * @author Cognizant
 */
public class QcTestRunner {
	public static String StrScenarioname = null;
	public static String StrTestCaseName = null;
	private static FrameworkParameters frameworkParameters = FrameworkParameters
			.getInstance();
	private static SeleniumTestParameters testParameters;
	public static String StrBrowser;
	private static String StrIterationMode;
	private static String StrStartIteration;
	private static String StrEndIteration;
	private static String StrExecutionMode;
	private static String StrMobileToolName;
	private static String StrMobileExecutionPlatform;
	private static String StrMobileDeviceName;
	private static String StrBrowserVersion;
	private static String StrPlatform;
	

	private QcTestRunner() {
		// To prevent external instantiation of this class
	}

	/**
	 * The entry point of the test execution from HP ALM/QC <br>
	 * Exits with a value of 0 if the test passes and 1 if the test fails
	 * 
	 * @param args
	 *            Command line arguments to control the test parameters (details
	 *            below):<br>
	 *            <b>Argument 1 :</b> The absolute path where the test report is
	 *            to be stored (Mandatory)<br>
	 *            <b>Argument 2 :</b> The name of the scenario which contains
	 *            the test case to be executed (Mandatory)<br>
	 *            <b>Argument 3 :</b> The name of the test case to be executed
	 *            (Mandatory)<br>
	 *            <b>Argument 4 :</b> The name of the test instance to be
	 *            executed (Mandatory)<br>
	 *            <b>Argument 5 :</b> The description of the test case to be
	 *            executed (Optional - Specify SKIP if not required)<br>
	 *            <b>Argument 6 :</b> The iteration mode (Optional - Specify
	 *            SKIP if not required)<br>
	 *            <b>Argument 7 :</b> The start iteration - applicable only for
	 *            RUN_RANGE_OF_ITERATIONS mode (Optional - Specify SKIP if not
	 *            required)<br>
	 *            <b>Argument 8 :</b> The end iteration - applicable only for
	 *            RUN_RANGE_OF_ITERATIONS mode (Optional - Specify SKIP if not
	 *            required)<br>
	 *            <b>Argument 9 :</b> The execution mode (Optional - Specify
	 *            SKIP if not required)<br>
	 *            <b>Argument 10 :</b> The Mobile ToolName(Optional - Specify
	 *            SKIP if not required)<br>
	 *            <b>Argument 11 :</b> The Mobile Execution Platform(Optional -
	 *            Specify SKIP if not required)<br>
	 *            <b>Argument 12 :</b> The device name - applicable only for
	 *            PERFECTO or EMULATED_DEVICE execution modes (Optional -
	 *            Specify SKIP if not required)<br>
	 *            <b>Argument 13 :</b> The browser on which the test is to be
	 *            executed (Optional - Specify SKIP if not required)<br>
	 *            <b>Argument 14 :</b> The browser version (Optional - Specify
	 *            SKIP if not required)<br>
	 *            <b>Argument 15 :</b> The platform on which the test is to be
	 *            executed (Optional - Specify SKIP if not required)
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("\nError: Insufficient parameters!"
					+ "\nUsage: java allocator.QcTestRunner "
					+ "<report-path> "
					+ "<scenario-name> <test-name> <test-instance> "
					+ "<test-description*> "
					+ "<iteration-mode*> <start-iteration*> <end-iteration*> "
					+ "<execution-mode*> <device-name*> "
					+ "<browser*> <browser-version*> <platform*> "
					+ "\n\n * - Optional (specify SKIP if not required)");
			return;
		}
		setRelativePath();
		initializeTestParameters(args);

		String testStatus = driveExecutionFromQc();
		if ("passed".equalsIgnoreCase(testStatus)) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}

	private static void setRelativePath() {
		String relativePath = new File(System.getProperty("user.dir"))
				.getAbsolutePath();
		if (relativePath.contains("allocator")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}

	private static void initializeTestParameters(String[] args) {
		System.setProperty("ReportPath", args[0]);
		Properties properties = Settings.getInstance();
		Properties mobileProperties = Settings.getMobilePropertiesInstance();
		String strBaseDirPath = System.getProperty("user.dir"); 
		String strProcessFolderPath = strBaseDirPath+Util.getFileSeparator()+"Process_Tracker";
		String strTextFilePath = strProcessFolderPath+Util.getFileSeparator()+args[2]+"_"+args[3].replaceAll("Instance", "")+".txt";
		String strLockFile = "ProcessTracker_Lock.xls";
		FileLockMechanism objFileLockMechanism = new FileLockMechanism(Long.valueOf(properties.getProperty("FileLockTimeOut")));
		FileLock objFileLock = objFileLockMechanism.SetLockOnFile(strLockFile);
		if(objFileLock!=null) {
			if(!new File(strProcessFolderPath).exists()){
				new File(strProcessFolderPath).mkdir();
			}
			objFileLockMechanism.ReleaseLockOnFile(objFileLock, strLockFile);
		}
		if(new File(strTextFilePath).exists()){
			new File(strTextFilePath).delete();
		}
		try{
			FileUtils.writeStringToFile(new File(strTextFilePath), "Process ID="+StringUtils.substringBefore(ManagementFactory.getRuntimeMXBean().getName(),"@"), "UTF-8");
		}
		catch(Exception e){
			System.out.println("Error - in writing text file for tracking the process id");
		}
		StrScenarioname=args[1];
		StrTestCaseName=args[2];
		testParameters = new SeleniumTestParameters(args[1], args[2]);
		testParameters.setCurrentTestInstance(args[3]);
		if (args.length >= 5 && !"SKIP".equalsIgnoreCase(args[4])) {
			testParameters.setCurrentTestDescription(args[4]);
		}
		
		ExcelDataAccessforxlsm runManagerAccess = new ExcelDataAccessforxlsm(
				frameworkParameters.getRelativePath(), "Run Manager");
		runManagerAccess.setDatasheetName(properties.getProperty("RunConfiguration"));

		int nTestInstances = runManagerAccess.getLastRowNum();

		for (int currentTestInstance = 1; currentTestInstance <= nTestInstances; currentTestInstance++) {
			String TestCase = runManagerAccess.getValue(currentTestInstance,
					"TestCase");
			int intTestInstance =Integer.parseInt(runManagerAccess.getValue(currentTestInstance,
					"TestInstance"));

			if (args[2].equalsIgnoreCase(TestCase) && intTestInstance==Integer.parseInt(args[3].replaceAll("Instance", ""))) {
				StrBrowser = runManagerAccess.getValue(
						currentTestInstance, "Browser");
				StrIterationMode =runManagerAccess.getValue(
						currentTestInstance, "IterationMode");
				StrStartIteration = runManagerAccess.getValue(
						currentTestInstance, "StartIteration");
				StrEndIteration = runManagerAccess.getValue(
						currentTestInstance, "EndIteration");
				StrExecutionMode = runManagerAccess.getValue(
						currentTestInstance, "ExecutionMode");
				StrMobileToolName = runManagerAccess.getValue(
						currentTestInstance, "MobileToolName");
				StrMobileExecutionPlatform = runManagerAccess.getValue(
						currentTestInstance, "MobileExecutionPlatform");
				StrMobileDeviceName = runManagerAccess.getValue(
						currentTestInstance, "DeviceName");
				StrBrowserVersion = runManagerAccess.getValue(
						currentTestInstance, "BrowserVersion");
				StrPlatform = runManagerAccess.getValue(
						currentTestInstance, "Platform");
				break;
			}
		}
			
				if (StrBrowser!=null && StrBrowser!="") {
					testParameters.setBrowser(Browser.valueOf(StrBrowser));
				} else {
					testParameters.setBrowser(Browser.valueOf(properties
							.getProperty("DefaultBrowser")));
				}
				if (StrIterationMode!=null && StrIterationMode!="") {
					testParameters.setIterationMode(IterationOptions.valueOf(StrIterationMode));
				} else {
					testParameters
					.setIterationMode(IterationOptions.RUN_ALL_ITERATIONS);
				}
				if (StrStartIteration!=null && StrStartIteration!="") {
					testParameters.setStartIteration(Integer.parseInt(StrStartIteration));
				} 
				if (StrEndIteration!=null && StrEndIteration!="") {
					testParameters.setEndIteration(Integer.parseInt(StrEndIteration));
				}
				if (StrExecutionMode!=null && StrExecutionMode!="") {
					testParameters.setExecutionMode(ExecutionMode.valueOf(StrExecutionMode));		
				} else {
					testParameters.setExecutionMode(ExecutionMode.valueOf(properties
							.getProperty("DefaultExecutionMode")));
				}
				if (StrMobileToolName!=null && StrMobileToolName!="") {
					testParameters.setMobileToolName((MobileToolName.valueOf(StrMobileToolName)));
						
				} else {
					testParameters.setMobileToolName(MobileToolName
							.valueOf(mobileProperties
									.getProperty("DefaultMobileToolName")));
				}
				if (StrMobileExecutionPlatform!=null && StrMobileExecutionPlatform!="") {
					testParameters.setMobileExecutionPlatform((MobileExecutionPlatform
							.valueOf(StrMobileExecutionPlatform)));
						
				} else {
					testParameters.setMobileExecutionPlatform(MobileExecutionPlatform
							.valueOf(mobileProperties
									.getProperty("DefaultMobileExecutionPlatform")));
				}
				if (StrMobileDeviceName!=null && StrMobileDeviceName!="") {
					testParameters.setDeviceName(StrMobileDeviceName);
						
				} else {
					testParameters.setDeviceName(mobileProperties
							.getProperty("DefaultDevice"));
				}
		
				if (StrBrowserVersion!=null && StrBrowserVersion!="") {
					testParameters.setBrowserVersion(StrBrowserVersion);
				}
				if (StrPlatform!=null && StrPlatform!="") {
					testParameters.setPlatform(Platform.valueOf(StrPlatform));
				}else {
					testParameters.setPlatform(Platform.valueOf(properties
							.getProperty("DefaultPlatform")));
				}
				if (testParameters.getExecutionMode().equals(ExecutionMode.SEETEST)) {
					testParameters.setSeeTestPort(mobileProperties
							.getProperty("SeeTestDefaultPort"));
				}
	}

	private static String driveExecutionFromQc() {
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.setLinkScreenshotsToTestLog(false);
		driverScript.driveTestExecution();
		return driverScript.getTestStatus();
	}
}