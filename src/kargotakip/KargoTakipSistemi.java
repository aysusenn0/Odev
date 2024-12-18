package kargotakip;

import java.util.*;

class Gonderi {
    String gonderiID;
    String tarih;
    String teslimDurumu;
    int teslimSuresi;

    public Gonderi(String gonderiID, String tarih, String teslimDurumu, int teslimSuresi) {
        this.gonderiID = gonderiID;
        this.tarih = tarih;
        this.teslimDurumu = teslimDurumu;
        this.teslimSuresi = teslimSuresi;
    }

    @Override
    public String toString() {
        return "Gonderi ID: " + gonderiID + ", Tarih: " + tarih + ", Teslim Durumu: " + teslimDurumu + ", Teslim Suresi: " + teslimSuresi + " gun";
    }
}

class Musteri {
    String musteriID;
    String isim;
    LinkedList<Gonderi> gonderiGecmisi;
    Stack<Gonderi> sonGonderiler;

    public Musteri(String musteriID, String isim) {
        this.musteriID = musteriID;
        this.isim = isim;
        this.gonderiGecmisi = new LinkedList<>();
        this.sonGonderiler = new Stack<>();
    }

    public void gonderiEkle(Gonderi gonderi) {
        gonderiGecmisi.add(gonderi);
        if (sonGonderiler.size() == 5) {
            sonGonderiler.remove(0); // İlk gönderiyi çıkararak stack'i güncel tutar
        }
        sonGonderiler.push(gonderi);
    }

    public void gonderiGecmisiGoster() {
        if (gonderiGecmisi.isEmpty()) {
            System.out.println("Gonderi gecmisi bos.");
        } else {
            for (Gonderi gonderi : gonderiGecmisi) {
                System.out.println(gonderi);
            }
        }
    }

    public void sonGonderileriGoster() {
        if (sonGonderiler.isEmpty()) {
            System.out.println("Son gonderi bulunmamaktadir.");
        } else {
            System.out.println("Son Gonderiler:");
            for (int i = sonGonderiler.size() - 1; i >= 0; i--) {
                System.out.println(sonGonderiler.get(i));
            }
        }
    }

    @Override
    public String toString() {
        return "Musteri ID: " + musteriID + ", Isim: " + isim;
    }
}

class Rota {
    String sehirAdi;
    String ilceAdi;
    List<Rota> altRotalar;

    public Rota(String sehirAdi, String ilceAdi) {
        this.sehirAdi = sehirAdi;
        this.ilceAdi = ilceAdi;
        this.altRotalar = new ArrayList<>();
    }

    public void altRotaEkle(Rota rota) {
        altRotalar.add(rota);
    }

    public void rotalariGoster(String prefix) {
        System.out.println(prefix + "- " + sehirAdi + " (" + ilceAdi + ")");
        for (Rota altRota : altRotalar) {
            altRota.rotalariGoster(prefix + "  ");
        }
    }
}

public class KargoTakipSistemi {
    static LinkedList<Musteri> musteriler = new LinkedList<>();
    static PriorityQueue<Gonderi> kargoOncelikSirasi = new PriorityQueue<>(Comparator.comparingInt(g -> g.teslimSuresi));
    static Rota merkez = new Rota("Merkez", "MERKEZ");
    static Scanner scanner = new Scanner(System.in);

    public static void yeniMusteriEkle() {
        System.out.print("Musteri ID: ");
        String musteriID = scanner.nextLine();
        System.out.print("Isim: ");
        String isim = scanner.nextLine();

        Musteri musteri = new Musteri(musteriID, isim);
        musteriler.add(musteri);
        System.out.println("Musteri basariyla eklendi!");
    }

    public static void musteriGonderiEkle() {
        System.out.print("Musteri ID: ");
        String musteriID = scanner.nextLine();

        for (Musteri musteri : musteriler) {
            if (musteri.musteriID.equals(musteriID)) {
                System.out.print("Gonderi ID: ");
                String gonderiID = scanner.nextLine();
                System.out.print("Tarih: ");
                String tarih = scanner.nextLine();
                System.out.print("Teslim Durumu (Teslim Edildi/Teslim Edilmedi): ");
                String teslimDurumu = scanner.nextLine();
                System.out.print("Teslim Suresi (gun): ");
                int teslimSuresi = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                Gonderi gonderi = new Gonderi(gonderiID, tarih, teslimDurumu, teslimSuresi);
                musteri.gonderiEkle(gonderi);
                kargoOncelikSirasi.add(gonderi);
                System.out.println("Gonderi basariyla eklendi!");
                return;
            }
        }

        System.out.println("Musteri bulunamadi!");
    }

    public static void oncelikliKargoIsle() {
        if (kargoOncelikSirasi.isEmpty()) {
            System.out.println("Islenecek kargo bulunmamaktadir.");
        } else {
            Gonderi oncelikliGonderi = kargoOncelikSirasi.poll();
            System.out.println("Oncelikli kargo isleniyor: " + oncelikliGonderi);
        }
    }

    public static void musteriGonderiGoster() {
        System.out.print("Musteri ID: ");
        String musteriID = scanner.nextLine();

        for (Musteri musteri : musteriler) {
            if (musteri.musteriID.equals(musteriID)) {
                System.out.println("Musteri Bilgileri: " + musteri);
                musteri.gonderiGecmisiGoster();
                return;
            }
        }

        System.out.println("Musteri bulunamadi!");
    }

    public static void musteriSonGonderileriGoster() {
        System.out.print("Musteri ID: ");
        String musteriID = scanner.nextLine();

        for (Musteri musteri : musteriler) {
            if (musteri.musteriID.equals(musteriID)) {
                musteri.sonGonderileriGoster();
                return;
            }
        }

        System.out.println("Musteri bulunamadi!");
    }
    
    

    public static void teslimatRotasiEkle() {
        System.out.print("Sehir Adi: ");
        String sehirAdi = scanner.nextLine();
        System.out.print("Ilce Adi : ");
        String ilceAdi = scanner.nextLine();
        System.out.print("Bagli Oldugu Sehir ID (Merkez icin 'MERKEZ'): ");
        String ustSehirID = scanner.nextLine();

        Rota yeniRota = new Rota(sehirAdi, ilceAdi);
        
        if (ustSehirID.equalsIgnoreCase("MERKEZ")) {
            merkez.altRotaEkle(yeniRota);
        } else {
            Rota ustRota = rotaBul(merkez, ustSehirID);
            if (ustRota != null) {
                ustRota.altRotaEkle(yeniRota);
            } else {
                System.out.println("Ust sehir bulunamadi!");
                return;
            }
        }

        System.out.println("Teslimat rotasi basariyla eklendi!");
    }

    public static Rota rotaBul(Rota rota, String ilceAdi) {
        if (rota.ilceAdi.equalsIgnoreCase(ilceAdi)) {
            return rota;
        }
        for (Rota altRota : rota.altRotalar) {
            Rota bulunan = rotaBul(altRota, ilceAdi);
            if (bulunan != null) {
                return bulunan;
            }
        }
        return null;
    }

    public static void teslimatRotalariniGoster() {
        System.out.println("\nTeslimat Rotalari:");
        merkez.rotalariGoster("");
    }
    
    // Kargo durumu sorgulama işlemleri:
// Kargo Durumu Sorgulama fonksiyonu
public static void kargoDurumuSorgula(int kargoId) {
    boolean kargoBulundu = false;

    for (Musteri musteri : musteriler) {
        for (Gonderi gonderi : musteri.gonderiGecmisi) {
            if (gonderi.gonderiID.equals(kargoId)) {
                kargoBulundu = true;
                // Teslim durumu kontrolü ve çıktı
                if (gonderi.teslimDurumu.equalsIgnoreCase("Teslim Edildi")) {
                    System.out.println("Kargo ID: " + kargoId + " - Teslim Edildi.");
                    System.out.println("Teslim Tarihi: " + gonderi.tarih);
                } else if (gonderi.teslimDurumu.equalsIgnoreCase("Teslim Edilmedi")) {
                    System.out.println("Kargo ID: " + kargoId + " - Teslim Edilmedi.");
                    System.out.println("Tahmini Teslim Tarihi:5 is günü icerisindedir. " );
                }
                return; // Kargo bulunduğunda döngüden çık
            }
        }
    }

    if (!kargoBulundu) {
        System.out.println("Kargo ID: " + kargoId + " ile ilgili gönderi bulunamadı.");
    }
}

    
    
    public static void menu() {
        while (true) {
            System.out.println("\n--- Kargo Takip Sistemi ---");
            System.out.println("1. Yeni Musteri Ekle");
            System.out.println("2. Musteriye Gonderi Ekle");
            System.out.println("3. Musteri Gonderi Gecmisini Goster");
            System.out.println("4. Oncelikli Kargo Isle");
            System.out.println("5. Teslimat Rotasi Ekle");
            System.out.println("6. Teslimat Rotalarini Goster");
            System.out.println("7. Son Gonderileri Goster"); 
            System.out.println("8. Kargolari Sorgula");
            System.out.println("9. Cikis");
            System.out.print("Seciminiz: ");

            try {
                int secim = Integer.parseInt(scanner.nextLine());
                switch (secim) {
                    case 1:
                        yeniMusteriEkle();
                        break;
                    case 2:
                        musteriGonderiEkle();
                        break;
                    case 3:
                        musteriGonderiGoster();
                        break;
                    case 4:
                        oncelikliKargoIsle();
                        break;
                    case 5:
                        teslimatRotasiEkle();
                        break;
                    case 6:
                        teslimatRotalariniGoster();
                        break;
                    case 7:
                        musteriSonGonderileriGoster();
                        break;
                     case 8:
                      System.out.print("Kargo ID: ");
                      int gonderiID = scanner.nextInt();
                      kargoDurumuSorgula(gonderiID);
                        break;
                    case 9:
                        System.out.println("Sistemden cikiliyor...");
                        return;
                    default:
                        System.out.println("Gecersiz secim. Lutfen 1 ile 8 arasinda bir sayi girin.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Hata: Lutfen gecerli bir sayi girin.");
            }
        }
    }

    public static void main(String[] args) {
        menu();
    }
}
