package com.uniquindio.edu.strucst;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.uniquindio.edu.strucst.data.SampleData;
import com.uniquindio.edu.strucst.dispatch.DispatchMode;
import com.uniquindio.edu.strucst.exception.QuindioExpressException;
import com.uniquindio.edu.strucst.facade.DistributionCenter;
import com.uniquindio.edu.strucst.model.Assignment;
import com.uniquindio.edu.strucst.model.Courier;
import com.uniquindio.edu.strucst.model.Parcel;
import com.uniquindio.edu.strucst.model.comparator.ParcelComparators;


/**
 * Interfaz de consola de QuindíoExpress.
 *
 * Muestra el menú principal, lee las opciones del usuario y delega cada
 * operación en {@link DistributionCenter}. Los errores de negocio y de entrada
 * se informan por consola sin interrumpir el programa.
 */
public class App {

    static Scanner sc = new Scanner(System.in);
    static DistributionCenter distributionCenter = new DistributionCenter();

    /**
     * Ejecuta el ciclo principal del menú hasta que el usuario elija salir.
     *
     * <p>El ciclo termina también si la entrada estándar se cierra o si el hilo
     * es interrumpido. Cualquier otro error inesperado se informa y el menú
     * continúa.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            try {
                showMenu();
                running = executeOption(readInt("Seleccione una opción: "));
            } catch (QuindioExpressException | IllegalArgumentException e) {
                IO.println("No se pudo completar la operación: " + e.getMessage());
            } catch (NoSuchElementException e) {
                IO.println("Entrada cerrada, no se pueden leer más datos.");
                running = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                IO.println("Ejecución interrumpida.");
                running = false;
            } catch (RuntimeException e) {
                IO.println("Error inesperado: " + e);
            }
        }
        IO.println("Programa finalizado.");
    }

    /** Imprime las opciones disponibles del menú principal. */
    static void showMenu() {
        IO.println();
        IO.println("========= QUINDIOEXPRESS =========");
        IO.println("========= Importante para usar registrar datos primero =========");
        IO.println("1. Registrar datos de prueba");
        IO.println("2. Buscar paquete por codigo");
        IO.println("3. Ver paquetes en orden de registro");
        IO.println("4. Ver municipios (sin repetir y ordenados)");
        IO.println("5. Ver paquetes por municipio");
        IO.println("6. Ordenar paquetes (codigo, prioridad, peso, tiempo)");
        IO.println("7. Ver siguiente paquete a despachar");
        IO.println("8. Despachar siguiente paquete");
        IO.println("9. Asignar paquete a un repartidor");
        IO.println("10. Registrar entrega");
        IO.println("11. Ver historial de entregas");
        IO.println("12. Peso total por municipio (recursivo)");
        IO.println("13. Contar paquetes con prioridad minima (recursivo)");
        IO.println("14. Buscar paquete con busqueda binaria");
        IO.println("15. Paquete mas pesado (divide y venceras)");
        IO.println("16. Ver repartidores");
        IO.println(" 0. Salir");
    }

    /**
     * Ejecuta la operación asociada a la opción del menú y hace una pausa breve
     * para que el usuario alcance a leer el resultado.
     *
     * @param option número de opción elegida
     * @return {@code false} si el usuario eligió salir; {@code true} en caso contrario
     * @throws InterruptedException si el hilo es interrumpido durante la pausa
     * @throws QuindioExpressException si la operación incumple una regla de negocio
     * @throws IllegalArgumentException si los datos ingresados no son válidos
     */
    static boolean executeOption(int option) throws InterruptedException {
        switch (option) {
            case 0 -> {
                return false;
            }
            case 1 -> registerDataTest();
            case 2 -> findParcel();
            case 3 -> printList("Paquetes en orden de registro", distributionCenter.parcelsInRegistrationOrder());
            case 4 -> showDestinations();
            case 5 -> showParcelsByDestination();
            case 6 -> showSortedParcels();
            case 7 -> showNextParcel();
            case 8 -> dispatchNextParcel();
            case 9 -> assignParcel();
            case 10 -> registerDelivery();
            case 11 -> showDeliveryHistory();
            case 12 -> showTotalWeight();
            case 13 -> showPriorityCount();
            case 14 -> binarySearchParcel();
            case 15 -> showMostHeaviestAndDivideAndConquer();
            case 16 -> showCurrentCourier();
            default -> IO.println("Opción inválida.");
        }
        Thread.sleep(1000);
        return true;
    }

    /** Carga los datos de prueba en el centro de distribución. */
    static void registerDataTest() {
        SampleData.loadInto(distributionCenter);
        IO.println("Datos de prueba registrados.");
    }

    /** Busca un paquete por código e imprime su descripción si existe. */
    static void findParcel() {
        String code = readText("Código del paquete: ");
        distributionCenter.findParcel(code)
                .ifPresentOrElse(parcel -> IO.println(parcel.describe()),
                        () -> IO.println("No existe un paquete con ese código."));
    }

    /** Muestra los municipios sin repetir, sin ordenar y ordenados. */
    static void showDestinations() {
        IO.println("Municipios: " + distributionCenter.destinations());
        IO.println("Municipios ordenados: " + distributionCenter.destinationsSorted());
    }

    /** Lista los paquetes del municipio indicado por el usuario. */
    static void showParcelsByDestination() {
        String destination = readText("Municipio: ");
        printList("Paquetes de " + destination,
                distributionCenter.parcelsByDestination().getOrDefault(destination, List.of()));
    }

    /** Lista los paquetes según el criterio de orden elegido (código, prioridad, peso o tiempo). */
    static void showSortedParcels() {
        IO.println("1. Código  2. Prioridad  3. Peso  4. Tiempo");
        switch (readInt("Criterio: ")) {
            case 1 -> printList("Orden por código", distributionCenter.parcelsSortedByCode());
            case 2 -> printList("Orden por prioridad",
                    distributionCenter.parcelsSortedBy(ParcelComparators.BY_PRIORITY_DESC));
            case 3 -> printList("Orden por peso", distributionCenter.parcelsSortedBy(ParcelComparators.BY_WEIGHT_DESC));
            case 4 -> printList("Orden por tiempo",
                    distributionCenter.parcelsSortedBy(ParcelComparators.BY_ESTIMATED_TIME_ASC));
            default -> IO.println("Criterio inválido.");
        }
    }

    /**
     * Pide el modo de despacho al usuario.
     *
     * @return {@link DispatchMode#PRIORITY} si elige 2; en otro caso, orden de llegada
     */
    static DispatchMode readDispatchMode() {
        IO.println("1. Orden de llegada  2. Prioridad");
        return readInt("Modo: ") == 2 ? DispatchMode.PRIORITY : DispatchMode.ARRIVAL_ORDER;
    }

    /** Muestra el siguiente paquete a despachar sin retirarlo. */
    static void showNextParcel() {
        distributionCenter.peekNextParcel(readDispatchMode())
                .ifPresentOrElse(parcel -> IO.println("Siguiente paquete: " + parcel.describe()),
                        () -> IO.println("No hay paquetes pendientes."));
    }

    /** Despacha el siguiente paquete según el modo elegido. */
    static void dispatchNextParcel() {
        Assignment assignment = distributionCenter.dispatchNext(readDispatchMode());
        IO.println("Paquete despachado: " + assignment);
    }

    /** Asigna un paquete a un repartidor, ambos indicados por el usuario. */
    static void assignParcel() {
        String code = readText("Código del paquete: ");
        String courierId = readText("Identificación del repartidor: ");
        IO.println("Asignación realizada: " + distributionCenter.assign(code, courierId));
    }

    /** Registra la entrega del paquete cuyo código indica el usuario. */
    static void registerDelivery() {
        String code = readText("Código del paquete entregado: ");
        IO.println("Entrega registrada: " + distributionCenter.registerDelivery(code));
    }

    /** Imprime el historial de entregas. */
    static void showDeliveryHistory() {
        IO.println("Historial de entregas:");
        IO.println(distributionCenter.deliveryHistory());
    }

    /** Calcula e imprime el peso total de los paquetes hacia un municipio (recursivo). */
    static void showTotalWeight() {
        String destination = readText("Municipio: ");
        IO.println("Peso total: " + distributionCenter.totalWeightTo(destination) + " kg");
    }

    /** Cuenta los paquetes con prioridad igual o superior a la indicada (recursivo). */
    static void showPriorityCount() {
        int minimum = readInt("Prioridad mínima (1-5): ");
        IO.println("Cantidad de paquetes: " + distributionCenter.countWithMinimumPriority(minimum));
    }

    /** Busca un paquete por código con búsqueda binaria sobre la lista ordenada. */
    static void binarySearchParcel() {
        String code = readText("Código a buscar: ");
        distributionCenter.binarySearchByCode(distributionCenter.parcelsSortedByCode(), code)
                .ifPresentOrElse(parcel -> IO.println("Encontrado: " + parcel.describe()),
                        () -> IO.println("No existe un paquete con ese código."));
    }

    /** Imprime los repartidores registrados. */
    static void showCurrentCourier() {
        for (Courier r : distributionCenter.couriers()) {
            IO.println(r);
        }
    }

    /** Muestra el paquete más pesado usando divide y vencerás. */
    static void showMostHeaviestAndDivideAndConquer() {
        distributionCenter.heaviestParcel()
                .ifPresentOrElse(parcel -> IO.println("El paquete más pesado es: " + parcel.describe()),
                        () -> IO.println("No hay paquetes registrados."));
    }

    /**
     * Lee un número decimal, repitiendo la solicitud hasta que sea válido.
     * Acepta coma o punto como separador.
     *
     * @param message texto que se muestra al usuario
     * @return el número ingresado
     * @throws NoSuchElementException si la entrada estándar se cierra
     */
    static double realDecimal(String message) {
        while (true) {
            try {
                IO.println(message);
                return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                IO.println("Debe escribir un numero (ej: 4.5)");
            }
        }
    }

    /**
     * Lee un entero, repitiendo la solicitud hasta que sea válido.
     *
     * @param message texto que se muestra al usuario
     * @return el entero ingresado
     * @throws NoSuchElementException si la entrada estándar se cierra
     */
    static int readInt(String message) {
        while (true) {
            try {
                IO.println(message);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                IO.println("Debe escribir un numero entero");
            }
        }
    }

    /**
     * Lee una línea de texto sin espacios en los extremos.
     *
     * @param message texto que se muestra al usuario
     * @return el texto ingresado
     * @throws NoSuchElementException si la entrada estándar se cierra
     */
    static String readText(String message) {
        IO.println(message);
        return sc.nextLine().trim();
    }

    /**
     * Imprime una lista numerada de paquetes bajo un título.
     *
     * @param titulo encabezado de la lista
     * @param list paquetes que se van a imprimir
     */
    static void printList(String titulo, List<Parcel> list) {
        IO.println(titulo + ":");
        for (int i = 0; i < list.size(); i++) {
            IO.println("  " + (i + 1) + ". " + list.get(i));
        }
    }
}
