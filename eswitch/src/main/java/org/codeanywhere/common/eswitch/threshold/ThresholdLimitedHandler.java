package org.codeanywhere.common.eswitch.threshold;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 当被限流时, 处理限流逻辑.
 * 
 * @author chenke
 */
public interface ThresholdLimitedHandler extends ThresholdHandler {

    /**
     * 处理被流控时的逻辑.
     * 
     * @param invocation 方法调用信息
     * @param sph 流控信息
     * @return
     */
    Object handleLimited(MethodInvocation invocation, ItemSph sph) throws Throwable;
}
