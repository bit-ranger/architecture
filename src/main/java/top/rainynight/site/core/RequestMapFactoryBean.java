package top.rainynight.site.core;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.LinkedHashMap;

public class RequestMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>>{

    private LinkedHashMap<RequestMatcher,Collection<ConfigAttribute>> requestMap;

    @Override
    public LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> getObject() throws Exception {
        if(this.requestMap == null){
            fill();
        }
        return this.requestMap;
    }

    @Override
    public Class<?> getObjectType() {
        if(requestMap != null){
            return  requestMap.getClass();
        }
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private void fill(){
        requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        RequestMatcher matcher = new AntPathRequestMatcher("/user/**");
        Collection<ConfigAttribute> attributes = SecurityConfig.createListFromCommaDelimitedString("ROLE_userAdmin");
        requestMap.put(matcher, attributes);
    }
}
