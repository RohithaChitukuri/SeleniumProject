package RC.Resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNg {
	
	public static ExtentReports getReportObject() {
		String path=System.getProperty("user.dir")+"\\Reports";
		
		ExtentSparkReporter sparkReporter=new ExtentSparkReporter(path);
		sparkReporter.config().setDocumentTitle("Test Results");
		sparkReporter.config().setReportName("Selenium Project Test Report");
		
		ExtentReports report=new ExtentReports();
		report.attachReporter(sparkReporter);
		report.setSystemInfo("Tester", "Rohitha");
		return report;
	}

}
