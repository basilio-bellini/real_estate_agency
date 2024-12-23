package org.example.real_estate_agency;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AppartmentService {
    private final AppartmentRepository repo;

    public List<Object[]> getAppartmentAvgPriceGroupedByCategory() {
        return repo.getAppartmentAvgPriceGroupedByCategory();
    }

    public AppartmentService(AppartmentRepository repo) {
        this.repo = repo;
    }

    public List<Appartment> listAll(String keyword) {
        if (keyword != null) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }

    public void save (Appartment appartment){
        repo.save(appartment);
    }

    public Appartment get (Long id){
        return repo.findById(id).get();
    }

    public void delete (Long id){
        repo.deleteById(id);
    }
}
