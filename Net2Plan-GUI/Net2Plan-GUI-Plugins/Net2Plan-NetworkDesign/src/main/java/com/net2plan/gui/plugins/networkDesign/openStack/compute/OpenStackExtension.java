package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.common.Extension;
import org.openstack4j.model.common.Link;

import java.net.URI;
import java.util.Date;
import java.util.List;

public class OpenStackExtension  extends OpenStackNetworkElement
{

    private String extensionAlias;
    private String extensionName;
    private String extensionDescription;
    private URI extensionNamespace;
    private Date extensionUpdated;
    private List<? extends Link> extensionLinks;

    private Extension osExtension;

    public static OpenStackExtension createFromAddExtension (OpenStackNet osn , String extensionAlias, String extensionName, String extensionDescription, URI extensionNamespace, Date extensionUpdated, List<? extends Link> extensionLinks)
    {
        final OpenStackExtension res = new OpenStackExtension(osn);
        res.extensionAlias= extensionAlias;
        res.extensionName=extensionName;
        res.extensionDescription=extensionDescription;
        res.extensionNamespace=extensionNamespace;
        res.extensionUpdated=extensionUpdated;
        res.extensionLinks=extensionLinks;
        return res;
    }

    private OpenStackExtension (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osExtensions);
    }

    @Override
    public String getId () { return this.extensionAlias; }
    public String getExtensionName () { return this.extensionName; }
    public String getExtensionDescription () { return this.extensionDescription; }
    public URI getExtensionNamespace () { return this.extensionNamespace; }
    public Date getExtensionUpdated () { return this.extensionUpdated; }
    public List<? extends Link> getExtensionLinks () { return this.extensionLinks; }


    @Override
    public String get50CharactersDescription()
    {
        return "Credentials" + this.getId();
    }


}
