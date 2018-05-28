package com.net2plan.gui.plugins.networkDesign.openStack;

import org.openstack4j.api.OSClient;

public class OpenStackNetDelete {

    final private OSClient.OSClientV3 osClient;

    public OpenStackNetDelete(OSClient.OSClientV3 osClient){
        this.osClient = osClient;
    }

    //Identity
    public void deleteOpenStackUser(String id){
        this.osClient.identity().users().delete(id);
    }
    public void deleteOpenStackDomain(String id){
        this.osClient.identity().domains().delete(id);
    }
    public void deleteOpenStackEndpoint(String id){
        this.osClient.identity().serviceEndpoints().deleteEndpoint(id);
    }
    public void deleteOpenStackGroup(String id){
        this.osClient.identity().groups().delete(id);
    }
    public void deleteOpenStackPolicy(String id){
        this.osClient.identity().policies().delete(id);
    }
    public void deleteOpenStackProject(String id){
        this.osClient.identity().projects().delete(id);
    }
    public void deleteOpenStackRegion(String id){
        this.osClient.identity().regions().delete(id);
    }
    public void deleteOpenStackRole(String id){
        this.osClient.identity().roles().delete(id);
    }
    public void deleteOpenStackService(String id){
        this.osClient.identity().serviceEndpoints().delete(id);
    }
    public void deleteOpenStackCredential(String id){
        this.osClient.identity().users().delete(id);
    }

    //Compute
    public void deleteOpenStackExtension(Object id){
        this.osClient.compute().listExtensions().remove(id);
    }
    public void deleteOpenStackFlavor(String id){
        this.osClient.compute().flavors().delete(id);
    }
    public void deleteOpenStackFloatingIp(String id){
        this.osClient.compute().floatingIps().removeFloatingIP(id,"");
    }
    public void deleteOpenStackKeypair(String id){
        this.osClient.compute().keypairs().delete(id);
    }
    public void deleteOpenStackSecurityGroup(String id){
        this.osClient.compute().securityGroups().delete(id);
    }

    //Network
    public void deleteOpenStackRouter(String id){
        this.osClient.networking().router().delete(id);
    }
    public void deleteOpenStackNetwork(String id){
        this.osClient.networking().network().delete(id);
    }
    public void deleteOpenStackSubnet(String id){
        this.osClient.networking().subnet().delete(id);
    }
    public void deleteOpenStackPort(String id){
        this.osClient.networking().port().delete(id);
    }


}