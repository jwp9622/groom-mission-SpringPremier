package com.example.SpringPremier.global.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect // 이 클래스가 Aspect임을 선언
@Component // 스프링 빈으로 등록
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass()); // 로거 인스턴스 생성

    // 패키지 내의 모든 메서드에 적용될 Pointcut을 정의
    @Pointcut("within(com.example..*)")
    public void applicationPackagePointcut() {
        // 이 메서드는 Pointcut을 정의하기 위한 것이므로 구현은 필요 없습니다.
    }

    // 실제 Advice. 위에서 정의한 Pointcut에 대해 동작
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {


        // 디버그 모드일 때 메서드 진입 로그 출력
        if (logger.isDebugEnabled()) {
            logger.debug("진입: {}.{}() 인수 = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            // 실제 메서드 실행
            Object result = joinPoint.proceed();
            // 디버그 모드일 때 메서드 종료 로그 출력
            if (logger.isDebugEnabled()) {
                logger.debug("종료: {}.{}() 결과 = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException 발생 시 로그 출력
            logger.error("잘못된 인수: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e; // 예외를 다시 던져서 처리를 위임
        }



    }
}
