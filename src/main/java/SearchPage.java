import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends BasePageObj
{
    String baseXPath ="//div[@class='products-list__content']/div[1]/div[@class='catalog-items-list view-simple']";

    @FindBy(xpath="//*/span[@class='cart-link__lbl']/span")
    WebElement basketPrice;

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
            JavascriptExecutor je = (JavascriptExecutor) driver;
            je.executeScript("arguments[0].scrollIntoView(false);", driver.findElement(By.xpath(baseXPath + "/div[" + (chosenOption + 1) + "]/div/div[contains(@class, 'main')]/div[contains(@class, 'info')]/div/div[contains(@class,'title')]/div[contains(@class,'title')]")));
            String xPath = baseXPath + "/div[" + (chosenOption + 1) + "]/div/div[contains(@class, 'main')]/div[contains(@class, 'info')]/div/div[contains(@class,'title')]/div[contains(@class,'title')]/a";
            waitForLoadElement(driver.findElement(By.xpath(xPath))).click();
        }
    }

    private void completeListOfItems()
    {
        waitForLoadElement(driver.findElement(By.xpath(baseXPath + "/div")));
        for(int i = 0; i < driver.findElements(By.xpath(baseXPath + "/div")).size(); i++)
        {
            elements.add(baseXPath + "/div[" + (i + 1) + "]");
        }
        JavascriptExecutor je = (JavascriptExecutor) driver;
        for(String s : elements)
        {
            WebElement tmp = waitForLoadElement(driver.findElement(By.xpath(s)));
            je.executeScript("arguments[0].scrollIntoView(false);", tmp);
            waitForLoad.until(ExpectedConditions.visibilityOf(tmp));
            String name = waitForLoadElement(driver.findElement(By.xpath(s + "/div/div[contains(@class, 'main')]/div[contains(@class, 'info')]/div/div[contains(@class,'title')]/div[contains(@class,'title')]/a"))).getText();
            String details = waitForLoadElement(driver.findElement(By.xpath(s + "/div/div[contains(@class, 'main')]/div[contains(@class, 'info')]/div/div[contains(@class,'title')]/span"))).getText();
            String price = waitForLoadElement(driver.findElement(By.xpath(s + "/div/div[contains(@class, 'main')]/div[contains(@class, 'price')]/div/div[@class='product-price__current']"))).getText();
            Boolean stockPresent = waitForLoadElement(driver.findElement(By.xpath(s + "/div/div[contains(@class, 'footer')]/div/div[contains(@class,'order')]/span"))).getAttribute("class").contains("no");
            Item newItem = new Item(name, price, details, stockPresent);
            itemOptions.add(newItem);
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
