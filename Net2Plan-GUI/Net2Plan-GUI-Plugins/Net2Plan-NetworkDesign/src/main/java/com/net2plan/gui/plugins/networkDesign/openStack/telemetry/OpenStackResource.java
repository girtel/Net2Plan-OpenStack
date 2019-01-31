package com.net2plan.gui.plugins.networkDesign.openStack.telemetry;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
import org.openstack4j.model.telemetry.Meter;
import org.openstack4j.model.telemetry.Resource;

import java.util.*;

public class OpenStackResource  extends OpenStackNetworkElement
{

    private String resource_id;
    private String resource_source ;
    private String resource_id_real ;
    private String resource_user_id ;
    private String resource_project_id ;
    private String type ;
    private JSONObject resourceJSON;
    private Resource resource ;


    public static OpenStackResource createFromAddResource (OpenStackNet osn , Resource resource, OpenStackClient openStackClient)
    {
        final OpenStackResource res = new OpenStackResource(osn,resource,openStackClient);
        res.resource_id= resource.getId()+"resource";
        res.resource_id_real= resource.getId();
        res.resource_source=resource.getSource();
        res.resource_user_id = resource.getUserId();
        res.resource_project_id=resource.getProjectId();
        res.resourceJSON= (JSONObject)resource.getMeataData().get("meta");
        res.type = res.resourceJSON.getString("type");

        return res;
    }

    private OpenStackResource (OpenStackNet osn ,Resource resource,OpenStackClient openStackClient)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackResources,openStackClient);
        this.resource = resource;


    }

    @Override
    public String getId () { return this.resource_id; }
    public String getName(){return this.resource_id;}

    public String getSource () { return this.resource_source; }
    public String getSourceId () { return this.resource_id_real; }
    public String getResource_user_id() {return this.resource_user_id;}
    public String getResource_project_id () { return this.resource_project_id; }
    public String getType() {return this.type;}
    public Map<String,String> getMetrics (){
       JSONObject metrics = (JSONObject) this.resourceJSON.get("metrics");
        Iterator<String> keyItr = metrics.keys();
        Map<String, String> map = new HashMap<>();

        while(keyItr.hasNext()) {
            String key = keyItr.next();
            map.put(key, metrics.getString(key));
        }

        System.out.println(metrics);
       return map;
    }
    @Override
    public String get50CharactersDescription()
    {
    /*    String description = "User: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getName() +
                this.NEWLINE + "Domain ID " + this.getDomainId() +
                this.NEWLINE + "Email " + this.getEmail() +
                this.NEWLINE + "Description " + this.getDescription() +
                this.NEWLINE + "Enable " + this.isUserEnable() +
                this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getUserLinks().keySet()) {
            description += key + " " + this.getUserLinks().get(key) + NEWLINE;
        }
        return description;*/
        return "Meter";
    }


}


