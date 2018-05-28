package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.identity.v3.Endpoint;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class OpenStackEndpoint  extends OpenStackNetworkElement
{

    private String endpointId ;
    private String endpointName ;
    private String endpointDescription ;
    private boolean endpointEnabled;
    private Map<String,String> endpointLinks;
    private String endpointRegion;
    private String endpointRegionId;
    private Facing endpointIface;
    private String endpointServiceId;
    private String endpointType;
    private URL endpointUrl;

    private Endpoint osEndpoint;

    public static OpenStackEndpoint createFromAddEndpoint (OpenStackNet osn , String endpointId, String endpointName, String endpointDescription, boolean endpointEnabled, Map<String,String> endpointLinks, String endpointRegion, String endpointRegionId, Facing endpointIface, String endpointServiceId, String endpointType, URL endpointUrl)
    {
        final OpenStackEndpoint res = new OpenStackEndpoint(osn);
        res.endpointId= endpointId;
        res.endpointName=endpointName;
        res.endpointDescription=endpointDescription;
        res.endpointEnabled=endpointEnabled;
        res.endpointLinks=endpointLinks;
        res.endpointRegion=endpointRegion;
        res.endpointRegionId= endpointRegionId;
        res.endpointIface=endpointIface;
        res.endpointServiceId=endpointServiceId;
        res.endpointType=endpointType;
        res.endpointUrl=endpointUrl;
        return res;
    }

    private OpenStackEndpoint (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osEndpoints);
        if(this.endpointId != null)
            this.osEndpoint = this.osn.getOs().identity().serviceEndpoints().getEndpoint(this.endpointId);
    }

    @Override
    public String getId () { return this.endpointId; }
    public String getEndpointName () { return this.endpointName; }
    public String getEndpointDescription () { return this.endpointDescription; }
    public boolean isEndpointEnabled () { return this.endpointEnabled; }
    public Map<String,String> getEndpointLinks () { return this.endpointLinks; }
    public String getEndpointRegion () { return this.endpointRegion; }
    public String getEndpointRegionId () { return this.endpointRegionId; }
    public Facing getEndpointIface () { return this.endpointIface; }
    public String getEndpointServiceId () { return this.endpointServiceId; }
    public String getEndpointType () { return this.endpointType; }
    public URL getEndpointUrl () { return this.endpointUrl; }

    public void setEndpointName (String value) { this.osn.getOs().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().name(value).build());  }
    public void setEndpointDescription (String value) { this.osn.getOs().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().description(value).build());  }
    public void isEnabled (boolean value) { this.osn.getOs().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().enabled(value).build());  }
    public void setEndpointRegion (String value) { this.osn.getOs().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().region(value).build()); }
    public void setEndpointRegionId (String value) { this.osn.getOs().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().regionId(value).build());  }
    public void setEndpointIface (Facing value) { this.osn.getOs().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().iface(value).build());  }
    public void setEndpointServiceId (String value) { this.osn.getOs().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().serviceId(value).build());  }
    public void setEndpointType (String value) { this.osn.getOs().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().type(value).build());  }
    public void setEndpointUrl (URL value) { this.osn.getOs().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().url(value).build()); }

    @Override
    public String get50CharactersDescription()
    {
        return "Endpoint" + this.getId();
    }


}
