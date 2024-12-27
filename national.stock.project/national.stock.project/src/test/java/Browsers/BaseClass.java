package Browsers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import Utilities.BrowserUtils;
import Utilities.ConfigReader;

import java.time.Duration;

public class BaseClass {
	
	private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    protected WebDriverWait wait;
    public WebDriver driver;
    private static final Logger logger = LogManager.getLogger(BaseClass.class);
    

    @Parameters("browser")
    @BeforeClass
    public void setUp(String browser) 
    {	
    	logger.info("Initialize Browser");
    	    	
    	
        if (browser.equalsIgnoreCase("chrome")) 
        {	
            WebDriverManager.chromedriver().setup(); 
            ChromeOptions chromeOptions = new ChromeOptions();
            BrowserUtils.configureChromeOptions(chromeOptions);
            driver = new ChromeDriver(chromeOptions);
            
        } 
        else if (browser.equalsIgnoreCase("edge")) 
        {
            WebDriverManager.edgedriver().setup();
            EdgeOptions edgeOptions = new EdgeOptions();
            BrowserUtils.configureEdgeOptions(edgeOptions);
            driver = new EdgeDriver(edgeOptions);
        }
        
        threadLocalDriver.set(driver);
        
        // Get Base URL and Stock Name from config.properties
        String baseUrl = ConfigReader.getProperty("baseUrl");
        
        logger.info("Base URL: {}", baseUrl);

        // Navigate to NSE website
        driver.get(baseUrl);
       logger.info("Navigated to NSE India website");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        logger.info("Browser setup complete.");
    }
       
    @AfterClass
    public void tearDown() 
    {
    	 WebDriver driver = threadLocalDriver.get();
         if (driver != null)
         {
             logger.info("Closing the browser.");
             driver.quit();
             threadLocalDriver.remove();
         }
    }
    
    public static WebDriver getDriver() {
    	return threadLocalDriver.get();
    }

}
