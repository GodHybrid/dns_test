import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class Initialization
{
    public static TestProperties settingProperties = TestProperties.getInstance();

    public static void initializeDriver()
    {
        switch(settingProperties.getProperty("browser"))
        {
            case "firefox":
                System.setProperty("webdriver.gecko.driver", "drivers/" + settingProperties.getProperty("driver"));
                BasePageObj.driver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                System.setProperty("webdriver.chrome.driver", "drivers/" + settingProperties.getProperty("driver"));
                BasePageObj.driver = new ChromeDriver();
                break;
        }
        BasePageObj.driver.manage().window().maximize();
        BasePageObj.driver.manage().deleteAllCookies();
        BasePageObj.driver.get(settingProperties.getProperty("url"));
        BasePageObj.driver.manage().timeouts().implicitlyWait(Integer.parseInt(settingProperties.getProperty("general-timeout")), TimeUnit.SECONDS);
        BasePageObj.driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(settingProperties.getProperty("onload-timeout")), TimeUnit.SECONDS);
    }

    public static WebDriver getDriver()
    {
        return BasePageObj.driver;
    }
}
