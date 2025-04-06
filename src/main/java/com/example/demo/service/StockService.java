package com.example.demo.service;

import com.example.demo.model.StockItem;
import com.example.demo.model.StockMovement;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockService {

    private static final Map<Long, StockItem> stockItems = new HashMap<>();
    private static final Map<Long, StockMovement> stockMovements = new HashMap<>();
    private static Long stockItemIdCounter = 1L;
    private static Long stockMovementIdCounter = 1L;

    // Kategoriler
    private static final List<String> categories = Arrays.asList(
            "Elektronik", "Giyim", "Mobilya", "Gıda", "Kırtasiye", "Spor", "Kozmetik", "Oyuncak"
    );

    // Birimler
    private static final List<String> units = Arrays.asList(
            "Adet", "Kg", "Litre", "Metre", "Kutu", "Paket", "Çift"
    );

    // Stok hareket nedenleri
    private static final List<String> movementReasons = Arrays.asList(
            "Satış", "Satın Alma", "İade", "Hasar", "Sayım Fazlası", "Sayım Eksiği", "Transfer", "Diğer"
    );

    @PostConstruct
    public void init() {
        if (stockItems.isEmpty()) {
            generateSampleData();
        }
    }

    private void generateSampleData() {
        // Örnek stok kayıtları oluştur
        List<String> productNames = Arrays.asList(
                "Dizüstü Bilgisayar", "Akıllı Telefon", "Tablet", "Monitör", "Klavye", "Mouse",
                "T-Shirt", "Pantolon", "Ceket", "Ayakkabı", "Şapka", "Çorap",
                "Masa", "Sandalye", "Dolap", "Yatak", "Koltuk", "Kitaplık",
                "Çikolata", "Bisküvi", "Su", "Meyve Suyu", "Süt", "Ekmek",
                "Kalem", "Defter", "Silgi", "Boya Kalemi", "Cetvel", "Dosya"
        );

        Random random = new Random();

        // 30 örnek ürün oluştur
        for (int i = 0; i < 30; i++) {
            String name = productNames.get(i % productNames.size());
            String code = "STK" + String.format("%04d", i + 1);
            String category = categories.get(random.nextInt(categories.size()));
            String unit = units.get(random.nextInt(units.size()));
            double price = 10.0 + (random.nextDouble() * 990.0); // 10 ile 1000 arası
            price = Math.round(price * 100.0) / 100.0; // 2 decimal places

            StockItem item = new StockItem(
                    stockItemIdCounter++,
                    code,
                    name,
                    category,
                    unit,
                    price,
                    "Bu bir " + name + " ürünüdür."
            );
            
            // Rastgele oluşturulma tarihi (son 1 yıl içinde)
            int daysToSubtract = random.nextInt(365);
            item.setCreationDate(LocalDate.now().minusDays(daysToSubtract));
            
            // Her ürün için rastgele stok hareketleri oluştur (1-5 arası)
            int movementCount = 1 + random.nextInt(5);
            for (int j = 0; j < movementCount; j++) {
                String movementType = random.nextBoolean() ? "IN" : "OUT";
                int quantity = 1 + random.nextInt(100);
                
                // Eğer çıkış hareketi ve ilk hareket ise, giriş hareketine dönüştür
                if (movementType.equals("OUT") && j == 0) {
                    movementType = "IN";
                }
                
                // Eğer çıkış hareketi ve yeterli stok yoksa, miktarı azalt
                if (movementType.equals("OUT") && quantity > item.getCurrentStock()) {
                    quantity = Math.max(1, item.getCurrentStock() / 2);
                }
                
                StockMovement movement = new StockMovement(
                        stockMovementIdCounter++,
                        item.getId(),
                        item.getStockName(),
                        movementType,
                        quantity,
                        movementReasons.get(random.nextInt(movementReasons.size())),
                        "REF" + String.format("%06d", stockMovementIdCounter),
                        "Otomatik oluşturulan hareket."
                );
                
                // Hareketin tarihini rastgele ayarla (ürün oluşturulma tarihinden sonra)
                int movementDaysAfter = random.nextInt(daysToSubtract);
                movement.setMovementDate(
                        LocalDateTime.of(
                                LocalDate.now().minusDays(daysToSubtract - movementDaysAfter),
                                LocalDateTime.now().toLocalTime()
                        )
                );
                
                stockMovements.put(movement.getId(), movement);
                item.addMovement(movement);
            }
            
            stockItems.put(item.getId(), item);
        }
    }

    // Stok kalemleri için CRUD işlemleri
    public List<StockItem> findAllStockItems() {
        return new ArrayList<>(stockItems.values());
    }

    public StockItem findStockItemById(Long id) {
        return stockItems.get(id);
    }

    public StockItem saveStockItem(StockItem stockItem) {
        if (stockItem.getId() == null) {
            stockItem.setId(stockItemIdCounter++);
            stockItem.setCreationDate(LocalDate.now());
        }
        stockItems.put(stockItem.getId(), stockItem);
        return stockItem;
    }

    public void deleteStockItemById(Long id) {
        stockItems.remove(id);
    }

    // Stok hareketleri için işlemler
    public List<StockMovement> findAllStockMovements() {
        return new ArrayList<>(stockMovements.values());
    }

    public List<StockMovement> findStockMovementsByItemId(Long itemId) {
        return stockMovements.values().stream()
                .filter(movement -> movement.getStockItemId().equals(itemId))
                .collect(Collectors.toList());
    }

    public StockMovement findStockMovementById(Long id) {
        return stockMovements.get(id);
    }

    public StockMovement saveStockMovement(StockMovement movement) {
        if (movement.getId() == null) {
            movement.setId(stockMovementIdCounter++);
            movement.setMovementDate(LocalDateTime.now());
        }
        
        StockItem stockItem = stockItems.get(movement.getStockItemId());
        if (stockItem != null) {
            // Hareket tipi OUT ise, yeterli stok kontrolü yap
            if (movement.getMovementType().equals("OUT")) {
                int existingStock = stockItem.getCurrentStock();
                
                // Eğer yeni hareket ise direkt kontrol et
                if (stockMovements.get(movement.getId()) == null) {
                    if (movement.getQuantity() > existingStock) {
                        throw new IllegalArgumentException("Yetersiz stok miktarı. Mevcut: " + existingStock);
                    }
                } else {
                    // Güncelleme durumunda, eski hareketi bulup farkı hesapla
                    StockMovement existingMovement = stockMovements.get(movement.getId());
                    int oldQuantity = existingMovement.getQuantity();
                    String oldType = existingMovement.getMovementType();
                    
                    if (oldType.equals("OUT") && movement.getMovementType().equals("OUT")) {
                        // Sadece fark kadar stok kontrolü
                        int diff = movement.getQuantity() - oldQuantity;
                        if (diff > 0 && diff > existingStock) {
                            throw new IllegalArgumentException("Yetersiz stok miktarı. Mevcut: " + existingStock);
                        }
                    } else if (oldType.equals("IN") && movement.getMovementType().equals("OUT")) {
                        // IN'den OUT'a dönüşürse
                        int totalEffect = oldQuantity + movement.getQuantity();
                        if (totalEffect > existingStock) {
                            throw new IllegalArgumentException("Yetersiz stok miktarı. Mevcut: " + existingStock);
                        }
                    }
                }
            }
            
            // Eski hareketi temizle (güncelleme durumu)
            if (stockMovements.containsKey(movement.getId())) {
                StockMovement oldMovement = stockMovements.get(movement.getId());
                // Eski hareketi stok kaleminden kaldır
                List<StockMovement> movements = stockItem.getMovements();
                movements.removeIf(m -> m.getId().equals(oldMovement.getId()));
                
                // Eski hareketin etkisini geri al
                if (oldMovement.getMovementType().equals("IN")) {
                    stockItem.setCurrentStock(stockItem.getCurrentStock() - oldMovement.getQuantity());
                } else if (oldMovement.getMovementType().equals("OUT")) {
                    stockItem.setCurrentStock(stockItem.getCurrentStock() + oldMovement.getQuantity());
                }
            }
            
            // Stok hareketini ekle
            stockMovements.put(movement.getId(), movement);
            movement.setStockItemName(stockItem.getStockName());
            
            // Stok miktarını güncelle
            if (movement.getMovementType().equals("IN")) {
                stockItem.setCurrentStock(stockItem.getCurrentStock() + movement.getQuantity());
            } else if (movement.getMovementType().equals("OUT")) {
                stockItem.setCurrentStock(stockItem.getCurrentStock() - movement.getQuantity());
            }
            
            // Stok kalemine hareketi ekle
            stockItem.getMovements().add(movement);
        }
        
        return movement;
    }

    public void deleteStockMovementById(Long id) {
        StockMovement movement = stockMovements.get(id);
        if (movement != null) {
            StockItem stockItem = stockItems.get(movement.getStockItemId());
            if (stockItem != null) {
                // Stok miktarını geri al
                if (movement.getMovementType().equals("IN")) {
                    stockItem.setCurrentStock(stockItem.getCurrentStock() - movement.getQuantity());
                } else if (movement.getMovementType().equals("OUT")) {
                    stockItem.setCurrentStock(stockItem.getCurrentStock() + movement.getQuantity());
                }
                
                // Stok kaleminden hareketi kaldır
                stockItem.getMovements().removeIf(m -> m.getId().equals(id));
            }
            
            stockMovements.remove(id);
        }
    }

    // Yardımcı metotlar
    public List<String> getAllCategories() {
        return categories;
    }

    public List<String> getAllUnits() {
        return units;
    }

    public List<String> getAllMovementReasons() {
        return movementReasons;
    }
    
    // Pagination için metotlar
    public List<StockItem> findPaginatedStockItems(int page, int size) {
        List<StockItem> all = findAllStockItems();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, all.size());
        
        if (fromIndex >= all.size()) {
            return new ArrayList<>();
        }
        
        return all.subList(fromIndex, toIndex);
    }
    
    public int getTotalStockItems() {
        return stockItems.size();
    }
    
    public List<StockMovement> findPaginatedStockMovements(int page, int size) {
        List<StockMovement> all = findAllStockMovements();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, all.size());
        
        if (fromIndex >= all.size()) {
            return new ArrayList<>();
        }
        
        return all.subList(fromIndex, toIndex);
    }
    
    public int getTotalStockMovements() {
        return stockMovements.size();
    }
}
