package com.uniquindio.edu.strucst;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import com.uniquindio.edu.strucst.data.SampleData;
import com.uniquindio.edu.strucst.facade.DistributionCenter;
import com.uniquindio.edu.strucst.model.Courier;
import com.uniquindio.edu.strucst.model.Parcel;

/**
 * Hello world!
 *
 */
public class App {

    static Scanner sc = new Scanner(System.in);
    static DistributionCenter distributionCenter = new DistributionCenter();

    public static void main(String[] args) {
        System.out.println("Hello World!");
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

    static void registerDataTest() {
        distributionCenter.registerCouriers(SampleData.couriers());
        distributionCenter.registerParcels(SampleData.parcels());
    }

      static void showCurrentCourier() {
        for (Courier r : distributionCenter.couriers()) {
            IO.println(r);
        }
    }

    static void showMostHeaviestAndDivideAndConquer() {
        CallTrace trace = CallTrace.enabled();
        double max = DivideAndConquer.maximum(distributionCenter.parcels(), Comparator.naturalOrder(), trace);
        IO.println("El paquete mas pesado tiene peso: " + max);
        IO.println(trace);
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
