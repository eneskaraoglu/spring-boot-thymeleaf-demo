package com.example.demo.model;

import java.time.LocalDateTime;

public class StockMovement {
    private Long id;
    private Long stockItemId;
    private String stockItemName; // For display
    private String movementType; // IN, OUT
    private int quantity;
    private String reason;
    private LocalDateTime movementDate;
    private String referenceNo;
    private String notes;

    public StockMovement() {
        this.movementDate = LocalDateTime.now();
    }

    public StockMovement(Long id, Long stockItemId, String stockItemName, String movementType, int quantity, String reason, String referenceNo, String notes) {
        this.id = id;
        this.stockItemId = stockItemId;
        this.stockItemName = stockItemName;
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

    public Long getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(Long stockItemId) {
        this.stockItemId = stockItemId;
    }

    public String getStockItemName() {
        return stockItemName;
    }

    public void setStockItemName(String stockItemName) {
        this.stockItemName = stockItemName;
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
                ", stockItemId=" + stockItemId +
                ", movementType='" + movementType + '\'' +
                ", quantity=" + quantity +
                ", movementDate=" + movementDate +
                '}';
    }
}
