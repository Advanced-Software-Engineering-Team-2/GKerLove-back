package com.gker.gkerlove.service;

import com.gker.gkerlove.bean.Aboutme;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Service
public class AboutmeService {
    @Resource
    MongoTemplate mongoTemplate;

    public void setabout(Aboutme aboutme) {
        mongoTemplate.save(aboutme, "about");
    }

    public Aboutme getabout(String username) {
        Aboutme aboutme = mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), Aboutme.class);
        assert aboutme != null;
        return aboutme;
    }

}
