package com.rainyalley.architecture.boot.config.security;

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

public class JitFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final FilterInvocationSecurityMetadataSource metadataSource;

    private final JitFilterInvocationSecurityMetadataSource.RequestMapLoader requestMapLoader;

    public JitFilterInvocationSecurityMetadataSource(JdbcTemplate jdbcTemplate) {
        Assert.notNull(jdbcTemplate, "factoryBean required");
        requestMapLoader = new JitFilterInvocationSecurityMetadataSource.RequestMapLoader(jdbcTemplate);
        this.metadataSource = new DefaultFilterInvocationSecurityMetadataSource(this.requestMapLoader.load());
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        return this.metadataSource.getAttributes(o);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return this.metadataSource.getAllConfigAttributes();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return this.metadataSource.supports(aClass);
    }

    public void setMetadataQuery(String metadataQuery) {
        requestMapLoader.metadataQuery = metadataQuery;
    }

    private static class RequestMapLoader {

        private static final String METADATA_QUERY = "SELECT w.sequence,w.content,r.name FROM resource w LEFT JOIN security_metadata s on w.id = s.resource_id LEFT JOIN role r ON r.id = s.role_id";
        private final JdbcTemplate jdbcTemplate;
        private String metadataQuery;

        public RequestMapLoader(JdbcTemplate jdbcTemplate) {
            this.metadataQuery = JitFilterInvocationSecurityMetadataSource.RequestMapLoader.METADATA_QUERY;
            this.jdbcTemplate = jdbcTemplate;
        }


        private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> load() {
            Set<JitFilterInvocationSecurityMetadataSource.Metadata> metadataList = new TreeSet<JitFilterInvocationSecurityMetadataSource.Metadata>(this.loadMetadata());
            LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
      /*  RequestMatcher matcher = new AntPathRequestMatcher("/user*//**");
             Collection<ConfigAttribute> attributes = SecurityConfig.createListFromCommaDelimitedString("ROLE_userAdmin");
             requestMap.put(matcher, attributes);*/

            for (JitFilterInvocationSecurityMetadataSource.Metadata metadata : metadataList) {
                RequestMatcher matcher = new AntPathRequestMatcher(metadata.content);
                Collection<ConfigAttribute> attributes = requestMap.get(matcher);
                if (attributes == null) {
                    attributes = new ArrayList<ConfigAttribute>();
                    requestMap.put(matcher, attributes);
                }
                attributes.add(new SecurityConfig(metadata.role));
            }

            return requestMap;
        }


        private List<JitFilterInvocationSecurityMetadataSource.Metadata> loadMetadata() {
            return this.jdbcTemplate.query(this.metadataQuery, new String[]{},
                    new RowMapper<JitFilterInvocationSecurityMetadataSource.Metadata>() {
                        @Override
                        public JitFilterInvocationSecurityMetadataSource.Metadata mapRow(ResultSet rs, int rowNum) throws SQLException {
                            int sequence = rs.getInt(1);
                            String pattern = rs.getString(2);
                            String role = rs.getString(3);
                            return new JitFilterInvocationSecurityMetadataSource.Metadata(sequence, pattern, role);
                        }
                    });
        }
    }

    private static class Metadata implements Comparable<JitFilterInvocationSecurityMetadataSource.Metadata> {

        private final int sequence;
        private final String content;
        private final String role;

        private Metadata(int sequence, String content, String role) {
            this.sequence = sequence;
            this.content = content;
            this.role = role;
        }

        @Override
        public int compareTo(JitFilterInvocationSecurityMetadataSource.Metadata o) {
            return sequence - o.sequence;
        }
    }
}
