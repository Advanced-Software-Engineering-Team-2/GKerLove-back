package com.gker.gkerlove.service;


import com.alibaba.fastjson.JSONObject;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.dto.UserDto;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.util.HttpUtils;
import com.tencentyun.TLSSigAPIv2;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeetService {
    @Resource
    MongoTemplate mongoTemplate;

    public List<UserDto> getUserList(User currentUser, String gender, Integer minAge, Integer maxAge, String city, String institute) {
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("username").ne(currentUser.getUsername()));
        if (StringUtils.hasLength(gender)) {
            criteriaList.add(Criteria.where("gender").is(gender));
        }
        if (minAge != null) {
            criteriaList.add(Criteria.where("age").gte(minAge));
        }
        if (maxAge != null) {
            criteriaList.add(Criteria.where("age").lte(maxAge));
        }
        if (StringUtils.hasLength(city)) {
            criteriaList.add(Criteria.where("city").is(city));
        }
        if (StringUtils.hasLength(institute)) {
            criteriaList.add(Criteria.where("institute").is(institute));
        }
        MatchOperation matchStage = Aggregation.match(new Criteria().andOperator(criteriaList));
        SampleOperation sampleStage = Aggregation.sample(10);
        Aggregation aggregation = Aggregation.newAggregation(matchStage, sampleStage);
        AggregationResults<User> output = mongoTemplate.aggregate(aggregation, "users", User.class);
        List<User> userList = output.getMappedResults();

        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Transactional
    public String likeSomeone(User currentUser, String userId) {
        User likedUser = mongoTemplate.findById(userId, User.class);
        if (likedUser == null) throw new GKerLoveException("用户不存在");

        // 将对应用户添加到我的喜欢列表中
        if (!currentUser.getLikeUserIdList().contains(userId)) {
            currentUser.getLikeUserIdList().add(userId);
        }
        currentUser.setLikes(currentUser.getLikeUserIdList().size());
        mongoTemplate.save(currentUser);

        // 将我添加到其人气列表中
        if (!likedUser.getLikedByUserIdList().contains(userId)) {
            likedUser.getLikedByUserIdList().add(currentUser.getId());
        }
        likedUser.setLikedBy(likedUser.getLikedByUserIdList().size());
        mongoTemplate.save(likedUser);

        TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
        String Sig = api.genUserSig("administrator", 60);
        String url = "https://console.tim.qq.com/v4/sns/friend_add?sdkappid=1600009914&identifier=administrator&usersig=" + Sig + "&random=99999999&contenttype=json";
        JSONObject param = new JSONObject();
        param.put("From_Account", currentUser.getUsername());
        List<JSONObject> frienditems = new ArrayList<>();
        JSONObject frienditem = new JSONObject();
        frienditem.put("To_Account", likedUser.getUsername());
        frienditem.put("GroupName", "我喜欢的人");
        frienditem.put("AddSource", "AddSource_Type_Meeting");
        frienditem.put("AddWording", "我将你列为了我喜欢的人，如果对我的个人信息感兴趣的话，就将我添加为好友开始聊天吧！");
        frienditems.add(frienditem);
        param.put("AddFriendItem", frienditems);

        return HttpUtils.sendPost(url, param.toJSONString());
    }

    public void dislikeSomeone(User currentUser, String userId) {
        User dislikedUser = mongoTemplate.findById(userId, User.class);
        if (dislikedUser == null) throw new GKerLoveException("用户不存在");

        // 将对应用户从我的喜欢列表中移除
        currentUser.getLikeUserIdList().remove(userId);
        currentUser.setLikes(currentUser.getLikeUserIdList().size());
        mongoTemplate.save(currentUser);

        // 将我从其人气列表中移除
        dislikedUser.getLikedByUserIdList().remove(currentUser.getId());
        dislikedUser.setLikedBy(dislikedUser.getLikedByUserIdList().size());
        mongoTemplate.save(dislikedUser);
    }

    public UserDto getUserById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        // 不返回用户喜欢的用户列表和喜欢该用户的用户列表,仅返回人气和喜欢数
        query.fields().exclude("likeUserIdList");
        query.fields().exclude("likedByUserIdList");
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) return null;
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public List<UserDto> getMyLikes(User currentUser) {
        List<String> likeUserIdList = currentUser.getLikeUserIdList();
        Query query = new Query(Criteria.where("id").in(likeUserIdList));
        query.fields().include("username", "avatar");
        List<User> likeUserList = mongoTemplate.find(query, User.class);
        ArrayList<UserDto> likeUserDtoList = new ArrayList<>();
        for (User likeUser : likeUserList) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(likeUser, userDto);
            likeUserDtoList.add(userDto);
        }
        return likeUserDtoList;
    }

    public List<UserDto> getWhoLikeMe(User currentUser) {
        List<String> likedByUserIdList = currentUser.getLikedByUserIdList();
        Query query = new Query(Criteria.where("id").in(likedByUserIdList));
        query.fields().include("username", "avatar");
        List<User> likedByUserList = mongoTemplate.find(query, User.class);
        ArrayList<UserDto> likedByUserDtoList = new ArrayList<>();
        for (User likedByUser : likedByUserList) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(likedByUser, userDto);
            likedByUserDtoList.add(userDto);
        }
        return likedByUserDtoList;
    }

}
