package StockPages;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomePage {
    private WebDriver driver;

   
    private static final Logger logger = LogManager.getLogger(HomePage.class);

    // Use @FindBy to locate elements
    @FindBy(xpath = "//input[@id='header-search-input']")
    private WebElement searchBox;

    @FindBy(xpath = "//div[@id='header-search-input_listbox']//p/span[2]")
    
    private WebElement stockSymbol;

    // Constructor that initializes the WebDriver with ChromeOptions
    public HomePage(WebDriver driver) {
        
        // Initialize WebDriver with the configured ChromeOptions
        this.driver = driver;

        // Initialize the elements on this page using PageFactory
        PageFactory.initElements(driver, this);
    }

    public void searchStock(String stockName) {
        logger.info("Searching for stock: {}", stockName);

        // Send the stock name to the search box
        searchBox.sendKeys(stockName);

        // Wait for the auto-suggestions to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement autoSuggestion = wait.until(ExpectedConditions.visibilityOf(stockSymbol));

        // Click on the suggestion
        autoSuggestion.click();
    }

}
