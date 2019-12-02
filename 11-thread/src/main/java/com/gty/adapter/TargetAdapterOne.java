package com.gty.adapter;

/**
 * 类适配器,继承源方法类,实现目标接口
 */
public class TargetAdapterOne extends Adaptee implements Target {
    @Override
    public void requestInfo(String name) {
        //在目标方法中调用源方法
        super.getInfo(name);
    }
    
    //测试
    public static void main(String[] args) {
        //创建适配器的对象
        Target target = new TargetAdapterOne();
        target.requestInfo("one======one");
    }
}
