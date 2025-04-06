package com.example.demo.service;

import com.example.demo.model.StockItem;
import com.example.demo.model.StockMovement;
import com.example.demo.repository.StockItemRepository;
import com.example.demo.repository.StockMovementRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockService {

    @Autowired
    private StockItemRepository stockItemRepository;
    
    @Autowired
    private StockMovementRepository stockMovementRepository;
    
    // Örnek veri oluşturmak için kullanılacak listeler (veritabanı kullanımında da işe yarar)

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
        if (stockItemRepository.count() == 0) {
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

            StockItem item = new StockItem();
            item.setStockCode(code);
            item.setStockName(name);
            item.setCategory(category);
            item.setUnit(unit);
            item.setUnitPrice(price);
            item.setDescription("Bu bir " + name + " ürünüdür.");
            item.setCurrentStock(0);
            
            // Rastgele oluşturulma tarihi (son 1 yıl içinde)
            int daysToSubtract = random.nextInt(365);
            item.setCreationDate(LocalDate.now().minusDays(daysToSubtract));
            
            // Stok kalemini kaydet
            item = stockItemRepository.save(item);
            
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
                
                StockMovement movement = new StockMovement();
                movement.setStockItem(item);
                movement.setMovementType(movementType);
                movement.setQuantity(quantity);
                movement.setReason(movementReasons.get(random.nextInt(movementReasons.size())));
                movement.setReferenceNo("REF" + String.format("%06d", i * 10 + j));
                movement.setNotes("Otomatik oluşturulan hareket.");
                
                // Hareketin tarihini rastgele ayarla (ürün oluşturulma tarihinden sonra)
                int movementDaysAfter = random.nextInt(daysToSubtract);
                movement.setMovementDate(
                        LocalDateTime.of(
                                LocalDate.now().minusDays(daysToSubtract - movementDaysAfter),
                                LocalDateTime.now().toLocalTime()
                        )
                );
                
                // Stok miktarını güncelle
                if (movementType.equals("IN")) {
                    item.setCurrentStock(item.getCurrentStock() + quantity);
                } else if (movementType.equals("OUT")) {
                    item.setCurrentStock(item.getCurrentStock() - quantity);
                }
                
                // Stok hareketini kaydet
                stockMovementRepository.save(movement);
            }
            
            // Güncellenmiş stok miktarıyla stok kalemini kaydet
            stockItemRepository.save(item);
        }
    }

    // Stok kalemleri için CRUD işlemleri
    public List<StockItem> findAllStockItems() {
        return stockItemRepository.findAll();
    }

    public StockItem findStockItemById(Long id) {
        return stockItemRepository.findById(id).orElse(null);
    }

    public StockItem saveStockItem(StockItem stockItem) {
        if (stockItem.getId() == null) {
            stockItem.setCreationDate(LocalDate.now());
        }
        return stockItemRepository.save(stockItem);
    }

    public void deleteStockItemById(Long id) {
        stockItemRepository.deleteById(id);
    }

    // Stok hareketleri için işlemler
    public List<StockMovement> findAllStockMovements() {
        return stockMovementRepository.findAll();
    }

    public List<StockMovement> findStockMovementsByItemId(Long itemId) {
        return stockMovementRepository.findByStockItem_Id(itemId);
    }

    public StockMovement findStockMovementById(Long id) {
        return stockMovementRepository.findById(id).orElse(null);
    }

    public StockMovement saveStockMovement(StockMovement movement) {
        if (movement.getId() == null) {
            movement.setMovementDate(LocalDateTime.now());
        }
        
        StockItem stockItem = movement.getStockItem();
        if (stockItem != null) {
            // Hareket tipi OUT ise, yeterli stok kontrolü yap
            if (movement.getMovementType().equals("OUT")) {
                int existingStock = stockItem.getCurrentStock();
                
                // Eğer yeni hareket ise direkt kontrol et
                if (movement.getId() == null) {
                    if (movement.getQuantity() > existingStock) {
                        throw new IllegalArgumentException("Yetersiz stok miktarı. Mevcut: " + existingStock);
                    }
                } else {
                    // Güncelleme durumunda, eski hareketi bulup farkı hesapla
                    StockMovement existingMovement = stockMovementRepository.findById(movement.getId()).orElse(null);
                    if (existingMovement != null) {
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
                        
                        // Eski hareketin etkisini geri al
                        if (oldType.equals("IN")) {
                            stockItem.setCurrentStock(stockItem.getCurrentStock() - oldQuantity);
                        } else if (oldType.equals("OUT")) {
                            stockItem.setCurrentStock(stockItem.getCurrentStock() + oldQuantity);
                        }
                    }
                }
            }
            
            // Stok miktarını güncelle
            if (movement.getMovementType().equals("IN")) {
                stockItem.setCurrentStock(stockItem.getCurrentStock() + movement.getQuantity());
            } else if (movement.getMovementType().equals("OUT")) {
                stockItem.setCurrentStock(stockItem.getCurrentStock() - movement.getQuantity());
            }
            
            // Stok kalemini kaydet
            stockItemRepository.save(stockItem);
        }
        
        // Hareketi kaydet
        return stockMovementRepository.save(movement);
    }

    public void deleteStockMovementById(Long id) {
        StockMovement movement = stockMovementRepository.findById(id).orElse(null);
        if (movement != null) {
            StockItem stockItem = movement.getStockItem();
            if (stockItem != null) {
                // Stok miktarını geri al
                if (movement.getMovementType().equals("IN")) {
                    stockItem.setCurrentStock(stockItem.getCurrentStock() - movement.getQuantity());
                } else if (movement.getMovementType().equals("OUT")) {
                    stockItem.setCurrentStock(stockItem.getCurrentStock() + movement.getQuantity());
                }
                
                // Stok kalemini güncelle
                stockItemRepository.save(stockItem);
            }
            
            // İlişkisel bütünlüğü korumak için hareketi veritabanından sil
            stockMovementRepository.deleteById(id);
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
    public Page<StockItem> findPaginatedStockItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return stockItemRepository.findAll(pageable);
    }
    
    public long getTotalStockItems() {
        return stockItemRepository.count();
    }
    
    public Page<StockMovement> findPaginatedStockMovements(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return stockMovementRepository.findAll(pageable);
    }
    
    public long getTotalStockMovements() {
        return stockMovementRepository.count();
    }
    
    // Stok kalemlerini arama
    public Page<StockItem> searchStockItems(String searchTerm, int page, int size) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findPaginatedStockItems(page, size);
        }
        Pageable pageable = PageRequest.of(page, size);
        return stockItemRepository.searchByCodeOrName(searchTerm.trim(), pageable);
    }
}
