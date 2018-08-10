package com.rainyalley.architecture.agent;
import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation instrumentation){
        ClassTrackingTransformer transformer = new ClassTrackingTransformer();
        instrumentation.addTransformer(transformer);
    }
}
