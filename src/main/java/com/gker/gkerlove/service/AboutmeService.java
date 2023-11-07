package com.gker.gkerlove.service;

import com.gker.gkerlove.bean.Aboutme;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


@Service
public class AboutmeService {
    @Resource
    MongoTemplate mongoTemplate;

    public void setabout(Aboutme aboutme) {
        mongoTemplate.save(aboutme, "about");
        Query query = new Query(Criteria.where("username").is(aboutme.getUsername()));
        Update update = Update.update("hasaboutme","true");
        mongoTemplate.updateFirst(query,update, "users");
    }

    public Aboutme getabout(String username) {
        Aboutme aboutme = mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), Aboutme.class);
        assert aboutme != null;
        return aboutme;
    }

}
