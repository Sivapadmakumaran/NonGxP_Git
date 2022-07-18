package businesscomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import io.github.sukgu.Shadow;
import supportlibraries.ScriptHelper;

public class Chatbot extends CommonActionsAndFunctions {

	public Chatbot(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}

	public void eleClick1() {

		driver.switchTo().frame(driver.findElement(By.xpath("//*[contains(@class,'frame')]")));
		expandShadow("[id*='all-topic-picker'][role=button]","SHow Me Everything");
		expandShadow("[aria-label*='Benefits'][role=button]","Benefits");
		expandShadow("a[textContent='Link to Benefits')]","Link to Benefits");
		
	}
	
	public void eleClick()
	{
		driver.switchTo().frame(driver.findElement(By.xpath("//*[contains(@class,'frame')]")));
			WebElement root1 = driver.findElement(By.tagName("sn-component-va-web-client"));
	        WebElement shadow_root1 = expandRootElement(root1);
	        
	        WebElement root2 = shadow_root1.findElement(By.tagName("sn-component-chat-window"));
	        WebElement shadow_root2 = expandRootElement(root2);
	        
	        WebElement root3 = shadow_root2.findElement(By.cssSelector("[id*='all-topic-picker'][role=button]"));
	        WebElement shadow_root3 = expandRootElement(root3);
	        
		if(shadow_root3.isEnabled()) {
			
			System.out.println("true");
		}else {
			
			System.out.println("false");
		}
		
	}

	public WebElement expandShadow(String ele,String strEleName)

	{
		WebElement shadowRoot = null;
		try {
			driver.switchTo().frame(driver.findElement(By.xpath("//*[contains(@class,'frame')]")));
			Shadow shade = new Shadow(driver.getWebDriver());
			WebElement root1 = shade.findElement("[rel*='noreferrer']");
			//div[class*='picker-message-list'][role='list']
			if (root1.isDisplayed()) {
				root1.click();
				System.out.println(strEleName+" is clicked in the Chatbox");
			} else {
				System.out.println("False");

			}

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		return shadowRoot;

	}

	// Returns webelement
	public WebElement expandRootElement(WebElement element)

	{

		/*
		 * RemoteWebElement element = ( RemoteWebElement ) context; JavascriptExecutor
		 * driver = ( JavascriptExecutor ) element.getWrappedDriver(); return (
		 * WebElement ) driver.executeScript( "return arguments[0].shadowRoot" ,
		 * element);
		 */
		WebElement ele = (WebElement) ((JavascriptExecutor) driver.getWebDriver())
				.executeScript("return arguments[0].shadowRoot", element);

		// JavascriptExecutor jsExec = (JavascriptExecutor) driver.getWebDriver();
		return ele;
	}
	
	

}
