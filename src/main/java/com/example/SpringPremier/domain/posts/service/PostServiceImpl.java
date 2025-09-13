package com.example.SpringPremier.domain.posts.service;

import com.example.SpringPremier.domain.boards.service.BoardCheckServiceImpl;
import com.example.SpringPremier.domain.posts.entity.Post;
import com.example.SpringPremier.domain.boards.entity.Board;
import com.example.SpringPremier.domain.members.entity.Member;
import com.example.SpringPremier.domain.boards.repository.BoardRepository;
import com.example.SpringPremier.domain.posts.repository.PostRepository;
import com.example.SpringPremier.domain.members.repository.MemberRepository;
import com.example.SpringPremier.domain.posts.dto.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PostServiceImpl implements PostService{

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BoardCheckServiceImpl boardCheckService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    //리스트를 entity 번호 다시 계산
    public List<PostDto> pagenation(Page<Post> list, int total, int page, int size, String keyword){

        AtomicInteger counter = new AtomicInteger();

        List<PostDto> dtoList = list.getContent().stream().map(board -> {
            int i = counter.getAndIncrement(); //전체 게시물 수
            Post post2 = (Post) list.getContent().get(i);
            int displayNumber = Math.toIntExact(total - ((long) (page - 1) * size) - i); //계산하여 log ->int 변환
            String param = "page="+page+"&keyword="+keyword;
            return PostDto.fromEntity(post2, displayNumber, param);
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    //일반 목록
    public Page<PostDto> getList(String board_code, String keyword, int page, int size){

        //현재페이지에서 0이면 기본 1, 그렇지 않으면 -1
        //pageable 선언
        Pageable pageable = PageRequest.of(page-1, size,
                Sort.by(Sort.Order.desc("ref"),Sort.Order.asc("step"), Sort.Order.asc("seq") ) );


        //db 값을 pageable 가져오기
        Page<Post> list;
        //list = getListList(board_code, keyword, page, size, pageable);
        if(keyword != null) {
            list = postRepository.findByBoard_codeAndTitleContaining(board_code, keyword, pageable);
        }else{
            list = postRepository.findByBoard_code(board_code, pageable);
        }


        //총 개시물수
        int total = Math.toIntExact(list.getTotalElements());

        //게시물 번호 역순으로 번호부여,  new 아이콘 표시
        String finalKeyword = (keyword != null) ? keyword : "";


        //게시물 번호 역순으로 번호부여,  new 아이콘 표시
        List<PostDto> dtoList  = pagenation( list, total, page, size, keyword);

        //dto, pageable, counts 더하기
        Page<PostDto> dtoPage = new PageImpl<>(dtoList, pageable, list.getTotalElements());

        //일반 목록 - page 리턴
        return dtoPage;

    }


    @Override
    //내용보기
    public Post getRead(String board_code, Long id){

        //게시판 등록여부 체크
        boardCheckService.boardCheck(board_code);

        return postRepository.findByBoard_codeAndId(board_code, id).orElse(null);
    }

    @Override
    //등록폼
    public Post getCreateForm(String board_code, Long id, PostDto dto){

        //게시판 등록여부 체크
        boardCheckService.boardCheck(board_code);

        //답변이면 부모 게시물 가져오기
        Post parent_post;
        if(dto.getMode().equals("reply")) {
            parent_post = postRepository.findById(id).orElse(null);
        }else{
            parent_post = null;
        }

        return parent_post;
    }

    @Override
    //등록 업데이트
    public Post create(String board_code, Long id, PostDto dto){

        //게시판 등록여부 체크
        boardCheckService.boardCheck(board_code);

        //게시판 매니저에서 정보 가져오기
        Board board = boardRepository.findByCode(board_code).orElse(null);

        //회원 정보 가져오기
        Member member = memberRepository.findByMemberId(dto.getMember_memberId()).orElse(null);

        //비밀번호 암호화
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        //답변글 체크 변수 삽입
        if(dto.getMode().equals("reply")) {

            long ref;
            long step;
            long seq;
            long pre_step;

            Post parent_post;

            //부모 게시물 가져오기
            parent_post = postRepository.findById(id).orElse(null);
            ref = parent_post.getRef();
            pre_step = parent_post.getStep();
            step = pre_step+1;

            //System.out.println("parent_post.getRef="+parent_post.getRef());

            //ref, step에서 seq max값 가져오기
            Post seq_old = postRepository.findTop1ByBoard_codeAndRefAndStepOrderBySeqDesc(board_code, ref, step).orElse(null);
            if(seq_old == null){
                seq = 1;
            }else{
                seq = seq_old.getSeq()+1;
            }

            // seq값보다 큰것은 +1 업데이트
            postRepository.updateSeq(board_code, dto.getId(), ref, step);

            //dto에 값 넣기
            dto.setRef(ref);
            dto.setStep(step);
            dto.setSeq(seq);

            //답변시에는 id 값을 없애주어야 등록됨, 아니면 수정된다.
            dto.setId(null);
        }
        //엔티티로 넘기기
        Post post = dto.toEntity(board, member);

        //글 등록저장후 아이디값 가져오기
        Post saved = postRepository.save(post);

        //등록시에는 등록된 ref 변경
        if(dto.getMode().equals("create")) {
            postRepository.updateRef(saved.getId());
        }

        return saved;

    }


    @Override
    //수정폼
    public Post getEditForm(String board_code, Long id){

        //게시판 등록여부 체크
        boardCheckService.boardCheck(board_code);

        //글 등록여부 체크
        Post target = postRepository.findByBoard_codeAndId(board_code, id).orElse(null);
        if(target == null || id != target.getId()){
            return null;
        }


        //Board updated = boardRepository.save(board);
        return target;
    }

    @Override
    //수정하기
    @Transactional
    public Post edit(String board_code, Long id, PostDto dto){

        //게시판 등록여부 체크
        boardCheckService.boardCheck(board_code);

        //존재여부 체크
        Post target = postRepository.findByBoard_codeAndId(board_code, id).orElse(null);
        if(target == null || id != target.getId()){
            return null;
        }

        //게시판 매니저에서 정보 가져오기
        Board board = boardRepository.findByCode(board_code).orElse(null);

        //회원 정보 가져오기
        Member member = memberRepository.findByMemberId(dto.getMember_memberId()).orElse(null);

        //엔티티로 넘기기
        Post postentity = dto.toEntity(board, member);

        //업데이트
        Post updated = postRepository.save(postentity);
        return updated;
    }


    @Override
    //비밀번호
    public String password(String board_code, Long id, String mode, PostDto dto){

        //게시판 등록여부 체크
        boardCheckService.boardCheck(board_code);


        //글 등록여부 체크
        Post target = postRepository.findByBoard_codeAndId(board_code, id).orElse(null);
        if(target == null ){
            return "no_post";
        }

        //비밀번호 확인
        boolean pass_chk = bCryptPasswordEncoder.matches(dto.getPassword(), target.getPassword());

        //2. 글존재여부 확인후 삭제
        //Post pass_chk = postRepository.findByBoard_codeAndIdAndPassword(board_code, id, dto.getPassword()).orElse(null);
        if(!pass_chk){
            return "not_equal_password";
        }else{
            if(mode.equals("delete")){
                postRepository.delete(target);
                return "success_delete";
            }
        }

        return "";
    }

    @Override
    //삭제
    public Post delete(String board_code, Long id, PostDto dto){

        //게시판 등록여부 체크
        boardCheckService.boardCheck(board_code);

        //글 등록여부 체크
        Post target = postRepository.findByBoard_codeAndId(board_code, id).orElse(null);
        if(target == null ){
            return null;
        }

        //2. 글존재여부 확인후 삭제
        Post pass_chk = postRepository.findByBoard_codeAndIdAndPassword(board_code, id, dto.getPassword()).orElse(null);

        if(pass_chk == null ){
            return null;
        }else{
            postRepository.delete(target);
        }
        return target;
    }

    @Override
    @Transactional
    public List<Post> createBoards(List<PostDto> dtos) {


        // 1. dto 묶음을 엔티티 묶음으로 변환하기
        List<Post> articleList = dtos.stream()
                .map(dto -> dto.toEntity(
                        boardRepository.findById(dto.getId()).orElse(null),
                        memberRepository.findByMemberId(dto.getMember_memberId()).orElse(null) ))
                .collect(Collectors.toList());


        // 2. 엔티티 묶음을 DB에 저장하기
        articleList.stream()
                .forEach(article -> postRepository.save(article));

        // 3. 강제 예외 발생시키기
        boardRepository.findById(-1L)
                .orElseThrow(() -> new IllegalArgumentException("결제 실패!"));

        // 4. 결과값 반환하기
        return articleList;

    }


}
