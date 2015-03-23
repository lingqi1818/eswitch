package org.codeanywhere.common.eswitch;

/**
 * <pre>
 * 开关项事件
 * 当开关项变动,会发出ItemChange事件.
 * </pre>
 * 
 * @author chenke
 */
public interface ItemListener {

    /**
     * <pre>
     * 开关项发生变动事件. 
     * TODO: 变量是否使用ItemEvent?
     * </pre>
     * 
     * @param item 变动后开关项
     */
    void onItemChanged(Item item);

}
