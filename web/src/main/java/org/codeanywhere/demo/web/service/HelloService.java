package org.codeanywhere.demo.web.service;

import org.codeanywhere.common.eswitch.item.DefaultSwitchEngine;
import org.codeanywhere.common.eswitch.threshold.Threshold;

@Threshold(item = "test.hello", defaultValue = 10)
public class HelloService {
    private DefaultSwitchEngine switchEngine;

    public String hello() {
        if (switchEngine.isOn("test.switch.hello", false)) {
            System.out.println("hello service !");
        } else {
            System.out.println("bad service !");
        }
        return "123";
    }

    public void setSwitchEngine(DefaultSwitchEngine switchEngine) {
        this.switchEngine = switchEngine;
    }

}
