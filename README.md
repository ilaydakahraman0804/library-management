# Kütüphane Yönetim Sistemi

## Proje Hakkında

Bu proje, IYD 328 İş Yeri Deneyimi dersi kapsamında geliştirilmiş bir Kütüphane Yönetim Sistemi web uygulamasıdır. Sistem; kitap, kullanıcı, ödünç alma ve rezervasyon süreçlerini yönetmek amacıyla Java ve Spring Boot kullanılarak geliştirilmiştir.

## Kullanılan Teknolojiler

- Java 21
- Spring Boot 3.5.15
- Spring Security ve JWT tabanlı kimlik doğrulama
- PostgreSQL 15
- Maven
- Docker ve Docker Compose
- HTML, CSS ve JavaScript (sunucu taraflı statik arayüz)

## Sistem Özellikleri

### Kitap Yönetim Sistemi
Yetkili kullanıcılar (Admin ve Kütüphaneci) kitap ekleyebilir, kitap bilgilerini güncelleyebilir ve kitap silebilir. Tüm kullanıcılar başlık, yazar, ISBN veya yayınevine göre arama yapabilir ve kitapların müsaitlik durumunu görüntüleyebilir.

### Ödünç Alma ve İade Sistemi
Sistem; kitap ödünç alma, iade etme, geçmiş işlemleri görüntüleme ve son teslim tarihlerini takip etme işlevlerini desteklemektedir.

### Rezervasyon Sistemi
Mevcut olmayan bir kitap için kullanıcılar rezervasyon oluşturabilir. Sistem rezervasyon kayıtlarını tutar, durumlarını takip eder ve aynı kullanıcı tarafından gereksiz tekrar rezervasyonları engeller.

### Raporlama ve Analitik
Sistem aşağıdaki raporları sunmaktadır:
- En çok ödünç alınan kitaplar
- Aktif kullanıcılar
- Ödünç alınmış kitaplar
- Gecikmiş kitaplar
- Aylık ödünç alma istatistikleri

### Rol Tabanlı Erişim Kontrolü
Sistemde üç farklı kullanıcı rolü bulunmaktadır:
- **Admin:** Sistemdeki tüm işlemlere erişebilir, kullanıcı silme dahil her işlemi gerçekleştirebilir.
- **Kütüphaneci:** Kitap ekleme, güncelleme, silme ve raporları görüntüleme yetkisine sahiptir; ancak kullanıcı silme işlemi yapamaz.
- **Öğrenci:** Kitapları görüntüleyebilir, ödünç alma ve rezervasyon işlemlerini gerçekleştirebilir; kullanıcı yönetimi ve raporlama sayfalarına erişimi yoktur.

## Veritabanı Tasarımı

Sistemde aşağıdaki dört temel tablo bulunmaktadır:

**Books:** ISBN, kitap adı, yazar, yayınevi, yayın yılı, müsaitlik durumu

**Users:** Kullanıcı adı, e-posta, şifre, rol, kayıt tarihi

**BorrowTransactions:** İşlem kimliği, kullanıcı, kitap, ödünç alma tarihi, son teslim tarihi, iade tarihi, durum

**Reservations:** Rezervasyon kimliği, kullanıcı, kitap, rezervasyon tarihi, durum

## Veri Seti

Proje, gerçek kitap verileri içeren açık kaynaklı bir veri seti kullanmaktadır.

Kaynak: [Kaggle - Books Dataset](https://www.kaggle.com/datasets/saurabhbagchi/books-dataset)

Veri seti, kullanılmadan önce eksik ve tekrarlanan kayıtlardan temizlenmiş, veri tipleri uygun formata dönüştürülmüş ve PostgreSQL veritabanına aktarılmıştır. Bu işlem `scripts/import_books.py` dosyası ile gerçekleştirilmektedir.

### Veri Setinin Kurulması

1. Yukarıdaki Kaggle bağlantısından veri setini indirin ve `Books.csv` dosyasını masaüstüne kaydedin.
2. Gerekli Python kütüphanelerini kurun: `pip install pandas psycopg2-binary`
3. PostgreSQL veritabanı çalışır durumdayken aşağıdaki komut ile içe aktarma işlemini başlatın: `python3 scripts/import_books.py`

## Gereksinimler

Projeyi çalıştırmadan önce aşağıdaki yazılımların bilgisayarda kurulu olması gerekmektedir:

- Java 21 (JDK)
- Docker Desktop
- Maven (proje içinde bulunan Maven Wrapper sayesinde ayrıca kurulum gerektirmez)
- Python 3 (veri seti aktarımı için, pandas ve psycopg2-binary kütüphaneleriyle birlikte)

Bu proje belirli bir geliştirme ortamına (IDE) bağımlı değildir. Java projelerini çalıştırabilen herhangi bir ortam (IntelliJ IDEA, VS Code, Eclipse) veya yalnızca terminal kullanılarak da çalıştırılabilir.

## Kurulum ve Çalıştırma

1. Depoyu klonlayın: `git clone https://github.com/ilaydakahraman0804/library-management.git`
2. Docker Desktop'ın çalışır durumda olduğundan emin olun.
3. Proje dizininde aşağıdaki komutu çalıştırarak PostgreSQL veritabanını başlatın: `docker-compose up -d`
4. Uygulamayı başlatın. Bunun için iki seçenek vardır:
    - Bir geliştirme ortamı (IDE) kullanılıyorsa: Projeyi açıp `LibraryManagementApplication` sınıfını çalıştırın.
    - Terminal kullanılıyorsa: Proje dizininde `./mvnw spring-boot:run` komutunu çalıştırın.
5. Veri setini yukarıdaki "Veri Setinin Kurulması" adımlarını izleyerek içe aktarın.
6. Tarayıcıdan `http://localhost:8080` adresine giderek uygulamaya erişebilirsiniz.

## Test Kullanıcıları

Sistemi test etmek için aşağıdaki örnek kullanıcı bilgileri kullanılabilir veya kayıt ekranından yeni kullanıcı oluşturulabilir:

- Kullanıcı Adı - Şifre - Rol -

- admin - admin123 - Admin -
- öğrenci01 - ogrenci123 - Öğrenci -

## Proje Yapısı

src/main/java/com/library/library_management/ dizini altında entity (veritabanı varlık sınıfları), repository (veri erişim katmanı), service (iş mantığı katmanı), controller (REST API uç noktaları) ve config (güvenlik ve JWT yapılandırması) paketleri bulunmaktadır.

src/main/resources/static/ dizini altında index.html (giriş sayfası), dashboard.html (ana sayfa), books.html (kitap yönetimi), borrow.html (ödünç alma ve iade işlemleri), reservations.html (rezervasyon işlemleri), users.html (kullanıcı yönetimi) ve reports.html (raporlama ve analitik) sayfaları bulunmaktadır.

## Geliştirici

İlayda Kahraman - IYD 328 İş Yeri Deneyimi