package com.gker.gkerlove.service;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            sessionDtoList.add(session2SessionDto(userId, session));
        }
        return sessionDtoList;
    }

    private Session getSession(String initiatorId, String recipientId) {
        Criteria criteriaInitiatorId = Criteria.where("initiatorId").is(initiatorId).and("recipientId").is(recipientId);
        Criteria criteriaRecipientId = Criteria.where("initiatorId").is(recipientId).and("recipientId").is(initiatorId);
        return mongoTemplate.findOne(new Query(new Criteria().orOperator(criteriaInitiatorId, criteriaRecipientId)), Session.class);
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