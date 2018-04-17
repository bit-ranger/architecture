package com.rainyalley.architecture.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 原子性执行器
 * @author brave
 */
public class AtomicExecutor {

    private List<AtomicRunner> runners;

    public AtomicExecutor(int initialCapacity) {
        runners = new ArrayList<>(initialCapacity);
    }

    public AtomicExecutor add(AtomicRunner runner){
        runners.add(runner);
        return this;
    }

    public boolean execute(){
        List<AtomicRunner> successList = new ArrayList<>(runners.size());
        for (AtomicRunner runner : runners) {
            if(!runner.runnable()){
                return false;
            }
        }

        for (AtomicRunner runner : runners) {
            boolean runSuccess = runner.run();
            if(runSuccess){
                successList.add(runner);
            } else {
                break;
            }
        }

        boolean allRunSuccess = successList.size() == runners.size();

        if(allRunSuccess){
            for (AtomicRunner runner : successList) {
                boolean commit = runner.commit();
                if(!commit){
                    throw new IllegalStateException(String.format("commit failure: %s", runner));
                }
            }

            return true;

        } else {
            for (int i = successList.size() - 1; i >= 0; i--) {
                AtomicRunner runner = successList.get(i);
                boolean rollback = runner.rollback();
                if(!rollback){
                    throw new IllegalStateException(String.format("rollback failure: %s", runner));
                }
            }
            return false;
        }
    }
}
