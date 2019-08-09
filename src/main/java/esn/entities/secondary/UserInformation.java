package esn.entities.secondary;

import esn.entities.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Calendar;

@Embeddable
public class UserInformation implements Serializable {

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Calendar birthDate;

    @Size(max = 12, message = "введите не более 12 цифр")
    @Column(length = 12)
    private String phoneMobile;

    @Size(max = 12, message = "введите не более 12 цифр")
    @Column(length = 12)
    private String phoneWork;

    @Size(max = 12, message = "введите не более 12 цифр")
    @Column(length = 12)
    private String phoneInternal;

    @Email(message = "неверный e-mail")
    private String email;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    public Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }
}
