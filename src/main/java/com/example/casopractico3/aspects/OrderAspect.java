package com.example.casopractico3.aspects;

import com.example.casopractico3.orders.Order;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderAspect {

    // --- Auditoría ---
    @Before("@annotation(com.example.casopractico3.annotations.Auditable) && args(order)")
    public void auditStart(JoinPoint joinPoint, Order order) {
        System.out.println("--- Auditoría: Inicio de proceso para " + order + " ---");
    }

    @AfterReturning(pointcut = "@annotation(com.example.casopractico3.annotations.Auditable) && args(order)")
    public void auditEnd(JoinPoint joinPoint, Order order) {
        System.out.println("--- Auditoría: Fin de proceso para " + order + " ---");
    }

    // --- Control de Rendimiento ---
    @Around("@annotation(com.example.casopractico3.annotations.TimedProcess) && args(order)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, Order order) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            System.out.println("[PERFORMANCE] " + order + " procesado en " + elapsedTime + " ms");
            return result;
        } catch (Throwable throwable) {
            // El error se relanza para que lo capture el advice @AfterThrowing
            throw throwable;
        }
    }

    // --- Manejo de Excepciones ---
    @AfterThrowing(pointcut = "execution(* com.example.casopractico3.service.OrderService.processOrder(..)) && args(order)", throwing = "ex")
    public void logError(Order order, Exception ex) {
        System.out.println("[ERROR] " + order + " falló: " + ex.getMessage() + " (Error simulado)");
        // No se imprime "Fin de proceso" para los pedidos con error
    }
}
