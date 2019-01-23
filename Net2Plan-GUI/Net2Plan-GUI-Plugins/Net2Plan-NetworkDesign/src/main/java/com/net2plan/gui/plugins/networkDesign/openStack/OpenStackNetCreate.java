package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackProject;
import com.net2plan.gui.plugins.utils.MyRunnable;
import org.apache.commons.lang.ObjectUtils;
import org.json.JSONObject;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.VNCConsole;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Role;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.image.v2.ContainerFormat;
import org.openstack4j.model.image.v2.DiskFormat;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.network.*;
import org.openstack4j.openstack.OSFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static edu.emory.mathcs.utils.ConcurrencyUtils.setNumberOfThreads;
import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

public class OpenStackNetCreate{

    private OSClient.OSClientV3 osClientV3;
    private OpenStackClient openStackClient;
    public OpenStackNetCreate(OpenStackClient openStackClient){
        this.osClientV3 = openStackClient.getClient();
        this.openStackClient = openStackClient;
    }

    //Identity
    public void createOpenStackUser(JSONObject information){

        final String userName = information.getString("Name");
        final String password = information.getString("Password");
        final String domainName= information.getString("Domain ID");
        final String projectName = information.getString("Tenant ID");
        final String roleName = information.getString("Role ID");
        final Boolean enabled = information.getBoolean("Enable");

        final String projectId = openStackClient.openStackProjects.stream().filter(n->n.getProjectName().equals(projectName)).findFirst().get().getId();
        final String domainId = openStackClient.openStackDomains.stream().filter(n->n.getDomainName().equals(domainName)).findFirst().get().getId();
        final String roleId = openStackClient.openStackRoles.stream().filter(n->n.getRoleName().equals(roleName)).findFirst().get().getId();

        try{
        User user = openStackClient.getClient().identity().users().create(Builders.user()
                .name(userName)
                .password(password)
                .domainId(domainId)
                .defaultProjectId(projectId)
                .enabled(enabled)
                .build());

        ActionResponse grantProjectRole = openStackClient.getClient().identity().roles().grantProjectUserRole(projectId, user.getId(), roleId);

        System.out.println(grantProjectRole.isSuccess());
        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }
    }
    public void createOpenStackProject(JSONObject information){

        final String projectName = information.getString("Name");
        final String projectDomainName = information.getString("Domain ID");
        final Boolean enabled = information.getBoolean("Enable");
        final String projectDomainId = openStackClient.openStackDomains.stream().filter(n->n.getDomainName().equals(projectDomainName)).findFirst().get().getId();

        try{

           openStackClient.keystone.createProject(projectName,projectDomainId,enabled);

       }catch(Exception ex){

        logPanel();
        ex.printStackTrace();
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

        String routerName = information.getString("Name");
        String routerTenantName = information.getString("Tenant ID");
        String routerGatewayName = information.getString("Network ID");

        final String routerTenantId = openStackClient.openStackProjects.stream().filter(n->n.getProjectName().equals(routerTenantName)).findFirst().get().getId();
        final String routerGateway = openStackClient.openStackNetworks.stream().filter(n->n.getName().equals(routerGatewayName)).findFirst().get().getId();

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

        String networkTenantName = information.getString("Tenant ID");
        String networkName = information.getString("Name");
        String networkProvider= information.getString("Provider ID");
        NetworkType networkType  = NetworkType.valueOf(information.getString("Network type"));
        Boolean networkExternal = information.getBoolean("IsExternal");
        final String projectId = openStackClient.openStackProjects.stream().filter(n->n.getProjectName().equals(networkTenantName)).findFirst().get().getId();

        try{
            this.osClientV3.networking().network().create(Builders.network()
                    .name(networkName)
                    .tenantId(projectId)
                    .networkType(networkType)
                    .isRouterExternal(networkExternal)
                    .segmentId(networkProvider)
                    .build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void createOpenStackSubnet(JSONObject information){

        String subnetName = information.getString("Name");
        String subnetNetworkName = information.getString("Network ID");
        String subnetTenantName = information.getString("Tenant ID");
        IPVersionType versionType = IPVersionType.valueOf(information.getString("IP version"));
        String subnetCidr = information.getString("Cidr");

        final String subnetNetworkId = openStackClient.openStackNetworks.stream().filter(n->n.getName().equals(subnetNetworkName)).findFirst().get().getId();
        final String projectId = openStackClient.openStackProjects.stream().filter(n->n.getProjectName().equals(subnetTenantName)).findFirst().get().getId();

        try {
            this.osClientV3.networking().subnet().create(Builders.subnet()
                    .name(subnetName)
                    .networkId(subnetNetworkId)
                    .ipVersion(versionType)
                    .cidr(subnetCidr)
                    .tenantId(projectId)
                    .build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void createOpenStackPort(JSONObject information){

        String portSubnetName= information.getString("Subnet ID");
        String portDeviceName = information.getString("Router ID");

        final String portSubnetId = openStackClient.openStackSubnets.stream().filter(n->n.getName().equals(portSubnetName)).findFirst().get().getId();
        final String portDeviceId = openStackClient.openStackRouters.stream().filter(n->n.getRouterName().equals(portDeviceName)).findFirst().get().getId();

        try {
            // Attach an External Interface
            RouterInterface iface = osClientV3.networking().router()
                    .attachInterface(portDeviceId, AttachInterfaceType.SUBNET, portSubnetId);

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }

    /*
    public String prepareIp(String ip){
        String[] parts = ip.split("-");
        String part1 = String.valueOf(Integer.parseInt(parts[0])); // ###
        String part2 = String.valueOf(Integer.parseInt(parts[1])); //###
        String part3 = String.valueOf(Integer.parseInt(parts[2])); // ###
        String part4 =  String.valueOf(Integer.parseInt(parts[3])); // ###
         String response = part1+"."+part2+"."+part3+"."+part4;

        return response;
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
*/
    //Create compute elements in OpenStack
    public void createOpenStackServer(JSONObject information){

        String serverName = information.getString("Name");
        String serverFlavorName = information.getString("Flavor ID");
        String serverImageName = information.getString("Image ID");
        String serverNetworkName = information.getString("Network ID");

        final String serverNetworkId = openStackClient.openStackNetworks.stream().filter(n->n.getName().equals(serverNetworkName)).findFirst().get().getId();
        final String serverImageId = openStackClient.openStackImages.stream().filter(n->n.getName().equals(serverImageName)).findFirst().get().getId();
        final String serverFlavorId = openStackClient.openStackFlavors.stream().filter(n->n.getFlavorName().equals(serverFlavorName)).findFirst().get().getId();

        List<String> list = new ArrayList<>();
        list.add(serverNetworkId);

        try {
            ServerCreate sc = this.osClientV3.compute().servers().serverBuilder()
                    .name(serverName)
                    .flavor(serverFlavorId)
                    .image(serverImageId)
                    .networks(list)
                  // .addNetworkPort(serverNetworkId)
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

        String serverName = information.getString("Server ID");
        String poolName = information.getString("Pool Name");

        final String serverId = openStackClient.openStackServers.stream().filter(n->n.getServerName().equals(serverName)).findFirst().get().getId();

        try {


            String pool = poolName;
            FloatingIP floatingIP = osClientV3.compute().floatingIps().allocateIP(pool);

            ActionResponse response = osClientV3.compute().floatingIps().addFloatingIP(this.osClientV3.compute().servers().get(serverId), floatingIP.getFixedIpAddress(), floatingIP.getFloatingIpAddress());

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



    }



    public  void logPanel(){
        JOptionPane.showMessageDialog(null, "Ups! One problem ocurred. Show console");
    }
    public void changeOs(Facing facing){
        Token token = osClientV3.getToken();
        MyRunnable newR;


            newR = new MyRunnable(token, facing);
            submit(newR);
            this.osClientV3 = newR.getOs();


    }


}