package pkw.forms;

import org.hibernate.validator.constraints.Email;

/**
 * Created by Elimas on 2016-05-01.
 */
public class UstawieniaKonta {
    private String email;
    private String haslo;
    private String powtorzHaslo;

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getPowtorzHaslo() {
        return powtorzHaslo;
    }

    public void setPowtorzHaslo(String powtorzHaslo) {
        this.powtorzHaslo = powtorzHaslo;
    }
}
