import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/*
1) открыть dns-shop
2) в поиске найти playstation
3) кликнуть по playstation 4 slim black
4) запомнить цену
5) Доп.гарантия - выбрать 2 года
6) дождаться изменения цены и запомнить цену с гарантией
7) Нажать Купить
8) выполнить поиск Detroit
9) запомнить цену
10) нажать купить
11) проверить что цена корзины стала равна сумме покупок
12) перейри в корзину
13) проверить, что для приставки выбрана гарантия на 2 года
14) проверить цену каждого из товаров и сумму
15) удалить из корзины Detroit
16) проверить что Detroit нет больше в корзине и что сумма уменьшилась на цену Detroit
17) добавить еще 2 playstation (кнопкой +) и проверить что сумма верна (равна трем ценам playstation)
18) нажать вернуть удаленный товар, проверить что Detroit появился в корзине и сумма увеличилась на его значение
 */

public class DNSMainTest
{
    Integer price1, price2;

    @Before
    public void preparation()
    {
        Initialization.initializeDriver();
    }

    @Test
    public void execute()
    {
        MainPage main = new MainPage();
        main.search("playstation");

        SearchPage searchResult = new SearchPage();
        searchResult.choose("4 slim black");

        ItemPage items = new ItemPage();
        items.savePrice();
        items.warrantyExpand(2);
        items.savePrice();
        items.buy();
        items.search("Detroit");

        ItemPage marcusBoi = new ItemPage(true);
        marcusBoi.savePrice();
        marcusBoi.buy();
        Assert.assertEquals(items.priceWithWarranty + marcusBoi.price, (int) marcusBoi.getBasketPriceCurrent(marcusBoi.basketPrice));
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        marcusBoi.goToBasket();

        BasketPage checkIn = new BasketPage();
        Assert.assertTrue(checkIn.checkWarranty("PlayStation"));
        Assert.assertEquals("25999", checkIn.priceItemCorrect("PlayStation").toString());
        Assert.assertEquals("2599", checkIn.priceItemCorrect("Detroit").toString());
        Assert.assertEquals("33278", checkIn.getTotalSum().toString());
        checkIn.deleteItem("Detroit");
        Assert.assertEquals("-1", checkIn.priceItemCorrect("Detroit").toString());
        checkIn.addItem("PlayStation", 2);
        //Assert.assertEquals("77997", checkIn.priceItemCorrect("PlayStation").toString());
        int tmp = checkIn.getTotalSum();
        checkIn.returnItems();
        //Assert.assertTrue(tmp - 77997 == 2599);
    }

    @After
    public void finisher()
    {
        //selectorDriver.quit();
    }
}
