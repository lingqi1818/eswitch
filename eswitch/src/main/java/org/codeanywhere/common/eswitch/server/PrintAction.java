package org.codeanywhere.common.eswitch.server;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codeanywhere.common.eswitch.Item.ItemNotFoundException;
import org.codeanywhere.common.eswitch.item.InternalItem;
import org.codeanywhere.common.eswitch.util.SwitchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PrintAction extends ActionSupport implements Action {

    private static final Logger logger = LoggerFactory.getLogger(PrintAction.class);

    @Override
    public ActionResult process(Map<String, String> context) {
        ActionResult result = new ActionResult();
        Object data = null;
        String itemNames = context.get("item");
        if (SwitchUtil.isEmpty(itemNames)) {
            Map<String, InternalItem> items = switchEngine.getItems();
            if (!SwitchUtil.isEmpty(items)) {
                data = items.values();
            }
        } else {
            String[] itemNameArray = itemNames.split(",");
            Map<String, InternalItem> items = new LinkedHashMap<String, InternalItem>();
            for (String name : itemNameArray) {
                try {
                    InternalItem item = switchEngine.getItem(name);
                    items.put(name, item);
                } catch (ItemNotFoundException e) {
                    logger.warn("no item exists in switch-engine. item=" + name);
                }
            }

            if (!SwitchUtil.isEmpty(items)) {
                data = items.values();
            }
        }
        if (null != data) {
            result.initSuccessInfo(null, data);
        } else {
            result.initErrorInfo(ActionResult.ERROR_CODE_NO_RESULT, "nothing to list, maybe wrong parameters!");
        }

        return result;
    }

}
