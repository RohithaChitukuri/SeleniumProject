//Uses excel file to fetch json data file-path
//uses external dataProviderClass
//mysql database has table called credentials in which there are 2 rows with person,username,password columns.
//1.rohitha rohitha03@gmail.com
//2.invalid rohitha03@gmail.com 12345

package RC.Tests;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import RC.PageObjects.CartPage;
import RC.PageObjects.LoginPage;
import RC.PageObjects.ProductCataloguePage;
import RC.TestComponents.BaseTest;
import RC.TestData.DataProviderData;

public class CartUsingDatabaseTests extends BaseTest {

	Connection con;

	public void connectToDatabase(String host, String port, String username, String password) throws SQLException {

		// connecting to database
		// connection url pattern "jdbc:mysql://"+host+":"+port+"/databasename";
		con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/qadb", username, password);

	}

	public String getUsername(String person) throws SQLException {
		String username = null;
		String password = null;
		// creating statement object
		Statement st = con.createStatement();
		// executing statement
		ResultSet rs = st.executeQuery("select * from credentials where person='" + person + "'");
		// initially rs points to base index. We need to make it point to next() for it
		// to point to real data
		while (rs.next()) {
			username = rs.getString("username");
			password = rs.getString("password");
		}
		return username;

	}

	public String getPassword(String person) throws SQLException {
		String username = null;
		String password = null;
		// creating statement object
		Statement st = con.createStatement();
		// executing statement
		ResultSet rs = st.executeQuery("select * from credentials where person='" + person + "'");
		// initially rs points to base index. We need to make it point to next() for it
		// to point to real data
		while (rs.next()) {
			username = rs.getString("username");
			password = rs.getString("password");
		}
		return password;
	}

	@Test(testName = "testCase4", dataProvider = "data", dataProviderClass = DataProviderData.class)
	public void printProductsCost(HashMap<Object, Object> cartData) throws IOException, SQLException {
		connectToDatabase("localhost", "3306", "root", "Rohitha@26");
		String username = getUsername("rohitha");
		String password = getPassword("rohitha");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();

		loginPage.loginApplication(username, password);
		ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		productCatalogue.searchAndAddProductsToCart((List<String>) cartData.get("searchWords"));
		CartPage cartPage = new CartPage(driver);
		cartPage.clickOnCart();
		cartPage.getProductsCost().stream().forEach(s -> System.out.println(s));
		// printing testName

		// System.out.println(Reporter.getCurrentTestResult().getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).testName());
	}

	@Test(testName = "testCase5", dataProvider = "data", dataProviderClass = DataProviderData.class/*,retryAnalyzer = RC.TestComponents.Retry.class*/)
	public void checkTotal(HashMap<Object, Object> cartData) throws InterruptedException, IOException, SQLException {
		connectToDatabase("localhost", "3306", "root", "Rohitha@26");
		String username = getUsername("rohitha");
		String password = getPassword("rohitha");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();
		loginPage.loginApplication(username, password);
		
		
		  ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		  productCatalogue.searchAndAddProductsToCart((List<String>)cartData.get("searchWords"));
		  CartPage cartPage = new CartPage(driver);
		  cartPage.clickOnCart();
		  int totalInCart =Integer.parseInt(cartPage.getTotal());
		  int sum =cartPage.getProductsCost().stream().map(s -> Integer.parseInt(s)).reduce(0,(x, y) -> x + y);
		  
		  Assert.assertEquals(sum, totalInCart);
		 

	}

	@Test(testName = "testCase6", dataProvider = "data", dataProviderClass = DataProviderData.class)
	public void checkCartProductsSameAsFoundItems(HashMap<Object, Object> cartData) throws IOException, SQLException {
		connectToDatabase("localhost", "3306", "root", "Rohitha@26");
		String username = getUsername("rohitha");
		String password = getPassword("rohitha");

		List<String> foundWords = new ArrayList<String>();
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();
		loginPage.loginApplication(username, password);
		ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		foundWords = productCatalogue.searchAndAddProductsToCart((List<String>) cartData.get("searchWords"));
		CartPage cartPage = new CartPage(driver);
		cartPage.clickOnCart();
		Assert.assertTrue(cartPage.checkProductsInCartPage(foundWords));
	}
	
	@Test(testName = "testCase7",retryAnalyzer = RC.TestComponents.Retry.class)
	public void checkInvalidLogin() throws InterruptedException, IOException, SQLException {
		connectToDatabase("localhost", "3306", "root", "Rohitha@26");
		String username = getUsername("invalid");
		String password = getPassword("invalid");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.goTo();
		loginPage.loginApplication(username, password);
		
		WebElement incorrectLoginToastMsg=driver.findElement(By.cssSelector("div[aria-label='Incorrect email or password.']"));
		Assert.assertEquals("Incorrect email or password.", incorrectLoginToastMsg.getText());
	}
}
