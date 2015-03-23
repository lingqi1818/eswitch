package org.codeanywhere.common.eswitch.threshold;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 当流控项没有找到时, 处理对应逻辑.
 * 
 * @author chenke
 */
public interface ThresholdNotFoundHandler extends ThresholdHandler {

    /**
     * 处理流控项未被找到时的逻辑.
     * 
     * @param invocation 方法调用信息
     * @return
     */
    Object handleNotFound(MethodInvocation invocation) throws Throwable;
}
