package com.atb.myblog.service;

import com.atb.myblog.po.Comment;

import java.util.List;

/**
 * Created by limi on 2017/10/22.
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
