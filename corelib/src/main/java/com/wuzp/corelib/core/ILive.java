package com.wuzp.corelib.core;

/**
 * 任何有生命周期的组件都可定义为 Live
 */
public interface ILive {

    boolean isActive();

    boolean isDestroyed();
}