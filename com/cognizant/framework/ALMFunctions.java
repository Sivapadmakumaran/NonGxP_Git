package com.cognizant.framework;

import supportlibraries.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Util;
import com.cognizant.framework.selenium.CraftDriver;
import com.cognizant.framework.selenium.SeleniumReport;
import com.cognizant.framework.selenium.SeleniumTestParameters;
import allocator.*;


/**
 * Class for storing general purpose business components
 * @author Cognizant
 */
public class ALMFunctions{
	
	protected SeleniumReport report;
	private final SeleniumTestParameters testParameters;
	private CraftDriver driver;
	private static Properties properties = Settings.getInstance();
	private boolean blnUploadResultsInALM;
	
	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	
	public ALMFunctions(SeleniumReport report,SeleniumTestParameters testParameters,CraftDriver driver) {
		this.report = report;
		this.testParameters =testParameters;
		this.driver = driver;
		blnUploadResultsInALM = Boolean.valueOf(properties.getProperty("UpdateResultsInALM", "true")); 
	}
	
	/**
	 * Function to take Screenshot
	 *
	 */
	
	public void Screenshot(){
		if(blnUploadResultsInALM) {
			boolean blnFlagToExit = false;
			
			/*File TestCaseFolder = new File(GetStepLevelParentFolderPath());
			blnFlagToExit = false;
			if (!TestCaseFolder.exists())			
				{
				TestCaseFolder.mkdir();
				int intCounter = 1;
					while(!TestCaseFolder.exists())
						{	
							try{
								Thread.sleep(1000);
							}
							catch(InterruptedException e)
							{
								report.updateTestLog("ERROR - Test Case folder creation", "Unable to wait for the Test Case folder in Screenshot folder to get created", Status.FAIL);
							}
							intCounter++;
							if (intCounter == 120)
							{			
								blnFlagToExit = true;
								break;
							}
						}
				}
			
			if(blnFlagToExit)
			{
				report.updateTestLog("ERROR -Screenshot folder creation", "Unable to create a folder to store the Screenshots", Status.FAIL);
			}
			
			File steplevelfolder = new File(TestCaseFolder.getPath()+Util.getFileSeparator()+(properties.getProperty("StepLevelScreenshotsFolderName")+" "+(report.getstepno())));

			blnFlagToExit = false;
			if (!steplevelfolder.exists())			
				{
				steplevelfolder.mkdir();
				int intCounter = 1;
					while(!steplevelfolder.exists())
						{	
						try{
							Thread.sleep(1000);
						}
						catch(InterruptedException e)
						{
							report.updateTestLog("ERROR -Screenshot folder creation", "Unable to wait for the step level screenshot folders to get created", Status.FAIL);
						}
							intCounter++;
							if (intCounter == 120)
							{			
								blnFlagToExit = true;
								break;
							}
						}
				}
			
			if(blnFlagToExit)
			{
				report.updateTestLog("ERROR - Step Level Screenshot folder creation", "Unable to create a folder to store the Screenshots on Step Level", Status.FAIL);		
			}
		
			try	{
				switch(properties.getProperty("ScreenshotType").toLowerCase()){
					case "robot":
						BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
						ImageIO.write(image, properties.getProperty("ScreenshotImageFormat"), new File(steplevelfolder+Util.getFileSeparator()+Screenshotname()+"."+properties.getProperty("ScreenshotImageFormat")));
					break;
					case "webdriver":
						File scrFile = ((TakesScreenshot)driver.getWebDriver()).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(scrFile, new File(steplevelfolder+Util.getFileSeparator()+Screenshotname()+"."+properties.getProperty("ScreenshotImageFormat")));
					break;
					case "webdriverwithannotation":
						scrFile = ((TakesScreenshot)driver.getWebDriver()).getScreenshotAs(OutputType.FILE);
						image = ImageIO.read(scrFile);
						WriteAnnotation(image);
						ImageIO.write(image, properties.getProperty("ScreenshotImageFormat"), new File(steplevelfolder+Util.getFileSeparator()+Screenshotname()+"."+properties.getProperty("ScreenshotImageFormat")));
					break;
					case "robotwithannotation":
						image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
						WriteAnnotation(image);
						ImageIO.write(image, properties.getProperty("ScreenshotImageFormat"), new File(steplevelfolder+Util.getFileSeparator()+Screenshotname()+"."+properties.getProperty("ScreenshotImageFormat")));
					break;
					default:
						throw new FrameworkException("Take Screenshot", properties.getProperty("ScreenshotType")+" is not handled");
				}
			}
			catch(Exception e)
			{
				report.updateTestLog("Screenshot", "Error - In capturing the Screenshot", Status.FAIL);
			}*/
		}
	}
	
	public void WriteAnnotation(BufferedImage image)
	{
		SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		Graphics g = image.getGraphics();
		g.setColor(Color.RED);
		g.setFont(new Font("Calibri", Font.BOLD, 19));
		Calendar now = Calendar.getInstance();
		TimeZone timeZone = now.getTimeZone();
		LocalDateTime dt = LocalDateTime.now();
		ZoneId zone = ZoneId.of(timeZone.getID());
		ZonedDateTime zdt = dt.atZone(zone);
		ZoneOffset zos = zdt.getOffset();
		String offset = zos.getId().replaceAll("Z", "+00:00");
		String[] UserName = System.getenv("UserProfile").split("\\\\");
		String text ="Tester: "+UserName[UserName.length-1]+",Executed On: "+displayFormat.format(new Date())+" "+timeZone.getID()+" "+"(UTC"+offset+")"+" ,Initial: "+UserName[UserName.length-1].substring(0, 2);
		drawString(g,text,(int)(image.getWidth()*0.50), 30);
		g.dispose();
	}
	
	/*public void UpdateTestLogALM(String strStepDescription, String strStepExpected, String strStepActual,ALMStatus stepStatus){
		if(blnUploadResultsInALM) {
			String strStepStatus = "";
			String strExcelFileName = testParameters.getCurrentTestcase()+"_"+
					  testParameters.getCurrentTestInstance().replaceAll("Instance", "")+".xls";
			String strExcelFilePath = GetStepLevelParentFolderPath()+Util.getFileSeparator()+strExcelFileName;
			if(report.getstepno()==1){
				if(new File(strExcelFilePath).exists()){
					new File(strExcelFilePath).delete();
				}
				if(!new File(GetStepLevelParentFolderPath()).exists()){
					new File(GetStepLevelParentFolderPath()).mkdir();
				}
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("ALM Steps");
				int intRowStartIndex = 0;
				int intColumnStartIndex = 0;
				sheet.createRow(intRowStartIndex);
				HSSFCellStyle style = workbook.createCellStyle(); 
				style.setAlignment(CellStyle.ALIGN_CENTER);
				HSSFFont font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style.setFont(font);
				HashMap<Integer,String> hmColumnIndexVsParameter = new HashMap<Integer,String>();
				hmColumnIndexVsParameter.put(intColumnStartIndex, "Step Name");
				hmColumnIndexVsParameter.put(++intColumnStartIndex, "Step Description");
				hmColumnIndexVsParameter.put(++intColumnStartIndex, "Expected");
				hmColumnIndexVsParameter.put(++intColumnStartIndex, "Actual");
				hmColumnIndexVsParameter.put(++intColumnStartIndex, "Step Status");
				for(Map.Entry<Integer, String> KeyValuePair: hmColumnIndexVsParameter.entrySet())
				{
					HSSFCell cell = sheet.getRow(intRowStartIndex).createCell(KeyValuePair.getKey());
					cell.setCellStyle(style);
					cell.setCellValue(new HSSFRichTextString(KeyValuePair.getValue()));
				}
				try (FileOutputStream outputStream = new FileOutputStream(strExcelFilePath)) {
		            workbook.write(outputStream);
		            workbook.close();
				}
				catch(Exception e){
					throw new FrameworkException("Error", "Error - in  creating the excel file for ALM Step Level Update");
				}
			}
			try{
				int intColumnStartIndex = 0;
				FileInputStream objFileIS = new FileInputStream(strExcelFilePath);
				HSSFWorkbook workbook = new HSSFWorkbook(objFileIS);
				HSSFSheet sheet = workbook.getSheet("ALM Steps");
				HashMap<Integer,String> hmColumnIndexVsParameter = new HashMap<Integer,String>();
				hmColumnIndexVsParameter.put(intColumnStartIndex, "Step "+String.valueOf(report.getstepno()));
				hmColumnIndexVsParameter.put(++intColumnStartIndex, strStepDescription.replaceAll("<br>", "\n"));
				hmColumnIndexVsParameter.put(++intColumnStartIndex, strStepExpected.replaceAll("<br>", "\n"));
				hmColumnIndexVsParameter.put(++intColumnStartIndex, strStepActual.replaceAll("<br>", "\n"));
				switch(stepStatus){
					case PASS:
						strStepStatus="Passed";
					break;
					case FAIL:
						strStepStatus="Failed";
					break;
				}
				hmColumnIndexVsParameter.put(++intColumnStartIndex, strStepStatus);
				HSSFCellStyle style = workbook.createCellStyle(); //Create new style
				style.setWrapText(true);
				sheet.createRow(report.getstepno());
				for(Map.Entry<Integer, String> KeyValuePair: hmColumnIndexVsParameter.entrySet())
				{
					HSSFCell cell = sheet.getRow(report.getstepno()).createCell(KeyValuePair.getKey());
					cell.setCellStyle(style);
					cell.setCellValue(new HSSFRichTextString(KeyValuePair.getValue()));
					sheet.autoSizeColumn(KeyValuePair.getKey());
				}
				objFileIS.close();
				FileOutputStream objFileOS = new FileOutputStream(new File(strExcelFilePath));
				workbook.write(objFileOS);
				workbook.close();
				objFileOS.close();
			}
			catch (IOException e) {
					// TODO Auto-generated catch block
					throw new FrameworkException("Error", "Error - in writing status in ALM step level excel file "+"\""+strExcelFilePath+"\"");
			}
			
			report.incrementStep();
		}
	}*/
	/**
	 * Function to set the Screenshot file name 
	 *
	 */
public String Screenshotname() 
	{
		DateFormat dat = new SimpleDateFormat(properties.getProperty("ScreenshotNameTimeFormat"));
		Calendar cal = Calendar.getInstance();
		String y = properties.getProperty("ScreenshotName") + properties.getProperty("ScreenshotFileNameDelimiter") + (dat.format(cal.getTime())).toString(); 
		return y;
	}

public void ThrowPageLoadError(String strPageName, boolean blnTakeScreenshotALM){
	String strDescription = "Page Load"; 
	String strExpected = "\"" + strPageName +"\""+ " page should be loaded ";
	String strActual = "\"" + strPageName +"\""+ " page is not loaded in "
			+ "\"" + properties.getProperty("PageLoadTimeout") + "\"" + " seconds";
	if(blnTakeScreenshotALM){
		Screenshot();
	}
	//UpdateTestLogALM(strDescription,strExpected , strActual,ALMStatus.FAIL);
	throw new FrameworkException(strDescription,strActual);
}

public void ThrowException(String strDescription, String strExpected, String strActual, boolean blnTakeScreenshotALM){
	if(blnTakeScreenshotALM){
		Screenshot();
	}
	//UpdateTestLogALM(strDescription, strExpected, strActual,ALMStatus.FAIL);
	throw new FrameworkException(strDescription, strActual);
}

public void UpdateReportLogAndALMForFailStatus(String strDescription, String strExpected, String strActual, boolean blnTakeScreenshotALM){
	if(blnTakeScreenshotALM){
		Screenshot();
	}
	//UpdateTestLogALM(strDescription, strExpected, strActual,ALMStatus.FAIL);
	report.updateTestLog(strDescription, strActual, Status.FAIL);
}

public void UpdateReportLogAndALMForPassStatus(String strDescription, String strExpected, String strActual, boolean blnTakeScreenshotALM){
	if(blnTakeScreenshotALM){
		Screenshot();
	}
	//UpdateTestLogALM(strDescription, strExpected, strActual,ALMStatus.PASS);
	report.updateTestLog(strDescription, strActual, Status.PASS);
}

public void UpdateReportLogNSAndALMForFailStatus(String strDescription, String strExpected, String strActual, boolean blnTakeScreenshotALM){
	if(blnTakeScreenshotALM){
		Screenshot();
	}
	//UpdateTestLogALM(strDescription, strExpected, strActual,ALMStatus.FAIL);
	report.updateTestLogPassNS(strDescription, strActual, Status.FAIL);
}

public void UpdateReportLogNSAndALMForPassStatus(String strDescription, String strExpected, String strActual, boolean blnTakeScreenshotALM){
	if(blnTakeScreenshotALM){
		Screenshot();
	}
	//UpdateTestLogALM(strDescription, strExpected, strActual,ALMStatus.PASS);
	report.updateTestLogPassNS(strDescription, strActual, Status.PASS);
}

public void drawString(Graphics g, String text, int x, int y) 
{
	int RectHeight = 0;
	int RectWidth = 0;
    for (String line : text.split(","))
    {
    	g.drawString(line, x, y += g.getFontMetrics(new Font("Calibri", Font.BOLD, 19)).getHeight());
    	RectHeight = y;
    	if(RectWidth<g.getFontMetrics(new Font("Calibri", Font.BOLD, 19)).stringWidth(line))
    	{
    		RectWidth = g.getFontMetrics(new Font("Calibri", Font.BOLD, 19)).stringWidth(line); 
    	}
    }
    g.setColor(Color.BLUE);
    float thickness = 4;
    ((Graphics2D) g).setStroke(new BasicStroke(thickness));
    g.drawRect(x-2, 35, RectWidth, RectHeight);
}

/*public String GetStepLevelParentFolderPath(){
	if(QcTestRunner.StrTestCaseName!=null && QcTestRunner.StrTestCaseName!=""){
		return System.getenv("Temp")+Util.getFileSeparator()+testParameters.getCurrentTestcase()+"_"+
				testParameters.getCurrentTestInstance().replaceAll("Instance", "");
	}
	else{
		return Allocator.strReportPath+Util.getFileSeparator()+testParameters.getCurrentTestcase()+"_"+
				testParameters.getCurrentTestInstance().replaceAll("Instance", "");
	}
}*/
}