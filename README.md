# Quindío Express

Proyecto académico en Java para simular la gestión de paquetes, repartidores y entregas en un centro de distribución.

## Descripción

Quindío Express es una aplicación de consola que permite registrar paquetes, consultarlos, ordenarlos, asignarlos a repartidores y registrar entregas. El proyecto está pensado para practicar estructuras de datos, algoritmos de búsqueda y ordenamiento, además de la organización de lógica de negocio en capas.

## Funcionalidades

- Registrar datos de prueba
- Buscar un paquete por código
- Ver paquetes en orden de registro
- Consultar municipios sin repetir y ordenados
- Ver paquetes por municipio
- Ordenar paquetes por:
  - código
  - prioridad
  - peso
  - tiempo estimado
- Ver el siguiente paquete a despachar
- Despachar el siguiente paquete
- Asignar paquetes a repartidores
- Registrar entregas
- Consultar historial de entregas
- Calcular peso total por municipio (recursivo)
- Contar paquetes con prioridad mínima (recursivo)
- Buscar paquetes con búsqueda binaria
- Encontrar el paquete más pesado con divide y vencerás
- Ver información de los repartidores

## Estructura del proyecto

```text
parcial_1_estructuras/
├── src/
│   ├── main/java/com/uniquindio/edu/strucst/
│   │   ├── algorithm/
│   │   ├── data/
│   │   ├── dispatch/
│   │   ├── exception/
│   │   ├── facade/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── structure/
│   │   └── App.java
│   └── test/java/
├── pom.xml
├── .gitignore
├── README.md
└── target/
```

## Tecnologías

- Java
- Maven
- JUnit

## Requisitos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- JDK 25 o compatible con el proyecto
- Maven

## Cómo ejecutar

1. Clona el repositorio:

```bash
git clone https://github.com/Pedroj-64/parcial_1_estructuras.git
cd parcial_1_estructuras
```

2. Compila el proyecto:

```bash
mvn clean compile
```

3. Ejecuta la aplicación:

```bash
java -cp target/classes com.uniquindio.edu.strucst.App
```

4. Para correr las pruebas:

```bash
mvn test
```

## Menú principal

Al iniciar la aplicación, se muestra este menú:

```text
========= QUINDIOEXPRESS =========
1. Registrar datos de prueba
2. Buscar paquete por codigo
3. Ver paquetes en orden de registro
4. Ver municipios (sin repetir y ordenados)
5. Ver paquetes por municipio
6. Ordenar paquetes (codigo, prioridad, peso, tiempo)
7. Ver siguiente paquete a despachar
8. Despachar siguiente paquete
9. Asignar paquete a un repartidor
10. Registrar entrega
11. Ver historial de entregas
12. Peso total por municipio (recursivo)
13. Contar paquetes con prioridad minima (recursivo)
14. Buscar paquete con busqueda binaria
15. Paquete mas pesado (divide y venceras)
16. Ver repartidores
0. Salir
```

## Puntos de entrada

La aplicación inicia desde:

- `src/main/java/com/uniquindio/edu/strucst/App.java`

## Observación

Este proyecto es de carácter académico y está orientado a reforzar conceptos de estructuras de datos, algoritmos y modelado de un sistema de logística básica.

## Licencia

Proyecto desarrollado con fines educativos dentro del curso de estructuras de datos.
