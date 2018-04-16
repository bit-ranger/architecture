package com.rainyalley.architecture.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author brave
 */
public class AtomicExecutor {

    private List<AtomicRunner> runners = new ArrayList<>(2);

    public AtomicExecutor add(AtomicRunner runner){
        runners.add(runner);
        return this;
    }

    public boolean execute(){
        List<AtomicRunner> successList = new ArrayList<>(runners.size());
        for (AtomicRunner runner : runners) {
            if(runner.run()){
                successList.add(runner);
            } else {
                break;
            }
        }

        if(successList.size() == runners.size()){
            for (AtomicRunner runner : successList) {
                runner.commit();
            }

            return true;
        } else {
            for (int i = successList.size() - 1; i >= 0; i--) {
                successList.get(i).rollback();
            }

            return false;
        }
    }
}
