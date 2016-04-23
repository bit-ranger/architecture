package com.rainyalley.architecture.web.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JitFilterInvocationSecurityMetadataSource  implements FilterInvocationSecurityMetadataSource{

    private FilterInvocationSecurityMetadataSource metadataSource;

    private RequestMapLoader requestMapLoader;

    public JitFilterInvocationSecurityMetadataSource(JdbcTemplate jdbcTemplate){
        Assert.notNull(jdbcTemplate, "factoryBean required");
        this.requestMapLoader = new RequestMapLoader(jdbcTemplate);
        metadataSource = new DefaultFilterInvocationSecurityMetadataSource(requestMapLoader.load());
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        return metadataSource.getAttributes(o);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return metadataSource.getAllConfigAttributes();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return metadataSource.supports(aClass);
    }


    private static class RequestMapLoader{

        private final static String METADATA_QUERY = "SELECT w.sequence,w.content,r.name FROM resource w LEFT JOIN security_metadata s on w.id = s.resource_id LEFT JOIN role r ON r.id = s.role_id";

        private String metadataQuery;

        private JdbcTemplate jdbcTemplate;

        public RequestMapLoader(JdbcTemplate jdbcTemplate){
            metadataQuery = METADATA_QUERY;
            this.jdbcTemplate = jdbcTemplate;
        }


        private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> load() {
            Set<Metadata> metadataList = new TreeSet<Metadata>(loadMetadata()) ;
            LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
      /*  RequestMatcher matcher = new AntPathRequestMatcher("/user*//**");
             Collection<ConfigAttribute> attributes = SecurityConfig.createListFromCommaDelimitedString("ROLE_userAdmin");
             requestMap.put(matcher, attributes);*/

            for (Metadata metadata : metadataList) {
                RequestMatcher matcher = new AntPathRequestMatcher(metadata.content);
                Collection<ConfigAttribute> attributes = requestMap.get(matcher);
                if(attributes == null){
                    attributes = new ArrayList<ConfigAttribute>();
                    requestMap.put(matcher, attributes);
                }
                attributes.add(new SecurityConfig(metadata.role));
            }

            return requestMap;
        }


        private List<Metadata> loadMetadata() {
            return jdbcTemplate.query(metadataQuery, new String[]{},
                    new RowMapper<Metadata>() {
                        public Metadata mapRow(ResultSet rs, int rowNum) throws SQLException {
                            int sequence = rs.getInt(1);
                            String pattern = rs.getString(2);
                            String role = rs.getString(3);
                            return new Metadata(sequence, pattern, role);
                        }
                    });
        }
    }


    private static class Metadata implements Comparable<Metadata>{

        private Metadata(int sequence, String content, String role){
            this.sequence = sequence;
            this.content = content;
            this.role = role;
        }

        private int sequence;
        private String content;
        private String role;

        @Override
        public int compareTo(Metadata o) {
            return this.sequence - o.sequence;
        }
    }

    public void setMetadataQuery(String metadataQuery) {
        this.requestMapLoader.metadataQuery = metadataQuery;
    }
}
