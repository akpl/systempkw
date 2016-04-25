package pkw;

public class WynikiKomitetu {
    private String nazwa;
    private int numer;
    private int liczbaPoslow;
    private String kolor;

    public WynikiKomitetu(String nazwa, int numer, int liczbaPoslow, String kolor) {
        this.nazwa = nazwa;
        this.numer = numer;
        this.liczbaPoslow = liczbaPoslow;
        this.kolor = kolor;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getNumer() {
        return numer;
    }

    public void setNumer(int numer) {
        this.numer = numer;
    }

    public int getLiczbaPoslow() {
        return liczbaPoslow;
    }

    public void setLiczbaPoslow(int liczbaPoslow) {
        this.liczbaPoslow = liczbaPoslow;
    }

    public String getKolor() {
        return kolor;
    }

    public void setKolor(String kolor) {
        this.kolor = kolor;
    }
}
