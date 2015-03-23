package org.codeanywhere.common.eswitch.example;

import org.codeanywhere.common.eswitch.threshold.Threshold;

/**
 * @author chenke
 */
@Threshold(item = "profile.api", defaultValue = 200)
public class ProfileApi {

    public void test1() {
        // sleep();
    }

    public void test2() {
        // sleep();
    }

    protected void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }
    }

}
