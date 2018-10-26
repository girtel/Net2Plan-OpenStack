package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.gui.plugins.utils.MyRunnable;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.identity.v3.Token;

import javax.swing.*;

import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

public class OpenStackNetDelete {

    private OpenStackClient openStackClient;

    public OpenStackNetDelete(OpenStackClient openStackClient){
         this.openStackClient = openStackClient;
    }

    //Identity
    public void deleteOpenStackUser(String id){

        this.openStackClient.getClient().identity().users().delete(id);
    }
    public void deleteOpenStackDomain(String id){
        this.openStackClient.getClient().identity().domains().delete(id);
    }
    public void deleteOpenStackEndpoint(String id){
        this.openStackClient.getClient().identity().serviceEndpoints().deleteEndpoint(id);
    }
    public void deleteOpenStackGroup(String id){
        this.openStackClient.getClient().identity().groups().delete(id);
    }
    public void deleteOpenStackPolicy(String id){
        this.openStackClient.getClient().identity().policies().delete(id);
    }
    public void deleteOpenStackProject(String id){
        this.openStackClient.getClient().identity().projects().delete(id);
    }
    public void deleteOpenStackRegion(String id){
        this.openStackClient.getClient().identity().regions().delete(id);
    }
    public void deleteOpenStackRole(String id){
        this.openStackClient.getClient().identity().roles().delete(id);
    }
    public void deleteOpenStackService(String id){
        this.openStackClient.getClient().identity().serviceEndpoints().delete(id);
    }
    public void deleteOpenStackCredential(String id){
        this.openStackClient.getClient().identity().credentials().delete(id);
    }

    //Network
    public void deleteOpenStackRouter(String id){
        try{
        this.openStackClient.getClient().networking().router().delete(id);
        }catch(Exception ex){

            logPanel(ex);

        }
    }
    public void deleteOpenStackNetwork(String id){

        try{

            this.openStackClient.getClient().networking().network().delete(id);

        }catch(Exception ex){

            logPanel(ex);

        }
    }
    public void deleteOpenStackSubnet(String id){
        try{
        this.openStackClient.getClient().networking().subnet().delete(id);
        }catch(Exception ex){

            logPanel(ex);

        }
    }
    public void deleteOpenStackPort(String id){
        try{
        this.openStackClient.getClient().networking().port().delete(id);
        }catch(Exception ex){

            logPanel(ex);

        }
    }

    //Compute
    public void deleteOpenStackFlavor(String id){
        this.openStackClient.getClient().compute().flavors().delete(id);
    }
    public void deleteOpenStackFloatingIp(String id){
        this.openStackClient.getClient().compute().floatingIps().removeFloatingIP(id,"");
    }
    public void deleteOpenStackKeypair(String id){
        this.openStackClient.getClient().compute().keypairs().delete(id);
    }
    public void deleteOpenStackSecurityGroup(String id){

        this.openStackClient.getClient().compute().securityGroups().delete(id);
    }
    public void deleteOpenStackImage(String id){
        try {
            openStackClient.getClient().imagesV2().delete(id);
        }catch (Exception ex){
            logPanel(ex);
        }
    }

    public  void logPanel(Exception ex){
        JOptionPane.showMessageDialog(null, "Ups! One problem ocurred. Show console");
        ex.printStackTrace();
    }

   /*
    public void changeOs(Facing facing){

        Token token = osClientV3.getToken();
        MyRunnable newR;

        if(system.equals("ubuntu")) {
            newR = new MyRunnable(token, facing);
            submit(newR);
            this.osClientV3 = newR.getOs();
        }

    }
   */
}
