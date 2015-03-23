package org.codeanywhere.common.eswitch.example;

import java.util.concurrent.atomic.AtomicInteger;

import org.codeanywhere.common.eswitch.SwitchEngine;
import org.codeanywhere.common.eswitch.item.InternalSwitchEngine;
import org.codeanywhere.common.eswitch.threshold.Threshold;
import org.codeanywhere.common.eswitch.threshold.ThresholdException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * <pre>
 * 演示开关和阀值
 * 
 * 开关:
 * if (switchEngine.isOn("api.on.return", true)) {
 *     ret = "on   :" + count.get();
 * } else {
 *     ret = "fasle:" + count.get();
 * }
 * 
 * 通过http修改开关值,开启
 *      => http://localhost:30000/eswitch/config?action=modify&item=api.on.return&api.on.return={on:true,threshold:0}
 * 则,输出 on   :xxx (并发的数量)
 * 通过http修改开关值,关闭
 *      => http://localhost:30000/eswitch/config?action=modify&item=api.on.return&api.on.return={on:true,threshold:0}
 * 则,输出 false:xxx (并发的数量)
 * 
 * 阀值:
 * @Threshold(item = "api.on", defaultValue = 50) // 上限50个并发.
 * public String example() {
 * }
 * 
 * 通过http修改开关值:
 *      => http://localhost:30000/eswitch/config?action=modify&item=api.on&api.on={on:true,threshold:20}
 * 则并发下降到20
 * </pre>
 * 
 * @author chenke
 */
public class SwitchExample {

    private SwitchEngine           switchEngine;
    private volatile AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("example/eswitch.xml");
        SwitchExample example = ((SwitchExample) ctx.getBean("example"));
        Worker worker = new Worker(example);

        while (true) {
            example.sleep(10000);
            new Thread(worker).start();
            System.out.println(((InternalSwitchEngine) example.getSwitchEngine()).getItems());
        }

    }

    @Threshold(item = "api.on", defaultValue = 50)
    public String example() {
        count.incrementAndGet();
        sleep(5000);
        String ret;
        if (switchEngine.isOn("api.on.return", true)) {
            ret = "on   :" + count.get();
        } else {
            ret = "fasle:" + count.get();
        }
        count.decrementAndGet();
        return ret;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public SwitchEngine getSwitchEngine() {
        return switchEngine;
    }

    public void setSwitchEngine(SwitchEngine switchEngine) {
        this.switchEngine = switchEngine;
    }

    public static final class Worker implements Runnable {

        private SwitchExample example;

        public Worker(SwitchExample example){
            this.example = example;
        }

        @Override
        public void run() {
            try {
                System.out.println(example.example());
            } catch (ThresholdException e) {
                System.out.println(e.getType());
            }
        }

    }

}
