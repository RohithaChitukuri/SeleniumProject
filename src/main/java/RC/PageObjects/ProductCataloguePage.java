package RC.PageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import RC.AbstractComponents.AbstractComponent;

public class ProductCataloguePage extends AbstractComponent {
	WebDriver driver;
	public ProductCataloguePage(WebDriver driver) {
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
		
	}
	@FindBy(css = ".card-body")
	List<WebElement> product;
	
	public List<WebElement> getProductList() {
		waitForElementToAppear(product);
		return product;
	}
	By toastContainerLocator=By.id("toast-container");
	
	//returns list of found items names
	public List<String> searchAndAddProductsToCart(List<String> searchWords) {
		List<String> foundWords=new ArrayList<String>();
		
		// for each item in the searchWords list, create stream of webElements, search
		// and add items to cart
		searchWords.stream().forEach(i -> {
			// if there is any match of products,click the first match
			if(getProductList().stream().anyMatch(p->p.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(i)))
			{ 
				//adding product name of found items to foundWords list
				
				  foundWords.add(product.stream().filter(p ->
				   p.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(i))
				  .findFirst().orElse(null).findElement(By.cssSelector("b")).getText());
				 
				
			
				//clicking 'add to cart' of the found item 
				/*
				 * product.stream().filter(p ->
				 * (p.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(i)))
				 * .findFirst().orElse(null).findElement(By.cssSelector("button:last-of-type")).
				 * click();
				 */
				
				product.stream().filter(p -> 
				(p.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(i)))
				.forEach(p->
						{	
							p.findElement(By.cssSelector("button:last-of-type")).click();
							waitForElementToDisappear(By.cssSelector(".ngx-spinner-overlay.ng-tns-c31-1.ng-trigger.ng-trigger-fadeIn.ng-star-inserted"));});
			}
			waitForElementToDisappear(toastContainerLocator);
	
		});
		return foundWords;
				
	}		
		
	}
	


	

