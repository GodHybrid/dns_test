import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

    public class textApproved implements ExpectedCondition<Boolean>
{
    String expectedText;
    WebElement element;
    Integer count = 0;

    public textApproved(String expectedText, WebElement element)
    {
        this.expectedText = expectedText;
        this.element = element;
    }

    @Override
    public Boolean apply(WebDriver driver)
    {
        Boolean isTextFieldCorrect = element.getAttribute("value").equals(expectedText);
        if(!isTextFieldCorrect)
        {
            element.clear();
            element.sendKeys(expectedText);
        }
        return isTextFieldCorrect;
    }
}
