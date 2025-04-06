package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "stock_movements")
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Bu alanları kaldırıyoruz çünkü ManyToOne ilişkisi kullanacağız
    // private Long stockItemId;
    // private String stockItemName; // For display
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_item_id", nullable = false)
    private StockItem stockItem;
    @Column(nullable = false)
    private String movementType; // IN, OUT
    private int quantity;
    private String reason;
    private LocalDateTime movementDate;
    private String referenceNo;
    private String notes;

    public StockMovement() {
        this.movementDate = LocalDateTime.now();
    }

    public StockMovement(Long id, StockItem stockItem, String movementType, int quantity, String reason, String referenceNo, String notes) {
        this.id = id;
        this.stockItem = stockItem;
        this.movementType = movementType;
        this.quantity = quantity;
        this.reason = reason;
        this.referenceNo = referenceNo;
        this.notes = notes;
        this.movementDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }
    
    // Uyumluluk için yardımcı metotlar
    public Long getStockItemId() {
        return stockItem != null ? stockItem.getId() : null;
    }
    
    public String getStockItemName() {
        return stockItem != null ? stockItem.getStockName() : null;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", stockItemId=" + getStockItemId() +
                ", movementType='" + movementType + '\'' +
                ", quantity=" + quantity +
                ", movementDate=" + movementDate +
                '}';
    }
}
