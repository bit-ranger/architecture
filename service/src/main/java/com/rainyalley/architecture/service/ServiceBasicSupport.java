package com.rainyalley.architecture.service;

import com.rainyalley.architecture.core.BeanMapConvertor;
import com.rainyalley.architecture.core.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实现了{@link Service}中所有方法的默认实现类
 * 该类可作为事务的通用实现
 *
 * @param <T>
 */
public abstract class ServiceBasicSupport<T> implements Service<T> {

    private Dao<T> dao;

    private Validator validator;

    @Override
    @Transactional
    public int save(T obj) {
        int count = 0;
        List<T> pojoList = this._get(obj, new Page());
        if (pojoList == null || pojoList.isEmpty()) {
            count = this.dao.insert(obj);
        } else {
            count = this.dao.update(obj);
        }
        return count;
    }

    @Override
    @Transactional
    public int remove(T obj) {
        return this.dao.delete(obj);
    }

    @Override
    @Transactional(readOnly = true)
    public T get(T obj) {
        List<T> pojoList = this._get(obj, new Page());
        if (pojoList == null || pojoList.isEmpty()) {
            return null;
        }
        return pojoList.get(0);
    }

    private List<T> _get(T obj, Page page) {
        Map<String, Object> params = BeanMapConvertor.merge(obj, page);
        return this.dao.select(params);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> get(T obj, Page page) {
        return this._get(obj, page);
    }

    public void setDao(Dao<T> dao) {
        this.dao = dao;
    }

    public void setValidatorFactory(ValidatorFactory validatorFactory) {
        Validator validator = validatorFactory == null ? null : validatorFactory.getValidator();
        this.validator = validator;
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

    protected <T> void validate(T object, Class<?>... groups) {
        this.resolveConstraint(this.validator().validate(object, groups));
    }
}
