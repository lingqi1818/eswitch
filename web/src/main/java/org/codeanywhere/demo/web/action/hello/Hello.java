package org.codeanywhere.demo.web.action.hello;

import org.codeanywhere.demo.web.service.HelloService;
import org.codeanywhere.easyRestful.base.Action;
import org.codeanywhere.easyRestful.base.RequestContext;
import org.codeanywhere.easyRestful.base.annotation.Json;
import org.codeanywhere.easyRestful.base.annotation.Jsonp;
import org.codeanywhere.easyRestful.base.annotation.Request;
import org.codeanywhere.easyRestful.base.annotation.SpringBean;


public class Hello implements Action {

    @Request
    private RequestContext context;
    @SpringBean
    private HelloService   helloService;

    public void execute() {
        String res = helloService.hello();
        System.out.println(res);
        context.put("fish", "i'm a gold fish !");
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

    @Json
    public JsonObject testJson(String s1, String s2, String s3, String s4) {
        JsonObject o = new JsonObject();
        o.setA("1");
        o.setB("2");
        return o;
    }

    @Jsonp(callbackMethodName = "callback")
    public JsonObject testJsonp(String s1, String s2, String s3, String s4) {
        JsonObject o = new JsonObject();
        o.setA("1");
        o.setB("2");
        return o;
    }

    @SuppressWarnings("unused")
    private class JsonObject {
        private String a;
        private String b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

    }

}
