package com.quindioexpress.model.comparator;

import com.quindioexpress.model.Parcel;

import java.util.Comparator;

/**
 * Catálogo de criterios de ordenamiento alternativos al orden natural de {@link Parcel}.
 *
 * <p>Cada criterio es un {@link Comparator} reutilizable: sirve para ordenar listas,
 * construir colas de prioridad o decidir qué paquete tiene precedencia sobre otro
 * (por ejemplo, en el algoritmo divide y vencerás). Usar estos comparadores nunca
 * modifica el orden natural (por código) del paquete.</p>
 */
public final class ParcelComparators {

    /** Prioridad de mayor a menor. */
    public static final Comparator<Parcel> BY_PRIORITY_DESC =
            Comparator.comparingInt(Parcel::getPriority).reversed();

    /** Peso de mayor a menor. */
    public static final Comparator<Parcel> BY_WEIGHT_DESC =
            Comparator.comparingDouble(Parcel::getWeight).reversed();

    /** Peso de menor a mayor (útil para buscar el máximo con divide y vencerás). */
    public static final Comparator<Parcel> BY_WEIGHT_ASC =
            Comparator.comparingDouble(Parcel::getWeight);

    /** Tiempo estimado de menor a mayor. */
    public static final Comparator<Parcel> BY_ESTIMATED_TIME_ASC =
            Comparator.comparingInt(Parcel::getEstimatedMinutes);

    /** Orden natural (código ascendente), expuesto como comparador por conveniencia. */
    public static final Comparator<Parcel> BY_CODE = Comparator.naturalOrder();

    /**
     * Regla de despacho prioritario del enunciado:
     * <ol>
     *   <li>mayor prioridad primero;</li>
     *   <li>a igual prioridad, menor tiempo estimado primero;</li>
     *   <li>si persiste el empate, código alfabéticamente menor primero.</li>
     * </ol>
     * Se construye componiendo los criterios anteriores, sin duplicar lógica.
     */
    public static final Comparator<Parcel> DISPATCH_ORDER =
            BY_PRIORITY_DESC.thenComparing(BY_ESTIMATED_TIME_ASC).thenComparing(BY_CODE);

    private ParcelComparators() {
    }
}
