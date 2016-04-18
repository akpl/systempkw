package pkw.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
/**
 * Created by Elleander on 18/04/2016.
 */

@Entity
@Table(name = "newsletter")
public class Newsletter {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Email
    @NotBlank
    @Column(name = "email")
    private String email;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email=email;}
}
