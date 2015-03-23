package org.codeanywhere.common.eswitch.threshold.handler;

import org.aopalliance.intercept.MethodInvocation;
import org.codeanywhere.common.eswitch.threshold.ThresholdNotFoundHandler;


/**
 * 当流控项未找到时, 忽视, 直接执行业务逻辑.
 * 
 * @author chenke
 */
public class IgnoreThresholdNotFoundHandler implements ThresholdNotFoundHandler {

    @Override
    public Object handleNotFound(MethodInvocation invocation) throws Throwable {
        return invocation.proceed();
    }

}
