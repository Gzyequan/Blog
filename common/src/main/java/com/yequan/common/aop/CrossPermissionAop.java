package com.yequan.common.aop;

import com.yequan.common.util.CurrentUserLocal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @Auther: yq
 * @Date: 2019/8/27 15:01
 * @Description:
 */
//@Aspect
//@Component
public class CrossPermissionAop {


    @Pointcut("@annotation(com.yequan.common.annotation.CrossPermission)")
    public void crossPermissionAspect() {

    }

    @Around("crossPermissionAspect()")
    public Object doAround(ProceedingJoinPoint point) {
        Object result = null;
        try {
            Integer userId = CurrentUserLocal.getUserId();
            if (userId==2){
                result = point.proceed();
            }
            String pointClassName = point.getTarget().getClass().getName();
            Signature signature = point.getSignature();
            String name1 = signature.getName();
            Object[] args = point.getArgs();
            for (Object arg : args) {
                System.out.println(arg);
            }
            System.out.println(pointClassName + "_____" + name1 + "_____" + userId);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

}
