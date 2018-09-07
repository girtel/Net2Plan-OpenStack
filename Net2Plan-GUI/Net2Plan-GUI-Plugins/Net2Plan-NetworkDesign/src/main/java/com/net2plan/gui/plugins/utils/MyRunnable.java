package com.net2plan.gui.plugins.utils;

import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;

public class MyRunnable  implements Runnable {
    private OSClient.OSClientV3 os;

    public MyRunnable(Token token, Facing facing) {
        this.os = OSFactory.clientFromToken(token,facing);
    }
    public OSClient.OSClientV3 getOs(){
        return os;
    }
    public void run() {

        // can now use the client :)
    }
}