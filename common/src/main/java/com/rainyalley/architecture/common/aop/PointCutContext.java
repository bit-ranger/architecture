package com.rainyalley.architecture.common.aop;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;

import java.lang.reflect.Method;

public class PointCutContext implements JoinPoint{

    private JoinPoint joinPoint;

    private Method method;

    private Class<?>[] parameterTypes;

    public PointCutContext(JoinPoint joinPoint) {
        this.joinPoint = joinPoint;
        parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        method = MethodUtils.getAccessibleMethod(getTarget().getClass(), getSignature().getName(), parameterTypes);
    }

    @Override
    public String toString() {
        return joinPoint.toString();
    }

    public String toShortString() {
        return joinPoint.toShortString();
    }

    public String toLongString() {
        return joinPoint.toLongString();
    }

    public Object getThis() {
        return joinPoint.getThis();
    }

    public Object getTarget() {
        return joinPoint.getTarget();
    }

    public Object[] getArgs() {
        return joinPoint.getArgs();
    }

    public Signature getSignature() {
        return joinPoint.getSignature();
    }

    public SourceLocation getSourceLocation() {
        return joinPoint.getSourceLocation();
    }

    public String getKind() {
        return joinPoint.getKind();
    }

    public JoinPoint.StaticPart getStaticPart() {
        return joinPoint.getStaticPart();
    }

    public Method getMethod(){
        return method;
    }


}
