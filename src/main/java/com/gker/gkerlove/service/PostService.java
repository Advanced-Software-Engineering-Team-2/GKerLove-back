package com.gker.gkerlove.service;

import com.gker.gkerlove.bean.Post;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.common.Page;
import com.gker.gkerlove.bean.dto.PostDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    @Resource
    MongoTemplate mongoTemplate;

    public void addPost(User user, PostDto postDto) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        post.setUserId(user.getId());
        BeanUtils.copyProperties(postDto, post);
        mongoTemplate.save(post);
    }

    public Page<Post> retrieve(Integer pageNumber, Integer pageSize) {
        Query query = new Query();
        long total = mongoTemplate.count(query, Post.class);
        if (pageNumber != null && pageSize != null) {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
            query.with(pageRequest);
        }
        List<Post> postList = mongoTemplate.find(query, Post.class);
        return new Page<>(total, postList);
    }
}