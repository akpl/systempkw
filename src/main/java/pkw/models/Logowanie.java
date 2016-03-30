package pkw.models;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Table(name = "logowania")
public class Logowanie {
    private int id;
    private String ip;
    private LocalDateTime dataLogowania;
    private String ua;
    private String przegladarka;
    private String os;
    private String typUrzadzenia;
    private String przegladarkaIkona;
    private String osIkona;
    private Uzytkownik uzytkownik;

    @Id
    @GeneratedValue
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "data_logowania")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    public LocalDateTime getDataLogowania() {
        return dataLogowania;
    }

    public void setDataLogowania(LocalDateTime dataLogowania) {
        this.dataLogowania = dataLogowania;
    }

    @Column(name = "ua")
    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    @Column(name = "przegladarka")
    public String getPrzegladarka() {
        return przegladarka;
    }

    public void setPrzegladarka(String przegladarka) {
        this.przegladarka = przegladarka;
    }

    @Column(name = "os")
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Column(name = "typ_urzadzenia")
    public String getTypUrzadzenia() {
        return typUrzadzenia;
    }

    public void setTypUrzadzenia(String typUrzadzenia) {
        this.typUrzadzenia = typUrzadzenia;
    }

    @Column(name = "przegladarka_ikona")
    public String getPrzegladarkaIkona() {
        return przegladarkaIkona;
    }

    public void setPrzegladarkaIkona(String przegladarkaIkona) {
        this.przegladarkaIkona = przegladarkaIkona;
    }

    @Column(name = "os_ikona")
    public String getOsIkona() {
        return osIkona;
    }

    public void setOsIkona(String osIkona) {
        this.osIkona = osIkona;
    }

    @ManyToOne
    @JoinColumn(name = "uzytkownik_id", referencedColumnName = "id", nullable = false)
    public Uzytkownik getUzytkownik() {
        return uzytkownik;
    }

    public void setUzytkownik(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Logowanie logowanie = (Logowanie) o;

        if (id != logowanie.id) return false;
        if (ip != null ? !ip.equals(logowanie.ip) : logowanie.ip != null) return false;
        if (dataLogowania != null ? !dataLogowania.equals(logowanie.dataLogowania) : logowanie.dataLogowania != null)
            return false;
        if (przegladarka != null ? !przegladarka.equals(logowanie.przegladarka) : logowanie.przegladarka != null)
            return false;
        return uzytkownik != null ? uzytkownik.equals(logowanie.uzytkownik) : logowanie.uzytkownik == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (dataLogowania != null ? dataLogowania.hashCode() : 0);
        result = 31 * result + (przegladarka != null ? przegladarka.hashCode() : 0);
        result = 31 * result + (uzytkownik != null ? uzytkownik.hashCode() : 0);
        return result;
    }
}