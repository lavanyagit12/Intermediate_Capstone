package StockPages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.ConfigReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.Duration;

public class SearchResultPage 
{

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(SearchResultPage.class);
    

    @FindBy(css = "#quoteLtp")
    private WebElement currentValue;

    @FindBy(xpath = "//div[@class='col-md-3 card-spacing priceinfodiv']//tbody/tr[1]/td[1]")
    private WebElement highValueTextElement;

    @FindBy(xpath = "//div[@class='col-md-3 card-spacing priceinfodiv']//tbody/tr[2]/td[1]")
    private WebElement lowValueTextElement;

    @FindBy(xpath = "//span[@id='week52highVal']")
    private WebElement fiftyTwoWeekHighValueElement;

    @FindBy(xpath = "//span[@id='week52lowVal']")
    private WebElement fiftyTwoWeekLowValueElement;

    // Constructor that initializes the WebDriver and PageFactory
    public SearchResultPage(WebDriver driver)
    {
        this.driver = driver;
        // Initialize the elements using PageFactory
        PageFactory.initElements(driver, this);       
    }
    
    public String getCurrentValue() 
    {
        logger.info("Waiting for the current value element to become visible");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,300);");

        // Wait for the current value element
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(currentValue));
                
        // Retrieve the text
        String currentValueText = currentValue.getText();

        logger.info("Current stock value retrieved: {}", currentValueText);
        return currentValueText;
    }

    public String getHighValue() 
    {
        logger.info("Waiting for the 52-week high value element to become visible");

        // Wait for the high value row
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(highValueTextElement));

        // Retrieve sibling value using the WebElement initialized with @FindBy
        String highValueTextContent = highValueTextElement.getText();
        String fiftyTwoWeekHighValue = fiftyTwoWeekHighValueElement.getText();

        logger.info("52 Week high value retrieved: {}", fiftyTwoWeekHighValue);

        System.out.println("High Value: " + highValueTextContent + " : " + fiftyTwoWeekHighValue);
        return fiftyTwoWeekHighValue;
    }

    public String getLowValue() 
    {
        logger.info("Waiting for the 52-week low value element to become visible");

        // Wait for the low value row
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(lowValueTextElement));

        // Retrieve sibling value using the WebElement initialized with @FindBy
        String lowValueTextContent = lowValueTextElement.getText();
        String fiftyTwoWeekLowValue = fiftyTwoWeekLowValueElement.getText();

        logger.info("52 Week Low value retrieved: {}", fiftyTwoWeekLowValue);

        System.out.println("Low Value: " + lowValueTextContent + " : " + fiftyTwoWeekLowValue);
        return fiftyTwoWeekLowValue;
    }
       
    // Calculate Profit/Loss
    public void calculateProfitLoss()
    {
    	
        try 
        {
            // Get values as strings
            String currentValueStr = getCurrentValue();
            String highValueStr = getHighValue();
            String lowValueStr = getLowValue();

            // Check if any of the values are "-" and Skip Calculation
            if (currentValueStr.equals("-") || highValueStr.equals("-") || lowValueStr.equals("-")) 
            {
                logger.warn("One or more values are unavailable (received '-')");
                System.out.println("Calculation is not possible due to missing data (value is '-')");
                return;
            }

            // Convert values to double for calculation
            double current = Double.parseDouble(currentValueStr);
            double high = Double.parseDouble(highValueStr);
            double low = Double.parseDouble(lowValueStr);

            // Calculate profit or loss
            double profitFromLow = ((current - low) / low) * 100;
            double lossFromHigh = ((current - high) / high) * 100;

            // Log the results
            logger.info("Profit from 52-week low: {}%", String.format("%.2f", profitFromLow));
            logger.info("Loss from 52-week high: {}%", String.format("%.2f", lossFromHigh));

            // Print the results
            System.out.println("Profit from 52-week low: " + String.format("%.2f", profitFromLow) + "%");
            System.out.println("Loss from 52-week high: " + String.format("%.2f", lossFromHigh) + "%");

        } 
        catch (NumberFormatException e) 
        {
            logger.error("Error parsing numeric values for profit/loss calculation", e);
        }
    }
    
    // Fetch stock values and update config.properties
    public void updateStockValuesInConfig(String stock)
    {
            // Get the current URL of the page
            String currentUrl = driver.getCurrentUrl();
            
            // Checking URL contains 'HINDALCO' or 'RCOM' and update respective stock values
            if (currentUrl.contains("symbol=HINDALCO")) 
            {
                String updatedCurrentValue = getCurrentValue();
                String updatedHighValue = getHighValue();
                String updatedLowValue = getLowValue();

                logger.info("Updating stock values in config.properties for HINDALCO");
                logger.info("Current Value: {}, High Value: {}, Low Value: {}", updatedCurrentValue, updatedHighValue, updatedLowValue);

                // Updating HINDALCO properties in config
                ConfigReader.updateStockValues("HINDALCO", updatedCurrentValue, updatedHighValue, updatedLowValue);
                System.out.println("HINDALCO stock values updated in config.properties");

            } 
            else if (currentUrl.contains("symbol=RCOM")) 
            {
                String updatedCurrentValue = getCurrentValue();
                String updatedHighValue = getHighValue();
                String updatedLowValue = getLowValue();

                logger.info("Updating stock values in config.properties for RCOM");
                logger.info("Current Value: {}, High Value: {}, Low Value: {}", updatedCurrentValue, updatedHighValue, updatedLowValue);

                // Updating RCOM properties in config
                ConfigReader.updateStockValues("RCOM", updatedCurrentValue, updatedHighValue, updatedLowValue);
                System.out.println("RCOM stock values updated in config.properties");

            } 
            else 
            {
                logger.warn("Unrecognized stock symbol in URL: {}", currentUrl);
                System.out.println("Unrecognized stock symbol in URL.");
            }
       }
   } 
   
