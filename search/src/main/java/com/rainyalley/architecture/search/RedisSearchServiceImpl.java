package com.rainyalley.architecture.search;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RedisSearchServiceImpl implements SearchService {


    private String keyPrefix = "";

    private ZSetOperations<String, String> zso;

    @Override
    public void index(String id, String text) {

        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id can not be blank");
        }

        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("text can not be blank");
        }

        List<Term> terms = ToAnalysis.parse(text.toString()).getTerms();
        for (Term term : terms) {
            String key = this.keyPrefix + term.getRealName();
            this.zso.add(key, id, 1);
        }
    }

    @Override
    public Set<String> search(String keyword) {
        List<Term> terms = ToAnalysis.parse(keyword.toString()).getTerms();

        Set<String> idSet = new HashSet<String>();
        if (terms.size() > 2) {
            List<String> keyList = new ArrayList<String>();
            for (Term term : terms) {
                String key = this.keyPrefix + term.getRealName();
                keyList.add(key);
            }
            String storeKey = "store";
            this.zso.unionAndStore(keyList.get(0), keyList.subList(1, keyList.size()), storeKey);
            idSet = this.zso.range(storeKey, 0, 9);
        } else {
            for (Term term : terms) {
                String key = this.keyPrefix + term.getRealName();
                Set<String> rst = this.zso.range(key, 0, 9);
                idSet.addAll(rst);
            }
        }

        return idSet;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.zso = redisTemplate.opsForZSet();
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
