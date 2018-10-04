package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
import org.openstack4j.model.identity.v3.Role;

import java.util.List;
import java.util.Map;

public class OpenStackRole  extends OpenStackNetworkElement
{

    private String roleId;
    private String roleName;
    private String roleDomainId;
    private Map<String,String> roleLinks;

    private Role osRole;

    public static OpenStackRole createFromAddRole (OpenStackNet osn , Role role, OpenStackClient openStackClient)
    {
        final OpenStackRole res = new OpenStackRole(osn,role,openStackClient);
        res.roleId= role.getId();
        res.roleName=role.getName();
        res.roleDomainId=role.getDomainId();
        res.roleLinks=role.getLinks();
        return res;
    }

    private OpenStackRole (OpenStackNet osn,Role role , OpenStackClient openStackClient )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackRoles,openStackClient);
        this.osRole = role;
    }

    @Override
    public String getId () { return this.roleId; }
    public String getRoleName () { return this.roleName; }
    public String getRoleDomainId () { return this.roleDomainId; }
    public Map<String,String> getRoleLinks () { return this.roleLinks; }

    public void setRoleName (JSONObject jsonObject) {

        try{
        this.openStackClient.getClient().identity().roles().update(osRole.toBuilder().name(jsonObject.getString("Name")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }
    public void setRoleDomainId (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().roles().update(osRole.toBuilder().domainId(jsonObject.getString("ID")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }

    @Override
    public String get50CharactersDescription()
    {
        String description = "Role: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getRoleName() +
                this.NEWLINE + "Domain ID " + this.getRoleDomainId() +
                this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getRoleLinks().keySet()) {
            description += key + " " + this.getRoleLinks().get(key) + NEWLINE;
        }
        return description;
    }


}