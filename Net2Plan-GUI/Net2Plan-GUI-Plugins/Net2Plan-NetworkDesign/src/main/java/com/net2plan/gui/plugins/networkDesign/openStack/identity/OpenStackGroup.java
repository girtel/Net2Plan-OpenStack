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

    public static OpenStackGroup createFromAddGroup (OpenStackNet osn , String groupId, String groupName, String groupDescription, String groupDomain, Map<String,String> groupLinks)
    {
        final OpenStackGroup res = new OpenStackGroup(osn);
        res.groupId= groupId;
        res.groupName=groupName;
        res.groupDescription=groupDescription;
        res.groupDomainId=groupDomain;
        res.groupLinks=groupLinks;
        return res;
    }

    private OpenStackGroup (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osGroups);
        if(this.groupId != null)
            this.osGroup = this.osn.getOs().identity().groups().get(this.groupId);
    }

    @Override
    public String getId () { return this.groupId; }
    public String getGroupName () { return this.groupName; }
    public String getGroupDescription () { return this.groupDescription; }
    public String getGroupDomainId () { return this.groupDomainId; }
    public Map<String,String> getGroupLinks () { return this.groupLinks; }

    public void setGroupName (String value) { this.osn.getOs().identity().groups().update(osGroup.toBuilder().name(value).build());  }
    public void setGroupDescription (String value) { this.osn.getOs().identity().groups().update(osGroup.toBuilder().description(value).build());  }
    public void setGroupDomainId (String value) { this.osn.getOs().identity().groups().update(osGroup.toBuilder().domainId(value).build());  }

    @Override
    public String get50CharactersDescription()
    {
        return "Endpoint" + this.getId();
    }


}

