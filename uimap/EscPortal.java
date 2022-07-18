package uimap;

import org.openqa.selenium.By;

public class EscPortal {

	public By escTextarea;
	public By escCombobox;
	public By escbutton;

	public EscPortal(String strLabel) 
	{
		escCombobox = By.xpath("//*[text()='" + strLabel
				+ "']//ancestor-or-self::*[contains(@class,'field-label')]//following-sibling::*[contains(@class,'field')]//*[contains(@class,'arrow')]");

		escTextarea = By.xpath("//*[contains(translate(text(),'\u00A0', ' '),'"+strLabel+"')]//ancestor-or-self::*[contains(@class,'field-label') or contains(@class,'form-group')]//following-sibling::*[contains(@class,'field') or contains(@class,'row')]//textarea");
		escbutton=By.xpath("//button[text()='"+strLabel+"' and contains(@class,'btn')]");
	}
	
	//public static final By escListItem=By.xpath("//ul[@role='tabpanel' and contains(@class,'active') or contains(@class,'list-group')]//li[ @role='listitem' or @role='button']");
	public static final By escListItem=By.xpath("//ul[@role='tabpanel' and contains(@class,'active') or contains(@class,'list-group') or (contains(@class,'select') and @role='listbox')]//li[contains(@class,'select') or @role='listitem' or @role='button']");

	public static final By escCaseNumber=By.xpath("//*[contains(@class,'case-number')]");
	public static final By showMore=By.xpath("//*[contains(@class,'tab') and contains(@class,'active')]//button[text()='Show more']");
	public static final By loading=By.xpath("//*[contains(@class,'loading') and not(contains(@class,'hidden'))]");
	public static final By PageBreadCrumbs=By.xpath("//*[@aria-label='Page breadcrumbs']//li//a");
}
