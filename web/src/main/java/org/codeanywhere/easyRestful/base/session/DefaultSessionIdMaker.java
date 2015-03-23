package org.codeanywhere.easyRestful.base.session;

import org.codeanywhere.easyRestful.base.session.util.UUID;

public class DefaultSessionIdMaker extends UUID implements SessionIDMaker {

    public String makeNewId() {
        return super.nextID();
    }

    public static void main(String args[]) {
        DefaultSessionIdMaker maker = new DefaultSessionIdMaker();
        for (int i = 0; i < 100; i++) {
            System.out.println(maker.makeNewId());
        }
    }

}
