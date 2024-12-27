package StockTests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Browsers.BaseClass;
import StockPages.HomePage;
import StockPages.SearchResultPage;
import Utilities.*;

public class StockInfoValidationTest extends BaseClass {

    private static final Logger logger = LogManager.getLogger(StockInfoValidationTest.class);
    
    @Test(dataProvider = "stockDataProvider")
    public void verifyStockInformation(String stockName) throws InterruptedException {
   
    	logger.info("Starting the stock information verification test for stock: {}", stockName);

       
        // Page object for homePage
        HomePage homePage = new HomePage(driver);

        // Search for stock
        logger.info("Initiating stock search for stock: {}", stockName);
        homePage.searchStock(stockName);
        
        
        //Page object for SearchResults
        SearchResultPage searchResultpage = new SearchResultPage(driver);
        searchResultpage.updateStockValuesInConfig("stock");
        
        // Expected values
        String expectedCurrentValue = ConfigReader.getProperty(stockName + "_currentValue");
        String expectedHighValue = ConfigReader.getProperty(stockName + "_highValue");
        String expectedLowValue = ConfigReader.getProperty(stockName + "_lowValue");
        
        logger.info("Validating stock values for {}", stockName);

        // Actual values
        String actualCurrentValue = searchResultpage.getCurrentValue();
        String actualHighValue = searchResultpage.getHighValue();
        String actualLowValue = searchResultpage.getLowValue();

        // assertions
        Assert.assertEquals(actualCurrentValue, expectedCurrentValue, "Current value does not match");
        Assert.assertEquals(actualHighValue, expectedHighValue, "High value does not match");
        Assert.assertEquals(actualLowValue, expectedLowValue, "Low value does not match");

        //Calculate profit/Loss
        searchResultpage.calculateProfitLoss();
        
        logger.info("Stock information verification test completed successfully for stock: {}", stockName);
    }
    
    @DataProvider(name = "stockDataProvider")
    public Object[][] stockDataProvider() {
        // Test data: each row is a test case with stockName and expected values
        return new Object[][]{
            {"RCOM"},
            {"HINDALCO"}
        };
    }
}

