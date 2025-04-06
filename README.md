# Spring Boot ve Thymeleaf Demo Projesi

Bu proje, Spring Boot ve Thymeleaf kullanarak web uygulaması geliştirmenin temel özelliklerini göstermek için hazırlanmıştır.

## Proje Özellikleri

- Spring Boot 2.7.x
- Thymeleaf şablon motoru
- Bootstrap 4 ile duyarlı tasarım
- Temel CRUD işlemleri
- Form işlemleri ve doğrulama
- Layout sistemi ve fragment kullanımı

## Proje Yapısı

```
src/main/java/com/example/demo/
  ├── controller/        # Web controller sınıfları
  │   ├── HomeController.java
  │   └── UserController.java
  ├── model/             # Model sınıfları
  │   └── User.java
  ├── service/           # Servis sınıfları
  │   └── UserService.java
  └── DemoApplication.java  # Ana uygulama sınıfı

src/main/resources/
  ├── static/            # Statik kaynaklar
  │   ├── css/           # CSS dosyaları
  │   ├── js/            # JavaScript dosyaları
  │   └── images/        # Görsel dosyaları
  ├── templates/         # Thymeleaf şablonları
  │   ├── users/         # Kullanıcı yönetimi şablonları
  │   │   ├── list.html
  │   │   ├── view.html
  │   │   └── form.html
  │   ├── index.html
  │   ├── basic-examples.html
  │   ├── form-demo.html
  │   └── layout-demo.html
  └── application.properties  # Uygulama yapılandırması
```

## Çalıştırma

Bu projeyi yerel geliştirme ortamınızda çalıştırmak için:

1. Projeyi klonlayın veya indirin
2. Maven ile bağımlılıkları yükleyin: `mvn install`
3. Uygulamayı başlatın: `mvn spring-boot:run`
4. Tarayıcınızda şu adresi açın: `http://localhost:8080`

## Demo Sayfaları

Proje, Thymeleaf'in çeşitli özelliklerini gösteren birkaç demo sayfası içerir:

- **Ana Sayfa**: Tüm demo sayfalarına bağlantılar
- **Temel Örnekler**: Thymeleaf ifadeleri, koşullar, döngüler vb.
- **Form İşlemleri**: Form oluşturma ve veri gönderme örnekleri
- **Layout Sistemi**: Şablonlar ve fragmentler kullanarak sayfa düzeni oluşturma
- **Kullanıcı Yönetimi**: Tam bir CRUD örneği

## Kaynaklar

- [Spring Boot Dökümantasyonu](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Thymeleaf Dökümantasyonu](https://www.thymeleaf.org/documentation.html)
- [Bootstrap Dökümantasyonu](https://getbootstrap.com/docs/4.5/getting-started/introduction/)
