import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class Initialization
{
    private static WebDriver driver;
    public static TestProperties settingProperties = TestProperties.getInstance();

    public static void initializeDriver()
    {
//        if(settingProperties.getProperty("browser")=="chrome")
//        {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
            driver = new ChromeDriver();
//        }
//        else
//        {
//            System.setProperty("webdriver.firefox.driver", "drivers/");
//            driver = new FirefoxDriver();
//        }
        driver.manage().window().maximize();
        driver.get(settingProperties.getProperty("url"));
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(settingProperties.getProperty("general-timeout")), TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(settingProperties.getProperty("onload-timeout")), TimeUnit.SECONDS);
    }

    public static WebDriver getDriver()
    {
        return driver;
    }
}
