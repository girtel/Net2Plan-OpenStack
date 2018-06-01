package com.net2plan.gui.plugins.networkDesign.openStack;

import org.json.JSONObject;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.network.IPVersionType;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class OpenStackNetCreate{

    private OSClient.OSClientV3 osClientV3;

    public OpenStackNetCreate(OSClient.OSClientV3 osClientV3){
        this.osClientV3 = osClientV3;
    }

    //Identity
    public void createOpenStackUser(JSONObject information){

        String userName = information.getString("Name");
    try{
        this.osClientV3.identity().users().create(Builders.user()
                .name(userName)
                .build());

    }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }
    }
    public void createOpenStackProject(JSONObject information){

        String projectName = information.getString("Name");
        String projectDomainId = information.getString("Domain ID");
    try{
        this.osClientV3.identity().projects().create(Builders.project()
                .name(projectName)
                .domainId(this.osClientV3.getToken().getProject().getDomainId())
                .domain(this.osClientV3.getToken().getProject().getDomain())
                .parentId(this.osClientV3.getToken().getProject().getParentId())
                .subtree(this.osClientV3.getToken().getProject().getSubtree())
                .enabled(true)
                .build());

    }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }
    }
    public void createOpenStackDomain(JSONObject information){

        String domainName = information.getString("Name");

        this.osClientV3.identity().domains().create(Builders.domain()
                .name(domainName)
                .build());
    }
    public void createOpenStackEndpoint(JSONObject information){

        String endpointName = information.getString("Name");
        String endpointServiceId = information.getString("Service ID");
        String endpointUrl = information.getString("URL");
        try {
            this.osClientV3.identity().serviceEndpoints().createEndpoint(Builders.endpoint()
                    .name(endpointName)
                    .iface(Facing.PUBLIC)
                    .serviceId(endpointServiceId)
                    .url(new URL(endpointUrl))
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            logPanel();
        }
    }
    public void createOpenStackService(JSONObject information){

        String serviceName = information.getString("Name");
        String serviceType = information.getString("Type");
        this.osClientV3.identity().serviceEndpoints().create(Builders.service()
                .name(serviceName)
                .type(serviceType)
                .build());
    }
    public void createOpenStackRegion(JSONObject information){
        String regionDescription = information.getString("Description");
        this.osClientV3.identity().regions().create(Builders.region()
                .description(regionDescription)
                .build());
    }
    public void createOpenStackCredential(JSONObject information){

        String credentialUserId = information.getString("User ID");
        String credentialProjectId = information.getString("Project ID");
        String credentialType = information.getString("Type");
        String credentialBlob = information.getString("Blob");

        this.osClientV3.identity().credentials().create(Builders.credential()
                .userId(credentialUserId)
                .projectId(credentialProjectId)
                .type(credentialType)
                .blob(credentialBlob)
                .build());
    }
    public void createOpenStackGroup(JSONObject information){
        try {
            String groupName = information.getString("Name");
            String groupDomainId = information.getString("Domain ID");
            this.osClientV3.identity().groups().create(Builders.group()
                    .name(groupName)
                    .domainId(groupDomainId)
                    .build());
        }catch(Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackPolicy(JSONObject information){

        String policyUserId = information.getString("User ID");
        String policyProjectId = information.getString("Project ID");
        String policyType = information.getString("Type");
        String policyBlob = information.getString("Blob");

        this.osClientV3.identity().policies().create(Builders.policy()
                .userId(policyUserId)
                .projectId(policyProjectId)
                .type(policyType)
                .blob(policyBlob)
                .build());
    }
    public void createOpenStackRole(JSONObject information){
        String roleName = information.getString("Name");
        this.osClientV3.identity().roles().create(Builders.role()
                .name(roleName)
                .build());
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


    //Create compute elements in OpenStack
    public void createOpenStackServer(JSONObject information){

        String serverName = information.getString("Name");

        this.osClientV3.compute().servers().serverBuilder()
                .name(serverName)
                .build();
    }
    public void createOpenStackFlavor(JSONObject information){

        String flavorName= information.getString("Name");
        int flavorRam= Integer.parseInt(information.getString("Ram"));
        int flavorVcpus= Integer.parseInt(information.getString("Vcpus"));
        this.osClientV3.compute().flavors().create(Builders.flavor()
                .name(flavorName)
                .vcpus(flavorVcpus)
                .ram(flavorRam)
                .build());
    }
    public void createOpenStackFloatingIp(JSONObject information){
        String floIpServerIp = information.getString("Server IP");
        String floIp = information.getString("Floating IP");
        this.osClientV3.compute().floatingIps().addFloatingIP(floIpServerIp,floIp);
    }
    public void createOpenStackKeypair(JSONObject information){
        String keypairName = information.getString("Name");
        this.osClientV3.compute().keypairs().create(keypairName,null);
    }
    public void createOpenStackSecurityGroup(JSONObject information){
        String securityGroupName = information.getString("Name");
        String securityGroupDescription = information.getString("Description");
        this.osClientV3.compute().securityGroups().create(securityGroupName,securityGroupDescription);
    }
    public void createOpenStackImage(){
        Payload<URL> payload = null;
        try {
            payload = Payloads.create(new URL(new File("").toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image image = this.osClientV3.images().create(Builders.image()
                .name("Cirros 0.3.0 x64")
                .isPublic(true)
                .containerFormat(ContainerFormat.BARE)
                .diskFormat(DiskFormat.QCOW2)
                .build(), payload);
    }
    public  void logPanel(){
        JOptionPane.showMessageDialog(null, "Ups! One problem ocurred. Show console");
    }

}
