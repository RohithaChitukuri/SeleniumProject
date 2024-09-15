package RC.Tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sun.net.httpserver.Authenticator.Retry;

import RC.PageObjects.CartPage;
import RC.PageObjects.CheckOutPage;
import RC.PageObjects.ConfirmationPage;
import RC.PageObjects.LoginPage;
import RC.PageObjects.ProductCataloguePage;
import RC.TestComponents.BaseTest;

public class SubmitOrderTests extends BaseTest {

	List<String> searchWords = new ArrayList<String>();
	
		@Test(dataProvider = "data",retryAnalyzer=RC.TestComponents.Retry.class)
		public void submitOrder(HashMap<Object,Object> data) throws InterruptedException, IOException {
			//WebDriver driver=initializeDriver();

			
			List<String> foundWords=new ArrayList<String>();
			LoginPage loginPage=new LoginPage(driver);
			loginPage.goTo();
			loginPage.loginApplication((String) data.get("userId"), (String) data.get("password"));
			
			ProductCataloguePage productCatalogue=new ProductCataloguePage(driver);
			foundWords=productCatalogue.searchAndAddProductsToCart((List<String>) data.get("searchWords"));
			CartPage cartPage=new CartPage(driver);
			cartPage.clickOnCart();
			
			//asset if searchWords and foundWords list have same items
			//Assert.assertFalse(foundWords.equals(searchWords));
			//assert if products in cart are same as found items
			//Assert.assertTrue(cartPage.checkProductsInCartPage(foundWords));
			cartPage.clickOnCheckOut();
			
			CheckOutPage checkOutPage=new CheckOutPage(driver);
			checkOutPage.enterCountry("india");
			checkOutPage.clickPlaceOrder();
			
			ConfirmationPage cp=new ConfirmationPage(driver);
			String msg=cp.getConfirmationMsg();
			
			Assert.assertTrue(msg.equalsIgnoreCase("Thankyou for the order."));

		}
		
		@Test
		public void checkIfTheProductsAreAvailable() throws IOException {
			List<String> foundWords=new ArrayList<String>();
			LoginPage loginPage=new LoginPage(driver);
			loginPage.goTo();
			loginPage.loginApplication("rohitha03@gmail.com", "Rohitha@26");
			
			ProductCataloguePage productCatalogue=new ProductCataloguePage(driver);
			foundWords=productCatalogue.searchAndAddProductsToCart(searchWords);
			//asset if searchWords and foundWords list have same items
			Assert.assertTrue(foundWords.equals(searchWords));
			
			
		}
		
		
		@DataProvider(name="data")
		public Object[][] getData(){
			HashMap<Object,Object> data1=new HashMap<Object, Object>();
			data1.put("userId", "rohitha03@gmail.com");
			data1.put("password", "Rohitha@26");
			data1.put("searchWords", Arrays.asList("ZARA COAT 3", "IPHONE 13 PRO"));
			
			HashMap<Object,Object> data2=new HashMap<Object, Object>();
			data2.put("userId", "rohitha03@gmail.com");
			data2.put("password", "Rohitha@26");
			data2.put("searchWords", Arrays.asList("ZARA COAT 3"));
			
			return new Object[][] {{data1},{data2}};
			
		}
}


		


