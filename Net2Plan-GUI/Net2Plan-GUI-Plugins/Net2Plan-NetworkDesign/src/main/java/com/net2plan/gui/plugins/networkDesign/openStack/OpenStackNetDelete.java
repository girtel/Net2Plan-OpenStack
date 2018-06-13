package com.net2plan.gui.plugins.networkDesign.openStack;

import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.identity.v3.Token;

import javax.swing.*;

import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

public class OpenStackNetDelete {

    private OSClient.OSClientV3 osClientV3;
    private String system;
    public OpenStackNetDelete(OSClient.OSClientV3 osClientV3,String system){
        this.osClientV3 = osClientV3;
        this.system=system;
    }

    //Identity
    public void deleteOpenStackUser(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().users().delete(id);
    }
    public void deleteOpenStackDomain(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().domains().delete(id);
    }
    public void deleteOpenStackEndpoint(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().serviceEndpoints().deleteEndpoint(id);
    }
    public void deleteOpenStackGroup(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().groups().delete(id);
    }
    public void deleteOpenStackPolicy(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().policies().delete(id);
    }
    public void deleteOpenStackProject(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().projects().delete(id);
    }
    public void deleteOpenStackRegion(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().regions().delete(id);
    }
    public void deleteOpenStackRole(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().roles().delete(id);
    }
    public void deleteOpenStackService(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().serviceEndpoints().delete(id);
    }
    public void deleteOpenStackCredential(String id){
        changeOs(Facing.INTERNAL);
        this.osClientV3.identity().credentials().delete(id);
    }

    //Network
    public void deleteOpenStackRouter(String id){
        changeOs(Facing.PUBLIC);
        try{
        this.osClientV3.networking().router().delete(id);
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void deleteOpenStackNetwork(String id){
        changeOs(Facing.PUBLIC);
        try{
        this.osClientV3.networking().network().delete(id);
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void deleteOpenStackSubnet(String id){
        changeOs(Facing.PUBLIC);
        try{
        this.osClientV3.networking().subnet().delete(id);
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void deleteOpenStackPort(String id){
        changeOs(Facing.PUBLIC);
        try{
        this.osClientV3.networking().port().delete(id);
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }

    //Compute
    public void deleteOpenStackFlavor(String id){
        changeOs(Facing.PUBLIC);
        this.osClientV3.compute().flavors().delete(id);
    }
    public void deleteOpenStackFloatingIp(String id){
        changeOs(Facing.PUBLIC);
        this.osClientV3.compute().floatingIps().removeFloatingIP(id,"");
    }
    public void deleteOpenStackKeypair(String id){
        changeOs(Facing.PUBLIC);
        this.osClientV3.compute().keypairs().delete(id);
    }
    public void deleteOpenStackSecurityGroup(String id){
        changeOs(Facing.PUBLIC);
        this.osClientV3.compute().securityGroups().delete(id);
    }


    public  void logPanel(){
        JOptionPane.showMessageDialog(null, "Ups! One problem ocurred. Show console");
    }
    public void changeOs(Facing facing){
        Token token = osClientV3.getToken();
        MyRunnable newR;

        if(system.equals("ubuntu")) {
            newR = new MyRunnable(token, facing);
            submit(newR);
            this.osClientV3 = newR.getOs();
        }

    }
}
