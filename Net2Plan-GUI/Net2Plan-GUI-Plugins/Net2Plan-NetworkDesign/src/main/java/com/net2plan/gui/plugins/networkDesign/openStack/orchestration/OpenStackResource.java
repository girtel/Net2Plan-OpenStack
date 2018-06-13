package com.net2plan.gui.plugins.networkDesign.openStack.orchestration;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.heat.Resource;

import java.util.List;

public class OpenStackResource extends OpenStackNetworkElement
{

    private String resourceId;
    private String resourceName ;
    private Resource resource ;


    public static OpenStackResource createFromAddResource (OpenStackNet osn , Resource resource)
    {
        final OpenStackResource res = new OpenStackResource(osn,resource);
        res.resourceId= resource.getLocalReourceId();
        res.resourceName=resource.getResourceName();


        return res;
    }

    private OpenStackResource (OpenStackNet osn , Resource resource)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackResources);
        this.resource = resource;


    }

    @Override
    public String getId () { return this.resourceId; }
    public String getResourceName () { return this.resourceName; }



    @Override
    public String get50CharactersDescription()
    {
        return "Resource: " + this.getId();
    }


}

