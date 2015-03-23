package org.codeanywhere.common.eswitch.threshold.configuration;

import org.aopalliance.intercept.MethodInvocation;
import org.codeanywhere.common.eswitch.threshold.Threshold;
import org.codeanywhere.common.eswitch.threshold.ThresholdConfigurationStrategy;


/**
 * 基于Annotation配置的实现.
 * 
 * @author chenke
 */
public class AnnotationConfigurationStrategy implements ThresholdConfigurationStrategy {

    @Override
    public ThresholdDefinition getThresholdItem(MethodInvocation invocation) {
        // 获取方法级别的Threshold配置
        Threshold threshold = invocation.getMethod().getAnnotation(Threshold.class);
        // 获取类级别的Threshold配置
        if (threshold == null) {
            threshold = invocation.getThis().getClass().getAnnotation(Threshold.class);
        }
        if (threshold == null) {
            return null;
        }
        // 构建threshold定义
        return new ThresholdDefinition(threshold.item(), threshold.defaultValue());
    }

}
