package org.namstorm.fluency;

import java.util.function.Consumer;

/**
 * Created by maxnam-storm on 12/8/2016.
 */
public class OnIntResult extends OnResult<Integer> {
    public static final OnIntResult RES_0 = new OnIntResult(0);

    public static final OnIntResult cast(int val) {
        return val==0?RES_0:new OnIntResult(val);
    }

    public OnIntResult(Integer result) {
        super(result);
    }

    public OnIntResult on0(Consumer<Integer> c) {
        if(result.intValue()==0) {
            c.accept(result);
        }
        return this;
    }

    public OnIntResult onPositive(Consumer<Integer> c) {
        if(result.intValue()>0) {
            c.accept(result);
        }
        return this;
    }
    public OnIntResult onNegative(Consumer<Integer> c) {
        if(result.intValue()<0) {
            c.accept(result);
        }
        return this;
    }

}
