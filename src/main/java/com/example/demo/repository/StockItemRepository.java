package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.StockItem;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    // Özel sorgular burada tanımlanabilir
    StockItem findByStockCode(String stockCode);
    
    // Stok kodu veya adına göre arama yapan metot
    @Query("SELECT i FROM StockItem i WHERE " +
           "LOWER(i.stockCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.stockName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<StockItem> searchByCodeOrName(@Param("searchTerm") String searchTerm, Pageable pageable);
}
