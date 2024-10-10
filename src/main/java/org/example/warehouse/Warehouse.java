package org.example.warehouse;


import java.math.BigDecimal;
import java.util.*; // Nödvändiga bibliotek
import java.util.stream.Collectors;


public class Warehouse {
    // Singleton-exemplar som hålls i ett Map, sparas baserat på namn
    private static Map<String, Warehouse> instances = new HashMap<>();


    private String name;
    private final List<ProductRecord> products = new ArrayList<>(); // Lista över produkter
    private final List<ProductRecord> changedProducts = new ArrayList<>(); // Lista över produkter med ändrat pris


    // Privat konstruktor, kan endast anropas internt
    private Warehouse(String name) {
        this.name = name;
    }


    // Statisk metod för att få en singleton-exemplar
    public static Warehouse getInstance() {
        return getInstance("Default"); // Anrop med standardnamn
    }


    // När den anropas med ett namn returnerar den samma exemplar om det finns, annars skapar ett nytt
    public static Warehouse getInstance(String name) {

        Warehouse instance = instances.get(name);
        if (instance == null) {
            instance = new Warehouse(name);
            instances.put(name, instance);
        } else {
            //Rensa automatiskt produkter när lagret hämtas
            instance.clearProducts();
        }
        return instance;
    }


    // Getter för namn
    public String getName() {
        return name;
    }


    // 1. isEmpty-metod: Kontrollerar om lagret är tomt eller inte
    public boolean isEmpty() {
        return products.isEmpty();
    }


    // 2. addProduct-metod: Lägger till en ny produkt i lagret
    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        if (uuid == null) {
            uuid = UUID.randomUUID(); // Skapar ett nytt UUID om det är null
        }
        if (price == null) {
            price = BigDecimal.ZERO; // Sätter priset till 0 om det är null
        }


        ProductRecord product = new ProductRecord(uuid, name, category, price);
        if (products.contains(product)) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }
        products.add(product); // Lägger till produkten i lagret
        return product;
    }


    // 3. getProducts-metod: Returnerar alla produkter i lagret
    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(products); // Returnerar listan som ej kan modifieras externt
    }


    // 4. getProductById-metod: Returnerar produkt baserat på ID
    public Optional<ProductRecord> getProductById(UUID id) {
        return products.stream()
                .filter(product -> product.uuid().equals(id))
                .findFirst();
    }


    // 5. updateProductPrice-metod: Uppdaterar priset på en produkt och lägger till den i listan över ändrade produkter
    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        ProductRecord product = getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with that id doesn't exist."));
        products.remove(product); // Tar bort den gamla produkten
        ProductRecord updatedProduct = new ProductRecord(id, product.name(), product.category(), newPrice);
        products.add(updatedProduct); // Lägger till produkten med nytt pris
        changedProducts.add(updatedProduct); // Lägger till produkten i listan över ändrade produkter
    }


    // 6. getChangedProducts-metod: Returnerar listan över produkter med ändrat pris
    public List<ProductRecord> getChangedProducts() {
        return changedProducts;
    }


    // 7. getProductsGroupedByCategories-metod: Grupperar produkterna efter kategorier
    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return products.stream()
                .collect(Collectors.groupingBy(ProductRecord::category)); // Grupperar efter kategorier
    }


    // 8. getProductsBy-metod: Returnerar produkter som tillhör en viss kategori
    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream()
                .filter(product -> product.category().equals(category))
                .collect(Collectors.toList());
    }

    public void clearProducts() {
        products.clear();
        changedProducts.clear();
    }
}
