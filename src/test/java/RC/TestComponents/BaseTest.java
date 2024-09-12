package RC.TestComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	public WebDriver driver;

	//returns hashMap list
	public List<HashMap<Object, Object>> getJsonDataToMap( String filePath) throws IOException {
		//json file
		File file = new File(filePath);
				
		// converting json file content to string
		String jsonString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		// converting string to list of HashMaps using jackson databind
		ObjectMapper mapper = new ObjectMapper();
		//readValue takes TypeReference object i.e which type to return, here it is List<HashMap<Oject,Object>>
		List<HashMap<Object, Object>> data = mapper.readValue(jsonString,
				new TypeReference<List<HashMap<Object, Object>>>() {
				});
		return data;

	}

	@BeforeMethod(alwaysRun = true)
	public void initializeDriver() throws IOException {

		Properties prop = new Properties();
		// FileInputStream f=new
		// FileInputStream("C:\\Users\\SUNNY\\eclipse-workspace\\SeleniumProject1\\src\\main\\java\\RC\\Resources\\GlobalData.properties");
		FileInputStream f = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\RC\\Resources\\GlobalData.properties");
		prop.load(f);
		String browserName = System.getProperty("browser")!=null?System.getProperty("browser"): prop.getProperty("browser");
		if (browserName.toLowerCase().contains("Chrome")) {
			ChromeOptions options=new ChromeOptions();
			WebDriverManager.chromedriver().setup();
			if(browserName.toLowerCase().contains("headless")) {
				options.addArguments("headless");
			 }
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
			driver.manage().window().setSize(new Dimension(1400,900));
		} 
		else if(browserName.equalsIgnoreCase("Firefox"))
		{
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			
		}
		
		else {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		// return driver;

	}
 
	public String getScreenShot(String fileName, WebDriver driver) throws IOException {
		TakesScreenshot ts= (TakesScreenshot)driver;
		File source=ts.getScreenshotAs(OutputType.FILE);
		String destFilePath=System.getProperty("user.dir")+"\\Reports\\"+fileName+".png";
		
		FileUtils.copyFile(source, new File(destFilePath));
		return destFilePath;
		
	}
	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		driver.quit();
	}

}
