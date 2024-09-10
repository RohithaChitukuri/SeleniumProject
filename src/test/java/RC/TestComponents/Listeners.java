package RC.TestComponents;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import RC.Resources.ExtentReporterNg;


public class Listeners extends BaseTest implements ITestListener {
	ExtentReports report= ExtentReporterNg.getReportObject();
	ExtentTest test;
	//Thread safe execution
	ThreadLocal<ExtentTest> threadLocal =new ThreadLocal<ExtentTest>();
	

	  public void onTestStart(ITestResult result) {
	     test= report.createTest(result.getMethod().getMethodName());  
	     
	     threadLocal.set(test);//creates unique thread id for the test
	  }

	
	  public void onTestSuccess(ITestResult result) {
		  //test.log(Status.PASS, "Test Passed");
		//getting the test object of current thread
	    threadLocal.get().log(Status.PASS, "Test Passed");
	  }

	  public void onTestFailure(ITestResult result) {
		  //test.fail(result.getThrowable());
		  //getting the test object of current thread
	    threadLocal.get().fail(result.getThrowable());

	    //Take screenshot, attach it to report
	    String screenShotPath = null;
	    try {
			driver=(WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    try {
			  screenShotPath=getScreenShot(result.getMethod().getMethodName(),driver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //test.addScreenCaptureFromPath(screenShotPath, null);
		threadLocal.get().addScreenCaptureFromPath(screenShotPath, null);
	  }

	 
	  public void onTestSkipped(ITestResult result) {
	    
	  }
	   public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	    
	  }
	  public void onTestFailedWithTimeout(ITestResult result) {
	    onTestFailure(result);
	  }

	  public void onStart(ITestContext context) {
	  }
	  
	
	  public void onFinish(ITestContext context) {
		  report.flush();
		  
	  }
}
	  
