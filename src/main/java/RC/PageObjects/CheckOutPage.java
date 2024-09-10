package RC.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import RC.AbstractComponents.AbstractComponent;

public class CheckOutPage extends AbstractComponent {
	WebDriver driver;
	
	public CheckOutPage(WebDriver driver) {
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//input[@placeholder=\"Select Country\"]")
	WebElement countryBox;
	
	@FindBy(xpath="(//button[@class=\"ta-item list-group-item ng-star-inserted\"])[2]")
	WebElement countrySuggestion;
	
	@FindBy(xpath="//a[contains(text(),\"Place Order \")]")
	WebElement placeOrderButton;
	
	public void enterCountry(String country) {
		countryBox.sendKeys(country);
		waitForElementToAppear(countrySuggestion);
		countrySuggestion.click();
	}
	
	public void clickPlaceOrder() {
		JavascriptExecutor js= (JavascriptExecutor)driver;
		js.executeScript("window.scrollBy(0,1000)");
		waitForElementToAppear(placeOrderButton);
		placeOrderButton.click();
	}
	
}	
