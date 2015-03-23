使用方法

a)限流配置
 
    <bean id="switchEngine" class="org.codeanywhere.common.eswitch.item.DefaultSwitchEngine" />
 
 
    <bean id="api" class="org.codeanywhere.common.eswitch.threshold.Api" />
    <bean id="api2" class="org.codeanywhere.common.eswitch.threshold.Api2" />
 
 
    <bean id="xmlConfigurationStrategy" class="org.codeanywher.common.eswitch.threshold.configuration.XmlConfigurationStrategy">
        <property name="config">
            <list>
                <value>org.codeanywhere.common.eswitch.threshold.Api2#func1 api2.func1 1</value>
                <value>org.codeanywhere.common.eswitch.threshold.Api3 api3 3</value>
                <value>org.codeanywhere.common.eswitch.threshold api2.func2 2</value><!-- 包级别 -->
            </list>
        </property>
    </bean>
 
 
    <bean id="thresholdInterceptor" class="org.codeanywhere.common.eswitch.threshold.DummyThresholdInterceptorX">
        <property name="strategies">
            <list>
                <ref local="xmlConfigurationStrategy" />
            </list>
        </property>
    </bean>
 
 
    <bean id="thresholdPointcut" class="org.springframework.aop.support.JdkRegexpMethodPointcut" scope="prototype">
        <property name="patterns">
            <list>
                <value>org.codeanywhere.common.eswitch.threshold.Api.*</value>
                <value>org.codeanywhere.common.eswitch.threshold.Api2.*</value>
                <value>org.codeanywhere.common.eswitch.threshold.Api3.*</value>
            </list>
        </property>
    </bean>
 
 
    <aop:config>
        <aop:advisor advice-ref="thresholdInterceptor" pointcut-ref="thresholdPointcut" />
    </aop:config>
 
另外支持annonation方式：
@Threshold(item = "test.hello", defaultValue = 10)//类级别
public class HelloService {
    private DefaultSwitchEngine switchEngine;
      @Threshold(item = "test.hello1", defaultValue = 10)  //方法级别
      public void hello() {
        if (switchEngine.isOn("test.switch.hello", false)) {
            System.out.println("hello service !");
        } else {
            System.out.println("bad service !");
        }
    }
 
    public void setSwitchEngine(DefaultSwitchEngine switchEngine) {
        this.switchEngine = switchEngine;
    }
}
b)默认降级逻辑注入（粗粒度）
提供针对每个方法的默认处理逻辑
 
<bean id="defaultDegradedHandlerInterceptor" class="org.codeanywhere.common.eswitch.degraded.DefaultDegradedHandlerInterceptor" >
 
    <property name="handlerMap">
 
    <map>
 
    <entry key="org.codeanywhere.demo.web.service.HelloService.hello" value-ref="helloHandler" />
 
    </map>
 
    </property>
 
    </bean>
<bean id="helloHandler" class="org.codeanywhere.demo.web.service.HelloHandler"/>
 
c)限流之后的降级逻辑处理
try{
xxxservice.doBusiness();
}catch(ThresholdException e){
 //降级逻辑
}
d)结合开关的降级处理流程
if (switchEngine.isOn("vip", false)) {
            //正常逻辑
        } else {
           //降级逻辑
        }
 
e)异步执行拦截器：
<bean id="asyncInvokeInterceptor" class="org.codeanywhere.common.eswitch.async.AsyncInvokeInterceptor" init-method="init">
 
    <property name="corePoolSize" value="20" />
 
    <property name="maxPoolSize" value="200" />
 
    <property name="keepAliveTime" value="5000" />
 
    <property name="queueSize" value="10240" />
 
    <property name="handlerMap">
 
    <map>
 
    <entry key="org.codeanywhere.demo.web.service.HelloService.hello" value-ref="timeoutHandler" />
 
    </map>
 
    </property>
 
    </bean>
     
    <bean id="timeoutHandler" class="org.codeanywhere.demo.web.service.HelloHandler"/>
 
f)动态调整开关：
chendeMacBook-Pro:bin chenke$ ./eswitch.py 
Usage: eswitch.py [-options] <on/off> <threshold>
 
Options:
  -h, --help            show this help message and exit
  -l HOST, --host=HOST  specify eswitch host
  -p PORT, --port=PORT  specify eswitch port
  -i ITEM, --item=ITEM  specify eswitch item' name
  -c ACTION, --action=ACTION
                        specify eswitch action

常用命令实例
1. print
bin/eswitch.py -l localhost -c print
2. reload
bin/eswitch.py -l localhost -a reload
3. modify
bin/eswitch.py -l localhost -c modify -i inquiry.service true 10
默认端口是30000,如果端口是其他值的话,通过-p参数指定<br>
bin/eswitch.py -l localhost -p 30001 -c modify -i inquiry.service true 10
 
 
命令执行后,会实时通知SwitchEngine.
