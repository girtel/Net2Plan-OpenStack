package com.net2plan.gui.plugins.networkDesign.openStack;


import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.*;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.*;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.interfaces.networkDesign.NetPlan;

import java.awt.geom.Point2D;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.compute.SecurityGroup;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.network.*;

/**
 *
 * @author Manuel
 */
public class OpenStackNet
{

    private GUINetworkDesign callback;
    private final NetPlan np;
    private OSClientV3 os;
    private OpenStackNetCreate osnc;
    private OpenStackNetDelete osnd;

    // Network
    public final List<OpenStackRouter> list_osRouters = new ArrayList<> ();
    public final List<OpenStackNetwork> list_osNetworks = new ArrayList<> ();
    public final List<OpenStackSubnet> list_osSubnets = new ArrayList<> ();
    public final List<OpenStackPort> list_osPorts = new ArrayList<> ();

    //Identity
    public final List<OpenStackUser> list_osUsers = new ArrayList<> ();
    public final List<OpenStackCredential> list_osCredentials = new ArrayList<> ();
    public final List<OpenStackDomain> list_osDomains = new ArrayList<> ();
    public final List<OpenStackEndpoint> list_osEndpoints = new ArrayList<> ();
    public final List<OpenStackGroup> list_osGroups = new ArrayList<> ();
    public final List<OpenStackPolicy> list_osPolicies = new ArrayList<> ();
    public final List<OpenStackProject> list_osProjects = new ArrayList<> ();
    public final List<OpenStackRegion> list_osRegions = new ArrayList<> ();
    public final List<OpenStackRole> list_osRoles = new ArrayList<> ();
    public final List<OpenStackService> list_osServices = new ArrayList<> ();

    //Compute
    public final List<OpenStackExtension> list_osExtensions = new ArrayList<> ();
    public final List<OpenStackFlavor> list_osFlavours = new ArrayList<> ();
    public final List<OpenStackFloatingIp> list_osFloatingIpDns = new ArrayList<> ();
    public final List<OpenStackImage> list_osImages = new ArrayList<> ();
    public final List<OpenStackKeypair> list_osKeypairs = new ArrayList<> ();
    public final List<OpenStackLimit> list_osLimits = new ArrayList<> ();
    public final List<OpenStackQuota> list_osQuotas = new ArrayList<> ();
    public final List<OpenStackSecurityGroup> list_osSecurityGroups = new ArrayList<> ();
    public final List<OpenStackServer> list_osServers = new ArrayList<> ();

    //General
    public final List<OpenStackGeneralInformation> list_osInformation = new ArrayList<> ();

    public final  NetPlan getNetPlan () { return np; }

    public OpenStackNet()
    {
        this.np = new NetPlan();
    }

    public OpenStackNet (GUINetworkDesign callback, OSClientV3 os )
    {
        this.callback = callback;
        this.np = callback.getDesign();
        this.os=os;
        this.osnc = new OpenStackNetCreate(os);
        this.osnd = new OpenStackNetDelete(os);
    }

    public static OpenStackNet buildOpenStackNetFromServer(GUINetworkDesign callback, String os_auth_url, String os_username, String os_password, String os_project_name,String os_user_domain_name,String os_project_domain_id)
    {
        try
        {

            final OpenStackNet res = new TopologyCreator(callback, os_auth_url, os_username, os_password, os_project_name,os_user_domain_name,os_project_domain_id).getOpenStackNet();
            return res;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public OSClientV3 getOs(){
        return this.os;
    }

    public OpenStackUser addOpenStackUser (User user,String userId, String userName, String userDomainId, String userEmail, String userDescription)
    {
        final OpenStackUser res = OpenStackUser.createFromAddUser(this ,user, userId, userName, userDomainId, userEmail, userDescription);
        if(list_osUsers.contains(res)) return res;
        list_osUsers.add(res);
        return res;
    }

    public OpenStackNetwork addOpenStackNetwork(String networkId,String networkName,State networkStatus,NetworkType networkType,List<? extends Subnet> networkNeutronSubnets,String networkProviderPhyNet,String networkProviderSegID,List <String> networkSubnets,String networkTenantId,boolean networkIsAdminStateUp,boolean networkIsRouterExternal, boolean networkIsShared,Integer networkMTU)
    {
        final OpenStackNetwork res = OpenStackNetwork.createFromAddNetwork(this , networkId, networkName, networkStatus, networkType, networkNeutronSubnets,networkProviderPhyNet,networkProviderSegID,networkSubnets,networkTenantId,networkIsAdminStateUp,networkIsRouterExternal,networkIsShared,networkMTU);
        if(list_osNetworks.contains(res)) return res;
        list_osNetworks.add(res);
        return res;
    }

    public OpenStackSubnet addOpenStackSubnet (String subnetId,String subnetName,List<? extends Pool> subnetAllocationPools,String subnetCidr,List<String> subnetDnsNames,String subnetGateway,List<? extends HostRoute> subnetHostRoutes,IPVersionType subnetIpVersion,Ipv6AddressMode subnetIpv6AddressMode,Ipv6RaMode subnetIpv6RaMode,String subnetNetworkId,String subnetTenantId,boolean subnetIsDHCPEnabled)
    {
        final OpenStackSubnet res = OpenStackSubnet.createFromAddSubnet(this,subnetId, subnetName, subnetAllocationPools, subnetCidr, subnetDnsNames, subnetGateway,subnetHostRoutes,subnetIpVersion,subnetIpv6AddressMode,subnetIpv6RaMode,subnetNetworkId,subnetTenantId,subnetIsDHCPEnabled);
        if(list_osSubnets.contains(res)) return res;
        list_osSubnets.add(res);
        return res;
    }

    public OpenStackRouter addOpenStackRouter(String nodeId,String nodeName,String nodeTenantId, State nodeStatus,boolean nodeIsAdminStateUp,boolean nodeDistributed,List<? extends HostRoute> nodeRoutes, ExternalGateway nodeExternalGatewayInfo)
    {
        final OpenStackRouter res = OpenStackRouter.createFromAddRouter(this,nodeId, nodeName,nodeTenantId, nodeStatus, nodeIsAdminStateUp, nodeDistributed, nodeRoutes,nodeExternalGatewayInfo);
        if(list_osRouters.contains(res)) return res;
        list_osRouters.add(res);
        return res;
    }

    public OpenStackPort addOpenStackPort( String portId, String portName,  String portTenantId, Set<? extends AllowedAddressPair> portAllowedAddressPair,String portDeviceId, String portDeviceOwner, Set<? extends IP> portFixedIps,String portHostId,String portMacAddress,String portNetworkId,Map<String,Object> portProfile, List<String> portSecurityGroups,State portState,boolean isAdminStateUp,boolean portSecurityEnable)
    {
        final OpenStackPort res = OpenStackPort.createFromAddPort(this,portId,portName,portTenantId,portAllowedAddressPair,portDeviceId,portDeviceOwner,portFixedIps,portHostId,portMacAddress,portNetworkId,portProfile,portSecurityGroups,portState,isAdminStateUp,portSecurityEnable);
        if(list_osPorts.contains(res)) return res;
        list_osPorts.add(res);
        return res;
    }
    public OpenStackCredential addOpenStackCredential(String credentialId, String credentialUserId, String credentialProjectId, String credentialBlob, String credentialType,Map<String,String> credentialLinks)
    {
        final OpenStackCredential res = OpenStackCredential.createFromAddCredential(this,credentialId,credentialUserId,credentialProjectId,credentialBlob,credentialType,credentialLinks);
        if(list_osCredentials.contains(res)) return res;
        list_osCredentials.add(res);
        return res;
    }

    public OpenStackDomain addOpenStackDomain(String domainId, String domainName,String domainDescription, boolean domainEnabled, Map<String,String>domainLinks)
    {
        final OpenStackDomain res = OpenStackDomain.createFromAddDomain(this,domainId,domainName,domainDescription,domainEnabled,domainLinks);
        if(list_osDomains.contains(res)) return res;
        list_osDomains.add(res);
        return res;
    }
    public OpenStackEndpoint addOpenStackEndpoint(String endpointId, String endpointName, String endpointDescription, boolean endpointEnabled, Map<String,String> endpointLinks, String endpointRegion, String endpointRegionId, Facing endpointIface, String endpointServiceId, String endpointType, URL endpointUrl)
    {
        final OpenStackEndpoint res = OpenStackEndpoint.createFromAddEndpoint(this,endpointId,endpointName,endpointDescription,endpointEnabled,endpointLinks,endpointRegion,endpointRegionId,endpointIface,endpointServiceId,endpointType,endpointUrl);
        if(list_osEndpoints.contains(res)) return res;
        list_osEndpoints.add(res);
        return res;
    }

    public OpenStackGroup addOpenStackGroup(String groupId, String groupName, String groupDescription, String groupDomain, Map<String,String> groupLinks)
    {
        final OpenStackGroup res = OpenStackGroup.createFromAddGroup(this,groupId,groupName,groupDescription,groupDomain,groupLinks);
        if(list_osGroups.contains(res)) return res;
        list_osGroups.add(res);
        return res;
    }
    public OpenStackPolicy addOpenStackPolicy(String policyId, String policyUserId, String policyProjectId, String policyType, String policyBlob, Map<String,String> policyLinks)
    {
        final OpenStackPolicy res = OpenStackPolicy.createFromAddPolicy(this,policyId,policyUserId,policyProjectId,policyType,policyBlob,policyLinks);
        if(list_osPolicies.contains(res)) return res;
        list_osPolicies.add(res);
        return res;
    }

    public OpenStackProject addOpenStackProject(String projectId, String projectName, String projectParentId, String projectDomainId, Domain projectDomain, String projectDescription, String projectParents, String projectSubtree, boolean projectEnabled, Map<String,String>projectLinks)
    {
        final OpenStackProject res = OpenStackProject.createFromAddProject(this,projectId,projectName,projectParentId,projectDomainId,projectDomain,projectDescription,projectParents,projectSubtree,projectEnabled,projectLinks);
        if(list_osProjects.contains(res)) return res;
        list_osProjects.add(res);
        return res;
    }
    public OpenStackRegion addOpenStackRegion(String regionId,String regionDescription,String regionParentRegionId)
    {
        final OpenStackRegion res = OpenStackRegion.createFromAddRegion(this,regionId,regionDescription,regionParentRegionId);
        if(list_osRegions.contains(res)) return res;
        list_osRegions.add(res);
        return res;
    }
    public OpenStackRole addOpenStackRole(String roleId,String roleName,String roleDomainId,Map<String,String> roleLinks)
    {
        final OpenStackRole res = OpenStackRole.createFromAddRole(this,roleId,roleName,roleDomainId,roleLinks);
        if(list_osRoles.contains(res)) return res;
        list_osRoles.add(res);
        return res;
    }
    public OpenStackService addOpenStackService(String serviceId, String serviceName, String serviceDescription, String serviceType, Integer serviceVersion, boolean serviceEnabled, List<? extends Endpoint>serviceEndpoints, Map<String,String>serviceLinks)
    {
        final OpenStackService res = OpenStackService.createFromAddService(this,serviceId,serviceName,serviceDescription,serviceType,serviceVersion,serviceEnabled,serviceEndpoints,serviceLinks);
        if(list_osServices.contains(res)) return res;
        list_osServices.add(res);
        return res;
    }
//////////////////////

    public OpenStackExtension addOpenStackExtension(String extensionAlias, String extensionName, String extensionDescription, URI extensionNamespace, Date extensionUpdated, List<? extends Link> extensionLinks)
    {
        final OpenStackExtension res = OpenStackExtension.createFromAddExtension(this,extensionAlias,extensionName,extensionDescription,extensionNamespace,extensionUpdated,extensionLinks);
        if(list_osExtensions.contains(res)) return res;
        list_osExtensions.add(res);
        return res;
    }

    public OpenStackFlavor addOpenStackFlavor(String flavorId,String flavorName,Integer flavorDisk,Integer flavorEphemeral,Integer flavorRam,Integer flavorSwap,Integer flavorVcpus, boolean flavorDisabled, boolean flavorPublic,Integer flavorRxtxCap,float flavorRxtxFactor,Integer flavorRxtxQuota)
    {
        final OpenStackFlavor res = OpenStackFlavor.createFromAddFlavor(this,flavorId,flavorName,flavorDisk,flavorEphemeral,flavorRam,flavorSwap,flavorVcpus,flavorDisabled,flavorPublic,flavorRxtxCap,flavorRxtxFactor,flavorRxtxQuota);
        if(list_osFlavours.contains(res)) return res;
        list_osFlavours.add(res);
        return res;
    }
    public OpenStackFloatingIp addOpenStackFloatingIP(String floatingIPId, String floatingIPInstanceId, String floatingIPPool, String floatingIPFloatingIpAddress, String floatingIPFixedIpAddress)
    {
        final OpenStackFloatingIp res = OpenStackFloatingIp.createFromAddFloatingIp(this,floatingIPId,floatingIPInstanceId,floatingIPPool,floatingIPFloatingIpAddress,floatingIPFixedIpAddress);
        if(list_osFloatingIpDns.contains(res)) return res;
        list_osFloatingIpDns.add(res);
        return res;
    }

    public OpenStackImage addOpenStackImage(String imageId, String imageName, long imageSize, org.openstack4j.model.compute.Image.Status imageStatus, Date imageCreated, Date imageUpdated, Map<String,Object> imageMetaData, Integer imageProgress, Integer imageMinDisk, Integer imageMinRam, boolean imageSnapshot, List<? extends Link>imageLinks)
    {
        final OpenStackImage res = OpenStackImage.createFromAddImage(this,imageId,imageName,imageSize,imageStatus,imageCreated,imageUpdated,imageMetaData,imageProgress,imageMinDisk,imageMinRam,imageSnapshot,imageLinks);
        if(list_osImages.contains(res)) return res;
        list_osImages.add(res);
        return res;
    }
    public OpenStackKeypair addOpenStackKeypair(Integer keypairId,String keypairName,String keypairUserId,Date keypairCreatedA,boolean keypairDeleted,Date keypairDeletedAt,Date keypairUpdatedAt,String keypairFingerprint,String keypairPrivateKey,String keypairPublicKey)
    {
        final OpenStackKeypair res = OpenStackKeypair.createFromAddKeypair(this,keypairId,keypairName,keypairUserId,keypairCreatedA,keypairDeleted,keypairDeletedAt,keypairUpdatedAt,keypairFingerprint,keypairPrivateKey,keypairPublicKey);
        if(list_osKeypairs.contains(res)) return res;
        list_osKeypairs.add(res);
        return res;
    }
    public OpenStackLimit addOpenStackLimit(AbsoluteLimit limitAbsolute, List<? extends RateLimit> limitRate)
    {
        final OpenStackLimit res = OpenStackLimit.createFromAddLimit(this,limitAbsolute,limitRate);
        if(list_osLimits.contains(res)) return res;
        list_osLimits.add(res);
        return res;
    }

    public OpenStackQuota addOpenStackQuotaSet(String quotaSetId,Integer quotaSetCores,Integer quotaSetFloatingIps,Integer quotaSetGigabytes,Integer quotaSetKeyPairs,Integer quotaSetRam,Integer quotaSetInstances,Integer quotaSetVolumes,Integer quotaSetSecurityGroups,Integer quotaSetSecurityGroupRules,Integer quotaSetMetadataItems, Integer quotaSetInjectedFileContentBytes,Integer quotaSetInjectedFilePathBytes,Integer quotaSetInjectedFiles)
    {
        final OpenStackQuota res = OpenStackQuota.createFromAddQuota(this,quotaSetId,quotaSetCores,quotaSetFloatingIps,quotaSetGigabytes,quotaSetKeyPairs,quotaSetRam,quotaSetInstances,quotaSetVolumes,quotaSetSecurityGroups,quotaSetSecurityGroupRules,quotaSetMetadataItems,quotaSetInjectedFileContentBytes,quotaSetInjectedFilePathBytes,quotaSetInjectedFiles);
        if(list_osQuotas.contains(res)) return res;
        list_osQuotas.add(res);
        return res;
    }
    public OpenStackSecurityGroup addOpenStackSecurityGroup(String secGroupExtensionId, String secGroupExtensionName, String secGroupExtensionDescription, String secGroupExtensionTenantId, List<? extends SecGroupExtension.Rule> secGroupExtensionRules, List<? extends Link>secGroupExtensionLinks)
    {
        final OpenStackSecurityGroup res = OpenStackSecurityGroup.createFromAddSegurityGroup(this,secGroupExtensionId,secGroupExtensionName,secGroupExtensionDescription,secGroupExtensionTenantId,secGroupExtensionRules,secGroupExtensionLinks);
        if(list_osSecurityGroups.contains(res)) return res;
        list_osSecurityGroups.add(res);
        return res;
    }
    public OpenStackServer addOpenStackServer(String serverId, String serverName, String serverAccessIPv4, String serverAccessIPv6, Addresses serverAddresses, String serverAdminPass, String serverAvailabilityZone, String serverConfigDrive, Date serverCreated, Server.DiskConfig serverDiskConfig, Fault serverFault, Flavor serverFlavor, String serverFlavorId, String serverHost, String serverHostId, String serverHypervisorHostname, Image serverImage, String serverImageId, String serverInstanceName, String serverKeyName, Date serverLaunchedAt, Map<String,String> serverMetadata, List<? extends Link> serverLinks, List<String> serverOsExtendedVolumesAttached, String serverPowerState, Integer serverProgress, List<? extends SecurityGroup> serverSecurityGroups, Server.Status serverStatus, String serverTaskState, String serverTenantId, Date serverTerminatedAt, Date serverUpdate, String serverUserId, String serverUuid, String serverVmState)
    {
        final OpenStackServer res = OpenStackServer.createFromAddServer(this,serverId,serverName,serverAccessIPv4,serverAccessIPv6,serverAddresses,serverAdminPass,serverAvailabilityZone,serverConfigDrive,serverCreated,serverDiskConfig,serverFault,serverFlavor,serverFlavorId,serverHost,serverHostId,serverHypervisorHostname,serverImage,serverImageId,serverInstanceName,serverKeyName,serverLaunchedAt,serverMetadata,serverLinks,serverOsExtendedVolumesAttached,serverPowerState,serverProgress,serverSecurityGroups,serverStatus,serverTaskState,serverTenantId,serverTerminatedAt,serverUpdate,serverUserId,serverUuid,serverVmState);
        if(list_osServers.contains(res)) return res;
        list_osServers.add(res);
        return res;
    }

    public OpenStackGeneralInformation addOpenStackInformation()
    {
        String projectID = os.getToken().getProject().getId();
         OpenStackGeneralInformation res = OpenStackGeneralInformation.createFromAddInformation(this,projectID,"User", list_osUsers.size());
        if(!list_osInformation.contains(res)) {
            list_osInformation.add(res);
        }

        res = OpenStackGeneralInformation.createFromAddInformation(this,projectID,"Network", list_osNetworks.size());
        if(!list_osInformation.contains(res)) {
            list_osInformation.add(res);
        }

        res = OpenStackGeneralInformation.createFromAddInformation(this,projectID,"Router", list_osRouters.size());
        if(!list_osInformation.contains(res)) {
            list_osInformation.add(res);
        }

        res = OpenStackGeneralInformation.createFromAddInformation(this,projectID,"Subnet", list_osSubnets.size());
        if(!list_osInformation.contains(res)) {
            list_osInformation.add(res);
        }
        res = OpenStackGeneralInformation.createFromAddInformation(this,projectID,"Subnet", list_osSubnets.size());
        if(!list_osInformation.contains(res)) {
            list_osInformation.add(res);
        }
        return res;
    }

    public String getTopologyName () { return np.getNetPlan().getNetworkName(); }
    public String getTopologyDescription () { return np.getNetPlan().getNetworkDescription(); }
    public void setTopologyName (String name) { this.np.getNetPlan().setNetworkName(name); }

    public List<OpenStackRouter> getOpenStackRouters () { return Collections.unmodifiableList(list_osRouters); }
    public List<OpenStackUser> getOpenStackUsers () { return Collections.unmodifiableList(list_osUsers); }
    public List<OpenStackNetwork> getOpenStackNetworks () { return Collections.unmodifiableList(list_osNetworks); }
    public List<OpenStackSubnet> getOpenStackSubnets () { return Collections.unmodifiableList(list_osSubnets); }
    public List<OpenStackPort> getOpenStackPorts () { return Collections.unmodifiableList(list_osPorts); }
    public List<OpenStackCredential> getOpenStackCredentials () { return Collections.unmodifiableList(list_osCredentials); }
    public List<OpenStackGeneralInformation> getOpenStackInformation () { return Collections.unmodifiableList(list_osInformation); }
    public List<OpenStackDomain> getOpenStackDomains () { return Collections.unmodifiableList(list_osDomains); }
    public List<OpenStackEndpoint> getOpenStackEndpoints () { return Collections.unmodifiableList(list_osEndpoints); }
    public List<OpenStackPolicy> getOpenStackPolicies () { return Collections.unmodifiableList(list_osPolicies); }
    public List<OpenStackProject> getOpenStackProjects () { return Collections.unmodifiableList(list_osProjects); }
    public List<OpenStackRegion> getOpenStackRegions () { return Collections.unmodifiableList(list_osRegions); }
    public List<OpenStackRole> getOpenStackRoles () { return Collections.unmodifiableList(list_osRoles); }
    public List<OpenStackService> getOpenStackServices () { return Collections.unmodifiableList(list_osServices); }
    public List<OpenStackExtension> getOpenStackExtensions () { return Collections.unmodifiableList(list_osExtensions); }
    public List<OpenStackFlavor> getOpenStackFlavor () { return Collections.unmodifiableList(list_osFlavours); }
    public List<OpenStackFloatingIp> getOpenStackFloatingIpDns () { return Collections.unmodifiableList(list_osFloatingIpDns); }
    public List<OpenStackImage> getOpenStackImages () { return Collections.unmodifiableList(list_osImages); }
    public List<OpenStackKeypair> getOpenStackKeypairs () { return Collections.unmodifiableList(list_osKeypairs); }
    public List<OpenStackLimit> getOpenStackLimits () { return Collections.unmodifiableList(list_osLimits); }
    public List<OpenStackQuota> getOpenStackQuotas () { return Collections.unmodifiableList(list_osQuotas); }
    public List<OpenStackSecurityGroup> getOpenStackSecurityGroups () { return Collections.unmodifiableList(list_osSecurityGroups); }
    public List<OpenStackServer> getOpenStackServers () { return Collections.unmodifiableList(list_osServers); }

    public OpenStackNetworkElement getOpenStackNetworkElementByOpenStackId (String openStackId)
    {
        final List<OpenStackNetworkElement> allOpenStackNetworkElements = Lists.newArrayList();
        allOpenStackNetworkElements.addAll(list_osRouters);
        allOpenStackNetworkElements.addAll(list_osUsers);
        allOpenStackNetworkElements.addAll(list_osNetworks);
        allOpenStackNetworkElements.addAll(list_osSubnets);
        allOpenStackNetworkElements.addAll(list_osPorts);
        allOpenStackNetworkElements.addAll(list_osCredentials);
        allOpenStackNetworkElements.addAll(list_osInformation);

        allOpenStackNetworkElements.addAll(list_osDomains);
        allOpenStackNetworkElements.addAll(list_osEndpoints);
        allOpenStackNetworkElements.addAll(list_osPolicies);
        allOpenStackNetworkElements.addAll(list_osProjects);
        allOpenStackNetworkElements.addAll(list_osRegions);
        allOpenStackNetworkElements.addAll(list_osRoles);
        allOpenStackNetworkElements.addAll(list_osServices);

        allOpenStackNetworkElements.addAll(list_osExtensions);
        allOpenStackNetworkElements.addAll(list_osFlavours);
        allOpenStackNetworkElements.addAll(list_osImages);
        allOpenStackNetworkElements.addAll(list_osKeypairs);
        allOpenStackNetworkElements.addAll(list_osLimits);
        allOpenStackNetworkElements.addAll(list_osQuotas);
        allOpenStackNetworkElements.addAll(list_osSecurityGroups);

        allOpenStackNetworkElements.addAll(list_osServers);

        Optional<OpenStackNetworkElement> element = allOpenStackNetworkElements.stream().filter(n->n.getId() == openStackId).findFirst();
        if (element.isPresent()) return element.get();
        else return null;
    }

    public void distributeTopologyOverCircle()
    {
        // Node list
        final List<OpenStackRouter> nodeList = getOpenStackRouters();

        final int numNodes = nodeList.size();
        double index = 0.0;

        for (OpenStackRouter node : nodeList)
        {
            final double xCoord = Math.sin(Math.toRadians((360 * index) / numNodes));
            final double yCoord = -Math.cos(Math.toRadians((360 * index) / numNodes));

            node.setXYPositionMap(new Point2D.Double(xCoord, yCoord));

            index++;
        }



    }

    public void updateUserTable(){
        list_osUsers.clear();
        List<User> users = (List<User>) os.identity().users().list();
        for (User user : users)
            addOpenStackUser(user,user.getId(), user.getName(), user.getDomainId(), user.getEmail(), user.getDescription());
    }

    public void updateRouterTable(){
        list_osRouters.clear();

        callback.getDesign().removeAllNodes();
        List<Router> routers = (List<Router>) os.networking().router().list();
        for (Router router : routers)
            addOpenStackRouter(router.getId(), router.getName(), router.getTenantId(), router.getStatus(), router.isAdminStateUp(), router.getDistributed(), router.getRoutes(), router.getExternalGatewayInfo());

        distributeTopologyOverCircle();
    }

    public void updateNetworkTable(){
        list_osNetworks.clear();
        final List<Network> networks = (List<Network>) os.networking().network().list();
        for (Network net : networks)
            addOpenStackNetwork(net.getId(),net.getName(),net.getStatus(),net.getNetworkType(),net.getNeutronSubnets(),net.getProviderPhyNet(),net.getProviderSegID(),net.getSubnets(),net.getTenantId(),net.isAdminStateUp(),net.isRouterExternal(),net.isShared(),net.getMTU());

    }

    public void updateSubnetTable(){
        list_osSubnets.clear();
        final List<Subnet> subnets = (List<Subnet>) os.networking().subnet().list();
        for (Subnet subnet : subnets)
            addOpenStackSubnet(subnet.getId(),subnet.getName(),subnet.getAllocationPools(),subnet.getCidr(),subnet.getDnsNames(),subnet.getGateway(),subnet.getHostRoutes(),subnet.getIpVersion(),subnet.getIpv6AddressMode(),subnet.getIpv6RaMode(),subnet.getNetworkId(),subnet.getTenantId(),subnet.isDHCPEnabled());

    }

    public void updateInformationTable(){

        list_osInformation.clear();
        addOpenStackInformation();
    }

    public void updateAllTables(){

        updateUserTable();
        updateInformationTable();
        updateNetworkTable();
        updateRouterTable();
        updateSubnetTable();

    }

    public OpenStackNetCreate getOsnc(){
        return this.osnc;
    }
    public OpenStackNetDelete getOsnd(){
        return this.osnd;
    }

}