import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class fieldChanged implements ExpectedCondition<Boolean>
{
    String xPath;
    Integer prevNum;

    public fieldChanged(String xPath, Integer prevNum)
    {
        this.xPath = xPath;
        this.prevNum = prevNum;
    }

    @Override
    public Boolean apply(WebDriver driver)
    {
        Boolean didFieldChange = !(Integer.parseInt(driver.findElement(By.xpath(xPath)).getText().replace(" ", "")) == prevNum);
        return didFieldChange;
    }
}
