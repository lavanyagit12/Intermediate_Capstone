package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import Browsers.BaseClass;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reporting extends TestListenerAdapter {
	
	private WebDriver driver;
	private static final Logger logger = LogManager.getLogger(Reporting.class);
    private static ExtentSparkReporter sparkReporter;
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private int passedCount = 0;
    private int failedCount = 0;
    private int skippedCount = 0;

    @Override
    public void onStart(ITestContext testContext) 
    {
        if (extent == null) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportName = "Test-Report-" + timeStamp + ".html";

            // Using ExtentSparkReporter instead of ExtentHtmlReporter
            String reportPath = System.getProperty("user.dir") + "/Reports/" + reportName;
            sparkReporter = new ExtentSparkReporter(reportPath);

            // Report configurations
            sparkReporter.config().setDocumentTitle("Automation Testing Report");
            sparkReporter.config().setReportName("Functional Test Report");
            sparkReporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // System details
            extent.setSystemInfo("Host Name", "Localhost");
            extent.setSystemInfo("QA Name", "Lavanya");
            extent.setSystemInfo("OS", "Windows 11");
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
        if (extent != null) {
            // Log summary counts to the Extent Report
            extent.createTest("Test Summary")
                  .log(Status.INFO, "Total Passed Tests: " + passedCount)
                  .log(Status.INFO, "Total Failed Tests: " + failedCount)
                  .log(Status.INFO, "Total Skipped Tests: " + skippedCount);

            extent.flush();
        }
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        passedCount++;
        ExtentTest xTest = extent.createTest(tr.getName());
        test.set(xTest);

        test.get().log(Status.PASS, MarkupHelper.createLabel(tr.getName(), ExtentColor.GREEN));
        test.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        failedCount++;
        ExtentTest xTest = extent.createTest(tr.getName());
        test.set(xTest);

        test.get().log(Status.FAIL, MarkupHelper.createLabel(tr.getName(), ExtentColor.RED));
        test.get().log(Status.FAIL, "Test Failed");

        // Take a screenshot
        String screenshotPath = attachScreenshot(tr.getName());

        // Attach screenshot to the Extent Report
        if (screenshotPath != null) {
            test.get().addScreenCaptureFromPath(screenshotPath);
        }
    }

    public String attachScreenshot(String testName) {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        String screenshotPath = System.getProperty("user.dir") + "/Screenshots/" + testName + "_" + timeStamp + ".png";

        try {
        	driver = BaseClass.getDriver(); 
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(screenshotPath));
            logger.info("Screenshot saved at: " + screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            logger.warn("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        skippedCount++;
        ExtentTest xTest = extent.createTest(tr.getName());
        test.set(xTest);

        test.get().log(Status.SKIP, MarkupHelper.createLabel(tr.getName(), ExtentColor.ORANGE));
        test.get().log(Status.SKIP, "Test Skipped");
    }
    
}
