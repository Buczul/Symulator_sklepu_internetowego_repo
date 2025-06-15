package shoppingCartPCG;

import java.util.ArrayList;
import java.util.List;
import productPCG.ProductServices.ProductInfo;

public class ShoppingCart {
    private static ShoppingCart instance;
    private List<ProductInfo> items;
    private double totalPrice;

    //Prywatny konstruktor tzw. singleton
    private ShoppingCart() {
        items = new ArrayList<>();
        totalPrice = 0.0;
    }

    //Tworzenie instancji ShoppingCart, sprawdza czy instancja nie istnieje
    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    //Dodawanie do koszyka
    public void addItem(ProductInfo product) {
        items.add(product);
        totalPrice += product.getPrice();
    }

    //Usuwanie z koszyka
    public void removeItem(ProductInfo product) {
        if (items.remove(product)) {
            totalPrice -= product.getPrice();
        }
    }

    //Czyszczenie koszyka
    public void clearCart() {
        items.clear();
        totalPrice = 0.0;
    }

    // Zwracanie kopii listy produktów
    public List<ProductInfo> getItems() {
        return new ArrayList<>(items);
    }

    // Zwracanie sumarycznej ceny
    public double getTotalPrice() {
        return totalPrice;
    }

    // Sprawdzenie czy koszyk jest pusty
    public boolean isEmpty() {
        return items.isEmpty();
    }
}