package org.example.real_estate_agency;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppartmentData {
    private String category;
    private int average_price;

    public AppartmentData(String category, int average_price) {
        this.category = category;
        this.average_price = average_price;
    }

}

