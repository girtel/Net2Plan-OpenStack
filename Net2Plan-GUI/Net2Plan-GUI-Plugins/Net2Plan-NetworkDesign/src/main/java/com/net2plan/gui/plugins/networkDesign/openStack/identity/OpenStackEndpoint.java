package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.json.JSONObject;
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

    public static OpenStackEndpoint createFromAddEndpoint (OpenStackNet osn , Endpoint endpoint, OpenStackClient openStackClient)
    {
        final OpenStackEndpoint res = new OpenStackEndpoint(osn,endpoint,openStackClient);
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

    private OpenStackEndpoint (OpenStackNet osn, Endpoint endpoint, OpenStackClient openStackClient )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackEndpoints,openStackClient);
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
           this.openStackClient.getClient().identity().serviceEndpoints().updateEndpoint(this.osEndpoint);



    }
    public void setEndpointDescription (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().description(jsonObject.getString("Description")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
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
        this.openStackClient.getClient().identity().serviceEndpoints().updateEndpoint(this.osEndpoint);


    }
    public void setEndpointRegion (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().region(jsonObject.getString("Region")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setEndpointRegionId (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().regionId(jsonObject.getString("Region ID")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setEndpointIface (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().iface(Facing.valueOf(jsonObject.getString("Facing"))).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setEndpointServiceId (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().serviceId(jsonObject.getString("Service ID")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setEndpointType (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().type(jsonObject.getString("Type")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setEndpointUrl (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().updateEndpoint(osEndpoint.toBuilder().url((URL)jsonObject.get("URL")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }

    @Override
    public String get50CharactersDescription()
    {
        String description = "Endpoint: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Endpoint name " + this.getEndpointName() +
                this.NEWLINE + "Project/Tenant ID " + this.getEndpointDescription() +
                this.NEWLINE + "Enable " + this.isEndpointEnabled() +
                this.NEWLINE + "Region ID " + this.getEndpointRegionId() +
                this.NEWLINE + "IFace " + this.getEndpointIface() +
                this.NEWLINE + "Service ID " + this.getEndpointServiceId() +
                this.NEWLINE + "Type " + this.getEndpointType() +
                this.NEWLINE + "URL " + this.getEndpointUrl() +
                this.NEWLINE + "Links " + this.NEWLINE;
        for(String key : this.getEndpointLinks().keySet()) {
            description += key + " " + this.getEndpointLinks().get(key) + NEWLINE;
        }
        return description;
    }


}
