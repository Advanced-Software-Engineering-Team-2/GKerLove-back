package com.gker.gkerlove.service;

import com.gker.gkerlove.bean.Message;
import com.gker.gkerlove.bean.Session;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.dto.SessionDto;
import com.gker.gkerlove.bean.dto.UserDto;
import com.gker.gkerlove.exception.GKerLoveException;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    @Resource
    MongoTemplate mongoTemplate;

    @Resource
    UserService userService;

    public List<SessionDto> retrieveMessages(String userId) {
        List<Session> sessionList = mongoTemplate.find(new Query(new Criteria().orOperator(Criteria.where("initiatorId").is(userId), Criteria.where("recipientId").is(userId))), Session.class);
        List<SessionDto> sessionDtoList = new ArrayList<>();
        for (Session session : sessionList) {
            SessionDto sessionDto = session2SessionDto(userId, session);
            // 过滤掉viewed为true的消息
            sessionDto.setMessages(sessionDto.getMessages().stream().filter((message -> message.getViewed() == null || !message.getViewed())).toList());
            if (!sessionDto.getMessages().isEmpty()) {
                sessionDtoList.add(sessionDto);
            }
        }
        return sessionDtoList;
    }

    public SessionDto getChatHistory(User user, String sessionId) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(Criteria.where("initiatorId").is(user.getId()), Criteria.where("recipientId").is(user.getId())));
        query.addCriteria(Criteria.where("id").is(sessionId));
        Session session = mongoTemplate.findOne(query, Session.class);
        if (session == null) {
            throw new GKerLoveException("会话不存在");
        }
        return session2SessionDto(user.getId(), session);
    }

    private SessionDto session2SessionDto(String userId, Session session) {
        SessionDto sessionDto = new SessionDto();
        BeanUtils.copyProperties(session, sessionDto);
        String peerId;
        if (userId.equals(session.getInitiatorId())) {
            peerId = session.getRecipientId();
            sessionDto.setLastRead(session.getInitiatorLastRead());
            sessionDto.setPeerLastRead(session.getRecipientLastRead());
        } else {
            peerId = session.getInitiatorId();
            sessionDto.setLastRead(session.getRecipientLastRead());
            sessionDto.setPeerLastRead(session.getInitiatorLastRead());
        }
        User peer = userService.getById(peerId);
        UserDto peerDto = new UserDto();
        BeanUtils.copyProperties(peer, peerDto);
        sessionDto.setPeer(peerDto);
        return sessionDto;
    }
}