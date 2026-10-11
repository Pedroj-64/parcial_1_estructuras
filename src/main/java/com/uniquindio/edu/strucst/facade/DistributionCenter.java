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

public class DistributionCenter {
    private final ParcelRepository parcels;
    private final CourierRepository couriers;
    private final AssignmentService assignments;
    private final DeliveryHistory history;
    private final Map<DispatchMode, DispatchStrategy> strategies = new EnumMap<>(DispatchMode.class);

    public DistributionCenter() {
        this(new ParcelRepository(), new CourierRepository(), new DeliveryHistory(),
                List.of(new ArrivalOrderDispatchStrategy(), new PriorityDispatchStrategy()));
    }

    public DistributionCenter(ParcelRepository parcels, CourierRepository couriers, DeliveryHistory history,
            Collection<? extends DispatchStrategy> strategies) {
        this.parcels = parcels;
        this.couriers = couriers;
        this.history = history;
        this.assignments = new AssignmentService(history);
        strategies.forEach(strategy -> this.strategies.put(strategy.mode(), strategy));
    }

    public void registerParcel(Parcel parcel) {
        parcels.save(parcel);
        strategies.values().forEach(strategy -> strategy.enqueue(parcel));
    }

    public void registerParcels(Collection<Parcel> parcels) {
        parcels.forEach(this::registerParcel);
        parcels.forEach(parcel -> strategies.values().forEach(strategy -> strategy.enqueue(parcel)));
    }

    public void registerCourier(Courier courier) {
        couriers.save(courier);
    }

    public void registerCouriers(Collection<Courier> couriers) {
        couriers.forEach(this::registerCourier);
    }

    public boolean parcelExists(String code) {
        return parcels.exists(code);
    }

    public Optional<Parcel> findParcel(String code) {
        return parcels.findByCode(code);
    }

    public Optional<Courier> findCourier(String id) {
        return couriers.findById(id);
    }

    public Collection<Courier> couriers() {
        return couriers.findAll();
    }

    public Collection<Parcel> parcels() {
        return parcels.findAllInRegistrationOrder();
    }

    public List<Parcel> parcelsInRegistrationOrder() {
        return parcels.findAllInRegistrationOrder();
    }

    public List<Parcel> pendingParcels() {
        return parcels.findPendingInRegistrationOrder();
    }

    public Set<String> destinations() {
        return parcels.findDestinations();
    }

    public SortedSet<String> destinationsSorted() {
        return parcels.findDestinationsSorted();
    }

    public Map<String, List<Parcel>> parcelsByDestination() {
        return parcels.findGroupedByDestination();
    }

    public List<Parcel> parcelsSortedByCode() {
        return parcels.findAllSortedByCode();
    }

    public List<Parcel> parcelsSortedBy(Comparator<Parcel> criterion) {
        List<Parcel> copy = new ArrayList<>(parcels.findAllInRegistrationOrder());
        copy.sort(criterion);
        return copy;
    }

    public Optional<Parcel> peekNextParcel(DispatchMode mode) {
        return strategyFor(mode).peekNext();
    }

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

    public Assignment assign(String parcelCode, String courierId) {
        return assignments.assign(parcels.getByCode(parcelCode), couriers.getById(courierId));
    }

    public Assignment registerDelivery(String parcelCode) {
        return assignments.registerDelivery(parcels.getByCode(parcelCode));
    }

    public DeliveryHistory deliveryHistory() {
        return history;
    }


    public double totalWeightTo(String destination) {
        return RecursiveAnalytics.totalWeightByDestination(parcels.findAllInRegistrationOrder(), destination);
    }

    public int countWithMinimumPriority(int minimumPriority) {
        return RecursiveAnalytics.countWithMinimumPriority(parcels.findAllInRegistrationOrder(), minimumPriority);
    }

    public Optional<Parcel> binarySearchByCode(List<Parcel> sortedByCode, String code) {
        int index = BinarySearch.searchByKey(sortedByCode, code, Parcel::getCode);
        return index == BinarySearch.NOT_FOUND ? Optional.empty() : Optional.of(sortedByCode.get(index));
    }

    public Optional<Parcel> heaviestParcel() {
        List<Parcel> all = parcels.findAllInRegistrationOrder();
        return all.isEmpty() ? Optional.empty()
                : Optional.of(DivideAndConquer.maximum(all, ParcelComparators.BY_WEIGHT_ASC));
    }

    private DispatchStrategy strategyFor(DispatchMode mode) {
        DispatchStrategy strategy = strategies.get(mode);
        if (strategy == null) {
            throw new IllegalArgumentException("No hay una estrategia configurada para " + mode);
        }
        return strategy;
    }

}
