package com.gker.gkerlove.service;

import com.gker.gkerlove.bean.Post;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.common.Page;
import com.gker.gkerlove.bean.dto.PostDto;
import com.gker.gkerlove.bean.dto.UserDto;
import com.gker.gkerlove.exception.GKerLoveException;
import com.mongodb.client.result.DeleteResult;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    @Resource
    MongoTemplate mongoTemplate;
    @Resource
    UserService userService;

    public PostDto addPost(User user, PostDto postDto) {
        postDto.setId(UUID.randomUUID().toString());
        postDto.setTime(LocalDateTime.now());
        Post post = new Post();
        BeanUtils.copyProperties(postDto, post);
        post.setUserId(user.getId());
        mongoTemplate.save(post);
        return postDto;
    }

    public void deletePost(User user, String id) {
        DeleteResult result = mongoTemplate.remove(new Query(Criteria.where("id").is(id).and("user_id").is(user.getId())), Post.class);
        if (result.getDeletedCount() != 1) {
            throw new GKerLoveException("删除失败");
        }
    }

    public Page<PostDto> retrieve(Integer pageNumber, Integer pageSize, String userId) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "time"));
        if (userId != null) {
            query.addCriteria(Criteria.where("userId").is(userId));
        }
        long total = mongoTemplate.count(query, Post.class);
        if (pageNumber != null && pageSize != null) {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
            query.with(pageRequest);
        }
        List<Post> postList = mongoTemplate.find(query, Post.class);
        List<PostDto> postDtoList = new ArrayList<>();
        for (Post post : postList) {
            PostDto postDto = new PostDto();
            BeanUtils.copyProperties(post, postDto);
            UserDto postUserDto = new UserDto();
            User postUser = userService.getById(userId);
            BeanUtils.copyProperties(postUser, postUserDto);
            postDto.setUser(postUserDto);
            List<PostDto.Comment> commentDtoList = new ArrayList<>();
            for (Post.Comment comment : post.getCommentList()) {
                PostDto.Comment commentDto = new PostDto.Comment();
                BeanUtils.copyProperties(comment, commentDto);
                UserDto commentUserDto = new UserDto();
                User commentUser = userService.getById(comment.getUserId());
                BeanUtils.copyProperties(commentUser, commentUserDto);
                commentDto.setUser(commentUserDto);
            }
            postDto.setCommentList(commentDtoList);
            postDtoList.add(postDto);
        }
        return new Page<>(total, postDtoList);
    }
}