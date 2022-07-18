package businesscomponents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cognizant.framework.FileLockMechanism;
import com.cognizant.framework.Status;

import decrypter.Password_Decrypter;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import uimap.Common;

public class CommonActionsAndFunctions extends ReusableLibrary {
	private final long lngPagetimeOutInSeconds = Long.parseLong(properties.getProperty("PageLoadTimeout"));
	private final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));

	public CommonActionsAndFunctions(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	/**
	 * Function to input data in text box
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 * @param Text
	 *            Expected text to be passed to the input field
	 * @param elementName
	 *            The name of the element in which the expected text to be
	 *            entered
	 * @param pageName
	 *            Page in which title is to be compared with expected title
	 */
	public void sendkeys(By locator, long timeOutInSeconds, String Text, String elementName, String pageName) {
		if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, "input field", pageName, true)) {
			if (driverUtil.waitUntilElementVisible(locator, timeOutInSeconds, elementName, "input field", pageName,
					true)) {
				if (driverUtil.waitUntilElementEnabled(locator, timeOutInSeconds, elementName, "input field",
						pageName)) {
					try {
						driver.findElement(locator).sendKeys(Text);
						elementName = elementName.replace("*", "");
						report.updateTestLog(elementName, elementName + " entered by the user is: " + "\"" + Text + "\""
								+ " in the page " + "\"" + pageName + "\"", Status.DONE);

					} catch (Exception e) {
						ALMFunctions.ThrowException(elementName,
								"User should be able to enter text " + "\"" + Text + "\"" + " in " + "\"" + elementName
										+ "\"" + " in the page " + "\"" + pageName + "\"",
								"Below exception is thrown while trying to enter text in field " + "\"" + elementName
										+ "\"" + "<br><br>" + e.getLocalizedMessage(),
								true);
					}
				}
			}
		}
	}

	public String getValue(By locator, long lngTimeOutInSeconds, String strValue, String strPageName) {
		if (objectExists(locator, "isDisplayed", lngTimeOutInSeconds, strValue, "field", strPageName, false)) {

			return driver.findElement(locator).getAttribute(strValue);

		} else {
			ALMFunctions.ThrowException(strValue, strValue + " should be displayed in the page " + strPageName,
					"Error - " + strValue + " is not displayed in " + strPageName, true);
			return "";
		}
	}

	/**
	 * Method to get text from the element
	 * 
	 * @param locator
	 *            - By locator of the control
	 * @param lngTimeOutInSeconds
	 *            - No of seconds to wait for the control to reach its
	 *            pre-requisite
	 * @param strElementName
	 *            - Name of the control
	 * @param strPageName
	 *            - Page name which the control resides
	 * @return - Text present in the control
	 */
	public String getText(By locator, long lngTimeOutInSeconds, String strElementName, String strPageName)
	
	{
		
		String strGetText="";
		if (driverUtil.waitUntilElementEnabled(locator, lngTimeOutInSeconds, strElementName, "Label", strPageName)) 
		{
			if(driverUtil.waitUntilElementLocated(locator, lngTimeOutInSeconds, strElementName, "Label", strPageName,false)) {
			// pagescrollByActions(locator, strElementName,strPageName);
				strGetText= driver.findElement(locator).getText().trim();
			}
		} else {
			ALMFunctions.ThrowException(strElementName,
					strElementName + " should be displayed in the page " + strPageName,
					"Error - " + strElementName + " is not displayed in " + strPageName, true);
			return null;
		}
		return strGetText;
	}

	/**
	 * Method to get text from the element
	 * 
	 * @param locator
	 *            - By locator of the control
	 * @param lngTimeOutInSeconds
	 *            - No of seconds to wait for the control to reach its
	 *            pre-requisite
	 * @param strElementName
	 *            - Name of the control
	 * @param strPageName
	 *            - Page name which the control resides
	 * @return - Text present in the control
	 */
	public String getAttributeValue(By locator, long lngTimeOutInSeconds, String strAttValue, String strElementName,
			String strPageName) {
		if (driverUtil.waitUntilElementLocated(locator, lngTimeOutInSeconds, strElementName, "Label", strPageName,
				false)) {
			// pagescrollByActions(locator, strElementName,strPageName);
			return driver.findElement(locator).getAttribute(strAttValue).trim();
		} else {
			ALMFunctions.ThrowException(strElementName,
					strElementName + " should be displayed in the page " + strPageName,
					"Error - " + strElementName + " is not displayed in " + strPageName, true);
			return "";
		}
	}

	/**
	 * Function to click on element
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 * @param elementName
	 *            The name of the element in which the click operation is to be
	 *            done
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which the element exists
	 *//*
	public void click(By locator, long timeOutInSeconds, String elementName, String elementType, String pageName,
			Boolean ReportLog) {
		if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, elementType, pageName, true)) {
			if (driverUtil.waitUntilElementVisible(locator, timeOutInSeconds, elementName, elementType, pageName,
					true)) {
				if (driverUtil.waitUntilElementEnabled(locator, timeOutInSeconds, elementName, elementType, pageName)) {
					try {
						driver.findElement(locator).click();
						if (ReportLog) {
							report.updateTestLog("Click",
									elementType + " - " + elementName + " in " + pageName + " is clicked", Status.DONE);
							ALMFunctions.UpdateReportLogAndALMForPassStatus("Click",
									"User should be able to click " + "\"" + elementName + "\"" + " in the page " + "\""
											+ pageName + "\"",
									"\"" + elementName + "\"" + elementType +" is clicked in the page " + "\"" + pageName + "\"",
									true);

						}
					} catch (Exception e) {
						ALMFunctions.ThrowException(elementName,
								"User should be able to click " + "\"" + elementName + "\"" + " in the page " + "\""
										+ pageName + "\"",
								"Below exception is thrown while trying to click " + "\"" + elementName + "\""
										+ "<br><br>" + e.getLocalizedMessage(),
								true);
					}
				}
			}
		}
	}*/
	
	/**
	 * Function to click on element
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element in which the expected text to be entered
	 * @param elementType Type of the element
	 * @param pageName Page in which title is to be compared with expected title
	 */
	public void click(By locator,long timeOutInSeconds,String elementName, String elementType, String pageName,Boolean ReportLog)
	{
		try{
			 if(driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, elementType, pageName,true))
			   {
				 if(driverUtil.waitUntilElementVisible(locator, timeOutInSeconds, elementName, elementType, pageName,true))
				 {
					 if(driverUtil.waitUntilElementEnabled(locator, timeOutInSeconds, elementName, elementType, pageName))
					 {
							 
						 driver.findElement(locator).click();
						 
						 if(ReportLog)
							 report.updateTestLog("Click", elementName+" "+elementType+" in "+pageName+" is clicked",Status.DONE);
							 //report.updateTestLog("Page Navigation", "User navigated from "+"\""+pageName+"\""+" to "+"\""+elementName+"\""+" page", Status.DONE);
						 
					 }
				 }
			   }
			}
		catch(Exception e)
			{
			ALMFunctions.UpdateReportLogAndALMForFailStatus(elementName, "User should be able to click on "+elementName, "Error - Unable to click "+elementName+" "+elementType+" in page: "+"\""+pageName+"\"", true);
			}
	}	

	/**
	 * Function to click on element
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 * @param elementName
	 *            The name of the element in which the expected text to be
	 *            entered
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which title is to be compared with expected title
	 */
	public void click(WebElement element, long timeOutInSeconds, String elementName, String elementType,
			String pageName, Boolean ReportLog) {
		try {

			if (driverUtil.waitUntilElementEnabled(element, timeOutInSeconds, elementName, elementType, pageName)) {
				element.click();
				report.updateTestLog("Click", elementName + " " + elementType + " in " + pageName + " is clicked",
						Status.DONE);

				if (ReportLog)

					ALMFunctions.UpdateReportLogAndALMForPassStatus(elementName,
							"User should be able to click " + "\"" + elementName + "\"" + " in the page " + "\""
									+ pageName + "\"",
							"\"" + elementName + "\"" + " is clicked in the page " + "\"" + pageName + "\"", true);

			}

		} catch (Exception e) {
			ALMFunctions.UpdateReportLogAndALMForFailStatus(elementName,
					"User should be able to click on " + elementName, "Error - Unable to click " + elementName + " "
							+ elementType + " in page: " + "\"" + pageName + "\"",
					true);
		}
	}

	/**
	 * Function to click on element
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 * @param elementName
	 *            The name of the element in which the click operation is to be
	 *            done
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which the element exists
	 */
	public void clickWithoutVisibileCheck(By locator, long timeOutInSeconds, String elementName, String elementType,
			String pageName, Boolean ReportLog) {
		if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, elementType, pageName, true)) {
			// if(driverUtil.waitUntilElementEnabled(locator, timeOutInSeconds,
			// elementName, elementType, pageName)){
			try {
				driver.findElement(locator).click();
				if (ReportLog) {
					report.updateTestLog("Click", elementType + " - " + elementName + " in " + pageName + " is clicked",
							Status.DONE);
				}
			} catch (Exception e) {
				ALMFunctions.ThrowException(elementName,
						"User should be able to click " + "\"" + elementName + "\"" + " in the page " + "\"" + pageName
								+ "\"",
						"Below exception is thrown while trying to click " + "\"" + elementName + "\"" + "<br><br>"
								+ e.getLocalizedMessage(),
						true);
			}
			// }
		}
	}

	public void selectDropdownByvalue(By locator, String strLabel, String strValue, String pageName, Boolean ReportLog)
			throws Exception {
		try {
			if (objectExists(locator, "isDisplayed", lngPagetimeOutInSeconds, strLabel, strValue, pageName, false)) {

				Select printer = new Select(driver.findElement(locator));
				printer.selectByVisibleText(strValue);
				if (ReportLog) {
					report.updateTestLog("Click", strLabel + " - " + strLabel + " in " + pageName + " is clicked",
							Status.DONE);
				}
			}

		} catch (Exception e) {
			ALMFunctions.ThrowException(strValue, "Specified value should be present in " + strLabel,
					"Error - Unable to search the " + strValue + " provided by the user in the " + strLabel, false);

		}

	}

	/**
	 * Function to click on element
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 * @param elementName
	 *            The name of the element in which the click operation is to be
	 *            done
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which the element exists
	 */
	public void clickByJsExec(By locator, long timeOutInSeconds, String elementName, String elementType,
			String pageName, Boolean ReportLog) {
		if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, elementType, pageName, true)) {
			if (driverUtil.waitUntilElementVisible(locator, timeOutInSeconds, elementName, elementType, pageName,
					true)) {
				if (driverUtil.waitUntilElementEnabled(locator, timeOutInSeconds, elementName, elementType, pageName)) {
					try {
						JavascriptExecutor jsExec = (JavascriptExecutor) driver.getWebDriver();
						jsExec.executeScript("arguments[0].click();", driver.findElement(locator));
						if (ReportLog) {
							report.updateTestLog("Click",
									elementType + " - " + elementName + " in " + pageName + " is clicked", Status.DONE);
						}
					} catch (Exception e) {
						ALMFunctions.ThrowException(elementName,
								"User should be able to click " + "\"" + elementName + "\"" + " in the page " + "\""
										+ pageName + "\"",
								"Below exception is thrown while trying to click " + "\"" + elementName + "\""
										+ "<br><br>" + e.getLocalizedMessage(),
								true);
					}
				}
			}
		}
	}

	public void clickByJS(By locator, long timeOutInSeconds, String elementName, String elementType, String pageName,
			Boolean ReportLog) {
		if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, elementType, pageName, true)) {
			try {
				JavascriptExecutor jsExec = (JavascriptExecutor) driver.getWebDriver();
				jsExec.executeScript("arguments[0].click();", driver.findElement(locator));
				if (ReportLog) {
					report.updateTestLog("Click", elementType + " - " + elementName + " in " + pageName + " is clicked",
							Status.DONE);
				}
			} catch (Exception e) {
				ALMFunctions.ThrowException(elementName,
						"User should be able to click " + "\"" + elementName + "\"" + " in the page " + "\"" + pageName
								+ "\"",
						"Below exception is thrown while trying to click " + "\"" + elementName + "\"" + "<br><br>"
								+ e.getLocalizedMessage(),
						true);
			}
		}
	}

	public void clickByJS(WebElement element, long timeOutInSeconds, String elementName, String elementType,
			String pageName, Boolean ReportLog) {

		if (driverUtil.waitUntilElementEnabled(element, timeOutInSeconds, elementName, elementType, pageName)) {
			try {
				JavascriptExecutor jsExec = (JavascriptExecutor) driver.getWebDriver();
				jsExec.executeScript("arguments[0].click();", element);
				if (ReportLog) {
					report.updateTestLog("Click", elementType + " - " + elementName + " in " + pageName + " is clicked",
							Status.DONE);
				}
			} catch (Exception e) {
				ALMFunctions.ThrowException(elementName,
						"User should be able to click " + "\"" + elementName + "\"" + " in the page " + "\"" + pageName
								+ "\"",
						"Below exception is thrown while trying to click " + "\"" + elementName + "\"" + "<br><br>"
								+ e.getLocalizedMessage(),
						true);
			}
		}

	}

	public void doubleClick(By locator, long timeOutInSeconds, String elementName, String elementType, String pageName,
			Boolean ReportLog) {
		if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, elementType, pageName, true)) {

			try {
				driver.findElement(locator).sendKeys(Keys.ENTER);
				if (ReportLog) {
					report.updateTestLog("Click", elementType + " - " + elementName + " in " + pageName + " is clicked",
							Status.DONE);
				}
			} catch (Exception e) {
				ALMFunctions.ThrowException(elementName,
						"User should be able to click " + "\"" + elementName + "\"" + " in the page " + "\"" + pageName
								+ "\"",
						"Below exception is thrown while trying to click " + "\"" + elementName + "\"" + "<br><br>"
								+ e.getLocalizedMessage(),
						true);

			}
		}

	}

	
	/**
	 * Function to clear data in text box
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 * @param elementName
	 *            The name of the element in which the expected text to be
	 *            entered
	 * @param pageName
	 *            Page in which title is to be compared with expected title
	 */
	public void clear(By locator, long timeOutInSeconds, String elementName, String pageName) {
		try {
			if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, "input field", pageName,
					true)) {
				if (driverUtil.waitUntilElementVisible(locator, timeOutInSeconds, elementName, "input field", pageName,
						true)) {
					if (driverUtil.waitUntilElementEnabled(locator, timeOutInSeconds, elementName, "input field",
							pageName)) {
						driver.findElement(locator).clear();
					}
				}
			}
		} catch (Exception e) {
			ALMFunctions.ThrowException(elementName, "User should be able to clear text on " + elementName,
					"Error - Unable to clear text in " + elementName + " input field in page: " + "\"" + pageName
							+ "\"",
					true);
		}
	}

	/**
	 * Function to select the specified value from a list box
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the list box
	 * @param item
	 *            The value to be selected within the list box by visible text
	 * @param elementName
	 *            The name of the list box
	 * @param pageName
	 *            Page Name in which the list box is available
	 */
	public boolean selectListItem(By locator, long timeOutInSeconds, String[] items, String elementName,
			String pageName, String TypeofItem) {

		ArrayList<String> AppsAvaiableinApplication = new ArrayList<String>();
		ArrayList<String> AppsSelectedinApplication = new ArrayList<String>();

		if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, "list box", pageName, true)) {
			if (driverUtil.waitUntilElementVisible(locator, timeOutInSeconds, elementName, "list box", pageName,
					true)) {
				if (driverUtil.waitUntilElementEnabled(locator, timeOutInSeconds, elementName, "list box", pageName)) {
					Select dropDownList = new Select(driver.findElement(locator));

					AppsAvaiableinApplication = verifyItemInApplication(dropDownList, items, elementName, TypeofItem,true);

					if (dropDownList.isMultiple()) {
						dropDownList.deselectAll();
					}

					for (int i = 0; i < AppsAvaiableinApplication.size(); i++) {
						try {
							dropDownList.selectByVisibleText(AppsAvaiableinApplication.get(i));
							AppsSelectedinApplication.add(AppsAvaiableinApplication.get(i));
							report.updateTestLog(
									elementName, TypeofItem + " selected by the user in " + elementName
											+ " list box is " + "\"" + AppsAvaiableinApplication.get(i) + "\"",
									Status.DONE);
						} catch (ElementNotSelectableException e) {
							ALMFunctions.ThrowException(elementName,
									"User should be able to select value " + AppsAvaiableinApplication.get(i) + " on "
											+ elementName,
									"Error - " + "\"" + AppsAvaiableinApplication.get(i) + "\""
											+ " is not selectable in the list box " + elementName + " in the page: "
											+ "\"" + pageName + "\"",
									true);
						} catch (Exception e) {
							ALMFunctions.ThrowException(elementName,
									"User should be able to select value " + AppsAvaiableinApplication.get(i) + " on "
											+ elementName,
									"Error - Unable to select " + "\"" + AppsAvaiableinApplication.get(i) + "\""
											+ " in the list box " + elementName + " in the page: " + "\"" + pageName
											+ "\"",
									true);
						}

					}
				}
			}
		}

		if (AppsSelectedinApplication.size() == items.length) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<String> verifyItemInApplication(Select element, String[] Items, String ElementName,
			String TypeofItem, boolean blnLog) {
		ArrayList<String> ItemsAvailableinApplication = new ArrayList<String>();
		ArrayList<String> ItemsNotAvailableinApplication = new ArrayList<String>();
		try {
			List<WebElement> options = element.getOptions();
			boolean flag = false;
			for (int i = 0; i < Items.length; i++) {
				for (int j = 0; j < options.size(); j++) {
					driverUtil.waitFor(lngMinTimeOutInSeconds, ElementName, ElementName, TypeofItem);
					
					if (options.get(j).getText().trim().equalsIgnoreCase(Items[i])) {

						flag = true;
						ItemsAvailableinApplication.add(Items[i]);
						break;
					}

				}

				if (!flag)
					ItemsNotAvailableinApplication.add(Items[i]);

				flag = false;

			}

		} catch (Exception e) {
			ALMFunctions.ThrowException(ElementName, "Specified value should be present in " + ElementName,
					"Error - Unable to search the " + TypeofItem + " provided by the user in the " + ElementName, true);
		}

		if (blnLog) {
			if (ItemsNotAvailableinApplication.size() != 0) {
				ALMFunctions.ThrowException(ElementName, "Specified value should be present in " + ElementName,
						TypeofItem + ":" + ItemsNotAvailableinApplication + " provided in the"
								+ " datasheet is not available in the field " + ElementName,
						true);
			}
		}
		return ItemsAvailableinApplication;
	}

	/**
	 * Function to do a mouseover on top of the specified element
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param elementName
	 *            The name of the element on which mouseover to be done
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which Element is located
	 */
	public void mouseOver(By by, long timeOutInSeconds, String elementName, String elementType, String pageName) {

		try {
			Actions actions = new Actions(driver.getWebDriver());
			actions.moveToElement(driver.findElement(by)).build().perform();
		} catch (Exception e) {
			ALMFunctions.ThrowException(elementName, "User should be able to perform mouse Over on " + elementName,
					"Error - Unable to perform MouseOver on the element " + elementName + " " + elementType
							+ " in the page: " + "\"" + pageName + "\"",
					true);
		}
	}

	/**
	 * Function to do a mouseover on top of the specified element
	 * 
	 * @param element
	 *            The {@link WebDriver} element
	 * @param elementName
	 *            The name of the element on which mouseover to be done
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which Element is located
	 */
	public void mouseOver(WebElement element, long timeOutInSeconds, String elementName, String elementType,
			String pageName) {

		try {
			Actions actions = new Actions(driver.getWebDriver());
			actions.moveToElement(element).build().perform();
		} catch (Exception e) {
			ALMFunctions.ThrowException(elementName, "User should be able to perform mouse Over on " + elementName,
					"Error - Unable to perform MouseOver on the element " + elementName + " " + elementType
							+ " in the page: " + "\"" + pageName + "\"",
					true);
		}
	}

	public void iSelectValueOptionDropDownfromlist(By locator, String Text, String element, String ElementName,
			String pageName) throws Exception {

		String value = null;
		try {

			Select printer = new Select(driver.findElement(locator));
			List<WebElement> li = printer.getOptions();
			int size = li.size();
			System.out.println(li);

			for (int i = 0; i < size; i++) {

				value = li.get(i).getText();
				System.out.println(value);
				if (value.equalsIgnoreCase(Text)) {

					printer.selectByValue(Text);
					break;
				}
			}

		} catch (Exception e) {
			ALMFunctions.ThrowException(ElementName, "Specified value should be present in " + ElementName,
					"Error - Unable to search the " + element + " provided by the user in the " + ElementName, true);
		}

	}

	/**
	 * Function to do a mouseover on top of the specified element and click on
	 * element
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param elementName
	 *            The name of the element on which mouseover to be done
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which Element is located
	 */
	public void mouseOverandClick(By by, long timeOutInSeconds, String elementName, String elementType, String pageName,
			boolean ReportLog) {

		try {
			if (driverUtil.waitUntilElementLocated(by, timeOutInSeconds, elementName, elementType, pageName, true)) {
				Actions actions = new Actions(driver.getWebDriver());
				actions.moveToElement(driver.findElement(by)).click(driver.findElement(by)).build().perform();
				if (ReportLog) {
					report.updateTestLog("Click", elementType + " - " + elementName + " in " + pageName + " is clicked",
							Status.DONE);
				}
			}
		} catch (Exception e) {
			ALMFunctions.ThrowException(elementName,
					"User should be able to perform mouse Over and click on " + elementName,
					"Error - Unable to perform MouseOver and click on the element " + elementName + " " + elementType
							+ " in the page: " + "\"" + pageName + "\"",
					true);
		}
	}

	public void mouseOverandEnter(By by, long timeOutInSeconds, String elementName, String strValue, String elementType,
			String pageName, boolean ReportLog) {

		try {
			if (driverUtil.waitUntilElementLocated(by, timeOutInSeconds, elementName, elementType, pageName, true)) {
				Actions actions = new Actions(driver.getWebDriver());
				actions.moveToElement(driver.findElement(by)).click(driver.findElement(by)).sendKeys(strValue).build()
						.perform();
				if (ReportLog) {
					report.updateTestLog(elementName, elementName + " entered by the user is: " + "\"" + strValue + "\""
							+ " in the page " + "\"" + pageName + "\"", Status.DONE);
				}
			}
		} catch (Exception e) {
			ALMFunctions.ThrowException(elementName,
					"User should be able to perform mouse Over and Enter value on " + elementName,
					"Error - Unable to perform MouseOver and Enter value on the element " + elementName + " "
							+ elementType + " in the page: " + "\"" + pageName + "\"",
					true);
		}
	}

	/**
	 * Function to do a mouseover on top of the specified element and click on
	 * element
	 * 
	 * @param element
	 *            WebElement in which action to be performed
	 * @param elementName
	 *            The name of the element on which mouseover to be done
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which Element is located
	 */
	public void mouseOverandClick(WebElement element, long timeOutInSeconds, String elementName, String elementType,
			String pageName) {

		try {
			if (driverUtil.waitUntilElementEnabled(element, timeOutInSeconds, elementName, elementType, pageName)) {
				Actions actions = new Actions(driver.getWebDriver());
				actions.moveToElement(element).click(element).build().perform();
			}
		} catch (Exception e) {
			ALMFunctions.ThrowException(elementName,
					"User should be able to perform mouse Over and click on " + elementName,
					"Error - Unable to perform MouseOver and click on the element " + elementName + " " + elementType
							+ " in the page: " + "\"" + pageName + "\"",
					true);
		}
	}

	public void draganddroppopup(By locator, long timeOutInSeconds, String elementName, String elementType,
			String pageName) {

		try {
			if (objectExists(locator, "isEnabled", timeOutInSeconds, elementName, elementType, pageName, false)) {

				WebElement draggable = driver.getWebDriver().findElement(locator);

				new Actions(driver.getWebDriver()).dragAndDropBy(draggable, 10, -200).build().perform();

			}

		} catch (Exception e) {
			ALMFunctions.ThrowException(elementName,
					"User should be able to perform mouse Over and click on " + elementName,
					"Error - Unable to perform MouseOver and click on the element " + elementName + " " + elementType
							+ " in the page: " + "\"" + pageName + "\"",
					true);
		}
	}

	public void menuClick(By loactor, By loactor1, long timeOutInSeconds, String elementName, String elementType,
			String pageName) {

		try {
			if (objectExists(loactor, "isEnabled", timeOutInSeconds, elementName, elementType, pageName, false)) {
				WebElement element = driver.findElement(loactor);
				WebElement element1 = driver.findElement(loactor1);
				Actions actions = new Actions(driver.getWebDriver());
				actions.moveToElement(element).moveToElement(element1).build().perform();
			}
		} catch (Exception e) {
			ALMFunctions.ThrowException(elementName,
					"User should be able to perform mouse Over and click on " + elementName,
					"Error - Unable to perform MouseOver and click on the element " + elementName + " " + elementType
							+ " in the page: " + "\"" + pageName + "\"",
					true);
		}
	}

	/**
	 * Function to do a mouse over on top of the specified element and click on
	 * element
	 * 
	 * @param element
	 *            WebElement in which action to be performed
	 * @param elementName
	 *            The name of the element on which mouseover to be done
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which Element is located
	 */
	public void mouseOverandDoubleClick(By by, long timeOutInSeconds, String elementName, String elementType,
			String pageName) {
		if (driverUtil.waitUntilElementEnabled(by, timeOutInSeconds, elementName, elementType, pageName)) {
			try {
				Actions actions = new Actions(driver.getWebDriver());
				actions.moveToElement(driver.findElement(by)).doubleClick(driver.findElement(by)).build().perform();
			} catch (Exception e) {
				ALMFunctions.ThrowException(elementName,
						"User should be able to perform mouse Over and click on " + elementName,
						"Error - Unable to perform MouseOver and double click on the element " + elementName + " "
								+ elementType + " in the page: " + "\"" + pageName + "\"",
						true);
			}
		}
	}

	/**
	 * Function to verify whether the specified object exists within the current
	 * page
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param condition
	 *            Element condition to be determined
	 * @param condition
	 *            Element condition to be determined
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 * @param elementName
	 *            The name of the element for which existence and condition to
	 *            be determined
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which title is to be compared with expected title
	 * @param Log
	 *            indicator true or false for report logging
	 * @return Boolean value indicating whether the specified object exists
	 */
	public Boolean objectExists(By by, String condition, long timeOutInSeconds, String elementName, String elementType,
			String pageName, Boolean LogIndicator) {
		Boolean StatusFlag = false;
		if (driverUtil.waitUntilElementLocated(by, timeOutInSeconds, elementName, elementType, pageName,LogIndicator)) {
			if (driverUtil.waitUntilElementVisible(by, timeOutInSeconds, elementName, elementType, pageName,LogIndicator)) {
				switch (condition) {
				case "isDisplayed":
					StatusFlag = driver.findElement(by).isDisplayed();
					break;
				case "isEnabled":
					if (driverUtil.waitUntilElementEnabled(by, timeOutInSeconds, elementName, elementType, pageName)) {
						StatusFlag = driver.findElement(by).isEnabled();
					}
					break;
				case "isSelected":
					StatusFlag = driver.findElement(by).isSelected();
					break;
				default:
					ALMFunctions.ThrowException("Input Data",
							"Only Pre-Defined Options are allowed in objExists function",
							"Change the operations as per switch case", true);
					break;
				}
			}
		}
		if (LogIndicator) {
			if (StatusFlag) {
				if (condition.equals("isDisplayed")) {
					ALMFunctions.UpdateReportLogAndALMForPassStatus(elementName,
							"\"" + elementName + "\"" + " " + elementType + " should be displayed in the page: " + "\""
									+ pageName + "\"",
							"\"" + elementName + "\"" + " " + elementType + " is displayed in the page: " + "\""
									+ pageName + "\"",
							true);
				} else if (condition.equals("isEnabled")) {
					ALMFunctions.UpdateReportLogAndALMForPassStatus(elementName,
							"\"" + elementName + "\"" + " " + elementType + " should be enabled in the page: " + "\""
									+ pageName + "\"",
							"\"" + elementName + "\"" + " " + elementType + " is enabled in the page: " + "\""
									+ pageName + "\"",
							true);
				} else if (condition.equals("isSelected")) {
					ALMFunctions.UpdateReportLogAndALMForPassStatus(elementName,
							"\"" + elementName + "\"" + " " + elementType + " should be selected in the page: " + "\""
									+ pageName + "\"",
							"\"" + elementName + "\"" + " " + elementType + " is selected in the page: " + "\""
									+ pageName + "\"",
							true);
				}
			} else {
				if (condition.equals("isDisplayed")) {
					ALMFunctions.ThrowException(elementName,
							"\"" + elementName + "\"" + " " + elementType + " should be displayed in the page: " + "\""
									+ pageName + "\"",
							"\"" + elementName + "\"" + " " + elementType + " is not displayed in the page: " + "\""
									+ pageName + "\"",
							true);
				} else if (condition.equals("isEnabled")) {
					ALMFunctions.ThrowException(elementName,
							"\"" + elementName + "\"" + " " + elementType + " should be enabled in the page: " + "\""
									+ pageName + "\"",
							"\"" + elementName + "\"" + " " + elementType + " is not enabled in the page: " + "\""
									+ pageName + "\"",
							true);
				} else if (condition.equals("isSelected")) {
					ALMFunctions.ThrowException(elementName,
							"\"" + elementName + "\"" + " " + elementType + " should be selected in the page: " + "\""
									+ pageName + "\"",
							"\"" + elementName + "\"" + " " + elementType + " is not selected in the page: " + "\""
									+ pageName + "\"",
							true);
				}
			}
		}
		return StatusFlag;
	}

	/**
	 * Function to verify whether the specified text is present within the
	 * current page
	 * 
	 * @param textPattern
	 *            The text to be verified
	 * @return Boolean value indicating whether the specified text is present
	 */
	public Boolean isTextPresent(String textPattern) {

		return driver.findElement(By.cssSelector("BODY")).getText().matches(textPattern);
	}

	/**
	 * Function to check if an alert is present on the current page
	 * 
	 * @param timeOutInSeconds
	 *            The number of seconds to wait while checking for the alert
	 * @return Boolean value indicating whether an alert is present
	 */
	public Boolean isAlertPresent(long timeOutInSeconds) {
		try {
			new WebDriverWait(driver.getWebDriver(), timeOutInSeconds).until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (TimeoutException ex) {
			return false;
		}
	}

	/**
	 * Function to accept alert
	 * 
	 * @param timeOutInSeconds
	 *            The number of seconds to wait while checking for the alert
	 * @return Boolean value indicating whether alert is accepted or not
	 */
	public Boolean acceptAlert(long timeOutInSeconds) {

		if (isAlertPresent(timeOutInSeconds)) {
			report.updateTestLog("Screen Shot", "Screen Shot Captured  " ,Status.SCREENSHOT);
			ALMFunctions.Screenshot();
			Alert alert = driver.switchTo().alert();
			driverUtil.waitFor(2000, "", "", "");
			alert.accept();
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Alert", "Alert should be accepted", "Alert is accepted successfully", true);
			return true;
		} else {
			ALMFunctions.ThrowException("Alert", "Alert message should be displayed", "Alert message is not displayed",true);
			return false;
		}
	}

	/**
	 * Function to deny alert
	 * 
	 * @param timeOutInSeconds
	 *            The number of seconds to wait while checking for the alert
	 * @return Boolean value indicating whether alert is dismissed
	 */
	public Boolean denyAlert(long timeOutInSeconds) {

		if (isAlertPresent(timeOutInSeconds)) {
			Alert alert = driver.switchTo().alert();
			
			alert.dismiss();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Function to deny alert
	 * 
	 * @param timeOutInSeconds
	 *            The number of seconds to wait while checking for the alert
	 * @return Boolean value indicating whether alert is dismissed
	 */
	public String getAlertText(long timeOutInSeconds) {

		if (isAlertPresent(timeOutInSeconds)) {
			Alert alert = driver.switchTo().alert();
			return alert.getText();
		} else {
			return "null";
		}
	}

	/**
	 * Function to scroll down
	 * 
	 * @param timeOutInSeconds
	 *            The number of seconds to wait while checking for the alert
	 * @param pageName
	 *            Name of the Page which has to be scrolled
	 */
	public void pagescroll(By locator, String pageName) {
		try {
			Thread.sleep(2000);
			//((JavascriptExecutor) driver.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(locator));
			((JavascriptExecutor) driver.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(locator));
		} catch (Exception e) {
			ALMFunctions.ThrowException("Page Scroll",
					"User should be able to perform scroll in the page " + pageName
							+ " to capture desired element in screenshot",
					"Unable to perform scroll on page: " + pageName, true);
		}

	}

	public void pagescroll(WebElement locator, String pageName) {
		try {

			((JavascriptExecutor) driver.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", locator);

		} catch (Exception e) {
			ALMFunctions.ThrowException("Page Scroll",
					"User should be able to perform scroll in the page " + pageName
							+ " to capture desired element in screenshot",
					"Unable to perform scroll on page: " + pageName, true);
		}
	}

	public boolean authenticateUsing(long timeOutInSeconds) {
		boolean flag = false;
		if (Password_Decrypter.decrypt(dataTable.getData("General_Data", "Encrypted Password")) != "null") {
			if (isAlertPresent(timeOutInSeconds)) {
				Alert alert = driver.switchTo().alert();
				alert.authenticateUsing(new UserAndPassword(dataTable.getData("General_Data", "User Name"),
						Password_Decrypter.decrypt(dataTable.getData("General_Data", "Encrypted Password"))));
				flag = true;
			}
		}
		return flag;
	}

	public boolean CheckMailNotification(String Subject, String Sender, String Body) {
		boolean flag = false;

		ALMFunctions.Screenshot();

		try {

			File f = new File(System.getProperty("user.dir") + "\\mail.txt");// Path
																				// where
																				// the
																				// text
			// file
			if (f.exists()) {
				f.delete();
			}

			String executable = null;

			String script = System.getProperty("user.dir") + "\\ReadOutlook.vbs";

			String architecture = "os.arch";
			String x = System.getProperty(architecture);
			String windowspath = System.getenv("Windir");
			if (x.contains("86")) {
				executable = windowspath + "\\System32\\wscript.exe";
			} else {
				executable = windowspath + "\\SysWOW64\\wscript.exe";
			}
			String line = null;

			String MsgSavePath = System.getProperty("user.dir") + "\\" + properties.getProperty("ScreenshotsFolderName")
					+ "\\" + testparameters.getCurrentTestcase() + "\\"
					+ properties.getProperty("StepLevelScreenshotsFolderName") + " " + (report.getstepno() + 1);

			System.out.println(MsgSavePath);

			String TxtFilePath = System.getProperty("user.dir");

			String[] cmdArr = { executable, script, Subject, Sender, MsgSavePath, TxtFilePath, Body };
			Process p = Runtime.getRuntime().exec(cmdArr);
			p.waitFor();

			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(TxtFilePath + "\\mail.txt"));
			line = br.readLine().toString();

			if (line.equals("Mail Received"))
				flag = true;
		} catch (Exception e) {
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Mail Notification",
					"User should be able to verify mail notification from outlook",
					"Error - in verifying mail from Outlook", true);
		}

		return flag;
	}

	public void switchToNewWindow(long lngPagetimeOutInSeconds, String strWindowHandle, String strWindowName) {
		if (driverUtil.waitUntilWindowCountAvailable(2, strWindowName, lngPagetimeOutInSeconds)) {
			Set<String> handles = driver.getWindowHandles();
			for (String windowHandle : handles) {
				if (!windowHandle.equals(strWindowHandle)) {
					driver.switchTo().window(windowHandle);
					driver.manage().window().maximize();
				}
			}
		}
	}

	public void sendkeys(By locator, long timeOutInSeconds, Keys keyStroke, String elementName, String pageName) {
		if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, "input field", pageName, true)) {
			if (driverUtil.waitUntilElementVisible(locator, timeOutInSeconds, elementName, "input field", pageName,
					true)) {
				if (driverUtil.waitUntilElementEnabled(locator, timeOutInSeconds, elementName, "input field",
						pageName)) {
					try {
						driver.findElement(locator).sendKeys(keyStroke);
					} catch (Exception e) {
						ALMFunctions.ThrowException(elementName,
								"User should be able to provide keystroke in " + "\"" + elementName + "\""
										+ " in the page " + "\"" + pageName + "\"",
								"Below exception is thrown while trying to provide keystroke in field " + "\""
										+ elementName + "\"" + "<br><br>" + e.getLocalizedMessage(),
								true);
					}
				}
			}
		}
	}

	/**
	 * Function to enter text using Javascript executor on element
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 * @param elementName
	 *            The name of the element in which the click operation is to be
	 *            done
	 * @param elementType
	 *            Type of the element
	 * @param pageName
	 *            Page in which the element exists
	 */
	public void sendKeysByJsExec(By locator, long timeOutInSeconds, String Text, String elementName, String pageName,
			Boolean ReportLog) {
		if (driverUtil.waitUntilElementLocated(locator, timeOutInSeconds, elementName, "input field", pageName, true)) {
			if (driverUtil.waitUntilElementVisible(locator, timeOutInSeconds, elementName, "input field", pageName,
					true)) {
				if (driverUtil.waitUntilElementEnabled(locator, timeOutInSeconds, elementName, "input field",
						pageName)) {
					try {
						JavascriptExecutor jsExec = (JavascriptExecutor) driver.getWebDriver();
						jsExec.executeScript("arguments[0].value='" + Text + "'", driver.findElement(locator));
						if (ReportLog) {
							report.updateTestLog(elementName, elementName + " entered by the user is: " + "\"" + Text
									+ "\"" + " in the page " + "\"" + pageName + "\"", Status.DONE);
						}
					} catch (Exception e) {
						ALMFunctions.ThrowException(elementName,
								"User should be able to enter text " + "\"" + Text + "\"" + " in " + "\"" + elementName
										+ "\"" + " in the page " + "\"" + pageName + "\"",
								"Below exception is thrown while trying to enter text in field " + "\"" + elementName
										+ "\"" + "<br><br>" + e.getLocalizedMessage(),
								true);
					}
				}
			}
		}
	}

	public void mouseOverandRightClick(WebElement element, long timeOutInSeconds, String elementName,
			String elementType, String pageName) {
		if (driverUtil.waitUntilElementEnabled(element, timeOutInSeconds, elementName, elementType, pageName)) {
			try {
				Actions actions = new Actions(driver.getWebDriver());
				actions.moveToElement(element).contextClick(element).build().perform();
			} catch (Exception e) {
				ALMFunctions.ThrowException(elementName,
						"User should be able to perform mouse Over and click on " + elementName,
						"Error - Unable to perform MouseOver and double click on the element " + elementName + " "
								+ elementType + " in the page: " + "\"" + pageName + "\"",
						true);
			}
		}
	}
	
	public void mouseOverandRightClick(By by, long timeOutInSeconds, String elementName, String elementType, String pageName,
			boolean ReportLog) {

		try {
			if (driverUtil.waitUntilElementEnabled(by, timeOutInSeconds, elementName, elementType, pageName)) {
				Actions actions = new Actions(driver.getWebDriver());
				actions.moveToElement(driver.findElement(by)).contextClick(driver.findElement(by)).build().perform();
				if (ReportLog) {
					report.updateTestLog("Click", elementType + " - " + elementName + " in " + pageName + " is clicked",
							Status.DONE);
				}
			}
		} catch (Exception e) {
			ALMFunctions.ThrowException(elementName,
					"User should be able to perform mouse Over and click on " + elementName,
					"Error - Unable to perform MouseOver and click on the element " + elementName + " " + elementType
							+ " in the page: " + "\"" + pageName + "\"",
					true);
		}
	}

	public void compareText(WebElement element, String valueToCompare) {

		if (element.getText().trim().matches(valueToCompare.trim())) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Value comparison",
					"Expected" + element.getText() + "should be equal to the actual " + valueToCompare,
					"Expected" + element.getText() + "is  equal to the actual " + valueToCompare, true);
		}

		else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Value comparison",
					"Expected : " + element.getText() + "should be equal to the actual:  " + valueToCompare,
					"Expected" + element.getText() + "is not equal to the actual " + valueToCompare, true);
		}

	}

	public int executeScript(boolean blnAutoIT, String strFilePath, String... arrArguments) {
		String[] cmdArr = new String[arrArguments.length + 1];
		cmdArr[0] = strFilePath;
		for (int i = 0; i < arrArguments.length; i++) {
			cmdArr[i + 1] = arrArguments[i];
		}
		try {
			Process p = Runtime.getRuntime().exec(cmdArr);
			p.waitFor();
			p.destroy();
			return p.exitValue();
		} catch (Exception e) {
			if (blnAutoIT) {
				ALMFunctions.ThrowException("AutoIT Script", "Should be able to execute AutoIT script",
						"Below exception is thrown while executing the AutoIT" + " script.<br><br>"
								+ e.getLocalizedMessage(),
						true);
			} else {
				ALMFunctions.ThrowException("VBS Script", "Should be able to execute VBS script",
						"Below exception is thrown while executing the VBS" + " script.<br><br>"
								+ e.getLocalizedMessage(),
						true);
			}

			return 1;// false
		}
	}

	public Sheet getSheetFromXLSWorkbook(HSSFWorkbook workbook, String strSheetName, String strFilePath) {
		boolean blnSheetFound = false;
		if (workbook.getNumberOfSheets() != 0) {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				if (workbook.getSheetName(i).equals(strSheetName)) {
					blnSheetFound = true;
					return workbook.getSheet(strSheetName);
				}
			}
			if (!blnSheetFound) {
				ALMFunctions.ThrowException("Verify Sheet",
						"Workbook should have the sheet " + "\"" + strSheetName + "\"",
						"Workbook does not contains a sheet with name " + "\"" + strSheetName + "\""
								+ " in the file found in the path " + "\"" + strFilePath + "\"",
						false);
			}
		} else {
			ALMFunctions.ThrowException("Verify Sheet", "Workbook should have the sheet " + "\"" + strSheetName + "\"",
					"Workbook is blank in the file found in the path " + "\"" + strFilePath + "\"", false);
		}
		return null;
	}

	public int getColumnIndex(String strFilePath, String strSheet, String strColumnName) {
		HSSFWorkbook wb = openExcelFile(strFilePath);
		Sheet sheet = getSheetFromXLSWorkbook(wb, strSheet, strFilePath);
		boolean blnColumnFound = false;
		Row row = sheet.getRow(0);
		if (row == null) {
			ALMFunctions.ThrowException("Header Row", "Header row should exists in the sheet " + "\"" + strSheet + "\"",
					"Header row does not exists in the sheet " + "\"" + strSheet + " of the excel file " + "\""
							+ strFilePath + "\"",
					false);
		} else {
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				String strActualString = getCellValueAsString(wb, cell);
				if (strActualString.equalsIgnoreCase(strColumnName)) {
					blnColumnFound = true;
					return i;
				}
			}
		}
		if (!blnColumnFound) {
			ALMFunctions.ThrowException("Column Header",
					"Column Header " + strColumnName + " should exists in the sheet " + "\"" + strSheet + "\"",
					"Column - " + "\"" + strColumnName + "\"" + " does not exists in the sheet " + "\"" + strSheet
							+ "\"" + " of the excel file " + "\"" + strFilePath + "\"",
					false);
		}
		return 0;
	}

	public HSSFWorkbook openExcelFile(String strFilePath) {
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(strFilePath));
		} catch (IOException e) {
			ALMFunctions.ThrowException("Excel File",
					"Excel File should be available in the path " + "\"" + strFilePath + "\"",
					"Excel File is not available in the path " + "\"" + strFilePath + "\"", false);
			return null;
		}
		return wb;
	}

	public String getCellValueAsString(HSSFWorkbook wb, Cell cell) {
		DataFormatter dataFormatter = new DataFormatter();
		String strValue;
		if (cell != null) {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				return "";
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				CalculateFormula(wb, cell);
				strValue = dataFormatter.formatCellValue(cell);
			} else {
				strValue = dataFormatter.formatCellValue(cell);
			}
		} else {
			return "";
		}
		return strValue;
	}

	public void CalculateFormula(Workbook wb, Cell cell) {
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		evaluator.evaluateInCell(cell);
	}

	/**
	 * Method to get column index from the table
	 * 
	 * @param strExpectedColumnName
	 *            - Name of the column for which index is to be found
	 * @param headers
	 *            - By locator of the header
	 * @param strTableName
	 *            - Name of the table in which the header exists
	 * @param blnLog
	 *            - Boolean to indicate whether log has to be captured in case
	 *            of failure
	 * @param blnThrowException
	 *            - Boolean to indicate whether exception has to be thrown
	 *            incase of failure
	 * @return - int - Index of the required header
	 */
	public int getColumnIndex(String strExpectedColumnName, By headers, String strTableName, boolean blnLog,
			boolean blnThrowException) {
		String strGetBrowser = testparameters.getBrowser().getValue().toLowerCase();
		int intColumnIndex = -1;
		
		List<WebElement> tableHeaders = driver.getWebDriver().findElements(headers);
		//System.out.println(tableHeaders.size());
		//List<WebElement> tableHeaders = driver.getWebDriver().findElements(headers);
		if (tableHeaders.size() == 0) {
			ALMFunctions.ThrowException(strTableName, strTableName + " headers should be displayed",
					strTableName + " headers are not displayed", true);
		}
		for (WebElement tableHeader : tableHeaders) {

			Actions action = new Actions(driver.getWebDriver());
			action.moveToElement(tableHeader).build().perform();

			if (tableHeader.getText().replace("Click here to sort", "").toLowerCase().trim()
					.equals(strExpectedColumnName.toLowerCase().trim())) {
				intColumnIndex = tableHeaders.indexOf(tableHeader);
				break;
			} 
			
			else if (tableHeader.getText().replace("Sort in ascending order", "").toLowerCase().trim()
					.equals(strExpectedColumnName.toLowerCase().trim())) {

				intColumnIndex = tableHeaders.indexOf(tableHeader);
				break;
			}
			else if (tableHeader.getText().replace("Sort in descending order", "").toLowerCase().trim()
					.equals(strExpectedColumnName.toLowerCase().trim())) {

				intColumnIndex = tableHeaders.indexOf(tableHeader);
				break;
			}
		}
		if (blnLog) {
			if (blnThrowException) {
				ALMFunctions.ThrowException("Column Name",
						strExpectedColumnName + " column name should be found in table - " + strTableName,
						strExpectedColumnName + " column name is not found in table - " + strTableName, true);
			} else {
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Column Name",
						strExpectedColumnName + " column name should be found in table - " + strTableName,
						strExpectedColumnName + " column name is not found in table - " + strTableName, true);
			}
		}
		return intColumnIndex;
	}
	

	/**
	 * Function to handle the file upload 
	 * @param flag for checking file is uploaded or not
	 * @param location of the file to be selected
	 * @param argument values for autoIT function.
	 *  * 
	 */
	public int executeScript(String strFilePath, String... arrArguments) {
		String[] cmdArr = new String[arrArguments.length + 1];
		cmdArr[0] = strFilePath;
		for (int i = 0; i < arrArguments.length; i++) {
			cmdArr[i + 1] = arrArguments[i];
		}
		try {
			Process p = Runtime.getRuntime().exec(cmdArr);
			p.waitFor();
			p.destroy();
			return p.exitValue();
		} catch (Exception e) {
			ALMFunctions.ThrowException("AutoIT Script", "Should be able to execute AutoIT script", "Below exception is thrown while executing the AutoIT"
                    + " script.<br><br>"+e.getLocalizedMessage(), true);
		}
		return 1;
	}



	
}
