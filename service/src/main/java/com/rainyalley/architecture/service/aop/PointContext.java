package com.rainyalley.architecture.service.aop;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;

import java.lang.reflect.Method;

public class PointContext implements JoinPoint {

    private final JoinPoint joinPoint;

    private final Method method;

    private final Class<?>[] parameterTypes;

    public PointContext(JoinPoint joinPoint) {
        this.joinPoint = joinPoint;
        this.parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        this.method = MethodUtils.getAccessibleMethod(this.getTarget().getClass(), this.getSignature().getName(), this.parameterTypes);
    }

    @Override
    public String toString() {
        return this.joinPoint.toString();
    }

    @Override
    public String toShortString() {
        return this.joinPoint.toShortString();
    }

    @Override
    public String toLongString() {
        return this.joinPoint.toLongString();
    }

    @Override
    public Object getThis() {
        return this.joinPoint.getThis();
    }

    @Override
    public Object getTarget() {
        return this.joinPoint.getTarget();
    }

    @Override
    public Object[] getArgs() {
        return this.joinPoint.getArgs();
    }

    @Override
    public Signature getSignature() {
        return this.joinPoint.getSignature();
    }

    @Override
    public SourceLocation getSourceLocation() {
        return this.joinPoint.getSourceLocation();
    }

    @Override
    public String getKind() {
        return this.joinPoint.getKind();
    }

    @Override
    public StaticPart getStaticPart() {
        return this.joinPoint.getStaticPart();
    }

    public Method getMethod() {
        return this.method;
    }


}
