package com.rainyalley.architecture.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {

    @Mock
    private List<String> list;

    @Test
    public void simpleTest(){

        //创建mock对象，参数可以是类，也可以是接口
//        List<String> list = mock(List.class);

        //设置方法的预期返回值
        when(list.get(0)).thenReturn("helloworld");

        String result = list.get(0);

        //验证方法调用(是否调用了get(0))
        verify(list).get(0);

        //junit测试
        Assert.assertEquals("helloworld", result);


        when(list.get(1)).thenThrow(new RuntimeException("test excpetion"));
        assertException(() -> list.get(1));

        //只有void方法能够donothing
//        doNothing().doThrow(new RuntimeException("second call")).when(list).size();
//        list.size();

        when(list.size()).thenReturn(0).thenThrow(new RuntimeException("second call"));
        list.size();
        assertException(() -> list.size());

    }


    private void assertException(Runnable runnable){
        boolean hasEx = false;
        try {
            runnable.run();
        } catch (Exception e){
            hasEx = true;
        }
        Assert.assertTrue(hasEx);
    }
}
