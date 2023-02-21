package com.myInventory.dao;


import com.myInventory.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findByManufacturerIgnoreCaseContainingOrModelIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String manufacturer, String model, String description, Pageable pageable);
}
