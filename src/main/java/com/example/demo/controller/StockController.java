package com.example.demo.controller;

import com.example.demo.model.StockItem;
import com.example.demo.model.StockMovement;
import com.example.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Stok Tanımlama
    @GetMapping("/items")
    public String listStockItems(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) String search,
                                @RequestParam(required = false) String stockCode,
                                @RequestParam(required = false) String stockName,
                                @RequestParam(required = false) String searchCategory,
                                @RequestParam(required = false) String searchActive,
                                Model model) {
        Page<StockItem> stockItemPage;
        
        // Eski genel arama parametresi (geriye dönük uyumluluk için)
        if (search != null && !search.trim().isEmpty()) {
            stockItemPage = stockService.searchStockItems(search, page, size);
            model.addAttribute("search", search);
        } 
        // Yeni detaylı arama parametreleri
        else if ((stockCode != null && !stockCode.trim().isEmpty()) || 
                 (stockName != null && !stockName.trim().isEmpty()) ||
                 (searchCategory != null && !searchCategory.trim().isEmpty()) ||
                 (searchActive != null && !searchActive.trim().isEmpty())) {
            stockItemPage = stockService.searchStockItemsByCodeAndName(stockCode, stockName, searchCategory, searchActive, page, size);
            model.addAttribute("stockCode", stockCode);
            model.addAttribute("stockName", stockName);
            model.addAttribute("searchCategory", searchCategory);
            model.addAttribute("searchActive", searchActive);
        } else {
            stockItemPage = stockService.findPaginatedStockItems(page, size);
        }
        
        long totalItems = stockService.getTotalStockItems();
        int totalPages = stockItemPage.getTotalPages();
        
        model.addAttribute("stockItems", stockItemPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("pageTitle", "Stok Tanımlama");
        model.addAttribute("categories", stockService.getAllCategories());
        model.addAttribute("searchCategory", searchCategory);  // Kategori filtresini modele ekle
        model.addAttribute("searchActive", searchActive);      // Durum filtresini modele ekle
        
        return "stock/items";
    }

    @GetMapping("/items/new")
    public String createStockItemForm(Model model) {
        StockItem stockItem = new StockItem();
        stockItem.setCreationDate(LocalDate.now());
        
        model.addAttribute("stockItem", stockItem);
        model.addAttribute("categories", stockService.getAllCategories());
        model.addAttribute("units", stockService.getAllUnits());
        model.addAttribute("pageTitle", "Yeni Stok Kalemi");
        model.addAttribute("isNew", true);
        
        return "stock/item-form";
    }

    @PostMapping("/items")
    public String saveStockItem(@ModelAttribute StockItem stockItem, RedirectAttributes redirectAttributes) {
        try {
            stockService.saveStockItem(stockItem);
            redirectAttributes.addFlashAttribute("successMessage", "Stok kalemi başarıyla kaydedildi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stok kalemi kaydedilemedi: " + e.getMessage());
        }
        return "redirect:/stock/items";
    }

    @GetMapping("/items/{id}")
    public String viewStockItem(@PathVariable Long id, Model model) {
        StockItem stockItem = stockService.findStockItemById(id);
        model.addAttribute("stockItem", stockItem);
        model.addAttribute("pageTitle", "Stok Detayı: " + stockItem.getStockName());
        
        return "stock/item-view";
    }

    @GetMapping("/items/{id}/edit")
    public String editStockItemForm(@PathVariable Long id, Model model) {
        StockItem stockItem = stockService.findStockItemById(id);
        
        model.addAttribute("stockItem", stockItem);
        model.addAttribute("categories", stockService.getAllCategories());
        model.addAttribute("units", stockService.getAllUnits());
        model.addAttribute("pageTitle", "Stok Düzenle: " + stockItem.getStockName());
        model.addAttribute("isNew", false);
        
        return "stock/item-form";
    }

    @GetMapping("/items/{id}/delete")
    public String deleteStockItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            stockService.deleteStockItemById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Stok kalemi başarıyla silindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stok kalemi silinemedi: " + e.getMessage());
        }
        return "redirect:/stock/items";
    }

    // Stok Hareketleri
    @GetMapping("/movements")
    public String listStockMovements(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) Long stockItemId,
                                     @RequestParam(required = false) String movementType,
                                     @RequestParam(required = false) String reason,
                                     @RequestParam(required = false) String referenceNo,
                                     @RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate,
                                     Model model) {
        Page<StockMovement> stockMovementPage;
        
        // Filtreleme parametreleri varsa
        if (stockItemId != null || 
            (movementType != null && !movementType.trim().isEmpty()) ||
            (reason != null && !reason.trim().isEmpty()) ||
            (referenceNo != null && !referenceNo.trim().isEmpty()) ||
            (startDate != null && !startDate.trim().isEmpty()) ||
            (endDate != null && !endDate.trim().isEmpty())) {
            
            stockMovementPage = stockService.searchStockMovements(
                stockItemId, movementType, reason, referenceNo, startDate, endDate, page, size);
                
            // Model'e arama parametrelerini ekle
            model.addAttribute("stockItemId", stockItemId);
            model.addAttribute("movementType", movementType);
            model.addAttribute("reason", reason);
            model.addAttribute("referenceNo", referenceNo);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } else {
            stockMovementPage = stockService.findPaginatedStockMovements(page, size);
        }
        
        long totalItems = stockService.getTotalStockMovements();
        int totalPages = stockMovementPage.getTotalPages();
        
        model.addAttribute("stockMovements", stockMovementPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("pageTitle", "Stok Hareketleri");
        model.addAttribute("stockItems", stockService.findAllStockItems()); // Filtre için stok kalemlerini ekle
        model.addAttribute("movementReasons", stockService.getAllMovementReasons()); // Filtre için hareket nedenlerini ekle
        
        return "stock/movements";
    }

    @GetMapping("/movements/new")
    public String createStockMovementForm(Model model) {
        StockMovement stockMovement = new StockMovement();
        
        model.addAttribute("stockMovement", stockMovement);
        model.addAttribute("stockItems", stockService.findAllStockItems());
        model.addAttribute("movementReasons", stockService.getAllMovementReasons());
        model.addAttribute("pageTitle", "Yeni Stok Hareketi");
        model.addAttribute("isNew", true);
        
        return "stock/movement-form";
    }

    @PostMapping("/movements")
    public String saveStockMovement(@ModelAttribute StockMovement stockMovement, RedirectAttributes redirectAttributes) {
        try {
            stockService.saveStockMovement(stockMovement);
            redirectAttributes.addFlashAttribute("successMessage", "Stok hareketi başarıyla kaydedildi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stok hareketi kaydedilemedi: " + e.getMessage());
        }
        return "redirect:/stock/movements";
    }

    @GetMapping("/movements/{id}")
    public String viewStockMovement(@PathVariable Long id, Model model) {
        StockMovement stockMovement = stockService.findStockMovementById(id);
        StockItem stockItem = stockMovement.getStockItem();
        
        model.addAttribute("stockMovement", stockMovement);
        model.addAttribute("stockItem", stockItem);
        model.addAttribute("pageTitle", "Stok Hareketi Detayı");
        
        return "stock/movement-view";
    }

    @GetMapping("/movements/{id}/edit")
    public String editStockMovementForm(@PathVariable Long id, Model model) {
        StockMovement stockMovement = stockService.findStockMovementById(id);
        
        model.addAttribute("stockMovement", stockMovement);
        model.addAttribute("stockItems", stockService.findAllStockItems());
        model.addAttribute("movementReasons", stockService.getAllMovementReasons());
        model.addAttribute("pageTitle", "Stok Hareketi Düzenle");
        model.addAttribute("isNew", false);
        
        return "stock/movement-form";
    }

    @GetMapping("/movements/{id}/delete")
    public String deleteStockMovement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            stockService.deleteStockMovementById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Stok hareketi başarıyla silindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Stok hareketi silinemedi: " + e.getMessage());
        }
        return "redirect:/stock/movements";
    }

    // Stok Takip
    @GetMapping("/track")
    public String trackStock(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String category,
                            @RequestParam(required = false) String search,
                            Model model) {
        Page<StockItem> stockItemPage;
        
        if (search != null && !search.trim().isEmpty()) {
            // Eğer arama parametresi varsa, stok kodu ve adına göre ara
            stockItemPage = stockService.searchStockItems(search, page, size);
            model.addAttribute("search", search);
        } else if (category != null && !category.trim().isEmpty()) {
            // Eğer kategori parametresi varsa, kategoriye göre ara
            stockItemPage = stockService.searchStockItemsByCodeAndName(null, null, category, null, page, size);
            model.addAttribute("selectedCategory", category);
        } else {
            // Herhangi bir filtre yoksa tüm kayıtları göster
            stockItemPage = stockService.findPaginatedStockItems(page, size);
        }
        
        long totalItems = stockService.getTotalStockItems();
        int totalPages = stockItemPage.getTotalPages();
        
        model.addAttribute("stockItems", stockItemPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("categories", stockService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("pageTitle", "Stok Takip");
        
        return "stock/track";
    }

    @GetMapping("/track/{id}")
    public String trackStockDetails(@PathVariable Long id, Model model) {
        StockItem stockItem = stockService.findStockItemById(id);
        List<StockMovement> movements = stockService.findStockMovementsByItemId(id);
        
        model.addAttribute("stockItem", stockItem);
        model.addAttribute("movements", movements);
        model.addAttribute("pageTitle", "Stok Detayı: " + stockItem.getStockName());
        
        return "stock/track-details";
    }
}
