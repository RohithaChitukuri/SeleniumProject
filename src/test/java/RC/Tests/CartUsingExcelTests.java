//Uses excel file to fetch json data file-path
//uses external dataProviderClass

package RC.Tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import RC.PageObjects.CartPage;
import RC.PageObjects.LoginPage;
import RC.PageObjects.ProductCataloguePage;
import RC.TestComponents.BaseTest;
import RC.TestComponents.GetExcelData;
import RC.TestData.DataProviderData;

public class CartUsingExcelTests extends BaseTest {

	@Test(testName = "testCase1", dataProvider = "data",dataProviderClass =DataProviderData.class)
	public void printProductsCost(HashMap<Object, Object> cartData) throws IOException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();
		loginPage.loginApplication((String) cartData.get("userId"), (String) cartData.get("password"));
		ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		productCatalogue.searchAndAddProductsToCart((List<String>) cartData.get("searchWords"));
		CartPage cartPage = new CartPage(driver);
		cartPage.clickOnCart();
		cartPage.getProductsCost().stream().forEach(s -> System.out.println(s));
		// printing testName

		// System.out.println(Reporter.getCurrentTestResult().getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).testName());
	}

	@Test(testName="testCase2",dataProvider = "data",dataProviderClass =DataProviderData.class, retryAnalyzer = RC.TestComponents.Retry.class)
	public void checkTotal(HashMap<Object, Object> cartData) throws InterruptedException, IOException {

		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();
		loginPage.loginApplication((String) cartData.get("userId"), (String) cartData.get("password"));
		ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		productCatalogue.searchAndAddProductsToCart((List<String>) cartData.get("searchWords"));
		CartPage cartPage = new CartPage(driver);
		cartPage.clickOnCart();
		int totalInCart = Integer.parseInt(cartPage.getTotal());
		int sum = cartPage.getProductsCost().stream().map(s -> Integer.parseInt(s)).reduce(0, (x, y) -> x + y);

		Assert.assertEquals(sum, totalInCart);

	}

	@Test(testName="testCase3",dataProvider = "data",dataProviderClass =DataProviderData.class)
	public void checkCartProductsSameAsFoundItems(HashMap<Object, Object> cartData) throws IOException {
		List<String> foundWords = new ArrayList<String>();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();
		loginPage.loginApplication((String) cartData.get("userId"), (String) cartData.get("password"));
		ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		foundWords = productCatalogue.searchAndAddProductsToCart((List<String>) cartData.get("searchWords"));
		CartPage cartPage = new CartPage(driver);
		cartPage.clickOnCart();
		Assert.assertTrue(cartPage.checkProductsInCartPage(foundWords));
	}

	
}