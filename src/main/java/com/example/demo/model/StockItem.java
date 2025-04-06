package com.example.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "stock_items")
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String stockCode;
    @Column(nullable = false)
    private String stockName;
    private String category;
    private String unit;
    private Double unitPrice;
    private LocalDate creationDate;
    private String description;
    private boolean active;
    @OneToMany(mappedBy = "stockItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockMovement> movements;
    private int currentStock;

    public StockItem() {
        this.movements = new ArrayList<>();
        this.creationDate = LocalDate.now();
        this.active = true;
        this.currentStock = 0;
    }

    public StockItem(Long id, String stockCode, String stockName, String category, String unit, Double unitPrice, String description) {
        this.id = id;
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.category = category;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.description = description;
        this.creationDate = LocalDate.now();
        this.active = true;
        this.movements = new ArrayList<>();
        this.currentStock = 0;
    }

    // Stok hareket ekleme
    public void addMovement(StockMovement movement) {
        this.movements.add(movement);
        // Stok miktarını güncelle
        if (movement.getMovementType().equals("IN")) {
            this.currentStock += movement.getQuantity();
        } else if (movement.getMovementType().equals("OUT")) {
            this.currentStock -= movement.getQuantity();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<StockMovement> getMovements() {
        return movements;
    }

    public void setMovements(List<StockMovement> movements) {
        this.movements = movements;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    @Override
    public String toString() {
        return "StockItem{" +
                "id=" + id +
                ", stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                ", category='" + category + '\'' +
                ", unit='" + unit + '\'' +
                ", unitPrice=" + unitPrice +
                ", currentStock=" + currentStock +
                '}';
    }
}
