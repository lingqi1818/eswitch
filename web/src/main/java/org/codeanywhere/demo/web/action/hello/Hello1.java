package org.codeanywhere.demo.web.action.hello;

import org.codeanywhere.easyRestful.base.BaseAction;

public class Hello1 extends BaseAction {

    public void execute() {
    }

    public void list(String start, String end) {
        System.out.println("start->" + start);
        System.out.println("end->" + end);

    }

    public void detail(String id) {
        System.out.println("detail id->" + id);
    }

    public void test(String s1, String s2, String s3, String s4) {
        execute();
        System.out.println("attr list ->");
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
    }

}
