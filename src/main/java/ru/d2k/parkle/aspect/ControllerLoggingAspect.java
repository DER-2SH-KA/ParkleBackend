package ru.d2k.parkle.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ControllerLoggingAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void pointcutForGetMethod() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void pointcutForPostMethod() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void pointcutForPutMethod() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void pointcutForPatchMethod() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void pointcutForDeleteMethod() {}

    @Before("pointcutForGetMethod()")
    public void logGetMethodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] agrs = joinPoint.getArgs();

        log.debug("GET method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("pointcutForGetMethod()")
    public void logGetMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.debug("GET method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "pointcutForGetMethod()", throwing = "ex")
    public void logGetMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("GET method {} return exception: {}", methodName, ex.getMessage());
    }

    @Before("pointcutForPostMethod()")
    public void logPostMethodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] agrs = joinPoint.getArgs();

        log.debug("POST method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("pointcutForPostMethod()")
    public void logPostMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.debug("POST method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "pointcutForPostMethod()", throwing = "ex")
    public void logPostMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("POST method {} return exception: {}", methodName, ex.getMessage());
    }

    @Before("pointcutForPutMethod()")
    public void logPutMethodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] agrs = joinPoint.getArgs();

        log.debug("PUT method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("pointcutForPutMethod()")
    public void logPutMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.debug("PUT method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "pointcutForPutMethod()", throwing = "ex")
    public void logPutMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("PUT method {} return exception: {}", methodName, ex.getMessage());
    }

    @Before("pointcutForPatchMethod()")
    public void logPatchMethodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] agrs = joinPoint.getArgs();

        log.debug("PATCH method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("pointcutForPatchMethod()")
    public void logPatchMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.debug("PATCH method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "pointcutForPatchMethod()", throwing = "ex")
    public void logPatchMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("PATCH method {} return exception: {}", methodName, ex.getMessage());
    }

    @Before("pointcutForDeleteMethod()")
    public void logDeleteMethodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] agrs = joinPoint.getArgs();

        log.debug("DELETE method {} taken with args {}...", methodName, Arrays.toString(agrs));
    }

    @AfterReturning("pointcutForDeleteMethod()")
    public void logDeleteMethodAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        log.debug("DELETE method {} was served", methodName);
    }

    @AfterThrowing(pointcut = "pointcutForDeleteMethod()", throwing = "ex")
    public void logDeleteMethodAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();

        log.error("DELETE method {} return exception: {}", methodName, ex.getMessage());
    }
}
