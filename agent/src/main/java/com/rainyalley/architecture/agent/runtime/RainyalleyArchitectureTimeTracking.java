package com.rainyalley.architecture.agent.runtime;

public class RainyalleyArchitectureTimeTracking {

    public static void track(String methodName, long startNanoTime, long endNanoTime){
        System.out.println(String.format("trcking %s %s %s", methodName, startNanoTime, endNanoTime));
    }
}
