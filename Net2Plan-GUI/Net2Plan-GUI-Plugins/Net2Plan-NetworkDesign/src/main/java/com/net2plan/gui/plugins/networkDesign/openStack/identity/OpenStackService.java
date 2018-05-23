package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.model.identity.v3.Service;

import java.util.List;
import java.util.Map;

public class OpenStackService  extends OpenStackNetworkElement
{

    private String serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceType;
    private Integer serviceVersion;
    private boolean serviceEnabled;
    private List<? extends Endpoint> serviceEndpoints;
    private Map<String,String> serviceLinks;

    Service osService;
    public static OpenStackService createFromAddService (OpenStackNet osn , String serviceId, String serviceName, String serviceDescription, String serviceType, Integer serviceVersion, boolean serviceEnabled, List<? extends Endpoint> serviceEndpoints, Map<String,String> serviceLinks)
    {
        final OpenStackService res = new OpenStackService(osn);
        res.serviceId= serviceId;
        res.serviceName=serviceName;
        res.serviceDescription=serviceDescription;
        res.serviceType=serviceType;
        res.serviceVersion=serviceVersion;
        res.serviceEnabled=serviceEnabled;
        res.serviceEndpoints=serviceEndpoints;
        res.serviceLinks=serviceLinks;
        return res;
    }

    private OpenStackService (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osServices);
        if(this.serviceId != null)
            this.osService = this.osn.getOs().identity().serviceEndpoints().get(this.serviceId);
    }

    @Override
    public String getId () { return this.serviceId; }
    public String getServiceName () { return this.serviceName; }
    public String getServiceDescription () { return this.serviceDescription; }
    public String getServiceType () { return this.serviceType; }
    public Integer getServiceVersion () { return this.serviceVersion; }
    public boolean isServiceEnabled () { return this.serviceEnabled; }
    public List<? extends Endpoint> getServiceEndpoints () { return this.serviceEndpoints; }
    public Map<String,String> getServiceLinks () { return this.serviceLinks; }

    public void setServiceName (String value) { this.osn.getOs().identity().serviceEndpoints().update(osService.toBuilder().name(value).build());  }
    public void setServiceDescription (String value) { this.osn.getOs().identity().serviceEndpoints().update(osService.toBuilder().description(value).build());  }
    public void setServiceType (String value) { this.osn.getOs().identity().serviceEndpoints().update(osService.toBuilder().type(value).build());  }
    public void setServiceVersion (Integer value) { this.osn.getOs().identity().serviceEndpoints().update(osService.toBuilder().version(value).build());  }
    public void setServiceEnabled (boolean value) { this.osn.getOs().identity().serviceEndpoints().update(osService.toBuilder().enabled(value).build());  }

    @Override
    public String get50CharactersDescription()
    {
        return "Endpoint" + this.getId();
    }


}

