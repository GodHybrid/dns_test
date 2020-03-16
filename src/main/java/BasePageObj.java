import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePageObj
{
    WebDriver driver;
    WebDriverWait waitForLoad;

    @FindBy(xpath="//*[@class='ui-link cart-link']")
    WebElement basketButton;

    @FindBy(xpath="//*/span[@class='cart-link__lbl']/span")
    WebElement basketPrice;

    @FindBy(xpath="//*[@type='search' and contains(@placeholder, '100')]")
    WebElement searchLine;

    public BasePageObj()
    {
        this.driver = Initialization.getDriver();
        waitForLoad = new WebDriverWait(driver, 5);
        PageFactory.initElements(driver, this);
    }

    public Integer getBasketPriceCurrent()
    {
        waitForLoad.pollingEvery(Duration.ofMillis(200)).until(ExpectedConditions.visibilityOf(basketPrice));
        return Integer.parseInt(basketPrice.getText().replace(" ",""));
    }

    public WebElement waitForLoadElement(WebElement element)
    {
        return waitForLoad.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForTextApproved(WebElement element, String text)
    {
        while(!waitForLoad.until(new textApproved(text, element))) waitForTextApproved(element, text);
        return element;
    }

    public void search(String entry)
    {
        waitForLoadElement(searchLine).click();
        searchLine.sendKeys(entry);
        waitForTextApproved(searchLine, entry);
        searchLine.sendKeys(Keys.ENTER);
    }

    public void goToBasket()
    {
        waitForLoadElement(basketButton).click();
    }
}
