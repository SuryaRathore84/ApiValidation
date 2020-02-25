package demoApi;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Properties;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.HashMap;


public class TestSetup {


	public static ExtentTest test;
	public static ExtentReports extent;
	public static ExtentHtmlReporter htmlReporter;
	public static String ResultFolderPath;
	public static String systemDir = System.getProperty("user.dir");
	public static String reportName= "";
	public static String TestCaseName = "";
	public static String OldTestCaseName = "";
	public static String newCase = "";
	public static boolean newTcReport = true;


	@BeforeSuite
	public void initializeExtendReport() throws IOException {			
		ResultFolderPath = systemDir + "\\ExecutionReports" ;	
		createFolder(ResultFolderPath);
		ResultFolderPath = ResultFolderPath +  "\\"  + getDateTimeStr()+  "\\" ;
		createFolder(ResultFolderPath);
		htmlReporter = StartHtmlReport(htmlReporter, ResultFolderPath);
		extent = StartExtentReport(htmlReporter);
	}
	

	
	@BeforeMethod
	public void beforeTestMethod(Method testMethod) {
		System.out.println(" Under @beforeTestMethod ");
		TestCaseName = testMethod.getName();
		test = testCreate(extent, TestCaseName);
		startTest(TestCaseName);
		System.out.println("XXXXXX======== Test Executed Started for " + TestCaseName + "==========XXXXXXXXX");
	}
	

	
	
	@BeforeClass
	public void beforeTest(ITestContext contextContext) {
		try {
			String className = this.getClass().getName();
			System.out.println(" Under @BeforeClass for " + className);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@AfterClass
	public void quitReport(ITestContext contextContext) throws IOException {
		System.out.println(
				"XXXXXX======== Test Executed Completed for " + contextContext.getName() + "==========XXXXXXXXX");
		extent.flush();
	}


	@AfterSuite
	public void sendReportEmail(ITestContext contextContext) throws InterruptedException {
		extent.flush();
		System.out.println("XXXXXX========XXXXX== End of Suite ===XXXXX==========XXXXXX");

	}
	
	/////////////////////////////////////// Reporting and Common function ////////////////////////////////////////////
	
	public static boolean createFolder(String FolderPath) {
		boolean result = false;
		try {
			File directory = new File(FolderPath);
			if (!directory.exists()) {
				result = directory.mkdir();
			}
		} catch (Exception e) {
			System.out.println("Error while creating the specific folder. Location " + FolderPath + ". Error message "
					+ e.getMessage());
		}
		return result;
	}
	
	
	
	public static String getDateTimeStr() {		
		 int MyDay = LocalDateTime.now().getDayOfMonth(); // dd
		 int MyYear = LocalDateTime.now().getYear(); // yyyy
		 int MyMonth = LocalDateTime.now().getMonthValue(); // yyyy
		 int Myhours = LocalDateTime.now().getHour();
		 int Mymins = LocalDateTime.now().getMinute();
		 int Mysecs = LocalDateTime.now().getSecond(); 
		 
		 return  ResultFolderPath = (String.valueOf(MyMonth) + String.valueOf(MyDay) +	
				 String.valueOf(MyYear) + String.valueOf(Myhours) + String.valueOf(Mymins) + String.valueOf(Mysecs)) ;
				
	}
	
	
	public static ExtentHtmlReporter StartHtmlReport(ExtentHtmlReporter htmlReporter, String ResultFolderPath) {
		reportName = ResultFolderPath + "Consolidated_" + "_" + getDateTimeStr() + ".html";
		htmlReporter = new ExtentHtmlReporter(reportName);

		htmlReporter.config().setDocumentTitle("Automation Execution Report");
		htmlReporter.config().setReportName("Test Execution Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);
		return htmlReporter;
	}
	

	public static ExtentReports StartExtentReport(ExtentHtmlReporter htmlReporter) {		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("OS", System.getProperty("os.name")); 
		return extent;
	}
	
	public static ExtentTest testCreate(ExtentReports extent, String Stepdetails) {
		test = extent.createTest(Stepdetails, Stepdetails);		
		return test;
	}	
	
	public static void startTest(String Stepdetails) {
		try {
			test.info(MarkupHelper.createLabel("XXXXXX======== Test Execution Started for " + Stepdetails
					+ "--->Iteration#" + "==========XXXXXXXXX", ExtentColor.BLACK));			
		}
		catch (Exception e) {
			System.out.println("error " + e.getMessage());
		}
	}
	
	public static void infoTest(String Stepdetails) {
		try {
			test.info(MarkupHelper.createLabel(Stepdetails, ExtentColor.BLUE));			
			System.out.println("test Info >>> " + Stepdetails + "   XXXXXXXXXXXXXXXXX");
		} catch (Exception e) {
			System.out.println("error " + e.getMessage());
		}
	}
	
	public static void passTest(String Stepdetails) {
		try {
			test.pass(MarkupHelper.createLabel(Stepdetails, ExtentColor.GREEN));		
			Assert.assertTrue(true);
		} catch (Exception e) {/// exception as test may not be initialize
			System.out.println("Excecption on passtest >>" + e.getMessage());		
			
		}
	}

	public static void failTest(String Stepdetails) {
		try {
			test.fail(MarkupHelper.createLabel(Stepdetails, ExtentColor.GREEN));		
			Assert.assertTrue(true);
		} catch (Exception e) {/// exception as test may not be initialize
			System.out.println("Excecption on passtest >>" + e.getMessage());		
			
		}
	}
	
	

}