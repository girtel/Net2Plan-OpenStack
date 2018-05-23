package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.identity.v3.Domain;

import java.util.List;
import java.util.Map;

public class OpenStackDomain extends OpenStackNetworkElement
{

    private String domainId ;
    private String domainName ;
    private String domainDescription ;
    private String credentialBlob ;
    private boolean domainEnabled;
    Map<String,String>domainLinks;
    private Domain osDomain;

    public static OpenStackDomain createFromAddDomain (OpenStackNet osn,String domainId, String domainName,String domainDescription, boolean domainEnabled, Map<String,String>domainLinks )
    {
        final OpenStackDomain res = new OpenStackDomain(osn);
        res.domainId= domainId;
        res.domainName=domainName;
        res.domainDescription=domainDescription;
        res.domainEnabled=domainEnabled;
        res.domainLinks=domainLinks;
        return res;
    }

    private OpenStackDomain (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osDomains);
        if(this.domainId != null)
            this.osDomain = this.osn.getOs().identity().domains().get(this.domainId);
    }

    @Override
    public String getId () { return this.domainId; }
    public String getDomainName () { return this.domainName; }
    public String getDomainDescription () { return this.domainDescription; }
    public boolean isDomainEnabled () { return this.domainEnabled; }
    public Map<String,String> getDomainLinks () { return this.domainLinks; }

    public void setDomainName (String value) { this.osn.getOs().identity().domains().update(osDomain.toBuilder().name(value).build());  }
    public void setDomainDescription (String value) { this.osn.getOs().identity().domains().update(osDomain.toBuilder().description(value).build()); }
    public void setDomainEnabled (boolean value) { this.osn.getOs().identity().domains().update(osDomain.toBuilder().enabled(value).build()); }

    @Override
    public String get50CharactersDescription()
    {
        return "Credentials" + this.getId();
    }


}
