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

public class CartUsingExcelTests extends BaseTest {

	@Test(testName = "testCase1", dataProvider = "data")
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

	@Test(testName="testCase2",dataProvider = "data", retryAnalyzer = RC.TestComponents.Retry.class)
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

	@Test(testName="testCase3",dataProvider = "data")
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

	@DataProvider(name = "data")
	public Object[][] getData(Method method) throws IOException {
		// getJsonDataToMap(uses jackson binder to convert string to list of HashMaps)
		// which is written in BaseTest returns list of hashMaps.

		// getting value of testName from @Test annotations
		String testCase = method.getAnnotation(Test.class).testName();
		
		//(Refer) GetExcelData class for getting filePath of JSON data file 
		GetExcelData excelData = new GetExcelData();
		FileInputStream fis = new FileInputStream(
				"C:\\Users\\SUNNY\\eclipse-workspace\\GitSample\\TestCasesDataFilePathMapper.xlsx");
		XSSFWorkbook workBook = new XSSFWorkbook(fis);
		XSSFSheet sheet = excelData.getSheet(workBook, "filePath");
		int testCaseNameColumnNumber = excelData.getColumnNumber(sheet, "testCaseName");
		int dataFilePathColumnNumber = excelData.getColumnNumber(sheet, "DataFilePath");
		int testCaseRowNumber = excelData.getRowNumber(sheet, testCaseNameColumnNumber, testCase);
		String filePath = System.getProperty("user.dir")
				+ excelData.getCellValue(sheet, testCaseRowNumber, dataFilePathColumnNumber);

		List<HashMap<Object, Object>> data = getJsonDataToMap(filePath);
		// data.get(index) is a hashMap

		return new Object[][] { { data.get(0) }, { data.get(1) } };
	}
}