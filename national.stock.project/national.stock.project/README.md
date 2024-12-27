# Stock Automation Project

## Overview
This project automates the process of verifying stock information on the NSE India website using Selenium WebDriver. It searches for a stock, retrieves its current value, 52-week high, and 52-week low, and validates the results through assertions.

---

## Features
- Automates stock search and data retrieval on the NSE website.
- Supports configurable stock details through a properties file.
- Built using Java and Selenium with a clean, maintainable structure.
- Provides real-time logs with easy-to-read output.

---

## Prerequisites

Ensure the following tools and dependencies are installed before proceeding:

1. **Java Development Kit (JDK)** - Version 11 or higher  
   Download from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) or [AdoptOpenJDK](https://adoptopenjdk.net/).

2. **Maven** - Version 3.8 or higher  
   Download from [Apache Maven](https://maven.apache.org/download.cgi).

3. **Google Chrome** - Latest version  
   Download from [Google Chrome](https://www.google.com/chrome/).

4. **ChromeDriver** - Match your Chrome version  
   Download from [ChromeDriver](https://sites.google.com/chromium.org/driver/).
   
5. **TestNG** - Version 7.0 or higher  
   Install the TestNG plugin in your IDE (e.g., Eclipse or IntelliJ IDEA). 
   Refer to [TestNG Installation Guide](https://testng.org/doc/eclipse.html) for more details.


---

## Setup Instructions

### 1. Clone the Repository
git clone <repository-url>

cd stock-automation

### 2.Install Dependencies
mvn clean install

### 3. Configure the Project
#####Update the config.properties file located in the src/main/resources directory:

1.baseUrl=https://www.nseindia.com/

2.stockName = RCOM or HINDALCO

### 4. ChromeDriver Setup
##### Ensure chromedriver is available in your system PATH or specify its path in the BaseClass.java file:
WebDriverManager.chromedriver().setup();

# Running the Automation Script
##### 1. Run All Tests
Execute all test cases using Maven:mvn test

##### 2. Run a Specific Test
Use your IDE to run the StockInfoValidationTest class 

   
# Key Classes
##### BaseClass.java
Handles setup and teardown for Selenium WebDriver. Configures browser properties.

##### HomePage.java
Contains methods for interacting with the homepage, including stock search functionality.

##### SearchResultPage.java
Retrieves and validates stock details such as the current value, 52-week high, and 52-week low.

##### StockInfoValidationTest.java
Test class for validating the end-to-end functionality of stock information retrieval and verification.

# Logs and Reports
Logs: View console logs during execution for success or error messages.
Assertions: Verify that stock values are displayed correctly.

## Troubleshooting
##### 1. No Auto-Suggestions Appearing :
Ensure the browser is not in headless mode.
Increase the timeout duration in the explicit wait for the suggestions list.

##### 2.No Records Found Error:
Verify that the stock name is correctly entered in config.properties.
Manually check if the stock is available on the NSE website.

##### 3.Incompatibility with Browser Versions:
Ensure ChromeDriver and Chrome browser versions are compatible.
