package com.gty.adapter;

/**
 * 对象适配器,实现目标接口.将源方法类作为对象传递进来
 */
public class TargetAdapterTwo implements Target{
    private Adaptee adaptee = new Adaptee();

    @Override
    public void requestInfo(String name) {
        adaptee.getInfo(name);
    }

    //测试
    public static void main(String[] args) {
        //创建适配器的对象
        Target target = new TargetAdapterTwo();
        target.requestInfo("two======two");
    }
}
