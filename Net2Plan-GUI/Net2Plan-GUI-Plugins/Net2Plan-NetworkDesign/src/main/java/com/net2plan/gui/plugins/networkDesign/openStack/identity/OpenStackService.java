package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
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

    public static OpenStackService createFromAddService (OpenStackNet osn ,Service service, OpenStackClient openStackClient)
    {
        final OpenStackService res = new OpenStackService(osn,service,openStackClient);
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

    private OpenStackService (OpenStackNet osn ,Service service, OpenStackClient openStackClient)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackServices,openStackClient);
        this.osService = service;
    }

    @Override
    public String getId () { return this.serviceId; }
    public String getServiceName () { return this.serviceName; }
    public String getName () { return this.serviceName; }
    public String getServiceDescription () { return this.serviceDescription; }
    public String getServiceType () { return this.serviceType; }
    public Integer getServiceVersion () { return this.serviceVersion; }
    public boolean isServiceEnabled () { return this.serviceEnabled; }
    public List<? extends Endpoint> getServiceEndpoints () { return this.serviceEndpoints; }
    public Map<String,String> getServiceLinks () { return this.serviceLinks; }

    public void setServiceName (JSONObject jsonObject) {
        try{
            this.openStackClient.getClient().identity().serviceEndpoints().update(osService.toBuilder().name(jsonObject.getString("Name")).build());
        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    } }
    public void setServiceDescription (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().update(osService.toBuilder().description(jsonObject.getString("Description")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }
    public void setServiceType (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().update(osService.toBuilder().type(jsonObject.getString("Type")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

        }
    public void setServiceVersion (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().update(osService.toBuilder().version(jsonObject.getInt("Version")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void isServiceEnabled (boolean value) {
        try{
        this.openStackClient.getClient().identity().serviceEndpoints().update(osService.toBuilder().enabled(value).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }

    @Override
    public String get50CharactersDescription()
    {
        String description = "Service: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Service name " + this.getServiceName() +
                this.NEWLINE + "Project/Tenant ID " + this.getServiceDescription() +
                this.NEWLINE + "Blob " + this.getServiceType() +
                this.NEWLINE + "Type " + this.getServiceVersion() +
                this.NEWLINE + "Enable " + this.isServiceEnabled() +
                this.NEWLINE + "Endpoints" + this.NEWLINE;
        if(this.getServiceEndpoints()!=null){
        for (Endpoint endpoint : this.getServiceEndpoints()) {
            description += endpoint + " " + NEWLINE;

        }
    }
        description +=this.NEWLINE + "Links" + this.NEWLINE;
        if(this.getServiceLinks()!=null) {
            for (String key : this.getServiceLinks().keySet()) {
                description += key + " " + this.getServiceLinks().get(key) + NEWLINE;
            }
        }
        return description;
    }


}

