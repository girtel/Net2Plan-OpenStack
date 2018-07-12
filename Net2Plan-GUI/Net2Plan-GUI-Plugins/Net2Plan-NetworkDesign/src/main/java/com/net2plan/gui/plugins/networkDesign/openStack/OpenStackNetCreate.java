package com.net2plan.gui.plugins.networkDesign.openStack;

import org.apache.commons.lang.ObjectUtils;
import org.json.JSONObject;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Role;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.image.v2.ContainerFormat;
import org.openstack4j.model.image.v2.DiskFormat;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.openstack.OSFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static edu.emory.mathcs.utils.ConcurrencyUtils.setNumberOfThreads;
import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

public class OpenStackNetCreate{

    private OSClient.OSClientV3 osClientV3;
    private String system;

    public OpenStackNetCreate(OSClient.OSClientV3 osClientV3,String system){
        this.osClientV3 = osClientV3;
        this.system=system;
    }

    //Identity
    public void createOpenStackUser(JSONObject information){
        changeOs(Facing.INTERNAL);
        String userName = information.getString("Name");
        String password = information.getString("Password");
        String domainId = information.getString("Domain ID");
        String projectId = information.getString("Tenant ID");
        Boolean enabled = information.getBoolean("Enable");
    try{
        this.osClientV3.identity().users().create(Builders.user()
                .name(userName)
                .password(password)
                .domainId(domainId)
                .defaultProjectId(projectId)
                .enabled(enabled)
                .build());

    }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }
    }
    public void createOpenStackProject(JSONObject information){
        changeOs(Facing.INTERNAL);
        String projectName = information.getString("Name");
        String projectDomainId = information.getString("Domain ID");
        Boolean enabled = information.getBoolean("Enable");
    try{
        this.osClientV3.identity().projects().create(Builders.project()
                .name(projectName)
                .domainId(projectDomainId)
                .enabled(enabled)
                .build());

    }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }
    }
    public void createOpenStackDomain(JSONObject information){
        changeOs(Facing.INTERNAL);
        String domainName = information.getString("Name");
        Boolean enabled = information.getBoolean("Enable");
        try {
            this.osClientV3.identity().domains().create(Builders.domain()
                    .name(domainName)
                    .enabled(enabled)
                    .build());
        }catch(Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackEndpoint(JSONObject information){
        changeOs(Facing.INTERNAL);
        String endpointName = information.getString("Name");
        String endpointServiceId = information.getString("Service ID");
        String endpointUrl = information.getString("URL");
        Facing endpointFacing = Facing.valueOf(information.getString("Facing"));
        try {
            this.osClientV3.identity().serviceEndpoints().createEndpoint(Builders.endpoint()
                    .name(endpointName)
                    .iface(endpointFacing)
                    .serviceId(endpointServiceId)
                    .url(new URL(endpointUrl))
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            logPanel();
        }
    }
    public void createOpenStackService(JSONObject information){
        changeOs(Facing.INTERNAL);
        String serviceName = information.getString("Name");
        String serviceType = information.getString("Service type");
        try {
            this.osClientV3.identity().serviceEndpoints().create(Builders.service()
                    .name(serviceName)
                    .type(serviceType)
                    .build());
        }catch(Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackRegion(JSONObject information){
        changeOs(Facing.INTERNAL);

        String regionDescription = information.getString("Description");
        String regionId = information.getString("Region ID");
        try {
            this.osClientV3.identity().regions().create(Builders.region()
                    .id(regionId)
                    .description(regionDescription)
                    .build());
        }catch(Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackCredential(JSONObject information){
        changeOs(Facing.INTERNAL);
        String credentialUserId = information.getString("User ID");
        String credentialProjectId = information.getString("Tenant ID");
        String blob = information.getString("Blob");
        String type = information.getString("Type");
        try {
            this.osClientV3.identity().credentials().create(Builders.credential()
                    .userId(credentialUserId)
                    .projectId(credentialProjectId)
                    .blob(blob)
                    .type(type)
                    .build());
        }catch (Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackGroup(JSONObject information){
        changeOs(Facing.INTERNAL);
        String groupName = information.getString("Name");
        String groupDomainId = information.getString("Domain ID");
        try {

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
        changeOs(Facing.INTERNAL);
        String policyUserId = information.getString("User ID");
        String policyProjectId = information.getString("Tenant ID");
        String blob = information.getString("Blob");
        String type = information.getString("Type");

        try {
            this.osClientV3.identity().policies().create(Builders.policy()
                    .userId(policyUserId)
                    .projectId(policyProjectId)
                    .blob(blob)
                    .type(type)
                    .build());
        }catch(Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackRole(JSONObject information){
        changeOs(Facing.INTERNAL);
        String roleName = information.getString("Name");
        String roleDomainId = information.getString("Domain ID");
        try {
             this.osClientV3.identity().roles().create(Builders.role()
                    .name(roleName)
                     .domainId(roleDomainId)
                    .build());
        }catch(Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }

    //Create networks elements in OpenStack
    public void createOpenStackRouter(JSONObject information){
        changeOs(Facing.PUBLIC);
        String routerName = information.getString("Name");
        String routerTenantId = information.getString("Tenant ID");
        String routerGateway = information.getString("Network ID");
        try{
        this.osClientV3.networking().router().create(Builders.router()
                .name(routerName)
                .tenantId(routerTenantId)
                .externalGateway(routerGateway,true)
                .build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }
    public void createOpenStackNetwork(JSONObject information){
        changeOs(Facing.PUBLIC);
        String networkTenantId = information.getString("Tenant ID");
        String networkName = information.getString("Name");
        NetworkType networkType = null;
        try {
             networkType = NetworkType.valueOf(information.getString("Network type"));
        }catch(Exception ex){
            createOpenStackSubnet(information);
            return;
        }
        Boolean networkExternal = information.getBoolean("IsExternal");

        try{
            this.osClientV3.networking().network().create(Builders.network()
                    .name(networkName)
                    .tenantId(networkTenantId)
                    .networkType(networkType)
                    .isRouterExternal(networkExternal)
                    .build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void createOpenStackSubnet(JSONObject information){
        changeOs(Facing.PUBLIC);
        String subnetName = information.getString("Name");
        String subnetNetworkId = information.getString("Network ID");
        String subnetTenantId = information.getString("Tenant ID");
        IPVersionType versionType = IPVersionType.valueOf(information.getString("IP version"));
        String subnetCidr = prepareCidr(information.getString("Cidr"));

        try {
            this.osClientV3.networking().subnet().create(Builders.subnet()
                    .name(subnetName)
                    .networkId(subnetNetworkId)
                    .ipVersion(versionType)
                    .cidr(subnetCidr)
                    .tenantId(subnetTenantId)
                    .build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public String prepareCidr(String cidr){

        String[] parts = cidr.split("-");
        String part1 = String.valueOf(Integer.parseInt(parts[0])); // ###
        String part2 = String.valueOf(Integer.parseInt(parts[1])); //###
        String part3 = String.valueOf(Integer.parseInt(parts[2])); // ###
        String part4 = parts[3]; //###/##
        String[] newParts = part4.split("/");
        String part5 = String.valueOf(Integer.parseInt(newParts[0])); // ###
        String part6 = String.valueOf(Integer.parseInt(newParts[1])); //##
        String response = part1+"."+part2+"."+part3+"."+part5+"/"+part6;

        return response;
    }
    public void createOpenStackPort(JSONObject information){
        changeOs(Facing.PUBLIC);
        String portName = information.getString("Name");
        String portNetworkId = information.getString("Network ID");
        String portFixedIp = prepareIp(information.getString("Fixed IP"));
        String portSubnetId = information.getString("Subnet ID");
        try {
            this.osClientV3.networking().port().create(Builders.port()
                    .name(portName)
                    .networkId(portNetworkId)
                    .fixedIp(portFixedIp,portSubnetId)
                    .build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public String prepareIp(String ip){
        String[] parts = ip.split("-");
        String part1 = String.valueOf(Integer.parseInt(parts[0])); // ###
        String part2 = String.valueOf(Integer.parseInt(parts[1])); //###
        String part3 = String.valueOf(Integer.parseInt(parts[2])); // ###
        String part4 =  String.valueOf(Integer.parseInt(parts[3])); // ###
         String response = part1+"."+part2+"."+part3+"."+part4;

        return response;
    }

    //Create compute elements in OpenStack
    public void createOpenStackServer(JSONObject information){
        changeOs(Facing.PUBLIC);
        String serverName = information.getString("Name");
        String serverFlavorId = information.getString("Flavor ID");
        String serverImageId = information.getString("Image ID");
        String serverNetworkId = information.getString("Port ID");
        try {
            ServerCreate sc = this.osClientV3.compute().servers().serverBuilder()
                    .name(serverName)
                    .flavor(serverFlavorId)
                    .image(serverImageId)
                    .addNetworkPort(serverNetworkId)
                    .build();

            Server server = this.osClientV3.compute().servers().boot(sc);
        }catch( Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackFlavor(JSONObject information){
        changeOs(Facing.PUBLIC);
        String flavorName= information.getString("Name");
        int flavorRam= Integer.parseInt(information.getString("RAM"));
        int flavorVcpus= Integer.parseInt(information.getString("CPUs"));

        try {
            this.osClientV3.compute().flavors().create(Builders.flavor()
                    .name(flavorName)
                    .vcpus(flavorVcpus)
                    .ram(flavorRam)
                    .build());
        }catch(Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackFloatingIp(JSONObject information){
        changeOs(Facing.PUBLIC);
        String floIpServerIp = prepareIp(information.getString("Server IP"));
        String floIp = prepareIp(information.getString("Floating IP"));
        try {
            this.osClientV3.compute().floatingIps().addFloatingIP(floIpServerIp, floIp);
        }catch(Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackKeypair(JSONObject information){
        changeOs(Facing.PUBLIC);
        String keypairName = information.getString("Name");
        try {
            this.osClientV3.compute().keypairs().create(keypairName,null);
        }catch (Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackSecurityGroup(JSONObject information){
        changeOs(Facing.PUBLIC);
        String securityGroupName = information.getString("Name");
        String securityGroupDescription = information.getString("Description");
        try {
            this.osClientV3.compute().securityGroups().create(securityGroupName, securityGroupDescription);
        }catch (Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }

    ///Glance
    public void createOpenStackTask(JSONObject information){
        changeOs(Facing.PUBLIC);
         String type = information.getString("Type");
        Map<String, Object> input = new HashMap<String, Object>();
        try {
            this.osClientV3.imagesV2().tasks().create(
                    Builders.taskBuilder()
                            .type(type)
                            .input(input)
                            .build()
            );
        }catch (Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void createOpenStackImage(JSONObject information){
      changeOs(Facing.PUBLIC);
        Payload<File> payload = null;


            String path = information.getString("PATH");
            String name = information.getString("NAME");
            payload = Payloads.create(new File(path));

            System.out.println(path + "    "+ name);

            Image image = this.osClientV3.imagesV2().create(
                    Builders.imageV2()
                            .name(name)
                            .containerFormat(ContainerFormat.BARE)
                            .visibility(org.openstack4j.model.image.v2.Image.ImageVisibility.PUBLIC)
                            .diskFormat(DiskFormat.QCOW2)
                            .minDisk((long)0)
                            .minRam((long)0)
                            .build()
            );

            ActionResponse upload = osClientV3.imagesV2().upload(
                    image.getId(),
                    payload,
                    image);

        System.out.println("Create");

        ControlWindow controlWindow =  new ControlWindow(information,this.osClientV3.getToken());
        submit(controlWindow);
        //submit(controlWindow);


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
class ControlWindow implements Runnable{

    private JSONObject information;
    private OSClient.OSClientV3 osClientV3;


    public ControlWindow (JSONObject information, Token token){
        this.information = information;
        this.osClientV3 = OSFactory.clientFromToken(token);
    }

    @Override
    public void run() {

        Payload<File> payload = null;
        try {

            String path = information.getString("PATH");
            String name = information.getString("NAME");
            payload = Payloads.create(new File(path));

            System.out.println(path + "    "+ name);

            Image image = this.osClientV3.imagesV2().create(
                    Builders.imageV2()
                            .name(name)
                            .containerFormat(ContainerFormat.BARE)
                            .visibility(org.openstack4j.model.image.v2.Image.ImageVisibility.PUBLIC)
                            .diskFormat(DiskFormat.QCOW2)
                            .minDisk((long)0)
                            .minRam((long)0)
                            .build()
            );

            ActionResponse upload = osClientV3.imagesV2().upload(
                    image.getId(),
                    payload,
                    image);


            System.out.println("Finish upload");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}