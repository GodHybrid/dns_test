import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class ItemPage extends BasePageObj
{
    Boolean boughtAnything;
    Integer price;
    Integer priceWithWarranty;

    @FindBy(xpath="//*/span[@class='cart-link__lbl']/span")
    WebElement basketPrice;

    @FindBy(xpath = "//div[@class='item-header']/div[contains(@class,'col-order')]/div[@class='clearfix']/div[contains(@class,'price-block')]/div/div/div/div[@class='price_g']/span")
    WebElement priceFull;

    @FindBy(xpath="//div[@class='item-header']/div[contains(@class,'col-order')]/div[@class='clearfix']/div[contains(@class,'price-block')]/div/div/div/div[contains(@class,'changed')]")
    WebElement priceChanged;

    @FindBy(xpath="//div[@class='item-header']/div[contains(@class,'recommends')]/div/div[@class='additional-warranty']/div[contains(@class,'desktop')]/select")
    WebElement warranty;

    @FindBy(xpath="//div[@class='item-header']/div[contains(@class,'order')]/div[contains(@class,'buttons')]/div[contains(@class,'buttons-wrapper')]/button")
    WebElement buyButton;

    public ItemPage()
    {
        this.boughtAnything = false;
    }

    public ItemPage(Boolean boughtAnything)
    {
        this.boughtAnything = boughtAnything;
    }

    public Integer savePrice()
    {
        if(!priceChanged.isDisplayed())
        {
            price = Integer.parseInt(waitForLoadElement(priceFull).getText().replace(" ",""));
            return price;
        }
        else
        {
            waitForLoadElement(priceChanged);
            priceWithWarranty = Integer.parseInt(waitForLoadElement(priceFull).getText().replace(" ",""));
            return priceWithWarranty;
        }
    }

    public void warrantyExpand(Integer i)
    {
        waitForLoadElement(warranty).click();
        warranty.sendKeys(i.toString());
        warranty.sendKeys(Keys.ENTER);
        waitForLoadElement(priceChanged);
    }

    public void buy()
    {
        if(boughtAnything)
        {
            Integer oldPrice = getBasketPriceCurrent(basketPrice);
            waitForLoadElement(buyButton);
            buyButton.click();
            waitForLoad.until(new fieldChanged("//*/span[@class='cart-link__lbl']/span", oldPrice));
        }
        else
        {
            waitForLoadElement(buyButton).click();
            waitForLoad.until(new fieldChanged("//*/span[@class='cart-link__lbl']/span", -1));
            boughtAnything = true;
        }
    }
}
