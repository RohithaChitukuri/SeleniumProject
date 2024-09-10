package RC.Tests;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ProductSelection {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get("https://rahulshettyacademy.com/client");
		//driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		JavascriptExecutor js= (JavascriptExecutor)driver;

		List<String> searchWords = Arrays.asList("ZARA COAT 3", "IPHONE 14 PRO");
		List<String> foundWords=new ArrayList<String>();
		driver.findElement(By.id("userEmail")).sendKeys("rohitha03@gmail.com");
		driver.findElement(By.id("userPassword")).sendKeys("Rohitha@26");
		driver.findElement(By.id("login")).click();
		// explicitly wait until product items are displayed
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".card-body"))));

		List<WebElement> product = driver.findElements(By.cssSelector(".card-body"));

		// for each item in the searchWords list, create stream of webElements, search
		// and add items to cart

		searchWords.stream().forEach(i -> {
			// if there is any match of products,click the first match
			if(product.stream().anyMatch(p->p.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(i)))
			{ 
				//adding product name of found items to foundWords list
				foundWords.add(product.stream().filter(p -> p.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(i))
						.findFirst().orElse(null).findElement(By.cssSelector("b")).getText());
				//clicking 'add to cart' of the found item 
				product.stream().filter(p -> p.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(i))
						.findFirst().orElse(null).findElement(By.cssSelector("button:last-of-type")).click();
			}
			
			// explicitly wait until "product added to cart toast msg is popped and
			// invisible"
			//wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("toast-container"))));
			wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("toast-container"))));
		});

		// click on cart button
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("[routerlink*=\"cart\"]"))));
		driver.findElement(By.cssSelector("[routerlink*=\"cart\"]")).click();
		
		//asset if searchWords and foundWords list have same items
		Assert.assertFalse(foundWords.equals(searchWords));
		
		//click checkout button
		
		
		
		Thread.sleep(5000);
		js.executeScript("window.scrollBy(0,1000)");
	
		
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[contains(text(),'Checkout')]"))));
		driver.findElement(By.xpath("//button[contains(text(),'Checkout')]")).click();
		driver.findElement(By.xpath("//input[@placeholder=\"Select Country\"]")).sendKeys("India");
		
		
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("(//button[@class=\"ta-item list-group-item ng-star-inserted\"])[2]"))));
		driver.findElement(By.xpath("(//button[@class=\"ta-item list-group-item ng-star-inserted\"])[2]")).click();
		js.executeScript("window.scrollBy(0,250)", "");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[contains(text(),\"Place Order \")]"))));
		driver.findElement(By.xpath("//a[contains(text(),\"Place Order \")]")).click();
		
		String thankYou=driver.findElement(By.cssSelector(".hero-primary")).getText();
		Assert.assertTrue(thankYou.equalsIgnoreCase("Thankyou for the order."));
		

	}

}
