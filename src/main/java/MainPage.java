import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends BasePageObj
{
    @FindBy(xpath="//*/span[@class='cart-link__lbl']/span")
    WebElement basketPrice;
}
