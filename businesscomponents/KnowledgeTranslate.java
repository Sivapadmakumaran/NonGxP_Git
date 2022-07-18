package businesscomponents;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import com.cognizant.framework.Status;


import supportlibraries.ScriptHelper;
import uimap.Common;
import uimap.EscPortal;

public class KnowledgeTranslate extends CommonFunctions {

	public KnowledgeTranslate(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}

	private final long timeOutInSeconds = Long.parseLong(properties.getProperty("ObjectSyncTimeout"));
	private final long lngPagetimeOutInSeconds = Long.parseLong(properties.getProperty("PageLoadTimeout"));
	private final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));

	public void verifyKnowTranslate() {
		try {
			String strTranslateLang = "";
			String strArticleContent = "";
			String strExpecetedPBC="";
			String strActualPBC="";
			String strPageName = "Employee Service Center";
			String strRecordNumber = dataTable.getExpectedResult("Case Number");
			String strDescription = dataTable.getExpectedResult("Description");
			
			String strTranslateData=dataTable.getExpectedResult("Translate_Content");
			String[] strTranslate = strTranslateData.split("!");
			
			String strArticleDes=dataTable.getExpectedResult("Article_Data");
			String[] strArticleDescription = strArticleDes.split("!");
			
			String[] strBreadCrumbs=dataTable.getExpectedResult("Page_BreadCrumbs").split("!");

			/*if (objectExists(new Common(strSPRButton).button, "isDisplayed", lngMinTimeOutInSeconds, strSPRButton,
					"Button", strPageName, false)) {
				click(new Common(strSPRButton).button, lngPagetimeOutInSeconds, strSPRButton, "Button", strPageName,
						true);
			}*/
			windowName.put("Window1", driver.getWindowHandle());
			manageAndSwitchNewWindow();
			driver.capture(strPageName);
			driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
			//driverUtil.waitUntilPageLoaded(strPageName);
			driverUtil.waitUntilElementVisible(Common.sparcPortTextbox, lngPagetimeOutInSeconds, "Record Search",
					"Textbox", strPageName, false);
			sendkeys(Common.sparcPortTextbox, lngPagetimeOutInSeconds, strRecordNumber, "Search", strPageName);
			click(Common.sparcPortSearchButton, lngPagetimeOutInSeconds, "Search", "Button", strPageName, true);
			driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
			driverUtil.waitUntilElementVisible(new Common(strDescription).translateSearchLink, lngMinTimeOutInSeconds,
					"Description", "Link", strPageName, false);
			click(new Common(strDescription).translateSearchLink, lngMinTimeOutInSeconds, "Description", "Link",
					strPageName, true);
			driverUtil.waitUntilStalenessOfElement(new Common(strDescription).translateSearchLink, strPageName);
			ALMFunctions.Screenshot();
			driver.capture("Verify Knowledge translate data");
			
			List<WebElement> strPageBreadCrumbs	=driver.getWebDriver().findElements(EscPortal.PageBreadCrumbs);
			
			for(String strExpPBC:strBreadCrumbs) {
				strExpecetedPBC=strExpecetedPBC.concat(strExpPBC+">");
				
			}
			for(WebElement strActPBC:strPageBreadCrumbs) {
				strActualPBC=strActualPBC.concat(strActPBC.getText().trim()+">");
				
			}
			
			
			if(strPageBreadCrumbs.size()==strBreadCrumbs.length&&strActualPBC.equals(strExpecetedPBC))
			{
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Page BreadCrumbs", 
						"Page BreadCrumbs should be display as <br>"+strExpecetedPBC, 
						"Page BreadCrumbs is displayed as <br>"+strActualPBC, true);
				
				
			}else {
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify Page BreadCrumbs", 
						"Page BreadCrumbs should be display as <br>"+strExpecetedPBC, 
						"Page BreadCrumbs is displayed as <br>"+strActualPBC, true);
				
			}
			
			if(!strArticleDes.isEmpty()) 
			{
				String strShortDes=StringUtils.substringAfter(strArticleDescription[0], "=");
				String strArticleBody=StringUtils.substringAfter(strArticleDescription[1], "=");
				String strShortDesText=getText(Common.kbTitleHeader, lngPagetimeOutInSeconds,
						"Short Description", strPageName).trim();
						String strActualArticleContent = getText(Common.articleContent, lngPagetimeOutInSeconds,
								"Article Content", strPageName).trim();
				if(strShortDesText.equals(strShortDes)&&(strActualArticleContent.equals(strArticleBody))) 
				{
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify KB Data", 
							"Knowledge Article data should be display as : <br>"+"<br>"+strShortDes+"<br>"+strArticleBody  ,
							"Knowledge Article data is displayed as : <br>"+"<br>"+strShortDesText+"<br>"+strActualArticleContent, true);
					report.updateTestLog("Verify KB Data","Knowledge Article data should be display as : <br>"+"<br>"+strShortDes+"<br>"+strArticleBody +"<br>"+
							"Knowledge Article data is displayed as : <br>"+"<br>"+strShortDesText+"<br>"+strActualArticleContent, Status.PASS);
					
				}else {
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify KB Data", 
							"Knowledge Article data should be display as : <br>"+"<br>"+strShortDes+"<br>"+strArticleBody  ,
							"Knowledge Article data is displayed as : <br>"+"<br>"+strShortDesText+"<br>"+strActualArticleContent, true);
					report.updateTestLog("Verify KB Data","Knowledge Article data should be display as : <br>"+"<br>"+strShortDes+"<br>"+strArticleBody +"<br>"+
							"Knowledge Article data is displayed as : <br>"+"<br>"+strShortDesText+"<br>"+strActualArticleContent, Status.FAIL);
					
				}
						
						
						
			}
			
			if(!strTranslateData.isEmpty()) {
			for (String strTranslateContent : strTranslate) {
				String[] strInputs = strTranslateContent.split(";");
				strTranslateLang = StringUtils.substringAfter(strInputs[0], "=");
				strArticleContent = StringUtils.substringAfter(strInputs[1], "=");
				if (objectExists(Common.avaliLanguageLink, "isDisplayed", lngMinTimeOutInSeconds,
						strTranslateLang, "Link", strPageName, false)) {
					click(Common.avaliLanguageLink, lngMinTimeOutInSeconds, strTranslateLang, "Link",
							strPageName, true);
					listValueSelect(Common.languageListItem, "Language",lngMinTimeOutInSeconds, strTranslateLang, "List", strPageName);
					driverUtil.waitUntilPageReadyStateComplete(lngPagetimeOutInSeconds, strPageName);
					driverUtil.waitUntilElementVisible(Common.articleContent, lngPagetimeOutInSeconds,
						"Article Contetnt","Text", strPageName,false);
				String strActualArticleContent = getText(Common.articleContent, lngPagetimeOutInSeconds,
						"Article Contetnt", strPageName);
				if (strActualArticleContent.equals(strArticleContent)) {
					ALMFunctions.UpdateReportLogAndALMForPassStatus(strTranslateLang,
							 "Article content should display  as " + strArticleContent+ " in "+ strTranslateLang+ " language",
							 "Article content is display  as " + strActualArticleContent+ " in "+ strTranslateLang+ " language",
							true);
				} else {

					ALMFunctions.UpdateReportLogAndALMForFailStatus(strTranslateLang,
							 "Article content should display  as " + strArticleContent+ " in "+ strTranslateLang+ " language",
							 "Article content is display  as " + strActualArticleContent+ " in "+ strTranslateLang+ " language",
							true);
				}
				
				} else {

					ALMFunctions.ThrowException(strTranslateLang, "Translate language should be display in the page",
							"Error - " + strTranslateLang + " No sunch language is found in the page", true);

				}

			}
		}
			closeAndSwitchPreviousWindow();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
