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

    public List<User> MeetingGetlist(String fromusername, String gender, Integer min_age, Integer max_age, String city, String institute){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").ne(fromusername));
        if (!gender.isEmpty()) {
            query.addCriteria(Criteria.where("info.gender").is(gender));
        }
        if (min_age != 0) {
            query.addCriteria(Criteria.where("info.age").gte(min_age));
        }
        if (max_age != 0) {
            query.addCriteria(Criteria.where("info.age").lte(max_age));
        }
        if (!city.isEmpty()) {
            query.addCriteria(Criteria.where("info.city").is(city));
        }
        if (!institute.isEmpty()) {
            query.addCriteria(Criteria.where("info.institute").is(institute));
        }
        List<User> userlist = mongoTemplate.find(query, User.class);
        Query query1 = new Query(Criteria.where("username").is(fromusername));
        User fromuser = mongoTemplate.findOne(query1, User.class);
        List<String> nomeetinglist = new ArrayList<>();
        if (fromuser != null) {
            nomeetinglist = fromuser.getNomeetinglist();
        }
        List<User> newuserlist = new ArrayList<>();
        if(nomeetinglist != null){
            for(User item : userlist){
                if(!nomeetinglist.contains(item.getUsername())){
                    newuserlist.add(item);
                }
            }
        }
        else{
            newuserlist = userlist;
        }
        return newuserlist;
    }

    public String MeetingAddlover(String fromusername,String tousername) {
        TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
        String Sig = api.genUserSig("administrator", 60);
        String url = "https://console.tim.qq.com/v4/sns/friend_add?sdkappid=1600009914&identifier=administrator&usersig="+Sig+"&random=99999999&contenttype=json";

        JSONObject param = new JSONObject();
        param.put("From_Account",fromusername);
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
        Integer liked_num;
        if (touser != null) {
            liked_num = touser.getLikedBy() + 1;
        }
        else{
            liked_num = 1;
        }
        Update update = Update.update("liked_by",liked_num);
        mongoTemplate.updateFirst(query,update, "users");



        Query query1 = new Query(Criteria.where("username").is(fromusername));
        User fromuser = mongoTemplate.findOne(query1, User.class);
        Integer like_num;
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

    public void MeetingNotlove(String fromusername,String tousername) {
        Query query = new Query(Criteria.where("username").is(tousername));
        User touser = mongoTemplate.findOne(query, User.class);
        List<String> touser_nomeetinglist = new ArrayList<>();
        if (touser != null) {
            touser_nomeetinglist = touser.getNomeetinglist();
            if(touser_nomeetinglist == null)
                touser_nomeetinglist = new ArrayList<>();
        }
        touser_nomeetinglist.add(fromusername);
        Update update_nomeetinglist = Update.update("nomeetinglist",touser_nomeetinglist);
        mongoTemplate.updateFirst(query,update_nomeetinglist, "users");


        Query query1 = new Query(Criteria.where("username").is(fromusername));
        User fromuser = mongoTemplate.findOne(query1, User.class);
        List<String> fromuser_nomeetinglist = new ArrayList<>();
        if (fromuser != null) {
            fromuser_nomeetinglist = fromuser.getNomeetinglist();
            if(fromuser_nomeetinglist == null)
                fromuser_nomeetinglist = new ArrayList<>();
        }
        fromuser_nomeetinglist.add(tousername);
        Update update_nomeetinglist1 = Update.update("nomeetinglist",fromuser_nomeetinglist);
        mongoTemplate.updateFirst(query1,update_nomeetinglist1, "users");
    }
}
