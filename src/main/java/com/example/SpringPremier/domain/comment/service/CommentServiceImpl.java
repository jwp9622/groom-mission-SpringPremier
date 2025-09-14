package com.example.SpringPremier.domain.comment.service;

import com.example.SpringPremier.domain.comment.dto.CommentDto;
import com.example.SpringPremier.domain.comment.entity.Comment;
import com.example.SpringPremier.domain.comment.repository.CommentRepository;
import com.example.SpringPremier.domain.members.entity.Member;
import com.example.SpringPremier.domain.members.repository.MemberRepository;
import com.example.SpringPremier.domain.posts.entity.Post;
import com.example.SpringPremier.domain.posts.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<CommentDto> getList(Long post_id){


        return commentRepository.findByPost_id(post_id) // 1. 특정 게시글의 댓글 조회
                .stream()   // 2. Stream으로 변환
                .map(CommentDto::createCommentDto) // 3. DTO 변환
                .collect(Collectors.toList());  // 4. 리스트로 수집

    }

    @Override
    public CommentDto create(Long post_id, CommentDto dto){
        
        //비밀번호 암호화
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        // 게시글 조회 및 예외 발생
        Post posts = postRepository.findById(post_id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! " +
                        "대상 게시글이 없습니다."));

        Member member = memberRepository.findByMemberId(dto.getMember_memberId()).orElse(null);

        // 댓글 엔티티 생성
        Comment boardComment = Comment.createComment(dto, posts, member);



        // 댓글 엔티티를 DB로 저장
        Comment created = commentRepository.save(boardComment);

        // DTO로 변환하여 반환
        return CommentDto.createCommentDto(created);


    }

    @Override
    public CommentDto update(Long id, CommentDto dto){

        // 1. 댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패!" +
                        "대상 댓글이 없습니다."));
        // 2. 댓글 수정
        target.patch(dto);
        // 3. DB로 갱신
        Comment updated = commentRepository.save(target);

        // 4. 댓글 엔티티를 DTO로 변환 및 반환
        return CommentDto.createCommentDto(updated);

    }
    @Override
    public CommentDto delete(Long id){

        // 1. 댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패! " +
                        "대상이 없습니다."));

        // 2. 댓글 삭제
        commentRepository.delete(target);

        // 3. 삭제 댓글을 DTO로 변환 및 반환
        return CommentDto.createCommentDto(target);
    }

    @Override
    public CommentDto chkPostidAndId(Long post_id, Long id){
        // 댓글 조회 및 예외 발생
        Comment target = commentRepository.findByPost_idAndId(post_id, id)
                .orElseThrow(() -> new IllegalArgumentException("대상 댓글이 없습니다."));

        return CommentDto.createCommentDto(target);
    }



    @Override
    public boolean passwordCheck(CommentDto dto){

        // 댓글 조회 및 예외 발생
        Comment target = commentRepository.findByPost_idAndId(dto.getPost_id(), dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("대상 댓글이 없습니다."));

        if(target != null) {
            //request와 dto비밀번호 일치여부 확인
            return bCryptPasswordEncoder.matches(dto.getPassword(), target.getPassword());
        }
        return false;
    }



}




