package com.daiyanping.cms.springdata.rest.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_address", schema = "test", catalog = "")
public class UserAddress {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "city", nullable = true, length = 50)
    private String city;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userByUserId;
}
