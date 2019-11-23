package com.daiyanping.cms.springdata.rest.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
public class User {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "name", nullable = true, length = 50)
    private String name;

    @Basic
    @Column(name = "email", nullable = true, length = 200)
    private String email;

    @OneToMany(mappedBy = "userByUserId")
    private Collection<UserAddress> userAddressesById;


}