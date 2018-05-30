package com.net2plan.gui.plugins.networkDesign.openStack;

import org.openstack4j.api.OSClient;

import javax.swing.*;

public class OpenStackNetDelete {

    private OSClient.OSClientV3 osClientV3;

    public OpenStackNetDelete(OSClient.OSClientV3 osClientV3){
        this.osClientV3 = osClientV3;
    }

    //Network
    public void deleteOpenStackRouter(String id){
        try{
        this.osClientV3.networking().router().delete(id);
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void deleteOpenStackNetwork(String id){
        try{
        this.osClientV3.networking().network().delete(id);
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void deleteOpenStackSubnet(String id){
        try{
        this.osClientV3.networking().subnet().delete(id);
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void deleteOpenStackPort(String id){
        try{
        this.osClientV3.networking().port().delete(id);
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public  void logPanel(){
        JOptionPane.showMessageDialog(null, "Ups! One problem ocurred. Show console");
    }
}
