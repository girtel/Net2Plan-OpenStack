package com.net2plan.gui.plugins.networkDesign.openStack;

import org.json.JSONObject;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.IPVersionType;

import javax.swing.*;
import java.util.List;

public class OpenStackNetCreate{

    private OSClient.OSClientV3 osClientV3;

    public OpenStackNetCreate(OSClient.OSClientV3 osClientV3){
        this.osClientV3 = osClientV3;
    }

    //Create networks elements in OpenStack
    public void createOpenStackRouter(JSONObject information){

        String routerName = information.getString("Name");

        try{
        this.osClientV3.networking().router().create(Builders.router()
                .name(routerName)
                .build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }
    public void createOpenStackNetwork(JSONObject information){

        String networkName = information.getString("Name");
        try{
            this.osClientV3.networking().network().create(Builders.network()
                    .name(networkName)
                    .build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void createOpenStackSubnet(JSONObject information){

        String subnetName = information.getString("Name");
        String subnetNetworkId = information.getString("Network ID");
        IPVersionType versionType;
        if(information.getString("IP version").equals("V6")){
            versionType   = IPVersionType.V6;
        }else{
            versionType   = IPVersionType.V4;
        }

        String subnetCidr = information.getString("Cidr");
        try {
            this.osClientV3.networking().subnet().create(Builders.subnet()
                    .name(subnetName)
                    .networkId(subnetNetworkId)
                    .ipVersion(versionType)
                    .cidr(subnetCidr)
                    .build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void createOpenStackPort(JSONObject information){

        String subnetName = information.getString("Name");
        String subnetNetworkId = information.getString("Network ID");
        try {
            this.osClientV3.networking().port().create(Builders.port()
                    .name(subnetName)
                    .networkId(subnetNetworkId)
                    .build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public  void logPanel(){
        JOptionPane.showMessageDialog(null, "Ups! One problem ocurred. Show console");
    }
}
