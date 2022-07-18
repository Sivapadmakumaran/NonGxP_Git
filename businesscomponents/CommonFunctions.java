
package businesscomponents;

import java.awt.Robot;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.nio.channels.FileLock;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.mail.internet.InternetAddress;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.ClickAction;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cognizant.framework.FileLockMechanism;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
//import com.gargoylesoftware.htmlunit.javascript.host.URL;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import io.github.sukgu.Shadow;
import supportlibraries.ScriptHelper;
import uimap.Common;
import uimap.EscPortal;

public class CommonFunctions extends CommonActionsAndFunctions {

	public CommonFunctions(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}

	private final long timeOutInSeconds = Long.parseLong(properties.getProperty("ObjectSyncTimeout"));
	private final long lngPagetimeOutInSeconds = Long.parseLong(properties.getProperty("PageLoadTimeout"));
	private final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));
	private final long loadingWindowTimeOut = Long.parseLong(properties.getProperty("LoadingWindowTimeOut"));
	private final long CtrlTimeOut = Long.parseLong(properties.getProperty("UploadControlTimeout"));
	private final String strEqualto = properties.getProperty("Equalto");
	private final String strSemicolon = properties.getProperty("Semicolon");
	private final String strExclamation = properties.getProperty("Exclamation");
	public  HashMap<String, String> windowName = new HashMap<String, String>();
	
	public void invoke() throws IOException, InterruptedException {
		try {
			Robot robot = new Robot();
			robot.mouseMove(0, 0);
		}
		catch(Exception e) {}
		driver.get(dataTable.getData("General_Data", "Application URL"));
		report.updateTestLog("Invoke Application URL", "The below URL is invoked:<br>" + 
				dataTable.getData("General_Data", "Application URL"), Status.DONE);
		ALMFunctions.Screenshot();
		
		//mouseMove(Common.link, timeOutInSeconds, "", "Calendar Button", "");
		
	}
	
	/*
	 * Declaration of Variable
	 */

	// private final SeleniumTestParameters testParameters;

	/**
	 * Method to fill input form in page
	 * 
	 * @param strParameters - Parameters for the form
	 * @param strPageName   - Name of the page in which form exists
	 */
	public void FillInputForm(String strParameters)
	{
		String[] arrParameters_Vs_Value = strParameters.split("~");
		for (String Parameter_Vs_Value : arrParameters_Vs_Value) {
			if (Parameter_Vs_Value.trim().length() > 0) {
				String[] arrParameters = StringUtils.split(Parameter_Vs_Value, ";");

				String strElementType = "";
				String strSection = "";
				String strElementName = "";
				String strValues = "";
				String strWindowName = "";
				String strPageName = "";

				for (int i = 0; i < arrParameters.length; i++) {
					switch (StringUtils.substringBefore(arrParameters[i], "=").toLowerCase()) {
					case "element type":

						strElementType = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "element label":
						strElementName = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "section name":
						strSection = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "element value":
						strValues = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "window name":
						strWindowName = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "page name":
						strPageName = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "storage":
						break;

					default:
						ALMFunctions.ThrowException("Test Data",
								"Only Pre-Defined Form Options must be provided in the test data sheet",
								"Error - Unhandled Form Options " + StringUtils.substringBefore(arrParameters[i], "="),
								false);
					}
				}

				if (strValues.trim().length() > 0) {
					switch (strElementType.toLowerCase()) {
					case "textarea":
						if (strWindowName.length() > 0) {
							dialogTextArea(strWindowName,strElementName, strValues, strPageName);
							}
						else {
							enterTextArea(strElementName, strValues, strPageName);
						}
						break;
					case "textbox":
						if (strWindowName.length() > 0) {
							if (strElementName.length() > 0) {
								enterSparcTextbox(strElementName, strValues, strPageName);
							} else {
								dialogTextbox(strWindowName, strValues, strPageName);
							}

						} else {
							enterTextbox(strElementName, strValues, strPageName);
						}

						break;
					case "warningicon":
						verifyWarningIcon(strElementName, strValues, strPageName);
						break;

					case "textboxlist":
						break;
						
					case "attachfile":
						attachFile(strElementName, strValues, strPageName);
						break;
					case "combobox":
						if (strSection.length() > 0) {
							comboBox(strSection, strElementName, strValues, strPageName);
						} else if (strWindowName.length() > 0) {
							dialogCombobox(strWindowName, strElementName, strValues, strPageName);
						} else {
							comboBoxLookup(strElementName, strValues, strPageName);
						}
						break;
					case "verifyrolesorgroups":
						verifyRolesOrGroups(strValues,strElementName);
						break;
					case "esccombobox":
						esccomboBox(strElementName, strValues, strPageName);
						break;

					case "comboboxtreeview":
						expandTreeView(strElementName, strValues, strPageName);
						break;
					case "unlockcombobox":
						unlockComboBox(strElementName, strValues, strPageName);
						break;
					case "dialogtextarea":
						dialogTextArea(strWindowName,strElementName, strValues, strPageName);
						break;
					case "multisearch":
						multiSearchValue(strElementName, strValues, strPageName);
						break;
					case "buttonnotexist":
						verifyButtonNotExist(strValues, strPageName);
						break;
					case "dropdown":
						dropDown(strElementName, strValues, strPageName);
						break;
					case "select":
						selectDropdown(strElementName, strValues, strPageName);
						break;
					case "spselect":
						spSelectDropdown(strElementName, strValues, strPageName);
						break;
					case "dialogdropdown":
						
						if (strWindowName.length() > 0) {
							dialogDropDown(strWindowName,strElementName, strValues, strPageName);
						} 
						else {
							dialogDropDown(strWindowName,strElementName, strValues, strPageName);
						}
						break;
					case "dialogsearch":
						dialogSearch(strElementName, strValues, strPageName);
						break;
					case"verifyattachment":
						verifyAttachment(strValues, strPageName);
						break;
					case "cleartextbox":
						clearTextbox(strElementName, strValues, strPageName);
						break;
					case "verifyattachmentsection":
						verifyReqItemAttachment(strElementName, strValues, strPageName);
						break;
					case "combotextbox":
						comboTextBox(strElementName, strValues, strPageName);
						break;
					case "dialogcheckbox":
						dialogCheckBox(strValues, strPageName);
						break;
					case "selectrecord":
						selectRecord(strValues);
						break;	
					case "verifystate":
						verifyStatus(strValues, strPageName);
						break;
					case "getreqnum":
						getReqNum(strElementName, strValues, strPageName);
						break;
					case "fieldexist":
						verifyFieldExist(strElementName, strValues, strPageName);
						break;
					case "titlecard":
						titleCard(strElementName, strValues, strPageName);
						break;
					case "breadcrumblink":
						breadcrumbLink(strElementName, strValues, strPageName);
						break;
					case "navlink":
						navLink(strValues, strPageName);
						break;
					case "button":
						if (strWindowName.length() > 0) {
						dialogbutton(strWindowName, strValues, strPageName);
						} 
						else if (strSection.length() > 0) {
						tabButton(strValues, strPageName);
						}
						else if (strValues.equalsIgnoreCase("back")) {
						backButton(strValues, strPageName);
						} 
						else if (strValues.equalsIgnoreCase("personalization")) {
						personalizationButton(strValues, strPageName);
						}
						else if (strElementName.equalsIgnoreCase("navigation")) {
						navigationButton(strValues, strPageName);
						}
						else if (strElementName.equalsIgnoreCase("logo")) { // amit 06-01-2022
						logoButton(strValues, strPageName);
						}
						else if (strElementName.equalsIgnoreCase("info")) { // amit 07-01-2022
						infoButton(strValues, strPageName);
						} 
						else if (strElementName.equalsIgnoreCase("filter")) { 
							filterButton(strValues, strPageName);
							} 
						else if (strElementName.equalsIgnoreCase("onhold")) {
							onHoldButton(strValues, strPageName);
							}
						else {
						button(strValues, strPageName);
						}
						break;
					case "dropdownarrowwithoutname":
						dropDownArrowWithoutName(strElementName, strValues, strPageName);
						break;
					
					case "navback":
						navigateBack(strValues, strPageName);
						break;
					case "refresh":
						refreshPage(strValues, strPageName);
						break;
					case "tablelinktext":
						findRecordAndCaptureID(strElementName, strValues, strPageName);
						break;
					case "imgbutton":
						imgButton(strElementName, strValues, strPageName);
						break;
					case "verifyknownerror"	:
						verifyKnownError(strElementName, strValues, strPageName);
						break;
					case "pagescroll":
						pagescroll(strElementName, strValues, strPageName);
						break;
					case "verifyautopopulatedvalues"	:
						verifyAutoPopulatedValue(strElementName, strValues, strPageName);
						break;
					case "getalertmsg":
						getAlertMsg(strValues, strPageName);
						break;
					case "textcontentbody":
						enterTextContentBody(strElementName,strValues, strPageName);
						break;
					case "norecord":
						verifyNoRecordDisplayInTable(strValues);
						break;
					case "norecordwithcondition":
						verifyNoRecordForAppliedCondtion(strElementName, strValues, strPageName);
						break;
					case "closetab":
						closeTab(strValues, strPageName);
						break;
					case "switchtab":
						switchTab(strValues, strPageName);
						break;
					case "dialogradiobutton":
						dialogSelectRadioButton(strElementName, strValues, strPageName);
						break;
					case "prefilltext":
						preFilledText(strElementName, strValues, strPageName);
						break;
					case "screenshot":

						report.updateTestLog("Screen Shot", "Screen Shot Captured in " + strPageName,
								Status.SCREENSHOT);
						ALMFunctions.Screenshot();
						break;
					case "commentsbutton":
						cmtbutton(strValues, strPageName);
						break;
					case "mailtextbox":
						mailTextbox(strElementName, strValues, strPageName);
						break;
					case "link":
						link(strValues, strPageName);
						break;
					case "linkexist":
						verifyLinkExist(strValues, strPageName);
						break;
					case "getlinktext":
						getLinkText(strElementName, strValues, strPageName);
						break;
					case "tab":
						tab(strValues, strPageName);
						break;
					case "alert":
						acceptAlert(lngMinTimeOutInSeconds);
						break;
					case "checkbox":
						By locator = null;

						if (strWindowName.length() > 0) {
							locator = new uimap.Common(strWindowName, strElementName, strValues).checkbox;
							checkbox(locator, strValues, strPageName);

						} else {
							locator = new uimap.Common(strValues).checkbox;
							checkbox(locator, strValues, strPageName);
						}
						break;
					case "datepicker":
						datePicker(strElementName, strValues, strPageName);
						break;
					case "errorhandling":
						errorHandling(strValues, strPageName);
						break;

					case "getcasenumber":
						getCaseNumber(strElementName, strValues, strPageName);
						break;
					case "surveychoice":
						surveyChoice(strElementName, strValues, strPageName);
						break;
					case "listitem":
						clickListItem(strElementName, strValues, strPageName);
						break;
					case "menu":
						clickMenu(strValues, strPageName);
						break;
					case "frame":
						if(strValues.equalsIgnoreCase("switchframe") || strValues.equalsIgnoreCase("default"))
						 {
							frameSwitchAndSwitchBack(Common.frame, strValues, strPageName);
						 }
						else if(strValues.equalsIgnoreCase("dialog")) {
							frameSwitchAndSwitchBack(Common.dialogFrame, strValues, strPageName);
						}
						else {
							locator = new uimap.Common(strElementName).commoniframe;
							frameSwitchAndSwitchBack(locator, strValues, strPageName);
							}
						
						break;
					case "surveyframe":
						frameSwitchAndSwitchBack(Common.surveyFrame, strValues, strPageName);
						break;
					case "verifytasktype":
						verifyTaskTypeState(strElementName, strValues, strPageName);
						break;	
						
					case "globalsearch":
						globalSearch(strValues, strPageName);
						break;
					
					case "retrieverecordfromtable":
						retrieveRecordFromTable(strElementName, strValues, strPageName);
						break;	
					case "searchtextbox":
						searchTextBox(strElementName, strValues, strPageName);
						break;
					case "textboxsearch":
						textBoxsearch(strValues, strPageName);
						break;
					
						
					
					case "alertaccepting":
						alertHandling(strPageName);
						break;
					case "alerttextverification":
						alertTextVerification(strValues,strPageName);
						break;	
					case "verifyobjectstate":
						verifyObjectState(strElementName, strValues, strPageName);
						break;
					case "findrecordandverifyintable":
						findRecordAndVerifyInTable(strElementName, strValues, strPageName);
						break;
					
					case "verifymessage":
						verifyMessage(strValues, strPageName);
						break;
					case "verifyrecordnotexistintbl":
						verifyRecordNotExistInTbl(strElementName, strValues, strPageName);
						break;
					
					case "retrieverecordfromactivitylog":
						retrieveRecordFromActivityLog(strElementName, strValues, strPageName);
						break;
					case "multiplevaluedropdown":
						multipleValuedropDown(strElementName, strValues, strPageName);
						break;
					case "partiallink":
						partialLink(strValues, strPageName);
						break;
					case "gettextboxvalue":
						getValueFromTextBox(strElementName, strValues, strPageName);
						break;
					case "multivaldropdownwithoutname":
						selectMultipleValueDropdownWithoutName(strElementName, strValues, strPageName);
						break;
					case "lookupsearch":
						lookupSearch(strElementName, strValues, strPageName);
						break;	
					case "findrecordandclick":
						findRecordAndClick(strElementName, strValues, strPageName);
						break;
					case "findrecordandnavigate":
						findRecordAndNavigate(strValues, strPageName);
						break;
					case "waitforloading":
						waitForLoading(strElementName, strValues, strPageName);
						break;
					case "rightclickform":
						righClickForm(strElementName, strValues, strPageName);
						break;
					case "rightclickformandselectmulti":
						righClickFormandSelectmultiple(strElementName, strValues, strPageName);
						break;
						
					case "rightclickbreadcrumbandselect":
						rightclickOnBreadCrumbandselect(strElementName, strValues, strPageName);
						break;
						
					case "sortbydescending":
						sortByDescending(strElementName, strValues, strPageName);
						break;
					case "sortbyascending":
						sortByAscending(strElementName, strValues, strPageName);
						break;
					case "customizingcolumn":
						customizingColumn(strElementName, strValues, strPageName);
						break;	
					case "filter":
						filter(strElementName, strValues, strPageName);
						break;	
						
					case "upload":
						UploadFileAutoIT(strElementName,strValues,strPageName);
					case "sptextcontentbody":
						spEnterTextContentBody(strElementName,strValues, strPageName);
						break;	
					case "rightclickandverify":
						rightClickAndVerify(strElementName, strValues, strPageName);
						break;
					case "rightclickandselect":
						rightclickandselect(strElementName, strValues, strPageName);
						break;
					case "clickonsparclabel":
						clickOnSparcLabel(strElementName, strPageName);
						break;
					case "verifylabelexist":
						verifyLabelExist(strElementName, strValues, strPageName);
						break;
					case "spdropdownbutton":
						spDropDownbutton(strElementName, strValues, strPageName);
						break;
					// amit 05-01-2022
					case "getvaluefromdropdown":
						getDisplayedFromDropdown(strElementName, strValues, strPageName);
						break;
						// amit 05-01-2022
					case "verifylinks":
						clickOnLinkAndVerify(strValues, strPageName);
						break;
						// amit 05-01-2022
					case "comparetext":
						compareText(driver.findElement(new Common(strElementName).anyLabelValue), strValues);
						break;

					case "getvalfromspbutton":
						getValueFromspDropDownButton(strElementName, strValues, strPageName);
						break;
					case "validto":
						validTo(strElementName, strValues, strPageName);
						break;
					case"verifysearchtextboxandselectvalue":
						verifySearchTextboxandSelectValue(strElementName, strValues, strPageName);
						break;
					case "cardlabelandclick":
						clickonCardlabel(strElementName, strValues, strPageName);
						break;
					case "getcomboboxvalue":
						getValueFromComboBox(strElementName, strValues, strPageName);
						break;
					case "getunlockcomboboxvalue":
						getValueFromUnlockCombobox(strElementName, strValues, strPageName);
						break;
					case "rateonarticle":
						clickonlabel(strElementName, strValues, strPageName);
						break;
					case "geteditabletextcontentbodyvalue":
						getValueFromEditableTextContentBody(strElementName, strValues, strPageName);
						break;
					case "validatecasenumber":
						validateCaseNumber(strElementName, strValues, strPageName);
						break;
					case "verifymandatoryfieldexist":
						verifyMandatoryFieldExist(strElementName, strValues, strPageName);
						break;
					case "verifyenabledtextboxstate":
						verifyenabledTextBoxStatus(strValues, strPageName);
						break;
					case"verifyselecteddropdownvalue":
						verifySelectedDropdownValue(strElementName, strValues, strPageName);
						break;
					case "dialogconfirm":
						dialogConfirm(strValues, strPageName);
						break;
					case "gettextareavalue":
						getValueFromReadonlyTextArea(strElementName, strValues, strPageName);
						break;
					case "edittablerecords":
                        editTableRecords(strElementName, strValues, strPageName);
                        break;
					case "gettextcontentbodyvalue":
						getValueFromTextContentBody(strElementName, strValues, strPageName);
						break;
					case "sparcdialogsearch":
						sparcdialogSearch(strElementName, strValues, strPageName);
						break;
					case "verifytag":
						verifyTag(strElementName, strValues, strPageName);
						break;
					case "validateemailbody":
						validateEmailBody(strElementName, strValues, strPageName);
						break;
					case "findpartialrecordandclick":
						findPartialRecordAndClick(strElementName, strValues, strPageName);
						break;
					case "rownocomparison":
						rowNoComparison(strElementName, strValues, strPageName);
						break;
						
					case "closeandswitchpreviouswindow":
						closeAndSwitchPreviousWindow();
						break;
					case "manageandswitchnewwindow":
						windowName.put("Window1", driver.getWindowHandle());
						manageAndSwitchNewWindow();
						break;
					default:
					
						ALMFunctions.ThrowException("Test Data",
								"Only Pre-Defined Fields Type must be provided in the test data sheet",
								"Error - Unhandled Field Type " + strElementType, false);
					}
				}
				// }
			}
		}
	}

	
	
	/**
	 * Keyword - To invoke application URL `
	 * 
	 * @param - No parameters
	 * 
	 * @return - No return
	 */

	public void launchApp() {
		try {
			Robot robot = new Robot();
			robot.mouseMove(0, 0);
		}
		catch(Exception e) {}
		driver.get(dataTable.getData("General_Data", "Application_URL"));
		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, "Home");
		report.updateTestLog("Environment", "Test Envirnoment :" + dataTable.getData("General_Data", "Environment"),
				Status.DONE);
		report.updateTestLog("Invoke Application",
				"Invoke the application under test @ " + dataTable.getData("General_Data", "Application_URL"),
				Status.DONE);

	}

	/**
	 * Function to login into SPRAC(Service Now) application
	 * 
	 * @param No parameter
	 * @return No return value
	 */

	public void loginAndVerify() {
		launchApp();
		String strPageName = "ServiceNow Home Page";
		String strApplicationName = dataTable.getData("General_Data", "Environment");
		try {
			String strUsername = dataTable.getData("General_Data", "Username");
			String strPassword = dataTable.getData("General_Data", "Password");
			
			sendkeys(new Common("User name").textbox, lngPagetimeOutInSeconds, strUsername,"User Name" , strPageName);
			sendkeys(new Common("Password").textbox, lngPagetimeOutInSeconds, strPassword, "Password", strPageName);
			driverUtil.waitUntilStalenessOfElement(new Common("Log in").CommentsButton, lngMinTimeOutInSeconds,strPageName);
			clickByJS(new Common("Log in").CommentsButton, lngMinTimeOutInSeconds, "Log in", "Button", strPageName, true);
			driverUtil.waitUntilStalenessOfElement(new Common("Log in").CommentsButton, lngMinTimeOutInSeconds,strPageName);
			driverUtil.waitUntilElementInVisible(Common.homePageLoadingIcon, "Home page Loading", "Loading Icon",
					strPageName);
			driverUtil.waitUntilElementInVisible(Common.homePageLoadingIcon, "Home page Loading", "Loading Icon",
					strPageName);
			/*
			 * driverUtil.waitUntilElementEnabled(Common.homePageValidation,
			 * lngPagetimeOutInSeconds, "Home Page", "Lable", strPageName);
			 */

			// String strUsername =properties.getProperty("UserName");

			if (driverUtil.waitUntilElementEnabled(Common.userMenuLabel, lngMinTimeOutInSeconds, "UserName", "label",
					strPageName)) {
				String strHomePageValidation = getText(Common.userMenuLabel, lngPagetimeOutInSeconds, "UserName",
						strPageName);

				ALMFunctions.UpdateReportLogAndALMForPassStatus("Login home page validation",
						"User should able to login into the " + strApplicationName + " application",
						"Login successfully into the " + strApplicationName + " application as "
								+ strHomePageValidation,
						true);

			} else {

				ALMFunctions.ThrowException("Login Home Page",
						"User should able to login into the " + strApplicationName + " application",
						"Error:-unable to login into the " + strApplicationName + " application", true);
			}

		} catch (Exception ex) {
			report.updateTestLog("Login Error", ex.getMessage(), Status.FAIL);

			ALMFunctions.ThrowException("Login",
					"User should able to login into the " + strApplicationName + " application",
					"Error:-unable to login into the " + strApplicationName + " application", true);
		}
	}

	/**
	 * Function to Case Creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void caseCreation() {

		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Create Case", "~", true, true);
		FillInputForm(strInputParameters);

	}
	/**
     * Function to Create taskk
     * 
      * @param No parameter
     * @return No return value
     */
     public void knowledgeManagement() {
             String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "KnowledgeManagement", "~", true, true);

             FillInputForm(strInputParameters);

     }
     
     /**
     * Function to Create taskk
     * 
      * @param No parameter
     * @return No return value
     */
     public void dataPrivacyIncident() {
             String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "dataPrivacyIncident", "~", true, true);

             FillInputForm(strInputParameters);

     }


	/**
	 * Function to Child Case Creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void childCaseCreation() {

		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Child Case Creation", "~", true, true);
		FillInputForm(strInputParameters);

	}

	/**
	 * Function to Create taskk
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void createTask() {
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Create Task", "~", true, true);

		FillInputForm(strInputParameters);

	}

	/**
	 * Function to accept task with different status
	 * 
	 * @param No parameter
	 * @return No return value
	 */

	public void acceptCase() {

		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Accept task", "~", true, true);
		FillInputForm(strInputParameters);

	}

	/**
	 * Function to accept task with different status
	 * 
	 * @param No parameter
	 * @return No return value
	 */

	public void assignTo() {

		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Assign To", "~", true, true);
		FillInputForm(strInputParameters);

	}

	public void edit() {

		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Edit", "~", true, true);
		FillInputForm(strInputParameters);

	}

	public void survey() 
	{
		String strPageName = "Take Survey page";
		windowName.put("Window1", driver.getWindowHandle());
		manageAndSwitchNewWindow();
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Survey", "~", true, true);
		FillInputForm(strInputParameters);
		if (objectExists(Common.surveySucessMsg, "isDisplayed", lngPagetimeOutInSeconds, "Survey Success message",
				"label", strPageName, false))
		{
			String strSurveyMsg = getText(Common.surveySucessMsg, lngMinTimeOutInSeconds, "Survey Messaage",
					strPageName);
			report.updateTestLog("Take Survey", strSurveyMsg, Status.PASS);

		} else {
			report.updateTestLog("Take Survey", "Survey is not submitted successfully", Status.PASS);

		}
		closeAndSwitchPreviousWindow();

	}

	/**
	 * Function to select multiple navigation
	 * 
	 * @param No parameter
	 * @return No return value
	 */

	public void navigationButton() {
		
		String[] strLable = dataTable.getExpectedResult("Navigation_Button").split("!");
		String strPageName = "Service Now Page";
		frameSwitchAndSwitchBack(Common.frame, "default", strPageName);
		if(objectExists(Common.homePageLogo, "isDisplayed", timeOutInSeconds, "Application Title", "Logo", strPageName, false)) {
			click(Common.homePageLogo, timeOutInSeconds, "Application Title", "Logo", strPageName,
					true);
		}
		driverUtil.waitUntilStalenessOfElement(Common.filterNavigationButton, strPageName);
//		String strFilterNavButton = getAttributeValue(Common.filterNavigationButton, lngPagetimeOutInSeconds,
//				"ariaHidden", "Filter Navigation", strPageName).trim();
//		if (strFilterNavButton.equals("false")) {
//			click(Common.filterNavigationButton, lngPagetimeOutInSeconds, "Filter Navigation", "Button", strPageName,
//					true);
//
//		}

		click(Common.allAppsTab, lngPagetimeOutInSeconds, "All App Tab", "Button", strPageName, true);
		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
		uimap.Common navButton = new uimap.Common(strLable[0]);

		if (strLable.length == 1) {
			if (objectExists(Common.filterNavigation, "isDisplayed", lngMinTimeOutInSeconds, "Filter Navigator","Search", strPageName, false)) {
				click(Common.filterNavigation, lngMinTimeOutInSeconds, "Filter Navigator", "Textbox", strPageName,
						false);
				sendkeys(Common.filterNavigation, lngMinTimeOutInSeconds, strLable[0], "Filter Navigator", strPageName);
				sendkeys(Common.filterNavigation, lngMinTimeOutInSeconds, Keys.ENTER, "Filter Navigator", strPageName);
				driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);

			}

		} else {

			String strExpandValue = getAttributeValue(navButton.navigationbutton, lngPagetimeOutInSeconds,
					"aria-expanded", strLable[0], strPageName);
			if (strExpandValue.equals("false")) {
				click(navButton.navigationbutton, lngMinTimeOutInSeconds, strLable[0], "Navigation Button", strPageName,
						true);
				report.updateTestLog("Navigation", "User is navigated to " + strLable[0], Status.DONE);
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Navigation",
						"User should be navigated to " + strLable[0], "User is navigated to " + strLable[0], true);

			}

			switch (String.valueOf(strLable.length)) {

			case "2":
				uimap.Common subnavButton1 = new uimap.Common(strLable[0], strLable[1]);
				if (objectExists(subnavButton1.navigationbutton, "isEnabled", lngMinTimeOutInSeconds, strLable[1],
						"Filter Navigation", strPageName, false)) {
					click(subnavButton1.navigationbutton, lngMinTimeOutInSeconds, strLable[1], "Navigation Button",
							strPageName, true);
					report.updateTestLog("Navigation", "User is navigated to " + strLable[0] + "->" + strLable[1],
							Status.DONE);
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Navigation",
							"User should be navigated to " + strLable[0] + "->" + strLable[1],
							"User is navigated to " + strLable[0] + "->" + strLable[1], true);
					driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);

				} else {
					ALMFunctions.ThrowException("Filter Navigation",
							strLable[1] + " Filter Navigation button should be display in the Filter Navigation List",
							"Error -" + strLable[1]
									+ " Filter Navigation button is not displayed in the Filter Navigation List",
							true);

				}

				break;
			case "3":
				subnavButton1 = new uimap.Common(strLable[0], strLable[1]);
				uimap.Common subnavButton2 = new uimap.Common(strLable[0], strLable[1], strLable[2]);
				if (objectExists(subnavButton1.navigationbutton, "isEnabled", lngMinTimeOutInSeconds, strLable[1],
						"Filter Navigation", strPageName, false)) {
					strExpandValue = getAttributeValue(subnavButton1.navigationbutton, lngMinTimeOutInSeconds,
							"aria-expanded", strLable[1], strPageName);
					if (strExpandValue.equals("false")) {
						click(subnavButton1.navigationbutton, lngMinTimeOutInSeconds, strLable[1], "Navigation Button",
								strPageName, true);
					}
					if (objectExists(subnavButton2.navigationbutton, "isEnabled", lngMinTimeOutInSeconds, strLable[2],
							"Filter Navigation", strPageName, false)) {
						click(subnavButton2.navigationbutton, lngMinTimeOutInSeconds, strLable[2], "Navigation Button",
								strPageName, true);
						report.updateTestLog("Navigation",
								"User is navigated to " + strLable[0] + "->" + strLable[1] + "->" + strLable[2],
								Status.DONE);
						ALMFunctions.UpdateReportLogAndALMForPassStatus("Navigation",
								"User should be navigated to " + strLable[0] + "->" + strLable[1] + "->" + strLable[2],
								"User is navigated to " + strLable[0] + "->" + strLable[1] + "->" + strLable[2], true);
						driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);

					} else {

						ALMFunctions.ThrowException("Filter Navigation",
								strLable[2]
										+ " Filter Navigation button should be display in the Filter Navigation List",
								"Error -" + strLable[2]
										+ " Filter Navigation button is not displayed in the Filter Navigation List",
								true);

					}
				} else {
					ALMFunctions.ThrowException("Filter Navigation",
							strLable[1] + " Filter Navigation button should be display in the Filter Navigation List",
							"Error -" + strLable[1]
									+ " Filter Navigation button is not displayed in the Filter Navigation List",
							true);

				}
				break;
			}
		}
	}

	public void verifyFilterNavigationtNotexist() {

		String strLable[] = dataTable.getExpectedResult("Navigation_Button").split("!");
		String strNavigation = "";
		if (strLable.length == 1) {
			strNavigation = strLable[0];
		} else if (strLable.length == 2) {
			strNavigation = strLable[0] + " > " + strLable[1];
		} else {

			strNavigation = strLable[0] + " > " + strLable[1] + " > " + strLable[2];
		}
		String strPageName = dataTable.getExpectedResult("PageName");
		click(Common.allAppsTab, lngPagetimeOutInSeconds, "All App Tab", "Button", strPageName, true);
		boolean blnRecordFound = false;
		if (objectExists(Common.filterNavigation, "isDisplayed", lngMinTimeOutInSeconds, "Filter Navigator", "Search",
				strPageName, false)) {
			click(Common.filterNavigation, lngMinTimeOutInSeconds, "Filter Navigator", "Textbox", strPageName, false);
			sendkeys(Common.filterNavigation, lngMinTimeOutInSeconds, strLable[0], "Filter Navigator", strPageName);
			sendkeys(Common.filterNavigation, lngMinTimeOutInSeconds, Keys.ENTER, "Filter Navigator", strPageName);
			driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		}

		uimap.Common navButton = new uimap.Common(strLable[0]);

		if (objectExists(navButton.navigationbutton, "isDisplayed", lngMinTimeOutInSeconds, strLable[0],
				"Navigation Button", strPageName, false)) {
			blnRecordFound = true;

		}

		if (blnRecordFound) {
			uimap.Common subnavButton1 = new uimap.Common(strLable[0], strLable[1]);
			if (objectExists(subnavButton1.navigationbutton, "isDisplayed", lngMinTimeOutInSeconds, strLable[1],
					"Navigation Button", strPageName, false)) {
				blnRecordFound = true;
			} else {
				blnRecordFound = false;
			}

		}
		if (blnRecordFound) {
			uimap.Common subnavButton2 = new uimap.Common(strLable[0], strLable[1], strLable[2]);
			if (objectExists(subnavButton2.navigationbutton, "isDisplayed", lngMinTimeOutInSeconds, strLable[2],
					"Navigation Button", strPageName, false)) {
				blnRecordFound = true;
			} else {
				blnRecordFound = false;
			}

		}

		if (blnRecordFound) {
			report.updateTestLog(strNavigation + " Navigation",
					strNavigation + " Navigation should not display in the nvaigation filter" + "<br>" + strNavigation
							+ " Navigation button is display in the navigation filter",
					Status.FAIL);
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strLable + " Navigation",
					strNavigation + " Navigation should not display in the navigation filter",
					strNavigation + " Navigation is display in the navigation filter", true);
		} else {
			report.updateTestLog(strNavigation + " Navigation",
					strNavigation + " Navigation should not display in the nvaigation filter" + "<br>" + strNavigation
							+ " Navigation is not display in the navigation filter",
					Status.PASS);
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strNavigation + " Navigation",
					strNavigation + " Navigation should not display in the navigation filter",
					strNavigation + " Navigation is not display in the navigation filter", true);

		}

	}

	public void filterNavigator() {
		String strPageName = "Service Now Home page";
		String strValue = dataTable.getExpectedResult("Navigation_Button");
		if (objectExists(Common.filterNavigation, "isDisplayed", lngMinTimeOutInSeconds, "Filter Navigator", "Search",
				strPageName, false)) {
			click(Common.filterNavigation, lngMinTimeOutInSeconds, "Filter Navigator", "Textbox", strPageName, false);
			sendkeys(Common.filterNavigation, lngMinTimeOutInSeconds, strValue, "Filter Navigator", strPageName);
			sendkeys(Common.filterNavigation, lngMinTimeOutInSeconds, Keys.ENTER, "Filter Navigator", strPageName);
			driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		}

	}

	public void refreshPage(String strValue, String strPageName) {
		driver.navigate().refresh();
}

	/**
	 * Function to Navigate to impersonate user and End impersonate user
	 * 
	 * @param No parameter
	 * @return No return value
	 */

	public void impersonateUserNavigation() {
	//	startTime("Function impersonateUserNavigation");
		
		String strPageName = "Service Now Home Page";
		String[] strInputParameter = dataTable.getExpectedResult("Impersonate User").split(strSemicolon);
		String strLabel = StringUtils.substringAfter(strInputParameter[0], strEqualto);
		String strValue = StringUtils.substringAfter(strInputParameter[1], strEqualto);
		impersonateDropdown(strLabel, strPageName);
		dropDown(strLabel, strValue, strPageName);
		//impersonateBackGroundPopup
		if(objectExists(Common.impersonateBackGroundPopup, "isDisplayed", timeOutInSeconds, "Close", "Text", strPageName, false)) {
			clickByJS(Common.impersonateBackGroundPopup, lngMinTimeOutInSeconds, "Close", "Lookup",
					strPageName, true);
		}
		driverUtil.waitUntilElementInVisible(Common.homePageLoadingIcon, "Home page Loading", "Loading Icon",
				strPageName);
		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
		String strUserNameText = getText(Common.userMenuLabel, lngPagetimeOutInSeconds, strValue, strPageName);
		if (strUserNameText.equals(strValue)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Impersonate User Name",
					"User should able to login into impersonate user as " + strValue,
					"Impersonate user is logged in successfully as " + strUserNameText, true);
			report.updateTestLog("Impersonate User Name", "User should able to login into the impersonate user as "
					+ strValue + "<br>" + "Impersonate User is logged in successfully as " + strUserNameText,
					Status.DONE);
		} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Impersonate User Name",
					"User should able to login into impersonate user as " + strValue,
					"Impersonate user is logged in successfully as " + strUserNameText, true);
			report.updateTestLog("Impersonate User Name", "User should able to login into the impersonate user as "
					+ strValue + "<br>" + "Impersonate User is logged in successfully as " + strUserNameText,
					Status.FAIL);
		}
		//endTime("Function impersonateUserNavigation");
	}

	public void impersonateDropdown(String strLabel, String strPageName) {
		//startTime("Function impersonateDropdown");
		click(Common.UserMenu, lngPagetimeOutInSeconds, "User Menu", "Button", strPageName, true);
		driverUtil.waitUntilElementInVisible(Common.tooltipUserMenu, "User Menu", "Tooltip", strPageName);
		driverUtil.waitUntilElementInVisible(Common.tooltipUserMenu, "User Menu", "Tooltip", strPageName);
		listValueSelect(Common.Usermenulist, "User Menu", lngPagetimeOutInSeconds, strLabel, "Dropdown", strPageName);
		driverUtil.waitUntilElementInVisible(Common.Usermenulist, "User Menu", "List", strPageName);
		//endTime("Function impersonateDropdown");
		
	}

	public void logout() {
		impersonateDropdown("Logout", "Service now");
		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, "Service now");
		ArrayList<String> arrlstLogOutTitles = new ArrayList<>(
				Arrays.asList(dataTable.getData("General_Data", "Logout_Page_Title").split("!")));
		boolean blnLoggedOut = false;
		for (String strLogOutTitle : arrlstLogOutTitles) {
			if (driverUtil.waituntilContainsPresentInPageTitle(lngMinTimeOutInSeconds, strLogOutTitle, false, false)) {
				blnLoggedOut = true;
				break;
			}
		}
		if (blnLoggedOut) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Logout",
					"User should be able to log out from " + properties.getProperty("ProjectName") + " application",
					"User has logged out from " + properties.getProperty("ProjectName") + " application", true);
		} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Logout",
					"User should be able to log out from " + properties.getProperty("ProjectName") + " application",
					"Logged Out Page title is not displayed as expected.<br>Expected : (anyone of) "
							+ arrlstLogOutTitles + "<br>Actual : " + driver.getTitle(),
					true);
		}
	}

	/**
	 * Function to select value in drop down
	 * 
	 * @param strValue Expected text to be passed to the drop down field
	 * @param strLabel The name of the element in which the expected text to be
	 *                 passed
	 * @param pageName Page in which the element exists
	 */

	public void dropDown(String strLabel, String strValue, String strPageName) {
		driverUtil.waitUntilElementVisible(new Common(strLabel).dropdown, lngPagetimeOutInSeconds, strLabel,
				strLabel + " PopUp", strPageName, false);
		click(new Common(strLabel).dropdown, lngPagetimeOutInSeconds, strLabel, "DropDown", strPageName, false);
		sendkeys(Common.searchTextbox, lngPagetimeOutInSeconds, strValue, strLabel, strPageName);
		driverUtil.waitUntilElementVisible(new Common(strValue).menu, lngPagetimeOutInSeconds, strLabel, "Menu",
				strPageName, false);
		driver.capture(strLabel);
		click(new Common(strValue).menu, lngPagetimeOutInSeconds, strValue, "Menu", strPageName, true);
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
	}
	
	

	/**
	 * Function to select value in combo box with in section
	 * 
	 * @param strSection Name of the combo box section
	 * @param strValue   Expected text to be passed to the drop down field
	 * @param strLabel   The name of the element in which the expected text to be
	 *                   passed
	 * @param pageName   Page in which the element exists
	 */
	public void comboBox(String strSection, String strLabel, String strValue, String strPageName) {
		driverUtil.waitUntilStalenessOfElement(new Common(strSection, strLabel).combobox, strPageName);
		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);	
		driverUtil.waitUntilElementInVisible(Common.tooltipUserMenu, strLabel, "Tooltip", strPageName);
		click(new Common(strSection, strLabel).combobox, lngMinTimeOutInSeconds, strLabel, "Combobox", strPageName,
				true);
		listValueSelect(Common.comboboxUserMenu, strLabel, lngMinTimeOutInSeconds, strValue, "Dropdown List",
				strPageName);

	}

	public void esccomboBox(String strLabel, String strValue, String strPageName) {
		driverUtil.waitUntilStalenessOfElement(new EscPortal(strLabel).escCombobox, strPageName);
		click(new EscPortal(strLabel).escCombobox, lngMinTimeOutInSeconds, strLabel, "Combobox", strPageName, true);
		listValueSelect(EscPortal.escListItem, strLabel, lngMinTimeOutInSeconds, strValue, "Dropdown List",
				strPageName);

	}

	public void clickListItem(String strLabel, String strValue, String strPageName) {
		driverUtil.waitUntilStalenessOfElement(Common.comboboxUserMenu, strPageName);
		listValueSelect(EscPortal.escListItem, strLabel,lngMinTimeOutInSeconds, strValue, "List Item", strPageName);
	}

	public void getWindowTitle() {
		
		System.out.println(driver.getTitle());
	}

	public void clickMenu(String strValue, String strPageName) {
		driverUtil.waitUntilStalenessOfElement(new Common(strValue).menu, strPageName);
		click(new Common(strValue).menu, lngPagetimeOutInSeconds, strValue, "Menu", strPageName, true);
	}
	
	
	
	public void verifyActivityLog(){
		String getCellValue = "";
		String strPageName = driver.getTitle();
		
		boolean blnFlag = false;
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);
//		tab("Activity Log", strPageName);
//		driverUtil.waitUntilElementVisible(Common.activityText, timeOutInSeconds, "Activities", "Label", strPageName, true);
		int count = 1;
		int subcount = 1;
		int intCount = 1;
		HashMap<String, String> logValue = new HashMap<>();
		String strLabel = "", strValue = "";
		try {
			for (String strItem : dataTable.getData("Parametrized_Checkpoints", "Activity_Log").split("!")) {
				for (String strSingleItem : strItem.split(";")) {
					if (StringUtils.substringBefore(strSingleItem, "=").toLowerCase().equals("element label")) {
						strLabel = StringUtils.substringAfter(strSingleItem, "=").trim();
					}
					if (StringUtils.substringBefore(strSingleItem, "=").toLowerCase().equals("element value")) {
						strValue = StringUtils.substringAfter(strSingleItem, "=").trim();
					}
				}
				logValue.put(strLabel, strValue);
				
				if(strValue.trim().length()>0) {
					List<WebElement> actualRows = driver.getWebDriver().findElements(new uimap.Common(strValue).cardRows);
					pagescroll(new uimap.Common(strValue).cardRows, strPageName);
					for(int i = 0;i < actualRows.size();i++) {
						if(count >= 1 ) {
							WebElement row = actualRows.get(i);
							List<WebElement> cells = row.findElements(By.xpath(".//span[contains(@class,'list-table-cell')]"));
							for(int j = 0;j < cells.size();j++) {
								getCellValue = cells.get(j).getText();
								if(subcount != 1  && !getCellValue.isEmpty()) {
									if(strValue.equalsIgnoreCase(getCellValue)) {
										//System.out.println(getCellValue);
										blnFlag = true;
										ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Activity Log",
												 "Actual - " +strLabel+ "  " +getCellValue+
												 "<br>Expected - " +strLabel+ "  " +strValue,
												 "<br> Actual - " +strLabel+ "  " +getCellValue+
												 "<br> Expected - " +strLabel+ "  " +strValue, true);
									}
								} else if (subcount != 1 && getCellValue.isEmpty()) {
									break;
								}
								if(getCellValue.contains("@gilead.com") || getCellValue.endsWith(".com")) {
									ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Activity Log",
											 "'To' field should not be empty","'To' field is not empty as expected", true);
								}
								subcount++;
								if(getCellValue.equalsIgnoreCase("SPARCTRAIN")) {
									intCount++;
								}
							}
							if(intCount>=3) {
								break;
							}
							count++;
						}
					}
				}
			}
		}catch (Exception e) {
			ALMFunctions.ThrowException("Verify Activity Log", "User should able to verify the " +strPageName,
					"Error: Unable to verify " + strPageName + "displayed in the " + strPageName + " tabe", true);
		}
		

		frameSwitchAndSwitchBack(uimap.Common.frame, "default", "Service Now Dashboard Table");
	}
	
	public void dateVerification() {
		String strSubLabel = dataTable.getData("Parametrized_Checkpoints", "Email_Label");
		String strPageName = dataTable.getData("Parametrized_Checkpoints", "PageName");
		
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
		tab("Activity Log", strPageName);
		driverUtil.waitUntilElementVisible(Common.activityText, timeOutInSeconds, "Activities", "Label", strPageName, true);
		if(objectExists(new uimap.Common(strSubLabel).cardDate, "isDisplayed", timeOutInSeconds, strSubLabel, "Text", strPageName, false)) {
			String strDate = getText(new uimap.Common(strSubLabel).cardDate, lngMinTimeOutInSeconds, "Email Sent Date", strPageName);
				if(!strDate.isEmpty()) {
					pagescroll(new uimap.Common(strSubLabel).cardDate, strPageName);
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Date & Time Verification", "Date & time should be displayed as expected ", "Actual Date with Time: " +strDate+ " is displayed for the Subject: " +strSubLabel, true);
				}
			} 
		frameSwitchAndSwitchBack(uimap.Common.frame, "default", "Service Now Dashboard Table");
	}
	
	public void verifyIncidentUpdate() {
		String strIncTitle = dataTable.getData("Parametrized_Checkpoints", "Incident_Title");
		String getCellValue = "";
		int count = 1;
		int subCount = 1;
		boolean blnFlag = false;
		String strPageName = dataTable.getData("Parametrized_Checkpoints", "PageName");
		String strSub = dataTable.getData("Parametrized_Checkpoints", "Email_Label");
		
		
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);
		tab("Activity Log", strPageName);
		driverUtil.waitUntilElementVisible(Common.activityText, timeOutInSeconds, "Activities", "Label", strPageName, true);
		if(objectExists(new uimap.Common(strSub).showEmailLink, "isDisplayed", timeOutInSeconds, strSub , "Add Icon", strPageName, false)) {
            pagescroll(new uimap.Common(strSub).showEmailLink, strPageName);
        	click(new uimap.Common(strSub).showEmailLink, timeOutInSeconds, strSub, "Add Icon", strPageName, false);
		}	
		frameSwitchAndSwitchBack(uimap.Common.cardFrame, "switchcardframe", "Service Now Dashboard Table");
		try {
			HashMap<String, String> logValue = new HashMap<>();
			String strLabel = "", strValue = "";
			for (String strItem : dataTable.getData("Parametrized_Checkpoints", "Incident_Update").split("!")) {
				for (String strSingleItem : strItem.split(";")) {
					if (StringUtils.substringBefore(strSingleItem, "=").toLowerCase().equals("element label")) {
						strLabel = StringUtils.substringAfter(strSingleItem, "=").trim();
					}
					if (StringUtils.substringBefore(strSingleItem, "=").toLowerCase().equals("element value")) {
						strValue = StringUtils.substringAfter(strSingleItem, "=").trim();
					}
				}
				logValue.put(strLabel, strValue);
				
				
				if(strValue.trim().length() > 0) {
					List<WebElement> actualRows = driver.getWebDriver().findElements(new uimap.Common(strIncTitle).cardTableRows);
					for(int i = 0; i< actualRows.size(); i++) {
						if(count>=1) {
							WebElement rows = actualRows.get(i);
							List<WebElement> cells = rows.findElements(By.xpath(".//td[not(@colspan)]"));
							for(int j = 0;j < cells.size();j++) {
								getCellValue = cells.get(j).getText();
								if (!getCellValue.isEmpty() && getCellValue.equalsIgnoreCase(strValue)) {
									blnFlag = true;
									System.out.println(getCellValue);
									ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Incident Update",
											 "Actual " +strLabel+ " : " +getCellValue+
											 "<br>Expected " +strLabel+ " : " +getCellValue,
											 "<br> Actual " +strLabel+ " : " +getCellValue+
											 "<br> Expected " +strLabel+ " : " +getCellValue, true);
									break;	
								}
//								subCount++;
//								if(blnFlag) {
//									break;
//								}
							}
//							if(blnFlag) {
//								break;
//							}
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			ALMFunctions.ThrowException("Verify Incident Update", "User should able to verify the " +strPageName,
					"Error: Unable to verify " + strPageName + "displayed in the " + strPageName + " tabe", true);
		}
		verifyReopenReason();
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
		
		if(objectExists(new uimap.Common(strSub).hideEmailLink, "isDisplayed", timeOutInSeconds, "Hide Email Address", "Link Text", strPageName, false)) {
			click(new uimap.Common(strSub).hideEmailLink, timeOutInSeconds, "Hide Email Address", "Link Text", strPageName, false);
		}
		frameSwitchAndSwitchBack(uimap.Common.frame, "default", "Service Now Dashboard Table");
		
	}
	
	
	public void verifyReopenReason() {
		String strTitle = "Reopened Incident";
		String strPageName = "Activity Log Tab";
		if(objectExists(new uimap.Common(strTitle).cardText, "isDisplayed", timeOutInSeconds, "Customer Comments", "Text", strPageName, false)) {
			String strGetText = driver.findElement(new uimap.Common(strTitle).cardText).getText();
			if(!strGetText.isEmpty()) {
				System.out.println(strGetText);
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Incident Reopen Comments", "Comments should be provided by the User",
						strGetText+ " is displayed in the " +strTitle+ " Page", true);
			}
		}
	}
	public void verifyActivityComments() {
		String strPageName = "Activity Log Verification";
		String strTitle = dataTable.getData("Parametrized_Checkpoints", "User Name");
		String strStatus = dataTable.getData("Parametrized_Checkpoints", "Status");
		int count=1;;
		int subcount=1;
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);
		tab("Activity Log", strPageName);
		driverUtil.waitUntilElementVisible(Common.activityText, timeOutInSeconds, "Activities", "Label", strPageName, true);
		
		try {
			HashMap<String, String> logValue = new HashMap<>();
			String strLabel = "", strValue = "";
			for (String strItem : dataTable.getData("Parametrized_Checkpoints", "Verify_Content").split("!")) {
				for (String strSingleItem : strItem.split(";")) {
					if (StringUtils.substringBefore(strSingleItem, "=").toLowerCase().equals("element label")) {
						strLabel = StringUtils.substringAfter(strSingleItem, "=").trim();
					}
					if (StringUtils.substringBefore(strSingleItem, "=").toLowerCase().equals("element value")) {
						strValue = StringUtils.substringAfter(strSingleItem, "=").trim();
					}
				}
				logValue.put(strLabel, strValue);
				
				if(strValue.trim().length() > 0) {
					frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);
					List<WebElement> listElement = driver.getWebDriver().findElements(new uimap.Common(strTitle, strStatus).comments);
					for(int i = 0; i< listElement.size(); i++) {
						WebElement rows = listElement.get(i);
						List<WebElement> cells = rows.findElements(By.xpath(".//span"));
							for(int j = 0;j < cells.size();j++) {
								WebElement strGetValue = cells.get(j);
								String strActValue = strGetValue.getText();
								if (!strActValue.isEmpty()) {
									if(strActValue.equalsIgnoreCase(strValue)) {
										System.out.println(strActValue);
										ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Activity Log Comments",
												 "Actual " +strLabel+ " : " +strActValue+
												 "<br>Expected " +strLabel+ " : " +strActValue,
												 "<br> Actual " +strLabel+ " : " +strActValue+
												 "<br> Expected " +strLabel+ " : " +strActValue, true);
										break;
									} else if(strActValue.equalsIgnoreCase("AssignedwasOpen")) {
										String strSubValue = strActValue.substring(0,8)+ " " + strActValue.substring(8,11)+ " " +strActValue.substring(11,15);
										System.out.println(strSubValue);
										ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Activity Log Comments",
												 "Actual " +strLabel+ " : " +strSubValue+
												 "<br>Expected " +strLabel+ " : " +strSubValue,
												 "<br> Actual " +strLabel+ " : " +strSubValue+
												 "<br> Expected " +strLabel+ " : " +strSubValue, true);
									}
								}
//								subCount++;
							}
//							count++;
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		frameSwitchAndSwitchBack(uimap.Common.frame, "default", strPageName);
		}
		
		public void verifyCustomerNotes() {
			String strPageName = dataTable.getData("Parametrized_Checkpoints", "PageName");
			String strLabel = dataTable.getData("Parametrized_Checkpoints", "Customer_Comments");
			frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);
			verifyNotes(strLabel, strPageName);
			frameSwitchAndSwitchBack(uimap.Common.frame, "default", strPageName);
		}
		
		public void verifyNotes(String strLabel, String strPageName) {
			if(objectExists(new uimap.Common("Customer Comms").userComments, "isDisplayed", timeOutInSeconds, strLabel, "Text", strPageName, false)) {
				WebElement strComment = driver.findElement(new uimap.Common("Customer Comms").userComments);
				String strGetCom = strComment.getText();
				pagescroll(strComment, strPageName);
				ALMFunctions.Screenshot();
				if(strGetCom.equalsIgnoreCase(strLabel)) {
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify "+strLabel, strLabel+" should be displayed as expected",
							strGetCom+ " is the comment displayed in the Customer Comments Section" , true);
				} else {
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify "+strLabel, strLabel+" should be displayed as expected",
							"Customer Comments is not displayed as expected", true);
				}
			
				if(objectExists(new uimap.Common(strLabel).cardDate, "isDisplayed", timeOutInSeconds, strLabel, strLabel, strPageName, false)) {
					String strDate = getText(new uimap.Common(strLabel).cardDate, lngMinTimeOutInSeconds, "Email Sent Date", strPageName);
					if(!strDate.isEmpty()) {
						pagescroll(new uimap.Common(strLabel).cardDate, strPageName);
						ALMFunctions.UpdateReportLogAndALMForPassStatus("Date & Time Verification", "Date & time should be displayed as expected ", "Actual Date with Time: " +strDate+ " is displayed successfully as expected for Customer Comments", true);
					}
				}
			}
		}
		
	
	/**
	 * Function to select value in combo box
	 * 
	 * @param strValue Expected text to be passed to the drop down field
	 * @param strLabel The name of the element in which the expected text to be
	 *                 passed
	 * @param pageName Page in which the element exists
	 */

	public void comboBoxLookup(String strLabel, String strValue, String strPageName) {
		String[] strinputPara = strLabel.split("!");
		String strComboboxlable = strinputPara[0];
		String strSearchBoxlable = strinputPara[1];
		String strColumnName = strinputPara[2];
		windowName.put("Window1", driver.getWindowHandle());
		if (objectExists(new Common(strComboboxlable).comboLookup, "isDisplayed", timeOutInSeconds, strComboboxlable,
				"Lookup", strPageName, false)) {
			clickByJS(new Common(strComboboxlable).comboLookup, lngMinTimeOutInSeconds, strComboboxlable, "Lookup",
					strPageName, true);
		}
		manageAndSwitchNewWindow();
		searchAndSelectRecord(strSearchBoxlable, strColumnName, strValue, strPageName);
		findRecordAndSelect(strColumnName, strValue, strPageName);
		manageAndSwitchNewWindow();
		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
		if(objectExists(new Common(strinputPara[0]).readonlyTextbox, "isDisplayed", timeOutInSeconds, strinputPara[0], "Value", strPageName, false)) {
			String strGetEnteredValue = getAttributeValue(new Common(strinputPara[0]).readonlyTextbox,
					lngPagetimeOutInSeconds, "value", strComboboxlable, strPageName);
			if (strGetEnteredValue.equals(strValue)) {
				report.updateTestLog(
						strComboboxlable + " Combobox", "User should select the " + strValue + " value from the list <br>"
								+ "<br>" + "Value selected by the user is " + strGetEnteredValue + " from the list",
						Status.PASS);
			}
		}	
	}

	public void expandTreeView(String strLabel, String strValue, String strPageName) {

		String[] strTreeViewValue = strValue.split("!");
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);
		String strLastValue = strTreeViewValue[strTreeViewValue.length - 1];
		String strGetEnteredValue = getAttributeValue(new Common(strLabel).readonlyTextbox, lngPagetimeOutInSeconds,
				"value", strLabel, strPageName);
		if (!strGetEnteredValue.equals(strLastValue)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Tree Node Expansion",
					strLastValue + " value should be display in the " + strLabel + " tree node ",
					"Alert- Value in " + strLabel + "", true);

			windowName.put("Window1", driver.getWindowHandle());
			clickByJS(new Common(strLabel).comboLookup, lngMinTimeOutInSeconds, strLabel, "Lookup", strPageName, true);
			manageAndSwitchNewWindow();
			String strExpand = "";

			for (int i = 0; i < strTreeViewValue.length; i++) {
				uimap.Common expand = new Common(strTreeViewValue[i]);
				if (objectExists(expand.expandView, "isDisplayed", lngPagetimeOutInSeconds, strTreeViewValue[i],
						"Tree View", strPageName, false)) {

					if (i < strTreeViewValue.length - 1) {
						strExpand = getAttributeValue(expand.expandView, lngMinTimeOutInSeconds, "src",
								strTreeViewValue[i], strPageName);
						if (strExpand.contains("minus")) {
							report.updateTestLog("Click", strTreeViewValue[i] + " is already clicked in the tree view",
									Status.PASS);
						} else {

							click(expand.expandView, lngMinTimeOutInSeconds, strTreeViewValue[i], "Expand button",
									strPageName, true);
						}
					} else {
						driver.capture(strTreeViewValue[i]);
						WebElement eleClick = driver.getWebDriver().findElement(new Common(strTreeViewValue[i]).link);
						eleClick.click();
						report.updateTestLog("Click",
								"Link" + " - " + strTreeViewValue[i] + " in " + strPageName + " is clicked",
								Status.DONE);
						break;
					}

				} else {

					ALMFunctions.ThrowException("Tree Node Expansion",
							"User should be able to expand tree node " + strTreeViewValue[i],
							"Below exception is thrown while " + "trying to expand tree node " + strTreeViewValue[i],
							true);
				}

			}
			manageAndSwitchNewWindow();
			driver.switchTo().frame(driver.findElement(Common.frame));
		} else {

			ALMFunctions.UpdateReportLogAndALMForPassStatus(strLabel,
					strLabel + " combobox should have value as : " + strLastValue,
					strLabel + " combobox is already selected as " + strGetEnteredValue, true);
		}
	}

	/**
	 * Function to select value in combo box
	 * 
	 * @param strValue Expected text to be passed to the drop down field
	 * @param strLabel The name of the element in which the expected text to be
	 *                 passed
	 * @param pageName Page in which the element exists
	 */

	public void unlockComboBox(String strLabel, String strValue, String strPageName) {
		String[] strinputPara = strLabel.split("!");
		String strComboboxlable = strinputPara[0];
		String strSearchBoxlable = strinputPara[1];
		String strColumnName = strinputPara[2];
		windowName.put("Window1", driver.getWindowHandle());
		clickByJS(new Common(strComboboxlable).unlockButton, lngMinTimeOutInSeconds, strComboboxlable, "Unlock Button",
				strPageName, true);
		driver.capture(strPageName);
		clickByJS(new Common(strComboboxlable).comboLookup, lngMinTimeOutInSeconds, strComboboxlable, "Lookup",
				strPageName, true);
		manageAndSwitchNewWindow();
		searchAndSelectRecord(strSearchBoxlable, strColumnName, strValue, strPageName);
		findRecordAndSelect(strColumnName, strValue, strPageName);
		// listValueSelect(Common.lookupItemList, strLabel, strValue, "List
		// Item", strPageName);
		manageAndSwitchNewWindow();
		driver.switchTo().frame(driver.findElement(Common.frame));
	}

	/**
	 * Function to select value in dialog combo box
	 * 
	 * @param strWindowname Name of the Dialog window
	 * @param strValue      Expected text to be passed to the drop down list field
	 * @param strLabel      The name of the element
	 * @param pageName      Page in which the element exists
	 */

	public void dialogCombobox(String strWindowName, String strLabel, String strValue, String strPageName) {
		if(objectExists(new Common(strLabel).comboLookup, "isDisplayed", timeOutInSeconds, strValue, "Lookup", strPageName,
				false)) {
			clickByJS(new Common(strLabel).comboLookup, lngMinTimeOutInSeconds, strLabel, "Lookup", strPageName, true);
			listValueSelect(new Common(strWindowName).dialogComboListItem, strLabel, lngMinTimeOutInSeconds, strValue,
					"List", strPageName);
		} else if(objectExists(new Common(strLabel).dialogComboBox, "isDisplayed", timeOutInSeconds, strValue, "Lookup", strPageName,
				false)) {
			click(new Common(strLabel).dialogComboBox, lngMinTimeOutInSeconds, strLabel, "Lookup", strPageName, true);
			sendkeys(new Common(strLabel).dialogComboBox, timeOutInSeconds, strValue, strLabel, strPageName);
			driverUtil.waitUntilElementVisible(new Common(strLabel).dialogComboBox, lngMinTimeOutInSeconds, strLabel, "ComboBox", strPageName, true);
			click(Common.highlightedList, lngMinTimeOutInSeconds, strLabel, "Combobox", strPageName, true);
			
		}
	}

	/**
	 * Method to click dialog button
	 * 
	 * @param strWindowname, Name of the Dialog window
	 * @param strValue, value to click button
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void dialogbutton(String strWindowName, String strValue, String strPageName) {
		uimap.Common button = new uimap.Common(strWindowName, strValue);
		driver.capture(strPageName);
		if (objectExists(button.dialogButton, "isDisplayed", timeOutInSeconds, strValue, "Popup Button", strPageName,false)) {
			clickByJS(button.dialogButton, lngMinTimeOutInSeconds, strValue, "Popup Button", strPageName, true);
			driverUtil.waitUntilStalenessOfElement(button.button, lngMinTimeOutInSeconds, strPageName);
		} else if (objectExists(new uimap.Common(strValue).dialogBtn, "isDisplayed", timeOutInSeconds, strValue,"Dialog Button", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(new uimap.Common(strValue).dialogBtn, lngMinTimeOutInSeconds, strPageName);
			pagescroll(new uimap.Common(strValue).dialogBtn, strPageName);
			clickByJS(new uimap.Common(strValue).dialogBtn, lngMinTimeOutInSeconds, strValue, "Dialog Button",
					strPageName, true);
			driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
//			driverUtil.waitUntilElementInVisible(new uimap.Common(strValue).dialogBtn, strValue, "Button", strPageName);
		}
	}
	
	public void dialogCheckBox(String strLabel, String strPageName) {
		if(objectExists(Common.dialogPopup, "isDisplayed", timeOutInSeconds, strLabel, "Check box", strPageName, false)) {
			click(new uimap.Common(strLabel).dialogCheckBox, timeOutInSeconds, strLabel, "Check box", strPageName, true);
		}
//		click(Common.closeBtn, timeOutInSeconds, strLabel, "Check box", strPageName, false);
	}
	
	public void verifyAutoPopulatedValue(String strLabel, String strValue, String strPageName) {
		String strGetEnteredValue = getAttributeValue(new Common(strLabel).readonlyTextbox,
				lngPagetimeOutInSeconds, "value", strLabel, strPageName);
		if (strGetEnteredValue.equals(strValue)) {
			report.updateTestLog(
					strLabel + " Text Field", strValue+ " should be Auto popualted as expected <br>"
							+ "<br>" +strLabel+  " is Auto populated with the Value" + strGetEnteredValue + " in " +strPageName,
					Status.PASS);
		}
	}
	
	/**
	 * 
	 * @param strValue Value to be provided to navigate back
	 * @param strPageName Page Name in which the control is available
	 */
	public void navigateBack(String strValue, String strPageName) {
		if (objectExists(Common.backButton, "isDisplayed", timeOutInSeconds, strValue, "Popup Button", strPageName,
				false)) {
			clickByJS(Common.backButton, lngMinTimeOutInSeconds, strValue, "Popup Button", strPageName, true);
		}
	}
	
	public void verifyAttachment(String strValue, String strPageName) {
		if (objectExists(new uimap.Common(strValue).attachmentLink, "isDisplayed", timeOutInSeconds, strValue, "Popup Button", strPageName,
				false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify "+strPageName, "Attachment should be displayed in the "+strPageName,
					"Attachment is displayed successfully in "+strPageName, true);
		}
	}
	
	public void attachFile(String strLabel, String strValue, String strPageName) {
		String strBaseDir = System.getProperty("user.dir");
		String strFilePath = strBaseDir + "\\Datatables\\Attachment\\" + dataTable.getData("Parametrized_Checkpoints", "Attachment_Name");
		if(objectExists(Common.attachmentIcon, "isDisplayed", timeOutInSeconds, strLabel, "File Upload", strPageName, false)) {
			click(Common.attachmentIcon, timeOutInSeconds, strSemicolon, strExclamation, strEqualto, true);
		}
		try {
			Thread.sleep(2000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			driver.capture(strPageName);
		if(objectExists(Common.choosefileBtn, "isDisplayed", timeOutInSeconds, strLabel, "File Upload", strPageName, false)) {
			click(Common.choosefileBtn, lngMinTimeOutInSeconds, strLabel, "Button", strPageName, true);
		}
		UploadFileAutoIT(strFilePath);
		try {
			Thread.sleep(2000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			driver.capture(strPageName);
		if(objectExists(Common.closeBtn, "isDisplayed", timeOutInSeconds, strLabel, "Close", strPageName, false)) {
			click(Common.closeBtn, lngMinTimeOutInSeconds, strLabel, "Button", strPageName, true);
			driverUtil.waitUntilElementInVisible(Common.closeBtn, strLabel, "Button", strPageName);
		}
	}
	
	public void UploadFileAutoIT(String strFilePath){
		String strBaseDir = System.getProperty("user.dir")+Util.getFileSeparator();
		if(BrowserName.equalsIgnoreCase("chrome")){
			executeScript(strBaseDir+"autoIT"+Util.getFileSeparator()+"File Upload.exe", "Open",strFilePath);
			
		} else{
			executeScript(strBaseDir+"autoIT"+Util.getFileSeparator()+"File Upload.exe", "Choose File to Upload",strFilePath);
		}
	}

	public void UploadFileAutoIT(String strLabel,String strFilePath,String strPageName) {
		String strBaseDir = System.getProperty("user.dir") + Util.getFileSeparator();
		if(objectExists(Common.attachment, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "File Upload", strPageName, false)) {
		click(Common.attachment, lngMinTimeOutInSeconds, "Attachment", "strLabel", "link", true);
		}

		if(objectExists(Common.chooseflButton, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "File Upload", strPageName, false)) {
		WebElement web=driver.findElement(By.xpath("//button[@id='close-messages-btn']"));
		//web.click();
		web.sendKeys(Keys.chord(Keys.TAB,Keys.ENTER));
		//clickByJS(Common.chooseflButton, lngMinTimeOutInSeconds, strLabel, "Button", strPageName, true);

		}

		if (BrowserName.equalsIgnoreCase("chrome")) {
		executeAutoITScript(strBaseDir + "autoIT" + Util.getFileSeparator() + "File Upload.exe", "Open",
		strFilePath);
		} else {
		executeAutoITScript(strBaseDir + "autoIT" + Util.getFileSeparator() + "File Upload.exe",
		"Choose File to Upload", strFilePath);
		}
		
		driver.capture(strPageName);
		if(objectExists(Common.OKButton, "isDisplayed", timeOutInSeconds,strLabel, "Close", strPageName, false)) {
			click(Common.OKButton, lngMinTimeOutInSeconds, strLabel, "Button", strPageName, true);
		//driverUtil.waitUntilElementInVisible(Common.OKButton, strElementName, "Button", strPageName);
		}
		}
	
	public void executeAutoITScript(String strFilePath, String... arrArguments) {
		String[] cmdArr = new String[arrArguments.length + 1];
		cmdArr[0] = strFilePath;
		for (int i = 0; i < arrArguments.length; i++) {
		cmdArr[i + 1] = arrArguments[i];
		}
		try {
		Process p = Runtime.getRuntime().exec(cmdArr);
		p.waitFor();
		p.destroy();
		} catch (Exception e) {
		ALMFunctions.ThrowException("AutoIT Script", "Should be able to execute AutoIT script",
		"Below exception is thrown while executing the AutoIT" + " script.<br><br>"
		+ e.getLocalizedMessage(),
		true);
		}
		}


	/**
	 * Method to click button
	 * 
	 * @param strValue, value to click button
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void button(String strButtonLabel, String strPageName)

	{
		By button = null;
		String strPageView = getCurrentPageView();
		if(strButtonLabel.equalsIgnoreCase("closedialogbutton")) {
			click(Common.dialogCloseButton, lngPagetimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
			driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
		}
		else {
			switch (strPageView) {
				case "SPARC Dev":
					button = new uimap.Common(strButtonLabel).button;
					break;
				case "ESC Portal":
					button = new uimap.EscPortal(strButtonLabel).escbutton;
					break;
				default:
					ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
							"Unhandled - View - " + strPageView, false);
					break;
				}
	
			driver.capture(strPageName);
				if(objectExists(new uimap.Common(strButtonLabel).tabNavButton, "isDisplayed", timeOutInSeconds, strButtonLabel, "Button", strPageName, false)) {
						clickByJS(new uimap.Common(strButtonLabel).tabNavButton, timeOutInSeconds, strButtonLabel, "Button", strPageName, true);
					} else {
						driverUtil.waitFor(5000, strButtonLabel, strButtonLabel, strPageName);
						clickByJS(button, lngPagetimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
						driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
					}
	}
	}
	
	public void tabButton(String strButtonLabel, String strPageName)

	{
		By button = null;
		button = new uimap.Common(strButtonLabel).TabButton;
		
		pagescroll(button, strPageName);
		driver.capture(strPageName);
		click(button, lngPagetimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
		driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
	}

	public void button()

	{
		String strPageName = dataTable.getExpectedResult("PageName");
		String strButtonLabel = dataTable.getExpectedResult("Button_Label");
		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
		driver.capture(strPageName);
		clickByJS(new uimap.Common(strButtonLabel).button, lngMinTimeOutInSeconds, strButtonLabel, "Button",
				strPageName, true);
		driverUtil.waitUntilStalenessOfElement(new uimap.Common(strButtonLabel).button, lngMinTimeOutInSeconds,
				strPageName);
	}
	/**
	 * 
	 * @param strLabel The name of the text box
	 * @param strValue Value to click button
	 * @param strPageName Page Name in which the control is available
	 */
	public void imgButton(String strLabel, String strValue, String strPageName) {
		if (objectExists(new uimap.Common(strLabel, strValue).arrowBtn, "isDisplayed", timeOutInSeconds, strValue,
				"Button", strPageName, false)) {
			click(new uimap.Common(strLabel, strValue).arrowBtn, timeOutInSeconds, strValue, "Button", strPageName,
					true);
			driver.capture(strPageName);
		} else if (objectExists(new uimap.Common(strLabel, strValue).imgIcon, "isDisplayed", timeOutInSeconds, strLabel,
				"Button", strPageName, false)) {
			click(new uimap.Common(strLabel, strValue).imgIcon, timeOutInSeconds, strLabel, "Button", strPageName,
					true);
//			if(objectExists(new uimap.Common("Open Record").dialogBtn, "isDisplayed", timeOutInSeconds, strLabel, "Button", strPageName, false)) {
//				ALMFunctions.UpdateReportLogAndALMForPassStatus(strValue+ "for" +strLabel, strValue+ " should be selected successfully", strValue+ "is selected successfully", true);
//			}
			if (objectExists(new uimap.Common(strValue).dialogCard, "isDisplayed", timeOutInSeconds, strValue,
					"Button", strPageName, false)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify " +strValue, strValue+ "should be displayed successfully"
						,strValue+ " is displayed successfully", true);
			}
		} else if (objectExists(new uimap.Common(strLabel).previewIcon, "isDisplayed", timeOutInSeconds, strLabel,
				"Button", strPageName, false)) {
			click(new uimap.Common(strLabel).previewIcon, timeOutInSeconds, strLabel, "Button", strPageName,
					true);
			if (objectExists(new uimap.Common(strValue).dialogCard, "isDisplayed", timeOutInSeconds, strValue,
					"Button", strPageName, false)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify " +strValue, strValue+ "should be displayed successfully"
						,strValue+ " is displayed successfully", true);
			}
			
		}
		else if (objectExists(new uimap.Common(strLabel).crossIcon, "isDisplayed", timeOutInSeconds, strLabel,
                "Button", strPageName, false)) {
          click(new uimap.Common(strLabel).crossIcon, timeOutInSeconds, strLabel, "Button", strPageName, true);
          report.updateTestLog("To click on cross icon", "Click icon is clicked", Status.PASS);
   }

	}

	public void tabButton()

	{
		By button = null;
		String strPageName = dataTable.getExpectedResult("PageName");
		String strButtonLabel = dataTable.getExpectedResult("Button_Label");
		try {
		
		button = new uimap.Common(strButtonLabel).TabButton;
		driver.capture(strPageName);
		click(button, lngPagetimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
		ALMFunctions.UpdateReportLogAndALMForPassStatus("tab Selection",
				"User should able to click the " +strButtonLabel+ " tab", 
				 strButtonLabel+ " tab is clicked in the " +strPageName+" page", true);
		driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
		
		} catch (Exception e) {
			ALMFunctions.ThrowException("tab Selection", "User should able to click the " + strButtonLabel + " tab",
					"Error: No Such " + strButtonLabel + " tab is displayed in the " + strPageName + " page", true);
		}
	}
	
	public void cmtbutton(String strButtonLabel, String strPageName) {
		driverUtil.waitUntilStalenessOfElement(new uimap.Common(strButtonLabel).CommentsButton, lngMinTimeOutInSeconds,strPageName);
		clickByJS(new Common(strButtonLabel).CommentsButton, lngMinTimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
		driverUtil.waitUntilStalenessOfElement(new uimap.Common(strButtonLabel).CommentsButton, lngMinTimeOutInSeconds,strPageName);
	}

	/**
	 * Method to Click Tab
	 * 
	 * @param strValue, value to click tab
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void tab(String strTab, String strPageName) {
		Common objTab = new Common(strTab);
		///pagescroll(objTab.tab, strPageName);
		if (objectExists(objTab.tab, "isDisplayed", lngPagetimeOutInSeconds, strTab, "Tab", strPageName, false)) {
			pagescroll(objTab.tab, strPageName);
			String strAttriValue = getAttributeValue(objTab.tab, lngPagetimeOutInSeconds, "class", strTab, strPageName);

			if (strAttriValue.contains("active")) {
				report.updateTestLog(strTab, "Tab - '" + strTab + "' is already selected in " + strPageName,
						Status.DONE);
			} else {
				clickByJS(objTab.tab, lngPagetimeOutInSeconds, strTab, "Tab", strPageName, true);
				driverUtil.waitUntilStalenessOfElement(objTab.tab, strPageName);
			}
		} else if (objectExists(objTab.tabText, "isDisplayed", lngPagetimeOutInSeconds, strTab, "Tab", strPageName, false)){
			pagescroll(objTab.tabText, strPageName);
			clickByJS(objTab.tabText, lngPagetimeOutInSeconds, strTab, "Tab", strPageName, true);
			driverUtil.waitUntilStalenessOfElement(objTab.tab, strPageName);
			
		} else {
			ALMFunctions.ThrowException(strTab, "Tab - " + strTab + " should be displayed in " + strPageName,
					"Error - Tab - " + strTab + " is not available in " + strPageName, true);
		}
	}

	public void tab() {

		String strPageName = dataTable.getExpectedResult("PageName");
		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
		String strTabName = dataTable.getExpectedResult("Tab_Name");
		Common objTab = new Common(strTabName);
		if (objectExists(objTab.tab, "isDisplayed", timeOutInSeconds, strTabName, "Tab", strPageName, false)) {
			String strAttriValue = getAttributeValue(objTab.tab, lngPagetimeOutInSeconds, "class", strTabName,
					strPageName);

			if (strAttriValue.contains("active")) {
				report.updateTestLog(strTabName, "Tab - '" + strTabName + "' is already selected in " + strPageName,
						Status.DONE);
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Tab Selection",
						"User should able to click the " +strTabName+ " tab", 
						strTabName+ " tab is clicked in the " +strPageName+" page", true);
			} else {
				clickByJS(objTab.tab, lngPagetimeOutInSeconds, strTabName, "Tab", strPageName, true);
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Tab Selection",
						"User should able to click the " +strTabName+ " tab", 
						strTabName+ " tab is clicked in the " +strPageName+" page", true);
			}
		} else {
			ALMFunctions.ThrowException("Tab Selection", "Tab - " + strTabName + " should display in the" + strPageName,
					"Error - Tab - " + strTabName + " is not available in the " + strPageName, true);
		}
	}

	public void configuration()

	{
		frameSwitchAndSwitchBack(Common.frame, "switchframe", "Configuration");
		click(Common.ActionButton, lngMinTimeOutInSeconds, "Action Bar", "Button", "Configuration", true);

	}
	
	/**
	* Method to select a radiobutton in a dialog  box
	* 
	 * @param locator, By object of the control
	* @param strFieldName, The name of the text box
	* @param strValue, value to be entered in a text box
	* @param strPageName, Page Name in which the control is available
	* @return No return value
	*/
	public void dialogSelectRadioButton(String strFieldName, String strValueToSelect, String strPageName) {
	        
	        try {
	                if (strValueToSelect.equalsIgnoreCase("Select")) {
	                        
	                        uimap.Common radio = new uimap.Common(strFieldName);
	                        pagescroll(radio.dialogRadioButton, strPageName);
	                        clickByJS(radio.dialogRadioButton, timeOutInSeconds, strValueToSelect + " in " + strFieldName, "Radio Button", strPageName, false);
	                        driverUtil.waitFor(lngMinTimeOutInSeconds, strFieldName, strFieldName, strPageName);
	                        
	                }
	                else {
	                                
	                        uimap.Common radio = new uimap.Common(strFieldName, strValueToSelect);
//	                        pagescroll(radio.dialogRadioButton, strPageName);
	                        clickByJS(radio.dialogRadioButton, timeOutInSeconds, strValueToSelect + " in " + strFieldName, "Radio Button", strPageName, false);
	                        driverUtil.waitFor(lngMinTimeOutInSeconds, strFieldName, strFieldName, strPageName);
	                        
	                }
	                report.updateTestLog(strPageName+ " Page", strValueToSelect+ " is the value selected for the question "+strFieldName, Status.DONE);

	                
	        }
	        catch(Exception e) {
	                throw new FrameworkException(strFieldName + " is not available in " + strPageName);
	        }
	}



	/**
	 * Method to click Link
	 * 
	 * @param strValue, value to click link
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void link(String strLink, String strPageName) {
		Common objLink = new Common(strLink);
		if (objectExists(objLink.link, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName, false)) {
			pagescroll(objLink.link, strPageName);
			click(objLink.link, lngPagetimeOutInSeconds, strLink, "Link", strPageName, true);
//			driverUtil.waitUntilElementInVisible(objLink.link, strLink, "Link", strPageName);
			//driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		} else if (objectExists(objLink.linkIcon, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName,
				false)) {
			click(objLink.linkIcon, timeOutInSeconds, strLink, "Link", strPageName, true);
		} else if(objectExists(Common.notificationLinkText, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName,false)){
			click(Common.notificationLinkText, timeOutInSeconds, strLink, "Link", strPageName, true);
		} else {
			ALMFunctions.ThrowException(strLink, "Link - " + strLink + " should be displayed in " + strPageName,
					"Error - Link - " + strLink + " is not available in " + strPageName, true);
		}
	}

	
	public void link1(String strLink, String strPageName) {
		Common objLink = new Common(strLink);
		if (objectExists(objLink.link, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName, false)) {
			click(objLink.link, lngPagetimeOutInSeconds, strLink, "Link", strPageName, true);
//			driverUtil.waitUntilElementInVisible(objLink.link, strLink, "Link", strPageName);
			driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		} else if (objectExists(objLink.linkIcon, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName,
				false)) {
			click(objLink.linkIcon, timeOutInSeconds, strLink, "Link", strPageName, true);
		} else if(objectExists(Common.notificationLinkText, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName,false)){
			click(Common.notificationLinkText, timeOutInSeconds, strLink, "Link", strPageName, true);
		} else {
			ALMFunctions.ThrowException(strLink, "Link - "  + " should be displayed in " + strPageName,
					"Error - Link - " + strLink + " is not available in " + strPageName, true);
		}
	}
public void navLink(String strLink, String strPageName) {
		Common objLink = new Common(strLink);
		if (objectExists(objLink.navLink, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName, false)) {
			click(objLink.navLink, lngPagetimeOutInSeconds, strLink, "Link", strPageName, true);
		}
	}
	
	public void verifyLinkExist(String strLink, String strPageName) {
		Common objLink = new Common(strLink);
		if (objectExists(objLink.link, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName, false)) {
			pagescroll(objLink.link, strPageName);
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify " +strLink+ " Exist",
					"Link should be displayed as expected in " +strPageName, strLink+ " is displayed in "+strPageName, true);
		} else if(!objectExists(objLink.link, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName, false)){
			report.updateTestLog("Verify " +strLink+ " Exist ", strLink+" should not be displayed in the " +strPageName, Status.DONE);
		}
	}
	
	public void getLinkText(String strLabel, String strValue, String strPageName) {
		
		String strLinkText = getText(Common.notificationLinkText, timeOutInSeconds, strLabel, strPageName);
		if(!strLinkText.isEmpty()) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify "+strLabel, "Link should be displayed successfully",
					strLinkText+ "is the " +strLabel+ " displayed in the " +strPageName, true);
			
			FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
		    if(inputDataFilelock!=null){
		        synchronized (CommonFunctions.class) {
		        	dataTable.putData("Parametrized_Checkpoints", strValue, strLinkText);
		        }   
		        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
		   
		}
			
		}
	
	}
	
	public void getAlertMsg(String strValue, String strPageName) {
		String strAlertText = getText(Common.alertMsg, timeOutInSeconds, strValue, strPageName);
		if(!strAlertText.isEmpty()) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify "+strValue, "Alert should be displayed successfully",
					"' "+strAlertText+ " ' is the "+strValue+ " displayed in the " +strPageName, true);
			
		}
	}
	
	public void getPopupAlert(String strValue, String strPageName) {
		String strAlert =  getAlertText(lngMinTimeOutInSeconds);
		if(!strAlert.isEmpty()) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify "+strValue, "Alert should be displayed successfully",
					"' "+strAlert+ " ' is the "+strValue+ " displayed in the " +strPageName, true);
			
		}
	}
	public void verifyKnownError(String strLabel, String strValue, String strPageName) {
		String strExpNum = dataTable.getData("Parametrized_Checkpoints", "Task Number");
//		String strVerifyTabExist = dataTable.getData("Parametrized_Checkpoints", "Tab Exist");
		int count = 1;
//		frameSwitchAndSwitchBack(Common.frame, "default", strPageName);
		ArrayList<String> arrListAllTabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(arrListAllTabs.get(1));
		ArrayList<String> arrListTabsTitle = new ArrayList<>(Arrays.asList(dataTable.getData("Parametrized_Checkpoints", "Page_Title").split("!")));
		String strPageTitle = driver.getWebDriver().getTitle();
		
		for(String strTabTitle : arrListTabsTitle) {
			if(strPageTitle.equalsIgnoreCase(strTabTitle)) {
				click(Common.containerSearch, timeOutInSeconds, strLabel, "Search Box", strPageTitle, false); //label - search
				clear(Common.containerSearch, timeOutInSeconds, strLabel,strPageTitle);
				sendkeys(Common.containerSearch, timeOutInSeconds, strValue, strLabel, strPageTitle);
				do {
					click(Common.submitBtn, timeOutInSeconds, strLabel, "Search Button", strPageTitle, true);
					driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
					count++;
				} while (count <=1 && driverUtil.waitUntilElementVisible(Common.errorLinkText, lngMinTimeOutInSeconds, strLabel, "Link", strPageName, false));
				click(Common.errorLinkText, timeOutInSeconds, strLabel, "Link Text", strPageTitle, true);
				driverUtil.waitUntilElementVisible(Common.readonlyNumber, lngMinTimeOutInSeconds, strLabel, "Link", strPageName, false);
				String strNum = driver.findElement(Common.readonlyNumber).getText();
				if(strNum.equalsIgnoreCase(strExpNum)) {
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Known Error is Displayed",
							"Known Error Number should be verified as expected", 
							strNum+ "is displayed in " +strPageTitle, true);
				}
			}
		}
//		 if(strVerifyTabExist.equalsIgnoreCase("Yes")) {
//			driver.close();
//		} else 
//		if(objectExists(new uimap.Common("Attach to Incident").button, "isDisplayed", timeOutInSeconds, strLabel, "Button", strPageName, false)) {
//			click(new uimap.Common("Attach to Incident").button, timeOutInSeconds, strLabel, "Button", strPageName, true);
//		}
//		driver.close();
//		driver.switchTo().window(arrListAllTabs.get(0));
//		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
	}
		
	
	public void closeTab(String strValue, String strPageName) {
		ArrayList<String> arrListAllTabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(arrListAllTabs.get(1));
		driver.close();
		driver.switchTo().window(arrListAllTabs.get(0));
		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
	}
	
	public void switchTab(String strValue, String strPageName) {
		ArrayList<String> arrListAllTabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(arrListAllTabs.get(0));
		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
	}	

	public void selectDropdown(String strLabel, String strValue, String strPageName) {
		if(strLabel.contains("Form1"))
		{
			String[] fieldName = strLabel.split("!");
			strLabel = fieldName[0];
			if (objectExists(new Common(strLabel).formdropdown, "isEnabled", lngMinTimeOutInSeconds, strLabel, "DropDown",
					strPageName, false)) {
				selectListItem(new Common(strLabel).formdropdown, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
						strPageName, "Value");
			}
			
					
		}
		else if(strLabel.contains("Form2"))
		{
			String[] fieldName = strLabel.split("!");
			strLabel = fieldName[0];
			if (objectExists(new Common(strLabel).multivaldropdownWithName, "isEnabled", lngMinTimeOutInSeconds, strLabel, "DropDown",
					strPageName, false)) {
				selectListItem(new Common(strLabel).multivaldropdownWithName, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
						strPageName, "Value");
			}
			
					
		}
		else if(strLabel.contains("Tabular"))
		{
			String[] fieldName = strLabel.split("!");
			strLabel = fieldName[0];
			if (objectExists(new Common(strLabel).tabDropDown, "isEnabled", lngMinTimeOutInSeconds, strLabel, "DropDown",
					strPageName, false)) {
				selectListItem(new Common(strLabel).tabDropDown, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
						strPageName, "Value");
			}
			
					
		}
		else
		{
			if(strLabel.equalsIgnoreCase("resolve"))
			{
				System.out.println("Wait");
			}
			if (objectExists(new Common(strLabel).select, "isEnabled", lngMinTimeOutInSeconds, strLabel, "DropDown",
					strPageName, false)) {
				pagescroll(new Common(strLabel).select, strPageName);
				driverUtil.waitUntilElementVisible(new Common(strLabel).select, lngMinTimeOutInSeconds, strLabel, "Dropdown", strPageName, false);
				selectListItem(new Common(strLabel).select, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
						strPageName, "Value");
			}
		}	
	}	
	//siva 11/17
	public void spSelectDropdown(String strLabel, String strValue, String strPageName) {
		
		
		if(strLabel.contains("sparchome"))
				{
					
					String[] strInputdata = strLabel.split("!");
					String strLabelName = strInputdata[1];
					
					driverUtil.waitFor(2000, strLabel, strValue, strPageName);
				//	click(new Common(strLabelName).spselect, timeOutInSeconds, strLabel, "Search Button", strPageName, true);
					WebElement elm =  driver.findElement(new uimap.Common(strLabelName).label);
					elm.click();
					WebElement elm1 =  driver.findElement(new uimap.Common(strValue).label1);
					elm1.click();
					driverUtil.waitFor(2000, strLabel, strValue, strPageName);
			
				}
		else {
			Common objtextbox = new Common(strLabel);
			if (objectExists(new Common(strLabel).spDropDownArrow, "isEnabled", lngMinTimeOutInSeconds, strLabel, "DropDown",strPageName, false)) {
				//pagescroll(new Common(strLabel).spDropDownArrow, strPageName);
				click(new Common(strLabel).spDropDownArrow, timeOutInSeconds, strLabel, "Search Box", strPageName, false);
			}
			
			
				sendkeys(objtextbox.sparcDropDowntextbox, timeOutInSeconds, strValue, strLabel, strPageName);
				driverUtil.waitFor(4000, strLabel, strValue, strPageName);
				sendkeys(objtextbox.sparcDropDowntextbox, lngMinTimeOutInSeconds, Keys.ENTER, strLabel, strPageName);
				//sendkeys(objtextbox.sparctextbox, lngMinTimeOutInSeconds, Keys.TAB, strLabel, strPageName);
				driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
//			
		}
	}
	public void verifyObjectState() {
		By locator = null;
		String strFieldName = "";
		String strElementType = "";
		String strElementState = "";
		String strPageName = dataTable.getExpectedResult("PageName");
		String[] strInputParameter = dataTable.getExpectedResult("EditAndNonEdit_FieldLabel").split("!");
		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		for (String strInput : strInputParameter) {
			String[] strObjectSateValue = strInput.split(";");
			strFieldName = StringUtils.substringAfter(strObjectSateValue[0], "=");
			strElementType = StringUtils.substringAfter(strObjectSateValue[1], "=");
			strElementState = StringUtils.substringAfter(strObjectSateValue[2], "=");

			switch (strElementType.toLowerCase()) {
			case "textbox":
				locator = new uimap.Common(strFieldName).textbox;
				break;
			case "textarea":
				locator = new uimap.Common(strFieldName).textarea;
				break;
			case "select":
				locator = new uimap.Common(strFieldName).select;
				break;
			case "combobox":
				locator = new uimap.Common(strFieldName).comboLookup;
				break;
			case "noneditable":
				locator = new uimap.Common(strFieldName).readonlyFiled;
				break;
			case "dropdown":
				locator = new uimap.Common(strFieldName).dropdown;
				break;

			case "unlockcombobox":
				locator = new uimap.Common(strFieldName).unlockButton;
				break;

			case "button":
				locator = new uimap.Common(strFieldName).button;
				break;
				
			case "tabbutton":
				locator = new uimap.Common(strFieldName).TabButton;
				break;
			case "checkbox":
				locator = new uimap.Common(strFieldName).checkbox;
				break;
			case "link":
				locator = new uimap.Common(strFieldName).link;
				break;
			case "menu":
				locator = new uimap.Common(strFieldName).menu;
				break;
			case "tab":
				locator = new uimap.Common(strFieldName).tab;
				break;
			case "readonlytextbox":
				locator = new uimap.Common(strFieldName).readonlyTextbox;
				break;
			case "dropdownoptions":
				locator = new uimap.Common(strFieldName).readonlyDropdownValues;
				break;
			default:
				ALMFunctions.ThrowException("Verify Object State", "Only pre-defined control must be provided",
						"Unhandled control " + strElementType, false);
				break;
			}

			switch (strElementState.toLowerCase()) {
			case "readonly":
				verifyReadOnly(locator, strFieldName, strElementType, strElementState, strPageName);
				break;
			case "editable":
				verifyEditable(locator, strFieldName, strElementType, strElementState, strPageName);
				break;
			case "button not exist":
				strElementState = "not exist";
				verifyButtonExist(locator, strFieldName, strElementState, strElementType, strPageName);
				break;
			case "button exist":
				strElementState = "exist";
				verifyButtonExist(locator, strFieldName, strElementState, strElementType, strPageName);
				break;
			case "select tab":
				tab(strFieldName, strPageName);
				break;
			default:
				ALMFunctions.ThrowException("Verify Object State", "Only pre-defined control must be provided",
						"Unhandled control " + strElementState, false);
				break;
			}

		}
		frameSwitchAndSwitchBack(Common.frame, "default", strPageName);

	}
	
	public void breadcrumbLink(String strLabel, String strValue, String strPageName) {
		
		if(objectExists(new uimap.Common(strValue).breadcrumbLink, "isDisplayed", timeOutInSeconds, strLabel, "Link", strPageName, false)) {
			click(new uimap.Common(strValue).breadcrumbLink, timeOutInSeconds, strLabel, "Link", strPageName, true);
			driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		}
	}
	
	
	public void verifyReadOnly(By locator, String strFieldName, String strElementType, String strElementState,
			String strPageName) {
		driverUtil.waitUntilStalenessOfElement(locator, strPageName);
		if (objectExists(locator, "isDisplayed", timeOutInSeconds, strFieldName, strElementType, strPageName, false)) {
			if (driver.findElement(locator).getAttribute("readonly") != null) {
				if (driver.findElement(locator).getAttribute("readonly").equalsIgnoreCase("true")) {
					report.updateTestLog("Verify '" + strFieldName + "' field as '" + strElementState + "'",
							"'" + strFieldName + "' field is " + strElementState + " in the " + strPageName,
							Status.DONE);
				} else {
					ALMFunctions.ThrowException("Verify " + strFieldName + " field as " + strElementState + "",
							strFieldName + " field should be " + strElementState + " in the " + strPageName,
							"Error - " + strFieldName + " is available for editing in " + strPageName, true);
				}
			} else if (driver.findElement(locator).getAttribute("disabled") != null) {
				if (driver.findElement(locator).getAttribute("disabled").equalsIgnoreCase("true")) {
					report.updateTestLog("Verify '" + strFieldName + "' field as '" + strElementState + "'",
							"'" + strFieldName + "' field is " + strElementState + " in the " + strPageName,
							Status.DONE);
				} else {
					ALMFunctions.ThrowException("Verify " + strFieldName + " field as " + strElementState + "",
							strFieldName + " field should be " + strElementState + " in the " + strPageName,
							"Error - " + strFieldName + " is available for editing in " + strPageName, true);
				}
			} else if (driver.findElement(locator).getAttribute("class").contains("disabled")) {
				report.updateTestLog("Verify '" + strFieldName + "' field as '" + strElementState + "'",
						"'" + strFieldName + "' field is " + strElementState + " in the " + strPageName, Status.DONE);
			} else {
				ALMFunctions.ThrowException("Verify " + strFieldName + " field as " + strElementState + "",
						strFieldName + " field should be " + strElementState + " in the " + strPageName,
						"Error - " + strFieldName + " is available for editing in " + strPageName, true);
			}
		} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strFieldName + " type as " + strElementType,
					strFieldName + " should be displayed as readonly in " + strPageName,
					"Error - " + strFieldName + " is not displayed as readonly in " + strPageName, true);
		}
	}

	public void verifyEditable(By locator, String strFieldName, String strElementType, String strElementState,
			String strPageName) {
		// driverUtil.waitUntilStalenessOfElement(locator, strPageName);
		
		if (driverUtil.waitUntilElementLocated(locator,timeOutInSeconds, strFieldName, strElementType, strPageName, false)) {
			if (driver.findElement(locator).getAttribute("class").contains("inputText")) {
				report.updateTestLog("Verify '" + strFieldName + "' field as '" + strElementState + "'",
						"'" + strFieldName + "' field is " + strElementState + " in the " + strPageName, Status.DONE);
			} else if (driver.findElement(locator).getAttribute("iscombocontrol") != null) {
				if (driver.findElement(locator).getAttribute("iscombocontrol").equalsIgnoreCase("iscombocontrol")) {
					report.updateTestLog("Verify '" + strFieldName + "' field as '" + strElementState + "'",
							"'" + strFieldName + "' field is " + strElementState + " in the " + strPageName,
							Status.DONE);
				} else {
					ALMFunctions.ThrowException("Verify '" + strFieldName + "' field as " + strElementState + "",
							"'" + strFieldName + "' field should be " + strElementState + " in the " + strPageName,
							"Error - " + strFieldName + " is NOT available for editing in " + strPageName, true);
				}
			} else if (driver.findElement(locator).isEnabled() == true) {
				report.updateTestLog("Verify '" + strFieldName + "' field as '" + strElementState + "'",
						"'" + strFieldName + "' field is " + strElementState + " in the " + strPageName, Status.DONE);
			} else {
				ALMFunctions.ThrowException("Verify " + strFieldName + " field as " + strElementState + "",
						strFieldName + " field should be " + strElementState + " in the " + strPageName,
						"Error - " + strFieldName + " is NOT available for editing in " + strPageName, true);
			}

		} else {
			ALMFunctions.ThrowException(strFieldName + " type as " + strElementType,
					strFieldName + " should be displayed in " + strPageName,
					"Error - " + strFieldName + " is not displayed in " + strPageName, true);
		}
	}
	
	public void verifyNonEditable(By locator, String strFieldName, String strElementType, String strElementState,
			String strPageName) {
		// driverUtil.waitUntilStalenessOfElement(locator, strPageName);
		
		if (driverUtil.waitUntilElementLocated(locator,timeOutInSeconds, strFieldName, strElementType, strPageName, false)) {
			if (!(driver.findElement(locator).getAttribute("class").contains("inputText"))) {
				report.updateTestLog("Verify '" + strFieldName + "' field as '" + strElementState + "'",
						"'" + strFieldName + "' field is " + strElementState + " in the " + strPageName, Status.DONE);
			}  
			else if (!(driver.findElement(locator).isEnabled() == true)) {
				report.updateTestLog("Verify '" + strFieldName + "' field as '" + strElementState + "'",
						"'" + strFieldName + "' field is " + strElementState + " in the " + strPageName, Status.DONE);
			} else {
				ALMFunctions.ThrowException("Verify " + strFieldName + " field as " + strElementState + "",
						strFieldName + " field should be " + strElementState + " in the " + strPageName,
						"Error - " + strFieldName + " is available for editing in " + strPageName, true);
			}

		} else {
			ALMFunctions.ThrowException(strFieldName + " type as " + strElementType,
					strFieldName + " should be displayed in " + strPageName,
					"Error - " + strFieldName + " is not displayed in " + strPageName, true);
		}
	}
	
	

	/*public void verifyEditandNonEditField() {               //Method not in use 

		String strPageName = dataTable.getExpectedResult("PageName");
		String[] strInputdata = dataTable.getExpectedResult("EditAndNonEdit_FieldLabel").split("!");
		String strFieldType = strInputdata[0];
		String[] strFieldlabel = strInputdata[1].split(";");
		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);

		switch (strFieldType.toLowerCase()) {

		case "readonly":

			for (String strLableName : strFieldlabel) {
				if (objectExists(new Common(strLableName).readonlyFiled, "isDisplayed", lngMinTimeOutInSeconds,
						strLableName, "Read Only Element", strPageName, false)) {

					ALMFunctions.UpdateReportLogAndALMForPassStatus(strLableName,
							strLableName + " element should be non editable(readonly) in the " + strPageName,
							strLableName + " element is non editable(readonly) in the " + strPageName, true);

				} else {

					ALMFunctions.UpdateReportLogAndALMForFailStatus(strLableName,
							strLableName + " element should be non editable(readonly) in the " + strPageName,
							strLableName + " element is editable in the " + strPageName, true);
				}

			}

			break;

		case "editable":

			for (String strLableName : strFieldlabel) {
				if (objectExists(new Common(strLableName).readonlyFiled, "isDisplayed", lngMinTimeOutInSeconds,
						strLableName, "Read Only Element", strPageName, false)) {
					ALMFunctions.UpdateReportLogAndALMForFailStatus(strLableName,
							strLableName + " filed should be editable in the " + strPageName,
							strLableName + " filed is not editable in the " + strPageName, true);

				} else {

					ALMFunctions.UpdateReportLogAndALMForPassStatus(strLableName,
							strLableName + " filed should be editable in the " + strPageName,
							strLableName + " filed is editable in the " + strPageName, true);

				}

			}

			break;

		default:

			ALMFunctions.ThrowException("Test Data",
					"Only Pre-Defined Form Options must be provided in the test data sheet",
					"Error - Unhandled Form Options " + strFieldType, false);
			break;
		}

	}*/

	public void verifyButtonExist(By loator, String strButtonName, String strElementState, String strElementType,
			String strPageName) {

		switch (strElementState) {
		case "exist":
			if (objectExists(loator, "isDisplayed", lngMinTimeOutInSeconds, strButtonName, "Read Only Element",
					strPageName, false)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus(strButtonName,
						strButtonName + " " + strElementType + " should be display in the " + strPageName,
						strButtonName + " " + strElementType + " is displayed in the " + strPageName, true);

			} else {
				ALMFunctions.UpdateReportLogAndALMForFailStatus(strButtonName,
						strButtonName + " " + strElementType + " should be display in the " + strPageName,
						strButtonName + " " + strElementType + " is not displayed in the " + strPageName, true);

			}

			break;

		case "not exist":
			if (objectExists(loator, "isDisplayed", lngMinTimeOutInSeconds, strButtonName, "Read Only Element",
					strPageName, false)) {
				ALMFunctions.UpdateReportLogAndALMForFailStatus(strButtonName,
						strButtonName + " " + strElementType + " should not display in the " + strPageName,
						strButtonName + " " + strElementType + " is displayed in the " + strPageName, true);

			} else {
				ALMFunctions.UpdateReportLogAndALMForPassStatus(strButtonName,
						strButtonName + " " + strElementType + " should not display in the " + strPageName,
						strButtonName + " " + strElementType + " is not displayed in the " + strPageName, true);

			}

			break;

		default:
			ALMFunctions.ThrowException("Verify object", "Only pre-defined control must be provided",
					"Unhandled control " + strElementState, false);
			break;
		}

	}

	public void addremoveMemberGroup()

	{

		String strLabelName = "";
		String strValue = "";
		String strGroupType = "";
		String strButton = "";
		String strPageName = dataTable.getExpectedResult("PageName");
		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
		String[] strInputParameter = dataTable.getExpectedResult("Add_Remove_Member").split("!");
		for (String strInput : strInputParameter) {
			String[] strMemebrData = strInput.split(";");
			strLabelName = StringUtils.substringAfter(strMemebrData[0], "=");
			strValue = StringUtils.substringAfter(strMemebrData[1], "=");
			strGroupType = StringUtils.substringAfter(strMemebrData[2], "=");
			strButton = StringUtils.substringAfter(strMemebrData[3], "=");
			if (objectExists(new Common(strLabelName).select, "isEnabled", lngMinTimeOutInSeconds, strLabelName,
					"Select", strPageName, false)) {
				switch (strGroupType.toLowerCase()) {
				case "add":

					for (String strMembername : strValue.split(",")) {

						sendkeys(new Common(strLabelName).groupmenberTextbox, lngMinTimeOutInSeconds, strValue,
								strLabelName, strPageName);
						driverUtil.waitUntilStalenessOfElement(new Common(strLabelName).select, strPageName);
						selectListItem(new Common(strLabelName).select, lngMinTimeOutInSeconds,
								new String[] { strMembername }, strLabelName, strPageName, "Value");
						click(new Common(strGroupType).addRemoveButton, lngMinTimeOutInSeconds, strGroupType, "Button",
								strPageName, true);

					}

					break;

				case "remove":
					for (String strMembername : strValue.split(",")) {

						selectListItem(new Common(strLabelName).select, lngMinTimeOutInSeconds,
								new String[] { strMembername }, strLabelName, strPageName, "Value");
						click(new Common(strGroupType).addRemoveButton, lngMinTimeOutInSeconds, strGroupType, "Button",
								strPageName, true);
					}

					break;

				default:

					ALMFunctions.ThrowException("Test Data",
							"Only Pre-Defined Form Options must be provided in the test data sheet",
							"Error - Unhandled Form Options " + strGroupType, false);

					break;

				}
				click(new Common(strButton).button, lngMinTimeOutInSeconds, strButton, "Button", strPageName, true);

			} else {
				ALMFunctions.ThrowException(strLabelName,
						strLabelName + " dropdown should be enabled in the " + strPageName + " Page",
						strLabelName + " dropdown is not enabled in the " + strPageName + " Page", true);

			}

		}
		frameSwitchAndSwitchBack(Common.frame, "default", strPageName);
	}

	/**
	 * Method to Click Check box
	 * 
	 * @param strValue, value to click check box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void checkbox(By locator, String strFieldName, String strPageName) {

		if(strFieldName.equalsIgnoreCase("Acquisition")) {
			System.out.println("Wait");
		}
		if (objectExists(locator, "isDisplayed", timeOutInSeconds, strFieldName, "check box", strPageName, false)) {
			boolean blnChecked = driver.findElement(locator).isSelected();
			if (blnChecked) {

				// clickByJS(locator, timeOutInSeconds, strFieldName, "check
				// box", strPageName, true);
				report.updateTestLog(strFieldName, strFieldName + " checkbox is already checked", Status.PASS);
			} else {

				clickByJS(locator, timeOutInSeconds, strFieldName, "check box", strPageName, true);

			}
		} else {
			ALMFunctions.ThrowException("Checkbox", strFieldName + " should be displayed in " + strPageName,
					"Error - " + strFieldName + " is not available for editing in " + strPageName, true);
		}

	}

	public void checkbox1(String strFieldName, String strCondition, String strPageName) {
		By locator = null;
		locator = new uimap.Common(strFieldName).checkbox;
		if (objectExists(locator, "isDisplayed", timeOutInSeconds, strFieldName, "check box", strPageName, false)) {
			if (driver.findElement(locator).isSelected()) {
				switch (strCondition.toLowerCase()) {
				case "check":
					report.updateTestLog("Checkbox Select", strFieldName + " is already selected", Status.DONE);
					break;
				case "uncheck":
					clickByJS(locator, timeOutInSeconds, strFieldName, "check box", strPageName, true);
					break;
				}
			} else {
				switch (strCondition.toLowerCase()) {
				case "check":
					clickByJS(locator, timeOutInSeconds, strFieldName, "check box", strPageName, true);
					break;
				case "uncheck":
					report.updateTestLog("Checkbox Select", strFieldName + " is already unchecked", Status.DONE);
					break;
				}
			}
		} else {
			ALMFunctions.ThrowException("Checkbox", strFieldName + " should be displayed in " + strPageName,
					"Error - " + strFieldName + " is not available for editing in " + strPageName, true);
		}

	}

	/**
	 * Method to handle error message
	 * 
	 * @param no parameter
	 * @return No return value
	 */
	public void errorHandling(String Label, String strPageName) {
		String strValue = "";
		String strValues = "";
		if (objectExists(Common.errorMessage, "isDisplayed", lngMinTimeOutInSeconds, "Error Message", "Label",
				strPageName, false)) {
			List<WebElement> errorLists = driver.getWebDriver().findElements(Common.errorMessage);
			for (WebElement errorList : errorLists) {
				strValue = errorList.getText().trim();

				strValues = strValues.concat(strValue + "<br>");
			}
			report.updateTestLog("Verify Error Message Displayed", strValues+  " is the message displayed", Status.DONE);
//			ALMFunctions.ThrowException("Error", "User should enter the mandatory value",
//					"Please enter the mandatory value : <br>" + strValues, true);
		} else if (objectExists(new uimap.Common("State").notifyError, "isDisplayed", lngMinTimeOutInSeconds,
				"Error Message", "Label", strPageName, false)) {
			String strText = driver.findElement(new uimap.Common("State").notifyError).getText();
			if (!strText.isEmpty()) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Notification",
						"Error message should be displayed as expected", strText + "is the error displayed", true);
			}

		}

	}

	public void dialogSearch(String strLabel, String strValue, String strPageName) {
		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
		//Commented by yash 
		//click(new uimap.Common(strLabel).dropDownArrow, lngMinTimeOutInSeconds, strLabel, "Dropdown Arrow", strPageName,true);
		 if(objectExists(new uimap.Common(strLabel).dropDownArrow,"isDisplayed", lngMinTimeOutInSeconds, strLabel, "Dropdown Search",
                 strPageName, false)) {                                                           
           click(new uimap.Common(strLabel).dropDownArrow, lngMinTimeOutInSeconds, strLabel, "Dropdown Arrow", strPageName,true);
    }

		if (objectExists(Common.dropDownSearch, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "Dropdown Search",
				strPageName, false)) {
			click(Common.dropDownSearch, timeOutInSeconds, strLabel, "Dropdown Search", strPageName, true);
			sendkeys(Common.dropDownSearch, timeOutInSeconds, strValue, strLabel, strPageName);
//			sendkeys(Common.dropDownSearch, lngMinTimeOutInSeconds, Keys.ENTER, strLabel, strPageName);
//			clickByJS(new uimap.Common(strValue).dropDownSelectValue, timeOutInSeconds, strLabel, "Dropdown Value", strPageName, true);
			mouseOverandClick(new uimap.Common(strValue).dropDownSelectValue, timeOutInSeconds, strLabel,
					"Dropdown Value", strPageName, true);
		}
	}

	/*public void dialogDropDown(String strLabel, String strValue, String strPageName) {
		try {
				if (objectExists(new uimap.Common(strLabel).dropDownClick, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Survey Choice",
						strPageName, false)) 
				{
				click(new uimap.Common(strLabel).dropDownClick, timeOutInSeconds, strLabel, "Dropdown Click", strPageName,
						true);
				click(new uimap.Common(strLabel, strValue).dropDownSelect, timeOutInSeconds, strLabel, "Dropdown Click",
						strPageName, true);
		
			}
		}
		
		catch (Exception e) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("DropDown : " + strLabel,"User should able to displayed " + strLabel + " DropDown",strLabel + " value is selected as : " + strValue, true);
		}
	}*/
	
	public void dialogDropDown(String WindowName,String strLabel, String strValue, String strPageName) {
		try {
			uimap.Common button = new uimap.Common(WindowName, strLabel);
			driver.capture(strPageName);
			driverUtil.waitFor(5000, strLabel, strLabel, strPageName);
			if (objectExists(button.dialogDropDown, "isDisplayed", timeOutInSeconds, strValue, "Popup Button", strPageName,false)) {
				selectListItem(button.dialogDropDown, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
						strPageName, "Value");
				//clickByJS(button.dialogDropDown, lngMinTimeOutInSeconds, strValue, "Popup Button", strPageName, true);
				driverUtil.waitUntilStalenessOfElement(button.button, lngMinTimeOutInSeconds, strPageName);
			} 	
			
			
			else if (objectExists(new uimap.Common(strLabel).dropDownClick, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Survey Choice",
						strPageName, false)) 
				{
				click(new uimap.Common(strLabel).dropDownClick, timeOutInSeconds, strLabel, "Dropdown Click", strPageName,
						true);
				click(new uimap.Common(strLabel, strValue).dropDownSelect, timeOutInSeconds, strLabel, "Dropdown Click",
						strPageName, true);
		
			}
		}
		
		catch (Exception e) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("DropDown : " + strLabel,"User should able to displayed " + strLabel + " DropDown",strLabel + " value is selected as : " + strValue, true);
		}
	}

	public void dialogTextArea(String strWindowName,String strLabel, String strValue, String strPageName) {
		uimap.Common textarea = new uimap.Common(strWindowName, strLabel);
		if (objectExists(textarea.dialogTextArea, "isDisplayed", lngMinTimeOutInSeconds, strLabel,"Dialog TextArea", strPageName, false)) {
			sendkeys(textarea.dialogTextArea, timeOutInSeconds, strValue, strLabel, strPageName);
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Dialog TextArea : " + strLabel,"User should able to enter " + strLabel + " in the TextArea",strLabel + " value is enterted as : " + strValue, true);
		}
		
		else if (objectExists(new uimap.Common(strLabel).dialogTextArea, "isDisplayed", lngMinTimeOutInSeconds, strLabel,"Dialog TextArea", strPageName, false)) {
			sendkeys(new uimap.Common(strLabel).dialogTextArea, timeOutInSeconds, strValue, strLabel, strPageName);
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Dialog TextArea : " + strLabel,"User should able to enter " + strLabel + " in the TextArea",strLabel + " value is enterted as : " + strValue, true);
		}
		else
		{
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Dialog TextArea : " + strLabel,"User should able to enter " + strLabel + " in the TextArea",strLabel + " value is able to enterted as : " + strValue, true);
		}
	}
	
	public void surveyChoice(String strLabel, String strValue, String strPageName) {
		uimap.Common choice = new Common(strLabel, strValue);
		try {

			if (objectExists(choice.surveyChoice, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Survey Choice",
					strPageName, false)) {
				click(choice.surveyChoice, lngMinTimeOutInSeconds, strValue, "Survey Choice", strPageName, true);

			}

		} catch (Exception e) {
			report.updateTestLog("Survey Choice", "Error " + e.getLocalizedMessage(), Status.FAIL);
		}

	}

	public void frameSwitchAndSwitchBack(By locator, String StrFrameValue, String strPageName) {

		switch (StrFrameValue.toLowerCase()) {
		case "switchframe":
			driver.switchTo().defaultContent();
			if (objectExists(locator, "isEnabled", lngMinTimeOutInSeconds, StrFrameValue, "Frame", strPageName,false)) {
				new WebDriverWait(driver.getWebDriver(), timeOutInSeconds).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));

			} /*
				 * else { ALMFunctions.ThrowException("Frame Switch",
				 * "Frame should be enabled in " + strPageName,
				 * "Error - Frame is not available in " + strPageName, true); }
				 */
			break;
		case "switchcardframe":
			if(objectExists(locator, "isEnabled", lngMinTimeOutInSeconds, StrFrameValue, "Frame", strPageName,
					false)) {
				new WebDriverWait(driver.getWebDriver(), timeOutInSeconds)
						.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
			}
			break;
		case "default":
			driver.switchTo().defaultContent();
			break;
			
		case "dialog":
			if (objectExists(locator, "isEnabled", lngMinTimeOutInSeconds, StrFrameValue, "Frame", strPageName,false)) {
				new WebDriverWait(driver.getWebDriver(), timeOutInSeconds).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));

			}
			break;	

		}
		
		
		
	}

	/**
	 * Method to get the case number from UI post data creation
	 * 
	 * @param no parameter
	 * @return No return value
	 */

	public void getCaseNumber(String strLabel, String fieldName, String strPageName) {

		String strPageView = getCurrentPageView();
		switch (strPageView) {
		case "SPARC Dev":
			if (objectExists(new Common(strLabel).readonlyTextbox, "isDisplayed", lngPagetimeOutInSeconds, strLabel,
					"Label", strPageName, false)) {
				String strGetValue = getAttributeValue(new Common(strLabel).readonlyTextbox, lngMinTimeOutInSeconds,
						"value", strLabel, strPageName);
				 if (strGetValue.charAt(0) == '$') {
                     strGetValue = strGetValue.substring(1);          ////added this extra line
              }

				ALMFunctions.UpdateReportLogAndALMForPassStatus("Problem Task Record : " + strLabel,
						"User should able to capture the " + strLabel + " Task Record",
						strLabel + " task record has been captured as : " + strGetValue, true);
				report.updateTestLog("Task Record : " + strLabel,
						strLabel + " task case record has been captured as : " + strGetValue, Status.PASS);
				FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
			    if(inputDataFilelock!=null){
			        synchronized (CommonFunctions.class) {
			            dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
			        }   
			        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
			   
			}
				
			} else if (objectExists(new Common(strLabel).readonlyFiled, "isDisplayed", lngPagetimeOutInSeconds, strLabel,
					"Label", strPageName, false)) {
				String strGetValue = getAttributeValue(new Common(strLabel).readonlyFiled, lngMinTimeOutInSeconds,
						"value", strLabel, strPageName);
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Case Record : " + strLabel,
						"User should able to capture the " + strLabel + " Case Record",
						strLabel + " case record has been captured as : " + strGetValue, true);
				report.updateTestLog("Case Record : " + strLabel,
						strLabel + " case record has been captured as : " + strGetValue, Status.PASS);
				FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
			    if(inputDataFilelock!=null){
			        synchronized (CommonFunctions.class) {
			            dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
			        }   
			        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
			   
			}
				
			}
			
			else if (objectExists(Common.totalRows, "isDisplayed", lngPagetimeOutInSeconds, strLabel,"Label", strPageName, true)) {
				String strValue = getText(Common.totalRows, lngPagetimeOutInSeconds, "Case Number", strPageName);
				System.out.println(strValue);
				manageAndSwitchNewWindow();
				frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
				
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Case Record : " + strLabel,
						"User should able to capture the " + strLabel + " Case Record",
						strLabel + " case record has been captured as : " + strValue, true);
				report.updateTestLog("Case Record : " + strLabel,
						strLabel + " case record has been captured as : " + strValue, Status.PASS);
				FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
			    if(inputDataFilelock!=null){
			        synchronized (CommonFunctions.class) {
			            dataTable.putData("Parametrized_Checkpoints", fieldName, strValue);
			        }   
			        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
			   
			}
				
			}
			break;
		case "ESC Portal":
			String strValue = getText(EscPortal.escCaseNumber, lngPagetimeOutInSeconds, "Case Number", strPageName);
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Case Record : " + strLabel,
					"User should able to capture the " + strLabel + " Case Record",
					strLabel + " case record has been captured as : " + strValue, true);
			report.updateTestLog("Case Record : " + strLabel,
					strLabel + " case record has been captured as : " + strValue, Status.PASS);
			dataTable.putData("Parametrized_Checkpoints", fieldName, strValue);
			break;
		default:
			ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
					"Unhandled - View - " + strPageView, false);
			break;
		}

	}

	/**
	 * Method to select the list value in drop down
	 * 
	 * @param strFieldName, By locator of the control
	 * @param strFieldName, Field name of the text box
	 * @param strValue, value to be select in a list box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void listValueSelect(By locator, String strFieldName,long timeout, String strValue, String strElementType,
			String strPageName) {
		boolean blnRecordFound = false;
		driverUtil.waitUntilElementVisible(locator, lngMinTimeOutInSeconds, strFieldName, strElementType, strPageName,
				false);
		if (driverUtil.waitUntilElementEnabled(locator, lngMinTimeOutInSeconds, strFieldName, strElementType,
				strPageName)) {
			List<WebElement> listItems = driver.getWebDriver().findElements(locator);
			for (WebElement listItem : listItems) {
				if (listItem.getText().compareTo(strValue) == 0) {
					driverUtil.waitFor(CtrlTimeOut, listItem.getText(), strElementType, strPageName);
					driver.capture(strValue);
					driverUtil.waitUntilStalenessOfElement(listItem, strPageName);
					listItem.click();
					report.updateTestLog("Click",strValue + " " + strElementType + " in " + strPageName + " is clicked", Status.DONE);
					blnRecordFound = true;
					break;
				} else if (listItem.getText().replaceAll("[0-9]", "").replace("items", "").trim()
						.equalsIgnoreCase(strValue)) {
					driver.capture(strValue);
					listItem.click();
					report.updateTestLog("Click",
							strValue + " " + strElementType + " in " + strPageName + " is clicked", Status.DONE);
					blnRecordFound = true;
					break;
				}

			}

			if (!blnRecordFound) {
				ALMFunctions.ThrowException("Select dropdown Value",
						strValue + " item should be display in the " + strFieldName + " dropdown",
						strValue + " item is not displayed in the " + strFieldName + " dropdown", true);

			}else {
				
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Select dropdown Value",
						strValue + " item should be display in the " + strFieldName + " dropdown",
						strValue + " item is displayed and selected in the " + strFieldName + " dropdown", true);
			}

		} else {
			ALMFunctions.ThrowException("Select List",
					"List of items should be display in the " + strFieldName + " dropdown",
					"List of items is not displayed in the " + strFieldName + " dropdown", true);
		}
	}
	public void findRecordAndNavigate(String strValue, String strPageName) {
		findRecordAndNavigate();
	}

	/**
	 * Method to find record in Table and select record with two condition.
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void findRecordAndNavigate() {
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
		String strTableName = getText(Common.Usergrouptitle, lngPagetimeOutInSeconds, "Table title",
				"Service Now page");
		driverUtil.waitUntilElementVisible(new uimap.Common("All").breadcrumbLink, lngMinTimeOutInSeconds, "All", "Link", strTableName, true);
		if(objectExists(new uimap.Common("All").breadcrumbLink, "isDisplayed", timeOutInSeconds, "All", "Link", strTableName, false)) {
			click(new uimap.Common("All").breadcrumbLink, timeOutInSeconds, "All", "Link", strTableName, true);
			driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strTableName);
		}
		searchRecord();
		findRecord(Common.tableHeader_1, Common.tableRow, dataTable.getExpectedResult("Find_Record_1"), false,
				strTableName); // Previously it wasord tableHeader
		driver.switchTo().defaultContent();
	}
	
	public void priorityAndNotification() {
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Priority and Notification", "~", true, true);
		FillInputForm(strInputParameters);
	}
	
	public void sparcCreation() {
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Create Sparc", "~", true, true);
		FillInputForm(strInputParameters);
	}
	
	
	public void findRecordAndCaptureID(String strLabel, String strValue, String strPageName) {
		if(objectExists(new uimap.Common(strLabel).tablelinkText, "isDisplayed", timeOutInSeconds, strLabel, "Link Text", strPageName, false)) {
			pagescroll(new uimap.Common(strLabel).tablelinkText, strPageName);
			clickByJS(new uimap.Common(strLabel).tablelinkText, timeOutInSeconds, strLabel, "Link Text", strPageName, true);
		}
	}
	
	
	
	public void verifyTaskTypeState(String strTaskType, String strExpState, String strPageName) {
		if(objectExists(new uimap.Common("Problem Tasks").navbarTitle, "isDisplayed", timeOutInSeconds, strTaskType, "Title", strPageName, false)) {
			pagescroll(new uimap.Common("Problem Tasks").navbarTitle, strPageName);
		} else {
			pagescroll(new uimap.Common(strPageName).navbarTitle, strPageName);
		}
		
		if(objectExists(new uimap.Common(strTaskType).tableText, "isDisplayed", timeOutInSeconds, strTaskType, "State", strPageName, false)) {
			String strGetState = driver.findElement((new uimap.Common(strTaskType).tableText)).getText();
			if(strGetState.equalsIgnoreCase(strExpState)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Task Type with State",
						"Task Type should be displayed with the expected state",
						 "<br> Task Type - " +strTaskType+ 
						 "<br> State Value - " +strGetState, true);
			}
		} else if(!objectExists(new uimap.Common(strTaskType).tableText, "isDisplayed", timeOutInSeconds, strTaskType, "State", strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Task Type with State", "Task should not be generated as expected", "Task is not generated as expected", true);
		}
	}
	
	
	public void verifyReqItemAttachment(String strLabel, String strValue, String strPageName) {
		if(objectExists(new uimap.Common(strPageName).navbarTitle, "isDisplayed", timeOutInSeconds, strLabel, "Title", strPageName, false)) {
			pagescroll(new uimap.Common(strPageName).navbarTitle, strPageName);
		}
		if(objectExists(new uimap.Common(strValue).tableText, "isDisplayed", timeOutInSeconds, strLabel, "Attachment Link", strPageName, false)) {
			String strGetState = driver.findElement((new uimap.Common(strValue).tableText)).getText();
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify "+strPageName, "Attachment should be displayed as expected", 
					strGetState+ " is displayed successfully under "+strPageName, true);
		}
	}
	
	/**
	 * Method to find record in Table and select record with one condition.
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void navigateToRecord() {
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
		searchRecord();
		String strTableName = getText(Common.Usergrouptitle, lngPagetimeOutInSeconds, "Table title",
				"Service Now page");
		findRecord(Common.tableHeader, Common.tableRow, dataTable.getExpectedResult("Find_Record_2"), true,
				strTableName);
		driver.switchTo().defaultContent();
	}
	
	public void selectRecord(String strValue) {
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
		searchRecord();
		String strTableName = getText(Common.Usergrouptitle, lngPagetimeOutInSeconds, "Table title",
				"Service Now page");
		click(Common.clickLink, lngMinTimeOutInSeconds, "Record", "Link Text", "Search", true);
		driver.switchTo().defaultContent();
	}
	
	public void titleCard(String strLabel, String strValue, String strPageName) {
		driverUtil.waitFor(15000, strLabel, strValue, strPageName);
		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
		if(objectExists(new uimap.Common(strLabel).titleCard , "isDisplayed", timeOutInSeconds, strLabel, strValue, strPageName, false)){
			click(new uimap.Common(strLabel).titleCard, lngMinTimeOutInSeconds, strLabel, "Link Text", strPageName, false);
			//driverUtil.waitUntilElementInVisible(new uimap.Common(strLabel).titleCard, strLabel, "Link Text", strPageName);
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Value clicked",
					strLabel + " Value should be clicked " + strPageName + " page",
					"Value is selected in the " + strPageName + " page", true);
		}
		else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify Value clicked",
					strLabel + " Value should be clicked " + strPageName + " page",
					"Value is not selected in the " + strPageName + " page", true);
		}
	}

	/**
	 * Method to find record in Table and select record.
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void findRecord() {

		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
		String strTableName = getText(Common.Usergrouptitle, lngPagetimeOutInSeconds, "Table title",
				"Service Now page");
		driverUtil.waitUntilElementInVisible(Common.tooltipUserMenu, "", "Tooltip", strTableName);
		searchRecord();
		driverUtil.waitUntilStalenessOfElement(Common.tableHeader, strTableName);
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, "Service Now Dashboard Table");
		findRecord(Common.tableHeader, Common.tableRow, dataTable.getExpectedResult("Find_Record"), true, strTableName);
		driver.switchTo().defaultContent();
	}

	public void tableHeaderSearch(String strInput, String strTableName) {
		if (objectExists(new uimap.Common("Users").tableHeaderSearch, "isDisplayed", lngMinTimeOutInSeconds,
				strTableName, "Table", strTableName + " Page", false)) {
			List<WebElement> listtableRow = driver.getWebDriver()
					.findElements(new uimap.Common("Users").tableHeaderSearch);
			for (int i = 0; i < listtableRow.size(); i++) {
				WebElement rows = listtableRow.get(i);
				List<WebElement> listtableValue = rows.findElements(By.xpath(".//tr[2]//td//input[@id]"));
				for (int j = 0; j < listtableValue.size(); j++) {
					WebElement strValue = listtableValue.get(j);
					strValue.sendKeys(strInput);
					strValue.sendKeys(Keys.ENTER);
				}

			}
		}
	}
	
	/**
	 * 
	 * @param strValues Value to search in the table
	 */
	public void verifyNoRecordDisplayInTable(String strValues) {
		String strUserNameText = "";
		
		windowName.put("Window1", driver.getWindowHandle());
		manageAndSwitchNewWindow();
		if (objectExists(Common.userMenuLabel, "isDisplayed", timeOutInSeconds, "Title", "Text", "Users Table",
				false)) {
			strUserNameText = getText(Common.userMenuLabel, lngPagetimeOutInSeconds, "UserName", "HR Case Home Page");
		}
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
		String strTableName = getText(Common.Usergrouptitle, lngPagetimeOutInSeconds, "Table title",
				"Service Now page");
		driverUtil.waitUntilElementInVisible(Common.tooltipUserMenu, "", "Tooltip", strTableName);
		searchRecord();
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, "Service Now Dashboard Table");

		if (objectExists(new uimap.Common("Users").tableHeaderSearch, "isDisplayed", lngMinTimeOutInSeconds,
				strTableName, "Table", strTableName + " Page", false)) {
			sendkeys(new uimap.Common("Users").tableHeaderSearch, timeOutInSeconds, strValues, "Name", strTableName);
			sendkeys(new uimap.Common("Users").tableHeaderSearch, timeOutInSeconds, Keys.ENTER, "Name", strTableName);
//			List<WebElement> getRows = driver.getWebDriver().findElements(Common.norecord);
			String strGetValue = driver.findElement(Common.norecord).getText();
			if(strGetValue.equalsIgnoreCase("No records to display")) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify no Record diaplay",
						strTableName + " record should not display in the " + strTableName + " table",
						"No Record is displayed in the " + strTableName + " table", true);
				}
		}
		closeAndSwitchPreviousWindow();
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
	}
	
	public void verifyNoRecordForAppliedCondtion(String strLabel, String strValue, String strPageName) {
		String[] strinputPara = strValue.split("!"); // State!is!Closed
		
		windowName.put("Window1", driver.getWindowHandle());
		if (objectExists(new Common(strLabel).comboLookup, "isDisplayed", timeOutInSeconds, strLabel,
				"Lookup", strPageName, false)) {
			clickByJS(new Common(strLabel).comboLookup, lngMinTimeOutInSeconds, strLabel, "Lookup",
					strPageName, true);
		}
		manageAndSwitchNewWindow();
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
		String strTableName = getText(Common.Usergrouptitle, lngPagetimeOutInSeconds, "Table title",
				"Service Now page");
		mouseOverandClick(Common.downArrowIcon, timeOutInSeconds, strLabel,"Dropdown Value", strPageName, true);
		if (objectExists(Common.dropDownSearch, "isDisplayed", timeOutInSeconds, strValue, "Dropdown Search",
				strPageName, false)) {
			for(String element : strinputPara) {
				selectListItem(new Common(element).dropdownWithoutName, lngMinTimeOutInSeconds, new String[] { element }, //State
						strValue, strPageName, "Value");
			}
//			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify No " +strLabel+ " displayed for the applied condition"
//					, "No record should be displayed as expected for the applied condition" , "", false);
			click(new uimap.Common("Run").button, timeOutInSeconds, strLabel, "Button", strPageName, true);
		}
		
		String strGetValue = driver.findElement(Common.norecord).getText();
		if(!strGetValue.isEmpty()) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify no Record display",
					strTableName + " record should not display in the " + strTableName + " table",
					"No Record is displayed in the " + strTableName + " table for the applied condition", true);
		}
		closeAndSwitchPreviousWindow();
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
	}
	
	public void navigateRecWithState() {
		
	}
	
	public void verifyNoRecordDisplayInTable1(String strValues) {
		boolean blnNotRecordExist = false;
		boolean blnRecordNotFound = false;
		int intCurrentPage = 1;
		String strActValue = "";
		String strUserNameText = getText(Common.userMenuLabel, lngPagetimeOutInSeconds, "UserName",
				"HR Case Home Page");
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
		String strTableName = getText(Common.Usergrouptitle, lngPagetimeOutInSeconds, "Table title",
				"Service Now page");

		driverUtil.waitUntilElementInVisible(Common.tooltipUserMenu, "", "Tooltip", strTableName);
		searchRecord();
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, "Service Now Dashboard Table");
		/*
		 * outerloop: do {
		 * 
		 * ALMFunctions.Screenshot(); if (intCurrentPage != 1) {
		 * 
		 * if (objectExists(uimap.Common.nextPage, "isDisplayed",
		 * lngMinTimeOutInSeconds, "Next", "Button", strTableName, false)) {
		 * click(uimap.Common.nextPage, timeOutInSeconds, "Next", "Button",
		 * strTableName, false);
		 * driverUtil.waitUntilStalenessOfElement(uimap.Common.nextPage, strTableName);
		 * 
		 * } else { blnRecordNotFound = true; }
		 * 
		 * }
		 */

		if (objectExists(Common.tableRow, "isDisplayed", lngMinTimeOutInSeconds, strTableName, "Table",
				strTableName + " Page", false)) {

			LinkedHashMap<String, String> hashUserDetails = new LinkedHashMap<>();
			ArrayList<String> arrUserList = new ArrayList<>();
			String strValue = "";
			/*
			 * findRecord(Common.tableHeader, Common.tableRow,
			 * dataTable.getExpectedResult("Find_Record_1"), false, strTableName);
			 */
			String[] strInput = dataTable.getExpectedResult("Find_Record_1").split(strExclamation);

			for (String strParameter : strInput) {
				String[] strlabelAndType = strParameter.split(",");
				String strLabel = StringUtils.substringAfter(strlabelAndType[0], strEqualto);
				String strtType = StringUtils.substringAfter(strlabelAndType[1], strEqualto);
				hashUserDetails.put(strLabel, strtType);
			}

			List<WebElement> listtableRow = driver.getWebDriver().findElements(Common.tableRow);

			for (WebElement row : listtableRow) {
				row.findElement(By.xpath(".//a[contains(@class,'formlink')][1]")).click();
				driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strTableName);

				for (String strUserLabel : hashUserDetails.keySet()) {

					switch (hashUserDetails.get(strUserLabel).toLowerCase()) {
					case "readonly":
						strValue = getAttributeValue(new Common(strUserLabel).readonlyFiled, lngMinTimeOutInSeconds,
								"value", strUserLabel, strTableName);
						break;
					case "editable":
						if (objectExists(new Common(strUserLabel).textbox, "isDisplayed", lngMinTimeOutInSeconds,
								strUserLabel, (hashUserDetails.get(strUserLabel)), strTableName, false)) {
							strValue = getAttributeValue(new Common(strUserLabel).textbox, lngMinTimeOutInSeconds,
									"value", strUserLabel, strTableName);
							break;
						}
					default:
						break;
					}

					arrUserList.add(strValue);

				}

				if (verifyMultipleRecord(strUserNameText, arrUserList)) {

					click(Common.backButton, lngMinTimeOutInSeconds, "Back", "Button", strTableName, false);
					driverUtil.waitUntilStalenessOfElement(Common.backButton, strTableName);
				} else {

					report.updateTestLog("Verify no Record diaplay", "HR case is opened by " + arrUserList.get(0),
							Status.FAIL);
					ALMFunctions.ThrowException("Verify no Record diaplay",
							strTableName + " record should not display in the " + strTableName + " table",
							"Record is displayed in the " + strTableName + " table", true);
					break;

				}
			}

		}

		/*
		 * intCurrentPage++; } while (!(blnNotRecordExist || blnRecordNotFound));
		 */
		if (!blnNotRecordExist) {

			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify no Record diaplay",
					strTableName + " record should not display in the " + strTableName + " table",
					"No Record is displayed in the " + strTableName + " table", true);
		}
		driver.switchTo().defaultContent();
	}

	public boolean verifyMultipleRecord(String strExpectedValue, ArrayList<String> arrUserList) {
		boolean blnFlag = false;
		for (String strValue : arrUserList) {

			if (strExpectedValue.equals(strValue)) {
				blnFlag = true;
				break;
			}
		}

		return blnFlag;

	}

	public void searchRecord()

	{

		if (!dataTable.getExpectedResult("Search_Record").isEmpty()) {
			String strPagename = "Service Now Dashboard Table";
			String[] strSearchRecord = dataTable.getExpectedResult("Search_Record").split("!");
			String strLabel = StringUtils.substringAfter(strSearchRecord[0], "=");
			String strValue = StringUtils.substringAfter(strSearchRecord[1], "=");
			String[] strValues = strValue.split(",");
			if (objectExists(new Common(strLabel).select, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Dropdown",
					strPagename, false)) {
				selectListItem(new Common(strLabel).select, lngPagetimeOutInSeconds, new String[] { strValues[0] },
						strLabel, strPagename, "Value");
				enterTextbox(strLabel, strValues[1], strPagename);
				driver.capture(strLabel);
				sendkeys(new Common(strLabel).textbox, lngMinTimeOutInSeconds, Keys.ENTER, strLabel, strPagename);
				driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPagename);
				driverUtil.waitUntilStalenessOfElement(new Common(strLabel).textbox, strPagename);
			} else {

				ALMFunctions.ThrowException("Dropdown", strLabel + " dropdown should be enabled in the " + strPagename,
						strLabel + " dropdown is not enabled in the " + strPagename, true);
			}
		}
	}

	public void searchAndSelectRecord(String strSearchLabelName, String strColumnName, String strValue,
			String strPageName) {
		selectListItem(new Common(strSearchLabelName).select, lngMinTimeOutInSeconds, new String[] { strColumnName },
				strSearchLabelName, strPageName, "Value");
		enterTextbox(strSearchLabelName, strValue, strPageName);
		driver.capture(strSearchLabelName);
		sendkeys(new Common(strSearchLabelName).textbox, lngMinTimeOutInSeconds, Keys.ENTER, strSearchLabelName,
				strPageName);
		driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
		driverUtil.waitUntilStalenessOfElement(new Common(strSearchLabelName).textbox, strPageName);
	}

	public void verifyStatus() {
		String strValue = dataTable.getExpectedResult("Status_Verification");
		verifyStatus(strValue, "Status verification page");
		driver.switchTo().defaultContent();
	}

	public void verifySearchDropdownValues() {
		ArrayList<String> strExpectedDropdownValues = new ArrayList<String>();
		String strListOfActualDropDownValues = "";
		String strListOfExpectedDropDownValues = "";
		String strActualValue = "";

		String[] strInputParameter = dataTable.getExpectedResult("Search_Verification").split(strExclamation);
		String strLabel = StringUtils.substringAfter(strInputParameter[0], strEqualto);
		String[] strValue = StringUtils.substringAfter(strInputParameter[1], strEqualto).split(";");
		int intExpectedSize = strValue.length;
		int intActualSize = 0;

		for (String strExpected : strValue) {
			strExpectedDropdownValues.add(strExpected);
			strListOfExpectedDropDownValues = strListOfExpectedDropDownValues.concat(strExpected + "<br>");
		}
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Search Page");
		Select dropDownList = new Select(driver.findElement(new Common(strLabel).select));

		List<WebElement> allOptions = dropDownList.getOptions();
		intActualSize = allOptions.size();
		for (WebElement strDropdownValues : allOptions) {
			strActualValue = strDropdownValues.getText();
			strListOfActualDropDownValues = strListOfActualDropDownValues.concat(strActualValue + "<br>");
			for (String strExpected : strValue) {
				if (strActualValue.equals(strExpected)) {
					break;
				}
			}

		}
		if (String.valueOf(intActualSize).equals(String.valueOf(intExpectedSize))) {
			report.updateTestLog(strLabel + " Dropdown",
					"List of value should be display in the " + strLabel + " dropdown is <br>"
							+ strListOfExpectedDropDownValues + "<br>" + "<br>" + "List of value is displayed in the "
							+ strLabel + " dropdown is <br>" + strListOfActualDropDownValues,
					Status.PASS);

			ALMFunctions.UpdateReportLogAndALMForPassStatus(strLabel + " Dropdown",
					"List of value should be display in the " + strLabel + " dropdown is <br>"
							+ strListOfExpectedDropDownValues,
					"List of value is displayed in the " + strLabel + " dropdown is <br>"
							+ strListOfActualDropDownValues,
					true);

		} else {
			report.updateTestLog(strLabel + " Dropdown",
					"List of value should be display in the " + strLabel + " dropdown is <br>"
							+ strListOfExpectedDropDownValues + "<br>" + "<br>" + "List of value is displayed in the "
							+ strLabel + " dropdown is <br>" + strListOfActualDropDownValues,
					Status.FAIL);

			ALMFunctions.ThrowException(strLabel + " Dropdown",
					"List of value should be display in the " + strLabel + " dropdown is <br>"
							+ strListOfExpectedDropDownValues,
					"List of value is displayed in the " + strLabel + " dropdown is <br>"
							+ strListOfActualDropDownValues,
					true);

		}

	}
	public void pagescroll(String strCase, String strLabel, String strPageName) {
		By locator = null;
		switch (strCase.toLowerCase()) {
		case "textbox":
			locator = new Common(strLabel).textbox;
			break;
		case "tab":
			locator = new Common(strLabel).tab;
			break;
		case "tabtext":
			locator = new Common(strLabel).tabText;
			break;	
		}
		pagescroll(locator, strPageName);
	}
	
	public void verifyFieldExist(String strLabel, String strValue, String strPageName) {
		if(objectExists(new uimap.Common(strLabel).textarea, "isDisplayed", timeOutInSeconds, strLabel, strValue, strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify " +strLabel+ "Exist", strLabel+ " should be displayed as expected", strLabel+ " is displayed successfully", true);
		}
	}
	public void verifyStatus(String strValues, String strPageName) {
		String strLabel = "";
		String strValue = "";
		String strActualValue = "";
		boolean blnRecordFound = false;
		String[] strInputValue = strValues.split(strExclamation);
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);

		for (String strLabelAndValue : strInputValue) {
			strLabel = StringUtils.substringBefore(strLabelAndValue, strEqualto);
			strValue = StringUtils.substringAfter(strLabelAndValue, strEqualto).trim();
			uimap.Common locator = new Common(strLabel);
			inner:if (objectExists(locator.readonlyFiled, "isDisplayed", lngMinTimeOutInSeconds, strLabel,
					"Read Only Textbox", strPageName, false)) {
				strActualValue = getAttributeValue(locator.readonlyFiled, lngPagetimeOutInSeconds, "textContent",strLabel, strPageName).replace("_", " ").trim();
				if(!(strActualValue.length()>0)) {
					
					if(!(strValue.contains("_")))
					{
					strActualValue = getAttributeValue(locator.readonlyFiled, lngPagetimeOutInSeconds, "value",strLabel, strPageName).replace("_", " ").trim();
					}
					else
					{
						strActualValue = getAttributeValue(locator.readonlyFiled, lngPagetimeOutInSeconds, "value",strLabel, strPageName).trim();
					}
				}
				blnRecordFound=true;
				break inner;

			} else if (objectExists(locator.select, "isDisplayed", lngMinTimeOutInSeconds, strLabel,
					"Select", strPageName, false)) {
				Select sel = new Select(driver.findElement(locator.select));
				WebElement option = sel.getFirstSelectedOption();
				strActualValue = option.getText().trim();
				blnRecordFound=true;
				break inner;
			}else if(objectExists(locator.textbox, "isDisplayed", lngMinTimeOutInSeconds, strLabel,
					"Textbox", strPageName, false)) {
				
				strActualValue = getAttributeValue(locator.textbox, lngPagetimeOutInSeconds, "value",
						strLabel, strPageName).trim();
				blnRecordFound=true;
				break inner;
			} else if (objectExists(locator.textarea, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "Textarea",
					strPageName, false)) {

				strActualValue = getAttributeValue(locator.textarea, lngPagetimeOutInSeconds, "value", strLabel,
						strPageName).replaceAll(String.valueOf((char) 160), " ").trim();
				blnRecordFound = true;
				break inner;
			}
			else if (objectExists(locator.readonlyTextContent, "isDisplayed", lngMinTimeOutInSeconds, strLabel,
					"TextContent", strPageName, false)) {

				strActualValue = getText(locator.readonlyTextContent, lngPagetimeOutInSeconds, strLabel, strPageName);
				blnRecordFound = true;
				break inner;
			}
			//Aparna 31-Dec-2021
            else if (objectExists(new uimap.Common(strLabel).textContentBodyFrame, "isEnabled", lngMinTimeOutInSeconds, "switchcardframe", "Frame", strPageName,false)) 
            {
                  frameSwitchAndSwitchBack(new uimap.Common(strLabel).textContentBodyFrame, "switchcardframe", strPageName);
            
                  if (objectExists(locator.textContentBody, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "Content Body",
                         strPageName, false)) {
                  
//                strActualValue = getAttributeValue(locator.textContentBody, lngPagetimeOutInSeconds, "value", strLabel,
//                              strPageName).replaceAll(String.valueOf((char) 160), " ").trim();
                  strActualValue=driver.findElement(new Common(strLabel).textContentReadonly).getText();     
                  blnRecordFound = true;
                  
                  }
                  frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
                  break inner;
            }
			
            else if (objectExists(locator.pausetimer, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "Textarea",
            		strPageName, false)) {

            		 strActualValue = driver.findElement(locator.pausetimer).getText();
            		blnRecordFound = true;
            		break inner;
			

            }
            else if(objectExists(locator.sparcgetdropdownvalue, "isDisplayed", lngMinTimeOutInSeconds, strLabel,
            		"Textbox", strPageName, false)) {

            		strActualValue=driver.findElement(new Common(strLabel).sparcgetdropdownvalue).getText();
            		blnRecordFound=true;
            		break inner;
            		}
            else if (objectExists(locator.textContentReadonly, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "Content Body",
            		strPageName, false)) {



            		strActualValue=driver.findElement(new Common(strLabel).textContentReadonly).getText();
            		blnRecordFound = true;
            		break inner;
            }
			else {

				ALMFunctions.ThrowException(strLabel,
						strLabel + " label should be display in the " + strPageName + " page",
						"No Such " + strLabel + " label is found in the " + strPageName + " page", true);
			}
			
			if(blnRecordFound) 
			{
			pass:if (strActualValue.equalsIgnoreCase(strValue)) {
				report.updateTestLog(strLabel, "Expected " + strLabel + " value should be display as : <br>" + strValue
						+ "<br>" + "Actual " + strLabel + " value is displayed as : <br>" + strActualValue, Status.PASS);
				ALMFunctions.UpdateReportLogAndALMForPassStatus(strLabel,
						strLabel + " value should be display as : " + strValue,
						strLabel + " value is displayed as : " + strActualValue, true);
				break pass;

			}else if (strActualValue.contains(strValue)) {
				report.updateTestLog(strLabel, "Expected " + strLabel + " value should be display as : <br>" + strValue
						+ "<br>" + "Actual " + strLabel + " value is displayed as : <br>" + strActualValue, Status.PASS);
				ALMFunctions.UpdateReportLogAndALMForPassStatus(strLabel,
						strLabel + " value should be display as : " + strValue,
						strLabel + " value is displayed as : " + strActualValue, true);
				break pass;
			} 
			
			else {
				report.updateTestLog(strLabel, "Expected " + strLabel + " value should be display as : <br>" + strValue
						+ "<br>" + "Actual " + strLabel + " value is displayed as : <br>" + strActualValue, Status.FAIL);
				ALMFunctions.UpdateReportLogAndALMForFailStatus(strLabel,
						strLabel + " value should be display as : " + strValue,
						strLabel + " value is displayed as : " + strActualValue, true);

			}
		}

		}

	}

	public void verifyComments() {
		String strPageName = "Comments verification Tab";
		String[] strValues = dataTable.getExpectedResult("Verify_Comments").split(strExclamation);

		for (String strValue : strValues) {
			String[] strInputValue = strValue.split(strSemicolon);
			String strTabName = StringUtils.substringAfter(strInputValue[0], strEqualto);
			String strExpectedValue = StringUtils.substringAfter(strInputValue[1], strEqualto);
			String strActualValue = "";
			boolean blnRecordFound = false;
			frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);
			tab(strTabName, strPageName);
			if (objectExists(Common.commentsList, "isDisplayed", lngMinTimeOutInSeconds, "Activities Comments", "List",
					strPageName, false)) {
				pagescroll(Common.commentsList, strPageName);
				List<WebElement> listElement = driver.getWebDriver().findElements(Common.commentsList);

				for (WebElement list : listElement) {
					strActualValue = list.getText();
					if (strActualValue.contains(strExpectedValue)) {
						report.updateTestLog("Activities Comments", "Expected : <br>" + strExpectedValue
								+ " activities comments should be display in the page" + "<br>" + "Actual : <br>"
								+ strActualValue + " activities comments is displayed in the page", Status.DONE);
						ALMFunctions.UpdateReportLogAndALMForPassStatus("Activities Comments",
								strExpectedValue + " activities comments should be display in the " + strPageName,
								strActualValue + " activities comments is displayed in the " + strPageName, true);

						blnRecordFound = true;
						break;
					}

				}
				if (!blnRecordFound) {
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Activities Comments",
							strExpectedValue + " activities comments should be display in the " + strPageName,
							"Error - " + " Activities comments : " + strExpectedValue + " is not displayed in the "
									+ strPageName,
							true);

				}

			} else {
				ALMFunctions.ThrowException("Activities Comments",
						"Activities comments element list should be display in the page",
						"No such element list is found in the page ", true);

			}

		}
		driver.switchTo().defaultContent();
	}

	/**
	 * Method to find record in Table and select record.
	 * 
	 * @param by                The {@link WebDriver} locator used to identify the
	 *                          header value and row count
	 * 
	 * @param strInputparameter The value to be selected within the table.
	 * @param pageName          Page Name in which the list box is available
	 */
	public void findRecord(By header, By row, String strInputparameter, boolean blnVerifyTableReadAccess,
			String strTableName) {

		LinkedHashMap<String, String> strLabelAndValue = new LinkedHashMap<>();
		LinkedHashMap<String, Integer> intIndex = new LinkedHashMap<>();
		String strVerifyTableValue = "";
		String strLabel = "";
		String strValue = "";
		String strActValue = "";
		int intIndexValue = 0;
		WebElement eleRows = null;
		String[] strInput = strInputparameter.split(strExclamation);

		for (String strRowValue : strInput) {
			String[] arrParameters = StringUtils.split(strRowValue, strSemicolon);
			for (String strInputValue : arrParameters) {
				switch (StringUtils.substringBefore(strInputValue, strEqualto).toLowerCase()) {

				case "element label":
					strLabel = StringUtils.substringAfter(strInputValue, strEqualto);
					break;
				case "element value":
					strValue = StringUtils.substringAfter(strInputValue, strEqualto);
					break;
				default:
					ALMFunctions.ThrowException("Test Data",
							"Only Pre-Defined Form Options must be provided in the test data sheet",
							"Error - Unhandled Form Options " + StringUtils.substringBefore(strInputValue, strEqualto),
							false);
					break;
				}
				strLabelAndValue.put(strLabel, strValue);
			}

		}

		for (String strColumnName : strLabelAndValue.keySet()) {
			int intColumnIndex = getColumnIndex(strColumnName, header, strTableName, false, false);

			if (objectExists(Common.tableHeader_1, "isEnabled", lngMinTimeOutInSeconds, strColumnName, "Header",
					strTableName, false)) {
				intColumnIndex = intColumnIndex + 1;

			} else {
				intColumnIndex = intColumnIndex + 2;

			}
			if (intColumnIndex != 0) {
				intIndex.put(strColumnName, intColumnIndex);
			} else {
				ALMFunctions.ThrowException("Get index of column name",
						"Expected column name as " + strColumnName + " shall be displayed",
						"Expected column name as " + strColumnName + " is not displayed", true);
			}
		}
		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		driverUtil.waitUntilStalenessOfElement(row, strTableName);
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strTableName + " table row", strTableName + " table row should be displayed",
					strTableName + " table row are not displayed", true);
			ALMFunctions.ThrowException("Verify table rows in " + strTableName + "",
					"" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			outerloop: do {

				ALMFunctions.Screenshot();
				if (intCurrentPage != 1) {

					if (objectExists(uimap.Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(uimap.Common.nextPage, timeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(uimap.Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}
				listtableRow = driver.getWebDriver().findElements(row);
				int i = 0;

				for (WebElement rows : listtableRow) {
					strVerifyTableValue = "";
					i = 0;
					for (String strKey : strLabelAndValue.keySet())

					{
						strActValue = rows
								.findElement(By.xpath(
										".//*[local-name()='th' or local-name()='td'][" + intIndex.get(strKey) + "]"))
								.getText().trim();

						if (strActValue.equals(strLabelAndValue.get(strKey))) {
							i = i + 1;
							intIndexValue = intIndex.get(strKey);
							eleRows = rows;
							strVerifyTableValue = strVerifyTableValue.concat(strKey + "=" + strActValue + " ; ");
							blnFound = true;

						}

					}

					if (blnFound && i == strLabelAndValue.keySet().size()) {
						if (blnFound && strLabelAndValue.keySet().size() > 1) {
							tableCheckBox(strActValue, eleRows, strTableName + " table");
							/*WebElement eleClick = eleRows
									.findElement(new Common(String.valueOf(intIndexValue)).tableLink);*/
							
							WebElement eleClick = eleRows.findElement(Common.tblLink);
							driver.capture(strTableName);
							click(eleClick, lngMinTimeOutInSeconds, strActValue, "Link", strTableName + " table", true);
							blnClick = true;
							break outerloop;
						} else if (blnFound && strLabelAndValue.keySet().size() == 1) {
							tableCheckBox(strActValue, eleRows, strTableName + " table");
							WebElement eleClick = eleRows
									.findElement(new Common(String.valueOf(intIndexValue)).tableLink2);
							driver.capture(strTableName);
							String strText = eleClick.getText();
							if (strText.length() > 0) {
								click(eleClick, lngMinTimeOutInSeconds, strActValue, "Link", strTableName + " table",
										true);
								blnClick = true;
								break outerloop;

							} else {
								tableCheckBox(strActValue, eleRows, strTableName + " table");
								continue;
							}
						}
						/*
						 * else if(blnFound && blnVerifyTableReadAccess) {
						 * ALMFunctions.UpdateReportLogAndALMForPassStatus( "Table Data verification",
						 * "Row should have read access in the "+strTableName+ " table",
						 * "Row had read access and navigated successfully in the "
						 * +strTableName+" table" , true); blnClick = true; break outerloop; }
						 */

					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnClick && blnFound && blnVerifyTableReadAccess) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Table Data verification",
						"Row should have read access in the " + strTableName + " table",
						"Row had read access and navigated successfully in the " + strTableName + " table", true);
				blnClick = true;
			}
			if (blnRecordNotFound)

			{

				ALMFunctions.ThrowException(strActValue,
						"Created From Trigger : " + strActValue + " in " + strTableName
								+ " Table row should be clicked",
						"Error - Specified Record " + strActValue + " is not found in the " + strTableName
								+ " table with expected condtion",
						true);

			}

			if (!blnClick) {

				ALMFunctions.ThrowException("Click on " + strTableName + " Table row",
						"Created From Trigger : " + strActValue + " in " + strTableName
								+ " Table row should be clicked on " + strTableName + "",
						"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
			}

		}

	}

	public void findRecordAndSelect(String strColumnName, String strValue, String strPageName) {
		String strActValue = "";
		WebElement eleRows = null;
		By row = Common.tableRow;
		String strTableName = "LookUp Table";
		int intColumnIndex = getColumnIndex(strColumnName, Common.tableHeader_1, strTableName, false, false) + 1;
		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strColumnName + " shall be displayed",
					"Expected column name as " + strColumnName + " is not displayed", true);
		}
		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strValue, strValue + " table row should be displayed",
					strValue + " table row are not displayed", true);
			ALMFunctions.ThrowException(strValue, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				ALMFunctions.Screenshot();
				if (intCurrentPage != 1) {

					if (objectExists(uimap.Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(uimap.Common.nextPage, timeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(uimap.Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}
				// driverUtil.waitUntilStalenessOfElement(row, strTableName);

				listtableRow = driver.getWebDriver().findElements(row);

				for (WebElement rows : listtableRow) {

					strActValue = rows
							.findElement(
									By.xpath(".//*[local-name()='th' or local-name()='td'][" + intColumnIndex + "]"))
							.getText().trim();

					if (strActValue.equals(strValue)) {

						eleRows = rows;
						blnFound = true;

					}

					if (blnFound) {
						WebElement eleClick = eleRows.findElement(new Common(String.valueOf(intColumnIndex)).tableLink);
						//WebElement eleClick = eleRows.findElement(Common.tblLink);
						driver.capture(strTableName);
						eleClick.click();
						blnClick = true;
						break;

					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnRecordNotFound) {
				ALMFunctions.ThrowException(strValue, strValue + " value should be display in table row",
						"Error - Specified Record " + strValue + " is not found in the " + strTableName + " table",
						true);

			}

			if (blnClick) {
				report.updateTestLog("Click on " + strActValue + " in table row",
						strActValue + " link is clicked in " + strTableName, Status.DONE);

			} else {
				ALMFunctions.ThrowException("Click on " + strValue + " Table row",
						"Created From Trigger : " + strActValue + " in " + strTableName
								+ " Table row should be clicked on " + strTableName + "",
						"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
			}

		}

	}

	/**
	 * Method to enter a value in a text box
	 * 
	 * @param locator, By object of the control
	 * @param strFieldName, The name of the text box
	 * @param strValue, value to be entered in a text box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void enterTextbox(String strFieldName, String strValue, String strPageName)

	{
		if(strFieldName.contains("Form1"))
		{
			String[] fieldName = strFieldName.split("!");
			strFieldName = fieldName[0];
			uimap.Common textbox = new uimap.Common(strFieldName);
			pagescroll(textbox.formtextBox, strPageName);
			clear(textbox.formtextBox, lngPagetimeOutInSeconds, strFieldName, strPageName);
			click(textbox.formtextBox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
			sendkeys(textbox.formtextBox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
			sendkeys(textbox.formtextBox, lngPagetimeOutInSeconds, Keys.ENTER, strFieldName, strPageName);
		}
		else if(strFieldName.contains("Form2"))
		{
					String[] fieldName = strFieldName.split("!");
			strFieldName = fieldName[0];
			uimap.Common textbox = new uimap.Common(strFieldName);
			pagescroll(textbox.formtextBox1, strPageName);
			clear(textbox.formtextBox1, lngPagetimeOutInSeconds, strFieldName, strPageName);
			click(textbox.formtextBox1, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
			sendkeys(textbox.formtextBox1, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
		}
		
		else if(strFieldName.contains("Tabular"))
		{
					String[] fieldName = strFieldName.split("!");
			strFieldName = fieldName[0];
			uimap.Common textbox = new uimap.Common(strFieldName);
			pagescroll(textbox.tabTextBox, strPageName);
			clear(textbox.tabTextBox, lngPagetimeOutInSeconds, strFieldName, strPageName);
			click(textbox.tabTextBox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
			sendkeys(textbox.tabTextBox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
			sendkeys(textbox.tabTextBox, lngPagetimeOutInSeconds, Keys.ENTER, strFieldName, strPageName);
		}
		else if(strFieldName.contains("Placeholder"))
		{


		pagescroll(Common.placeholder, strPageName);
		clear(Common.placeholder, lngPagetimeOutInSeconds, strFieldName, strPageName);
		click(Common.placeholder, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
		sendkeys(Common.placeholder, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
		sendkeys(Common.placeholder, lngPagetimeOutInSeconds, Keys.ENTER, strFieldName, strPageName);
		}
		else
		{
			uimap.Common textbox = new uimap.Common(strFieldName);
			pagescroll(textbox.textbox, strPageName);
			clear(textbox.textbox, lngPagetimeOutInSeconds, strFieldName, strPageName);
			//click(textbox.textbox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
			sendkeys(textbox.textbox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
			sendkeys(textbox.textbox, lngPagetimeOutInSeconds, Keys.ENTER, strFieldName, strPageName);
			sendkeys(textbox.textbox, lngPagetimeOutInSeconds, Keys.TAB, strFieldName, strPageName);
		}
	}
		//siva 11/16
	/**
	 * Method to enter a value in a text box
	 * 
	 * @param locator,      By object of the control
	 * @param strFieldName, The name of the text box
	 * @param strValue,     value to be entered in a text box
	 * @param strPageName,  Page Name in which the control is available
	 * @return No return value
	 */

	public void enterSparcTextbox(String strFieldName, String strValue, String strPageName)

	{
		if(strFieldName.contains("!")) {
			String[] strinputPara = strFieldName.split("!");
			
			String WindowName = strinputPara[0];
			String TextBoxName = strinputPara[1];
			uimap.Common button = new uimap.Common(WindowName, TextBoxName);
			
			
		if (objectExists(button.dialogTextBox, "isDisplayed", timeOutInSeconds, strValue, "TextBox", strPageName,false)) {
					
					clear(button.dialogTextBox, lngPagetimeOutInSeconds, strFieldName, strPageName);
					click(button.dialogTextBox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
					sendkeys(button.dialogTextBox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
					//sendkeys(button.dialogTextBox, lngMinTimeOutInSeconds, Keys.ENTER, strValue, strPageName);
					}
			}
		else if(strFieldName.equalsIgnoreCase("sparcSearchTextbox")) {
			clear(Common.sparcPortTextbox, lngPagetimeOutInSeconds, strFieldName, strPageName);
			click(Common.sparcPortTextbox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
			sendkeys(Common.sparcPortTextbox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
			sendkeys(Common.sparcPortTextbox, lngMinTimeOutInSeconds, Keys.ENTER, strValue, strPageName);
			}
	else{
		uimap.Common textbox = new uimap.Common(strFieldName);
	
//		pagescroll(textbox.textbox, strPageName);
		clear(textbox.sparctextbox, lngPagetimeOutInSeconds, strFieldName, strPageName);
		click(textbox.sparctextbox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
		sendkeys(textbox.sparctextbox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
		sendkeys(textbox.sparctextbox, lngMinTimeOutInSeconds, Keys.ENTER, strValue, strPageName);
	}
	}
	public void  clearTextbox(String strFieldName, String strValue, String strPageName)

	{
		uimap.Common textbox = new uimap.Common(strFieldName);
		click(textbox.textbox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
		clear(textbox.textbox, lngPagetimeOutInSeconds, strFieldName, strPageName);
		clear(textbox.textbox, lngPagetimeOutInSeconds, strFieldName, strPageName);
		sendkeys(textbox.sparctextbox, lngMinTimeOutInSeconds, Keys.TAB, strValue, strPageName);
		driverUtil.waitFor(5000, strFieldName, strValue, strPageName);
		String strGetEnteredValue = getAttributeValue(new Common(strFieldName).readonlyTextbox,lngPagetimeOutInSeconds, "value", strFieldName, strPageName);
		if(strGetEnteredValue.isEmpty()) {
			//nocode
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify textbox is cleared",
					"Textbox should be cleared ", strValue+" value is cleared",true);
		}
	}
	
	public void verifyButtonNotExist(String strButtonLabel, String strPageName) {
		{
			By button = null;
			String strPageView = getCurrentPageView();
			switch (strPageView) {
			case "SPARC Dev":
				button = new uimap.Common(strButtonLabel).button;
				break;
			case "ESC Portal":
				button = new uimap.EscPortal(strButtonLabel).escbutton;
				break;
			default:
				ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
						"Unhandled - View - " + strPageView, false);
				break;
			}

			driver.capture(strPageName);
			if(!objectExists(button, "isDisplayed", timeOutInSeconds, strButtonLabel, "Button", strPageName, false)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Button Not Exist",
						"Button should be displayed as expected", strButtonLabel+" button is not displayed",false);
			}
		}
	}
			
	public void comboTextBox(String strLabel, String strValue, String strPageName) {
		uimap.Common textbox = new uimap.Common(strLabel);
//		pagescroll(textbox.textbox, strPageName);
		clear(textbox.comboTextBox, lngPagetimeOutInSeconds, strLabel, strPageName);
		click(textbox.comboTextBox, lngPagetimeOutInSeconds, strValue, strLabel, strPageName, false);
		sendkeys(textbox.comboTextBox, lngPagetimeOutInSeconds, strValue, strLabel, strPageName);
		sendkeys(textbox.comboTextBox, lngMinTimeOutInSeconds, Keys.ENTER, strValue, strPageName);
		
	}

	/**
	 * Method to enter a value in a dialog text box
	 * 
	 * @param strWindowName, Name of the Window name
	 * @param strValue,      value to be entered in a text box
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */

	public void dialogTextbox(String strWindowName, String strValue, String strPageName) {
		uimap.Common textbox = new uimap.Common(strWindowName);
		
		if(objectExists(textbox.dialogTextbox, "isDisplayed", lngMinTimeOutInSeconds, strWindowName, strValue, strPageName, false)) {
			click(textbox.dialogTextbox, lngPagetimeOutInSeconds, strValue, strWindowName, strPageName, false);
			sendkeys(textbox.dialogTextbox, lngPagetimeOutInSeconds, strValue, strWindowName, strPageName);
		} else if(objectExists(textbox.textBoxInput, "isDisplayed", lngMinTimeOutInSeconds, strWindowName, strValue, strPageName, false)){
			click(textbox.textBoxInput, lngPagetimeOutInSeconds, strValue, strWindowName, strPageName, false);
			sendkeys(textbox.textBoxInput, lngPagetimeOutInSeconds, strValue, strWindowName, strPageName);
			sendkeys(textbox.textBoxInput, timeOutInSeconds, Keys.ENTER, strWindowName, strPageName);
		}
		
	}

	/**
	 * Method to enter a value in a text area
	 * 
	 * @param locator, By object of the control
	 * @param strSectionLabel, section label name
	 * @param strFieldName, The name of the text box
	 * @param strValue, value to be entered in a text box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void enterTextArea(String strFieldName, String strValue, String strPageName) {

		if(strFieldName.contains("Form"))
		{
			String[] fieldName = strFieldName.split("!");
			strFieldName = fieldName[0];
			By textArea = null;
			textArea = new uimap.Common(strFieldName).formtextarea;
			pagescroll(textArea, strPageName);		
			//click(textArea, lngPagetimeOutInSeconds, strFieldName, "Text Area", strPageName, false);
			clear(textArea, lngPagetimeOutInSeconds, strFieldName, strPageName);
			sendKeysByJsExec(textArea, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, true);
			sendkeys(textArea, lngPagetimeOutInSeconds, Keys.RETURN, strFieldName, strPageName);
		}
		else {
				By textArea = null;
				String strPageView = getCurrentPageView();
				switch (strPageView) {
				case "SPARC Dev":
					textArea = new uimap.Common(strFieldName).textarea;
					
					break;
				case "ESC Portal":
					textArea = new uimap.EscPortal(strFieldName).escTextarea;
					break;
				default:
					ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
							"Unhandled - View - " + strPageView, false);
					break;
				}
		if(!driverUtil.waitUntilElementVisible(textArea, lngMinTimeOutInSeconds, strFieldName, "Text Area", strPageName, false)) {
			pagescroll(textArea, strPageName);
		}
				pagescroll(textArea, strPageName);		
				click(textArea, lngPagetimeOutInSeconds, strFieldName, "Text Area", strPageName, false);
				clear(textArea, lngPagetimeOutInSeconds, strFieldName, strPageName);
				sendKeysByJsExec(textArea, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, true);
				sendkeys(textArea, lngPagetimeOutInSeconds, Keys.RETURN, strFieldName, strPageName);
				}
	}
	public void enterTextContentBody(String strLabel, String strValue, String strPageName) {
		frameSwitchAndSwitchBack(new uimap.Common(strLabel).textContentBodyFrame, "switchcardframe", strPageName);
		if(objectExists(new uimap.Common(strLabel).textContentBody, "isDisplayed", timeOutInSeconds, strLabel, "Content Body", strPageName, false)) {
			click(new uimap.Common(strLabel).textContentBody, timeOutInSeconds, strLabel, "Content Body", strPageName, true);
			sendkeys(new uimap.Common(strLabel).textContentBody, timeOutInSeconds, strValue, strLabel, strPageName);
		}
		frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
	}

	public void datePicker(String strLabel, String strDateAndMonth, String strPageName) {
		clear(new uimap.Common(strLabel).textbox, lngPagetimeOutInSeconds, "Hour", strPageName);
		String strDateValue = StringUtils.substringBefore(strDateAndMonth, "/");
		String strMonthValue = StringUtils.substringAfter(strDateAndMonth, "/");
		SimpleDateFormat formatter=new SimpleDateFormat("d/MMM"); 
		 Date dat;
		try {
			dat = formatter.parse(strDateAndMonth);
			 Calendar c = Calendar.getInstance();
			 c.setTime(dat);
			 String text  = new SimpleDateFormat("M").format(dat);
			 int inputmonthval = Integer.parseInt(text); 
			java.util.Date date= new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int currentMonth = cal.get(Calendar.MONTH)+1;
			click(new Common(strLabel).datePicker, lngMinTimeOutInSeconds, strLabel, "Button", strPageName, true);
			if (objectExists(Common.monthPicker, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Date Picker",
				strPageName, false)) {
			 
			 int decrementMonth=0;
			 int incrementMonth=0;
			 	if(currentMonth==inputmonthval) {
					report.updateTestLog("Select Month", "Month is selected as " + strMonthValue, Status.DONE);
					//blnMonthfound = true;
					
				}
				else if(currentMonth<inputmonthval)
				{
					for(int i=1;i<=inputmonthval-currentMonth;i++){
						click(Common.DatePickerNextbutton, lngMinTimeOutInSeconds, "Next", "Button", strPageName, false);
						incrementMonth++;
						driverUtil.waitFor(CtrlTimeOut, "Next", "Button", strPageName);
						
					}
					
					if(incrementMonth==inputmonthval-currentMonth)
					{
						report.updateTestLog("Select Month", "Month is selected as " + strMonthValue, Status.DONE);
					}
				}
				else if(currentMonth>inputmonthval) {
					for(int i=1;i<=currentMonth-inputmonthval;i++){
						
						click(Common.DatePickerPreviousbutton, lngMinTimeOutInSeconds, "Next", "Button", strPageName, false);
						driverUtil.waitFor(CtrlTimeOut, "Next", "Button", strPageName);
						 decrementMonth++;
						 
						
						
					}
					if(decrementMonth==currentMonth-inputmonthval)
					{
						report.updateTestLog("Select Month", "Month is selected as " + strMonthValue, Status.DONE);
					}
					
					
				}
				

			List<WebElement> columns = driver.getWebDriver().findElements(Common.dateSelection);
			for (WebElement cell : columns) {

				String strDate = cell.getText().trim();
				if (strDate.equals(strDateValue.trim())) {
					cell.findElement(By.xpath(".//a")).click();
					report.updateTestLog("Select Date", "Date is selected as " + strDateValue, Status.DONE);
					break;
				}
			}
			Date date1 = new Date();
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			String pstDate = df.format(date1);
			
			String[] PSTTimeValue = pstDate.split(":");
			String strPSTHour = PSTTimeValue[0];
			String strPSTMinValue = PSTTimeValue[1];
			String strPSTSecValue = PSTTimeValue[2];
		
		    int min = Integer.parseInt(strPSTMinValue); 
		    int updatedMin = 0;
		  //Aparna
		    int hour = Integer.parseInt(strPSTHour); 
		 //   
		    if(testparameters.getCurrentTestcase().contains("Emergency Work Flow"))
		    {
		    	 updatedMin = min+7;
		    	 if(updatedMin>=60) {
		    		 updatedMin= updatedMin-60;
		    	 }
		    }
		    else if(testparameters.getCurrentTestcase().contains("Runbook-013"))
		    {
		    	//Aparna
		    	int updateHr=0;
		    	//int	updateHr = hour+8;
		    	hour=hour+8;
		    	 if(hour>=24) {
		    		 hour= hour-24;
		    		 }
		    	updatedMin = min+2;
		    	 if(updatedMin>=60) {
		    		 updatedMin= updatedMin-60;
		    		 }
		    }
		    else if(testparameters.getCurrentTestcase().contains("TC058_Functionality_MainPage"))
		    {
		    	 updatedMin = min+3;
		    	 if(updatedMin>=60) {
		    		 updatedMin= updatedMin-60;
		    	 }
		    }
		    else
		    {
		    	 updatedMin = min+1;
		    	 if(updatedMin>=60) {
		    		 updatedMin= updatedMin-60;
		    	 }
		    	// updateHr=hour;
		    }
		    String mins = String.valueOf(updatedMin);
		  //Aparna
		    String hours = String.valueOf(hour);
		    //String secs = Secs.format(date1); 
			    if(objectExists(Common.datePickerHour, "isDisplayed", lngPagetimeOutInSeconds, strPSTSecValue, mins, strPageName, false))
			    {
					clear(Common.datePickerHour, lngPagetimeOutInSeconds, "Hour", strPageName);
					//sendkeys(Common.datePickerHour, lngPagetimeOutInSeconds, strPSTHour, "Hour", strPageName);
					sendkeys(Common.datePickerHour, lngPagetimeOutInSeconds, hours, "Hour", strPageName);
					clear(Common.datePickerMinute, lngPagetimeOutInSeconds, "Minutes", strPageName);
					sendkeys(Common.datePickerMinute, lngPagetimeOutInSeconds, mins, "Minutes", strPageName);
					clear(Common.datePickerSecond, lngPagetimeOutInSeconds, "Seconds", strPageName);
					sendkeys(Common.datePickerSecond, lngPagetimeOutInSeconds, strPSTSecValue, "Seconds", strPageName);
					clickByJS(Common.dateMonthOkButton, lngMinTimeOutInSeconds, "OK", "Button", strPageName, true);
					driverUtil.waitFor(lngMinTimeOutInSeconds, "", "", strPageName);
			    }
		} else {

			ALMFunctions.ThrowException("Date picker", "Date picker should be display in the " + strPageName,
					"Date picker is not displayed in the " + strPageName, true);
		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mailTextbox(String strFieldName, String strValue, String strPageName) {
		uimap.Common textbox = new uimap.Common(strFieldName);

		// frameSwitchAndSwitchBack(new Common(strFieldName).mailframe,
		// "switchframe", strPageName);
		driver.switchTo().frame(driver.findElement(new Common(strFieldName).mailframe));
		pagescroll(textbox.mailTextbox, strPageName);
		clear(textbox.mailTextbox, lngMinTimeOutInSeconds, strFieldName, strPageName);
		clickByJS(textbox.mailTextbox, lngMinTimeOutInSeconds, strFieldName, "Textbox", strPageName, false);
		mouseOverandEnter(textbox.mailTextbox, lngMinTimeOutInSeconds, strFieldName, strValue, "Textbox", strPageName,
				true);
	}
	public void dialogDatePicker(String strLabel, String strValue, String strPageName) {
		if(objectExists(new uimap.Common(strLabel).calenderClick, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "Date Picker", strPageName, false)) {
			click(new uimap.Common(strLabel).calenderClick, lngMinTimeOutInSeconds, strLabel, strValue, strPageName, null);
		}
	}

	private void tableCheckBox(String strActValue, WebElement eleRows, String strTableName) {

		if (objectExists(Common.tableCheckbox, "isEnabled", lngMinTimeOutInSeconds, strActValue, "CheckBox",
				strTableName, false)) {
			WebElement eleCheckbox = eleRows.findElement(Common.tableCheckbox);
			driverUtil.waitUntilStalenessOfElement(eleCheckbox, strTableName);
			clickByJS(eleCheckbox, lngMinTimeOutInSeconds, strActValue, "CheckBox", strTableName, true);
		}
	}

	

	/**
	 * @param strSheetName     - Name of the Sheet in which data to be concatenated
	 *                         is present
	 * @param strColumnName    - Column Name in which data to be concatenated is
	 *                         present
	 * @param strScenario      - Scenario for which test data is to be user
	 * @param strDelimiter     - Delimiter to be used during concatenation
	 * @param includeDelimiter - Boolean to include delimiter or not
	 * @return - Concatenated String
	 */
	public String getConcatenatedStringFromExcel(String strSheetName, String strColumnName,
			String strConcatenationFlagColumn, String strScenario, String strDelimiter, boolean blnIncludeDelimiter,
			boolean blnInput) {
		String strValue = "";
		String strLockFile = "Excel_Data";
		FileLockMechanism objFileLockMechanism = new FileLockMechanism(
				Long.valueOf(properties.getProperty("FileLockTimeOut")));
		FileLock objFileLock = objFileLockMechanism.SetLockOnFile(strLockFile);
		if (objFileLock != null) {
			String strFilePath = dataTable.datatablePath + Util.getFileSeparator() + dataTable.datatableName + ".xls";
			HSSFWorkbook wb = openExcelFile(strFilePath);
			wb.setForceFormulaRecalculation(true);
			HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			Sheet sheet = getSheetFromXLSWorkbook(wb, strSheetName, strFilePath);
			int intColumnIndexConcatFlag = getColumnIndex(strFilePath, strSheetName, strConcatenationFlagColumn);
			int intColumnIndex = getColumnIndex(strFilePath, strSheetName, strColumnName);
			int intStartRow = getStartRow(wb, strFilePath, strSheetName, sheet, strScenario);
			int intEndRow = getEndRow(wb, sheet, intStartRow, strSheetName);
			int intFlagWriteDataInFormSheet = getColumnIndex(strFilePath, strSheetName,
					"Flag_Write_Data_In_This_Sheet");
			int intStorageIndex = getColumnIndex(strFilePath, strSheetName, "Stored_Value");
			for (int i = intStartRow; i < intEndRow; i++) {
				if (sheet.getRow(i) != null) {
					if (getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndexConcatFlag))
							.equalsIgnoreCase("yes")) {
						if (blnInput) {
							String strTemp = getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex));
							String strStorage = "";
							strStorage = StringUtils.substringAfter(strTemp, "Storage=");
							String strValueToBeWritten = StringUtils.substringBetween(strTemp, "Element Value=",
									";Storage=");
							if (strStorage != null && strStorage.trim().length() > 0) {
								for (String strWriteParameter : StringUtils.split(strStorage, ";")) {
									dataTable.putData(StringUtils.substringBefore(strWriteParameter, "!"),
											StringUtils.substringAfter(strWriteParameter, "!"), strValueToBeWritten);
								}
							}
							if (getCellValueAsString(wb, sheet.getRow(i).getCell(intFlagWriteDataInFormSheet))
									.equalsIgnoreCase("yes")) {
								writeData(strFilePath, strSheetName, i, intStorageIndex, strValueToBeWritten);
							}
						}
						if (blnIncludeDelimiter) {
							if (i == intStartRow) {
								strValue = getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex))
										+ strDelimiter;
							} else if (i != intEndRow) {
								strValue = strValue + getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex))
										+ strDelimiter;
							} else {
								strValue = strValue + getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex));
							}
						} else {
							if (i == intStartRow) {
								strValue = getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex));
							} else {
								strValue = strValue + getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex));
							}
						}
					}
				}
			}
			try {
				wb.close();
			} catch (IOException e) {
				ALMFunctions.ThrowException("Excel Close", "Should be able to close excel file",
						"Below Exception is thrown when trying to " + "close excel file found in the path "
								+ strFilePath + "<br><br>" + e.getLocalizedMessage(),
						false);
			}
			objFileLockMechanism.ReleaseLockOnFile(objFileLock, strLockFile);
		} else {
			throw new FrameworkException("Error", "Error in getting data from excel due to file lock exception");
		}
		return strValue;
	}

	/**
	 * @param wb           - HSSFWorkbook Object
	 * @param strFilePath  - File Path of the Excel File
	 * @param strSheetName - Sheet Name in which data to be concatenated is present
	 * @param sheet        - Sheet object
	 * @param strScenario  - Scenario for which this test data is going to be used
	 * @return - Index of Start Row
	 */
	public int getStartRow(HSSFWorkbook wb, String strFilePath, String strSheetName, Sheet sheet, String strScenario) {
		boolean blnRowFound = false;
		int intTCIDColumnIndex = getColumnIndex(strFilePath, strSheetName, "TC_ID");
		int intScenarioColumnIndex = getColumnIndex(strFilePath, strSheetName, "TC_Scenario");
		int intIterationColumnIndex = getColumnIndex(strFilePath, strSheetName, "Iteration");
		int intSubIterationColumnIndex = getColumnIndex(strFilePath, strSheetName, "SubIteration");
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			if (sheet.getRow(i) != null) {
				if (getCellValueAsString(wb, sheet.getRow(i).getCell(intTCIDColumnIndex))
						.equalsIgnoreCase(testparameters.getCurrentTestcase())
						&& getCellValueAsString(wb, sheet.getRow(i).getCell(intScenarioColumnIndex))
								.equalsIgnoreCase(strScenario)
						&& Integer.valueOf(getCellValueAsString(wb,
								sheet.getRow(i).getCell(intIterationColumnIndex))) == (dataTable.currentIteration)
						&& Integer.valueOf(getCellValueAsString(wb, sheet.getRow(i)
								.getCell(intSubIterationColumnIndex))) == (dataTable.currentSubIteration)) {
					blnRowFound = true;
					return i + 2;
				}
			}
		}
		if (!blnRowFound) {
			ALMFunctions.ThrowException("Test Data", "Test Data should be found in the sheet " + sheet,
					"Error - Test Data with " + "TC_ID as " + testparameters.getCurrentTestcase() + " , TC_Scenario as "
							+ strScenario + " , Iteration as " + dataTable.currentIteration + " , SubIteration as "
							+ dataTable.currentSubIteration + " does not exists in the sheet " + strSheetName,
					false);
		}
		return 0;
	}

	/**
	 * @param wb           - HSSFWorkbook Object
	 * @param sheet        - Sheet object
	 * @param intStartRow  - Index of Start Row
	 * @param strSheetName - Sheet Name in which data to be concatenated is present
	 * @return - Index of End Row
	 */
	public int getEndRow(HSSFWorkbook wb, Sheet sheet, int intStartRow, String strSheetName) {
		boolean blnEnd = false;
		for (int i = intStartRow; i <= sheet.getLastRowNum(); i++) {
			if (sheet.getRow(i) != null) {
				if (getCellValueAsString(wb, sheet.getRow(i).getCell(0)).equalsIgnoreCase("end")) {
					blnEnd = true;
					return i;
				}
			}
		}
		if (!blnEnd) {
			ALMFunctions.ThrowException("Test Data", "Test Data with End Tag should be found in the sheet " + sheet,
					"Error - Test Data with " + "End Tag does not exists in the sheet " + strSheetName, false);
		}
		return 0;
	}

	/**
	 * @param strFilePath     - File Path of the Excel File
	 * @param strSheetName    - Sheet Name in which data to be concatenated is
	 *                        present
	 * @param intRowNumber    - Index of the Row
	 * @param intColumnNumber - Index of the Column
	 * @param strValue        - Value to be written
	 */
	public void writeData(String strFilePath, String strSheetName, int intRowNumber, int intColumnNumber,
			String strValue) {
		String strLockFile = "PutData_Lock.xls";
		FileLockMechanism objFileLockMechanism = new FileLockMechanism(
				Long.valueOf(properties.getProperty("FileLockTimeOut")));
		FileLock objFileLock = objFileLockMechanism.SetLockOnFile(strLockFile);
		if (objFileLock != null) {
			synchronized (CommonFunctions.class) {
				HSSFWorkbook wb = openExcelFile(strFilePath);
				Sheet sheet = getSheetFromXLSWorkbook(wb, strSheetName, strFilePath);
				Row row = sheet.getRow(intRowNumber);
				Cell cell = row.createCell(intColumnNumber);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(strValue);
				FileOutputStream fileOutputStream;
				try {
					fileOutputStream = new FileOutputStream(strFilePath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					throw new FrameworkException("The specified file \"" + strFilePath + "\" does not exist!");
				}
				try {
					wb.write(fileOutputStream);
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"Error while writing into the specified Excel workbook \"" + strFilePath + "\"");
				}
			}
			objFileLockMechanism.ReleaseLockOnFile(objFileLock, strLockFile);
		}
	}

	/**
	 * Function to manage and switch to new window which opened recently
	 * 
	 * @param No parameters
	 * @return No return value
	 */
	public void manageAndSwitchNewWindow() {
		if (!windowName.isEmpty()) {
			if (driverUtil.waitUntilWindowCountAvailable(windowName.size() + 1, "New Window", loadingWindowTimeOut,
					false)) {
				updateWindowHandle();
			} else if (driverUtil.waitUntilWindowCountAvailable(windowName.size() - 2, "New Window",
					loadingWindowTimeOut, false)) {
				updateWindowHandle();
			} else if (driverUtil.waitUntilWindowCountAvailable(windowName.size() - 1, "New Window",
					loadingWindowTimeOut, false)) {
				updateWindowHandle();
			}
		} else {
			ALMFunctions.ThrowException("Verify New Window Available",
					"New window title as " + driver.getTitle() + " should be displayed",
					"New window " + driver.getTitle() + " is NOT displayed", true);
		}
		driver.switchTo().window(windowName.get("Window" + windowName.size()));
		report.updateTestLog("Switch Window", "Switched to new window title as " + driver.getTitle(), Status.DONE);
		driver.manage().window().maximize();
	}

	/**
	 * Function to manage and switch to previous window after closing the active
	 * window
	 * 
	 * @param No parameters
	 * @return No return value
	 */
	public void closeAndSwitchPreviousWindow() {
		driver.close();
		windowName.remove("Window" + windowName.size());

		driver.switchTo().window(windowName.get("Window" + windowName.size()));
		report.updateTestLog("Switch Window", "Switched to new window title as " + driver.getTitle(), Status.DONE);
		driver.manage().window().maximize();
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, "Window Switch");
		
	}

	public void updateWindowHandle() {
		if (windowName.size() < driver.getWindowHandles().size()) {
			for (String windowHand : driver.getWindowHandles()) {
				if (!windowName.containsValue(windowHand)) {
					windowName.put("Window" + (windowName.size() + 1), windowHand);
				}
			}
		}
		if (windowName.size() > driver.getWindowHandles().size()) {
			List<String> windowToRemove = new ArrayList<String>();
			for (String strWindowName : windowName.values()) {
				if (!driver.getWindowHandles().contains(strWindowName)) {
					windowToRemove.add(strWindowName);

				}
			}
			windowName.values().removeAll(windowToRemove);
		}

	}

	/**
	 * method to get current page view
	 * 
	 * @return - Name of the view
	 */
	public String getCurrentPageView() {
		String strPageView = "SPARC Dev";
		String strGetTitle = driver.getTitle().toLowerCase();
		try {

			if (strGetTitle.toLowerCase().contains(properties.getProperty("ESCPortal").toLowerCase())) {
				strPageView = "ESC Portal";

			} else if (strGetTitle.toLowerCase().contains(properties.getProperty("SparcPortal").toLowerCase())) {
				strPageView = "SPARC Dev";

			}

			return strPageView;
		} catch (Exception e) {
			return strPageView; // Considering as Default Page
		}
	}
	
	/**
	 * Method to enter a value in a Global Search text box
	 * 
	 * @param strWindowName, Name of the Window name
	 * @param strValue, value to be entered in a text box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void globalSearch(String strValue, String strPageName) {
		
		click(Common.globalSearchButton, lngPagetimeOutInSeconds, strValue, "", strPageName, false);
		clear(Common.globalSearch, lngMinTimeOutInSeconds, strValue, strPageName);
		sendkeys(Common.globalSearch, lngPagetimeOutInSeconds, Keys.chord(Keys.CONTROL,"a",Keys.DELETE), "", strPageName);
		sendkeys(Common.globalSearch, lngPagetimeOutInSeconds, strValue, "", strPageName);
		sendkeys(Common.globalSearch, lngMinTimeOutInSeconds, Keys.ENTER, "Global Search", strPageName);
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		
	}
	
		/**
	 * Function to Create taskk
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void test() {
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", "Test", "~", true, true);

		FillInputForm(strInputParameters);

	}
	/**
	 * Function to Create taskk
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void resourceManagement() {
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "ResourceManagement", "~", true, true);

		FillInputForm(strInputParameters);

	}
	
	
	/**
	 * Function to Create taskk
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void securityIncident() {
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "SecurityIncident", "~", true, true);

		FillInputForm(strInputParameters);

	}
	
	
		/**
	 * Function to pass Value in search TextBox
	 * @param 
	 * @return
	 */
	
	public void textBoxsearch(String strValue,String strPageName) {
	
	 click(Common.searchBar1,lngPagetimeOutInSeconds, strValue, "", strPageName, false);
	clear(Common.searchtextValue, lngMinTimeOutInSeconds, strValue, strPageName);
	sendkeys(Common.searchtextValue, lngPagetimeOutInSeconds, strValue, "", strPageName);
		sendkeys(Common.searchtextValue, lngMinTimeOutInSeconds, Keys.ENTER, "textboxsearch", strPageName);
	driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
	}

	public String getTextDisplay(String strPageName) {
		if (driverUtil.waitUntilElementLocated(Common.status, timeOutInSeconds, "Incident", "Status", "Home Page",
				true)) {
			return driver.findElement(Common.status).getText();
		} else {
			ALMFunctions.ThrowException("Incident Status", "Incident Status should be displayed in " + strPageName,
					"Error - Incident Status is not displayed", true);
			return null;
		}
	}
	
	public void getReqNum(String strLabel, String strValue, String strPageName) {
		if(objectExists(new uimap.Common(strLabel).getNumItem, "isDisplayed", timeOutInSeconds, strLabel, "Number", strPageName, false)) {
			String strGetText = getText(new uimap.Common(strLabel).getNumItem, timeOutInSeconds, strLabel, strPageName);
			if(!strGetText.isEmpty()) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify " +strLabel, strLabel+ " should be displayed as expected", strLabel+ " : " +strGetText, true);
				FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
			    if(inputDataFilelock!=null){
			        synchronized (CommonFunctions.class) {
			        	dataTable.putData("Parametrized_Checkpoints", strValue, strGetText);
			        }   
			        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
			   
			}
				
			}
		} else if(objectExists(Common.getNumText, "isDisplayed", timeOutInSeconds, strLabel, "Number", strPageName, false)) {
			String strGetText = getText(Common.getNumText, timeOutInSeconds, strLabel, strPageName);
			if(!strGetText.isEmpty()) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify " +strLabel, strLabel+ " should be displayed as expected", strLabel+ " : " +strGetText, true);
				FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
			    if(inputDataFilelock!=null){
			        synchronized (CommonFunctions.class) {
			        	dataTable.putData("Parametrized_Checkpoints", strValue, strGetText);
			        }   
			        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
			   
			}
				
			}
		}
	}
	
	public void preFilledText(String strLabel, String strValue, String strPageName) {
		if(objectExists(new uimap.Common(strLabel).preFilledText, "isDisplayed", timeOutInSeconds, strLabel, "PreFilled Text", strPageName, false)) {
			String strGetText = driver.findElement(new uimap.Common(strLabel).preFilledText).getText();
			String strText[] = strGetText.split(",");
			for(String strGetValues : strText) {
				if(strGetValues.trim().equalsIgnoreCase(strValue)) {
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify "+strLabel+ " field displays" +strGetValues,
							strValue+ " should be displayed as expected",
							strGetValues+ " is displayed as expected against " +strLabel+ " field", true);
				}
			}
		}
	}
	public void incidentCreation() {
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "Create Incident", "~", true, true);
		FillInputForm(strInputParameters);
		String strStatus = getTextDisplay("Incident Creation");
		if (!strStatus.isEmpty()) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Incident Creation Number & Status",
					"Incident should be created as expected", strStatus, true);
			String[] strIncMsg = strStatus.split(" ");
			String strINCNum = strIncMsg[1].trim();
			dataTable.putData("Parametrized_Checkpoints", "Case Number", strINCNum);
		}
	}

//supi
	public void searchBoxValidation() {
		
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "searchBoxValidation", "~", true, true);


		FillInputForm(strInputParameters);
		
		
		
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param strLabel  The name of the lookup field
	 * @param strValue  Value to be entered in the lookup field
	 * @param strPageName Page Name in which the control is available
	 */
	public void lookupSearch(String strLabel, String strValue, String strPageName) {
		if (objectExists(new uimap.Common(strLabel, strValue).imgIcon, "isDisplayed", timeOutInSeconds, strLabel,
				"Button", strPageName, false)) {
			click(new uimap.Common(strLabel, strValue).imgIcon, timeOutInSeconds, strLabel, "Button", strPageName,
					true);
			if (objectExists(new uimap.Common(strLabel).comboLookup, "isDisplayed", timeOutInSeconds, strLabel,
					"Button", strPageName, false)) {
				click(new uimap.Common(strLabel).comboLookup, timeOutInSeconds, strLabel, "Button", strPageName, true);
//				findRecordAndClick(strColumnName, strValue, strPageName);
			}
		} else if (objectExists(new uimap.Common(strLabel).lookupSearchBtn, "isDisplayed", timeOutInSeconds, strLabel,
				"Button", strPageName, false)) {
			click(new uimap.Common(strLabel).lookupSearchBtn, timeOutInSeconds, strLabel, "Button", strPageName, true);
		}
	}
	
	/**
	 * 
	 * @param strLabel The name of the field to be selected
	 * @param strValue	Value to be entered in the field
	 * @param strPageName Page Name in which the control is available
	 */
	public void multiSearchValue(String strLabel, String strValue, String strPageName) {
		if (objectExists(new uimap.Common(strLabel).search, "isDisplayed", timeOutInSeconds, strLabel, "Button",
				strPageName, false)) {
			click(new uimap.Common(strLabel).search, timeOutInSeconds, strLabel, "Button", strPageName, true);
			sendkeys(new uimap.Common(strLabel).search, timeOutInSeconds, strValue, strLabel, strPageName);
			driverUtil.waitUntilStalenessOfElement(new uimap.Common(strLabel).multiSelect, strPageName);
			selectListItem(new uimap.Common(strLabel).multiSelect, timeOutInSeconds, new String[] { strValue },
					strLabel, strPageName, "Select");
			click(Common.addBtn, timeOutInSeconds, strLabel, "Button", strPageName, true);
		}
	}
	
	public void findRecordAndClick(String strColumnName, String strValue, String strPageName) {
	String[] strColNames = strColumnName.split(":");
		String strFirstCol = strColNames[0];
		String strSecondCol = strColNames[1];
		String strActValue = "";
		String tableHeaderDeciderVal = "";
		int intColumnIndex = 0;
		WebElement eleRows = null;
		By row = null;
		if (strValue.contains("CMDB Inheritance"))
		{
			 row = Common.tableRowCMDB;
			 String[] strVal =strValue.split("!");
			 strValue = strVal[0];
		}
		else
		{
			 row = Common.tableRow;
		}
		
		String strTableName = "LookUp Table";
		if(strValue.contains("NavigationTable"))
		{
			 intColumnIndex = getColumnIndex(strFirstCol, Common.navigationTableHeader, strTableName, false, false) + 3;
			if (!(intColumnIndex != 0)) {
				ALMFunctions.ThrowException("Get index of column name",
						"Expected column name as " + strFirstCol + " shall be displayed",
						"Expected column name as " + strFirstCol + " is not displayed", true);
			}
			 String[] strVal =strValue.split("!");
			 strValue = strVal[0];
			 tableHeaderDeciderVal = strVal[1];
		}
		else
		{
			 intColumnIndex = getColumnIndex(strFirstCol, Common.tableHeader, strTableName, false, false) + 3;
			if (!(intColumnIndex != 0)) {
				ALMFunctions.ThrowException("Get index of column name",
						"Expected column name as " + strFirstCol + " shall be displayed",
						"Expected column name as " + strFirstCol + " is not displayed", true);
			}
			
			
		}
		
		if(objectExists(uimap.Common.firstPage, "isEnabled", lngMinTimeOutInSeconds, "Next", "Button",strTableName, false)) {
			click(uimap.Common.firstPage, timeOutInSeconds, "Next", "Button", strTableName, false);
		}
		
		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strValue, strValue + " table row should be displayed",
					strValue + " table row are not displayed", true);
			ALMFunctions.ThrowException(strValue, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				ALMFunctions.Screenshot();
				if (intCurrentPage != 1) {

					if (objectExists(uimap.Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",strTableName, false)) {
						click(uimap.Common.nextPage, timeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(uimap.Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}
				// driverUtil.waitUntilStalenessOfElement(row, strTableName);

				listtableRow = driver.getWebDriver().findElements(row);

				for (WebElement rows : listtableRow) {

					strActValue = rows.findElement(By.xpath(".//*[local-name()='th' or local-name()='td'][" + intColumnIndex + "]")).getText().trim();

					if (strActValue.equals(strValue)) {

						eleRows = rows;
						blnFound = true;

					}

					if (blnFound) {
						int intColumnIndex1 =0;
						if(tableHeaderDeciderVal.equals("NavigationTable"))
						{
							 intColumnIndex1 = getColumnIndex(strSecondCol, Common.navigationTableHeader, strTableName, false, false) + 3;
							if (!(intColumnIndex1 != 0)) {
								ALMFunctions.ThrowException("Get index of column name",
										"Expected column name as " + strSecondCol + " shall be displayed",
										"Expected column name as " + strSecondCol + " is not displayed", true);
							}
						}
						else {
							 intColumnIndex1 = getColumnIndex(strSecondCol, Common.tableHeader, strTableName, false, false) + 3;
							if (!(intColumnIndex1 != 0)) {
								ALMFunctions.ThrowException("Get index of column name",
										"Expected column name as " + strSecondCol + " shall be displayed",
										"Expected column name as " + strSecondCol + " is not displayed", true);
								}
						}
						
						WebElement eleClick = eleRows.findElement(new Common(String.valueOf(intColumnIndex1)).tableLink);
						pagescroll(eleClick, strPageName);
						ALMFunctions.Screenshot();
						//WebElement eleClick = eleRows.findElement(Common.tblLink);
						driver.capture(strTableName);
						clickByJS(eleClick, intCurrentPage, strSecondCol, strTableName, strPageName, true);
						//eleClick.click();
						blnClick = true;
						break;

					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnRecordNotFound) {
				ALMFunctions.ThrowException(strValue, strValue + " value should be display in table row",
						"Error - Specified Record " + strValue + " is not found in the " + strTableName + " table",
						true);

			}

			if (blnClick) {
				report.updateTestLog("Click on " + strActValue + " in table row",
						strActValue + " link is clicked in " + strTableName, Status.DONE);

			} else {
				ALMFunctions.ThrowException("Click on " + strValue + " Table row",
						"Created From Trigger : " + strActValue + " in " + strTableName
								+ " Table row should be clicked on " + strTableName + "",
						"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
			}

		}

	}
	
public void retrieveRecordFromTable(String strColumnNames, String strValue, String strPageName) {
		String[] strColNames = strColumnNames.split(":");
		String strFirstCol = strColNames[0];
		String strSecondCol = strColNames[1];
		String[] strColValue = strValue.split(":");
		String ExpectedColVal = strColValue[0];
		String ExcelColumnName = strColValue[1];
		
		String strActValue = "";
		WebElement eleRows = null;
		By row = Common.tableRow;
		String strTableName = "LookUp Table";
		int intColumnIndex = getColumnIndex(strFirstCol, Common.tableHeader, strTableName, false, false) + 3;
		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}
		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(ExpectedColVal, ExpectedColVal + " table row should be displayed",
					ExpectedColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(ExpectedColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				ALMFunctions.Screenshot();
				if (intCurrentPage != 1) {

					if (objectExists(uimap.Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(uimap.Common.nextPage, timeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(uimap.Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}
				// driverUtil.waitUntilStalenessOfElement(row, strTableName);

				listtableRow = driver.getWebDriver().findElements(row);

				for (WebElement rows : listtableRow) {

					strActValue = rows.findElement(By.xpath(".//*[local-name()='th' or local-name()='td'][" + intColumnIndex + "]")).getText().trim();

					if (strActValue.equals(ExpectedColVal)) {

						eleRows = rows;
						blnFound = true;

					}

					if (blnFound) {
						
						int intColumnIndex1 = getColumnIndex(strSecondCol, Common.tableHeader, strTableName, false, false) + 3;
						WebElement eleClick = eleRows.findElement(new Common(String.valueOf(intColumnIndex1)).tableLink);
						//WebElement eleClick = eleRows.findElement(Common.tblLink);
						driver.capture(strTableName);
						String approverName = 	eleClick.getText();
						FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
					    if(inputDataFilelock!=null){
					        synchronized (CommonFunctions.class) {
					        	dataTable.putData("Parametrized_Checkpoints", ExcelColumnName, approverName);
					        }   
					        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
					   
					}
						
					
						blnClick = true;
						break;

					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnRecordNotFound) {
				ALMFunctions.ThrowException(ExpectedColVal, ExpectedColVal + " value should be display in table row",
						"Error - Specified Record " + ExpectedColVal + " is not found in the " + strTableName + " table",
						true);

			}

			if (blnClick) {
				report.updateTestLog("Click on " + strActValue + " in table row",
						strActValue + " link is clicked in " + strTableName, Status.DONE);

			} else {
				ALMFunctions.ThrowException("Click on " + ExpectedColVal + " Table row",
						"Created From Trigger : " + strActValue + " in " + strTableName
								+ " Table row should be clicked on " + strTableName + "",
						"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
			}

		}

	}	

/**
 * Method to enter a value in a Search text box
 * 
 * @param locator, By object of the control
 * @param strFieldName, The name of the text box
 * @param strValue, value to be entered in a text box
 * @param strPageName, Page Name in which the control is available
 * @return No return value
 */

public void searchTextBox(String strFieldName, String strValue, String strPageName)

{
	if(strFieldName.contains("Form1"))
	{
		String[] fieldName= strFieldName.split("!");
		strFieldName = fieldName[0];
		uimap.Common textbox = new uimap.Common(strFieldName);
		pagescroll(textbox.formtextBox, strPageName);
		clear(textbox.formtextBox, lngPagetimeOutInSeconds, strFieldName, strPageName);
		click(textbox.formtextBox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
		sendkeys(textbox.formtextBox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
		sendkeys(textbox.formtextBox, lngPagetimeOutInSeconds, Keys.ENTER, strFieldName, strPageName);
		driverUtil.waitFor(lngMinTimeOutInSeconds, strFieldName, strFieldName, strPageName);
	}
	else if(strFieldName.contains("Form2"))
	{
		String[] fieldName= strFieldName.split("!");
		strFieldName = fieldName[0];
		uimap.Common textbox = new uimap.Common(strFieldName);
		//driverUtil.waitFor(lngMinTimeOutInSeconds, strFieldName, strFieldName, strPageName);
		
		driverUtil.waitUntilStalenessOfElement(textbox.formtextBox1, lngMinTimeOutInSeconds, strPageName);
		pagescroll(textbox.formtextBox1, strPageName);
		clear(textbox.formtextBox1, lngPagetimeOutInSeconds, strFieldName, strPageName);
		click(textbox.formtextBox1, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
		sendkeys(textbox.formtextBox1, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
		sendkeys(textbox.formtextBox1, lngPagetimeOutInSeconds, Keys.ENTER, strFieldName, strPageName);
		driverUtil.waitFor(lngMinTimeOutInSeconds, strFieldName, strFieldName, strPageName);
	}
	else {
		uimap.Common textbox = new uimap.Common(strFieldName);
		driverUtil.waitUntilStalenessOfElement(textbox.textbox, lngMinTimeOutInSeconds, strPageName);
		pagescroll(textbox.textbox, strPageName);
		clear(textbox.textbox, lngPagetimeOutInSeconds, strFieldName, strPageName);
		click(textbox.textbox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName, false);
		sendkeys(textbox.textbox, lngPagetimeOutInSeconds, strValue, strFieldName, strPageName);
		sendkeys(textbox.textbox, lngPagetimeOutInSeconds, Keys.ENTER, strFieldName, strPageName);
		driverUtil.waitFor(lngMinTimeOutInSeconds, strFieldName, strFieldName, strPageName);
	}
}


/**
 * Method to select a radiobutton in a dialog  box
 * 
 * @param locator, By object of the control
 * @param strFieldName, The name of the text box
 * @param strValue, value to be entered in a text box
 * @param strPageName, Page Name in which the control is available
 * @return No return value
 *//*
public void dialogSelectRadioButton(String strFieldName, String strValueToSelect, String strPageName) {
	
	try {
		if (strValueToSelect.equalsIgnoreCase("Select")) {
			
			uimap.Common radio = new uimap.Common(strFieldName);
			pagescroll(radio.dialogRadioButton, strPageName);
			clickByJS(radio.dialogRadioButton, timeOutInSeconds, strValueToSelect + " in " + strFieldName, "Radio Button", strPageName, false);
			driverUtil.waitFor(lngMinTimeOutInSeconds, strFieldName, strFieldName, strPageName);
			
		}
		else {
				
			uimap.Common radio = new uimap.Common(strFieldName, strValueToSelect);
			pagescroll(radio.dialogRadioButton, strPageName);
			clickByJS(radio.dialogRadioButton, timeOutInSeconds, strValueToSelect + " in " + strFieldName, "Radio Button", strPageName, false);
			driverUtil.waitFor(lngMinTimeOutInSeconds, strFieldName, strFieldName, strPageName);
			
		}

		
	}
	catch(Exception e) {
		throw new FrameworkException(strFieldName + " is not available in " + strPageName);
	}
}*/

public void alertHandling(String strPageName) {
	
	try {
		acceptAlert(3000);
		

	} catch (Exception e) {
		ALMFunctions.UpdateReportLogAndALMForFailStatus("Alert should be displayed",  " in the page ", " Alert is not displayed in the page", true);
		//report.updateTestLog("Survey Choice", "Error " + e.getLocalizedMessage(), Status.FAIL);
	}

}

public void alertTextVerification(String strValueToSelect,String strPageName) {
	
	try {
		if(isAlertPresent(3000)) {
			String strAlertText = 	getAlertText(3000);
			String strAlertText1 =  strAlertText.replace("\n", "");
			if(strAlertText1.equalsIgnoreCase(strValueToSelect)) 
			{
				
				ALMFunctions.UpdateReportLogAndALMForPassStatus(strValueToSelect, strValueToSelect + " value should be displayed",
						"Specified Record " + strValueToSelect + " is found. Retrieved Value is " + strAlertText1 + " in Activity Log",
						true);
				//ALMFunctions.UpdateReportLogAndALMForPassStatus(strLinkName,strLinkName + " " + strElementType + " should be display in the " + strPageName,strLinkName + " " + strElementType + " is displayed in the " + strPageName, true);
				acceptAlert(3000);
			}
			else
			{
				ALMFunctions.UpdateReportLogAndALMForFailStatus(strValueToSelect, strValueToSelect + " value should be displayed",
						"Specified Record " + strValueToSelect + " is not found. Retrieved Value is " + strAlertText1 + " in Activity Log",
						true);
				
				acceptAlert(3000);
			}
			
		}
		else
		{
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Alert should be displayed",  " in the page ", " Alert is not displayed in the page", true);
			//alert not available
		}
	
	
		

	} catch (Exception e) {
		ALMFunctions.UpdateReportLogAndALMForFailStatus(strValueToSelect,strValueToSelect +   " should be displayed in the Alert"  +" " + strValueToSelect + " Alert is not displayed"," ", true);
		//report.updateTestLog("Survey Choice", "Error " + e.getLocalizedMessage(), Status.FAIL);
	}

}


public void backButton(String strButtonLabel, String strPageName)

{
	By button = null;
	button = Common.backButton;
	driver.capture(strPageName);
	click(button, lngPagetimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
	driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
}


public void verifyObjectState(String strFieldName, String strValueState, String strPageName) {
	By locator = null;
	
	String strElementType = "";
	String strElementState = "";
	
	//String[] strInputParameter = dataTable.getExpectedResult("EditAndNonEdit_FieldLabel").split("!");
	//frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
	driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
	
		/*String[] strObjectSateValue = strInput.split(";");
		//strFieldName = StringUtils.substringAfter(strObjectSateValue[0], "=");
		strElementType = StringUtils.substringAfter(strObjectSateValue[1], "=");
		strElementState = StringUtils.substringAfter(strObjectSateValue[2], "=");*/
		
		String[] strObjectStateValue = strValueState.split("!");
		//strFieldName = StringUtils.substringAfter(strObjectSateValue[0], "=");
		strElementType = strObjectStateValue[0];
		strElementState = strObjectStateValue[1];
		
		

		switch (strElementType.toLowerCase()) {
		case "textbox":
			locator = new uimap.Common(strFieldName).textbox;
			break;
		case "textarea":
			locator = new uimap.Common(strFieldName).textarea;
			break;
		case "select":
			locator = new uimap.Common(strFieldName).select;
			break;
		case "combobox":
			locator = new uimap.Common(strFieldName).comboLookup;
			break;
		case "noneditable":
			locator = new uimap.Common(strFieldName).readonlyFiled;
			break;
		case "dropdown":
			locator = new uimap.Common(strFieldName).dropdown;
			break;

		case "unlockcombobox":
			locator = new uimap.Common(strFieldName).unlockButton;
			break;

		case "button":
			locator = new uimap.Common(strFieldName).button;
			break;
			
		case "tabbutton":
			locator = new uimap.Common(strFieldName).TabButton;
			break;
		case "checkbox":
			locator = new uimap.Common(strFieldName).checkbox;
			break;
		case "link":
			locator = new uimap.Common(strFieldName).link;
			break;
		case "menu":
			locator = new uimap.Common(strFieldName).menu;
			break;
		case "readonlytextbox":
			locator = new uimap.Common(strFieldName).readonlyTextbox;
			break;
		case "dropdownoptions":
			locator = new uimap.Common(strFieldName).readonlyDropdownValues;
			break;
		case "label":
			locator = new uimap.Common(strFieldName).backendlabel;
			break;

		default:
			ALMFunctions.ThrowException("Verify Object State", "Only pre-defined control must be provided",
					"Unhandled control " + strElementType, false);
			break;
		}

		switch (strElementState.toLowerCase()) {
		case "readonly":
			verifyReadOnly(locator, strFieldName, strElementType, strElementState, strPageName);
			break;
		case "editable":
			verifyEditable(locator, strFieldName, strElementType, strElementState, strPageName);
			break;
		case "noteditable":
			verifyNonEditable(locator, strFieldName, strElementType, strElementState, strPageName);
			break;
			
			
		case "button not exist":
			strElementState = "not exist";
			verifyButtonExist(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "button exist":
			strElementState = "exist";
			verifyButtonExist(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "linknotexist":
			strElementState = "not exist";
			verifyLinkExist(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "linkexist":
			strElementState = "exist";
			verifyLinkExist(locator, strFieldName, strElementState, strElementType, strPageName);
			break;	
		case "checkboxchecked":
			strElementState = "checked";
			verifycheckboxstatus(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "checkboxnotchecked":
			strElementState = "not checked";
			verifycheckboxstatus(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "dropdowneditable":
			strElementState = "not checked";
			verifycheckboxstatus(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "lableexist":
			strElementState = "exist";
			verifyBackEndLableExist(locator, strFieldName, strElementState, strElementType, strPageName);
			break;	
		case "selecteditable":
			strElementState = "exist";
			verifySelectEditable(locator, strFieldName, strElementType, strElementState, strPageName);
			break;
		case "textareanotexist":
			strElementState = "not exist";
			verifyTextAreaExist(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		default:
			ALMFunctions.ThrowException("Verify Object State", "Only pre-defined control must be provided",
					"Unhandled control " + strElementState, false);
			break;
		}

	
	//frameSwitchAndSwitchBack(Common.frame, "default", strPageName);

}

public void verifyLinkExist(By locator, String strLinkName, String strElementState, String strElementType,
		String strPageName) {

	switch (strElementState) {
	case "exist":
		if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strLinkName, "Read Only Element",
				strPageName, false)) {
			pagescroll(locator, strPageName);
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strLinkName,strLinkName + " " + strElementType + " should be display in the " + strPageName,strLinkName + " " + strElementType + " is displayed in the " + strPageName, true);

		} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strLinkName,
					strLinkName + " " + strElementType + " should be display in the " + strPageName,
					strLinkName + " " + strElementType + " is not displayed in the " + strPageName, true);

		}

		break;

	case "not exist":
		if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strLinkName, "Read Only Element",
				strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strLinkName,
					strLinkName + " " + strElementType + " should not display in the " + strPageName,
					strLinkName + " " + strElementType + " is displayed in the " + strPageName, true);

		} else {
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strLinkName,
					strLinkName + " " + strElementType + " should not display in the " + strPageName,
					strLinkName + " " + strElementType + " is not displayed in the " + strPageName, true);

		}

		break;

	default:
		ALMFunctions.ThrowException("Verify object", "Only pre-defined control must be provided",
				"Unhandled control " + strElementState, false);
		break;
	}

}

public void verifyTextAreaExist(By locator, String strTextAreaName, String strElementState, String strElementType,
		String strPageName) {

	switch (strElementState) {
	case "exist":
		if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strTextAreaName, "Read Only Element",
				strPageName, false)) {
			pagescroll(locator, strPageName);
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strTextAreaName,strTextAreaName + " " + strElementType + " should be display in the " + strPageName,strTextAreaName + " " + strElementType + " is displayed in the " + strPageName, true);

		} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strTextAreaName,
					strTextAreaName + " " + strElementType + " should be display in the " + strPageName,
					strTextAreaName + " " + strElementType + " is not displayed in the " + strPageName, true);

		}

		break;

	case "not exist":
		if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strTextAreaName, "Read Only Element",
				strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strTextAreaName,
					strTextAreaName + " " + strElementType + " should not display in the " + strPageName,
					strTextAreaName + " " + strElementType + " is displayed in the " + strPageName, true);

		} else {
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strTextAreaName,
					strTextAreaName + " " + strElementType + " should not display in the " + strPageName,
					strTextAreaName + " " + strElementType + " is not displayed in the " + strPageName, true);

		}

		break;

	default:
		ALMFunctions.ThrowException("Verify object", "Only pre-defined control must be provided",
				"Unhandled control " + strElementState, false);
		break;
	}

}

public void verifyBackEndLableExist(By locator, String strLinkName, String strElementState, String strElementType,
		String strPageName) {

	switch (strElementState) {
	case "exist":
		if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strLinkName, "Read Only Element",strPageName, false)) {
			pagescroll(locator, strPageName);
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strLinkName,strLinkName + " " + strElementType + " should be display in the " + strPageName,strLinkName + " " + strElementType + " is displayed in the " + strPageName, true);

		} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strLinkName,
					strLinkName + " " + strElementType + " should be display in the " + strPageName,
					strLinkName + " " + strElementType + " is not displayed in the " + strPageName, true);

		}

		break;

	case "not exist":
		if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strLinkName, "Read Only Element",
				strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strLinkName,
					strLinkName + " " + strElementType + " should not display in the " + strPageName,
					strLinkName + " " + strElementType + " is displayed in the " + strPageName, true);

		} else {
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strLinkName,
					strLinkName + " " + strElementType + " should not display in the " + strPageName,
					strLinkName + " " + strElementType + " is not displayed in the " + strPageName, true);

		}

		break;

	default:
		ALMFunctions.ThrowException("Verify object", "Only pre-defined control must be provided",
				"Unhandled control " + strElementState, false);
		break;
	}

}



public void findRecordAndVerifyInTable(String strColumnName, String strValue, String strPageName) {
	
	/*String[] strColNames = strColumnName.split(":");
	String[] strValues= 	strValue.split("!");*/
	String strFirstCol = "";
	String strSecondCol = "";
	String refernceColVal = "";
	String expectedColVal = "";
	String tableLocation ="";
	String strActValue = "";
	WebElement eleRows = null;
	By row = Common.tableRow;
	int intColumnIndex =0;
	String strTableName = "LookUp Table";
	if(strValue.contains("NavigateTable")) {
		
		String[] strColNames = strColumnName.split(":");
		String[] strValues= 	strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		refernceColVal = strValues[0];
		expectedColVal =  strValues[1];
		tableLocation = strValues[2];
		 driverUtil.waitUntilStalenessOfElement(Common.navigationTableHeader, lngMinTimeOutInSeconds,strPageName);
		 intColumnIndex = getColumnIndex(strFirstCol, Common.navigationTableHeader, strTableName, false, false) + 3;
		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}
	}
	else {
			
			String[] strColNames = strColumnName.split(":");
			String[] strValues= 	strValue.split("!");
			strFirstCol = strColNames[0];
			strSecondCol = strColNames[1];
			refernceColVal = strValues[0];
			expectedColVal =  strValues[1];
			//tableLocation = strValues[2];
			driverUtil.waitUntilStalenessOfElement(Common.tableHeader, lngMinTimeOutInSeconds,strPageName);
			intColumnIndex = getColumnIndex(strFirstCol, Common.tableHeader, strTableName, false, false) + 3;
				if (!(intColumnIndex != 0)) {
					ALMFunctions.ThrowException("Get index of column name",
							"Expected column name as " + strFirstCol + " shall be displayed",
							"Expected column name as " + strFirstCol + " is not displayed", true);
		}
	}
	
	
	//frameSwitchAndSwitchBack(Common.frame, "switchframe", "Service Now Dashboard Table");
	/*if(objectExists(uimap.Common.firstPage, "isEnabled", lngMinTimeOutInSeconds, "Next", "Button",strTableName, false)) {
		click(uimap.Common.firstPage, timeOutInSeconds, "Next", "Button", strTableName, false);
	}*/
	
	boolean blnFound = false;
	boolean blnClick = false;
	int intCurrentPage = 1;
	List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
	if (listtableRow.isEmpty()) {

		ALMFunctions.ThrowException(refernceColVal, refernceColVal + " table row should be displayed",
				refernceColVal + " table row are not displayed", true);
		ALMFunctions.ThrowException(refernceColVal, "" + strTableName + " Table row should be displayed",
				"" + strTableName + " Table row is NOT displayed", true);

	} else {

		boolean blnRecordNotFound = false;
		do {

			ALMFunctions.Screenshot();
			if (intCurrentPage != 1) {

				if (objectExists(uimap.Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
						strTableName, false)) {
					click(uimap.Common.nextPage, timeOutInSeconds, "Next", "Button", strTableName, false);
					driverUtil.waitUntilStalenessOfElement(uimap.Common.nextPage, strTableName);

				} else {
					blnRecordNotFound = true;
				}

			}
			// driverUtil.waitUntilStalenessOfElement(row, strTableName);

			listtableRow = driver.getWebDriver().findElements(row);

			for (WebElement rows : listtableRow) {

				strActValue = rows.findElement(By.xpath(".//*[local-name()='th' or local-name()='td'][" + intColumnIndex + "]")).getText().trim();

				if (strActValue.equals(refernceColVal)) {

					eleRows = rows;
					blnFound = true;

				}

				if (blnFound) {
					int intColumnIndex1 = 0;
					if(tableLocation.contains("Navigate")) {
						intColumnIndex1 = getColumnIndex(strSecondCol, Common.navigationTableHeader, strTableName, false, false) + 3;
						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}
						
					}
					else
					{
						intColumnIndex1 = getColumnIndex(strSecondCol, Common.tableHeader, strTableName, false, false) + 3;
						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}
					}
					
					
					WebElement eleClick = eleRows.findElement(By.xpath(".//*[local-name()='th' or local-name()='td'][" + intColumnIndex1 + "]"));
					pagescroll(eleClick, strPageName);
					String actualVal	= eleClick.getText().trim();
					
					//WebElement eleClick = eleRows.findElement(new Common(String.valueOf(intColumnIndex1)).tableLink);
					//pagescroll(eleClick, strPageName);
					ALMFunctions.Screenshot();
					//WebElement eleClick = eleRows.findElement(Common.tblLink);
					driver.capture(strTableName);
					//String actualVal = eleClick.getText();
					if (actualVal.equals(expectedColVal)) {
						ALMFunctions.UpdateReportLogAndALMForPassStatus(expectedColVal, expectedColVal + " value should be displayed",
								"Specified Record " + expectedColVal + " is found. Actual value found is " + actualVal + " in table",
								true);
						//ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",expectedColVal," is matching with Actual Value: "+actualVal,true);

					}
					else
					{
						ALMFunctions.UpdateReportLogAndALMForFailStatus(expectedColVal, expectedColVal + " value should be displayed",
								"Specified Record " + expectedColVal + " is not found. Actual value found is " + actualVal + " in table",true);
						//ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",expectedColVal," is not matching with Actual Value: "+actualVal,true);
					}
					
					blnClick = true;
					break;

				}

			}

			intCurrentPage++;
		} while (!(blnClick || blnRecordNotFound));

		if (blnRecordNotFound) {
			ALMFunctions.ThrowException(refernceColVal, refernceColVal + " value should be display in table row",
					"Error - Specified Record " + refernceColVal + " is not found in the " + strTableName + " table",
					true);

		}

		if (blnClick) {
			report.updateTestLog("Existense of Record " + strActValue + " in table row",
					strActValue + " value is available in " + strTableName, Status.DONE);

		} else {
			ALMFunctions.ThrowException("Existense of Record " + refernceColVal + " Table row",
					"Created From Trigger : " + strActValue + " in " + strTableName
							+ " Table row should be exist in " + strTableName + "",
					"Error - Specified file in table row is NOT exist on " + strTableName + " Page", true);
		}

	}

}


public void startTime(String strName) {
	SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
	Date date = new Date(System.currentTimeMillis());
	System.out.println(strName+"-Start time"+formatter.format(date));
}

public void endTime(String strName) {
	SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
	Date date = new Date(System.currentTimeMillis());
	System.out.println(strName+"-End time"+formatter.format(date));
	System.out.println("*************************************************************");
}



/**
 * Method to enter a value in a dialog text area
 * 
 * @param strWindowName, Name of the Window name
 * @param strValue, value to be entered in a text box
 * @param strPageName, Page Name in which the control is available
 * @return No return value
 */

public void verifyMessage(String strValue, String strPageName) {
	uimap.Common textarea = new uimap.Common(strValue);
	if(driverUtil.waitUntilElementLocated(new Common(strValue).notificationMsg, lngPagetimeOutInSeconds, strValue,strValue + " PopUp", strPageName, false))
	{
		//ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",strValue," is presented in the page ",true);
		pagescroll(new Common(strValue).notificationMsg, strPageName);
		ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",
				strValue + " " + strValue + " should be available in the " + strPageName,
				strValue + " " + strValue + " is available in the " + strPageName, true);
	}
	else
	{
		//ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",strValue," is not presented in the page ",true);
		ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",
				strValue + " " + strValue + " should be available in the " + strPageName,
				strValue + " " + strValue + " is not available in the " + strPageName, true);
	}
	
}



public void verifycheckboxstatus(By loator, String strButtonName, String strElementState, String strElementType,
		String strPageName) {
	boolean blnChecked = driver.findElement(loator).isSelected();
	driverUtil.waitFor(1000, strButtonName, strElementType, strPageName);
	 pagescroll(loator, strPageName);
	switch (strElementState) {
	
	case "checked":
		
		if (blnChecked) {
			
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strButtonName,
					strButtonName + " " + strElementType + " should be checked in the " + strPageName,
					strButtonName + " " + strElementType + " is checked in the " + strPageName, true);

		} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strButtonName,
					strButtonName + " " + strElementType + " should be checked in the " + strPageName,
					strButtonName + " " + strElementType + " is not checked in the " + strPageName, true);

		}

		break;

	case "not checked":
		
		if (blnChecked)  {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(strButtonName,
					strButtonName + " " + strElementType + " should not checked in the " + strPageName,
					strButtonName + " " + strElementType + " is checked in the " + strPageName, true);

		} else {
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strButtonName,
					strButtonName + " " + strElementType + " should not checked in the " + strPageName,
					strButtonName + " " + strElementType + " is not checked in the " + strPageName, true);

		}

		break;

	default:
		ALMFunctions.ThrowException("Verify object", "Only pre-defined control must be provided",
				"Unhandled control " + strElementState, false);
		break;
	}

}

public void endImpersonation() {
	impersonateDropdown("End Impersonation", "Service now");
	ALMFunctions.UpdateReportLogAndALMForPassStatus("End Impersonation",
				"User should be able to End Impersonation from " + properties.getProperty("ProjectName") + " application",
				"User has logged out from " + properties.getProperty("ProjectName") + " application", true);
	
}

public void verifyRecordNotExistInTbl(String strColumnName, String strValue, String strPageName) {
	/*
	String[] strColNames = strColumnName.split(":");
	String[] strValues= 	strValue.split("!");
	String strFirstCol = strColNames[0];
	String strSecondCol = strColNames[1];
	String refernceColVal = strValues[0];
	String expectedColVal =  strValues[1];*/
	String strActValue = "";
	WebElement eleRows = null;
	By row = Common.tableRow;
	String strTableName = "LookUp Table";
	//frameSwitchAndSwitchBack(Common.frame, "switchframe", "Service Now Dashboard Table");
	driverUtil.waitUntilStalenessOfElement(Common.tableHeader, lngMinTimeOutInSeconds,strPageName);
	int intColumnIndex = getColumnIndex(strColumnName, Common.tableHeader, strTableName, false, false) + 3;
	if (!(intColumnIndex != 0)) {
		ALMFunctions.ThrowException("Get index of column name",
				"Expected column name as " + strColumnName + " shall be displayed",
				"Expected column name as " + strColumnName + " is not displayed", true);
	}
	boolean blnFound = false;
	boolean blnClick = false;
	int intCurrentPage = 1;
	List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
	if (listtableRow.isEmpty()) {

		ALMFunctions.ThrowException(strColumnName, strColumnName + " table row should be displayed",
				strColumnName + " table row are not displayed", true);
		ALMFunctions.ThrowException(strColumnName, "" + strTableName + " Table row should be displayed",
				"" + strTableName + " Table row is NOT displayed", true);

	} else {

		boolean blnRecordNotFound = false;
		do {

			ALMFunctions.Screenshot();
			if (intCurrentPage != 1) {

				if (objectExists(uimap.Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
						strTableName, false)) {
					click(uimap.Common.nextPage, timeOutInSeconds, "Next", "Button", strTableName, false);
					driverUtil.waitUntilStalenessOfElement(uimap.Common.nextPage, strTableName);

				} else {
					blnRecordNotFound = true;
				}

			}
			// driverUtil.waitUntilStalenessOfElement(row, strTableName);

			listtableRow = driver.getWebDriver().findElements(row);

			for (WebElement rows : listtableRow) {

				strActValue = rows.findElement(By.xpath(".//*[local-name()='th' or local-name()='td'][" + intColumnIndex + "]")).getText().trim();

				if (strActValue.equals(strValue)) {
					ALMFunctions.Screenshot();
					//eleRows = rows;
					blnFound = true;
					ALMFunctions.UpdateReportLogAndALMForFailStatus(strColumnName, strColumnName + " value should be display in table row",
							"Specified Record " + strColumnName + " is found in the " + strTableName + " table",
							true);

				}
				
}

			intCurrentPage++;
		} while (!(blnClick || blnRecordNotFound));

		if (blnRecordNotFound) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strColumnName, strColumnName + " value should be display in table row",
					"Specified Record " + strColumnName + " is not found in the " + strTableName + " table",
					true);

		}

	

	}

}

public void waitForLoading(String elementName, String elementValue, String pageName) {
	
	int waitTime = Integer.parseInt(elementValue);
	driverUtil.waitFor(waitTime, elementName, elementValue, pageName);
	
	
}
/**
 * Function to retrieve Record From ActivityLog
 * 
 * @param strFieldName reference text to be passed to find the expected field in the activity log
 * @param strLabel The name of the element in which the expected text to be
 *                 passed
 * @param pageName Page in which the element exists
 */
public void retrieveRecordFromActivityLog(String strFieldName, String strValue, String strPageName) {
	List <WebElement> elm = driver.getWebDriver().findElements(By.xpath(".//li[contains(@class,'card_comments')]"));
	//System.out.println(elm.size());
	for (WebElement label : elm) {
		String status = "Failed";
		List <WebElement> elm1 = label.findElements(By.xpath(".//span[@class='sn-widget-list-table-cell']"));
		for(int i=0; i<=elm1.size();i++)
		{
			String strActValu = 	elm1.get(i).getText();
			if(strActValu.equalsIgnoreCase(strValue)) {
				String strdateValue = label.findElement(By.xpath(".//span[contains(@class,'component-time')]//div[@class='date-calendar']")).getText().trim();
				FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
			    if(inputDataFilelock!=null){
			        synchronized (CommonFunctions.class) {
			        	dataTable.putData("Parametrized_Checkpoints", strFieldName, strdateValue);
			        }   
			        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
			   
			}
				
				ALMFunctions.Screenshot();
				ALMFunctions.UpdateReportLogAndALMForPassStatus(strFieldName, strFieldName + " value should be displayed",
						"Specified Record " + strFieldName + " is found. Retrieved Value is " + strdateValue + " in Activity Log",
						true);
				//System.out.println(strdateValue);
				status = "Passed";
				break;
				
				
			}
		}
		
		
		if(status.equalsIgnoreCase("passed")) {
			break;
		}
		
	
	}
}

/**
 * Function to select multiple value in drop down
 * 
 * @param strValue Expected text to be passed to the drop down field
 * @param strLabel The name of the element in which the expected text to be
 *                 passed
 * @param pageName Page in which the element exists
 */

public void multipleValuedropDown(String strLabel, String strValue, String strPageName) {
	driverUtil.waitUntilElementVisible(new Common(strLabel).multiplevaluedropdown, lngPagetimeOutInSeconds, strLabel,strLabel + " PopUp", strPageName, false);
	
	if (objectExists(new Common(strLabel).multiplevaluedropdown, "isEnabled", lngMinTimeOutInSeconds, strLabel, "DropDown",strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(new Common(strLabel).multiplevaluedropdown, strPageName);
			selectListItem(new Common(strLabel).multiplevaluedropdown, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
						strPageName, "Value");
			
		
	}
	
}


public void personalizationButton(String strButtonLabel, String strPageName)

{
	By button = null;
	button = Common.personalizationButton;
	driver.capture(strPageName);
	click(button, lngPagetimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
	driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
}

public void addingIncidentChange()
{
	frameSwitchAndSwitchBack(Common.frame, "switchframe", driver.getTitle());
	clickByJS(new Common("AND").button, lngMinTimeOutInSeconds, "Add filter", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementVisible(new Common("choose field").dropdownArrorWithoutName, lngPagetimeOutInSeconds, "Dropdown Searchbox", "Menu",driver.getTitle(), false);
	click(new Common("choose field").dropdownArrorWithoutName, lngMinTimeOutInSeconds, "Add filter", "Button", driver.getTitle(), true);
	sendkeys(Common.searchTextbox, lngPagetimeOutInSeconds, "Priority", "Dropdown Searchbox", driver.getTitle());
	driverUtil.waitUntilElementVisible(new Common("Priority").menu, lngPagetimeOutInSeconds, "Dropdown Searchbox", "Menu",driver.getTitle(), false);
	click(new Common("Priority").menu, lngPagetimeOutInSeconds, "Priority", "Menu", driver.getTitle(), true);
	driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, driver.getTitle());
	selectListItem(new Common("None").dropdownWithoutName1, lngMinTimeOutInSeconds, new String[] { "1 - Critical" }, "None","None", "Value");
	//click(new Common("1 - Critical").menu, lngPagetimeOutInSeconds, "1 - Critical", "Menu", driver.getTitle(), true);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
	clickByJS(new Common("Run filter").button, lngMinTimeOutInSeconds, "Run filter", "Button", driver.getTitle(), true);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
	driverUtil.waitUntilStalenessOfElement(By.xpath(".//select[@aria-label='Collection']"), "");
	Select dropdown = new Select(driver.findElement(By.xpath(".//select[@aria-label='Collection']")));
	List<WebElement> elm = dropdown.getOptions();
	elm.size();
	String dropDownFirstVal = elm.get(0).getText();
	selectListItem(Common.incidentFormValues, lngMinTimeOutInSeconds, new String[] { dropDownFirstVal }, "None","None", "Value");
	//click(Common.incidentFormValues, lngMinTimeOutInSeconds, "Add filter", "Button", driver.getTitle(), true);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
	clickByJS(new Common("Add").navigateButtonWithoutName, lngMinTimeOutInSeconds, "Add", "Button", driver.getTitle(), true);
	report.updateTestLog("Screen Shot", "Screen Shot Captured in " +  driver.getTitle(),Status.SCREENSHOT);
	ALMFunctions.Screenshot();
	clickByJS(new Common("Save").CommentsButton, lngMinTimeOutInSeconds, "Save", "Button", driver.getTitle(), true);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
	
}

/**
 * Method to click Link with partial value provided
 * 
 * @param strValue, value to click link
 * @param strPageName, Page Name in which the control is available
 * @return No return value
 */

public void partialLink(String strLink, String strPageName) {
	Common objLink = new Common(strLink);
	if (objectExists(objLink.partialLink, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName, false)) {
		click(objLink.partialLink, lngPagetimeOutInSeconds, strLink, "Link", strPageName, true);
	} else {
		ALMFunctions.ThrowException(strLink, "Link - " + strLink + " should be displayed in " + strPageName,
				"Error - Link - " + strLink + " is not available in " + strPageName, true);
	}
}




public void verifyLogUpdate() {
		String getCellValue = "";
		String strPageName = dataTable.getData("Parametrized_Checkpoints", "PageName");
		String strSub = dataTable.getData("Parametrized_Checkpoints", "Email_Label");
		int count = 1;
		int subcount = 1;
		int intCount = 1;
		boolean blnFlag = false;
		
		frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);
		tab("Activity Log", strPageName);
		driverUtil.waitUntilElementVisible(Common.activityText, timeOutInSeconds, "Activities", "Label", strPageName, true);
		if(objectExists(new uimap.Common(strSub).showEmailLink, "isDisplayed", timeOutInSeconds, strSub , "Add Icon", strPageName, false)) {
            pagescroll(new uimap.Common(strSub).showEmailLink, strPageName);
        	click(new uimap.Common(strSub).showEmailLink, timeOutInSeconds, strSub, "Add Icon", strPageName, false);
		}	
		
		HashMap<String, String> logValue = new HashMap<>();
		String strLabel = "", strValue = "";
		try {
			for (String strItem : dataTable.getData("Parametrized_Checkpoints", "Activity_Log").split("!")) {
				for (String strSingleItem : strItem.split(";")) {
					if (StringUtils.substringBefore(strSingleItem, "=").toLowerCase().equals("element label")) {
						strLabel = StringUtils.substringAfter(strSingleItem, "=").trim();
					}
					if (StringUtils.substringBefore(strSingleItem, "=").toLowerCase().equals("element value")) {
						strValue = StringUtils.substringAfter(strSingleItem, "=").trim();
					}
				}
				logValue.put(strLabel, strValue);
				frameSwitchAndSwitchBack(uimap.Common.cardFrame, "switchcardframe", "Service Now Dashboard Table");
				if(strValue.trim().length()>0) {
					List<WebElement> actualRows = driver.getWebDriver().findElements(uimap.Common.tableBodyData);
					for(int i = 0;i < actualRows.size();i++) {
						if(count >= 1 ) {
							WebElement row = actualRows.get(i);
							List<WebElement> cells = row.findElements(By.xpath(".//td"));
							for(int j = 0;j < cells.size();j++) {
								getCellValue = cells.get(j).getText();
								if(subcount != 1  && !getCellValue.isEmpty()) {
									if(strValue.equalsIgnoreCase(getCellValue)) {
										System.out.println(getCellValue);
										blnFlag = true;
										ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Activity Log",
												 "Actual - " +strLabel+ " : " +getCellValue+
												 "<br>Expected - " +strLabel+ " : " +strValue,
												 "<br> Actual - " +strLabel+ " : " +getCellValue+
												 "<br> Expected - " +strLabel+ " : " +strValue, true);
									}
								} else if (subcount != 1 && getCellValue.isEmpty()) {
									break;
								}
//								if(getCellValue.contains("@gilead.com") || getCellValue.endsWith(".com")) {
//									ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Activity Log",
//											 "'To' field should not be empty","'To' field is not empty as expected", true);
//								}
								subcount++;
//								if(getCellValue.equalsIgnoreCase("SPARCTRAIN")) {
//									intCount++;
//								}
							}
//							if(intCount>=3) {
//								break;
//							}
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			ALMFunctions.ThrowException("Verify Activity Log", "User should able to verify the " +strPageName,
					"Error: Unable to verify " + strPageName + "displayed in the " + strPageName + " tabe", true);
		}
			if(objectExists(new uimap.Common(strSub).hideEmailLink, "isDisplayed", timeOutInSeconds, "Hide Email Address", "Link Text", strPageName, false)) {
			click(new uimap.Common(strSub).hideEmailLink, timeOutInSeconds, "Hide Email Address", "Link Text", strPageName, false);
		}
		frameSwitchAndSwitchBack(uimap.Common.frame, "default", "Service Now Dashboard Table");	
	}



/**
 * Method to get the case number from UI post data creation
 * 
 * @param strLabel, value will be retrieved from the label
 * @return No return value
 */

public void getValueFromTextBox(String strLabel, String fieldName, String strPageName) {

	String strPageView = getCurrentPageView();
	switch (strPageView) {
	case "SPARC Dev":
		if (objectExists(new Common(strLabel).textbox, "isDisplayed", lngPagetimeOutInSeconds, strLabel,"Label", strPageName, true)) {
			String strGetValue = getAttributeValue(new Common(strLabel).textbox, lngMinTimeOutInSeconds,
					"value", strLabel, strPageName);
			ALMFunctions.UpdateReportLogAndALMForPassStatus("TextBox Value : " + strLabel,
					"User should able to capture the " + strLabel + " TextBox Value",
					strLabel + " TextBox Value has been captured as : " + strGetValue, true);
			report.updateTestLog("TextBox Value : " + strLabel,
					strLabel + " TextBox Value has been captured as : " + strGetValue, Status.PASS);
			FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
			 if(inputDataFilelock!=null){
			        synchronized (CommonFunctions.class) {
			        	dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
			        }   
			        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
			   
			}
			
		}
		break;
	
	default:
		ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
				"Unhandled - View - " + strPageView, false);
		break;
	}

}
/**
 * Method to select multiple values from a dropdown which dont have a name/label
 * 
 * @param @param strValue, value to click link
 * @return No return value
 */
public void selectMultipleValueDropdownWithoutName(String strLabel, String strValue, String strPageName) {
	
		
		if (objectExists(Common.incidentFormValues, "isEnabled", lngMinTimeOutInSeconds, strLabel, "DropDown",
				strPageName, false)) {
			driverUtil.waitFor(lngMinTimeOutInSeconds, strLabel, strValue, strPageName);
			selectListItem(Common.incidentFormValues, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
					strPageName, "Value");
		}
		
				
		
}

public void navigationButton(String strButtonLabel, String strPageName)

{
	By button = null;
	button = new Common(strButtonLabel).navigateButtonWithoutName;
	driver.capture(strPageName);
	click(button, lngPagetimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
	driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
}


public void exportPDF() 
{
	String strLabel = "Test";
	String strValue = "Export!PDF!Portrait";
	String strPageName = driver.getTitle();
	Common colValue = new Common("Child Class");
	By radiobtnlocator = colValue.tableColName;
	
	frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
	//WebElement elm = driver.findElement(By.xpath(".//*[contains(@class,'col-header') and text()='Child Class' and @tabindex='0']"));
	mouseOverandRightClick(radiobtnlocator, lngMinTimeOutInSeconds, strLabel, strValue, strPageName, false);
	//frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
	//WebElement elm = driver.findElement(By.xpath(".//*[contains(@class,'col-header') and text()='Child Class' and @tabindex='0']"));
	//mouseOverandRightClick(radiobtnlocator, lngMinTimeOutInSeconds, strLabel, strValue, strPageName, false);
	//new WebDriverWait(driver.getWebDriver(), timeOutInSeconds).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(Common.windowframe));
	//driver.switchTo().frame("//iframe[@title='Developer Sidebar']");
	String[] arrInputVal=strValue.split(strExclamation);
	
	for (String listItem:arrInputVal ) {
		selectMultiListItem(listItem, timeOutInSeconds, "", "multiselect", strPageName, true);
		System.out.println("Passed");
	}
	
	By button = null;
	button = new Common("Download").CommentsButton;
	driver.capture(strPageName);
	click(button, lngPagetimeOutInSeconds, "Download", "Button", strPageName, true);
	
	getDownloadFilePath();
}

public void selectMultiListItem(String strListValue, long timeOutInSeconds, String elementName, String elementType, String pageName, boolean ReportLog) {
	boolean blnClick = false;
	try {
		
		
			Common menuValue = new Common(strListValue);
			By menuItem = menuValue.menuItem;
			
			driverUtil.waitFor(1000, strListValue, strListValue, pageName);
			driverUtil.waitUntilStalenessOfElement(new Common(strListValue).menuItem, pageName);
			driverUtil.waitUntilPageReadyStateComplete(1000, pageName);	
			//driverUtil.waitUntilStalenessOfElement(menuItem, "");
			driverUtil.waitFor(lngMinTimeOutInSeconds, strListValue, strListValue, pageName);
			//pagescroll(menuItem, pageName);
			WebElement elm  = driver.findElement(menuItem);
			//try {
				//pagescroll(radiobtnlocator, pageName);
				
				try {
					new Actions(driver.getWebDriver()).moveToElement(elm).build().perform();
					clickByJS(elm, lngMinTimeOutInSeconds, "", "Lookup",pageName, true);
				} catch(Exception e) {
					// TODO Auto-generated catch block
					new Actions(driver.getWebDriver()).moveToElement(elm).build().perform();
					clickByJS(elm, lngMinTimeOutInSeconds, "", "Lookup",pageName, true);
				}
				
				//driver.findElement(radiobtnlocator).click();
				if (ReportLog) {
					report.updateTestLog("Click", elementType + " - '" + strListValue + "' for '" + elementName + "' field in " + pageName + " is clicked", Status.DONE);
				}
			//} catch(Exception e) {
				//ALMFunctions.ThrowException(elementName, "User should be able to click " + "\"" + elementName + "\"" + " in the page " + "\"" + pageName + "\"", "Below exception is thrown while trying to click " + "\"" + elementName + "\"" + "<br><br>" + e.getLocalizedMessage(), true);
			//}
			
		
		
	
	} catch(Exception e) {
		ALMFunctions.ThrowException(elementName, "User should be able to select " + strListValue, "Error - Unable to perform selection of " + strListValue + " " + elementType + " in the page: " + "\"" + pageName + "\"", true);
	}
}

public void getDownloadFilePath() {
    String strFileDownloadPath = null;
   // String strFileName = dataTable.getExpectedResult("Report_Name")+".xlsx";
    String strFileName =  "u_cmdb_inheritance_rule.pdf";
   
    String strRelativeDirectory = System.getProperty("user.dir") + Util.getFileSeparator();
    if (BrowserName.equalsIgnoreCase("internet explorer")) {
           String strUserProfilePath = System.getenv("USERPROFILE");
           strFileDownloadPath = strUserProfilePath + "\\Downloads\\";
    } else if (BrowserName.equalsIgnoreCase("chrome")) {
           String strUserProfilePath = System.getenv("USERPROFILE");
           strFileDownloadPath = strUserProfilePath + "\\Downloads\\";
           //strFileDownloadPath = strRelativeDirectory+Util.getFileSeparator()+"downloads";
    }
    String strParentFolder = "testDataFiles";
    File strDestinationFilePath = null;

    boolean blnFileExist;
    try {
           blnFileExist = FileExists(strFileDownloadPath + strFileName);
           if (blnFileExist) {
                 File parentFolder = new File(strRelativeDirectory + strParentFolder);
                 if (!parentFolder.exists()) {
                        parentFolder.mkdir();
                 }

                  if(testparameters.getCurrentTestcase().equalsIgnoreCase("Custom_Report_Creation")) {
                        strDestinationFilePath = new File(strRelativeDirectory + Util.getFileSeparator() + strParentFolder + Util.getFileSeparator() + properties.getProperty("EmployeeReportDump")+".xlsx");
                 }else if(testparameters.getCurrentTestcase().equalsIgnoreCase("Gilead_New_Hire_Report")) {
                        strDestinationFilePath = new File(strRelativeDirectory + Util.getFileSeparator() + strParentFolder + Util.getFileSeparator() + properties.getProperty("NewHireDump")+".xlsx");      
                 }else {
                        strDestinationFilePath = new File(strRelativeDirectory + Util.getFileSeparator() + strParentFolder + Util.getFileSeparator() + strFileName);
                 }

                 boolean blnDirFileExist = FileExists(strDestinationFilePath.getPath());
                 if (blnDirFileExist) {
                        new File(strDestinationFilePath.getPath()).delete();
                        FileUtils.copyFile(new File(strFileDownloadPath + strFileName), strDestinationFilePath);
                        new File(strFileDownloadPath + strFileName).delete();
                        ALMFunctions.UpdateReportLogNSAndALMForPassStatus("Files are downloaded in the following path", "Downloaded file path", strDestinationFilePath.getParent(), false);
                 }else {
                        FileUtils.copyFile(new File(strFileDownloadPath + strFileName), strDestinationFilePath);
                        new File(strFileDownloadPath + strFileName).delete();
                        ALMFunctions.UpdateReportLogNSAndALMForPassStatus("Files are downloaded in the following path", "Downloaded file path", strDestinationFilePath.getParent(), false);
                 }


           } else {
                 ALMFunctions.ThrowException("Verify Downloaded File Exist in Userprofile Path", strFileName + " - should exist in Userprofile Path", "Error - " + strFileName + " is not exist in Userprofile Path", false);
           }
    } catch(Exception ex) {
           ALMFunctions.ThrowException("Verify Downloaded File Exist in Userprofile Path", strFileName + " - should exist in Userprofile Path", "Error - " + strFileName + " is not exist in Userprofile Path", false);
    }

}


public boolean FileExists(String strFilePath) throws InterruptedException {
	File file = new File(strFilePath);
	boolean blnFound = false;
	int counter = 0;
	if (!file.exists()) {
		while (counter <= 10) {
			Thread.sleep(2000);
			if (file.exists()) {
				blnFound = true;
				break;
			}
			counter++;
		}
	}
	else {
		blnFound = true;
	}
	return blnFound;
}

public void righClickForm(String strElementName, String strValue, String strPageName)
{
	By button = null;
	By label = null;
	button = new Common(strElementName).CommentsButton;
	label = new Common(strValue).label;
	
	frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", "Service Now Dashboard Table");
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
	mouseOverandRightClick(button, lngMinTimeOutInSeconds, "Save", "Button", strPageName, true);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
	if (objectExists(label, "isEnabled", lngMinTimeOutInSeconds, strElementName, "Button",strPageName, false)) {
	click(label, timeOutInSeconds, strValue, "Button", strPageName,true);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
	
	}
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
}


public void sortByDescending(String strElementName, String strValues, String strPageName)
{
	By button = null;
	button = new Common(strElementName).sortByDescending;
	Common colName = new Common(strElementName);
	pagescroll(colName.tableColName, strPageName);
	if (objectExists(button, "isEnabled", lngMinTimeOutInSeconds, strElementName, "Button",strPageName, false)) {
		
		
		driverUtil.waitFor(timeOutInSeconds, strElementName, strValues, strPageName);
		driverUtil.waitFor(timeOutInSeconds, strElementName, strValues, strPageName);
		driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
		clickByJS(button, timeOutInSeconds, "Sort By Descending", "Button", strPageName,true);
		driverUtil.waitFor(2000, strElementName, strValues, strPageName);
		/*if (objectExists(button, "isEnabled", lngMinTimeOutInSeconds, strElementName, "Button",strPageName, false)) {
			driverUtil.waitFor(timeOutInSeconds, strElementName, strValues, strPageName);
			click(button, timeOutInSeconds, "Sort By Descending", "Button", strPageName,true);
			
			
		}*/
		
	}
	
}

public void sortByAscending(String strElementName, String strValues, String strPageName)
{
	By button = null;
	button = new Common(strElementName).sortByascending;
	Common colName = new Common(strElementName);
	pagescroll(colName.tableColName, strPageName);
	if (objectExists(button, "isEnabled", lngMinTimeOutInSeconds, strElementName, "Button",strPageName, false)) {
		
		driverUtil.waitFor(timeOutInSeconds, strElementName, strValues, strPageName);
		driverUtil.waitFor(timeOutInSeconds, strElementName, strValues, strPageName);
		driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
		clickByJS(button, timeOutInSeconds, "Sort By ascending", "Button", strPageName,true);
		driverUtil.waitFor(2000, strElementName, strValues, strPageName);
		/*if (objectExists(button, "isEnabled", lngMinTimeOutInSeconds, strElementName, "Button",strPageName, false)) {
			driverUtil.waitFor(lngMinTimeOutInSeconds, strElementName, strValues, strPageName);
			click(button, timeOutInSeconds, "Sort By ascending", "Button", strPageName,true);
			
			
		}*/
		
	}
	
}
public static void main(String[] args) {
    String strURL= "C:/Users/ssenthil1/Desktop/LM-0456.01 ApprovedLabelMaster.pdf";
String input= "Date Approved: 17-Jan-2020";
    boolean blnVerifyPDF = verifyPDFContent(strURL,input);
    if(blnVerifyPDF) {
           //Almfunctions.UpdateReportLogAndALMForPassStatus("")
           System.out.println("pass");
    } else {
           System.out.println("fail");
    }

}   

public static boolean verifyPDFContent(String pdfpath,String strInput) {
    boolean blnVerify = false;
    //String strURL= "C:/Users/ssenthil1/Desktop/LM-0456.01 ApprovedLabelMaster.pdf";
//String input= "Document status: Effective";
try {
   //strURL.Position = Number.Zero;
   PdfReader reader = new PdfReader(pdfpath);
   for(int i=1;i<=reader.getNumberOfPages();i++)
   {
  
         String textFromPage = PdfTextExtractor.getTextFromPage(reader,i);
         if(textFromPage.contains(strInput))
         {
         blnVerify= true;
         reader.close();
         break;
         
         }
         
   }
} catch (IOException e) {
  e.printStackTrace();
}
    return blnVerify;
}


public void customizingColumn(String strElementName, String strValues, String strPageName)
{
	By button = null;
	button = Common.personalizationButton;
	driver.capture(strPageName);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Personalize Button", "Button", driver.getTitle());
	click(button, lngPagetimeOutInSeconds, "Personalize Button", "Button", strPageName, true);
	driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
	driverUtil.waitUntilStalenessOfElement(By.xpath(".//select[@aria-label='Available']"), "");
	uimap.Common dropDownValue = new uimap.Common(strValues);
	
	if (objectExists(dropDownValue.dropDownSelectedValue, "isDisplayed", lngPagetimeOutInSeconds, "Dropdown Selected Value","label", strPageName, false))
	{
		ALMFunctions.UpdateReportLogAndALMForPassStatus(strValues + " DropDown",
				strValues + " should be Selected Already in the Dropdown",
				strValues + " is Selected in the dropdown already", true);
	}
	
	else {
	selectListItem(Common.incidentFormValues, lngMinTimeOutInSeconds, new String[] { strValues }, "None","None", "Value");
	//click(Common.incidentFormValues, lngMinTimeOutInSeconds, "Add filter", "Button", driver.getTitle(), true);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
	clickByJS(new Common("Add").navigateButtonWithoutName, lngMinTimeOutInSeconds, "Add", "Button", driver.getTitle(), true);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Button", "Button", driver.getTitle());
	ALMFunctions.UpdateReportLogAndALMForPassStatus(strValues + " DropDown",
			strValues + " should be added in the Dropdown",
			strValues + " is added in the dropdown", true);
	}
	clickByJS(new Common("OK").CommentsButton, lngMinTimeOutInSeconds, "Add", "Button", driver.getTitle(), true);
	
}

public void filter(String strElementName, String strValues, String strPageName)
{
	driverUtil.waitUntilElementVisible(new uimap.Common("-- choose field --").breadcrumbLink, lngMinTimeOutInSeconds, "All", "Link", "", true);
	
	String[] strinputPara = strValues.split("!");
	if(strinputPara.length>2) {
	String firstFilterValue = strinputPara[0];
	String secondFilterVal = strinputPara[1];
	String ThirdFilterVal = strinputPara[2];
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle());
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
	driverUtil.waitUntilElementVisible(new Common("choose field").dropdownArrorWithoutName, lngPagetimeOutInSeconds, "Dropdown Searchbox", "Menu",driver.getTitle(), false);
	click(new Common("choose field").dropdownArrorWithoutName, lngMinTimeOutInSeconds, "Add filter", "Button", driver.getTitle(), true);
	sendkeys(Common.searchTextbox, lngPagetimeOutInSeconds, firstFilterValue, "Dropdown Searchbox", driver.getTitle());
	if(objectExists(new Common(firstFilterValue).dropDownVal, "isDisplayed", timeOutInSeconds, "Filter", "Button", "", false)) {
		click(new Common(firstFilterValue).dropDownVal, lngMinTimeOutInSeconds, "DropDown", "DropDown Value", driver.getTitle(), false);
	}
//	driverUtil.waitUntilElementVisible(new Common(firstFilterValue).menu, lngPagetimeOutInSeconds, "Dropdown Searchbox", "Menu",driver.getTitle(), false);
	//click(new Common(firstFilterValue).menu, lngPagetimeOutInSeconds, firstFilterValue, "Menu", driver.getTitle(), true);
	driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, driver.getTitle());
	selectListItem(new Common(firstFilterValue).secondfilterdropdown, lngMinTimeOutInSeconds, new String[] { secondFilterVal }, "None","None", "Value");
	driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, driver.getTitle());
	
	driverUtil.waitUntilElementVisible(new Common(firstFilterValue).menu, lngPagetimeOutInSeconds, "Dropdown Searchbox", "Menu",driver.getTitle(), false);
	List<WebElement> options=driver.getWebDriver().findElements(new Common(firstFilterValue).menu);
	for(WebElement option:options)
	{
	if(option.getText().equals(firstFilterValue))
	{
	System.out.println(option.getText());
	click(option,lngMinTimeOutInSeconds,firstFilterValue,"menu",strPageName,true);
	break;
	}
	}
	
	 if(objectExists(new Common(firstFilterValue).thirdfiltertextBox, "isDisplayed", timeOutInSeconds, "Filter", "Button", "", false))
	{
		sendkeys(new Common(firstFilterValue).thirdfiltertextBox, lngPagetimeOutInSeconds, ThirdFilterVal, "Dropdown Searchbox", driver.getTitle());
		sendkeys(new Common(firstFilterValue).thirdfiltertextBox, lngPagetimeOutInSeconds, Keys.TAB, "Dropdown Searchbox", driver.getTitle());
	}
	
	 else if(objectExists(new Common(firstFilterValue).thirdfilterdropdown, "isDisplayed", timeOutInSeconds, "Filter", "Button", "", false)) {
		selectListItem(new Common(firstFilterValue).thirdfilterdropdown, lngMinTimeOutInSeconds, new String[] { ThirdFilterVal }, "None","None", "Value");
		
		}
	//click(new Common("1 - Critical").menu, lngPagetimeOutInSeconds, "1 - Critical", "Menu", driver.getTitle(), true);
	driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
	
	}
	else
	{
		String firstFilterValue = strinputPara[0];
		String secondFilterVal = strinputPara[1];
		
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle());
		
		
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
		driverUtil.waitUntilElementVisible(new Common("choose field").dropdownArrorWithoutName, lngPagetimeOutInSeconds, "Dropdown Searchbox", "Menu",driver.getTitle(), false);
		click(new Common("choose field").dropdownArrorWithoutName, lngMinTimeOutInSeconds, "Add filter", "Button", driver.getTitle(), true);
		sendkeys(Common.searchTextbox, lngPagetimeOutInSeconds, firstFilterValue, "Dropdown Searchbox", driver.getTitle());
		driverUtil.waitUntilElementVisible(new Common(firstFilterValue).menu, lngPagetimeOutInSeconds, "Dropdown Searchbox", "Menu",driver.getTitle(), false);
		click(new Common(firstFilterValue).menu, lngPagetimeOutInSeconds, firstFilterValue, "Menu", driver.getTitle(), true);
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, driver.getTitle());
		selectListItem(new Common(firstFilterValue).secondfilterdropdown, lngMinTimeOutInSeconds, new String[] { secondFilterVal }, "None","None", "Value");
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, driver.getTitle());
		
		//click(new Common("1 - Critical").menu, lngPagetimeOutInSeconds, "1 - Critical", "Menu", driver.getTitle(), true);
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Dropdown", "Button", driver.getTitle());
	}
	
}

public void filterButton(String strButtonLabel, String strPageName)

{
	if(objectExists(Common.filterButton, "isDisplayed", timeOutInSeconds, "Filter", "Button", "", false))
	{
		driverUtil.waitUntilStalenessOfElement(Common.filterButton, lngMinTimeOutInSeconds, strPageName);
		clickByJS(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button",strPageName, true);
	}
	
	
}



//Aparna 30-Dec-2021
public void getValueFromComboBox(String strLabel, String fieldName, String strPageName) {

     String strPageView = getCurrentPageView();
     switch (strPageView) {
     case "SPARC Dev":
            if (objectExists(new Common(strLabel).comboboxreadonly, "isDisplayed", lngPagetimeOutInSeconds, strLabel,
                         "Label", strPageName, true)) {
                   String strGetValue = getAttributeValue(new Common(strLabel).comboboxreadonly, lngMinTimeOutInSeconds,
                                "value", strLabel, strPageName);
                   ALMFunctions.UpdateReportLogAndALMForPassStatus("Combobox Value : " + strLabel,
                                "User should able to capture the " + strLabel + " Combobox Value",
                                strLabel + " Combobox Value has been captured as : " + strGetValue, true);
                   report.updateTestLog("Combobox Value : " + strLabel,
                                strLabel + " Combobox Value has been captured as : " + strGetValue, Status.PASS);
                   FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
                   if(inputDataFilelock!=null){
                           synchronized (CommonFunctions.class) {
                                dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
                           }   
                           new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
                      
                   }
            }
            break;
     
     default:
            ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
                         "Unhandled - View - " + strPageView, false);
            break;
     }

}

//Amit 31-Dec-2021
public void getValueFromTextContentBody(String strLabel, String fieldName, String strPageName) {

String strPageView = getCurrentPageView();
switch (strPageView) {
case "SPARC Dev":
     frameSwitchAndSwitchBack(uimap.Common.Articleframe, "switchcardframe", "Service Now Dashboard Table");
     if (objectExists(new Common(strLabel).textContentReadonly, "isDisplayed", lngPagetimeOutInSeconds, strLabel,
                   "Label", strPageName, true)) {
            
            /*String strGetValue = getAttributeValue(new Common(strLabel).textContentReadonly, lngMinTimeOutInSeconds,
                         "value", strLabel, strPageName);*/
            String strGetValue=driver.findElement(new Common(strLabel).textContentReadonly).getText();
            ALMFunctions.UpdateReportLogAndALMForPassStatus("TextContentBody Value : " + strLabel,
                         "User should able to capture the " + strLabel + " TextContentBody Value",
                         strLabel + " TextContentBody Value has been captured as : " + strGetValue, true);
            report.updateTestLog("TextContentBody Value : " + strLabel,
                         strLabel + " TextContentBody Value has been captured as : " + strGetValue, Status.PASS);
            FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
            if(inputDataFilelock!=null){
                    synchronized (CommonFunctions.class) {
                         dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
                    }   
                    new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
               
            }
            
     }
     frameSwitchAndSwitchBack(Common.frame, "switchframe", "Service Now Dashboard Table");
     break;

default:
     ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
                   "Unhandled - View - " + strPageView, false);
     break;
}

}
//Aparna 31-Dec-2021
public void getValueFromUnlockCombobox(String strLabel, String fieldName, String strPageName) {

     String strPageView = getCurrentPageView();
     switch (strPageView) {
     case "SPARC Dev":
            if (objectExists(new Common(strLabel).unlockButtonReadonly, "isDisplayed", lngPagetimeOutInSeconds, strLabel,
                         "Label", strPageName, true)) {
//                String strGetValue = getAttributeValue(new Common(strLabel).unlockButtonReadonly, lngMinTimeOutInSeconds,
//                              "value", strLabel, strPageName);
                   String strGetValue=driver.findElement(new Common(strLabel).unlockButtonReadonly).getText();
                   ALMFunctions.UpdateReportLogAndALMForPassStatus("UnlockCombobox Value : " + strLabel,
                                "User should able to capture the " + strLabel + " TextBox Value",
                                strLabel + " UnlockCombobox Value has been captured as : " + strGetValue, true);
                   report.updateTestLog("UnlockCombobox Value : " + strLabel,
                                strLabel + " UnlockCombobox Value has been captured as : " + strGetValue, Status.PASS);
                   FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
                   if(inputDataFilelock!=null){
                           synchronized (CommonFunctions.class) {
                                dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
                           }   
                           new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
                      
                   }
                   
            }
            break;
     
     default:
            ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
                         "Unhandled - View - " + strPageView, false);
            break;
     }

}
//siva 04/01
public void spEnterTextContentBody(String strLabel, String strValue, String strPageName) {
	frameSwitchAndSwitchBack(new uimap.Common(strLabel).sptextContentBodyFrame, "switchcardframe", strPageName);
	if(objectExists(new uimap.Common(strLabel).textContentBody, "isDisplayed", timeOutInSeconds, strLabel, "Content Body", strPageName, false)) {
		click(new uimap.Common(strLabel).textContentBody, timeOutInSeconds, strLabel, "Content Body", strPageName, true);
		sendkeys(new uimap.Common(strLabel).textContentBody, timeOutInSeconds, strValue, strLabel, strPageName);
		sendkeys(new uimap.Common(strLabel).textContentBody, timeOutInSeconds, Keys.TAB, strLabel, strPageName);
	}
	frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
}

/**
 * Function to Create task
 * 
 * @param No parameter
 * @return No return value
 */
public void demandManagement() {
	String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "DemandManagement", "~", true, true);

	FillInputForm(strInputParameters);

}

/**
 * Function to Create task
 * 
 * @param No parameter
 * @return No return value
 */
public void sparcPortal() {
	String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "SparcPortal", "~", true, true);

	FillInputForm(strInputParameters);

}

//Aparna 29-Dec-2021
public void rightClickAndVerify(String strLink, String strValue, String strPageName) {
     
     //frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
     Common objLink = new Common(strLink);
     if (objectExists(objLink.link, "isDisplayed", timeOutInSeconds, strLink, "Link", strPageName, false)) {
            pagescroll(objLink.link, strPageName);
            mouseOverandRightClick(objLink.link, lngMinTimeOutInSeconds, strLink, strLink , strPageName, false);
            
            if(objectExists(Common.verifyApprove, "isDisplayed", timeOutInSeconds, strValue , "Text", strPageName, false)) {
     
                   
                   ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",strValue,strValue+" is presented in the page ",true);
            }
            else
            {
                   ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",strValue,strValue+" is not presented in the page ",true);
            }
     }
     else {
            ALMFunctions.ThrowException(strLink, "Link - " + strLink + " should be displayed in " + strPageName,
                         "Error - Link - " + strLink + " is not available in " + strPageName, true);
     }
}
//Amit 29=Dec-2021
public void rightclickandselect(String strLink, String strValue, String strPageName) {
rightClickAndVerify(strLink, strValue,strPageName);
Actions A = new Actions(driver.getWebDriver());
A.moveToElement(driver.findElement(Common.clickApprove)).click().build().perform();
}

public void clickOnSparcLabel(String strmousover, String strPageName) {
    Common mouseover = new Common(strmousover);
    driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
    if (objectExists(mouseover.clickLabelSparc, "isDisplayed", timeOutInSeconds, strmousover, "Link", strPageName, false)) {

           clickByJS(mouseover.clickLabelSparc, lngPagetimeOutInSeconds, strmousover, "Link", strPageName, true);
           driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
           

    }
}

//Yash 04/01

/*public void verifyLabelExist(String strLink, String strValue, String strPageName) {
	 By locator = new Common(strValue).spLabel;
	
	
	if (driverUtil.waitUntilElementLocated(locator, 10, strValue, "Label", strPageName, false)) {
		ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",strValue,strValue+" is presented in the page ",true);
	}
	else
	{
		ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",strValue,strValue+" is not presented in the page ",true);
	}



}*/

/**
 * Method to click button
 * 
 * @param strValue, value to click button
 * @param strPageName, Page Name in which the control is available
 * @return No return value
 */
public void spDropDownbutton(String strLabel, String strButtonLabel, String strPageName)

{
		
		driver.capture(strPageName);
		if(objectExists(new uimap.Common(strLabel, strButtonLabel).spDropDownButton, "isDisplayed", timeOutInSeconds, strButtonLabel, "Button", strPageName, false)) {
			clickByJS(new uimap.Common(strLabel, strButtonLabel).spDropDownButton, timeOutInSeconds, strLabel, "Dropdown Click",strPageName, true);
		
				} 
}


public void verifySparcEmailIconExist() {

	if (objectExists(Common.SparcEmailbox, "isEnabled", lngMinTimeOutInSeconds, "Emailbox", "Icon",
			"SPARC Portal Page", false)){
		ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value:SparcEmailBox ",Common.SparcEmailbox+" is presented in the page ","SparcEmailBox",true);
	}
	else
	{
		ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",Common.SparcEmailbox+" is not presented in the page ","SparcEmailBox",true);
	}
	}
	
public void clickonEmail() {
	if (objectExists(Common.SparcEmailbox, "isEnabled", lngMinTimeOutInSeconds, "Emailbox", "Icon",
	"SPARC Portal Page", false)) {
		//driverUtil.waitFor(lngMinTimeOutInSeconds, strLabel, strValue, strPageName);
		click(Common.SparcEmailbox, lngPagetimeOutInSeconds, "Emailbox", "Icon", "SPARC Portal Page", true);
		driverUtil.waitUntilStalenessOfElement(Common.SparcEmailbox, lngMinTimeOutInSeconds, "SPARC Portal Page");
	
	}
	
	}
public void getValueFromspDropDownButton(String strLabel, String fieldName, String strPageName) {

String strPageView = getCurrentPageView();
switch (strPageView) {
case "SPARC Dev":
  
     if (objectExists(new Common(strLabel,fieldName).spDropDownButtonVal, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Label", strPageName, true)) {
            
            /*String strGetValue = getAttributeValue(new Common(strLabel).textContentReadonly, lngMinTimeOutInSeconds,
                         "value", strLabel, strPageName);*/
            String strGetValue=driver.findElement(new Common(strLabel,fieldName).spDropDownButtonVal).getText();
            ALMFunctions.UpdateReportLogAndALMForPassStatus("Button Value : " + strLabel,
                         "User should able to capture the " + strLabel + " Button Value",
                         strLabel + " Button Value has been captured as : " + strGetValue, true);
            report.updateTestLog("Button Value : " + strLabel,
                         strLabel + " Button Value has been captured as : " + strGetValue, Status.PASS);
            FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
            if(inputDataFilelock!=null){
                    synchronized (CommonFunctions.class) {
                         dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
                    }   
                    new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
               
            }
            
     }
     
     break;

default:
     ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
                   "Unhandled - View - " + strPageView, false);
     break;
}

}

/*public void clickOnLinkAndVerify(String label, String strPageName) {
    By locator = new Common(label).links;
    pagescroll(locator, strPageName);
    List<WebElement> links = driver.getWebDriver().findElements(locator);
    for (WebElement link : links) {
           String linktext = link.getAttribute("href");
           try {
                 HttpURLConnection url = (HttpURLConnection) new URL(linktext).openConnection();
                 url.setRequestMethod("HEAD");
                 if (linktext.equals(url.getURL().toString())) {
                        report.updateTestLog("LINK verification",
                                      "Expected link is " + linktext + " Actual value is " + url.getURL().toString(),
                                      Status.PASS);
                 } else {
                        report.updateTestLog("LINK verification",
                                      "Expected link is " + linktext + " Actual value is " + url.getURL().toString(),
                                      Status.FAIL);
                 }
           } catch (MalformedURLException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
           } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
           }

    }
}
*/

//amit 05-01-2022
public void getDisplayedFromDropdown(String strLabel, String fieldName, String strPageName) {
    if (objectExists(new Common(strLabel).editabledropdown, "isDisplayed", lngPagetimeOutInSeconds, strLabel,
                 "Label", strPageName, true)) {
           String strGetValue = getAttributeValue(new Common(strLabel).editabledropdown, lngMinTimeOutInSeconds,
                        "value", strLabel, strPageName);
           if (strGetValue.charAt(0) == '$') {
                 strGetValue = strGetValue.substring(1);
           }
           ALMFunctions.UpdateReportLogAndALMForPassStatus("Case Record : " + strLabel,
                        "User should able to capture the " + strLabel + " Case Record",
                        strLabel + " case record has been captured as : " + strGetValue, true);
           report.updateTestLog("Case Record : " + strLabel,
                        strLabel + " case record has been captured as : " + strGetValue, Status.PASS);
           FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
           if (inputDataFilelock != null) {
                 synchronized (CommonFunctions.class) {
                        dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
                 }
                 new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");

           }

    }
}

//Aparna 29-12-21
public void validTo(String strLabel, String strDateAndMonth, String strPageName) {


			String strDateValue = StringUtils.substringBefore(strDateAndMonth, "/");
			String strMonthValue = StringUtils.substringAfter(strDateAndMonth, "/");
			boolean blnMonthfound = false;
			click(new Common(strLabel).validTo, lngMinTimeOutInSeconds, strLabel, "Button", strPageName, true);
			if (objectExists(Common.monthPicker, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Date Picker",
			strPageName, false)) {
			do {
			
			
			
			
			
			String strGetMonthValue = getText(Common.monthPicker, lngPagetimeOutInSeconds, "Month", strPageName);
			if (strGetMonthValue.contains(strMonthValue)) {
			report.updateTestLog("Select Month", "Month is selected as " + strMonthValue, Status.DONE);
			blnMonthfound = true;
			break;
			} else {
			
			
			
			
			
			click(Common.DatePickerNextbutton, lngMinTimeOutInSeconds, "Next", "Button", strPageName, false);
			}
			} while (!blnMonthfound);
			
			
			
			
			
			List<WebElement> columns = driver.getWebDriver().findElements(Common.dateSelection);
			for (WebElement cell : columns) {
			
			
			
			
			
			String strDate = cell.getText().trim();
			if (strDate.equals(strDateValue.trim())) {
			cell.findElement(By.xpath(".//a")).click();
			report.updateTestLog("Select Date", "Date is selected as " + strDateValue, Status.DONE);
			break;
			}
			}
			
			
			
			//Commented @ Aparna 24 Nov
			//click(Common.dateMonthOkButton, lngMinTimeOutInSeconds, "OK", "Button", strPageName, true);
			} else {
			
			
			
			
			
			ALMFunctions.ThrowException("Date picker", "Date picker should be display in the " + strPageName,
			"Date picker is not displayed in the " + strPageName, true);
			}
		
}
public void clickOnLinkAndVerify(String label, String strPageName) {
	By locator = new Common(label).links;
	pagescroll(locator, strPageName);
	List<WebElement> links = driver.getWebDriver().findElements(locator);
	for (WebElement link : links) {
		String linktext = link.getAttribute("href");
		try {
			HttpURLConnection url = (HttpURLConnection)new URL(linktext).openConnection();
			url.setRequestMethod("HEAD");
			if (linktext.equals(url.getURL().toString())) {
				report.updateTestLog("LINK verification",
						"Expected link is " + linktext + " Actual value is " + url.getURL().toString(),
						Status.PASS);
			} else {
				report.updateTestLog("LINK verification",
						"Expected link is " + linktext + " Actual value is " + url.getURL().toString(),
						Status.FAIL);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

public void verifySearchTextboxandSelectValue(String strLabel, String strValue, String strPageName) {
		if (objectExists(new Common(strLabel).spDropDownArrow, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "DropDown",strPageName, false)) {
		
		click(new Common(strLabel).spDropDownArrow, timeOutInSeconds, strLabel, "DropDown", strPageName, false);
		ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify " +strLabel+ "Exist", strLabel+ " should be displayed as expected", strLabel+ " is displayed successfully", true);
		if(objectExists(Common.searchTextbox, "isDisplayed", timeOutInSeconds, strLabel, strValue, strPageName, false)) {
		
		
		
		
		ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",strValue,"Search textbox is presented in the page ",true);
		
		//click(new Common(strLabel).spDropDownArrow, timeOutInSeconds, strLabel, "DropDown", strPageName, false);
		sendkeys(Common.searchTextbox, timeOutInSeconds, strValue, strLabel, strPageName);
		driverUtil.waitFor(4000, strLabel, strValue, strPageName);
		sendkeys(Common.searchTextbox, lngMinTimeOutInSeconds, Keys.ENTER, strLabel, strPageName);
		//sendkeys(objtextbox.sparctextbox, lngMinTimeOutInSeconds, Keys.TAB, strLabel, strPageName);
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		}
		else if(objectExists(new Common(strLabel).sparcDropDowntextbox, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "DropDown",strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",strValue,"Search textbox is presented in the page ",true);

			//click(new Common(strLabel).spDropDownArrow, timeOutInSeconds, strLabel, "DropDown", strPageName, false);
			sendkeys(new Common(strLabel).sparcDropDowntextbox, timeOutInSeconds, strValue, strLabel, strPageName);
			driverUtil.waitFor(4000, strLabel, strValue, strPageName);
			sendkeys(new Common(strLabel).sparcDropDowntextbox, lngMinTimeOutInSeconds, Keys.ENTER, strLabel, strPageName);
			//sendkeys(objtextbox.sparctextbox, lngMinTimeOutInSeconds, Keys.TAB, strLabel, strPageName);
			driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
			}
		
		
		}
}
public void sparcPortalCreation() {
	
	String strStatus = getTextDisplay("Incident Creation");
	if (!strStatus.isEmpty()) {
		ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Incident Creation Number & Status",
				"Incident should be created as expected", strStatus, true);
		String[] strIncMsg = strStatus.split(" ");
		String strINCNum = strIncMsg[1].trim();
		dataTable.putData("Parametrized_Checkpoints", "Case Number", strINCNum);
	}
}

//Yash 05-01-2022

		public void verifyLabelExist(String strLink, String strValue, String strPageName) {
		Common label = new Common(strValue);
		
			if (driverUtil.waitUntilElementLocated(label.spLabel, timeOutInSeconds, "Incident", "Status", "Home Page",true)) {
			
			//if (objectExists(label.spLabel, "isDisplayed", timeOutInSeconds, strValue, "Label", strPageName, false)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",strValue,strValue+" is presented in the page ",true);
			}
			else
			{
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",strValue,strValue+" is not presented in the page ",true);
			}
		
		}

//Yash 05-01-2022
			public void dropDownArrowWithoutName(String strLabel,String strValue, String strPageName) {
			By arrow = null;
			arrow = new Common(strLabel).SPdropdownArrorWithoutName;
			if (objectExists(arrow, "isEnabled", lngMinTimeOutInSeconds, strLabel, "DropDown",
			strPageName, false)) {
			driverUtil.waitFor(lngMinTimeOutInSeconds, strLabel, strValue, strPageName);
			click(arrow, lngPagetimeOutInSeconds, strLabel, "dropdownarrowwithoutname", strPageName, true);
			//selectListItem(Common.incidentFormValues, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
			//strPageName, "Value");
			}}
			
			
			
			public void dialogConfirm(String strLabel, String strPageName) {
			if(objectExists(Common.dialogPopup, "isDisplayed", timeOutInSeconds, strLabel, "Check box", strPageName, false)) {
			click(Common.YesButton, timeOutInSeconds, strLabel, "Yes Button", strPageName, true);
			}
			//click(Common.closeBtn, timeOutInSeconds, strLabel, "Check box", strPageName, false);
			}
			
			
			
			
			public void clickonCardlabel(String strLabel, String strValue, String strPageName) {
			By labelcard = null;
			labelcard = new Common(strLabel).cardlabel;
			click(labelcard, lngPagetimeOutInSeconds, strLabel, "card label", strPageName, true);
			}
			
			public void logoButton(String strButtonLabel, String strPageName)
			
			
			
			{
			By button = null;
			button = Common.homePageLogo;
			driver.capture(strPageName);
			click(button, lngPagetimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
			driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
			}
			
			/**
			 * Function to Create task
			 * 
			 * @param No parameter
			 * @return No return value
			 */
			public void runBook() {
				String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "RunBook", "~", true, true);
			
				FillInputForm(strInputParameters);
			
			}
			
			public void runBook1() {
				// String strInputParameters = getConcatenatedStringFromExcel("FillForm",
				// "Input_Parameters",
				// "Concatenate_Flag_Input", "RunBook", "~", true, true);

				FillInputForm("FillForm", "Input_Parameters", "Concatenate_Flag_Input", "RunBook", "~", true, true);

			}

public void verifyBackEndLabelExist(String strLink, String strValue, String strPageName) {
	Common label = new Common(strValue);
	
		if (driverUtil.waitUntilElementLocated(label.label, timeOutInSeconds, "Incident", "Status", "Home Page",
				true)) {
		
		//if (objectExists(label.spLabel, "isDisplayed", timeOutInSeconds, strValue, "Label", strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",strValue,strValue+" is presented in the page ",true);
		}
		else
		{
		ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",strValue,strValue+" is not presented in the page ",true);
		}
	
			}
		
		public void infoButton(String strButtonLabel, String strPageName)
		
		{
		                By button = null;
		                button = Common.infoIcon;
		                driver.capture(strPageName);
		                click(button, lngPagetimeOutInSeconds, strButtonLabel, "Button", strPageName, true);
		                driverUtil.waitUntilStalenessOfElement(button, lngMinTimeOutInSeconds, strPageName);
		}
		
		public void clickHeartIcon()
		
		
		{
		if (objectExists(Common.Sparc_New_Portal_HeartIcon_xpath, "isEnabled", lngMinTimeOutInSeconds, "Red Heart Icon", "Icon",
		"Knowledge Mangement", false)) {
		////driverUtil.waitFor(lngMinTimeOutInSeconds, "", "", strPageName);
		click(Common.Sparc_New_Portal_HeartIcon_xpath, lngPagetimeOutInSeconds, "Red Heart Icon", "Icon", "Knowledge Mangement", true);
		
		}
		
		else {
		report.updateTestLog("click on Heart", "Heart Icon aleaready Rred in Colour clicked", Status.PASS);
		
		
		
		}
		}
		
		public void clickonlabel( String strLabel,String strValue, String strPageName) {
		
		
		
		
		By rate = null;
		rate = new Common(strLabel).rateArticle;
		System.out.println("Rate should print");
		windowName.put("Window1", driver.getWindowHandle());
		manageAndSwitchNewWindow();
		
		
		//driver.findElement(rate).click();
		click(rate, lngPagetimeOutInSeconds, strLabel, "rateonarticle", strPageName, true);
		
		System.out.println("Rate is print");
		
		
		
		
		
		}


//		public void verifyMandatoryFieldExist(String strLabel, String strValue, String strPageName) {
//			Common label = new Common(strValue);
//			
//				if (driverUtil.waitUntilElementLocated(label.mandatoryMark, timeOutInSeconds, "Incident", "Status", "Home Page",
//						true)) {
//				
//				//if (objectExists(label.spLabel, "isDisplayed", timeOutInSeconds, strValue, "Label", strPageName, false)) {
//					ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",strValue,strValue+" is presented in the page ",true);
//				}
//				else
//				{
//				ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",strValue,strValue+" is not presented in the page ",true);
//				}
//			
//			}
		public void verifyMandatoryFieldExist(String strLabel, String strValue, String strPageName) {
			Common label = new Common(strValue);
			switch (strLabel) {
			case "Mandatory":
			if (objectExists(label.mandatoryMark, "isDisplayed", timeOutInSeconds, strLabel, "Mandatory Field",
			"Incident", false)) {



			// if (objectExists(label.spLabel, "isDisplayed", timeOutInSeconds, strValue,
			// "Label", strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strValue, strValue,
			strValue + " is a mandatory field presented in the page ", true);
			} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ", strValue,
			strValue + " is a mandatory field not presented in the page ", true);
			}
			break;
			case "Non Mandatory":
			if (!objectExists(label.mandatoryMark, "isDisplayed", timeOutInSeconds, strLabel, "Mandatory Field",
			"Incident", false)) {



			// if (objectExists(label.spLabel, "isDisplayed", timeOutInSeconds, strValue,
			// "Label", strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus(strValue, strValue,
			strValue + " is a non-mandatory field presented in the page ", true);
			} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ", strValue,
			strValue + " is a non-mandatory field not presented in the page ", true);
			}
			break;
			
			}



			}
		
		
		public void getValueFromEditableTextContentBody(String strLabel, String fieldName, String strPageName) {



			String strPageView = getCurrentPageView();
			switch (strPageView) {
			case "SPARC Dev":
			frameSwitchAndSwitchBack(new uimap.Common(strLabel).textContentBodyFrame, "switchcardframe", strPageName);

			if (objectExists(new Common(strLabel).textContentBody, "isDisplayed", lngPagetimeOutInSeconds, strLabel,
			"Label", strPageName, true)) {

			/*String strGetValue = getAttributeValue(new Common(strLabel).textContentReadonly, lngMinTimeOutInSeconds,
			"value", strLabel, strPageName);*/
			String strGetValue=driver.findElement(new Common(strLabel).textContentBody).getText();
			ALMFunctions.UpdateReportLogAndALMForPassStatus("TextContentBody Value : " + strLabel,
			"User should able to capture the " + strLabel + " TextContentBody Value",
			strLabel + " TextContentBody Value has been captured as : " + strGetValue, true);
			report.updateTestLog("TextContentBody Value : " + strLabel,
			strLabel + " TextContentBody Value has been captured as : " + strGetValue, Status.PASS);
			FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
			if(inputDataFilelock!=null){
			synchronized (CommonFunctions.class) {
			dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
			}
			new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");

			}

			}
			frameSwitchAndSwitchBack(Common.frame, "switchframe", "Service Now Dashboard Table");
			break;



			default:
			ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
			"Unhandled - View - " + strPageView, false);
			break;
			}
			}
		
		public void verifySelectEditable(By locator, String strFieldName, String strElementType, String strElementState,
				String strPageName) {



				if (driverUtil.waitUntilElementLocated(locator,timeOutInSeconds, strFieldName, strElementType, strPageName, false)) {
				 click(locator, lngMinTimeOutInSeconds, " select", "dropdown", strPageName, true);
				// List<WebElement> selectvalues= driver.findElements(locator);
					Select options = new Select(driver.findElement(locator));
					List<WebElement>selectvalues= options.getOptions();
					for(WebElement value :selectvalues) {
					// ALMFunctions.UpdateReportLogAndALMForPassStatus(value.getText()+ strValue,
					// ""," Case Number should starts with RBS followed by 7 digits", true);
					report.updateTestLog("Verify '" + value.getText() + "' field '" + strElementState + "'",
					"'" + value.getText() + "' field " + strElementState + " in the " + strPageName, Status.DONE);
					}
					
				}
		}
		
		
		public void validateCaseNumber(String strLabel, String strValue, String strPageName) {
			switch (strValue) {
			case "RunBook Schedule":
			if (objectExists(new Common(strLabel).textbox, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Label",
			strPageName, true)) {
			String strInput = dataTable.getExpectedResult("Case Number");
			if (strInput.startsWith("RBS") && strInput.length() == 10) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Case Number : " + strInput, "",
			" Case Number should starts with RBS followed by 7 digits", true);
			}
			}
			break;
			case "RunBook Table":
			if (objectExists(new Common(strLabel).textbox, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Label",
			strPageName, true)) {
			String strInput = dataTable.getExpectedResult("Case Number");
			if (strInput.startsWith("RNB") && strInput.length() == 10) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Case Number : " + strInput, "",
			" Case Number should starts with RNB followed by 7 digits", true);
			}
			}
			break;
			case "Catalog Task":
			if (objectExists(new Common(strLabel).textbox, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Label",
			strPageName, true)) {
			String strInput = dataTable.getExpectedResult("Case Number");
			if (strInput.startsWith("TASK") && strInput.length() == 11) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Case Number : " + strInput, "",
			" Case Number should starts with TASK followed by 7 digits", true);
			}
			}
			break;




			}
			}

		
		public void verifyenabledTextBoxStatus(String strValues, String strPageName) {
			String strLabel = "";
			String strValue = "";
			String strActualValue = "";
			boolean blnRecordFound = false;
			String[] strInputValue = strValues.split(strExclamation);
			frameSwitchAndSwitchBack(uimap.Common.frame, "switchframe", strPageName);

			for (String strLabelAndValue : strInputValue) {
				strLabel = StringUtils.substringBefore(strLabelAndValue, strEqualto);
				strValue = StringUtils.substringAfter(strLabelAndValue, strEqualto).trim();
				uimap.Common locator = new Common(strLabel);
				inner:
				 if (objectExists(locator.textbox, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "Textarea",strPageName, false)) {
					 	strActualValue = driver.findElement(locator.textbox).getAttribute("value");
		        		strActualValue = getAttributeValue(locator.textbox, lngPagetimeOutInSeconds, "value",strLabel, strPageName).replace("_", " ").trim();
		        		blnRecordFound = true;
		        		break inner;
				

		        }
				else {

					ALMFunctions.ThrowException(strLabel,
							strLabel + " label should be display in the " + strPageName + " page",
							"No Such " + strLabel + " label is found in the " + strPageName + " page", true);
				}
				
				if(blnRecordFound) 
				{
				pass:if (strActualValue.equalsIgnoreCase(strValue)) {
					report.updateTestLog(strLabel, "Expected " + strLabel + " value should be display as : <br>" + strValue
							+ "<br>" + "Actual " + strLabel + " value is displayed as : <br>" + strActualValue, Status.PASS);
					ALMFunctions.UpdateReportLogAndALMForPassStatus(strLabel,
							strLabel + " value should be display as : " + strValue,
							strLabel + " value is displayed as : " + strActualValue, true);
					break pass;

				}else if (strActualValue.contains(strValue)) {
					report.updateTestLog(strLabel, "Expected " + strLabel + " value should be display as : <br>" + strValue
							+ "<br>" + "Actual " + strLabel + " value is displayed as : <br>" + strActualValue, Status.PASS);
					ALMFunctions.UpdateReportLogAndALMForPassStatus(strLabel,
							strLabel + " value should be display as : " + strValue,
							strLabel + " value is displayed as : " + strActualValue, true);
					break pass;
				} 
				
				else {
					report.updateTestLog(strLabel, "Expected " + strLabel + " value should be display as : <br>" + strValue
							+ "<br>" + "Actual " + strLabel + " value is displayed as : <br>" + strActualValue, Status.FAIL);
					ALMFunctions.UpdateReportLogAndALMForFailStatus(strLabel,
							strLabel + " value should be display as : " + strValue,
							strLabel + " value is displayed as : " + strActualValue, true);

				}
			}

			}

		}	
		
		
		
		public void verifySelectedDropdownValue(String strLabel, String fieldName, String strPageName) {
		    if (objectExists(new Common(strLabel).editabledropdown, "isDisplayed", lngPagetimeOutInSeconds, strLabel, "Label", strPageName, true)) {
		           String strGetValue = getAttributeValue(new Common(strLabel).editabledropdown, lngMinTimeOutInSeconds,
		                        "value", strLabel, strPageName);
		           if (strGetValue.charAt(0) == '$') {
		                 strGetValue = strGetValue.substring(1);
		           }
		           
		           if (strGetValue.equalsIgnoreCase(fieldName)){
		        	   ALMFunctions.UpdateReportLogAndALMForPassStatus("Verification : " + strLabel,
		                       "Expected  " + strLabel + " value",
		                       strLabel + " is matching with the actual value: " + strGetValue, true);
		         
		           }
		           else
		           {
		        	   ALMFunctions.UpdateReportLogAndALMForFailStatus("Verification : " + strLabel,
		                       "Expected  " + strLabel + " value",
		                       strLabel + " is not matching with the actual value: " + strGetValue, true);
		         
		           }
		           

		    }
		}	
		
		
		public void getValueFromReadonlyTextArea(String strLabel, String fieldName, String strPageName) {

			String strPageView = getCurrentPageView();
			switch (strPageView) {
			case "SPARC Dev":
				if (objectExists(new Common(strLabel).readonlyTextarea, "isDisplayed", lngPagetimeOutInSeconds, strLabel,"Label", strPageName, true)) {
//					String strGetValue = getAttributeValue(new Common(strLabel).readonlyTextarea, lngMinTimeOutInSeconds,
//							"value", strLabel, strPageName);
					String strGetValue=driver.findElement(new Common(strLabel).readonlyTextarea).getText();
					ALMFunctions.UpdateReportLogAndALMForPassStatus("TextArea Value : " + strLabel,
							"User should able to capture the " + strLabel + " TextBox Value",
							strLabel + " TextArea Value has been captured as : " + strGetValue, true);
					report.updateTestLog("TextArea Value : " + strLabel,
							strLabel + " TextArea Value has been captured as : " + strGetValue, Status.PASS);
					FileLock inputDataFilelock = new FileLockMechanism(5).SetLockOnFile("GetData");
					 if(inputDataFilelock!=null){
					        synchronized (CommonFunctions.class) {
					        	dataTable.putData("Parametrized_Checkpoints", fieldName, strGetValue);
					        }   
					        new FileLockMechanism(5).ReleaseLockOnFile(inputDataFilelock, "GetData");
					   
					}
					
				}
				break;
			
			default:
				ALMFunctions.ThrowException("Error", "Locators are available only for the Pre-Defined Views",
						"Unhandled - View - " + strPageView, false);
				break;
			}

		}
		
		public void editTableRecords(String strColumnName, String strValue, String strPageName) {
            String[] strColNames = strColumnName.split(":");
            String strFirstCol = strColNames[0];
            String strSecondCol = strColNames[1];
            String strActValue = "";
            String tableHeaderDeciderVal = "";
            int intColumnIndex = 0;
            WebElement eleRows = null;
            By row = null;
            if (strValue.contains("CMDB Inheritance")) {
                  row = Common.tableRowCMDB;
                  String[] strVal = strValue.split("!");
                  strValue = strVal[0];
            } else {
                  row = Common.tableRow;
            }

            String strTableName = "LookUp Table";
            if (strValue.contains("NavigationTable")) {
                  intColumnIndex = getColumnIndex(strFirstCol, Common.navigationTableHeader, strTableName, false, false) + 3;
                  if (!(intColumnIndex != 0)) {
                         ALMFunctions.ThrowException("Get index of column name",
                                       "Expected column name as " + strFirstCol + " shall be displayed",
                                       "Expected column name as " + strFirstCol + " is not displayed", true);
                   }
                  String[] strVal = strValue.split("!");
                  strValue = strVal[0];
                  tableHeaderDeciderVal = strVal[1];
            } else {
                  intColumnIndex = getColumnIndex(strFirstCol, Common.tableHeader, strTableName, false, false) + 3;
                  if (!(intColumnIndex != 0)) {
                         ALMFunctions.ThrowException("Get index of column name",
                                       "Expected column name as " + strFirstCol + " shall be displayed",
                                       "Expected column name as " + strFirstCol + " is not displayed", true);
                  }

            }

            /*
            * if (objectExists(uimap.Common.firstPage, "isEnabled", lngMinTimeOutInSeconds,
            * "Next", "Button", strTableName, false)) { click(uimap.Common.firstPage,
            * timeOutInSeconds, "Next", "Button", strTableName, false); }
            */

            boolean blnFound = false;
            boolean blnClick = false;
            int intCurrentPage = 1;
            List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
            if (listtableRow.isEmpty()) {

                  ALMFunctions.ThrowException(strValue, strValue + " table row should be displayed",
                                strValue + " table row are not displayed", true);
                  ALMFunctions.ThrowException(strValue, "" + strTableName + " Table row should be displayed",
                                "" + strTableName + " Table row is NOT displayed", true);

            } else {

                  boolean blnRecordNotFound = false;
                  do {

                         ALMFunctions.Screenshot();
                         if (intCurrentPage != 1) {

                                if (objectExists(uimap.Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
                                              strTableName, false)) {
                                       click(uimap.Common.nextPage, timeOutInSeconds, "Next", "Button", strTableName, false);
                                       driverUtil.waitUntilStalenessOfElement(uimap.Common.nextPage, strTableName);

                                } else {
                                       blnRecordNotFound = true;
                                }

                         }
                         // driverUtil.waitUntilStalenessOfElement(row, strTableName);

                         listtableRow = driver.getWebDriver().findElements(row);

                         for (WebElement rows : listtableRow) {

                                strActValue = rows
                                              .findElement(
                                                            By.xpath(".//*[local-name()='th' or local-name()='td'][" + intColumnIndex + "]"))
                                              .getText().trim();

                                if (strActValue.equals(strValue)) {

                                       eleRows = rows;
                                       blnFound = true;

                                }

                                if (blnFound) {
                                       int intColumnIndex1 = 0;
                                       if (tableHeaderDeciderVal.equals("NavigationTable")) {
                                              intColumnIndex1 = getColumnIndex(strSecondCol, Common.navigationTableHeader, strTableName,
                                                            false, false) + 3;
                                              if (!(intColumnIndex1 != 0)) {
                                                     ALMFunctions.ThrowException("Get index of column name",
                                                                  "Expected column name as " + strSecondCol + " shall be displayed",
                                                                  "Expected column name as " + strSecondCol + " is not displayed", true);
                                              }
                                       } else {
                                              intColumnIndex1 = getColumnIndex(strSecondCol, Common.tableHeader, strTableName, false,
                                                            false) + 3;
                                              if (!(intColumnIndex1 != 0)) {
                                                     ALMFunctions.ThrowException("Get index of column name",
                                                                  "Expected column name as " + strSecondCol + " shall be displayed",
                                                                  "Expected column name as " + strSecondCol + " is not displayed", true);
                                              }
                                       }

                                       WebElement eleClick = eleRows
                                                     .findElement(new Common(String.valueOf(intColumnIndex1)).tableLink);
                                       // pagescroll(eleClick, strPageName);
                                       // WebElement eleClick = eleRows.findElement(Common.tblLink);
                                       driver.capture(strTableName);

                                       Actions actions = new Actions(driver.getWebDriver());
                                       actions.moveToElement(eleClick, 30, 18).doubleClick().build().perform();
                                       ALMFunctions.Screenshot();
                                       if (objectExists(Common.errormsg, "isDisplayed", lngMinTimeOutInSeconds, strActValue, "textbox",
                                                     strPageName, false)) {
                                              ALMFunctions.UpdateReportLogAndALMForPassStatus(
                                                            "check " + strSecondCol + " field is read only or not",
                                                            strSecondCol + " Field should be read only", strSecondCol + " Field is read only",
                                                            true);
                                       } else {
                                              ALMFunctions.UpdateReportLogAndALMForPassStatus(
                                                            "check " + strSecondCol + " field is read only or not",
                                                            strSecondCol + " Field should not read only",
                                                            strSecondCol + " Field is not read only", true);
                                       }

                                       blnClick = true;
                                       break;

                                }

                         }

                         intCurrentPage++;
                  } while (!(blnClick || blnRecordNotFound));

                  if (blnRecordNotFound) {
                         ALMFunctions.ThrowException(strValue, strValue + " value should be display in table row",
                                       "Error - Specified Record " + strValue + " is not found in the " + strTableName + " table",
                                       true);

                  }

                  if (blnClick) {
                         report.updateTestLog("Click on " + strActValue + " in table row",
                                       strActValue + " link is clicked in " + strTableName, Status.DONE);

                  } else {
                         ALMFunctions.ThrowException("Click on " + strValue + " Table row",
                                       "Created From Trigger : " + strActValue + " in " + strTableName
                                                     + " Table row should be clicked on " + strTableName + "",
                                       "Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
                  }

            }

     }
		public void sparcdialogSearch(String strLabel, String strValue, String strPageName) {
			driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
			//Commented by yash

				if (objectExists(Common.dropDownSearch, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "Dropdown Search",
				strPageName, false)) {
				click(Common.dropDownSearch, timeOutInSeconds, strLabel, "Dropdown Search", strPageName, true);
				sendkeys(Common.dropDownSearch, timeOutInSeconds, strValue, strLabel, strPageName);
				// sendkeys(Common.dropDownSearch, lngMinTimeOutInSeconds, Keys.ENTER, strLabel, strPageName);
				// clickByJS(new uimap.Common(strValue).dropDownSelectValue, timeOutInSeconds, strLabel, "Dropdown Value", strPageName, true);
				mouseOverandClick(new uimap.Common(strValue).dropDownSelectValue, timeOutInSeconds, strLabel,
				"Dropdown Value", strPageName, true);
				}
			}
	

		
		/**
		 * Function to Create taskk
		 * 
		 * @param No parameter
		 * @return No return value
		 */
		public void calls() {
			String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "Calls", "~", true, true);

			FillInputForm(strInputParameters);

		}	
		
		
		
		
		
		public void verifyTag(String strLink, String strValue, String strPageName) {
			Common label = new Common(strValue);

				if (driverUtil.waitUntilElementLocated(label.breadcrumbLink, timeOutInSeconds, "Incident", "Status", "Home Page",true)) {
	
				//if (objectExists(label.spLabel, "isDisplayed", timeOutInSeconds, strValue, "Label", strPageName, false)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected Value: ",strValue,strValue+" is presented in the page ",true);
				}
				else
				{
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Expected Value: ",strValue,strValue+" is not presented in the page ",true);
				}

			}
		
		
		
		

		
	public void validateEmailBody(String strLabel, String strValue, String strPageName) {
		String strPageView = getCurrentPageView();
		switch (strPageView) {
		case "SPARC Dev":
			if (objectExists(new Common(strLabel).readonlyTextarea, "isDisplayed", lngPagetimeOutInSeconds,strLabel, "TextArea", strPageName, true)) {
			String strInput1 = dataTable.getExpectedResult("Description");
			String strInput2 = dataTable.getExpectedResult("Article_Data");
			
			//System.out.print("**"+StringUtils.substringBetween(strInput2, "Email body: \n", "\n\nThis Case was")+"**");
			if (strInput1.equals(StringUtils.substringBetween(strInput2, "Email body: \n", "\n\nThis Case was"))){
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Email body: in " + strValue, "",
			"Latest work notes with text value: Email body: " + strInput1, true);
			}
			
			}
			
			
			
			}
	
	
	
	}	
		
		
	public void findPartialRecordAndClick(String strColumnName, String strValue, String strPageName) {
		String[] strColNames = strColumnName.split(":");
			String strFirstCol = strColNames[0];
			String strSecondCol = strColNames[1];
			String strActValue = "";
			String tableHeaderDeciderVal = "";
			int intColumnIndex = 0;
			WebElement eleRows = null;
			By row = null;
			if (strValue.contains("CMDB Inheritance"))
			{
				 row = Common.tableRowCMDB;
				 String[] strVal =strValue.split("!");
				 strValue = strVal[0];
			}
			else
			{
				 row = Common.tableRow;
			}
			
			String strTableName = "LookUp Table";
			if(strValue.contains("NavigationTable"))
			{
				 intColumnIndex = getColumnIndex(strFirstCol, Common.navigationTableHeader, strTableName, false, false) + 3;
				if (!(intColumnIndex != 0)) {
					ALMFunctions.ThrowException("Get index of column name",
							"Expected column name as " + strFirstCol + " shall be displayed",
							"Expected column name as " + strFirstCol + " is not displayed", true);
				}
				 String[] strVal =strValue.split("!");
				 strValue = strVal[0];
				 tableHeaderDeciderVal = strVal[1];
			}
			else
			{
				 intColumnIndex = getColumnIndex(strFirstCol, Common.tableHeader, strTableName, false, false) + 3;
				if (!(intColumnIndex != 0)) {
					ALMFunctions.ThrowException("Get index of column name",
							"Expected column name as " + strFirstCol + " shall be displayed",
							"Expected column name as " + strFirstCol + " is not displayed", true);
				}
				
				
			}
			
			if(objectExists(uimap.Common.firstPage, "isEnabled", lngMinTimeOutInSeconds, "Next", "Button",strTableName, false)) {
				click(uimap.Common.firstPage, timeOutInSeconds, "Next", "Button", strTableName, false);
			}
			
			boolean blnFound = false;
			boolean blnClick = false;
			int intCurrentPage = 1;
			List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
			if (listtableRow.isEmpty()) {

				ALMFunctions.ThrowException(strValue, strValue + " table row should be displayed",
						strValue + " table row are not displayed", true);
				ALMFunctions.ThrowException(strValue, "" + strTableName + " Table row should be displayed",
						"" + strTableName + " Table row is NOT displayed", true);

			} else {

				boolean blnRecordNotFound = false;
				do {

					ALMFunctions.Screenshot();
					if (intCurrentPage != 1) {

						if (objectExists(uimap.Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",strTableName, false)) {
							click(uimap.Common.nextPage, timeOutInSeconds, "Next", "Button", strTableName, false);
							driverUtil.waitUntilStalenessOfElement(uimap.Common.nextPage, strTableName);

						} else {
							blnRecordNotFound = true;
						}

					}
					// driverUtil.waitUntilStalenessOfElement(row, strTableName);

					listtableRow = driver.getWebDriver().findElements(row);

					for (WebElement rows : listtableRow) {

						strActValue = rows.findElement(By.xpath(".//*[local-name()='th' or local-name()='td'][" + intColumnIndex + "]")).getText().trim();

						if (strActValue.startsWith(strValue)) {

							eleRows = rows;
							blnFound = true;

						}

						if (blnFound) {
							int intColumnIndex1 =0;
							if(tableHeaderDeciderVal.equals("NavigationTable"))
							{
								 intColumnIndex1 = getColumnIndex(strSecondCol, Common.navigationTableHeader, strTableName, false, false) + 3;
								if (!(intColumnIndex1 != 0)) {
									ALMFunctions.ThrowException("Get index of column name",
											"Expected column name as " + strSecondCol + " shall be displayed",
											"Expected column name as " + strSecondCol + " is not displayed", true);
								}
							}
							else {
								 intColumnIndex1 = getColumnIndex(strSecondCol, Common.tableHeader, strTableName, false, false) + 3;
								if (!(intColumnIndex1 != 0)) {
									ALMFunctions.ThrowException("Get index of column name",
											"Expected column name as " + strSecondCol + " shall be displayed",
											"Expected column name as " + strSecondCol + " is not displayed", true);
									}
							}
							
							WebElement eleClick = eleRows.findElement(new Common(String.valueOf(intColumnIndex1)).tableLink);
							pagescroll(eleClick, strPageName);
							ALMFunctions.Screenshot();
							//WebElement eleClick = eleRows.findElement(Common.tblLink);
							driver.capture(strTableName);
							clickByJS(eleClick, intCurrentPage, strSecondCol, strTableName, strPageName, true);
							//eleClick.click();
							blnClick = true;
							break;

						}

					}

					intCurrentPage++;
				} while (!(blnClick || blnRecordNotFound));

				if (blnRecordNotFound) {
					ALMFunctions.ThrowException(strValue, strValue + " value should be display in table row",
							"Error - Specified Record " + strValue + " is not found in the " + strTableName + " table",
							true);

				}

				if (blnClick) {
					report.updateTestLog("Click on " + strActValue + " in table row",
							strActValue + " link is clicked in " + strTableName, Status.DONE);

				} else {
					ALMFunctions.ThrowException("Click on " + strValue + " Table row",
							"Created From Trigger : " + strActValue + " in " + strTableName
									+ " Table row should be clicked on " + strTableName + "",
							"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
				}

			}

		}
		
		
		
	public void rightclickOnBreadCrumbandselect(String strLink, String strValue, String strPageName) {
		  //Common objLink = new Common(strLink);
		mouseOverandRightClick(uimap.Common.tableBreadcrumbLink, lngMinTimeOutInSeconds, strLink, strLink , strPageName, false);
	//	rightClickAndVerify(strLink, strValue,strPageName);
		//Actions A = new Actions(driver.getWebDriver());
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
		By label = null;
		label = new Common(strValue).label;
		if (objectExists(label, "isEnabled", lngMinTimeOutInSeconds, strLink, "Link",strPageName, false)) {
		click(label, timeOutInSeconds, strValue, "Button", strPageName,true);
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
		
		}
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
		//A.moveToElement(driver.findElement(Common.clickApprove)).click().build().perform();
		}	
		
		
		
	public void righClickFormandSelectmultiple(String strElementName, String strValue, String strPageName)
	{
		By button = null;
		By label = null;
		By label1 = null;
		String[] strinputPara = strValue.split("!");
		String strLabel1 = strinputPara[0];
		String strLabel2 = strinputPara[1];
		button = new Common(strElementName).CommentsButton;
		label = new Common(strLabel1).label;
		label1 = new Common(strLabel2).label;
		
		windowName.put("Window1", driver.getWindowHandle());
		manageAndSwitchNewWindow();
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
		mouseOverandRightClick(button, lngMinTimeOutInSeconds, "Save", "Button", strPageName, true);
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
		if (objectExists(label, "isEnabled", lngMinTimeOutInSeconds, strElementName, "Button",strPageName, false)) {
		click(label, timeOutInSeconds, strLabel1, "Button", strPageName,true);
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
		
		}
		if (objectExists(label1, "isEnabled", lngMinTimeOutInSeconds, strElementName, "Button",strPageName, false)) {
			click(label1, timeOutInSeconds, strLabel2, "Button", strPageName,true);
			driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
			
			}
		driverUtil.waitFor(lngMinTimeOutInSeconds, "Save", "Button", strPageName);
	}	
		
		

		
		
	public void rowNoComparison(String strLabel, String fieldName, String strPageName)
	{	
		
		
		
		if (objectExists(Common.totalRows, "isDisplayed", lngPagetimeOutInSeconds, strLabel,"Label", strPageName, true)) {
			String strValue = getText(Common.totalRows, lngPagetimeOutInSeconds, "Case Number", strPageName);
			System.out.println(strValue);
			if(strValue.equalsIgnoreCase(fieldName)) {
				
				report.updateTestLog(strLabel, "Expected " + strLabel + " value should be display as : <br>" + fieldName
						+ "<br>" + "Actual " + strLabel + " value is displayed as : <br>" + strValue, Status.PASS);
				
				
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Expected",
						strLabel + " Count should be matching : " + fieldName,
						strLabel + " value is matching with : " + strValue, true);
				
			}
			else
			{
				{
					report.updateTestLog(strLabel, "Expected " + strLabel + " value should be display as : <br>" + fieldName
							+ "<br>" + "Actual " + strLabel + " value is displayed as : <br>" + strValue, Status.WARNING);
					
				}
			}
			closeAndSwitchPreviousWindow();
			frameSwitchAndSwitchBack(Common.frame, "switchframe", strPageName);
			
			
			
		}
		
		
		
	}	
		
		
	public void FillInputForm(String strSheetName, String strColumnName, String strConcatenationFlagColumn,
			String strScenario, String strDelimiter, boolean blnIncludeDelimiter, boolean blnInput) {
		String strInputParameters = getConcatenatedStringFromExcel(strSheetName, strColumnName,
				strConcatenationFlagColumn, strScenario, strDelimiter, blnIncludeDelimiter, blnInput);
		String[] arrParameters_Vs_Value = strInputParameters.split("~");
		for (int j = 0; j < arrParameters_Vs_Value.length; j++) {
			if (arrParameters_Vs_Value[j].trim().length() > 0) {
				String[] arrParameters = StringUtils.split(arrParameters_Vs_Value[j], ";");

				String strElementType = "";
				String strSection = "";
				String strElementName = "";
				String strValues = "";
				String strWindowName = "";
				String strPageName = "";
				strInputParameters = getConcatenatedStringFromExcel(strSheetName, strColumnName,
						strConcatenationFlagColumn, strScenario, strDelimiter, blnIncludeDelimiter, blnInput);
				arrParameters_Vs_Value = strInputParameters.split("~");

				for (int i = 0; i < arrParameters.length; i++) {
					switch (StringUtils.substringBefore(arrParameters[i], "=").toLowerCase()) {
					case "element type":
						strElementType = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "element label":
						strElementName = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "section name":
						strSection = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "element value":
						strValues = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "window name":
						strWindowName = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "page name":
						strPageName = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "storage":
						break;

					default:
						ALMFunctions.ThrowException("Test Data",
								"Only Pre-Defined Form Options must be provided in the test data sheet",
								"Error - Unhandled Form Options " + StringUtils.substringBefore(arrParameters[i], "="),
								false);
					}
				}

				if (strValues.trim().length() > 0) {
					switch (strElementType.toLowerCase()) {
					case "textarea":
						if (strWindowName.length() > 0) {
							dialogTextArea(strWindowName, strElementName, strValues, strPageName);
						} else {
							enterTextArea(strElementName, strValues, strPageName);
						}
						break;
					case "textbox":
						if (strWindowName.length() > 0) {
							if (strElementName.length() > 0) {
								enterSparcTextbox(strElementName, strValues, strPageName);
							} else {
								dialogTextbox(strWindowName, strValues, strPageName);
							}

						} else {
							enterTextbox(strElementName, strValues, strPageName);
						}

						break;

					case "textboxlist":
						break;
					case "attachfile":
						attachFile(strElementName, strValues, strPageName);
						break;
					case "combobox":
						if (strSection.length() > 0) {
							comboBox(strSection, strElementName, strValues, strPageName);
						} else if (strWindowName.length() > 0) {
							dialogCombobox(strWindowName, strElementName, strValues, strPageName);
						} else {
							comboBoxLookup(strElementName, strValues, strPageName);
						}
						break;
					case "esccombobox":
						esccomboBox(strElementName, strValues, strPageName);
						break;

					case "comboboxtreeview":
						expandTreeView(strElementName, strValues, strPageName);
						break;
					case "unlockcombobox":
						unlockComboBox(strElementName, strValues, strPageName);
						break;
					case "dialogtextarea":
						dialogTextArea(strWindowName, strElementName, strValues, strPageName);
						break;
					case "multisearch":
						multiSearchValue(strElementName, strValues, strPageName);
						break;
					case "buttonnotexist":
						verifyButtonNotExist(strValues, strPageName);
						break;
					case "dropdown":
						dropDown(strElementName, strValues, strPageName);
						break;
					case "select":
						selectDropdown(strElementName, strValues, strPageName);
						break;
					case "spselect":
						spSelectDropdown(strElementName, strValues, strPageName);
						break;
					case "dialogdropdown":

						if (strWindowName.length() > 0) {
							dialogDropDown(strWindowName, strElementName, strValues, strPageName);
						} else {
							dialogDropDown(strWindowName, strElementName, strValues, strPageName);
						}
						break;
					case "dialogsearch":
						dialogSearch(strElementName, strValues, strPageName);
						break;
					case "verifyattachment":
						verifyAttachment(strValues, strPageName);
						break;
					case "cleartextbox":
						clearTextbox(strElementName, strValues, strPageName);
						break;
					case "verifyattachmentsection":
						verifyReqItemAttachment(strElementName, strValues, strPageName);
						break;
					case "combotextbox":
						comboTextBox(strElementName, strValues, strPageName);
						break;
					case "dialogcheckbox":
						dialogCheckBox(strValues, strPageName);
						break;
					case "selectrecord":
						selectRecord(strValues);
						break;
					case "verifystate":
						verifyStatus(strValues, strPageName);
						break;
					case "getreqnum":
						getReqNum(strElementName, strValues, strPageName);
						break;
					case "fieldexist":
						verifyFieldExist(strElementName, strValues, strPageName);
						break;
					case "titlecard":
						titleCard(strElementName, strValues, strPageName);
						break;
					case "breadcrumblink":
						breadcrumbLink(strElementName, strValues, strPageName);
						break;
					case "navlink":
						navLink(strValues, strPageName);
						break;
					case "button":
						if (strWindowName.length() > 0) {
							dialogbutton(strWindowName, strValues, strPageName);
						} else if (strSection.length() > 0) {
							tabButton(strValues, strPageName);
						} else if (strValues.equalsIgnoreCase("back")) {
							backButton(strValues, strPageName);
						} else if (strValues.equalsIgnoreCase("personalization")) {
							personalizationButton(strValues, strPageName);
						} else if (strElementName.equalsIgnoreCase("navigation")) {
							navigationButton(strValues, strPageName);
						} else if (strElementName.equalsIgnoreCase("logo")) { // amit 06-01-2022
							logoButton(strValues, strPageName);
						} else if (strElementName.equalsIgnoreCase("info")) { // amit 07-01-2022
							infoButton(strValues, strPageName);
						} else if (strElementName.equalsIgnoreCase("filter")) {
							filterButton(strValues, strPageName);
						} else {
							button(strValues, strPageName);
						}
						break;
					case "dropdownarrowwithoutname":
						dropDownArrowWithoutName(strElementName, strValues, strPageName);
						break;

					case "navback":
						navigateBack(strValues, strPageName);
						break;
					case "refresh":
						refreshPage(strValues, strPageName);
						break;
					case "tablelinktext":
						findRecordAndCaptureID(strElementName, strValues, strPageName);
						break;
					case "imgbutton":
						imgButton(strElementName, strValues, strPageName);
						break;
					case "verifyknownerror":
						verifyKnownError(strElementName, strValues, strPageName);
						break;
					case "pagescroll":
						pagescroll(strElementName, strValues, strPageName);
						break;
					case "verifyautopopulatedvalues":
						verifyAutoPopulatedValue(strElementName, strValues, strPageName);
						break;
					case "getalertmsg":
						getAlertMsg(strValues, strPageName);
						break;
					case "textcontentbody":
						enterTextContentBody(strElementName, strValues, strPageName);
						break;
					case "norecord":
						verifyNoRecordDisplayInTable(strValues);
						break;
					case "norecordwithcondition":
						verifyNoRecordForAppliedCondtion(strElementName, strValues, strPageName);
						break;
					case "closetab":
						closeTab(strValues, strPageName);
						break;
					case "switchtab":
						switchTab(strValues, strPageName);
						break;
					case "dialogradiobutton":
						dialogSelectRadioButton(strElementName, strValues, strPageName);
						break;
					case "prefilltext":
						preFilledText(strElementName, strValues, strPageName);
						break;
					case "screenshot":

						report.updateTestLog("Screen Shot", "Screen Shot Captured in " + strPageName,
								Status.SCREENSHOT);
						ALMFunctions.Screenshot();
						break;
					case "commentsbutton":
						cmtbutton(strValues, strPageName);
						break;
					case "mailtextbox":
						mailTextbox(strElementName, strValues, strPageName);
						break;
					case "link":
						link(strValues, strPageName);
						break;
					case "linkexist":
						verifyLinkExist(strValues, strPageName);
						break;
					case "getlinktext":
						getLinkText(strElementName, strValues, strPageName);
						break;
					case "tab":
						tab(strValues, strPageName);
						break;
					case "alert":
						acceptAlert(lngMinTimeOutInSeconds);
						break;
					case "checkbox":
						By locator = null;

						if (strWindowName.length() > 0) {
							locator = new uimap.Common(strWindowName, strElementName, strValues).checkbox;
							checkbox(locator, strValues, strPageName);

						} else {
							locator = new uimap.Common(strValues).checkbox;
							checkbox(locator, strValues, strPageName);
						}
						break;
					case "datepicker":
						datePicker(strElementName, strValues, strPageName);
						break;
					case "errorhandling":
						errorHandling(strValues, strPageName);
						break;

					case "getcasenumber":
						getCaseNumber(strElementName, strValues, strPageName);
						break;

					case "surveychoice":
						surveyChoice(strElementName, strValues, strPageName);
						break;
					case "listitem":
						clickListItem(strElementName, strValues, strPageName);
						break;
					case "menu":
						clickMenu(strValues, strPageName);
						break;
					case "frame":
						if (strValues.equalsIgnoreCase("switchframe") || strValues.equalsIgnoreCase("default")) {
							frameSwitchAndSwitchBack(Common.frame, strValues, strPageName);
						} else if (strValues.equalsIgnoreCase("dialog")) {
							frameSwitchAndSwitchBack(Common.dialogFrame, strValues, strPageName);
						} else {
							locator = new uimap.Common(strElementName).commoniframe;
							frameSwitchAndSwitchBack(locator, strValues, strPageName);
						}

						break;
					case "surveyframe":
						frameSwitchAndSwitchBack(Common.surveyFrame, strValues, strPageName);
						break;
					case "verifytasktype":
						verifyTaskTypeState(strElementName, strValues, strPageName);
						break;

					case "globalsearch":
						globalSearch(strValues, strPageName);
						break;

					case "retrieverecordfromtable":
						retrieveRecordFromTable(strElementName, strValues, strPageName);
						break;
					case "searchtextbox":
						searchTextBox(strElementName, strValues, strPageName);
						break;
					case "textboxsearch":
						textBoxsearch(strValues, strPageName);
						break;

					case "alertaccepting":
						alertHandling(strPageName);
						break;
					case "alerttextverification":
						alertTextVerification(strValues, strPageName);
						break;
					case "verifyobjectstate":
						verifyObjectState(strElementName, strValues, strPageName);
						break;
					case "findrecordandverifyintable":
						findRecordAndVerifyInTable(strElementName, strValues, strPageName);
						break;

					case "verifymessage":
						verifyMessage(strValues, strPageName);
						break;
					case "verifyrecordnotexistintbl":
						verifyRecordNotExistInTbl(strElementName, strValues, strPageName);
						break;

					case "retrieverecordfromactivitylog":
						retrieveRecordFromActivityLog(strElementName, strValues, strPageName);
						break;
					case "multiplevaluedropdown":
						multipleValuedropDown(strElementName, strValues, strPageName);
						break;
					case "partiallink":
						partialLink(strValues, strPageName);
						break;
					case "gettextboxvalue":
						getValueFromTextBox(strElementName, strValues, strPageName);
						break;
					case "multivaldropdownwithoutname":
						selectMultipleValueDropdownWithoutName(strElementName, strValues, strPageName);
						break;
					case "lookupsearch":
						lookupSearch(strElementName, strValues, strPageName);
						break;
					case "findrecordandclick":
						findRecordAndClick(strElementName, strValues, strPageName);
						break;
					case "findpartialrecordandclick":
						findPartialRecordAndClick(strElementName, strValues, strPageName);
						break;
					case "findrecordandnavigate":
						findRecordAndNavigate(strValues, strPageName);
						break;
					case "waitforloading":
						waitForLoading(strElementName, strValues, strPageName);
						break;
					case "rightclickform":
						righClickForm(strElementName, strValues, strPageName);
						break;
					case "sortbydescending":
						sortByDescending(strElementName, strValues, strPageName);
						break;
					case "customizingcolumn":
						customizingColumn(strElementName, strValues, strPageName);
						break;
					case "filter":
						filter(strElementName, strValues, strPageName);
						break;

					case "upload":
						UploadFileAutoIT(strElementName, strValues, strPageName);
					case "sptextcontentbody":
						spEnterTextContentBody(strElementName, strValues, strPageName);
						break;
					case "rightclickandverify":
						rightClickAndVerify(strElementName, strValues, strPageName);
						break;
					case "rightclickandselect":
						rightclickandselect(strElementName, strValues, strPageName);
						break;
					case "clickonsparclabel":
						clickOnSparcLabel(strElementName, strPageName);
						break;
					case "verifylabelexist":
						verifyLabelExist(strElementName, strValues, strPageName);
						break;
					case "spdropdownbutton":
						spDropDownbutton(strElementName, strValues, strPageName);
						break;
					// amit 05-01-2022
					case "getvaluefromdropdown":
						getDisplayedFromDropdown(strElementName, strValues, strPageName);
						break;
					// amit 05-01-2022
					case "verifylinks":
						clickOnLinkAndVerify(strValues, strPageName);
						break;
					// amit 05-01-2022
					case "comparetext":
						compareText(driver.findElement(new Common(strElementName).anyLabelValue), strValues);
						break;

					case "getvalfromspbutton":
						getValueFromspDropDownButton(strElementName, strValues, strPageName);
						break;
					case "validto":
						validTo(strElementName, strValues, strPageName);
						break;
					case "verifysearchtextboxandselectvalue":
						verifySearchTextboxandSelectValue(strElementName, strValues, strPageName);
						break;
					case "cardlabelandclick":
						clickonCardlabel(strElementName, strValues, strPageName);
						break;
					case "getcomboboxvalue":
						getValueFromComboBox(strElementName, strValues, strPageName);
						break;
					case "getunlockcomboboxvalue":
						getValueFromUnlockCombobox(strElementName, strValues, strPageName);
						break;
					case "rateonarticle":
						clickonlabel(strElementName, strValues, strPageName);
						break;
					case "geteditabletextcontentbodyvalue":
						getValueFromEditableTextContentBody(strElementName, strValues, strPageName);
						break;
					case "validatecasenumber":
						validateCaseNumber(strElementName, strValues, strPageName);
						break;
					case "verifyactivities":
						verifyActivities(strElementName, strValues, strPageName);
						break;
					case "validateemailbody":
						validateEmailBody(strElementName, strValues, strPageName);
						break;
					case "verifymandatoryfieldexist":
						verifyMandatoryFieldExist(strElementName, strValues, strPageName);
						break;
					case "verifyenabledtextboxstate":
						verifyenabledTextBoxStatus(strValues, strPageName);
						break;
					case "verifyselecteddropdownvalue":
						verifySelectedDropdownValue(strElementName, strValues, strPageName);
						break;
					case "dialogconfirm":
						dialogConfirm(strValues, strPageName);
						break;
					case "gettextareavalue":
						getValueFromReadonlyTextArea(strElementName, strValues, strPageName);
						break;
					case "edittablerecords":
						editTableRecords(strElementName, strValues, strPageName);
						break;
					case "gettextcontentbodyvalue":
						getValueFromTextContentBody(strElementName, strValues, strPageName);
						break;
					case "sparcdialogsearch":
						sparcdialogSearch(strElementName, strValues, strPageName);
						break;
					case "verifytag":
						verifyTag(strElementName, strValues, strPageName);
						break;
					default:

						ALMFunctions.ThrowException("Test Data",
								"Only Pre-Defined Fields Type must be provided in the test data sheet",
								"Error - Unhandled Field Type " + strElementType, false);
					}
				}
				// }
			}
		}
	}	
		
	public void verifyActivities(String strLabel, String strValue, String strPageName) {
		String strGetEnteredValue = getText(new Common(strLabel).activities, lngMinTimeOutInSeconds, strLabel,
		strPageName);
		if (strGetEnteredValue.equals(strValue)) {
		report.updateTestLog(strLabel + " Text Field", strValue + " should be popualted as expected <br>" + "<br>"
		+ strLabel + " is populated with the Value" + strGetEnteredValue + " in " + strPageName,
		Status.PASS);
		}
		else
		{
		report.updateTestLog(strLabel + " Text Field", strValue + " should be popualted as expected <br>" + "<br>"
		+ strLabel + " is not populated with the Value" + strGetEnteredValue + " in " + strPageName,
		Status.FAIL);
		}
		}
		
	
	
	
	//Yash 16-02-2022
	public void onHoldButton(String strButtonLabel, String strPageName)



	{
	if(objectExists(Common.onHold, "isDisplayed", timeOutInSeconds, "Filter", "Button", "", false))
	{
	driverUtil.waitUntilStalenessOfElement(Common.onHold, lngMinTimeOutInSeconds, strPageName);
	clickByJS(Common.onHold, lngMinTimeOutInSeconds, "Filter", "Button",strPageName, true);
	}


	}
	
	public void verifyRolesOrGroups(String strElementValue, String type) {
		String[] RolesOrGroups = strElementValue.split(",");
		if (type.equals("Roles")) {
			VerifyRolesorGroups(RolesOrGroups, RolesOrGroups.length, "sys_user_has_role_table", type);
		} else {
			VerifyRolesorGroups(RolesOrGroups, RolesOrGroups.length, "sys_user_grmember_table", type);
		}
	}

	public void VerifyRolesorGroups(String[] Roles, int passingCount, String tableid, String type) {
		// int index1=getColumnIndex1("//table[@id='"+tableid+"']//th", type, false);

		int FromPreReq = Roles.length;
		System.out.println("Values Passing from Prerequrisite Properties :" + FromPreReq);
		System.out.println("Count Passing from Method :" + passingCount);

		if (!(FromPreReq == passingCount))
			// Reporter.reportStep("The Values are passing from Prerequsite :"+FromPreReq+"
			// and Value passing from Method :"+passingCount+" are missmatch Please verify",
			// "WARNING");
			report.updateTestLog("The Values are passing from Prerequsite :" + FromPreReq
					+ " and Value passing from Method :" + passingCount + " are missmatch Please verify", "WARNING",
					Status.WARNING);
		List<WebElement> Data = driver.getWebDriver().findElements(
				By.xpath("//table[@id='" + tableid + "']//tbody[@class='list2_body']//td[@colspan='99']"));
		int i = 0;
		int l = 0;
		boolean bln = false;

		List<String> Role = new ArrayList<>();
		List<String> RoleNotPresent = new ArrayList<>();
		List<String> RolePresent = new ArrayList<>();

		List<String> RoleNotPresent1 = new ArrayList<>();
		List<String> RolePresent1 = new ArrayList<>();
		List<String> RoleExtraPresent = new ArrayList<>();
		for (WebElement e : Data) {
			String s = null;
			if (e.getText().contains("Role:")) {
				s = e.getText().replaceAll("Role: ", "").replaceAll("\\(.*\\)", "").trim().toLowerCase();
			}
			if (e.getText().contains("Group:"))
				s = e.getText().replaceAll("Group: ", "").replaceAll("\\(.*\\)", "").trim().toLowerCase();
			System.out.println("Role is :" + s);
			Role.add(i, s);
			i++;
			String val = e.getText();
			pagescroll(new uimap.Common(val).link, driver.getTitle());
			ALMFunctions.Screenshot();
			
		}

		List<String> element = new ArrayList<>();
		for (int j = 0; j < Roles.length; j++) {
			element.add(l, Roles[j].toLowerCase());
			l++;
		}

		// List<String>element=Arrays.asList(Roles);
		System.out.println("Element (PASSING) Size :" + element.size());
		System.out.println("Role (GETTING) Size :" + Role.size());
		System.out.println("Element is : " + String.join(",", element));
		System.out.println("Role is : " + String.join(",", Role));

		if (element.size() != Role.size()) {

			if (element.size() > Role.size()) {
				for (String s : element) {
					if (Role.contains(s)) {
						RolePresent1.add(s);
					} else {
						RoleNotPresent1.add(s);
					}

				}
				// Reporter.reportStep("The "+type+":"+String.join(", ", RoleNotPresent1)+" are
				// NOT Present check", "WARNING");
				report.updateTestLog(
						"The " + type + ":" +  String.join(", ", RoleNotPresent1) + " are NOT Present check", "WARNING",
						Status.WARNING);
			} else {
				System.out.println("Role size is greather than element size");
				for (String s : Role) {
					if (element.contains(s)) {
						RolePresent1.add(s);
					} else
						RoleExtraPresent.add(s);
				}
				report.updateTestLog(
						"The " + type + ":" + String.join(", ", RoleExtraPresent) + " are Extra please check",
						"WARNING", Status.WARNING);
				// Reporter.reportStep("The "+type+":"+String.join(", ", RoleExtraPresent)+" are
				// Extra please check", "WARNING");
			}

		} else {
			for (int j = 0; j < Role.size(); j++) {
				for (int k = 0; k < element.size(); k++) {
					if (Role.get(j).contains(element.get(k))) {
						bln = true;
						RolePresent.add(Role.get(j));
						break;
					} else {
						bln = false;
					}

				}

				if (!bln) {
					RoleNotPresent.add(Role.get(j));
					System.out.println("Not present : " + Role.get(j));
				}
			}
			if (RoleNotPresent.isEmpty())
				/*
				 * Reporter.reportStep("The "+type+": "+String.join(", ", RolePresent)+
				 * " are present as expected", "SUCCESS");
				 */
		ALMFunctions.UpdateReportLogAndALMForPassStatus("The " + type + ": " + String.join(", ", RolePresent) + " should be presented as expected" + type + ": " + String.join(", ", RolePresent) + " are present as expected","Pass"," "	,true);
			else
				report.updateTestLog(
						"The " + type + ":" + String.join(", ", RoleNotPresent) + " are Extra please check", "WARNING",
						Status.WARNING);
			// Reporter.reportStep("The "+type+":"+String.join(", ", RoleNotPresent)+" are
			// Extra please check", "WARNING");
			
		}

	}
	
	public void verifyWarningIcon(String strLabel, String strValue, String strPageName) {
		if(objectExists(new uimap.Common(strLabel).warningicon, "isDisplayed", timeOutInSeconds, strLabel, strValue, strPageName, false)) {
		ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify " +strLabel+ "is VIP user", strLabel+ " should be VIP user as expected", strLabel+ " is VIP user", true);
		}
		}
	
	
	public void fsm() {
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters","Concatenate_Flag_Input", "FSM", "~", true, true);

		FillInputForm(strInputParameters);

		}
	
	
	
	

}

