package org.codeanywhere.common.eswitch.item;

import org.codeanywhere.common.eswitch.Item;

/**
 * 内部Item接口,包括set方法.
 * 
 * @author chenke
 */
public interface InternalItem extends Item {

    /**
     * 设置开关项名字.
     * 
     * @param name 开关项名
     */
    void setName(String name);

    /**
     * 设置开关 开启/关闭 状态
     * 
     * @param on 开启/关闭 状态
     */
    void setOn(boolean on);

    /**
     * 设置阀值
     * 
     * @param threshold 阀值
     */
    void setThreshold(long threshold);
    
    /**
     * 设置详情
     * 
     * @param threshold 阀值
     */
    void setDetail(String detail);

}
