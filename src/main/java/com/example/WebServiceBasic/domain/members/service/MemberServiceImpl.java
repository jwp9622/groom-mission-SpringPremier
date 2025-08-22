package com.example.WebServiceBasic.domain.members.service;

import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.members.repository.MemberRepository;
import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.auth.role.Role;
import com.example.WebServiceBasic.domain.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public List<MemberDto> pagenation( Page<Member> list, int total, int page, int size, String keyword){

        AtomicInteger counter = new AtomicInteger();

        List<MemberDto> dtoList = list.getContent().stream().map(board -> {
            int i = counter.getAndIncrement(); //전체 게시물 수
            Member member2 = (Member) list.getContent().get(i);
            int displayNumber = Math.toIntExact(total - ((long) (page - 1) * size) - i); //계산하여 log ->int 변환
            String param = "keyword="+keyword+"&page="+page;
            return MemberDto.fromEntity(member2, displayNumber,param);
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public Page<MemberDto> getList(String keyword, int page, int size){

        //pageable 선언
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("id")));



        //db 값을 pageable 가져오기
        Page<Member> list;
        //list = getListList(keyword, page, size, pageable);
        if(keyword != null) {
            list = memberRepository.findByUsernameContaining(keyword, pageable);
        }else{
            list = memberRepository.findAll(pageable);
        }

        //총 개시물수
        int total = Math.toIntExact(list.getTotalElements());

        AtomicInteger counter = new AtomicInteger();

        //게시물 번호 역순으로 번호부여,  new 아이콘 표시
        List<MemberDto> dtoList  = pagenation( list, total, page, size, keyword);


        //dto, pageable, counts 더하기
        Page<MemberDto> dtoPage = new PageImpl<>(dtoList, pageable, list.getTotalElements());

        //일반 목록 - page 리턴
        return dtoPage;

    }

    @Override
    public Member getRead(Long id){

        //내용 가져오기
        //Member member = memberRepository.findById(id)
        //        .orElseThrow(()->new MemberNotFoundException("등록된 아이디가 없습니다."));

        Member member = memberRepository.findById(id).orElse(null);

        return member;
    }

    @Override
    public Member create(MemberDto dto){

        //권한 정보 가져오기
        Role role = Role.valueOf(dto.getRole());

        //엔티티로 넘기기
        Member entity = dto.toEntity(role);

        //비밀번호 암호화
        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        //저장하기
        Member saved = memberRepository.save(entity);

        return saved;
    }


    @Override
    public Member getEditForm(Long id){

        Member member = memberRepository.findById(id).orElse(null);
        return member;
    }

    @Override
    public Member edit(MemberDto dto){

        //엔티티로 넘기기
        //Role role = Role.valueOf(dto.getRole());

        //엔티티로 넘기기
        //Member entity = dto.toEntity(role);

        //비밀번호 암호화
        //entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        //내용확인후 있으면 수정하기
        //Member saved ;
        //if(entity != null) {
        //    saved = memberRepository.save(entity);
        //}else{
        //    saved = null;
        //}

        Member member = memberRepository.findByMemberId(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        member.setRole(Role.valueOf(dto.getRole()));
        memberRepository.save(member);

        return member;
    }

    @Override
    @Transactional
    public Member delete(Long id){

        Member member = memberRepository.findById(id).orElse(null);
        if(member != null){
            memberRepository.delete(member);
            return member;
        }else{
            return null;
        }
    }
}
