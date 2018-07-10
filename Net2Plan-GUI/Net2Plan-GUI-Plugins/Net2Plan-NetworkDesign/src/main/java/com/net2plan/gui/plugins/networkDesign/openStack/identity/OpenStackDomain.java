package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
import org.openstack4j.model.identity.v3.Domain;

import java.util.List;
import java.util.Map;

public class OpenStackDomain extends OpenStackNetworkElement
{

    private String domainId ;
    private String domainName ;
    private String domainDescription ;
    private boolean domainEnabled;
    Map<String,String> domainLinks;
    private Domain osDomain;

    public static OpenStackDomain createFromAddDomain (OpenStackNet osn, Domain domain )
    {
        final OpenStackDomain res = new OpenStackDomain(osn,domain);
        res.domainId= domain.getId();
        res.domainName=domain.getName();
        res.domainDescription=domain.getDescription();
        res.domainEnabled=domain.isEnabled();
        res.domainLinks=domain.getLinks();
        return res;
    }

    private OpenStackDomain (OpenStackNet osn, Domain domain  )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackDomains);
        this.osDomain = domain;
    }

    @Override
    public String getId () { return this.domainId; }
    public String getDomainName () { return this.domainName; }
    public String getDomainDescription () { return this.domainDescription; }
    public boolean isDomainEnabled () { return this.domainEnabled; }
    public Map<String,String> getDomainLinks () { return this.domainLinks; }

    public void setDomainName (JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().identity().domains().update(osDomain.toBuilder().name(jsonObject.getString("Name")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setDomainDescription(JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().identity().domains().update(osDomain.toBuilder().description(jsonObject.getString("Description")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void isDomainEnabled (boolean value) {
        this.osn.getOSClientV3().identity().domains().update(osDomain.toBuilder().enabled(value).build()); }

    @Override
    public String get50CharactersDescription()
    {
         String description = "Domain: " +
            this.NEWLINE + "ID " + this.getId() +
            this.NEWLINE + "Name " + this.getDomainName() +
            this.NEWLINE + "Description " + this.getDomainDescription() +
            this.NEWLINE + "Enabled " + this.isDomainEnabled() +
            this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getDomainLinks().keySet()) {
            description += key + " " + this.getDomainLinks().get(key) + NEWLINE;
        }
        return description;
    }


}
