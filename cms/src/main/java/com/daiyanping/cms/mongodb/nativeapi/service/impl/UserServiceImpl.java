package com.daiyanping.cms.mongodb.nativeapi.service.impl;

import com.daiyanping.cms.mongodb.nativeapi.entity.User;
import com.daiyanping.cms.mongodb.nativeapi.service.UserService;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserServiceImpl  implements UserService {

    @Resource
    private MongoOperations tempelate;
    
    @Override
    @Transactional
    public void doTransaction() {
        Query query = query(where("username").is("lison"));
        Update update = new Update().inc("lenght",1);
        tempelate.updateMulti(query,update, User.class);

        query = query(where("username").is("james"));
        update = new Update().inc("lenght",-1);
        tempelate.updateMulti(query,update, User.class);
    }
}