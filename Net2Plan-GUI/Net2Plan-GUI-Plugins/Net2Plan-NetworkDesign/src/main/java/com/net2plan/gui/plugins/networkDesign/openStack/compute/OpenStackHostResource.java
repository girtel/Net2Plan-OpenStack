package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.HostResource;

import java.util.List;

public class OpenStackHostResource extends OpenStackNetworkElement
{

    private String hostResourceId;
    private String hostResourceName;
    private Integer hostResourceCpu;
    private Integer hostResourceDiskInGb;
    private Integer hostResourceMemoryInMb;
    private String hostResourceProject;
    private String hostResourceService;
    private String hostResourceZone;

    private HostResource hostResource;
    private OpenStackClient openStackClient;
    public static OpenStackHostResource createFromAddHostResource (OpenStackNet osn , HostResource hostResource,OpenStackClient openStackClient)
    {
        final OpenStackHostResource res = new OpenStackHostResource(osn,hostResource,openStackClient);
        res.hostResourceId= hostResource.getHost();
        res.hostResourceName=hostResource.getHostName();
        res.hostResourceCpu=hostResource.getCpu();
        res.hostResourceDiskInGb=hostResource.getDiskInGb();
        res.hostResourceMemoryInMb=hostResource.getMemoryInMb();
        res.hostResourceProject=hostResource.getProject();
        res.hostResourceService=hostResource.getService();
        res.hostResourceZone=hostResource.getZone();
        return res;
    }

    private OpenStackHostResource (OpenStackNet osn ,HostResource hostResource,OpenStackClient openStackClient)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackHostResources,openStackClient);
        this.hostResource = hostResource;
    }

    @Override
    public String getId () { return this.hostResourceId; }
    public String getName () { return this.hostResourceId; }
    public Integer getHostCpu () { return this.hostResourceCpu; }
    public Integer getHostDisk () { return this.hostResourceDiskInGb; }
    public Integer getHostMemory () { return this.hostResourceMemoryInMb; }
    public String getHostProject () { return this.hostResourceProject; }
    public String getHostService () { return this.hostResourceService; }
    public String getHostZone () { return this.hostResourceZone; }

    @Override
    public String get50CharactersDescription()
    {
        String description = "Host: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getName() +
                this.NEWLINE + "Cpu " + this.getHostCpu() +
                this.NEWLINE + "Disk " + this.getHostDisk() +
                this.NEWLINE + "RAM " + this.getHostMemory() +
                this.NEWLINE + "Project " + this.getHostProject() +
                this.NEWLINE + "Service " + this.getHostService() +
                this.NEWLINE + "Zone " + this.getHostZone();
        return description;
    }


}
