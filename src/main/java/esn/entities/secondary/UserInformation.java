package esn.entities.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import esn.entities.User;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class UserInformation implements Serializable {

    private Date birthDate;

    @Column(length = 12, columnDefinition = "varchar(12) default 'Не указано'", nullable = false)
    private String phoneMobile;

    @Column(length = 12, columnDefinition = "varchar(12) default 'Не указано'", nullable = false)
    private String phoneWork;

    @Column(length = 12, columnDefinition = "varchar(12) default 'Не указано'", nullable = false)
    private String phoneInternal;

    @Email(message = "неверный e-mail")
    @Column(nullable = false, columnDefinition = "varchar(255) default 'Не указано'")
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    private User boss;

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getPhoneWork() {
        return phoneWork;
    }

    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    public String getPhoneInternal() {
        return phoneInternal;
    }

    public void setPhoneInternal(String phoneInternal) {
        this.phoneInternal = phoneInternal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getBoss() {
        return boss;
    }

    public void setBoss(User boss) {
        this.boss = boss;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
