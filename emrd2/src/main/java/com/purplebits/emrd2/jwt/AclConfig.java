package com.purplebits.emrd2.jwt;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.purplebits.emrd2.repositories.PermissionsRepository;

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;


import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;

@Configuration
public class AclConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), permissionGrantingStrategy());
    }

    @Bean
    public EhCacheBasedAclCache aclCache() {
        return new EhCacheBasedAclCache(aclEhCacheFactoryBean().getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public JdbcMutableAclService aclService() {
        return new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
        
    }
    
    @Bean
    public PermissionFactory permissionFactory(PermissionsRepository permissionsRepository) {
        return new CustomPermissionFactory(permissionsRepository);
    }

    @Bean
    public AclPermissionEvaluator aclPermissionEvaluator(AclService aclService, PermissionFactory permissionFactory) {
        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService);
        permissionEvaluator.setPermissionFactory(permissionFactory);
        return permissionEvaluator;
    }

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(AclService aclService,
                                                                                  PermissionFactory permissionFactory) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator(aclService, permissionFactory));
        return expressionHandler;
    }
    
    //======================================================================================================================
    
//    @Bean
//    public LookupStrategy lookupStrategy() {
//        return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), permissionGrantingStrategy());
//    }
//
//    @Bean
//    public EhCacheBasedAclCache aclCache() {
//        return new EhCacheBasedAclCache(aclEhCacheFactoryBean().getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy());
//    }
//
//    @Bean
//    public EhCacheFactoryBean aclEhCacheFactoryBean() {
//        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
//        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
//        ehCacheFactoryBean.setCacheName("aclCache");
//        return ehCacheFactoryBean;
//    }
//
//    @Bean
//    public EhCacheManagerFactoryBean aclCacheManager() {
//        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//        cacheManagerFactoryBean.setShared(true);
//        return cacheManagerFactoryBean;
//    }
//
//    @Bean
//    public PermissionGrantingStrategy permissionGrantingStrategy() {
//        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
//    }
//
//    @Bean
//    public AclAuthorizationStrategy aclAuthorizationStrategy() {
//    	 return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
//    	  
//    }
//
//    @Bean
//    public JdbcMutableAclService aclService() {
//        return new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
//    }
//
//    @Bean
//    public AclPermissionEvaluator aclPermissionEvaluator(AclService aclService) {
//        return new AclPermissionEvaluator(aclService);
//    }
}
