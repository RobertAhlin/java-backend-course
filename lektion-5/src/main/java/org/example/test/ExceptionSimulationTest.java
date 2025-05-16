package org.example.test;

import org.example.controller.ProductController;
import org.example.handler.GlobalExceptionHandler;
import org.example.http.Response;
import org.example.repository.InMemoryProductRepository;
import org.example.repository.ProductRepository;
import org.example.service.ProductService;

public class ExceptionSimulationTest {
    public static void main(String[] args) {
        // Setup
        ProductRepository repo = new InMemoryProductRepository();
        ProductService service = new ProductService(repo);
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ProductController controller = new ProductController(service, handler);

        // === Testfall 1: Produkt finns ===
        System.out.println("--- Test 1: Product exists ---");
        Response res1 = controller.getProduct(1L, "/products/1");
        System.out.println(res1);

        // === Testfall 2: Produkt saknas ===
        System.out.println("--- Test 2: Product not found ---");
        Response res2 = controller.getProduct(999L, "/products/999");
        System.out.println(res2);

        // === Testfall 3: Otillräckligt lager ===
        System.out.println("--- Test 3: Insufficient stock ---");
        try {
            service.updateStock(1L, -100); // Försök minska med mer än som finns
        } catch (Exception e) {
            Response res3 = Response.error(handler.handleException(e, "/products/1/update-stock"));
            System.out.println(res3);
        }

        // === Testfall 4: Normal uppdatering ===
        System.out.println("--- Test 4: Valid stock update ---");
        try {
            service.updateStock(1L, -2); // Minska lagret med 2
            System.out.println("Stock updated successfully.");
        } catch (Exception e) {
            Response res4 = Response.error(handler.handleException(e, "/products/1/update-stock"));
            System.out.println(res4);
        }
    }
}
