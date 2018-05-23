package com.net2plan.gui.plugins.networkDesign.openStack;


import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.interfaces.networkDesign.Net2PlanException;
import java.util.List;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.*;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.OSFactory;

/**
 *
 * @author Manuel
 */
class TopologyCreator
{
    private final OSClientV3 os;
    private final GUINetworkDesign callback;

    TopologyCreator(GUINetworkDesign callback, String os_auth_url, String os_username, String os_password, String os_project_name, String os_user_domain_name , String os_project_domain_id)
    {

            os = OSFactory.builderV3()
                    .endpoint(os_auth_url)
                    .credentials(os_username, os_password, Identifier.byName(os_user_domain_name))
                    .scopeToProject(Identifier.byName(os_project_name), Identifier.byId(os_project_domain_id))
                    .authenticate();

            this.callback = callback;
    }


    OpenStackNet getOpenStackNet()
    {
        /* Empty NetPlan */
        final OpenStackNet osn = new OpenStackNet(callback,os);

        /* Get elements */

        /*Identity elements*/
        final List<Credential> credentials = (List<Credential>) os.identity().credentials().list();
        final List<Domain> domains = (List<Domain>) os.identity().domains().list();
        final List<Endpoint> endpoints = (List<Endpoint>) os.identity().serviceEndpoints().listEndpoints();
        final List<Group> groups = (List<Group>) os.identity().groups().list();
        final List<Policy> policies = (List<Policy>) os.identity().policies().list();
        final List<User> users = (List<User>) os.identity().users().list();
        final List<? extends Project> projects = (List<? extends Project>)os.identity().tokens().getProjectScopes(os.getToken().getUser().getId());
        final List<Region> regions = (List<Region>)os.identity().regions().list();
        final List<Role> roles = (List<Role>)os.identity().roles().list();
        final List<Service> services = (List<Service>) os.identity().serviceEndpoints().list();

        /* Network elements */
        final List<Network> networks = (List<Network>) os.networking().network().list();
        final List<Subnet> subnets = (List<Subnet>) os.networking().subnet().list();
        final List<Router> routers = (List<Router>) os.networking().router().list();
        final List<Port> ports = (List<Port>) os.networking().port().list();

        /* Create users objects */
        for (User user : users)
            osn.addOpenStackUser(user,user.getId(), user.getName(), user.getDomainId(), user.getEmail(), user.getDescription());

        /* Create networks objects */
        for (Network net : networks)
            osn.addOpenStackNetwork(net.getId(),net.getName(),net.getStatus(),net.getNetworkType(),net.getNeutronSubnets(),net.getProviderPhyNet(),net.getProviderSegID(),net.getSubnets(),net.getTenantId(),net.isAdminStateUp(),net.isRouterExternal(),net.isShared(),net.getMTU());

        /* Create subnets objects */
        for (Subnet subnet : subnets)
            osn.addOpenStackSubnet(subnet.getId(),subnet.getName(),subnet.getAllocationPools(),subnet.getCidr(),subnet.getDnsNames(),subnet.getGateway(),subnet.getHostRoutes(),subnet.getIpVersion(),subnet.getIpv6AddressMode(),subnet.getIpv6RaMode(),subnet.getNetworkId(),subnet.getTenantId(),subnet.isDHCPEnabled());

        /* Create routers objects */
        for (Router router : routers) {
            osn.addOpenStackRouter(router.getId(), router.getName(), router.getTenantId(), router.getStatus(), router.isAdminStateUp(), router.getDistributed(), router.getRoutes(), router.getExternalGatewayInfo());
        }

        for (Port port : ports) {
           osn.addOpenStackPort(port.getId(), port.getName(), port.getTenantId(), port.getAllowedAddressPairs(),port.getDeviceId(),port.getDeviceOwner(),port.getFixedIps(),port.getHostId(),port.getMacAddress(),port.getNetworkId(),port.getProfile(),port.getSecurityGroups(),port.getState(), port.isAdminStateUp(), port.isPortSecurityEnabled());
        }

        for (Credential credential : credentials) {
            osn.addOpenStackCredential(credential.getId(),credential.getUserId(),credential.getProjectId(),credential.getBlob(),credential.getType(),credential.getLinks());
        }

        for (Domain domain : domains) {
            osn.addOpenStackDomain(domain.getId(),domain.getName(),domain.getDescription(),domain.isEnabled(),domain.getLinks());
        }

        for (Endpoint endpoint : endpoints) {
            osn.addOpenStackEndpoint(endpoint.getId(),endpoint.getName(),endpoint.getDescription(),endpoint.isEnabled(),endpoint.getLinks(),endpoint.getRegion(),endpoint.getRegionId(),endpoint.getIface(),endpoint.getServiceId(),endpoint.getType(),endpoint.getUrl());
        }

        for (Group group : groups) {
            osn.addOpenStackGroup(group.getId(),group.getName(),group.getDescription(),group.getDomainId(),group.getLinks());
        }

        for (Policy policy : policies) {
            osn.addOpenStackPolicy(policy.getId(),policy.getUserId(),policy.getProjectId(),policy.getType(),policy.getBlob(),policy.getLinks());
        }
        for (Project project : projects) {
            osn.addOpenStackProject(project.getId(),project.getName(),project.getParentId(),project.getDomainId(),project.getDomain(),project.getDescription(),project.getParents(),project.getSubtree(),project.isEnabled(),project.getLinks());
        }
        for (Region region : regions) {
            osn.addOpenStackRegion(region.getId(),region.getDescription(),region.getParentRegionId());
        }
        for (Role role : roles) {
            osn.addOpenStackRole(role.getId(),role.getName(),role.getDomainId(),role.getLinks());
        }
        for (Service service : services) {
            osn.addOpenStackService(service.getId(),service.getName(),service.getDescription(),service.getType(),service.getVersion(),service.isEnabled(),service.getEndpoints(),service.getLinks());
        }

        osn.addOpenStackInformation();


        if (routers.isEmpty()) throw new Net2PlanException("The OpenStack topology is empty");


        osn.distributeTopologyOverCircle();

        return osn;
    }


}

