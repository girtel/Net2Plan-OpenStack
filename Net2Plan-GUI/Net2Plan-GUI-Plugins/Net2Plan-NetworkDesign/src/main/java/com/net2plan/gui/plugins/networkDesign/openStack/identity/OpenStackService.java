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
    private Service osService;

    public static OpenStackService createFromAddService (OpenStackNet osn ,Service service)
    {
        final OpenStackService res = new OpenStackService(osn,service);
        res.serviceId= service.getId();
        res.serviceName=service.getName();
        res.serviceDescription=service.getDescription();
        res.serviceType=service.getType();
        res.serviceVersion=service.getVersion();
        res.serviceEnabled=service.isEnabled();
        res.serviceEndpoints=service.getEndpoints();
        res.serviceLinks=service.getLinks();
        return res;
    }

    private OpenStackService (OpenStackNet osn ,Service service)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackServices);
        this.osService = service;
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

    public void setServiceName (String value) { this.osn.getOSClientV3().identity().serviceEndpoints().update(osService.toBuilder().name(value).build());  }
    public void setServiceDescription (String value) { this.osn.getOSClientV3().identity().serviceEndpoints().update(osService.toBuilder().description(value).build());  }
    public void setServiceType (String value) { this.osn.getOSClientV3().identity().serviceEndpoints().update(osService.toBuilder().type(value).build());  }
    public void setServiceVersion (Integer value) { this.osn.getOSClientV3().identity().serviceEndpoints().update(osService.toBuilder().version(value).build());  }
    public void isServiceEnabled (boolean value) { this.osn.getOSClientV3().identity().serviceEndpoints().update(osService.toBuilder().enabled(value).build());  }

    @Override
    public String get50CharactersDescription()
    {
        String description = "Service: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Service name " + this.getServiceName() +
                this.NEWLINE + "Project/Tenant ID " + this.getServiceDescription() +
                this.NEWLINE + "Blob " + this.getServiceType() +
                this.NEWLINE + "Type " + this.getServiceVersion() +
                this.NEWLINE + "Type " + this.isServiceEnabled() +
                this.NEWLINE + "Endpoints" + this.NEWLINE;
        for(Endpoint endpoint : this.getServiceEndpoints()) {
            description += endpoint + " " + NEWLINE;

        }

        description +=this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getServiceLinks().keySet()) {
            description += key + " " + this.getServiceLinks().get(key) + NEWLINE;
        }
        return description;
    }


}

