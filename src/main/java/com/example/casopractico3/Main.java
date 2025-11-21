package com.example.casopractico3;

import com.example.casopractico3.orders.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import com.example.casopractico3.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication(scanBasePackages = {"com.example.casopractico3.service", "com.example.casopractico3.aspects"})
@EnableAsync
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner run(OrderService orderService) {
        return args -> {
            System.out.println("=== INICIO DE SIMULACIÓN DE PEDIDOS ===");

            List<Order> orders = new ArrayList<>();
            orders.add(new Order(1, 100.0, "Ana López"));
            orders.add(new Order(2, 150.0, "Carlos Gómez"));
            orders.add(new Order(3, 200.0, "Marta Ruiz"));
            orders.add(new Order(4, 50.0, "Diego Torres"));
            orders.add(new Order(5, 300.0, "Laura Fernández"));
            orders.add(new Order(6, 75.0, "Pedro Ramírez"));
            orders.add(new Order(7, 125.0, "Sofía Medina"));
            orders.add(new Order(8, 250.0, "Juan Pérez"));
            orders.add(new Order(9, 175.0, "Lucía Vargas"));
            orders.add(new Order(10, 225.0, "Jorge Castillo"));

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (Order order : orders) {
                System.out.println("[INFO] Pedido " + order.getId() + " recibido para el cliente: " + order.getCustomerName());
                futures.add(orderService.processOrder(order));
            }

            AtomicInteger successfulOrders = new AtomicInteger(0);
            AtomicInteger failedOrders = new AtomicInteger(0);

            long totalStartTime = System.currentTimeMillis();

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

            allFutures.whenComplete((res, ex) -> {
                for (CompletableFuture<Void> future : futures) {
                    if (future.isDone() && !future.isCompletedExceptionally()) {
                        successfulOrders.incrementAndGet();
                    } else {
                        failedOrders.incrementAndGet();
                    }
                }
            });

            allFutures.join(); 

            long totalElapsedTime = System.currentTimeMillis() - totalStartTime;

            System.out.println("\n=== PROCESAMIENTO FINALIZADO ===");
            System.out.println("Pedidos completados exitosamente: " + successfulOrders.get());
            System.out.println("Pedidos con error: " + failedOrders.get());
            System.out.println("Tiempo total de simulación: " + totalElapsedTime + " ms aprox.");
        };
    }
}
