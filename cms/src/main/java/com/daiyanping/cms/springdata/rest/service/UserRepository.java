package com.daiyanping.cms.springdata.rest.service;

import com.daiyanping.cms.springdata.rest.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {

    Page<User> findByName(@Param("name") String name, Pageable pageable);
}
