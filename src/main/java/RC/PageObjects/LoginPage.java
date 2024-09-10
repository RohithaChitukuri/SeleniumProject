package RC.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import RC.AbstractComponents.AbstractComponent;

public class LoginPage extends AbstractComponent{
	
	WebDriver driver;
	
	public LoginPage(WebDriver driver) {
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(id="userEmail")
	WebElement usernameBox;
	
	@FindBy(id="userPassword")
	WebElement passwordBox;
	
	@FindBy(id="login")
	WebElement loginButton;
	
	public void loginApplication(String username, String password) {
		usernameBox.sendKeys(username);
		passwordBox.sendKeys(password);
		loginButton.click();
		
	}
	public void goTo() {
		driver.get("https://rahulshettyacademy.com/client");
	}

}
