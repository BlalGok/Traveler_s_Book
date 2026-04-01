


## Temel Özellikler

* **Google Maps Entegrasyonu:** Kullanıcılar harita üzerinde istedikleri konumu görüntüleyebilir, zoom yapabilir ve gezinebilir.
* **Konum İşaretleme (Marker):** Harita üzerinde istenilen bir noktaya uzun basılarak özel bir işaretçi (marker) eklenebilir.
* **Veritabanı Kaydı (SQLite/Room):** İşaretlenen konumlar, kullanıcı tarafından girilen bir başlık ve isteğe bağlı notlarla birlikte cihazın yerel veritabanına kaydedilir.
* **Kayıtlı Yerler Listesi:** Kullanıcı, daha önce kaydettiği tüm yerleri bir liste halinde görüntüleyebilir.
* **Konuma Gitme:** Listeden bir yer seçildiğinde, uygulama otomatik olarak haritayı o konuma odaklar ve detayları gösterir.
* **Kayıt Silme:** Kullanıcı, artık ihtiyaç duymadığı kayıtlı yerleri listeden silebilir.

## Kullanılan Teknolojiler

* **Dil:** Java
* **SDK:** Android SDK
* **Harita Servisi:** Google Maps Platform (Android için Harita SDK'sı)
* **Veritabanı:** Room Persistence Library (SQLite tabanlı)
* **Arayüz Bağlama:** ViewBinding (Modern ve güvenli arayüz yönetimi için)

## Uygulama Demosu

Bu bölümde, uygulamanın emülatör veya gerçek bir cihaz üzerindeki çalışma anına ait kısa bir video yer almaktadır. Bu demo, harita entegrasyonunun, konum işaretlemenin ve veritabanı işlemlerinin (kaydetme/görüntüleme) aktif olarak çalıştığını kanıtlamaktadır.
![Traveler's Book Demo](https://github.com/user-attachments/assets/d9254f3b-26ef-4565-8944-f43edaf8f90a)

---

## Kurulum ve Çalıştırma Notu

Proje, **güvenlik ve "best practices" (en iyi uygulamalar)** gereği Google Maps API anahtarını kod içerisinde veya GitHub üzerinde barındırmamaktadır. API anahtarı, `local.properties` dosyası üzerinden güvenli bir şekilde yönetilmektedir.

Projeyi kendi bilgisayarınızda çalıştırmak için aşağıdaki adımları izlemeniz gerekmektedir:

1.  Proje ana dizininde bulunan `local.properties` dosyasını Android Studio veya herhangi bir metin düzenleyici ile açın. (Eğer dosya yoksa, kendiniz oluşturun).
2.  Dosyanın en altına aşağıdaki satırı ekleyin ve `SİZİN_API_ANAHTARINIZ` kısmına kendi Google Cloud Console'unuzdan aldığınız geçerli bir Google Maps API anahtarını yapıştırın:
    ```properties
    MAPS_API_KEY=SİZİN_API_ANAHTARINIZ
    ```
3.  Değişiklikleri kaydedin ve Android Studio'da **Build > Rebuild Project** seçeneğiyle projeyi yeniden derleyin.
