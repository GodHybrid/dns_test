public class Item
{
    public String name;
    public String price;
    public Integer priceInt;
    public String priceWithWarranty;
    public Integer priceWithWarrantyInt;
    public String details;
    public Boolean stockPresent;
    public Boolean hasWarranty = false;

    Item(String name, String price, String details, Boolean stockPresent)
    {
        this.name = name.toLowerCase();
        this.price = price;
        this.details = details.toLowerCase();
        this.stockPresent = stockPresent;
        parsePrice();
    }

    Item(String name, String price, String details, Boolean stockPresent, Boolean hasWarranty, String priceWithWarranty)
    {
        this.name = name.toLowerCase();
        this.price = price;
        this.details = details.toLowerCase();
        this.stockPresent = stockPresent;
        this.hasWarranty = hasWarranty;
        this.priceWithWarranty = priceWithWarranty;
        parsePrice();
    }

    private void parsePrice()
    {
        priceInt = Integer.parseInt(price.replace(" ",""));
        priceWithWarrantyInt = Integer.parseInt(price.replace(" ",""));
    }
}
