package com.example.SpringPremier.domain.comment.service;

import com.example.SpringPremier.domain.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getList(Long post_id);
    CommentDto create(Long post_id, CommentDto dto);
    CommentDto update(Long id, CommentDto dto);
    CommentDto delete(Long id);
    CommentDto chkPostidAndId(Long post_id, Long id);
    boolean passwordCheck(CommentDto dto);
}
