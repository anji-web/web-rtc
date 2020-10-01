package com.example.webrtc.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "token_activation_email")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Long idToken;

    private String token;
    private Date expirationDate;
    private Date createdDate;
    private Date modifiedDate;
    private boolean isActive;


    @OneToOne(targetEntity = UserInvited.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id_invited")
    private UserInvited userInvited;

    public Date calculateExpireDate(int expiryTimeMinutes){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, expiryTimeMinutes);
        return new Date(cal.getTime().getTime());
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getIdToken() {
        return idToken;
    }

    public void setIdToken(Long idToken) {
        this.idToken = idToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UserInvited getUserInvited() {
        return userInvited;
    }

    public void setUserInvited(UserInvited userInvited) {
        this.userInvited = userInvited;
    }
}
