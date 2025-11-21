# Caso Práctico 3: Simulación de Procesamiento de Pedidos con AOP

## Miembros del Grupo

*   [Rodrigo Yepes Rubio]
*   [Gabriel Hernanz Rodríguez]

## Explicación de la Lógica

Este proyecto simula un sistema de procesamiento de pedidos concurrentes utilizando Spring Boot. La Programación Orientada a Aspectos (AOP) se utiliza para separar las responsabilidades transversales (auditoría, métricas de rendimiento y manejo de errores) de la lógica de negocio principal, manteniéndola limpia y enfocada.

La simulación se inicia en la clase `Main`, que crea 10 pedidos y los envía a un `OrderService`. Cada pedido es procesado en un hilo separado gracias a la anotación `@Async` de Spring.

Los aspectos interceptan los métodos del servicio marcados con anotaciones personalizadas para registrar información antes, después, o alrededor de la ejecución del procesamiento de un pedido.

## Archivos Relevantes

*   `pom.xml`: Define las dependencias del proyecto, incluyendo Spring Boot y Spring AOP.
*   `Main.java`: Punto de entrada de la aplicación que inicia la simulación de procesamiento de pedidos.
*   `Order.java`: Clase de datos (POJO) que representa un pedido con su ID, cliente y total.
*   `OrderService.java`: Contiene la lógica de negocio para procesar un pedido de forma asíncrona (`@Async`).
*   `OrderAspect.java`: Aspecto AOP que intercepta la ejecución para auditar, medir el rendimiento y registrar errores.
*   `@Auditable.java`: Anotación personalizada que marca un método para que el aspecto registre su inicio y fin.
*   `@TimedProcess.java`: Anotación personalizada que marca un método para que el aspecto mida su tiempo de ejecución.
