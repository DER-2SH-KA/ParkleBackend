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
    public void logGetMethods() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void logPostMethods() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void logPutMethods() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void logDeleteMethods() {}

    @Before("logGetMethods()")
    public void logGetMethodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] agrs = joinPoint.getArgs();

        log.info("GET method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("logGetMethods()")
    public void logGetMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.info("GET method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "logGetMethods()", throwing = "ex")
    public void logGetMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("GET method {} return exception: {}", methodName, ex.getMessage());
    }

    @Before("logPostMethods()")
    public void logPostMethodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] agrs = joinPoint.getArgs();

        log.info("POST method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("logPostMethods()")
    public void logPostMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.info("POST method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "logPostMethods()", throwing = "ex")
    public void logPostMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("POST method {} return exception: {}", methodName, ex.getMessage());
    }

    @Before("logPutMethods()")
    public void logPutMethodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] agrs = joinPoint.getArgs();

        log.info("PUT method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("logPutMethods()")
    public void logPutMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.info("PUT method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "logPutMethods()", throwing = "ex")
    public void logPutMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("PUT method {} return exception: {}", methodName, ex.getMessage());
    }

    @Before("logDeleteMethods()")
    public void logDeleteMethodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] agrs = joinPoint.getArgs();

        log.info("DELETE method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("logDeleteMethods()")
    public void logDeleteMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.info("DELETE method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "logDeleteMethods()", throwing = "ex")
    public void logDeleteMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("DELETE method {} return exception: {}", methodName, ex.getMessage());
    }
}
