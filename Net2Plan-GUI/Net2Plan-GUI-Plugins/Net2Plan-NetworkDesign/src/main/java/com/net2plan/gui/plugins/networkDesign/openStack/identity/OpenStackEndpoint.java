package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.omg.IOP.ENCODING_CDR_ENCAPS;
import org.openstack4j.api.Builders;
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

    public static OpenStackEndpoint createFromAddEndpoint (OpenStackNet osn , Endpoint endpoint)
    {
        final OpenStackEndpoint res = new OpenStackEndpoint(osn,endpoint);
        res.endpointId= endpoint.getId();
        res.endpointName=endpoint.getName();
        res.endpointDescription=endpoint.getDescription();
        res.endpointEnabled=endpoint.isEnabled();
        res.endpointLinks=endpoint.getLinks();
        res.endpointRegion=endpoint.getRegion();
        res.endpointRegionId= endpoint.getRegionId();
        res.endpointIface=endpoint.getIface();
        res.endpointServiceId=endpoint.getServiceId();
        res.endpointType=endpoint.getType();
        res.endpointUrl=endpoint.getUrl();
        return res;
    }

    private OpenStackEndpoint (OpenStackNet osn, Endpoint endpoint )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackEndpoints);
        this.osEndpoint = endpoint;
    }

    @Override
    public String getId () { return this.endpointId; }
    public String getEndpointName () { return this.endpointName; }
    public String getEndpointDescription () { return this.endpointDescription; }
    public Boolean isEndpointEnabled () { return this.endpointEnabled; }
    public Map<String,String> getEndpointLinks () { return this.endpointLinks; }
    public String getEndpointRegion () { return this.endpointRegion; }
    public String getEndpointRegionId () { return this.endpointRegionId; }
    public Facing getEndpointIface () { return this.endpointIface; }
    public String getEndpointServiceId () { return this.endpointServiceId; }
    public String getEndpointType () { return this.endpointType; }
    public URL getEndpointUrl () { return this.endpointUrl; }

    public void setEndpointName (String value) {

        System.out.println("in" + value);
        this.osEndpoint=Builders.endpoint()
                .id(this.endpointId)
                .url(this.endpointUrl)
                .enabled(this.endpointEnabled)
                .serviceId(this.endpointServiceId)
                .name(value)
                .region(this.endpointRegion)
                .regionId(this.endpointRegionId)
                .links(this.endpointLinks)
                .iface(this.endpointIface)
                .description(this.endpointDescription)
                .type(this.endpointType)
                .build();
        System.out.println(this.osEndpoint);
        // Builders.endpoint().
        this.osn.getOSClientV3().identity().serviceEndpoints().updateEndpoint(this.osEndpoint);



    }
    public void setEndpointDescription (String value) { this.osn.getOSClientV3().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().description(value).build());  }
    public void isEndpointEnabled (Boolean value) {


        System.out.println("in" + value);
        this.osEndpoint=Builders.endpoint()
                .id(this.endpointId)
                .url(this.endpointUrl)
                .enabled(value)
                .serviceId(this.endpointServiceId)
                .name(this.endpointName)
                .region(this.endpointRegion)
                .regionId(this.endpointRegionId)
                .links(this.endpointLinks)
                .iface(this.endpointIface)
                .description(this.endpointDescription)
                .type(this.endpointType)
                .build();
        System.out.println(this.osEndpoint);
       // Builders.endpoint().
        this.osn.getOSClientV3().identity().serviceEndpoints().updateEndpoint(this.osEndpoint);


    }
    public void setEndpointRegion (String value) { this.osn.getOSClientV3().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().region(value).build()); }
    public void setEndpointRegionId (String value) { this.osn.getOSClientV3().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().regionId(value).build());  }
    public void setEndpointIface (Facing value) { this.osn.getOSClientV3().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().iface(value).build());  }
    public void setEndpointServiceId (String value) { this.osn.getOSClientV3().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().serviceId(value).build());  }
    public void setEndpointType (String value) { this.osn.getOSClientV3().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().type(value).build());  }
    public void setEndpointUrl (URL value) { this.osn.getOSClientV3().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().url(value).build()); }

    @Override
    public String get50CharactersDescription()
    {
        return "Endpoint: " + this.getId();
    }


}
