package com.net2plan.gui.plugins.networkDesign.openStack.information;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.api.OSClient;

import java.util.List;

public class OpenStackSummary  extends OpenStackNetworkElement {

    private Integer numberOfProjects;
    private Integer numberOfUsers;
    private Integer numberOfNetworks;
    private Integer numberOfRouters;
    private String projectId;

    private OSClient.OSClientV3 osClientV3 ;


    public static OpenStackSummary createFromAddSummary (OpenStackNet osn , OSClient.OSClientV3 osClientV3)
    {
        final OpenStackSummary res = new OpenStackSummary(osn,osClientV3);
        res.numberOfProjects= 0;
        res.numberOfUsers= osClientV3.identity().users().list().size();
        res.numberOfNetworks=osClientV3.networking().network().list().size();
        res.numberOfRouters=osClientV3.networking().router().list().size();
        res.projectId = osClientV3.getToken().getProject().getId();
        return res;
    }

    private OpenStackSummary (OpenStackNet osn ,  OSClient.OSClientV3 osClientV3)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackSummaries);
        this.osClientV3 = osClientV3;

    }

    @Override
    public String getId () { return this.projectId; }
    public Integer getNumberOfProjects () { return this.numberOfProjects; }
    public Integer getNumberOfUsers () { return this.numberOfUsers; }
    public Integer getNumberOfNetworks () { return this.numberOfNetworks; }
    public Integer getNumberOfRouters () { return this.numberOfRouters; }


    @Override
    public String get50CharactersDescription()
    {
        return "Summary of project : " + this.getId();
    }

}


