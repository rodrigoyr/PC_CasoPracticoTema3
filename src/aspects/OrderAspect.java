package aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.stereotype.Component;
import orders.Order;

@Aspect
@Component
public class OrderAspect {

    @Around("@annotation(annotations.TimedProcess) && args(order)")
    public Object audit(ProceedingJoinPoint joinPoint, Order order) throws Throwable {
        System.out.println("--- Auditoría: Inicio de proceso para " + order + " ---");
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            System.out.println("[PERFORMANCE] " + order + " procesado en " + elapsedTime + " ms");
            System.out.println("--- Auditoría: Fin de proceso para " + order + " ---");
            return result;
        } catch (Throwable t) {
            System.out.println("[ERROR] " + order + " falló: " + t.getMessage());
            throw t;
        }
    }

    @AfterThrowing(pointcut = "execution(* service.OrderService.processOrder(..)) && args(order)", throwing = "ex")
    public void logError(Order order, Exception ex) {
        // This is redundant as the @Around advice already logs the error.
        // It's here to demonstrate @AfterThrowing.
    }
}
