package com.net2plan.gui.plugins.networkDesign.openStack;

import org.openstack4j.api.OSClient;

import javax.swing.*;

public class OpenStackNetDelete {

    private OSClient.OSClientV3 osClientV3;

    public OpenStackNetDelete(OSClient.OSClientV3 osClientV3){
        this.osClientV3 = osClientV3;
    }

    //Identity
    public void deleteOpenStackUser(String id){
        this.osClientV3.identity().users().delete(id);
    }
    public void deleteOpenStackDomain(String id){
        this.osClientV3.identity().domains().delete(id);
    }
    public void deleteOpenStackEndpoint(String id){
        this.osClientV3.identity().serviceEndpoints().deleteEndpoint(id);
    }
    public void deleteOpenStackGroup(String id){
        this.osClientV3.identity().groups().delete(id);
    }
    public void deleteOpenStackPolicy(String id){
        this.osClientV3.identity().policies().delete(id);
    }
    public void deleteOpenStackProject(String id){
        this.osClientV3.identity().projects().delete(id);
    }
    public void deleteOpenStackRegion(String id){
        this.osClientV3.identity().regions().delete(id);
    }
    public void deleteOpenStackRole(String id){
        this.osClientV3.identity().roles().delete(id);
    }
    public void deleteOpenStackService(String id){
        this.osClientV3.identity().serviceEndpoints().delete(id);
    }
    public void deleteOpenStackCredential(String id){
        this.osClientV3.identity().credentials().delete(id);
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
