package businesscomponents;

import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.cognizant.framework.Status;

import supportlibraries.ScriptHelper;
import uimap.Common;


public class MailVerification extends CommonFunctions {

	public MailVerification(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}

	private final long timeOutInSeconds = Long.parseLong(properties.getProperty("ObjectSyncTimeout"));
	private final long lngPagetimeOutInSeconds = Long.parseLong(properties.getProperty("PageLoadTimeout"));
	private final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));
	String strSheetName = "Mail_Verification";

	public void verifyMail1() {
		try {
			String strPageName = "SPARC Portal Home page";
			String strRecordNumber = dataTable.getData(strSheetName, "Record_Number");
			String strSPRButton = dataTable.getData(strSheetName, "Portal_Button");
			if(objectExists(new Common(strSPRButton).button,"isDisplayed" ,lngMinTimeOutInSeconds, strSPRButton, "Button", strPageName,
					false)){
			click(new Common(strSPRButton).button, lngPagetimeOutInSeconds, strSPRButton, "Button", strPageName,
					true);
			
		}
			driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
			driverUtil.waitUntilElementVisible(Common.envelope, lngPagetimeOutInSeconds, "Envelope", "Button",
					strPageName, false);
			click(Common.envelope, lngPagetimeOutInSeconds, "Envelope", "Button", strPageName, true);
			driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
			click(new Common("All").link, lngPagetimeOutInSeconds, "All", "Button", strPageName, true);
			driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
			boolean blnFlag=verifyMailRec1();
			if(!blnFlag)
			{
				report.updateTestLog("Mail Verification","No Such "+strRecordNumber+" record is found in the mail list ",Status.FAIL);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean verifyMailRec1() throws ParseException {

		String strPageName = "My Message";
		boolean blnSubjectFound = false;
		String strExpectedbodyMsg = "";

		// String strFolderName = dataTable.getData(strSheetName,
		// "Folder_Name");
		String[] strMailbody = dataTable.getData(strSheetName, "Mail_Body").split("!");
		String strRecordNumber = dataTable.getData(strSheetName, "Record_Number");
		String strRecordStatus = dataTable.getData(strSheetName, "Status");

		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, "WebMail Home");
		boolean blnFound = false;
		WebElement ele = null;

		if (driverUtil.waitUntilElementLocated(Common.mailItemList, timeOutInSeconds, "Mail Item", "List", strPageName,
				false)) {
			Do: do

			{

				List<WebElement> visibleMail = driver.getWebDriver().findElements(Common.mailItemList);

				for (WebElement eleVisibleMail : visibleMail) 
				{
					String strmailBody ="";
					//click(eleVisibleMail, lngPagetimeOutInSeconds, strRecordNumber, "list", strPageName, false);
					eleVisibleMail.click();
					String strMailSubject = eleVisibleMail.getText().trim();
					driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
					//driverUtil.waitUntilStalenessOfElement(Common.mailBody, strPageName);
					driverUtil.waitUntilElementEnabled(Common.mailBody, lngMinTimeOutInSeconds,strRecordNumber,"Mail Body Content", strPageName);
					//strmailBody=getText(Common.mailBody, lngMinTimeOutInSeconds, "Mail Subject", strPageName);
					//strmailBody=getAttributeValue(Common.mailBody, lngMinTimeOutInSeconds,"innerText" ,"Mail Subject", strPageName);
					if (strMailSubject.contains(strRecordNumber) && strMailSubject.toLowerCase().contains(strRecordStatus.toLowerCase())/* && strmailBody.toLowerCase().contains(strRecordStatus.toLowerCase())*/) 
					{
						report.updateTestLog(strRecordNumber, "Record found in the list item and clicked",
								Status.DONE);
						ele = eleVisibleMail;
					blnSubjectFound = true;
						break Do;

					}

				}

			} while ((blnSubjectFound/* || !(System.currentTimeMillis() - startTime < mailNotificationTimeOut)*/));

		} else {
			ALMFunctions.ThrowException("Error", "Mails should be present in the home page", "Mail box is empty", true);
		}

		if (blnSubjectFound) {

			try {

				driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, "WebMail Home");
				driverUtil.waitUntilStalenessOfElement(ele, strPageName);
				ele.click();
				String strMailSubject = ele.getText().trim();
				String strmailBody = getText(Common.mailBody, lngMinTimeOutInSeconds, "Mail Subject", strPageName);
				int a = 0;
				for (String strMailbodyValue : strMailbody) {

					if (strmailBody.contains(strMailbodyValue)) 
					{
						
						strExpectedbodyMsg = strExpectedbodyMsg.concat(strMailbodyValue + " ");
						blnFound = true;
						a = a + 1;

					}

				}

				if (blnFound && a == strMailbody.length) 
				{
					if (strMailSubject.contains(strRecordNumber) && blnFound) 
					{
						uimap.Common link=new Common(dataTable.getData("Mail_Verification", "Navigation_link"));
						click(link.link, lngPagetimeOutInSeconds, strRecordNumber,
								"Link", strPageName, true);
						driverUtil.waitUntilElementInVisible(link.link, strRecordNumber, "Link", strPageName);
						driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
							ALMFunctions.UpdateReportLogAndALMForPassStatus(
									"Internal Mail notification : " + strMailSubject,
									"Verify the notification recieved with expected content : <br>" + "<br>"
											+ strExpectedbodyMsg,
									"Notification recieved with expected content : <br>" + "<br>" + strmailBody, true);
							report.updateTestLog("Internal Mail notification : " + strMailSubject,
									"Expected Internal Notification : <br>" + strExpectedbodyMsg + "<br>" + "<br>"
											+ "Actual Internal Notification : <br>" + strmailBody,
									Status.PASS);
						} else {

							ALMFunctions.UpdateReportLogAndALMForFailStatus(
									"Internal Mail notification : " + strMailSubject,
									"Verify the notification recieved with expected content : " + "<br>"
											+ strExpectedbodyMsg,
									"Notification recieved with expected content : " + "<br>" + strmailBody, true);
							report.updateTestLog("Internal Mail notification : " + strMailSubject,
									"Expected Internal Notification : <br>" + strExpectedbodyMsg + "<br>" + "<br>"
											+ "Actual Internal Notification : <br>" + strmailBody,
									Status.FAIL);
						}

					
				}

			} catch (StaleElementReferenceException e) {
				e.getSystemInformation();
			}

		}
		return blnFound;
	}
}
