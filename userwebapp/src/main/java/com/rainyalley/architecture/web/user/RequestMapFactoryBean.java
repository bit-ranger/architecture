package com.rainyalley.architecture.web.user;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RequestMapFactoryBean extends JdbcDaoSupport implements FactoryBean<LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>> {

    private final static String METADATA_QUERY = "SELECT w.sequence,w.pattern,r.name FROM web_resource w LEFT JOIN security_metadata s on w.id = s.webresource_id LEFT JOIN role r ON r.id = s.role_id";

    private String metadataQuery;

    private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    public RequestMapFactoryBean(){
        metadataQuery = METADATA_QUERY;
    }

    @Override
    public LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> getObject() throws Exception {
        if (this.requestMap == null) {
            fill();
        }
        return this.requestMap;
    }

    @Override
    public Class<?> getObjectType() {
        if (requestMap != null) {
            return requestMap.getClass();
        }
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private void fill() {
        Set<Metadata> metadataList = new TreeSet<Metadata>(loadMetadata()) ;
        requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
      /*  RequestMatcher matcher = new AntPathRequestMatcher("/user*//**");
        Collection<ConfigAttribute> attributes = SecurityConfig.createListFromCommaDelimitedString("ROLE_userAdmin");
        requestMap.put(matcher, attributes);*/

        for (Metadata metadata : metadataList) {
            RequestMatcher matcher = new AntPathRequestMatcher(metadata.pattern);
            Collection<ConfigAttribute> attributes = requestMap.get(matcher);
            if(attributes == null){
                attributes = new ArrayList<ConfigAttribute>();
                requestMap.put(matcher, attributes);
            }
            attributes.add(new SecurityConfig(metadata.role));
        }
    }


    protected List<Metadata> loadMetadata() {
        return getJdbcTemplate().query(metadataQuery, new String[]{},
                new RowMapper<Metadata>() {
                    public Metadata mapRow(ResultSet rs, int rowNum) throws SQLException {
                        int sequence = rs.getInt(1);
                        String pattern = rs.getString(2);
                        String role = rs.getString(3);
                        return new Metadata(sequence, pattern, role);
                    }
                });
    }

    public void setMetadataQuery(String metadataQuery) {
        this.metadataQuery = metadataQuery;
    }

    protected static class Metadata implements Comparable<Metadata>{

        private Metadata(int sequence, String pattern, String role){
            this.sequence = sequence;
            this.pattern = pattern;
            this.role = role;
        }

        private int sequence;
        private String pattern;
        private String role;

        @Override
        public int compareTo(Metadata o) {
            return this.sequence - o.sequence;
        }
    }
}
