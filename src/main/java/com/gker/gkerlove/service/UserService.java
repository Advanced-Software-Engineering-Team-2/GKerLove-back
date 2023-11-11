package com.gker.gkerlove.service;

import com.gker.gkerlove.bean.Page;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.UserDTO;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Resource
    MongoTemplate mongoTemplate;
    @Resource
    CodeService codeService;

    public String login(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (username.length() < 4 || username.length() > 20) throw new GKerLoveException("用户名长度必须在4-20之间");
        if (password.length() < 6 || password.length() > 50) throw new GKerLoveException("密码长度必须在6-50之间");
        Query query = new Query(Criteria.where("username").is(username));
        User userDetails = mongoTemplate.findOne(query, User.class);
        if (userDetails == null) throw new GKerLoveException("用户名或密码错误");
        if (!MD5Util.encrypt(password).equals(userDetails.getPassword()))
            throw new GKerLoveException("用户名或密码错误");
        return JwtUtils.getToken(userDetails);
    }

    public void register(User user, String code) {
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        if (username.length() < 4 || username.length() > 20) throw new GKerLoveException("用户名长度必须在4-20之间");
        if (password.length() < 6 || password.length() > 50) throw new GKerLoveException("密码长度必须在6-50之间");
        if (email.length() > 50) throw new GKerLoveException("邮箱长度不能大于50");
        Query query = new Query(Criteria.where("username").is(username));
        long count = mongoTemplate.count(query, User.class);
        if (count >= 1) throw new GKerLoveException("用户名重复");
        codeService.checkCode(email, code);
        user.setPassword(MD5Util.encrypt(password));
        user.setId(String.valueOf(UUID.randomUUID()));
        user.setCreateTime(LocalDateTime.now());
        mongoTemplate.save(user, "users");
    }

    public UserDTO getById(String id) {
        User user = mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), User.class);
        assert user != null;
        UserDTO UserDTO = new UserDTO();
        BeanUtils.copyProperties(user, UserDTO);
        return UserDTO;
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

    public void update(User user) {
        mongoTemplate.save(user);
    }

}