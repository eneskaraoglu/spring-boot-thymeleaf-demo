package com.example.demo.controller;

import com.example.demo.model.StockItem;
import com.example.demo.model.StockMovement;
import com.example.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
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
                                Model model) {
        List<StockItem> stockItems = stockService.findPaginatedStockItems(page, size);
        int totalItems = stockService.getTotalStockItems();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        
        model.addAttribute("stockItems", stockItems);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("pageTitle", "Stok Tanımlama");
        
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
                                     Model model) {
        List<StockMovement> stockMovements = stockService.findPaginatedStockMovements(page, size);
        int totalItems = stockService.getTotalStockMovements();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        
        model.addAttribute("stockMovements", stockMovements);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("pageTitle", "Stok Hareketleri");
        
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
        StockItem stockItem = stockService.findStockItemById(stockMovement.getStockItemId());
        
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
                            Model model) {
        List<StockItem> stockItems = stockService.findPaginatedStockItems(page, size);
        int totalItems = stockService.getTotalStockItems();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        
        model.addAttribute("stockItems", stockItems);
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
