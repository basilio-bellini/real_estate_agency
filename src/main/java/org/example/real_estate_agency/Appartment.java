package org.example.real_estate_agency;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Entity
public class Appartment {
    private int ID;
    @Getter
    private String category;
    @Getter
    private BigDecimal size;
    @Getter
    private String floor_number;
    @Getter
    private String address;
    @Getter
    private String metro_station;
    @Getter
    private String description;
    @Getter
    private int price;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "Appartment[ID=" + ID + ", Category=" + category + ", Size=" + size + ", Floor_number=" + floor_number + ", Address=" + address +
                ", Metro_station=" + metro_station + ", Description=" + description + ", Price=" + price + "]";
    }
}
