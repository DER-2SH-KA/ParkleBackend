package ru.d2k.parkle.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void loggableGetMethods() {}

    @Before("loggableGetMethods()")
    public void logGetMethodBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();

        String methodName = signature.getName();
        Object[] agrs = joinPoint.getArgs();

        log.info("GET method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("loggableGetMethods()")
    public void logGetMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.info("GET method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "loggableGetMethods()", throwing = "ex")
    public void logGetMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("GET method {} return exception: {}", methodName, ex.getMessage());
    }
}
