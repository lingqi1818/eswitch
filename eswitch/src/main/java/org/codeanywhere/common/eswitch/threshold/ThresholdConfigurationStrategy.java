package org.codeanywhere.common.eswitch.threshold;

import org.aopalliance.intercept.MethodInvocation;

/**
 * <pre>
 * Threshold配置.
 * 可以实现不同的配置方案,比如:
 * 1. 基于Annotation配置;
 * 2. 基于XML配置;
 * </pre>
 * 
 * @author chenke
 */
public interface ThresholdConfigurationStrategy {

    /**
     * <pre>
     * 获取方法的Threshold配置信息.
     * </pre>
     * 
     * @param clazz
     * @param method
     * @return
     */
    ThresholdDefinition getThresholdItem(MethodInvocation invocation);

    /**
     * 阀值定义类
     * 
     * @author chenke
     */
    static class ThresholdDefinition {

        private String item;
        private long   threshold;

        public ThresholdDefinition(String item, long threshold){
            this.item = item;
            this.threshold = threshold;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public long getThreshold() {
            return threshold;
        }

        public void setThreshold(long threshold) {
            this.threshold = threshold;
        }

    }

}
