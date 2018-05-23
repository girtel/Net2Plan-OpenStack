package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.SecGroupExtension;

import java.util.List;

public class OpenStackSecurityGroup  extends OpenStackNetworkElement
{

    private String secGroupExtensionId;
    private String secGroupExtensionName;
    private String secGroupExtensionDescription;
    private String secGroupExtensionTenantId;
    private List<? extends SecGroupExtension.Rule> secGroupExtensionRules;
    private List<? extends Link>secGroupExtensionLinks;

    public static OpenStackSecurityGroup createFromAddSegurityGroup (OpenStackNet osn , String secGroupExtensionId, String secGroupExtensionName, String secGroupExtensionDescription, String secGroupExtensionTenantId, List<? extends SecGroupExtension.Rule> secGroupExtensionRules, List<? extends Link>secGroupExtensionLinks)
    {
        final OpenStackSecurityGroup res = new OpenStackSecurityGroup(osn);
        res.secGroupExtensionId= secGroupExtensionId;
        res.secGroupExtensionName=secGroupExtensionName;
        res.secGroupExtensionTenantId=secGroupExtensionTenantId;
        res.secGroupExtensionDescription=secGroupExtensionDescription;
        res.secGroupExtensionRules=secGroupExtensionRules;
        res.secGroupExtensionLinks=secGroupExtensionLinks;
        return res;
    }

    private OpenStackSecurityGroup (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osSecurityGroups);
    }

    @Override
    public String getId () { return this.secGroupExtensionId; }
    public String getSecGroupExtensionName () { return this.secGroupExtensionName; }
    public String getSecGroupExtensionDescription () { return this.secGroupExtensionDescription; }
    public String getSecGroupExtensionTenantId () { return this.secGroupExtensionTenantId; }
    public List<? extends SecGroupExtension.Rule> getSecGroupExtensionRules () { return this.secGroupExtensionRules; }
    public List<? extends Link> getSecGroupExtensionLinks () { return this.secGroupExtensionLinks; }


    @Override
    public String get50CharactersDescription()
    {
        return "Credentials" + this.getId();
    }


}
