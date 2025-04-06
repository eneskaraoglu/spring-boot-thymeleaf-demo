# Spring Boot ve Thymeleaf Demo Projesi

Bu proje, Spring Boot ve Thymeleaf kullanarak web uygulaması geliştirmenin temel özelliklerini göstermek için hazırlanmıştır.

## Proje Özellikleri

- Spring Boot 2.7.x
- Thymeleaf şablon motoru
- Bootstrap 4 ile duyarlı tasarım (responsive)
- Temel CRUD işlemleri
- Form işlemleri ve veri doğrulama
- Layout sistemi ve fragment kullanımı
- DataTables ile veri görüntüleme ve filtreleme
- Chart.js ile veri görselleştirme
- Flatpickr ile tarih ve saat seçimi

## Proje Modülleri

### Temel Özellikler
- **Temel Örnekler**: Thymeleaf ifadeleri, koşullar, döngüler vb.
- **Form İşlemleri**: Form oluşturma ve veri gönderme örnekleri
- **Layout Sistemi**: Şablonlar ve fragmentler kullanarak sayfa düzeni oluşturma

### Kullanıcı Yönetimi
- Kullanıcı listeleme, görüntüleme, ekleme, düzenleme ve silme işlemleri
- Tam CRUD (Create, Read, Update, Delete) fonksiyonalitesi

### Stok Yönetimi
- **Stok Tanımlama**: Stok kalemleri oluşturma ve yönetme
  - Stok kodu, stok adı, kategori, birim, birim fiyat vb.
  - Kategori ve birim için dropdown menüler
  - Stok kalemi listeleme ve filtreleme
  
- **Stok Hareketi**: Giriş/çıkış hareketleri ile stok miktarlarını güncelleme
  - Hareket tipi seçimi (giriş/çıkış)
  - Miktar, tarih ve neden girilmesi
  - Referans numarası ve notlar ekleme
  - Stok miktarı kontrolü ve yetersiz stok durumunu engelleme
  
- **Stok Takip**: Mevcut stok durumlarını görüntüleme ve raporlama
  - Kart ve tablo görünümleri arasında geçiş
  - Kategori bazlı filtreleme
  - Stok seviyelerini grafikle görselleştirme
  - Hareket geçmişini zaman çizelgesi ile gösterme

### Dashboard Modülleri
- **Analitik Dashboard**: Kullanıcı istatistikleri ve aktivite takibi
- **Satış Dashboard**: Satış istatistikleri ve raporlama

## Proje Yapısı

```
src/main/java/com/example/demo/
  ├── controller/        # Web controller sınıfları
  │   ├── HomeController.java
  │   ├── UserController.java
  │   └── StockController.java
  ├── model/             # Model sınıfları
  │   ├── User.java
  │   ├── StockItem.java
  │   └── StockMovement.java
  ├── service/           # Servis sınıfları
  │   ├── UserService.java
  │   └── StockService.java
  └── DemoApplication.java  # Ana uygulama sınıfı

src/main/resources/
  ├── static/            # Statik kaynaklar
  │   ├── css/           # CSS dosyaları
  │   ├── js/            # JavaScript dosyaları
  │   └── images/        # Görsel dosyaları
  ├── templates/         # Thymeleaf şablonları
  │   ├── users/         # Kullanıcı yönetimi şablonları
  │   ├── stock/         # Stok yönetimi şablonları
  │   │   ├── items.html           # Stok kalemleri listesi
  │   │   ├── item-form.html       # Stok kalemi form
  │   │   ├── item-view.html       # Stok kalemi detay
  │   │   ├── movements.html       # Stok hareketleri listesi
  │   │   ├── movement-form.html   # Stok hareketi form
  │   │   ├── movement-view.html   # Stok hareketi detay
  │   │   ├── track.html           # Stok takip sayfası
  │   │   └── track-details.html   # Stok takip detay
  │   ├── dashboards/    # Dashboard şablonları
  │   ├── index.html     # Ana sayfa
  │   ├── basic-examples.html
  │   ├── form-demo.html
  │   └── layout-demo.html
  └── application.properties  # Uygulama yapılandırması
```

## Thymeleaf Bileşenleri ve Özellikleri

Bu projede kullanılan başlıca Thymeleaf özellikleri:

- Temel ifadeler: `${...}`, `*{...}`, `@{...}`, `#{...}`
- Koşullu işleme: `th:if`, `th:unless`, `th:switch`, `th:case`
- Döngüler: `th:each`
- Form işleme: `th:object`, `th:field`, `th:action`
- Form bileşenleri: 
  - Text input, number input, textarea
  - Dropdown (select/option)
  - Radio butonlar
  - Checkbox'lar
  - Tarih seçiciler (Flatpickr entegrasyonu)
- Fragment'lar ve şablonlar
- İfade nesneleri: #dates, #numbers, #strings, #lists vb.
- İnline işleme: `th:inline="javascript"`

## JavaScript Kütüphaneleri

- **Bootstrap 4**: Responsive tasarım ve UI bileşenleri
- **jQuery**: DOM manipülasyonu
- **DataTables**: Tablo görüntüleme, sıralama, filtreleme ve sayfalama
- **Chart.js**: Veri görselleştirme ve grafikler
- **Flatpickr**: Tarih ve saat seçim aracı
- **Font Awesome**: İkonlar

## Çalıştırma

Bu projeyi yerel geliştirme ortamınızda çalıştırmak için:

1. Projeyi klonlayın veya indirin
2. Maven ile bağımlılıkları yükleyin: `mvn install`
3. Uygulamayı başlatın: `mvn spring-boot:run`
4. Tarayıcınızda şu adresi açın: `http://localhost:8080`

## Notlar

- Uygulama başlatıldığında otomatik olarak 30 adet örnek stok kalemi ve hareketleri oluşturulur
- Bu uygulama sadece demo amaçlıdır, gerçek bir veritabanı kullanmaz (veriler in-memory olarak saklanır)

## Kaynaklar

- [Spring Boot Dökümantasyonu](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Thymeleaf Dökümantasyonu](https://www.thymeleaf.org/documentation.html)
- [Bootstrap Dökümantasyonu](https://getbootstrap.com/docs/4.5/getting-started/introduction/)
- [Chart.js Dökümantasyonu](https://www.chartjs.org/docs/latest/)
- [DataTables Dökümantasyonu](https://datatables.net/manual/)
