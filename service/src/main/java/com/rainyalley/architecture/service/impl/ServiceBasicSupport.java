package com.rainyalley.architecture.service.impl;

import com.rainyalley.architecture.core.Id;
import com.rainyalley.architecture.dao.BaseMapper;
import com.rainyalley.architecture.service.Service;
import com.rainyalley.architecture.service.util.ValidationInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 实现了{@link Service}中所有方法的默认实现类
 * 该类可作为事务的通用实现
 *
 * @param <B>
 */
public abstract class ServiceBasicSupport<B extends Id,D extends Id> implements Service<B> {

    private Validator validator;

    @Override
    @Transactional
    public B save(B obj) {
        D d = toDo(obj);
        if(obj.getId() == null){
            getDao().insert(d);
        } else {
            getDao().update(d);
        }

        d = getDao().get(d.getId());
        assertion(d);
        B b = toBo(d);
        assertion(b);
        return b;
    }

    @Override
    @Transactional
    public int remove(String id) {
        return this.getDao().delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public B get(String id) {
        D d = getDao().get(id);
        assertion(d);
        B b = toBo(d);
        assertion(d);
        return b;
    }


    protected abstract BaseMapper<D> getDao();

    protected abstract  D toDo(B b);

    protected abstract  B toBo(D d);


    public void setValidatorFactory(ValidatorFactory validatorFactory) {
        this.validator = validatorFactory == null ? null : validatorFactory.getValidator();
    }

    protected final Validator validator() {
        if (this.validator == null) {
            this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        }
        return this.validator;
    }

    protected <T> void resolveConstraint(Id obj, Set<ConstraintViolation<T>> result) {
        Assert.notNull(result, "result can not be null");
        if (result.size() == 0) {
            return;
        }

        Map<String,Object> map = new HashMap<>(result.size());
        Iterator<ConstraintViolation<T>> iterator = result.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<T> constraint = iterator.next();
            map.put(constraint.getPropertyPath().toString(), constraint.getInvalidValue());
        }
        ValidationInfo info = new ValidationInfo(obj.getId(), map);
        throw new ValidationException(info.toString());
    }

    protected void assertion(Id obj){
        resolveConstraint(obj, validator().validate(obj));
    }
}
