package org.codeanywhere.common.eswitch.degraded;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.codeanywhere.common.eswitch.threshold.ThresholdException;


/**
 * 默认降级拦截器，触发默认降级逻辑
 * 
 * @author chenke
 * @date 2015-3-23 上午10:26:40
 */
public class DefaultDegradedHandlerInterceptor implements MethodInterceptor {
    private Map<String, DefaultDegradedMethodHandler> handlerMap;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        try {
            Object obj = invocation.proceed();
            return obj;
        } catch (ThresholdException ex) {
            if (handlerMap != null) {
                DefaultDegradedMethodHandler handler = handlerMap.get(invocation.getMethod()
                        .getDeclaringClass().getName()
                        + "." + invocation.getMethod().getName());
                if (handler != null) {
                    return handler.handle(invocation.getArguments());
                }
            }
            throw ex;
        }
    }

    public void setHandlerMap(Map<String, DefaultDegradedMethodHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }

}
