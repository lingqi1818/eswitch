package org.codeanywhere.common.eswitch.threshold;

/**
 * @author chenke
 */
public interface Sph {

    /**
     * <pre>
     * 获取资源
     * 当获取资源后,返回true;反之返回false.
     * </pre>
     * 
     * @return 是否成功获取资源.
     */
    boolean entry();

    /**
     * <pre>
     * 释放资源
     * 当释放资源后,返回true;反之返回false.
     * </pre>
     * 
     * @return 是否成功释放资源.
     */
    boolean release();

}
