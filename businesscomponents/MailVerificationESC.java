package businesscomponents;

import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.cognizant.framework.Status;

import supportlibraries.ScriptHelper;
import uimap.Common;
import uimap.EscPortal;

public class MailVerificationESC extends CommonFunctions {

	public MailVerificationESC(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}

	private final long timeOutInSeconds = Long.parseLong(properties.getProperty("ObjectSyncTimeout"));
	private final long lngPagetimeOutInSeconds = Long.parseLong(properties.getProperty("PageLoadTimeout"));
	private final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));
	private final long mailNotificationTimeOut = Long.parseLong(properties.getProperty("MailNotificationTimeout"));
	String strSheetName = "Mail_Verification";

	public void verifyMail() {
		try {
			String strPageName = "Esc Portal";
			windowName.put("Window1", driver.getWindowHandle());
			manageAndSwitchNewWindow();
		//	driver.get(dataTable.getData(strSheetName, "Esc_URL"));
			String strESCEMenu = dataTable.getData(strSheetName, "Esc_Menu");
			String strESCMailTab = dataTable.getData(strSheetName, "Tab");
			String strRecordNumber = dataTable.getData(strSheetName, "Record_Number");
			driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
			click(new Common(strESCEMenu).menu, lngPagetimeOutInSeconds, strESCEMenu, "Menu", strPageName, true);
			driverUtil.waitUntilElementVisible(new Common(strESCMailTab).escportaltab, lngPagetimeOutInSeconds,
					strESCMailTab, "Tab", strPageName, false);
			click(new Common(strESCMailTab).escportaltab, lngPagetimeOutInSeconds, strESCMailTab, "Tab", strPageName,
					true);
			driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);

			boolean blnFlag = verifyMailRec();
			if (!blnFlag) {
				report.updateTestLog("Mail Verification",
						"No Such " + strRecordNumber + " record is found in the mail list ", Status.FAIL);
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Mail Verification",
						
						"Mail Record should be display in the Mail list Item", "No Such " + strRecordNumber + " record is found in the mail list ",
						true);
			}
			closeAndSwitchPreviousWindow();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean verifyMailRec() throws ParseException {

		String strPageName = "Approval Request page";
		boolean blnShowMore = false;
		long startTime = System.currentTimeMillis();
		String strRecordNumber = dataTable.getData(strSheetName, "Record_Number");
		String strReportRequest = dataTable.getData(strSheetName, "Report Request");
		String strSuccessMessage = dataTable.getData(strSheetName, "Success_Message");
		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
		driverUtil.waitUntilStalenessOfElement(EscPortal.escListItem, strPageName);
		boolean blnFound = false;

		if (driverUtil.waitUntilElementLocated(EscPortal.escListItem, timeOutInSeconds, "Mail Item", "List",
				strPageName, false)) {
			showmore: do {
			if (objectExists(EscPortal.showMore, "isEnabled", lngMinTimeOutInSeconds, "Show More", "Button",strPageName, false)) {
					click(EscPortal.showMore, lngMinTimeOutInSeconds, "Show More", "Button", strPageName, false);
					driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
					driverUtil.waitUntilElementInVisible(EscPortal.loading, "Loading", "Loading Icon", strPageName);
					blnShowMore = false;
				} else {
					blnShowMore = true;
					break showmore;
				}

			} while (!blnShowMore);

			Do: do

			{
				int i = 0;
				List<WebElement> visibleMail = driver.getWebDriver().findElements(EscPortal.escListItem);
				if (visibleMail.size() > 0) {

					for (WebElement eleVisibleMail : visibleMail) {
						
						String strMailContent = "";

						eleVisibleMail.click();
						driverUtil.waitUntilStalenessOfElement(eleVisibleMail, strPageName);
						driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
						driverUtil.waitUntilElementEnabled(Common.mailBody, lngMinTimeOutInSeconds, strRecordNumber,
								"Mail Body Content", strPageName);
						driverUtil.waitUntilElementVisible(Common.mailBody, lngMinTimeOutInSeconds, strRecordNumber,
								"Mail Body Content", strPageName, false);
						strMailContent = getAttributeValue(Common.mailBody, lngMinTimeOutInSeconds, "outerText",
								"Mail body Content", strPageName);
						if (strMailContent.contains(strRecordNumber)) {
							report.updateTestLog(strRecordNumber, "Record found in the mail list", Status.DONE);
							click(new Common(strReportRequest).escPoratlBtn, lngMinTimeOutInSeconds, strReportRequest,
									"Button", strPageName, true);
							/*
							 * if(driverUtil.waitUntilElementVisible(Common.successMessage,
							 * lngMinTimeOutInSeconds, "Success Message", "Label", strPageName, false)) {
							 */

							String strSuccessMsg = getText(Common.successMessage, lngMinTimeOutInSeconds,
									"Success Message", strPageName);

							if (strSuccessMsg.contains(strSuccessMessage))

							{
								ALMFunctions.UpdateReportLogAndALMForPassStatus(
										"Accept/Reject the " + strRecordNumber + " Case",
										strRecordNumber + " case should be accepted/Rejected",
										strSuccessMsg + " : " + strRecordNumber, true);

							}
						/*	driverUtil.waitUntilElementInVisible(Common.successMessage, "Success Message", "Label",
									strPageName);*/
							blnFound = true;
							break Do;

						}

					}
					
				} else {

					blnFound = false;
					break Do;
				}
			} while (!(blnFound || !(System.currentTimeMillis() - startTime < mailNotificationTimeOut)));

		} else {
			ALMFunctions.ThrowException("Error", "Mails should be present in the home page", "Mail box is empty", true);
			blnFound = false;
		}

		return blnFound;
	}
}
