package com.uniquindio.edu.strucst.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

import com.uniquindio.edu.strucst.algorithm.BinarySearch;
import com.uniquindio.edu.strucst.algorithm.DivideAndConquer;
import com.uniquindio.edu.strucst.algorithm.RecursiveAnalytics;
import com.uniquindio.edu.strucst.dispatch.ArrivalOrderDispatchStrategy;
import com.uniquindio.edu.strucst.dispatch.DispatchMode;
import com.uniquindio.edu.strucst.dispatch.DispatchStrategy;
import com.uniquindio.edu.strucst.dispatch.PriorityDispatchStrategy;
import com.uniquindio.edu.strucst.exception.AssignmentException;
import com.uniquindio.edu.strucst.model.Assignment;
import com.uniquindio.edu.strucst.model.Courier;
import com.uniquindio.edu.strucst.model.Parcel;
import com.uniquindio.edu.strucst.model.comparator.ParcelComparators;
import com.uniquindio.edu.strucst.repository.CourierRepository;
import com.uniquindio.edu.strucst.repository.ParcelRepository;
import com.uniquindio.edu.strucst.service.AssignmentService;
import com.uniquindio.edu.strucst.service.DeliveryHistory;

/**
 * Punto de entrada principal para operar el centro de distribución.
 *
 * La fachada reúne los repositorios, las estrategias de despacho, el servicio
 * de asignaciones y el historial de entregas. El código cliente puede registrar
 * paquetes y repartidores, consultar la información disponible, despachar
 * paquetes y ejecutar los algoritmos del dominio sin coordinar directamente
 * todos esos componentes.
 *
 * <p>Los paquetes se registran en las estrategias configuradas al momento de
 * crearlos. Por eso {@link #peekNextParcel(DispatchMode)} y
 * {@link #dispatchNext(DispatchMode)} respetan el modo de despacho solicitado.
 */
public class DistributionCenter {
    private final ParcelRepository parcels;
    private final CourierRepository couriers;
    private final AssignmentService assignments;
    private final DeliveryHistory history;
    private final Map<DispatchMode, DispatchStrategy> strategies = new EnumMap<>(DispatchMode.class);

    /**
     * Crea un centro con repositorios, historial y estrategias predeterminadas.
     *
     * Las estrategias configuradas son orden de llegada y prioridad.
     */
    public DistributionCenter() {
        this(new ParcelRepository(), new CourierRepository(), new DeliveryHistory(),
                List.of(new ArrivalOrderDispatchStrategy(), new PriorityDispatchStrategy()));
    }

    /**
     * Crea un centro usando las dependencias recibidas.
     *
     * @param parcels repositorio de paquetes
     * @param couriers repositorio de repartidores
     * @param history historial donde se registran las entregas
     * @param strategies estrategias de despacho disponibles
     */
    public DistributionCenter(ParcelRepository parcels, CourierRepository couriers, DeliveryHistory history,
            Collection<? extends DispatchStrategy> strategies) {
        this.parcels = parcels;
        this.couriers = couriers;
        this.history = history;
        this.assignments = new AssignmentService(history);
        strategies.forEach(strategy -> this.strategies.put(strategy.mode(), strategy));
    }

    /**
     * Registra un paquete y lo agrega a todas las estrategias configuradas.
     *
     * @param parcel paquete que se va a registrar
     */
    public void registerParcel(Parcel parcel) {
        parcels.save(parcel);
        strategies.values().forEach(strategy -> strategy.enqueue(parcel));
    }

    /**
     * Registra cada paquete de la colección.
     *
     * @param parcels paquetes que se van a registrar
     */
    public void registerParcels(Collection<Parcel> parcels) {
        parcels.forEach(this::registerParcel);
        parcels.forEach(parcel -> strategies.values().forEach(strategy -> strategy.enqueue(parcel)));
    }

    /**
     * Registra un repartidor en el repositorio.
     *
     * @param courier repartidor que se va a registrar
     */
    public void registerCourier(Courier courier) {
        couriers.save(courier);
    }

    /**
     * Registra cada repartidor de la colección.
     *
     * @param couriers repartidores que se van a registrar
     */
    public void registerCouriers(Collection<Courier> couriers) {
        couriers.forEach(this::registerCourier);
    }

    /**
     * Comprueba si existe un paquete con el código indicado.
     *
     * @param code código del paquete
     * @return {@code true} si el paquete existe
     */
    public boolean parcelExists(String code) {
        return parcels.exists(code);
    }

    /**
     * Busca un paquete por su código.
     *
     * @param code código del paquete
     * @return el paquete encontrado o {@link Optional#empty()} si no existe
     */
    public Optional<Parcel> findParcel(String code) {
        return parcels.findByCode(code);
    }

    /**
     * Busca un repartidor por su identificación.
     *
     * @param id identificación del repartidor
     * @return el repartidor encontrado o {@link Optional#empty()} si no existe
     */
    public Optional<Courier> findCourier(String id) {
        return couriers.findById(id);
    }

    /**
     * Obtiene todos los repartidores registrados.
     *
     * @return colección de repartidores
     */
    public Collection<Courier> couriers() {
        return couriers.findAll();
    }

    /**
     * Obtiene los paquetes en el orden en que fueron registrados.
     *
     * @return colección de paquetes
     */
    public Collection<Parcel> parcels() {
        return parcels.findAllInRegistrationOrder();
    }

    /**
     * Obtiene los paquetes en el orden en que fueron registrados.
     *
     * @return lista de paquetes
     */
    public List<Parcel> parcelsInRegistrationOrder() {
        return parcels.findAllInRegistrationOrder();
    }

    /**
     * Obtiene los paquetes que siguen pendientes, conservando el orden de registro.
     *
     * @return lista de paquetes pendientes
     */
    public List<Parcel> pendingParcels() {
        return parcels.findPendingInRegistrationOrder();
    }

    /**
     * Obtiene los destinos registrados sin ordenar.
     *
     * @return conjunto de destinos
     */
    public Set<String> destinations() {
        return parcels.findDestinations();
    }

    /**
     * Obtiene los destinos registrados en orden ascendente.
     *
     * @return conjunto ordenado de destinos
     */
    public SortedSet<String> destinationsSorted() {
        return parcels.findDestinationsSorted();
    }

    /**
     * Agrupa los paquetes según su destino.
     *
     * @return mapa cuya clave es el destino y cuyo valor son sus paquetes
     */
    public Map<String, List<Parcel>> parcelsByDestination() {
        return parcels.findGroupedByDestination();
    }

    /**
     * Obtiene todos los paquetes ordenados por su código.
     *
     * @return lista ordenada por código ascendente
     */
    public List<Parcel> parcelsSortedByCode() {
        return parcels.findAllSortedByCode();
    }

    /**
     * Devuelve una copia de los paquetes ordenada según el criterio recibido.
     *
     * <p>La lista interna del repositorio no se modifica.</p>
     *
     * @param criterion criterio de ordenamiento
     * @return copia ordenada de los paquetes
     */
    public List<Parcel> parcelsSortedBy(Comparator<Parcel> criterion) {
        List<Parcel> copy = new ArrayList<>(parcels.findAllInRegistrationOrder());
        copy.sort(criterion);
        return copy;
    }

    /**
     * Consulta el siguiente paquete según el modo de despacho.
     *
     * <p>La consulta no elimina el paquete de la estrategia.</p>
     *
     * @param mode modo de despacho que se desea consultar
     * @return siguiente paquete o {@link Optional#empty()} si no hay paquetes pendientes
     * @throws IllegalArgumentException si no existe una estrategia para el modo
     */
    public Optional<Parcel> peekNextParcel(DispatchMode mode) {
        return strategyFor(mode).peekNext();
    }

    /**
     * Despacha el siguiente paquete según el modo indicado.
     *
     * <p>Busca un repartidor disponible en la zona del paquete, retira el paquete
     * de la cola y crea la asignación correspondiente.</p>
     *
     * @param mode modo de despacho que se va a utilizar
     * @return asignación creada
     * @throws AssignmentException si no hay paquetes pendientes o no hay un
     *         repartidor disponible en la zona
     * @throws IllegalArgumentException si no existe una estrategia para el modo
     */
    public Assignment dispatchNext(DispatchMode mode) {
        DispatchStrategy strategy = strategyFor(mode);
        Parcel next = strategy.peekNext()
                .orElseThrow(() -> new AssignmentException("No hay paquetes pendientes por despachar"));
        Courier courier = couriers.findFirstAvailableInZone(next.getDestination())
                .orElseThrow(() -> new AssignmentException("No hay repartidores disponibles en "
                        + next.getDestination() + " para el paquete " + next.getCode()));
        // Se retira de la cola antes de asignar: al asignarlo deja de estar pendiente y
        // pollNext lo descartaría junto con el siguiente paquete.
        strategy.pollNext();
        return assignments.assign(next, courier);
    }

    /**
     * Asigna un paquete específico a un repartidor específico.
     *
     * @param parcelCode código del paquete
     * @param courierId identificación del repartidor
     * @return asignación creada
     * @throws AssignmentException si la asignación incumple una regla de negocio
     */
    public Assignment assign(String parcelCode, String courierId) {
        return assignments.assign(parcels.getByCode(parcelCode), couriers.getById(courierId));
    }

    /**
     * Registra la entrega de un paquete.
     *
     * @param parcelCode código del paquete entregado
     * @return asignación que se cerró
     * @throws AssignmentException si el paquete no tiene una asignación activa
     */
    public Assignment registerDelivery(String parcelCode) {
        return assignments.registerDelivery(parcels.getByCode(parcelCode));
    }

    /**
     * Obtiene el historial de entregas.
     *
     * @return historial de paquetes entregados
     */
    public DeliveryHistory deliveryHistory() {
        return history;
    }

    /**
     * Calcula recursivamente el peso total destinado a un municipio.
     *
     * @param destination municipio consultado
     * @return peso total en kilogramos
     */
    public double totalWeightTo(String destination) {
        return RecursiveAnalytics.totalWeightByDestination(parcels.findAllInRegistrationOrder(), destination);
    }

    /**
     * Cuenta recursivamente los paquetes cuya prioridad alcanza el mínimo indicado.
     *
     * @param minimumPriority prioridad mínima, incluida en la comparación
     * @return cantidad de paquetes que cumplen la condición
     */
    public int countWithMinimumPriority(int minimumPriority) {
        return RecursiveAnalytics.countWithMinimumPriority(parcels.findAllInRegistrationOrder(), minimumPriority);
    }

    /**
     * Busca un paquete por código usando búsqueda binaria.
     *
     * <p>La lista recibida debe estar ordenada ascendentemente por código.
     * El método no ordena la lista ni comprueba esa precondición.</p>
     *
     * @param sortedByCode lista ordenada por código
     * @param code código que se desea buscar
     * @return paquete encontrado o {@link Optional#empty()} si no existe
     */
    public Optional<Parcel> binarySearchByCode(List<Parcel> sortedByCode, String code) {
        int index = BinarySearch.searchByKey(sortedByCode, code, Parcel::getCode);
        return index == BinarySearch.NOT_FOUND ? Optional.empty() : Optional.of(sortedByCode.get(index));
    }

    /**
     * Busca el paquete de mayor peso mediante divide y vencerás.
     *
     * @return paquete más pesado o {@link Optional#empty()} si no hay paquetes
     */
    public Optional<Parcel> heaviestParcel() {
        List<Parcel> all = parcels.findAllInRegistrationOrder();
        return all.isEmpty() ? Optional.empty()
                : Optional.of(DivideAndConquer.maximum(all, ParcelComparators.BY_WEIGHT_ASC));
    }

    /**
     * Obtiene la estrategia configurada para un modo de despacho.
     *
     * @param mode modo de despacho
     * @return estrategia correspondiente
     * @throws IllegalArgumentException si no hay estrategia para el modo
     */
    private DispatchStrategy strategyFor(DispatchMode mode) {
        DispatchStrategy strategy = strategies.get(mode);
        if (strategy == null) {
            throw new IllegalArgumentException("No hay una estrategia configurada para " + mode);
        }
        return strategy;
    }

}
