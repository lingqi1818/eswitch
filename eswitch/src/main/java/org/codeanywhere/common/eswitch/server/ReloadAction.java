package org.codeanywhere.common.eswitch.server;

import java.util.Map;

public class ReloadAction extends ActionSupport implements Action {

    @Override
    public ActionResult process(Map<String, String> context) {
        ActionResult result = new ActionResult();
        switchEngine.reload();
        result.setSuccess(true);
        return result;
    }
}
