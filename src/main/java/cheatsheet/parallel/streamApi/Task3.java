package cheatsheet.parallel.streamApi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Task3 {
    static class Product {
        private final String name;
        private final String category;
        private final double price;
        private final double rating;
        private final boolean inStock;

        public String getCategory() {
            return category;
        }

        public double getPrice() {
            return price;
        }

        public double getRating() {
            return rating;
        }

        public boolean isInStock() {
            return inStock;
        }

        public Product(String name, String category, double price, double rating, boolean inStock) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.rating = rating;
            this.inStock = inStock;
        }
    }

    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
                new Product("Laptop", "Electronics", 1500, 4.5, true),
                new Product("Smartphone", "Electronics", 800, 4.7, true),
                new Product("Desk", "Furniture", 300, 4.2, false),
                new Product("Chair", "Furniture", 150, 4.0, true),
                new Product("Headphones", "Electronics", 200, 4.3, true),
                new Product("Coffee Maker", "Appliances", 100, 4.1, true),
                new Product("Blender", "Appliances", 80, 3.9, false)
                // Добавьте больше продуктов по необходимости
        );
        Map<String, Double> averagePriceByCategory = products.stream()
                // Фильтруем только доступные товары
                .filter(Product::isInStock)
                // Фильтруем товары с рейтингом выше 4.0
                .filter(p -> p.getRating() > 4.0)
                // Группируем товары по категории
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        // Вычисляем среднюю цену в каждой категории
                        Collectors.averagingDouble(Product::getPrice)
                ));
        averagePriceByCategory.forEach((category, avgPrice) ->
                System.out.println("Категория: " + category + ", Средняя цена: " + avgPrice)
        );
    }
}
