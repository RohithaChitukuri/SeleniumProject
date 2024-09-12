package RC.PageObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import RC.AbstractComponents.AbstractComponent;

public class CartPage extends AbstractComponent {

	WebDriver driver;

	public CartPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//button[contains(text(),\"Checkout\")]")
	WebElement checkOutButton;

	@FindBy(xpath = "//div[@class=\"cart\"]/ul/li/div//div/h3")
	List<WebElement> cartItems;

	@FindBy(css=".cartWrap.ng-star-inserted")
	List<WebElement> cartItemElements;
	
	@FindBy(xpath="//span[normalize-space()='Subtotal']/following-sibling::span[@class=\"value\"]")
	WebElement subTotalValueElement;
	
	@FindBy(xpath="//span[normalize-space()='Total']/following-sibling::span[@class=\"value\"]")
	WebElement totalValueElement;
	
	@FindBy(xpath="//div[@class=\"ngx-spinner-overlay\"]")
	WebElement toasterMessage;
	
	
	List<String> cartItemNames=new ArrayList<String>();	
	//List<String> cartItemNames=cartItems.stream().map(p->p.getText()).collect(Collectors.toList());
	
	

	public List<String> getProductsInCartPage() {

		cartItemNames=cartItems.stream().map(p->p.getText()).collect(Collectors.toList());
		return cartItemNames;	
	}

	public boolean checkProductsInCartPage(List<String> foundItems) {
		return getProductsInCartPage().equals(foundItems);
	}

	public void clickOnCheckOut() throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Thread.sleep(5000);
		js.executeScript("window.scrollBy(0,2000)");
		Thread.sleep(2000);
		waitForElementToAppear(checkOutButton);
		checkOutButton.click();
	}

	public List<String> getProductsCost() {
		return cartItemElements.stream().map(p->p.findElement(By.cssSelector(".prodTotal.cartSection p")).getText().split(" ")[1]).collect(Collectors.toList());

	}
	
	public String getSubTotal() {
		return subTotalValueElement.getText().split("\\$")[1];
	}
	
	public String getTotal() {
		//System.out.println(totalValueElement.getText().split("\\$")[1]);
		return totalValueElement.getText().split("\\$")[1];
	}
	
//	public String addAndGetTotal() {
//		
//	}

}
