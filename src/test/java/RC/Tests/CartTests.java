package RC.Tests;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import RC.PageObjects.CartPage;
import RC.PageObjects.LoginPage;
import RC.PageObjects.ProductCataloguePage;
import RC.TestComponents.BaseTest;

public class CartTests extends BaseTest {
	

	@Test(dataProvider = "data")
	public void printProductsCost(HashMap<Object,Object> cartData) throws IOException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();
		loginPage.loginApplication((String)cartData.get("userId"), (String)cartData.get("password"));
		ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		productCatalogue.searchAndAddProductsToCart((List<String>)cartData.get("searchWords"));
		CartPage cartPage = new CartPage(driver);
		cartPage.clickOnCart();
		cartPage.getProductsCost().stream().forEach(s -> System.out.println(s));
	}

	@Test(dataProvider = "data",retryAnalyzer=RC.TestComponents.Retry.class)
	public void checkTotal(HashMap<Object,Object> cartData) throws InterruptedException, IOException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();
		loginPage.loginApplication((String)cartData.get("userId"), (String)cartData.get("password"));
		ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		productCatalogue.searchAndAddProductsToCart((List<String>)cartData.get("searchWords"));
		CartPage cartPage = new CartPage(driver);
		cartPage.clickOnCart();
		int totalInCart = Integer.parseInt(cartPage.getTotal());
		int sum = cartPage.getProductsCost().stream().map(s -> Integer.parseInt(s)).reduce(0, (x, y) -> x + y);
		
		Assert.assertEquals(sum, totalInCart);
	
	}

	@Test(dataProvider = "data")
	public void checkCartProductsSameAsFoundItems(HashMap<Object,Object> cartData) throws IOException {
		List<String> foundWords = new ArrayList<String>();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();
		loginPage.loginApplication((String)cartData.get("userId"), (String)cartData.get("password"));
		ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		foundWords = productCatalogue.searchAndAddProductsToCart((List<String>)cartData.get("searchWords"));
		CartPage cartPage = new CartPage(driver);
		cartPage.clickOnCart();
		Assert.assertTrue(cartPage.checkProductsInCartPage(foundWords));
	}
	
	@DataProvider(name="data")
	public Object[][] getData() throws IOException{
		//getJsonDataToMap(uses jackson binder to convert string to list of HashMaps) which is written in BaseTest returns list of hashMaps. 
		String filePath=System.getProperty("user.dir")+"\\src\\test\\java\\RC\\TestData\\cartTestData.json";
		List<HashMap<Object,Object>> data=getJsonDataToMap(filePath);
		//data.get(index) is a  hashMap 
		return new Object[][] {{data.get(0)},{data.get(1)}};	
	}
}
