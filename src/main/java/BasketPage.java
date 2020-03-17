import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        waitForLoadElement(driver.findElement(By.xpath(baseXPath + "/div")));
        for(int i = 0; i < driver.findElements(By.xpath(baseXPath + "/div")).size(); i++)
        {
            elements.add(baseXPath + "/div[" + (i + 1) + "]");
        }
        String xxPath = "/div/div[contains(@class,'product')]";
        for(String s : elements)
        {
            Integer count = Integer.parseInt(waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'count')]/div/input"))).getAttribute("value"));
            String name = waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'name')]/a"))).getText();
            String price = waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'price')]/div[@class='price']/div/span"))).getText();
            Boolean hasWarranty =  !driver.findElements(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'services')]/div/div/span[not(contains(@class,'list'))]")).isEmpty()
                    ? driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'services')]/div/div/span[not(contains(@class,'list'))]"))
                            .getText().contains("24")
                    : false;
            String priceOfWarranty;
            System.out.println(count);
            if(hasWarranty)
            {
                priceOfWarranty = waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'services')]/div/div/span[not(contains(@class,'list'))]/span")))
                        .getText().replace("(+", "").replace(")", "");
            }
            else priceOfWarranty = "0";
            Item tmp = new Item(name, price, "Empty", true, hasWarranty, priceOfWarranty);
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
                totalSum += i.priceInt;
                totalSum += i.priceOfWarrantyInt;
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
                break;
            }
        }
        String xPath = "//*/div[@class='cart-items__products']/div[" + (index + 1) + "]/div/div[contains(@class,'product')]/div[contains(@class,'count')]/div/input";
        WebElement counter = driver.findElement(By.xpath(xPath));
        Integer count = Integer.parseInt(waitForLoadElement(counter).getAttribute("value"));
        WebElement tmpPlus = waitForLoadElement(driver.findElement(By.xpath(elements.get(index) + "/div/div[contains(@class,'product')]/div[contains(@class,'count')]/div/button[contains(@class,'plus')]")));
        waitForLoadElement(tmpPlus);
//        for( Integer j = 0 ; j < i; j++)
//        {
            //Integer current = Integer.parseInt(waitForLoadElement(counter).getAttribute("value"));
        while(Integer.parseInt(counter.getAttribute("value")) != count + i)
        {
            waitForLoad.until(ExpectedConditions.elementToBeClickable(tmpPlus));
            waitForLoadElement(tmpPlus).click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//            Integer tmp = count + j;
//            waitForLoad.until(new fieldChanged(xPath, tmp));
//        }
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
