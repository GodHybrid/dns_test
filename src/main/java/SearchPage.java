import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchPage extends BasePageObj
{
    String baseXPath ="//div[@class='products-list__content']/div[1]/div[@class='catalog-items-list view-simple']";

    String searchItem;
    List<String> elements;
    ArrayList<Item> itemOptions;

    public SearchPage()
    {
        super();
        elements = new ArrayList<>();
        itemOptions = new ArrayList<>();
    }

    public void choose(String searchItem)
    {
        this.searchItem = searchItem;
        completeListOfItems();
        int chosenOption = shuffleThrough(searchItem);
        if(chosenOption >= 0)
        {
            String xPath = baseXPath + "/div[" + (chosenOption + 1) + "]/div/div[contains(@class, 'main')]/div[contains(@class, 'info')]/div/div[contains(@class,'title')]/div[contains(@class,'title')]/a";
            waitForLoadElement(driver.findElement(By.xpath(xPath))).click();
        }
    }

    private void completeListOfItems()
    {
        waitForLoadElement(driver.findElement(By.xpath(baseXPath)));
        for(int i = 0; i < driver.findElements(By.xpath(baseXPath)).size(); i++)
        {
            elements.add(baseXPath + "/div[" + (i + 1) + "]");
        }
        for(String s : elements)
        {
            String name = waitForLoadElement(driver.findElement(By.xpath(s + "/div/div[contains(@class, 'main')]/div[contains(@class, 'info')]/div/div[contains(@class,'title')]/div[contains(@class,'title')]/a"))).getText();
            String details = waitForLoadElement(driver.findElement(By.xpath(s + "/div/div[contains(@class, 'main')]/div[contains(@class, 'info')]/div/div[contains(@class,'title')]/span"))).getText();
            String price = waitForLoadElement(driver.findElement(By.xpath(s + "/div/div[contains(@class, 'main')]/div[contains(@class, 'price')]/div/div[@class='product-price__current']"))).getText();
            Boolean stockPresent = waitForLoadElement(driver.findElement(By.xpath(s + "/div/div[contains(@class, 'footer')]/div/div[contains(@class,'order')]/span"))).getAttribute("class").contains("no");
            Item tmp = new Item(name, price, details, stockPresent);
            itemOptions.add(tmp);
        }
    }

    private int shuffleThrough(String s)
    {
        for(Item i : itemOptions)
        {
            if (i.name.contains(s.toLowerCase()))
            {
                return itemOptions.indexOf(i);
            }
        }
        return -1;
    }
}
