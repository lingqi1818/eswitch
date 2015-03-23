package org.codeanywhere.common.eswitch.threshold.configuration;

import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.codeanywhere.common.eswitch.threshold.ThresholdConfigurationStrategy;
import org.codeanywhere.common.eswitch.util.SwitchUtil;


/**
 * <pre>
 * 基于XML的配置策略
 * </pre>
 * 
 * @author chenke
 */
public class XmlConfigurationStrategy implements ThresholdConfigurationStrategy {

    // 配置信息
    private List<String> config;

    /**
     * <pre>
     * SORRY. 不支持同名函数! 原因如下:
     * 1. 重载方法, 一般用途一样, 流控制也不会差太多;
     * 2. 区分重载方法, 依赖参数值, 对于配置来说, 也特别繁琐.
     * 
     * 配置遍历优先级:
     * 1. 方法级别
     * 2. 类级别
     * 3. 包级别
     * </pre>
     */
    @Override
    public ThresholdDefinition getThresholdItem(MethodInvocation invocation) {
        if (config == null) {
            return null;
        }
        // 遍历方法级别的配置
        for (String line : config) {
            String[] info = line.split(" ");
            if (buildMethodKey(invocation).equals(info[0].trim())) {
                return new ThresholdDefinition(info[1].trim(), Long.valueOf(info[2].trim()));
            }
        }
        // 遍历类级别的配置
        for (String line : config) {
            String[] info = line.split(" ");
            if (buildClassKey(invocation).equals(info[0].trim())) {
                return new ThresholdDefinition(info[1].trim(), Long.valueOf(info[2].trim()));
            }
        }
        // 遍历包级别的配置
        for (String line : config) {
            String[] info = line.split(" ");
            if (buildPackageKey(invocation).startsWith(info[0].trim())) {
                return new ThresholdDefinition(info[1].trim(), Long.valueOf(info[2].trim()));
            }
        }
        return null;
    }

    private String buildMethodKey(MethodInvocation invocation) {
        return invocation.getMethod().getDeclaringClass().getName() + "#" + invocation.getMethod().getName();
    }

    private String buildClassKey(MethodInvocation invocation) {
        return invocation.getMethod().getDeclaringClass().getName();
    }

    private String buildPackageKey(MethodInvocation invocation) {
        return invocation.getMethod().getDeclaringClass().getPackage().getName();
    }

    public void setConfig(List<String> config) {
        // 验证配置信息
        for (String line : config) {
            String[] info = line.split(" ");
            if (info.length != 3 || info[0].length() <= 0 || info[1].length() <= 0 || !SwitchUtil.isNumber(info[2])) {
                throw new IllegalArgumentException("threshold config fail. info: " + line);
            }
        }
        this.config = config;
    }

}
