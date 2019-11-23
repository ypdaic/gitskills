package com.daiyanping.cms.springdata.rest.service;

import com.daiyanping.cms.springdata.rest.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress,Integer> {

}
