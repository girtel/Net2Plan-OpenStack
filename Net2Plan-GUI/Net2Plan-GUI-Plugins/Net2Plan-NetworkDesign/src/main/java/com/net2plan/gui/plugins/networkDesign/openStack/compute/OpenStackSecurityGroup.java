package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.model.compute.SecurityGroup;

import java.util.List;

public class OpenStackSecurityGroup  extends OpenStackNetworkElement
{

    private String secGroupExtensionId;
    private String secGroupExtensionName;
    private String secGroupExtensionDescription;
    private String secGroupExtensionTenantId;
    private List<? extends SecGroupExtension.Rule> secGroupExtensionRules;
    private List<? extends Link>secGroupExtensionLinks;
    private SecGroupExtension osSecGroupExtension;
    public static OpenStackSecurityGroup createFromAddSegurityGroup (OpenStackNet osn , SecGroupExtension secGroupExtension)
    {
        final OpenStackSecurityGroup res = new OpenStackSecurityGroup(osn,secGroupExtension);
        res.secGroupExtensionId= secGroupExtension.getId();
        res.secGroupExtensionName=secGroupExtension.getName();
        res.secGroupExtensionTenantId=secGroupExtension.getTenantId();
        res.secGroupExtensionDescription=secGroupExtension.getDescription();
        res.secGroupExtensionRules=secGroupExtension.getRules();
        res.secGroupExtensionLinks=secGroupExtension.getLinks();
        return res;
    }

    private OpenStackSecurityGroup (OpenStackNet osn,SecGroupExtension secGroupExtension )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackSecurityGroups);
        this.osSecGroupExtension = secGroupExtension;
    }

    @Override
    public String getId () { return this.secGroupExtensionId; }
    public String getSecGroupExtensionName () { return this.secGroupExtensionName; }
    public String getSecGroupExtensionDescription () { return this.secGroupExtensionDescription; }
    public String getSecGroupExtensionTenantId () { return this.secGroupExtensionTenantId; }
    public List<? extends SecGroupExtension.Rule> getSecGroupExtensionRules () { return this.secGroupExtensionRules; }
    public List<? extends Link> getSecGroupExtensionLinks () { return this.secGroupExtensionLinks; }
    public void setSecGroupName (JSONObject jsonObject) { this.osn.getOSClientV3().compute().securityGroups().update(this.osSecGroupExtension.getId(),jsonObject.getString("Name"),this.osSecGroupExtension.getDescription());}
    public void setSecGroupDescription (JSONObject jsonObject) { this.osn.getOSClientV3().compute().securityGroups().update(this.osSecGroupExtension.getId(),this.osSecGroupExtension.getName(),jsonObject.getString("Description"));}

    @Override
    public String get50CharactersDescription()
    {
        String description = "Security Group: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "User ID " + this.getSecGroupExtensionName() +
                this.NEWLINE + "Project/Tenant ID " + this.getSecGroupExtensionTenantId() +
                this.NEWLINE + "Blob " + this.getSecGroupExtensionDescription();
                description += this.NEWLINE + "Links" + this.NEWLINE;
        if(this.getSecGroupExtensionLinks()!=null) {
            for (Link link : this.getSecGroupExtensionLinks()) {
                description += link + " " + NEWLINE;
            }
        }
        if(this.getSecGroupExtensionRules()!=null) {
            description += this.NEWLINE + "Rule" + this.NEWLINE;
            for (SecGroupExtension.Rule rule : this.getSecGroupExtensionRules()) {
                description += rule + " " + NEWLINE;
            }
        }
        return description;
    }


}
