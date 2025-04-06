package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.StockMovement;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    // Özel sorgular burada tanımlanabilir
    List<StockMovement> findByStockItem_Id(Long stockItemId);
}
