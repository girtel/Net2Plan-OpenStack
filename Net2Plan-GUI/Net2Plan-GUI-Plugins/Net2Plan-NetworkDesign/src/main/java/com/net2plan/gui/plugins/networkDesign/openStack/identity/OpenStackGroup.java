package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
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

    public void setGroupName (JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().identity().groups().update(osGroup.toBuilder().name(jsonObject.getString("Name")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setGroupDescription (JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().identity().groups().update(osGroup.toBuilder().description(jsonObject.getString("Description")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setGroupDomainId (JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().identity().groups().update(osGroup.toBuilder().domainId(jsonObject.getString("Domain ID")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }

    @Override
    public String get50CharactersDescription()
    {
        String description = "Group: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getGroupName() +
                this.NEWLINE + "Description " + this.getGroupDescription() +
                this.NEWLINE + "Domain ID " + this.getGroupDomainId() +
                this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getGroupLinks().keySet()) {
            description += key + " " + this.getGroupLinks().get(key) + NEWLINE;
        }
        return description;
    }


}

