package ru.yaroslav.test.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
@Slf4j
public class UserBankAndCardControllerLoggingAspect {

    @Pointcut("within(ru.yaroslav.test.controllers.*)")
    public void userBankControllerPointcut() {
    }

    @Before("userBankControllerPointcut()")
    public void gettingAllUsersLoggingAdvice(JoinPoint joinPoint) {
        log.info(String.format("Поступил следующий запрос: %s", joinPoint.getArgs()));
    }

    @AfterThrowing(pointcut = "userBankControllerPointcut()",
            throwing = "throwable")
    public void afterThrowingUserBankAccountExceptionAdvice(JoinPoint joinPoint, Throwable throwable) {
        log.info(String.format("Следующий запрос - %s - привел к следующей ошибке - %s", Arrays.toString(joinPoint.getArgs()), throwable.getMessage()));
    }

    @AfterReturning(pointcut = "userBankControllerPointcut()", returning = "result")
    public void afterReturningUserBankAccountAdvice(JoinPoint joinPoint, Object result) {
        if (result != null) {
            log.info(String.format("Запрос вернул следующий результат: %s", result));
        }
    }
}
