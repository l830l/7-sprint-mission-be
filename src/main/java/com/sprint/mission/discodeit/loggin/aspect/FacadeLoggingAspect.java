package com.sprint.mission.discodeit.loggin.aspect;

import com.sprint.mission.discodeit.loggin.support.LogArgumentSanitizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class FacadeLoggingAspect {

  private final LogArgumentSanitizer logArgumentSanitizer;

  // Pointcut : Service 계층 전체
  @Pointcut("execution(public * com.sprint.mission.discodeit.facade..*(..))")
  public void facadeMethods() {
  }

  @Around("facadeMethods()")
  public Object logService(ProceedingJoinPoint pjp) throws Throwable {
    MethodSignature signature = (MethodSignature) pjp.getSignature();
    String className = signature.getDeclaringTypeName();
    String methodName = signature.getName();

    // 진입 로그
    if (log.isDebugEnabled()) {
      log.debug("➡️ [FACADE] {}.{} 진입", className, methodName);
      log.debug("📌 [FACADE] 파라미터: {}", logArgumentSanitizer.sanitizeArgs(pjp.getArgs()));
    }

    try {
      Object result = pjp.proceed();
      if (log.isDebugEnabled()) {
        log.debug("✅ [FACADE] {}.{} 정상 종료", className, methodName);
      }

      return result;
    } catch (Exception e) {
      // 예외 로그 (비즈니스 추적용)
      log.debug(
          "❌ [FACADE] {}.{} 예외 발생 - type={}, message={}",
          className, methodName, e.getClass().getSimpleName(), e.getMessage()
      );
      throw e;
    }
  }
}
