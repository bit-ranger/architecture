package com.rainyalley.architecture.service.aop;

import com.rainyalley.architecture.service.user.model.entity.User;
import com.rainyalley.architecture.service.user.service.UserService;
import com.rainyalley.architecture.core.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.logging.LogManager;

/**
 * InvokeCacheAspect Tester.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-common.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class InvokeCacheAspectTest {

    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private InvokeCacheAspect invokeCacheAspect;

    @Autowired
    private UserService userService;


    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: around(ProceedingJoinPoint joinPoint)
     */
    @Test
    public void testAround() throws Exception {
        this.userService.get(new User(), new Page());
        this.userService.get(new User(), new Page());
    }

    /**
     * Method: setCacheProvider(CacheProvider cacheProvider)
     */
    @Test
    public void testSetCacheProvider() throws Exception {
        //TODO: Test goes here... 
    }

    /**
     * Method: put(String key, Object value)
     */
    @Test
    public void testPut() throws Exception {
        //TODO: Test goes here... 
    }

    /**
     * Method: get(String key, Class<V> type)
     */
    @Test
    public void testGet() throws Exception {
        //TODO: Test goes here... 
    }


// private methods ~~~~


    /**
     * Method: extraCacheKey(PointContext context)
     */
    @Test
    public void testExtraCacheKey() throws Exception {
        //TODO: Test goes here... 
                /* 
                try { 
                   Method method = InvokeCacheAspect.getClass().getMethod("extraCacheKey", PointContext.class); 
                   method.setAccessible(true); 
                   method.invoke(<Object>, <Parameters>); 
                } catch(NoSuchMethodException e) { 
                } catch(IllegalAccessException e) { 
                } catch(InvocationTargetException e) { 
                } 
                */
    }

    /**
     * Method: doPut(String key, Object value)
     */
    @Test
    public void testDoPut() throws Exception {
        //TODO: Test goes here... 
                /* 
                try { 
                   Method method = InvokeCacheAspect.getClass().getMethod("doPut", String.class, Object.class); 
                   method.setAccessible(true); 
                   method.invoke(<Object>, <Parameters>); 
                } catch(NoSuchMethodException e) { 
                } catch(IllegalAccessException e) { 
                } catch(InvocationTargetException e) { 
                } 
                */
    }

    /**
     * Method: doGet(String key, Class<V> type)
     */
    @Test
    public void testDoGet() throws Exception {
        //TODO: Test goes here... 
                /* 
                try { 
                   Method method = InvokeCacheAspect.getClass().getMethod("doGet", String.class, Class<V>.class); 
                   method.setAccessible(true); 
                   method.invoke(<Object>, <Parameters>); 
                } catch(NoSuchMethodException e) { 
                } catch(IllegalAccessException e) { 
                } catch(InvocationTargetException e) { 
                } 
                */
    }

} 
