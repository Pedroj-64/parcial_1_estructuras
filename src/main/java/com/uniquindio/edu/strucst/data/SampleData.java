package com.uniquindio.edu.strucst.data;

import java.util.List;

import com.uniquindio.edu.strucst.facade.DistributionCenter;
import com.uniquindio.edu.strucst.model.Courier;
import com.uniquindio.edu.strucst.model.Parcel;

public class SampleData {

    private SampleData() {
    }

    public static List<Parcel> parcels() {
        return List.of(
                new Parcel("PQ731", "Armenia", 4.5, 5, 45),
                new Parcel("PQ105", "Salento", 12.0, 5, 25),
                new Parcel("PQ942", "Armenia", 3.2, 4, 20),
                new Parcel("PQ318", "Calarcá", 18.7, 3, 35),
                new Parcel("PQ567", "Salento", 7.4, 2, 50),
                new Parcel("PQ829", "Armenia", 5.1, 1, 15),
                new Parcel("PQ420", "Montenegro", 2.8, 3, 30),
                new Parcel("PQ214", "Quimbaya", 9.6, 4, 55),
                new Parcel("PQ057", "Circasia", 1.2, 5, 40),
                new Parcel("PQ688", "La Tebaida", 6.3, 2, 28),
                new Parcel("PQ903", "Armenia", 14.9, 5, 18),
                new Parcel("PQ146", "Calarcá", 0.8, 1, 22),
                new Parcel("PQ372", "Montenegro", 11.4, 4, 60),
                new Parcel("PQ815", "Salento", 3.9, 3, 70),
                new Parcel("PQ269", "Quimbaya", 8.2, 5, 33),
                new Parcel("PQ594", "Circasia", 2.1, 2, 26),
                new Parcel("PQ421", "La Tebaida", 16.5, 4, 38),
                new Parcel("PQ733", "Armenia", 7.7, 3, 12),
                new Parcel("PQ088", "Calarcá", 5.5, 5, 48),
                new Parcel("PQ642", "Montenegro", 4.0, 1, 42),
                new Parcel("PQ999", "Quimbaya", 10.3, 2, 65),
                new Parcel("PQ310", "Armenia", 2.4, 4, 16),
                new Parcel("PQ476", "Circasia", 13.1, 3, 52),
                new Parcel("PQ842", "Salento", 6.8, 5, 58),
                new Parcel("PQ123", "La Tebaida", 3.3, 1, 24),
                new Parcel("PQ760", "Calarcá", 9.0, 4, 36),
                new Parcel("PQ555", "Armenia", 1.9, 2, 14),
                new Parcel("PQ284", "Montenegro", 15.2, 5, 44),
                new Parcel("PQ637", "Quimbaya", 4.4, 3, 47),
                new Parcel("PQ901", "Circasia", 8.8, 4, 31));
    }

    public static List<Courier> couriers() {
        return List.of(
                new Courier("R-1001", "Laura Gómez", "Armenia"),
                new Courier("R-1002", "Brandon", "Armenia"),
                new Courier("R-1003", "Padilla", "Salento"),
                new Courier("R-1004", "Carlos Henao", "Calarcá"),
                new Courier("R-1005", "Juliana Marín", "Montenegro"),
                new Courier("R-1006", "Sebastián Arango", "Quimbaya"),
                new Courier("R-1007", "Daniela Cardona", "Circasia"),
                new Courier("R-1008", "Felipe Londoño", "La Tebaida", false));
    }

    public static DistributionCenter loadInto(DistributionCenter center) {
        couriers().forEach(center::registerCourier);
        parcels().forEach(center::registerParcel);
        return center;
    }
}
