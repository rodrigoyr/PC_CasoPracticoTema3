package com.example.casopractico3.service;

import com.example.casopractico3.annotations.Auditable;
import com.example.casopractico3.annotations.TimedProcess;
import com.example.casopractico3.orders.Order;
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
    public CompletableFuture<Void> processOrder(Order order) {
        try {
            // Simular tareas con pausas aleatorias
            long processingTime = new Random().nextInt(2000) + 1000;
            TimeUnit.MILLISECONDS.sleep(processingTime);

            // Simular errores aleatorios
            if (new Random().nextInt(5) == 0) { // Aproximadamente 20% de probabilidad de error
                if (new Random().nextBoolean()) {
                    throw new RuntimeException("Pago rechazado");
                } else {
                    throw new RuntimeException("Error al verificar stock");
                }
            }

            return CompletableFuture.completedFuture(null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        } catch (RuntimeException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
