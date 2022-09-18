package com.example.auctionuser.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class JwtSuperintendModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "user")
    @OneToOne
    private UserModel user;

    @Column(nullable = false, length = 600)
    private String accessToken;

    @Column(nullable = false, length = 600)
    private String refreshToken;

    @CreationTimestamp
    private Timestamp createToken;

    @UpdateTimestamp
    private Timestamp modifyToken;

}
