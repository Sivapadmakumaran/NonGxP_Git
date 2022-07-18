package allocator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
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
public class AzureRunner {
	private FrameworkParameters frameworkParameters = FrameworkParameters
			.getInstance();
	private Properties properties;
	private Properties mobileProperties;
	private ResultSummaryManager resultSummaryManager = ResultSummaryManager
			.getInstance();
	private String strReportPath;
	

	/**
	 * The entry point of the test batch execution <br>
	 * Exits with a value of 0 if the test passes and 1 if the test fails
	 * 
	 * @param args
	 *            Command line arguments to the AzureRunner (Not applicable)
	 */
	public static void main(String[] args) {
		AzureRunner azureRunner = new AzureRunner();		
		System.setProperty("ManualTCID", "TC_01_DtoA_Doc_Creation_And_Review_Edit");
		System.setProperty("RunConfiguration","Regression");
		azureRunner.driveBatchExecution();
	}

	private void driveBatchExecution() {
		resultSummaryManager.setRelativePath();
		properties = Settings.getInstance();
		mobileProperties = Settings.getMobilePropertiesInstance();
		
		String runConfiguration;
		
		if (System.getProperty("RunConfiguration") != null && !System.getProperty("RunConfiguration").isEmpty()) {
			runConfiguration = System.getProperty("RunConfiguration");
		} else {
			runConfiguration = properties.getProperty("RunConfiguration");
		}
		
		resultSummaryManager.initializeTestBatch(runConfiguration);

		resultSummaryManager.initializeSummaryReport(1);

		resultSummaryManager.setupErrorLog();
		
		strReportPath = resultSummaryManager.reportPath;
		
		//String[] arrReportFolderName = StringUtils.split(strReportPath,Util.getFileSeparator());
		
		int testBatchStatus = executeTestBatch(1);

		resultSummaryManager.wrapUp(false);
		
		zipDirs(System.getProperty("user.dir")+Util.getFileSeparator()+"Results", System.getProperty("ManualTCID")+".zip", true, strReportPath);
		
		String strTextToWrite = System.getProperty("user.dir")+Util.getFileSeparator()+"Results"+Util.getFileSeparator()+
				System.getProperty("ManualTCID")+".zip";
		
		writeResultPathInFile(System.getProperty("ManualTCID"), strTextToWrite);
		
		System.exit(testBatchStatus);
		
	}
	
	private int executeTestBatch(int nThreads) {
		
		List<SeleniumTestParameters> testInstancesToRun = getRunInfo(frameworkParameters
				.getRunConfiguration());
		
		ExecutorService parallelExecutor = Executors
				.newFixedThreadPool(nThreads);
		
		LinkedList<ParallelRunner> lnklstRunner = new LinkedList<>();
		
		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun
				.size(); currentTestInstance++) {
			ParallelRunner testRunner = new ParallelRunner(
					testInstancesToRun.get(currentTestInstance));
			lnklstRunner.add(testRunner);
			parallelExecutor.execute(testRunner);
		}

		parallelExecutor.shutdown();
		
		while (!parallelExecutor.isTerminated()) {
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (lnklstRunner.isEmpty()) {
			return 0; // All tests flagged as "No" in the Run Manager
		} else {
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
			
			String[] arrManualTCID = StringUtils.split(runManagerAccess.getValue(currentTestInstance, "ManualTCID"), ",");
			
			for(String strManualTCID : arrManualTCID) {
				
				if (System.getProperty("ManualTCID").equalsIgnoreCase(strManualTCID)) {
					
					String executeFlag = runManagerAccess.getValue(currentTestInstance,
							"Execute");

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
						
						String browser;
						
						if (System.getProperty("Browser") != null && !System.getProperty("Browser").isEmpty()) {
							browser = System.getProperty("Browser");
						} else {
							browser = runManagerAccess.getValue(currentTestInstance,
									"Browser");
						}

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
			}
			//String manualTCID = runManagerAccess.getValue(currentTestInstance, "ManualTCID");
		}

		return testInstancesToRun;
	}
	
	public static void zipDirs (String destinationDir,String zipName,boolean deleteExisting,String... sourceDirs) {

		File destinationDirFile = new File(destinationDir);
		File zipFile = new File(destinationDir + File.separatorChar + zipName);
		
		if (!destinationDirFile.exists()) {
			if (!destinationDirFile.mkdirs()) {
			    throw new RuntimeException("cannot create directories ");
			}
		} else {
			boolean exists = zipFile.exists();
			if (exists && deleteExisting && !zipFile.delete()) {
			    throw new RuntimeException("cannot delete existing zip file: " +
			                        zipFile.getAbsolutePath());
			} else if (exists && !deleteExisting) {
			    System.out.println("Zip file already exists: " +
			                        zipFile.getAbsolutePath());
			    return;
			}
		}
		createZip(zipFile,  sourceDirs);
	}
	
	private static void createZip (File destination, String... sourceDirs){

		if (sourceDirs == null) {
			throw new RuntimeException("Source dirs are null");
		}
		
		
		try (ZipOutputStream out = new ZipOutputStream(
		                new BufferedOutputStream(new
		                                    FileOutputStream(destination)))) {
		
			for (String sourceDir : sourceDirs) {
			    File sourceDirFile = new File(sourceDir);
			    if (!sourceDirFile.exists()) {
			        throw new RuntimeException("Source dir doesn't exists "
			                            + sourceDirFile);
			    }
			
			    addDirRecursively(sourceDirFile.getName(),sourceDirFile.getAbsolutePath(),sourceDirFile,out);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String fileToRelativePath (File file, String baseDir) {
		return file.getAbsolutePath()
				.substring(baseDir.length() + 1);
	}

	private static void addDirRecursively (String baseDirName,
	                        String baseDir,
	                        File dirFile,
	                        final ZipOutputStream out) throws IOException {

		File[] files = dirFile.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					addDirRecursively(baseDirName, baseDir, file, out);
					continue;
				}
				ZipEntry zipEntry = new ZipEntry(baseDirName + File.separatorChar +
						fileToRelativePath(file, baseDir));
				BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				zipEntry.setLastModifiedTime(attr.lastModifiedTime());
				zipEntry.setCreationTime(attr.creationTime());
				zipEntry.setLastAccessTime(attr.lastAccessTime());
				zipEntry.setTime(attr.lastModifiedTime().toMillis());
				out.putNextEntry(zipEntry);
				try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
					byte[] b = new byte[1024];
					int count;
					while ((count = in.read(b)) > 0) {
						out.write(b, 0, count);
					}
					out.closeEntry();
				}
			}
		}
	}
	
	private void writeResultPathInFile(String strManualTCID,String strText) {
		String textFilePath = System.getProperty("user.dir")+Util.getFileSeparator()+"Results"+Util.getFileSeparator()+strManualTCID+".txt";
		if(new File(textFilePath).exists()) {
			new File(textFilePath).delete();
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(textFilePath, "UTF-8");
			writer.println(strText);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if(writer!=null) {
				writer.close();
			}
		}
	}
}