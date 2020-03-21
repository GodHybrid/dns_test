import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    @Before
    public void preparation()
    {
        Initialization.initializeDriver();
    }

    @Test
    public void execute()
    {
        //1
        MainPage main = new MainPage();
        //2
        main.search("playstation");

        SearchPage searchResult = new SearchPage();
        //3
        searchResult.choose("4 slim black");

        ItemPage items = new ItemPage();
        //4
        items.savePrice();
        //5
        items.warrantyExpand(2);
        //6
        items.savePrice();
        //7
        items.buy();
        //8
        items.search("Detroit");

        ItemPage marcusBoi = new ItemPage(true);
        //9
        marcusBoi.savePrice();
        //10
        marcusBoi.buy();
        //11
        Assert.assertEquals(items.priceWithWarranty + marcusBoi.price, (int) marcusBoi.getBasketPriceCurrent(marcusBoi.basketPrice));

        //12
        BasketPage checkIn = marcusBoi.goToBasket();
        //13
        Assert.assertTrue(checkIn.checkWarranty("PlayStation", 24));
        //14
        Assert.assertEquals(Initialization.settingProperties.getProperty("ps_price"), checkIn.priceItemCorrect("PlayStation", false).toString());
        Assert.assertEquals(Initialization.settingProperties.getProperty("detroit_price"), checkIn.priceItemCorrect("Detroit", true).toString());
        Assert.assertEquals(checkIn.getTotalSum().toString(), checkIn.basketPrice.getText().replace(" ", ""));
        //15
        checkIn.deleteItem("Detroit");
        //16
        Assert.assertEquals("-1", checkIn.priceItemCorrect("Detroit", false).toString());
        //17
        checkIn.addItem("PlayStation", 2);
        Assert.assertEquals(Integer.parseInt(Initialization.settingProperties.getProperty("ps_price")) * 3, (long)checkIn.priceItemCorrect("PlayStation", false));
        //18
        checkIn.returnItems();
        Assert.assertEquals("Something wrong uwu", checkIn.getTotalSum() - checkIn.priceItemCorrect("PlayStation", true), Integer.parseInt(Initialization.settingProperties.getProperty("detroit_price")));
    }

    @After
    public void finish()
    {
        BasePageObj.driver.quit();
    }
}
