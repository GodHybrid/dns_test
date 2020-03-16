import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class fieldChanged implements ExpectedCondition<Boolean>
{
    String text;
    String xPath;

    public fieldChanged(String xPath, String prevText)
    {
        this.xPath = xPath;
        text = prevText;
    }

    @Override
    public Boolean apply(WebDriver driver)
    {
        Boolean didPercentChange = driver.findElement(By.xpath(xPath)).getText().equals(text);
        return !didPercentChange;
    }
}
