package com.rainyalley.architecture.entity;


import com.rainyalley.architecture.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReentrantLockEntityExample extends Page {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReentrantLockEntityExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andTargetIsNull() {
            addCriterion("target is null");
            return (Criteria) this;
        }

        public Criteria andTargetIsNotNull() {
            addCriterion("target is not null");
            return (Criteria) this;
        }

        public Criteria andTargetEqualTo(String value) {
            addCriterion("target =", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetNotEqualTo(String value) {
            addCriterion("target <>", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetGreaterThan(String value) {
            addCriterion("target >", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetGreaterThanOrEqualTo(String value) {
            addCriterion("target >=", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetLessThan(String value) {
            addCriterion("target <", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetLessThanOrEqualTo(String value) {
            addCriterion("target <=", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetLike(String value) {
            addCriterion("target like", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetNotLike(String value) {
            addCriterion("target not like", value, "target");
            return (Criteria) this;
        }

        public Criteria andTargetIn(List<String> values) {
            addCriterion("target in", values, "target");
            return (Criteria) this;
        }

        public Criteria andTargetNotIn(List<String> values) {
            addCriterion("target not in", values, "target");
            return (Criteria) this;
        }

        public Criteria andTargetBetween(String value1, String value2) {
            addCriterion("target between", value1, value2, "target");
            return (Criteria) this;
        }

        public Criteria andTargetNotBetween(String value1, String value2) {
            addCriterion("target not between", value1, value2, "target");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeIsNull() {
            addCriterion("acquire_time is null");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeIsNotNull() {
            addCriterion("acquire_time is not null");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeEqualTo(Date value) {
            addCriterion("acquire_time =", value, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeNotEqualTo(Date value) {
            addCriterion("acquire_time <>", value, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeGreaterThan(Date value) {
            addCriterion("acquire_time >", value, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("acquire_time >=", value, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeLessThan(Date value) {
            addCriterion("acquire_time <", value, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeLessThanOrEqualTo(Date value) {
            addCriterion("acquire_time <=", value, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeIn(List<Date> values) {
            addCriterion("acquire_time in", values, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeNotIn(List<Date> values) {
            addCriterion("acquire_time not in", values, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeBetween(Date value1, Date value2) {
            addCriterion("acquire_time between", value1, value2, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andAcquireTimeNotBetween(Date value1, Date value2) {
            addCriterion("acquire_time not between", value1, value2, "acquireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIsNull() {
            addCriterion("expire_time is null");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIsNotNull() {
            addCriterion("expire_time is not null");
            return (Criteria) this;
        }

        public Criteria andExpireTimeEqualTo(Date value) {
            addCriterion("expire_time =", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotEqualTo(Date value) {
            addCriterion("expire_time <>", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeGreaterThan(Date value) {
            addCriterion("expire_time >", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("expire_time >=", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeLessThan(Date value) {
            addCriterion("expire_time <", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeLessThanOrEqualTo(Date value) {
            addCriterion("expire_time <=", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIn(List<Date> values) {
            addCriterion("expire_time in", values, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotIn(List<Date> values) {
            addCriterion("expire_time not in", values, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeBetween(Date value1, Date value2) {
            addCriterion("expire_time between", value1, value2, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotBetween(Date value1, Date value2) {
            addCriterion("expire_time not between", value1, value2, "expireTime");
            return (Criteria) this;
        }

        public Criteria andEntrantCountIsNull() {
            addCriterion("entrant_count is null");
            return (Criteria) this;
        }

        public Criteria andEntrantCountIsNotNull() {
            addCriterion("entrant_count is not null");
            return (Criteria) this;
        }

        public Criteria andEntrantCountEqualTo(Integer value) {
            addCriterion("entrant_count =", value, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andEntrantCountNotEqualTo(Integer value) {
            addCriterion("entrant_count <>", value, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andEntrantCountGreaterThan(Integer value) {
            addCriterion("entrant_count >", value, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andEntrantCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("entrant_count >=", value, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andEntrantCountLessThan(Integer value) {
            addCriterion("entrant_count <", value, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andEntrantCountLessThanOrEqualTo(Integer value) {
            addCriterion("entrant_count <=", value, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andEntrantCountIn(List<Integer> values) {
            addCriterion("entrant_count in", values, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andEntrantCountNotIn(List<Integer> values) {
            addCriterion("entrant_count not in", values, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andEntrantCountBetween(Integer value1, Integer value2) {
            addCriterion("entrant_count between", value1, value2, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andEntrantCountNotBetween(Integer value1, Integer value2) {
            addCriterion("entrant_count not between", value1, value2, "entrantCount");
            return (Criteria) this;
        }

        public Criteria andLockKeeperIsNull() {
            addCriterion("lock_keeper is null");
            return (Criteria) this;
        }

        public Criteria andLockKeeperIsNotNull() {
            addCriterion("lock_keeper is not null");
            return (Criteria) this;
        }

        public Criteria andLockKeeperEqualTo(String value) {
            addCriterion("lock_keeper =", value, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperNotEqualTo(String value) {
            addCriterion("lock_keeper <>", value, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperGreaterThan(String value) {
            addCriterion("lock_keeper >", value, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperGreaterThanOrEqualTo(String value) {
            addCriterion("lock_keeper >=", value, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperLessThan(String value) {
            addCriterion("lock_keeper <", value, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperLessThanOrEqualTo(String value) {
            addCriterion("lock_keeper <=", value, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperLike(String value) {
            addCriterion("lock_keeper like", value, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperNotLike(String value) {
            addCriterion("lock_keeper not like", value, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperIn(List<String> values) {
            addCriterion("lock_keeper in", values, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperNotIn(List<String> values) {
            addCriterion("lock_keeper not in", values, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperBetween(String value1, String value2) {
            addCriterion("lock_keeper between", value1, value2, "lockKeeper");
            return (Criteria) this;
        }

        public Criteria andLockKeeperNotBetween(String value1, String value2) {
            addCriterion("lock_keeper not between", value1, value2, "lockKeeper");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}