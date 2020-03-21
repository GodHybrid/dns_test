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
            String price1 = waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'price')]/div[@class='price']/div/span"))).getText().replace(" ", "");
            Integer currPr = Integer.parseInt(price1)/count;
            String price = currPr.toString();
            Boolean hasWarranty =  !driver.findElements(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'services')]")).isEmpty();
            String priceOfWarranty;
            Integer monthsWarranty;
            //System.out.println(count);
            if(hasWarranty)
            {
                priceOfWarranty = waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'services')]/div/div/span[not(contains(@class,'list'))]/span")))
                        .getText().replace("(+", "").replace(")", "");
                monthsWarranty = Integer.parseInt(waitForLoadElement(driver.findElement(By.xpath(s + xxPath + "/div[contains(@class,'info')]/div/div[contains(@class,'services')]/div/div/span[not(contains(@class,'list'))]")))
                        .getText().split("[(]")[0].replaceAll("[^0-9]",""));
            }
            else
            {
                priceOfWarranty = "0";
                monthsWarranty = 0;
            }
            for(; count > 0; count--) {
                Item tmp = new Item(name, price, "Empty", true, hasWarranty, priceOfWarranty, monthsWarranty);
                itemOptions.add(tmp);
            }
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

    public Integer priceItemCorrect(String item, Boolean includeWarranty)
    {
        renew();
        int totalSum = 0;
        for(Item i : itemOptions)
        {
            if(i.name.contains(item.toLowerCase()))
            {
                totalSum += i.priceInt;
                if(i.hasWarranty && includeWarranty) totalSum += i.priceOfWarrantyInt;
            }
        }
        return totalSum == 0 ? -1 : totalSum;
    }

    public Boolean checkWarranty(String item, Integer months)
    {
        for(Item i : itemOptions)
        {
            if(i.name.contains(item.toLowerCase()))
            {
                if(i.hasWarranty && i.monthsWarranty == months) return true;
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
        for(Item ii : itemOptions)
        {
            if(ii.name.contains(lastDeleted.name))
            {
                return true;
            }
        }
        return false;
    }
}
