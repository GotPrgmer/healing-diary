package com.ssafy.healingdiary.domain.member.service;

import com.ssafy.healingdiary.domain.member.domain.Member;
import com.ssafy.healingdiary.domain.member.dto.*;
import com.ssafy.healingdiary.domain.member.repository.MemberRepository;
import com.ssafy.healingdiary.global.auth.PrincipalDetails;
import com.ssafy.healingdiary.global.auth.PrincipalDetailsService;
import com.ssafy.healingdiary.global.error.CustomException;
import com.ssafy.healingdiary.global.error.ErrorCode;
import com.ssafy.healingdiary.global.error.ErrorResponse;
import com.ssafy.healingdiary.global.jwt.CookieUtil;
import com.ssafy.healingdiary.global.jwt.JwtTokenizer;
import com.ssafy.healingdiary.global.jwt.TokenRegenerateRequest;
import com.ssafy.healingdiary.global.jwt.TokenRegenerateResponse;
import com.ssafy.healingdiary.global.redis.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static com.ssafy.healingdiary.global.error.ErrorCode.BAD_REQUEST;
import static com.ssafy.healingdiary.global.error.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final JwtTokenizer jwtTokenizer;

    private final PrincipalDetailsService principalDetailsService;

    private final PrincipalDetails principalDetails;

    private final RedisUtil redisUtil;

    private final CookieUtil cookieUtil;



    public MemberInfoResponse memberInfoFind(String providerEmail) {
        Member member = memberRepository.findMemberByProviderEmail(providerEmail);
        if(member == null){
            throw new CustomException(MEMBER_NOT_FOUND);
        }

        MemberInfoResponse foundMember =  MemberInfoResponse.of(member);

        return foundMember;
    }

    public MemberUpdateResponse memberUpdate(String providerEmail, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findMemberByProviderEmail(providerEmail);
//        Member member = memberRepository.findById(1L)
//                .orElseThrow(() -> {
//                    throw new CustomException(NOT_FOUND_USER);
//                });
        System.out.println("컴온");
        System.out.println(member.getProviderEmail());
        if(member == null){
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        member.updateMember(memberUpdateRequest);
        MemberUpdateResponse foundMember =  MemberUpdateResponse.of(member);

        return foundMember;
    }

    public NicknameCheckResponse nicknameCheck(NicknameCheckRequest nickname) {
        Member member = memberRepository.findMemberByNickname(nickname.getNickname());
        if(member == null){
            NicknameCheckResponse foundMember =  NicknameCheckResponse.of(false);
            return foundMember;
        }
        else{
            NicknameCheckResponse foundMember =  NicknameCheckResponse.of(true);
            System.out.println(foundMember);
            return foundMember;
        }

    }

    public ResponseEntity<?> reissue(TokenRegenerateRequest tokenRegenerateRequest, HttpServletRequest request
                                     ,Authentication authentication
    ) {
        //refreshToken얻어오는 방법
        Cookie[] cookies = request.getCookies();
        String refreshTokenCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenCookie = cookie.getValue();
                    break;
                }
            }
            if(refreshTokenCookie == null){
                throw new CustomException(BAD_REQUEST);
            }
        }
        else{
            throw new CustomException(BAD_REQUEST);

        }

        String refreshTokenInRedis = redisUtil.getToken(principalDetails.getUsername());

        if (ObjectUtils.isEmpty(refreshTokenInRedis)) {
            throw new CustomException(BAD_REQUEST);
        }

        if (!refreshTokenInRedis.equals(refreshTokenCookie)) {
            throw new CustomException(BAD_REQUEST);
        }

        //엑세스토큰 재발급
        String newAccessToken = jwtTokenizer.createAccessToken(jwtTokenizer.getUsernameFromToken(refreshTokenInRedis),
                jwtTokenizer.getRoleListFromToken(refreshTokenInRedis));
        //리프레시토큰 재발급
        String newRefreshToken = jwtTokenizer.createRefreshToken(jwtTokenizer.getUsernameFromToken(refreshTokenInRedis),
                jwtTokenizer.getRoleListFromToken(refreshTokenInRedis));
        String memberId = jwtTokenizer.getUsernameFromToken(newRefreshToken);

        redisUtil.dataExpirationsInput(memberId,newRefreshToken,7);
        TokenRegenerateResponse tokenRegenerateResponse = TokenRegenerateResponse.of(newAccessToken);




        return cookieUtil.TokenCookie(newRefreshToken, tokenRegenerateResponse);
    }

}
