package com.example.auctionuser.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 댓글 모델
 *
 * */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ReplyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    /**
     * 댓글
     *
     * */
    @Column(length = 500)
    private String content;

    // board ID
    private int boardId;

    // Write User Id
    private String username;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;



}
