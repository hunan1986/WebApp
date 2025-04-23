package com.example.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(public * com.example.Controller.*.*(..))")
    public void logBeforeEmployeeXML(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("{}.{}.{}",className, methodName, "Before");
    }


    @Around("execution(public * com.example.Controller.*.*(..))")
    public Object logAroundEmployeeXML(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("{}.{}.{}ms",className, methodName,executionTime);
        return proceed;
    }

    @AfterReturning(pointcut = "execution(public * com.example.Controller.*.*(..))", returning = "results")
    public void logAfterReturningEmployeeXML(JoinPoint joinPoint, Object results) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("className {}. methodName {}. message {} arguments[s] {} results {}",className, methodName,"AfterReturning", Arrays.toString(joinPoint.getArgs()), results);
        //System.out.println("Before xml");
    }

    @After("execution(public * com.example.Controller.*.*(..))")
    public void logAfterEmployeeXML(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("{}.{}.{}",className, methodName,"After");

    }

    @AfterThrowing(pointcut = "execution(public * com.example.Controller.*.*(..))", throwing = "ex")
    public void logAfterThrowingEmployeeXML(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("{}.{}.{}",className, methodName,ex.getMessage());
    }
}
