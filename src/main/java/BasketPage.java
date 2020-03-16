import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class BasketPage extends BasePageObj
{
    String baseXPath = "//*/div[@class='cart-items__products']";

    @FindBy(xpath="//span[@class='restore-last-removed']")
    WebElement returnLastItem;

    @FindBy(xpath="//*/span[@class='cart-link__lbl']/span")
    WebElement basketPrice;

    Item lastDeleted;
    List<String> elements;
    ArrayList<Item> itemOptions;

    public BasketPage()
    {
        super();
        elements = new ArrayList<>();
        itemOptions = new ArrayList<>();
        completeListOfItems();
    }

    public void renew()
    {
        elements = new ArrayList<>();
        itemOptions = new ArrayList<>();
        completeListOfItems();
    }

    private void completeListOfItems()
    {
        for(int i = 0; i < driver.findElements(By.xpath(baseXPath)).size(); i++)
        {
            elements.add(baseXPath + "/div[" + (i + 1) + "]");
        }
        String xxPath = "/div/div[contains(@class,'product')]";
        for(String s : elements)
        {
            String name = waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'name')]/a"))).getText();
            String price = waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'price')]/div[@class='price']/div/span"))).getText();
            Boolean hasWarranty =
                    (waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'services')]/div/div/span[not(contains(@class,'list'))]")))
                            .isDisplayed()
                            &&
                            waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'services')]/div/div/span[not(contains(@class,'list'))]")))
                            .getText().contains("24"));
            String priceWithWarranty = waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'services')]/div/div/span[not(contains(@class,'list'))]/span")))
                    .getText().replace("(+", "");
            Item tmp = new Item(name, price, "Empty", true, hasWarranty, priceWithWarranty);
            itemOptions.add(tmp);
        }
    }

    public void deleteItem(String item)
    {
        waitForLoadElement(driver.findElement(By.xpath("//div[@class='cart-items__products']")));
        int index = 0;
        for(Item i : itemOptions)
        {
            if(i.name.contains(item.toLowerCase()))
            {
                index = itemOptions.indexOf(i);
                lastDeleted = i;
                break;
            }
        }
        WebElement tmpMinus = waitForLoadElement(driver.findElement(By.xpath(elements.get(index) + "/div/div[contains(@class,'product')]/div[contains(@class,'count')]/div/button[contains(@class,'minus')]")));
        waitForLoadElement(tmpMinus);
        tmpMinus.click();
        renew();
    }

    public Integer priceItemCorrect(String item)
    {
        renew();
        for(Item i : itemOptions)
        {
            if(i.name.contains(item.toLowerCase()))
            {
                return i.priceInt;
            }
        }
        return -1;
    }

    public Boolean checkWarranty(String item)
    {
        for(Item i : itemOptions)
        {
            if(i.name.contains(item.toLowerCase()))
            {
                if(i.hasWarranty) return true;
            }
        }
        return false;
    }

    public Integer getTotalSum()
    {
        Integer totalSum = 0;
        for(Item i : itemOptions)
        {
            if(i.hasWarranty)
            {
                totalSum += i.priceWithWarrantyInt;
            }
            else totalSum += i.priceInt;
        }
        return totalSum;
    }

    public void addItem(String item, int i)
    {
        int index = 0;
        for(Item ii : itemOptions)
        {
            if(ii.name.contains(item.toLowerCase()))
            {
                index = itemOptions.indexOf(ii);
                lastDeleted = ii;
                break;
            }
        }
        WebElement tmpPlus = waitForLoadElement(driver.findElement(By.xpath(elements.get(index) + "/div/div[contains(@class,'product')]/div[contains(@class,'count')]/div/button[contains(@class,'plus')]")));
        waitForLoadElement(tmpPlus);
        for(;i>0;i--)tmpPlus.click();
        renew();
    }

    public void returnItems()
    {
        waitForLoadElement(returnLastItem);
        returnLastItem.click();
        while(!contains(lastDeleted)) renew();
    }

    private Boolean contains(Item lastDeleted)
    {
        return itemOptions.contains(lastDeleted);
    }
}
