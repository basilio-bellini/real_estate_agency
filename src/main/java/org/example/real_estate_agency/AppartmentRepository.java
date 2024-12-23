package org.example.real_estate_agency;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppartmentRepository extends JpaRepository<Appartment, Long> {
    @Query("SELECT p FROM Appartment p WHERE CONCAT(" +
            "p.category, ' ', p.size, ' ', p.floor_number, ' ', p.address, ' '," +
            " p.metro_station, ' ', p.description, ' ', p.price) LIKE %?1%"
    )
    List<Appartment> search(String keyword);

    @Query("SELECT e.category, AVG(e.price) FROM Appartment e GROUP BY e.category")
    List<Object[]> getAppartmentAvgPriceGroupedByCategory();
}
