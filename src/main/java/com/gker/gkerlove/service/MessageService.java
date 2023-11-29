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
        Criteria criteriaInitiatorId = Criteria.where("initiatorId").is(userId);
        Criteria criteriaRecipientId = Criteria.where("recipientId").is(userId);
        List<Session> sessionList = mongoTemplate.find(new Query(new Criteria().orOperator(criteriaInitiatorId, criteriaRecipientId)), Session.class);
        List<SessionDto> sessionDtoList = new ArrayList<>();
        for (Session session : sessionList) {
            sessionDtoList.add(session2SessionDto(session));
        }
        return sessionDtoList;
    }

    private Session getSession(String initiatorId, String recipientId) {
        Criteria criteriaInitiatorId = Criteria.where("initiatorId").is(initiatorId).and("recipientId").is(recipientId);
        Criteria criteriaRecipientId = Criteria.where("initiatorId").is(recipientId).and("recipientId").is(initiatorId);
        return mongoTemplate.findOne(new Query(new Criteria().orOperator(criteriaInitiatorId, criteriaRecipientId)), Session.class);
    }

    private SessionDto session2SessionDto(Session session) {
        SessionDto sessionDto = new SessionDto();
        BeanUtils.copyProperties(session, sessionDto);
        String initiatorId = session.getInitiatorId();
        User initiator = userService.getById(initiatorId);
        UserDto initiatorDto = new UserDto();
        BeanUtils.copyProperties(initiator, initiatorDto);
        sessionDto.setInitiator(initiatorDto);
        String recipientId = session.getRecipientId();
        User recipient = userService.getById(recipientId);
        UserDto recipientDto = new UserDto();
        BeanUtils.copyProperties(recipient, recipientDto);
        sessionDto.setRecipient(recipientDto);
        return sessionDto;
    }

    public SessionDto initSession(String initiatorId, String recipientId) {
        Session historySession = getSession(initiatorId, recipientId);
        if (historySession != null) return session2SessionDto(historySession);
        if (userService.getById(initiatorId) == null || userService.getById(recipientId) == null)
            throw new GKerLoveException("用户不存在");
        Session session = new Session();
        session.setId(String.valueOf(UUID.randomUUID()));
        session.setInitiatorId(initiatorId);
        session.setRecipientId(recipientId);
        session.setLastUpdated(LocalDateTime.now());
        mongoTemplate.save(session);
        return session2SessionDto(session);
    }
}