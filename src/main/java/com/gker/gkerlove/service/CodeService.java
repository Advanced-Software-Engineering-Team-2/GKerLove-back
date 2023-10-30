package com.gker.gkerlove.service;

import com.gker.gkerlove.bean.MailRequest;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.exception.GKerLoveException;
import com.gker.gkerlove.util.RandomUtil;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CodeService {
    @Resource
    MongoTemplate mongoTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    MailService mailService;

    public void sendCode(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        long count = mongoTemplate.count(query, User.class);
        if (count >= 1) throw new GKerLoveException("此邮箱已被注册过");
        String code = getCode(email);
        MailRequest mailRequest = new MailRequest();
        mailRequest.setSubject("邮箱验证码");
        mailRequest.setSendTo(email);
        mailRequest.setText("感谢注册果壳之恋，您的验证码为：" + code + "，在5分钟内有效。");
        mailService.sendSimpleMail(mailRequest);
    }

    public String getCode(String email) {
        Long expire = stringRedisTemplate.getExpire(email);
        if (expire != null && expire > 300 - 35) throw new GKerLoveException("请等待30秒后重试");
        String code = RandomUtil.getSixBitRandom();
        stringRedisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
        return code;
    }

    public void checkCode(String email, String code) {
        String correctCode = stringRedisTemplate.opsForValue().get(email);
        if (code == null || !code.equals(correctCode)) throw new GKerLoveException("邮箱验证码错误");
        stringRedisTemplate.delete(email);
    }
}