package service;

import annotations.Auditable;
import annotations.TimedProcess;
import orders.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

    @Async
    @Auditable
    @TimedProcess
    public CompletableFuture<String> processOrder(Order order) {
        System.out.println("[INFO] Pedido " + order.getId() + " recibido para el cliente: " + order.getCustomerName());

        try {
            // Simular tareas con pausas aleatorias
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(2000) + 1000);

            // Simular errores aleatorios
            if (new Random().nextBoolean()) {
                throw new RuntimeException("Pago rechazado (Error simulado)");
            }

            return CompletableFuture.completedFuture("Pedido " + order.getId() + " procesado exitosamente");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        } catch (RuntimeException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
