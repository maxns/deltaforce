package org.namstorm.fluency;

/**
 * Created by maxnam-storm on 12/8/2016.
 *
 * For all those time you were tired of doing res = blah(); if (res == 0)...
 * you can do
 *
 * doYouBelieve().
 *      onTrue( r -> halelluja("!!!") );
 *      onFalse( r -> share("Testimony");
 */
public class OnBooleanResult extends OnResult<Boolean> {
    public static OnBooleanResult cast(boolean res) { return res?TRUE:FALSE; }
    public static final OnBooleanResult TRUE = new OnBooleanResult(true);
    public static final OnBooleanResult FALSE = new OnBooleanResult(false);

    public OnBooleanResult(Boolean result) {
        super(result);
    }

    public OnBooleanResult onTrue(ResultConsumer<Boolean> consumer) {
        if (result) {
            consumer.accept(result);
        }
        return this;
    }
    public OnBooleanResult onFalse(ResultConsumer<Boolean> consumer) {
        if(!result) {
            consumer.accept(result);
        }
        return this;
    }


}
