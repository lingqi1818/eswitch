package org.codeanywhere.common.eswitch.async;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.codeanywhere.common.eswitch.degraded.DefaultDegradedMethodHandler;
import org.codeanywhere.common.eswitch.item.DefaultSwitchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 函数异步调用拦截器
 * 
 * @author chenke
 * @date 2015-3-23 上午11:04:18
 */
public class AsyncInvokeInterceptor implements MethodInterceptor {
    private ExecutorService                           executorService;
    private int                                       corePoolSize  = 20;
    private int                                       maxPoolSize   = 200;
    private long                                      keepAliveTime = 5000;                                          //5秒
    private int                                       queueSize     = 10240;
    private Map<String, DefaultDegradedMethodHandler> handlerMap;
    private DefaultSwitchEngine                       switchEngine;
    private Logger                                    logger        = LoggerFactory
                                                                            .getLogger(AsyncInvokeInterceptor.class);

    public void init() {
        executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(queueSize),
                new DiscardOldestPolicy());
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (switchEngine.isOn("AsyncInvokeInterceptor", true)) {
            return asyncProceed(invocation);
        }
        return invocation.proceed();

    }

    private Object asyncProceed(final MethodInvocation invocation) {
        Future<Object> future = executorService.submit(new Callable<Object>() {

            @Override
            public Object call() {
                try {
                    return invocation.proceed();
                } catch (Throwable e) {
                    logger.error("execute method "
                            + invocation.getMethod().getDeclaringClass().getName() + "."
                            + invocation.getMethod().getName() + "failed", e);
                    return null;
                }
            }
        });

        try {
            return future.get(keepAliveTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error(" get result form pool error,method name: "
                    + invocation.getMethod().getDeclaringClass().getName() + "."
                    + invocation.getMethod().getName(), e);
            if (handlerMap != null) {
                DefaultDegradedMethodHandler handler = handlerMap.get(invocation.getMethod()
                        .getDeclaringClass().getName()
                        + "." + invocation.getMethod().getName());
                if (handler != null) {
                    return handler.handle(invocation.getArguments());
                }
            }
            return null;
        }
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public void setSwitchEngine(DefaultSwitchEngine switchEngine) {
        this.switchEngine = switchEngine;
    }

    public void setHandlerMap(Map<String, DefaultDegradedMethodHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }

}
