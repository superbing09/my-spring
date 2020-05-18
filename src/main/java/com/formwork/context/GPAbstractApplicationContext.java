package com.formwork.context;

/**
 * Created by Administrator on 2020/5/18.
 */
public abstract class GPAbstractApplicationContext {

    protected void onRefresh() {

    }

    protected abstract void refreshBeanFactory();
}
