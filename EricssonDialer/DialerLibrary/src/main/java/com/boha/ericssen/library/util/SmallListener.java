package com.boha.ericssen.library.util;

/**
 * Created by aubreyM on 2014/11/12.
 */
public interface SmallListener {
    public void onOffTheHook(int count);
    public void onIdle(int count);
    public void onRinging(int count);
}
