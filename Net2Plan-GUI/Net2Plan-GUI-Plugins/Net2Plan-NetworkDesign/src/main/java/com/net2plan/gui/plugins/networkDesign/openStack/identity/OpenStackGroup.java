package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.identity.v3.Group;

import java.util.List;
import java.util.Map;

public class OpenStackGroup  extends OpenStackNetworkElement
{

    private String groupId ;
    private String groupName ;
    private String groupDescription ;
    private String groupDomainId;
    private Map<String,String> groupLinks;

    private Group osGroup;

    public static OpenStackGroup createFromAddGroup (OpenStackNet osn , Group group)
    {
        final OpenStackGroup res = new OpenStackGroup(osn,group);
        res.groupId= group.getId();
        res.groupName=group.getName();
        res.groupDescription=group.getDescription();
        res.groupDomainId=group.getDomainId();
        res.groupLinks=group.getLinks();

        return res;
    }

    private OpenStackGroup (OpenStackNet osn,  Group group )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackGroups);
        this.osGroup = group;
    }

    @Override
    public String getId () { return this.groupId; }
    public String getGroupName () { return this.groupName; }
    public String getGroupDescription () { return this.groupDescription; }
    public String getGroupDomainId () { return this.groupDomainId; }
    public Map<String,String> getGroupLinks () { return this.groupLinks; }

    public void setGroupName (String value) { this.osn.getOSClientV3().identity().groups().update(osGroup.toBuilder().name(value).build());  }
    public void setGroupDescription (String value) { this.osn.getOSClientV3().identity().groups().update(osGroup.toBuilder().description(value).build());  }
    public void setGroupDomainId (String value) { this.osn.getOSClientV3().identity().groups().update(osGroup.toBuilder().domainId(value).build());  }

    @Override
    public String get50CharactersDescription()
    {
        String description = "Credential: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "User ID " + this.getCredentialUserId() +
                this.NEWLINE + "Project/Tenant ID " + this.getCredentialProjectId() +
                this.NEWLINE + "Blob " + this.getCredentialBlob() +
                this.NEWLINE + "Type " + this.getCredentialType() +
                this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getCredentialLinks().keySet()) {
            description += key + " " + this.getCredentialLinks().get(key) + NEWLINE;
        }
        return description;
    }


}

