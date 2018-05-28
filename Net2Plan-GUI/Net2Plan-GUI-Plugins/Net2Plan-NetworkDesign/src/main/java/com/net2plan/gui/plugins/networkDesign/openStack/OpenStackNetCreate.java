package com.net2plan.gui.plugins.networkDesign.openStack;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;

import java.util.List;

public class OpenStackNetCreate {

    final private OSClient.OSClientV3 osClient;

    public OpenStackNetCreate(OSClient.OSClientV3 osClient){
        this.osClient = osClient;
    }

    //Identity
    public void createOpenStackUser(List<String> response){
        String userName = response.get(0);
        String userDescription= response.get(0);
        String userPassword= response.get(0);
        String  userEmail= response.get(0);
        System.out.println(userName);
        /*this.osClient.identity().users().create(Builders.user()
                .name(userName)
                .description(userDescription)
                .password(userPassword)
                .email(userEmail)
                .domainId(this.osClient.getToken().getUser().getDomainId())
                .build());*/
    }
    public void createOpenStackDomain(String domainName, String domainDescription){
        this.osClient.identity().domains().create(Builders.domain()
        .name(domainName)
        .description(domainDescription)
        .build());
    }
    public void createOpenStackEndpoint(String endpointName, String endpointDescription,String endpointRegionId, String endpointServiceId,String endpointType){
        this.osClient.identity().serviceEndpoints().createEndpoint(Builders.endpoint()
        .description(endpointDescription)
        .name(endpointName)
        .regionId(endpointRegionId)
        .serviceId(endpointServiceId)
        .type(endpointType)
        .build());
    }
    public void createOpenStackGroup(String groupName, String groupDescription, String groupDomainId){
        this.osClient.identity().groups().create(Builders.group()
        .name(groupName)
        .description(groupDescription)
        .domainId(groupDomainId)
        .build());
    }
    public void createOpenStackPolicy(String policyUserId,String policyProjectId,String policyType,String policyBlob){
        this.osClient.identity().policies().create(Builders.policy()
        .userId(policyUserId)
        .projectId(policyProjectId)
        .type(policyType)
        .blob(policyBlob)
        .build());
    }
    public void createOpenStackProject(String projectName,String projectDescription,String projectDomainId,String projectParentId,String projectParents,String projectSubtree){
        this.osClient.identity().projects().create(Builders.project()
        .name(projectName)
        .description(projectDescription)
        .domainId(projectDomainId)
        .parentId(projectParentId)
        .parents(projectParents)
        .subtree(projectSubtree)
        .build());
    }
    public void createOpenStackRegion(String regionDescription, String parentRegionId){
        this.osClient.identity().regions().create(Builders.region()
        .description(regionDescription)
        .parentRegionId(parentRegionId)
        .build());
    }
    public void createOpenStackRole(String roleName,String roleDomainId){
        this.osClient.identity().roles().create(Builders.role()
        .name(roleName)
        .domainId(roleDomainId)
        .build());
    }
    public void createOpenStackService(String serviceName,String serviceDescription, String serviceType){
        this.osClient.identity().serviceEndpoints().create(Builders.service()
        .name(serviceName)
        .description(serviceDescription)
        .type(serviceType)
        .build());
    }
    public void createOpenStackCredential(String credentialUserId,String credentialProjectId, String credentialType,String credentialBlob){
        this.osClient.identity().credentials().create(Builders.credential()
                .userId(credentialUserId)
                .projectId(credentialProjectId)
                .type(credentialType)
                .blob(credentialBlob)
                .build());
    }

    //Compute
    public void createOpenStackFlavor(String flavorName,Integer flavorDisk,Integer flavorEphemeral,Integer flavorRam,Integer flavorRxTxFactor,Integer flavorSwap,Integer flavorVcpu){
        this.osClient.compute().flavors().create(Builders.flavor()
        .name(flavorName)
        .disk(flavorDisk)
        .ephemeral(flavorEphemeral)
        .ram(flavorRam)
        .rxtxFactor(flavorRxTxFactor)
        .swap(flavorSwap)
        .vcpus(flavorVcpu)
        .build());
    }
    public void createOpenStackFloatingIp(String floIpServerIp,String floIp){
        this.osClient.compute().floatingIps().addFloatingIP(floIpServerIp,floIp);
    }
    public void createOpenStackKeypair(String keypairName){
        this.osClient.compute().keypairs().create(keypairName,null);
    }
    public void createOpenStackSecurityGroup(String securityGroupName){
        this.osClient.compute().securityGroups().create(securityGroupName,null);
    }
    public void createOpenStackServer(String serverName, String serverFlavor, String serverImage, String serverKeypair, String serverUserData, List<String> serverNetworks){
        this.osClient.compute().servers().serverBuilder()
                .name(serverName)
                .flavor(serverFlavor)
                .image(serverImage)
                .keypairName(serverKeypair)
                .userData(serverUserData)
                .networks(serverNetworks)
                .build();
    }

    //Network
    public void createOpenStackRouter(String routerName, String routerTenantId, String routerExternalGateway){
        this.osClient.networking().router().create(Builders.router()
        .name(routerName)
        .tenantId(routerTenantId)
        .externalGateway(routerExternalGateway)
        .build());
    }
    public void createOpenStackNetwork(String networkName, String networkSegmentId,String networkTenantId){
        this.osClient.networking().network().create(Builders.network()
        .name(networkName)
        .segmentId(networkSegmentId)
        .tenantId(networkTenantId)
        .build());
    }
    public void createOpenStackSubnet(String subnetName, String subnetNetworkId, String subnetGateway, String subnetTenantId){
        this.osClient.networking().subnet().create(Builders.subnet()
        .name(subnetName)
        .networkId(subnetNetworkId)
        .gateway(subnetGateway)
        .tenantId(subnetTenantId)
        .build());
    }
    public void createOpenStackPort(String subnetName,String subnetNetworkId, String subnetDeviceId,String subnetHostId,String subnetMacAddress,String subnetSecurityGroup){
        this.osClient.networking().port().create(Builders.port()
        .name(subnetName)
        .networkId(subnetNetworkId)
        .deviceId(subnetDeviceId)
        .hostId(subnetHostId)
        .macAddress(subnetMacAddress)
        .securityGroup(subnetSecurityGroup)
        .build());
    }


}
