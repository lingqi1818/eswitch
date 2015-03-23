package org.codeanywhere.demo.web.service;

import org.codeanywhere.common.eswitch.degraded.DefaultDegradedMethodHandler;

public class HelloHandler implements DefaultDegradedMethodHandler {

    @Override
    public Object handle(Object[] argements) {
        System.out.println("default handler");
        return "default";
    }

}
