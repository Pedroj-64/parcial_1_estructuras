package com.uniquindio.edu.strucst;

import java.util.List;
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
 * Hello world!
 *
 */
public class App {

    static Scanner sc = new Scanner(System.in);
    static DistributionCenter distributionCenter = new DistributionCenter();



    public static void main(String[] args) throws InterruptedException {
        boolean running = true;
        while (running) {
            showMenu();
            int option = readInt("Seleccione una opción: ");
            try {
                running = executeOption(option);
            } catch (QuindioExpressException | IllegalArgumentException e) {
                IO.println("No se pudo completar la operación: " + e.getMessage());
            }
        }
        IO.println("Programa finalizado.");
    }

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

    static void registerDataTest() {
        distributionCenter.registerCouriers(SampleData.couriers());
        distributionCenter.registerParcels(SampleData.parcels());
        IO.println("Datos de prueba registrados.");
    }

    static void findParcel() {
        String code = readText("Código del paquete: ");
        distributionCenter.findParcel(code)
                .ifPresentOrElse(parcel -> IO.println(parcel.describe()),
                        () -> IO.println("No existe un paquete con ese código."));
    }

    static void showDestinations() {
        IO.println("Municipios: " + distributionCenter.destinations());
        IO.println("Municipios ordenados: " + distributionCenter.destinationsSorted());
    }

    static void showParcelsByDestination() {
        String destination = readText("Municipio: ");
        printList("Paquetes de " + destination,
                distributionCenter.parcelsByDestination().getOrDefault(destination, List.of()));
    }

    static void showSortedParcels() {
        IO.println("1. Código  2. Prioridad  3. Peso  4. Tiempo");
        switch (readInt("Criterio: ")) {
            case 1 -> printList("Orden por código", distributionCenter.parcelsSortedByCode());
            case 2 -> printList("Orden por prioridad", distributionCenter.parcelsSortedBy(ParcelComparators.BY_PRIORITY_DESC));
            case 3 -> printList("Orden por peso", distributionCenter.parcelsSortedBy(ParcelComparators.BY_WEIGHT_DESC));
            case 4 -> printList("Orden por tiempo", distributionCenter.parcelsSortedBy(ParcelComparators.BY_ESTIMATED_TIME_ASC));
            default -> IO.println("Criterio inválido.");
        }
    }

    static DispatchMode readDispatchMode() {
        IO.println("1. Orden de llegada  2. Prioridad");
        return readInt("Modo: ") == 2 ? DispatchMode.PRIORITY : DispatchMode.ARRIVAL_ORDER;
    }

    static void showNextParcel() {
        distributionCenter.peekNextParcel(readDispatchMode())
                .ifPresentOrElse(parcel -> IO.println("Siguiente paquete: " + parcel.describe()),
                        () -> IO.println("No hay paquetes pendientes."));
    }

    static void dispatchNextParcel() {
        Assignment assignment = distributionCenter.dispatchNext(readDispatchMode());
        IO.println("Paquete despachado: " + assignment);
    }

    static void assignParcel() {
        String code = readText("Código del paquete: ");
        String courierId = readText("Identificación del repartidor: ");
        IO.println("Asignación realizada: " + distributionCenter.assign(code, courierId));
    }

    static void registerDelivery() {
        String code = readText("Código del paquete entregado: ");
        IO.println("Entrega registrada: " + distributionCenter.registerDelivery(code));
    }

    static void showDeliveryHistory() {
        IO.println("Historial de entregas:");
        IO.println(distributionCenter.deliveryHistory());
    }

    static void showTotalWeight() {
        String destination = readText("Municipio: ");
        IO.println("Peso total: " + distributionCenter.totalWeightTo(destination) + " kg");
    }

    static void showPriorityCount() {
        int minimum = readInt("Prioridad mínima (1-5): ");
        IO.println("Cantidad de paquetes: " + distributionCenter.countWithMinimumPriority(minimum));
    }

    static void binarySearchParcel() {
        String code = readText("Código a buscar: ");
        distributionCenter.binarySearchByCode(distributionCenter.parcelsSortedByCode(), code)
                .ifPresentOrElse(parcel -> IO.println("Encontrado: " + parcel.describe()),
                        () -> IO.println("No existe un paquete con ese código."));
    }

    static void showCurrentCourier() {
        for (Courier r : distributionCenter.couriers()) {
            IO.println(r);
        }
    }

    static void showMostHeaviestAndDivideAndConquer() {
        distributionCenter.heaviestParcel()
                .ifPresentOrElse(parcel -> IO.println("El paquete más pesado es: " + parcel.describe()),
                        () -> IO.println("No hay paquetes registrados."));
    }

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

    static String readText(String message) {
        IO.println(message);
        return sc.nextLine().trim();
    }

    static void printList(String titulo, List<Parcel> list) {
        IO.println(titulo + ":");
        for (int i = 0; i < list.size(); i++) {
            IO.println("  " + (i + 1) + ". " + list.get(i));
        }
    }
}
