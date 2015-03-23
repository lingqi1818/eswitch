package org.codeanywhere.common.eswitch.server;

import java.util.Map;

import org.codeanywhere.common.eswitch.item.InternalSwitchEngine;


/**
 * 配置变更请求中分action的处理接口定义。
 * 
 * @author chenke
 */
public interface Action {

    /**
     * 具体执行逻辑的接口。
     * 
     * @param action
     * @param data
     * @return
     */
    public ActionResult process(Map<String, String> context);

    /**
     * 绑定SwitchEngine的接口。
     * 
     * @param switchEngine
     */
    public void setSwitchEngine(InternalSwitchEngine switchEngine);

}
