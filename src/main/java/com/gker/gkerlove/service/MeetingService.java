package com.gker.gkerlove.service;


import com.alibaba.fastjson.JSONObject;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.util.HttpUtils;
import com.tencentyun.TLSSigAPIv2;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingService {
    @Resource
    MongoTemplate mongoTemplate;

    public List<User> MeetingGetlist(String tousername, String gender, Integer min_age, Integer max_age, String city, String institute){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").ne(tousername));
        if (gender != null) {
            query.addCriteria(Criteria.where("info.gender").is(gender));
        }
        if (min_age != null) {
            query.addCriteria(Criteria.where("info.age").gte(min_age));
        }
        if (max_age != null) {
            query.addCriteria(Criteria.where("info.age").lte(max_age));
        }
        if (city != null) {
            query.addCriteria(Criteria.where("info.city").is(city));
        }
        if (institute != null) {
            query.addCriteria(Criteria.where("info.institute").is(institute));
        }
        return mongoTemplate.find(query, User.class);
    }

    public String MeetingAddlover(String formusername,String tousername) {
        TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
        String Sig = api.genUserSig("administrator", 60);
        String url = "https://console.tim.qq.com/v4/sns/friend_add?sdkappid=1600009914&identifier=administrator&usersig="+Sig+"&random=99999999&contenttype=json";

        JSONObject param = new JSONObject();
        param.put("From_Account",formusername);
        List<JSONObject> frienditems = new ArrayList<>();
        JSONObject frienditem = new JSONObject();
        frienditem.put("To_Account",tousername);
        frienditem.put("GroupName","我喜欢的人");
        frienditem.put("AddSource","AddSource_Type_Meeting");
        frienditem.put("AddWording","我将你列为了我喜欢的人，如果对我的个人信息感兴趣的话，就将我添加为好友开始聊天吧！");
        frienditems.add(frienditem);
        param.put("AddFriendItem",frienditems);

        Query query = new Query(Criteria.where("username").is(tousername));
        User touser = mongoTemplate.findOne(query, User.class);
        Integer liked_num = null;
        if (touser != null) {
            liked_num = touser.getLikedBy() + 1;
        }
        else{
            liked_num = 1;
        }
        Update update = Update.update("liked_by",liked_num);
        mongoTemplate.updateFirst(query,update, "users");

        Query query1 = new Query(Criteria.where("username").is(formusername));
        User fromuser = mongoTemplate.findOne(query, User.class);
        Integer like_num = null;
        if (fromuser != null) {
            like_num = fromuser.getLikes() + 1;
        }
        else{
            like_num = 1;
        }
        Update update1 = Update.update("likes",like_num);
        mongoTemplate.updateFirst(query1,update1, "users");
        return HttpUtils.sendPost(url, param.toJSONString());
    }
}
