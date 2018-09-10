package com.rainyalley.architecture.util;

import org.junit.Test;

import java.util.Date;

public class Repl {

    @Test
    public void repl(){
        System.out.println(Long.MAX_VALUE);
        System.out.println(9007199254740992L);
        System.out.println(new Date(Long.MAX_VALUE));
        System.out.println(new Date(9007199254740992L));

    }
}
