package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
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

    public static OpenStackRole createFromAddRole (OpenStackNet osn , String roleId, String roleName, String roleDomainId, Map<String,String> roleLinks)
    {
        final OpenStackRole res = new OpenStackRole(osn);
        res.roleId= roleId;
        res.roleName=roleName;
        res.roleDomainId=roleDomainId;
        res.roleLinks=roleLinks;
        return res;
    }

    private OpenStackRole (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osRoles);
        if(this.roleId != null)
            this.osRole = this.osn.getOs().identity().roles().get(this.roleId);
    }

    @Override
    public String getId () { return this.roleId; }
    public String getRoleName () { return this.roleName; }
    public String getRoleDomainId () { return this.roleDomainId; }
    public Map<String,String> getRoleLinks () { return this.roleLinks; }

    public void setRoleName (String value) { this.osn.getOs().identity().roles().update(osRole.toBuilder().name(value).build());  }
    public void setRoleDomainId (String value) { this.osn.getOs().identity().roles().update(osRole.toBuilder().domainId(value).build());  }

    @Override
    public String get50CharactersDescription()
    {
        return "Endpoint" + this.getId();
    }


}

