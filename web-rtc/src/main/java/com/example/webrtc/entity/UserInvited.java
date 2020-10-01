package com.example.webrtc.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_invited")
public class UserInvited {
    @Id
    @Column(name = "id_invited")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUserInvited;

    private String username;
    private String email;
    private String status;

    public Long getIdUserInvited() {
        return idUserInvited;
    }

    public void setIdUserInvited(Long idUserInvited) {
        this.idUserInvited = idUserInvited;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
