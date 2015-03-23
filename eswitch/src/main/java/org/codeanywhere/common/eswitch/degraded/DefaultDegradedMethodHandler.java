package org.codeanywhere.common.eswitch.degraded;

/**
 * 默认的降级函数处理逻辑
 * 
 * @author chenke
 * @date 2015-3-23 上午10:11:42
 */
public interface DefaultDegradedMethodHandler {
    /**
     * 默认处理方法
     * 
     * @param argements 函数的参数列表
     * @return 函数返回类型
     */
    public Object handle(Object[] argements);

}
