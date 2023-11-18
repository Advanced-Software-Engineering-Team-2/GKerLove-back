package com.gker.gkerlove.service;

import com.gker.gkerlove.bean.common.Page;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.dto.req.UpdateUserInfoReq;
import com.gker.gkerlove.constants.CityConstants;
import com.gker.gkerlove.constants.InstituteConstants;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.util.JwtUtils;
import com.gker.gkerlove.util.MD5Util;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Resource
    MongoTemplate mongoTemplate;
    @Resource
    CodeService codeService;

    public String login(String username, String password) {
        if (username == null || password == null) throw new GKerLoveException("用户名、密码不能为空");
        // 长度校验
        if (username.length() < 4 || username.length() > 20) throw new GKerLoveException("用户名长度必须在4-20之间");
        if (password.length() < 6 || password.length() > 50) throw new GKerLoveException("密码长度必须在6-50之间");
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) throw new GKerLoveException("用户名或密码错误");
        if (!MD5Util.encrypt(password).equals(user.getPassword())) throw new GKerLoveException("用户名或密码错误");
        return JwtUtils.getToken(user);
    }

    public void register(String username, String password, String email, String code) {
        if (username == null || password == null || email == null) {
            throw new GKerLoveException("用户名、密码、邮箱不能为空");
        }
        // 长度校验
        if (username.length() < 4 || username.length() > 20) throw new GKerLoveException("用户名长度必须在4-20之间");
        if (password.length() < 6 || password.length() > 50) throw new GKerLoveException("密码长度必须在6-50之间");
        if (email.length() > 50) throw new GKerLoveException("邮箱长度不能大于50");
        long count = mongoTemplate.count(new Query(Criteria.where("username").is(username)), User.class);
        if (count >= 1) throw new GKerLoveException("用户名重复");
        count = mongoTemplate.count(new Query(Criteria.where("email").is(email)), User.class);
        if (count >= 1) throw new GKerLoveException("邮箱已被注册");
        codeService.checkCode(email, code);
        User user = new User();
        // 对用户进行一些初始化
        user.setId(String.valueOf(UUID.randomUUID()));
        user.setUsername(username);
        user.setPassword(MD5Util.encrypt(password));
        user.setEmail(email);
        user.setCreateTime(LocalDateTime.now());
        user.setLikes(0);
        user.setLikedBy(0);
        mongoTemplate.save(user);
    }

    public User getById(String id) {
        return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), User.class);
    }

    public Page<User> retrieve(Integer pageNumber, Integer pageSize) {
        Query query = new Query();
        long total = mongoTemplate.count(query, User.class);
        if (pageNumber != null && pageSize != null) {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
            query.with(pageRequest);
        }
        List<User> userList = mongoTemplate.find(query, User.class);
        return new Page<>(total, userList);
    }

    public void updateInfo(User user, UpdateUserInfoReq updateUserInfoReq) {
        // 性别校验
        if (StringUtils.hasLength(updateUserInfoReq.getGender()) && !updateUserInfoReq.getGender().equals("男") && !updateUserInfoReq.getGender().equals("女"))
            throw new GKerLoveException("性别错误");
        // 年龄
        if (updateUserInfoReq.getAge() != null && (updateUserInfoReq.getAge() < 0 || updateUserInfoReq.getAge() > 200))
            throw new GKerLoveException("年龄错误");
        // 城市
        if (StringUtils.hasLength(updateUserInfoReq.getCity())) {
            System.out.println(updateUserInfoReq.getCity());
            if (updateUserInfoReq.getCity().length() > 20) throw new GKerLoveException("城市错误");
            if (!CityConstants.CITIES.contains(updateUserInfoReq.getCity())) throw new GKerLoveException("城市错误");
        }
        // 培养单位
        if (StringUtils.hasLength(updateUserInfoReq.getInstitute())) {
            if (updateUserInfoReq.getInstitute().length() > 20) throw new GKerLoveException("培养单位错误");
            if (!InstituteConstants.INSTITUTES.contains(updateUserInfoReq.getInstitute()))
                throw new GKerLoveException("培养单位错误");
        }
        // 自我介绍
        if (StringUtils.hasLength(updateUserInfoReq.getIntroduction()) && updateUserInfoReq.getIntroduction().length() > 50)
            throw new GKerLoveException("自我介绍长度不能超过50");
        BeanUtils.copyProperties(updateUserInfoReq, user);
        mongoTemplate.save(user);
    }

}