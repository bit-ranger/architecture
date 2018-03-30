package com.rainyalley.architecture.service.impl;

import com.rainyalley.architecture.core.BeanMapConvertor;
import com.rainyalley.architecture.core.Page;
import com.rainyalley.architecture.dao.BaseMapper;
import com.rainyalley.architecture.service.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 实现了{@link Service}中所有方法的默认实现类
 * 该类可作为事务的通用实现
 *
 * @param <B>
 */
public abstract class ServiceBasicSupport<B,D> implements Service<B> {

    private Validator validator;

    @Override
    @Transactional
    public B save(B obj) {
        List<B> pojoList = this.get(obj, new Page());
        D d = toDo(obj);
        if (pojoList == null || pojoList.isEmpty()) {
            this.getDao().insert(d);
        } else {
            this.getDao().update(d);
        }
        return toBo(d);
    }

    @Override
    @Transactional
    public int remove(B obj) {
        return this.getDao().delete(toDo(obj));
    }

    @Override
    @Transactional(readOnly = true)
    public B get(B obj) {
        List<B> pojoList = this.get(obj, new Page());
        if (pojoList == null || pojoList.isEmpty()) {
            return null;
        }
        return pojoList.get(0);
    }

    private List<B> doGet(B obj, Page page) {
        Map<String, Object> params = BeanMapConvertor.merge(obj, page);
        return this.getDao().select(params).stream().map(this::toBo).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<B> get(B obj, Page page) {
        return this.doGet(obj, page);
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

    protected <T> void resolveConstraint(Set<ConstraintViolation<T>> result) {
        Assert.notNull(result);
        if (result.size() > 0) {
            StringBuilder message = new StringBuilder();
            Iterator<ConstraintViolation<T>> iterator = result.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> constraint = iterator.next();
                message.append(constraint.getPropertyPath()).append(" ");
                message.append(constraint.getMessage());
                if (iterator.hasNext()) {
                    message.append(", ");
                }
            }
            throw new ValidationException(message.toString());
        }
    }

    <T> void validate(T object, Class<?>... groups) {
        this.resolveConstraint(this.validator().validate(object, groups));
    }
}
