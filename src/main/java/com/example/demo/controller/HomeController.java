package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Thymeleaf Demo");
        model.addAttribute("message", "Thymeleaf Demo Projesi'ne Hoş Geldiniz!");
        return "index";
    }
    
    @GetMapping("/basic-examples")
    public String basicExamples(Model model) {
        model.addAttribute("pageTitle", "Temel Thymeleaf Örnekleri");
        model.addAttribute("username", "Demo Kullanıcı");
        model.addAttribute("isAdmin", true);
        model.addAttribute("currentDate", new java.util.Date());
        return "basic-examples";
    }
    
    @GetMapping("/form-demo")
    public String formDemo(Model model) {
        model.addAttribute("pageTitle", "Form Örneği");
        return "form-demo";
    }
    
    @GetMapping("/layout-demo")
    public String layoutDemo(Model model) {
        model.addAttribute("pageTitle", "Layout Örneği");
        return "layout-demo";
    }
    
    @GetMapping("/analytics-dashboard")
    public String analyticsDashboard(Model model) {
        model.addAttribute("pageTitle", "Analitik Dashboard");
        
        // Kullanıcı istatistikleri için örnek veriler
        model.addAttribute("totalUsers", 8459);
        model.addAttribute("newUsers", 385);
        model.addAttribute("activeUsers", 6271);
        model.addAttribute("conversionRate", 3.2);
        
        // Aylık aktif kullanıcı grafiği için veriler
        List<Integer> monthlyActiveUsers = Arrays.asList(4250, 4380, 5120, 5430, 5890, 6150, 6271);
        model.addAttribute("monthlyActiveUsers", monthlyActiveUsers);
        
        // Mobil cihaz kullanımı verisi
        Map<String, Double> deviceUsage = new HashMap<>();
        deviceUsage.put("Desktop", 62.5);
        deviceUsage.put("Mobile", 29.3);
        deviceUsage.put("Tablet", 8.2);
        model.addAttribute("deviceUsage", deviceUsage);
        
        // Son kullanıcı aktiviteleri (log)
        model.addAttribute("recentActivities", generateRecentActivities());
        
        return "dashboards/analytics";
    }
    
    @GetMapping("/sales-dashboard")
    public String salesDashboard(Model model) {
        model.addAttribute("pageTitle", "Satış Dashboard");
        
        // Satış istatistikleri için örnek veriler
        model.addAttribute("totalSales", 538750);
        model.addAttribute("monthlyRevenue", 127650);
        model.addAttribute("averageOrderValue", 345.80);
        model.addAttribute("conversionRate", 4.7);
        
        // Aylık satış verileri
        List<Integer> monthlySales = Arrays.asList(85400, 92700, 88300, 107500, 114200, 120600, 127650);
        model.addAttribute("monthlySales", monthlySales);
        
        // Ürün kategorisi dağılımı
        Map<String, Double> categorySales = new HashMap<>();
        categorySales.put("Elektronik", 42.8);
        categorySales.put("Giyim", 28.3);
        categorySales.put("Ev & Yaşam", 17.5);
        categorySales.put("Spor", 11.4);
        model.addAttribute("categorySales", categorySales);
        
        // Son satışlar
        model.addAttribute("recentSales", generateRecentSales());
        
        return "dashboards/sales";
    }
    
    // Örnek aktiviteler üretmek için yardımcı metod
    private List<Map<String, String>> generateRecentActivities() {
        List<Map<String, String>> activities = Arrays.asList(
            createActivity("Ahmet Yılmaz", "Giriş yaptı", "2 dakika önce"),
            createActivity("Zeynep Kaya", "Profil bilgilerini güncelledi", "15 dakika önce"),
            createActivity("Mehmet Demir", "Yeni bir belge yükledi", "32 dakika önce"),
            createActivity("Ayşe Şahin", "Yorum yaptı", "1 saat önce"),
            createActivity("Ali Öztürk", "Yeni hesap oluşturdu", "3 saat önce")
        );
        return activities;
    }
    
    private Map<String, String> createActivity(String user, String action, String time) {
        Map<String, String> activity = new HashMap<>();
        activity.put("user", user);
        activity.put("action", action);
        activity.put("time", time);
        return activity;
    }
    
    // Örnek satışlar üretmek için yardımcı metod
    private List<Map<String, Object>> generateRecentSales() {
        List<Map<String, Object>> sales = Arrays.asList(
            createSale("#S-1289", "Mert Aksoy", "Akıllı Telefon", 4599.90, "Tamamlandı"),
            createSale("#S-1288", "Selin Yıldız", "Laptop", 8750.00, "İşleniyor"),
            createSale("#S-1287", "Deniz Kara", "Kulaklık", 899.90, "Tamamlandı"),
            createSale("#S-1286", "Burak Çelik", "Tablet", 2399.90, "Tamamlandı"),
            createSale("#S-1285", "Elif Yılmaz", "Monitör", 1850.50, "İptal Edildi")
        );
        return sales;
    }
    
    private Map<String, Object> createSale(String orderNo, String customer, String product, double amount, String status) {
        Map<String, Object> sale = new HashMap<>();
        sale.put("orderNo", orderNo);
        sale.put("customer", customer);
        sale.put("product", product);
        sale.put("amount", amount);
        sale.put("status", status);
        return sale;
    }
}
