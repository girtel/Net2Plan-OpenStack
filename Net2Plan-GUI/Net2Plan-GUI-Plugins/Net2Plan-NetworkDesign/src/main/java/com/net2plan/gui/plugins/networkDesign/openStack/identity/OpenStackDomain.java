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

    public void setDomainName (String value) { this.osn.getOSClientV3().identity().domains().update(osDomain.toBuilder().name(value).build());  }
    public void isDomainEnabled (boolean value) { this.osn.getOSClientV3().identity().domains().update(osDomain.toBuilder().enabled(value).build()); }

    @Override
    public String get50CharactersDescription()
    {
        return "Domain: " + this.getId();
    }


}
