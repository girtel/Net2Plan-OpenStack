package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackFloatingIp;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackServer;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackProject;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackUser;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackImageV2;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.utils.MyRunnable;
import com.net2plan.gui.plugins.utils.OpenStackUtils;
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

    private OpenStackClient openStackClient;

    public OpenStackNetCreate(OpenStackClient openStackClient){
        this.openStackClient = openStackClient;
    }

    public void createOpenStackNetworkElement(ViewEditTopologyTablesPane.AJTableType ajTableType, JSONObject information){

        try {
            if (ajTableType == ViewEditTopologyTablesPane.AJTableType.USERS) {

                final String userName = information.getString("Name");
                final String password = information.getString("Password");
                final String domainName= information.getString("Domain ID");
                final String projectName = information.getString("Tenant ID");
                final String roleName = information.getString("Role ID");
                final Boolean enabled = information.getBoolean("Enable");

                final String projectId = openStackClient.openStackProjects.stream().filter(n->n.getProjectName().equals(projectName)).findFirst().get().getId();
                final String domainId = openStackClient.openStackDomains.stream().filter(n->n.getDomainName().equals(domainName)).findFirst().get().getId();
                final String roleId = openStackClient.openStackRoles.stream().filter(n->n.getRoleName().equals(roleName)).findFirst().get().getId();


                User user = openStackClient.getClient().identity().users().create(Builders.user()
                        .name(userName)
                        .password(password)
                        .domainId(domainId)
                        .defaultProjectId(projectId)
                        .enabled(enabled)
                        .build());

               ActionResponse actionResponse = openStackClient.getClient().identity().roles().grantProjectUserRole(projectId, user.getId(), roleId);

                if (!actionResponse.isSuccess()) {
                    OpenStackUtils.openStackLogDialog(actionResponse.getFault());
                }

            } else if (ajTableType == ViewEditTopologyTablesPane.AJTableType.PROJECTS) {

                final String projectName = information.getString("Name");
                final String projectDomainName = information.getString("Domain ID");
                final Boolean enabled = information.getBoolean("Enable");
                final String projectDomainId = openStackClient.openStackDomains.stream().filter(n->n.getDomainName().equals(projectDomainName)).findFirst().get().getId();

                openStackClient.keystone.createProject(projectName,projectDomainId,enabled);

            } else if (ajTableType == ViewEditTopologyTablesPane.AJTableType.NETWORKS) {

                String networkTenantName = information.getString("Tenant ID");
                String networkName = information.getString("Name");
                String networkProvider= information.getString("Provider ID");
                NetworkType networkType  = NetworkType.valueOf(information.getString("Network type"));
                Boolean networkExternal = information.getBoolean("IsExternal");
                final String projectId = openStackClient.openStackProjects.stream().filter(n->n.getProjectName().equals(networkTenantName)).findFirst().get().getId();


                if(openStackClient.isThisClientAdmin()) {
                    this.openStackClient.getClient().networking().network().create(Builders.network()
                            .name(networkName)
                            .tenantId(projectId)
                            .networkType(networkType)
                            .isRouterExternal(networkExternal)
                            .segmentId(networkProvider)
                            .build());
                }else {
                    this.openStackClient.getClient().networking().network().create(Builders.network()
                            .name(networkName)
                            .tenantId(projectId)
                           // .networkType(networkType)
                            .isRouterExternal(networkExternal)
                            //.segmentId(networkProvider)
                            .build());
                }

            } else if (ajTableType == ViewEditTopologyTablesPane.AJTableType.SUBNETS) {

                String subnetName = information.getString("Name");
                String subnetNetworkName = information.getString("Network ID");
                String subnetTenantName = information.getString("Tenant ID");
                IPVersionType versionType = IPVersionType.valueOf(information.getString("IP version"));
                String subnetCidr = information.getString("Cidr");

                final String subnetNetworkId = openStackClient.openStackNetworks.stream().filter(n->n.getName().equals(subnetNetworkName)).findFirst().get().getId();
                final String projectId = openStackClient.openStackProjects.stream().filter(n->n.getProjectName().equals(subnetTenantName)).findFirst().get().getId();


                this.openStackClient.getClient().networking().subnet().create(Builders.subnet()
                        .name(subnetName)
                        .networkId(subnetNetworkId)
                        .ipVersion(versionType)
                        .cidr(subnetCidr)
                        .tenantId(projectId)
                        .build());


            } else if (ajTableType == ViewEditTopologyTablesPane.AJTableType.ROUTERS) {

                String routerName = information.getString("Name");
                String routerTenantName = information.getString("Tenant ID");
                String routerGatewayName = information.getString("Network ID");

                final String routerTenantId = openStackClient.openStackProjects.stream().filter(n->n.getProjectName().equals(routerTenantName)).findFirst().get().getId();
                final String routerGateway = openStackClient.openStackNetworks.stream().filter(n->n.getName().equals(routerGatewayName)).findFirst().get().getId();

                if(openStackClient.isThisClientAdmin()) {
                    this.openStackClient.getClient().networking().router().create(Builders.router()
                            .name(routerName)
                            .tenantId(routerTenantId)
                            .externalGateway(routerGateway, true)
                            .build());

                }else{
                    this.openStackClient.getClient().networking().router().create(Builders.router()
                            .name(routerName)
                            .tenantId(routerTenantId)
                            //.externalGateway(routerGateway, true)
                            .build());
                }

            } else if (ajTableType == ViewEditTopologyTablesPane.AJTableType.PORTS) {

                String portSubnetName= information.getString("Subnet ID");
                String portDeviceName = information.getString("Router ID");

                final String portSubnetId = openStackClient.openStackSubnets.stream().filter(n->n.getName().equals(portSubnetName)).findFirst().get().getId();
                final String portDeviceId = openStackClient.openStackRouters.stream().filter(n->n.getRouterName().equals(portDeviceName)).findFirst().get().getId();

                    // Attach an External Interface
                    RouterInterface iface = openStackClient.getClient().networking().router()
                            .attachInterface(portDeviceId, AttachInterfaceType.SUBNET, portSubnetId);


            }else if (ajTableType == ViewEditTopologyTablesPane.AJTableType.SERVERS) {


                String serverName = information.getString("Name");
                String serverFlavorName = information.getString("Flavor ID");
                String serverImageName = information.getString("Image ID");
                String serverNetworkName = information.getString("Network ID");
                String securityGroupName = information.getString("Security group ID");

                final String serverNetworkId = openStackClient.openStackNetworks.stream().filter(n->n.getName().equals(serverNetworkName)).findFirst().get().getId();
                final String serverImageId = openStackClient.openStackImages.stream().filter(n->n.getName().equals(serverImageName)).findFirst().get().getId();
                final String serverFlavorId = openStackClient.openStackFlavors.stream().filter(n->n.getFlavorName().equals(serverFlavorName)).findFirst().get().getId();
                final String serverSecurtyGroupId = openStackClient.openStackSecurityGroups.stream().filter(n->n.getName().equals(securityGroupName)).findFirst().get().getId();

                List<String> list = new ArrayList<>();
                list.add(serverNetworkId);

                try {
                    ServerCreate sc = this.openStackClient.getClient().compute().servers().serverBuilder()
                            .name(serverName)
                            .flavor(serverFlavorId)
                            .image(serverImageId)
                            .networks(list)
                            .addSecurityGroup(serverSecurtyGroupId)
                            .build();

                    Server server = this.openStackClient.getClient().compute().servers().boot(sc);

                }catch (Exception ex){
                    OpenStackUtils.openStackLogDialog(ex.getMessage());
                }

            }else if (ajTableType == ViewEditTopologyTablesPane.AJTableType.FLOATINGIPS) {

                String serverName = information.getString("Server ID");
                String poolName = information.getString("Pool Name");

                final String serverId = openStackClient.openStackServers.stream().filter(n->n.getServerName().equals(serverName)).findFirst().get().getId();

                String pool = poolName;
                FloatingIP floatingIP = this.openStackClient.getClient().compute().floatingIps().allocateIP(pool);

                System.out.println(floatingIP.getId());
                ActionResponse actionResponse = this.openStackClient.getClient().compute().floatingIps().addFloatingIP(this.openStackClient.getClient().compute().servers().get(serverId), floatingIP.getFixedIpAddress(), floatingIP.getFloatingIpAddress());

                if (!actionResponse.isSuccess()) {
                    OpenStackUtils.openStackLogDialog(actionResponse.getFault());
                }

            }else if (ajTableType == ViewEditTopologyTablesPane.AJTableType.IMAGES) {

                Payload<File> payload = null;


                String path = information.getString("PATH");
                String name = information.getString("NAME");
                payload = Payloads.create(new File(path));

              //  System.out.println(path + "    "+ name);

                Image image = this.openStackClient.getClient().imagesV2().create(
                        Builders.imageV2()
                                .name(name)
                                .containerFormat(ContainerFormat.BARE)
                                .visibility(org.openstack4j.model.image.v2.Image.ImageVisibility.PUBLIC)
                                .diskFormat(DiskFormat.QCOW2)
                                .minDisk((long)0)
                                .minRam((long)0)
                                .build()
                );

                ActionResponse actionResponse = openStackClient.getClient().imagesV2().upload(
                        image.getId(),
                        payload,
                        image);

                if (!actionResponse.isSuccess()) {
                    OpenStackUtils.openStackLogDialog(actionResponse.getFault());
                }

            }else if(ajTableType == ViewEditTopologyTablesPane.AJTableType.SECURITYGROUPS){

                String name = information.getString("Name");
                String description = information.getString("Description");
                try {
                    this.openStackClient.getClient().compute().securityGroups().create(name, description);
                }catch (Exception ex){
                    OpenStackUtils.openStackLogDialog(ex.getMessage());
                }
            }

        }catch (Exception ex){

            OpenStackUtils.openStackLogDialog(ex.getMessage());
            ex.printStackTrace();
        }
    }


}