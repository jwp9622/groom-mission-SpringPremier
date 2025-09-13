package com.example.SpringPremier.domain.auth.service;

import com.example.SpringPremier.domain.auth.dto.AuthRequest;
import com.example.SpringPremier.domain.auth.dto.AuthResponse;
import com.example.SpringPremier.domain.auth.role.Role;
import com.example.SpringPremier.domain.members.dto.MemberDto;
import com.example.SpringPremier.domain.members.entity.Member;
import com.example.SpringPremier.domain.members.repository.MemberRepository;
import com.example.SpringPremier.global.advise.CustomValidationException;
import com.example.SpringPremier.global.jwt.JwtProvider;
import com.example.SpringPremier.global.jwt.entity.RefreshToken;
import com.example.SpringPremier.global.jwt.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public Member register(MemberDto dto) {

        if (memberRepository.findByMemberId(dto.getMemberId()).isPresent()) {
            throw new CustomValidationException ("이미 등록된 아이디입니다.");
        }

        //권한 정보 가져오기
        Role role = Role.valueOf(dto.getRole());

        //엔티티로 넘기기
        Member entity = dto.toEntity(role);

        //비밀번호 암호화
        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        //저장하기
        Member saved = memberRepository.save(entity);

        //Member member = new Member();
        //member.setUsername(request.getUsername());
        //member.setPassword(passwordEncoder.encode(request.getPassword()));
        //memberRepository.save(member);

        return saved;
    }

    @Override
    public AuthResponse login(AuthRequest request) {


        Member member = memberRepository.findByMemberId(request.getUsername()).orElse(null);
        if(member == null)  {
            throw new CustomValidationException ("등록된 사용자가 없습니다.");
        }

        if (!bCryptPasswordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomValidationException ("비밀번호가 일치하지 않습니다.");
        }

        // username / password 검증
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        //권한 정보 가져오기
        request.setRole(String.valueOf(member.getRole()));

        //토큰 생성
        String accessToken = jwtProvider.createAccessToken(request);
        String refreshToken = jwtProvider.createRefreshToken(request);


        System.out.println("    ===========login======> accessToken="+accessToken);
        System.out.println("    ===========login======> refreshToken="+refreshToken);
        //만료일계산
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(jwtProvider.getAccessTokenExpiration() / 1000);

        // 리프레시 토큰 저장 또는 갱신
        refreshTokenRepository.findByMember_MemberId(request.getUsername())
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken, expiryDate),
                        () -> refreshTokenRepository.save(new RefreshToken(member, refreshToken, expiryDate))
                );

        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public void logout(HttpServletRequest request) {

        System.out.println("============logout===="+request);

        String token = jwtProvider.extractToken(request);


        System.out.println("============logout====token="+token);

        if (token != null && jwtProvider.isValidToken(token)) {
            System.out.println("============logout====222");
            String username = jwtProvider.getUsernameFromToken(token);
            System.out.println("============logout====333");
            long expiration = jwtProvider.getExpiration(token);
            System.out.println("============logout====444");
            System.out.println("============logout====userId="+username);

            refreshTokenRepository.deleteByMember_MemberId(username);
            System.out.println("============logout====555");

            //blacklistService.blacklistToken(token, expiration);
        }else{
            System.out.println("============logout====666");
            throw new CustomValidationException ("유효하지 않은 토큰입니다.");
        }

    }

    @Override
    public AuthResponse reissue(String refreshToken) {
        
        //만료 여부 체크
        if (!jwtProvider.isValidToken(refreshToken)) {
            throw new CustomValidationException ("Refresh Token이 유효하지 않습니다.");
        }
        //기존 토큰에서 정보 가져오기
        String username = jwtProvider.getUsernameFromToken(refreshToken);

        //회원 정보 확인
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new CustomValidationException ("등록된 사용자가 없습니다."));

        //리프레시 토큰 찾기
        RefreshToken savedToken = refreshTokenRepository.findByMember_MemberId(username)
                .orElseThrow(() -> new CustomValidationException ("저장된 리프레시 토큰이 없습니다."));

        //토큰 일치 여부 확인
        if (!savedToken.getToken().equals(refreshToken)) {
            throw new CustomValidationException ("토큰이 일치하지 않습니다.");
        }

        //저장할 정보 세팅
        AuthRequest request = new AuthRequest();
        request.setUsername(username);
        request.setUsername2(member.getUsername());
        request.setRole(String.valueOf(member.getRole()));

        // 새로운 AccessToken 발급
       // return jwtProvider.createAccessToken(authRequest);

        //토큰 생성
        String accessToken = jwtProvider.createAccessToken(request);
        String refreshToken2 = jwtProvider.createRefreshToken(request);

        //만료일계산
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(jwtProvider.getRefreshTokenExpiration() / 1000);

        // 리프레시 토큰 저장 또는 갱신
        refreshTokenRepository.findByMember_MemberId(request.getUsername())
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken, expiryDate),
                        () -> refreshTokenRepository.save(new RefreshToken(member, refreshToken, expiryDate))
                );

        // 클라이언트에 응답
        return new AuthResponse(accessToken, refreshToken2);

    }
}
