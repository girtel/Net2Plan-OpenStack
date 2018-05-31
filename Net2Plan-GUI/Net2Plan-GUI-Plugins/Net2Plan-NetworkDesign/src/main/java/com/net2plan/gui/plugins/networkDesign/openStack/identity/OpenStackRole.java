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

    public static OpenStackRole createFromAddRole (OpenStackNet osn , Role role)
    {
        final OpenStackRole res = new OpenStackRole(osn,role);
        res.roleId= role.getId();
        res.roleName=role.getName();
        res.roleDomainId=role.getDomainId();
        res.roleLinks=role.getLinks();
        return res;
    }

    private OpenStackRole (OpenStackNet osn,Role role )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackRoles);
        this.osRole = role;
    }

    @Override
    public String getId () { return this.roleId; }
    public String getRoleName () { return this.roleName; }
    public String getRoleDomainId () { return this.roleDomainId; }
    public Map<String,String> getRoleLinks () { return this.roleLinks; }

    public void setRoleName (String value) { this.osn.getOSClientV3().identity().roles().update(osRole.toBuilder().name(value).build());  }
    public void setRoleDomainId (String value) { this.osn.getOSClientV3().identity().roles().update(osRole.toBuilder().domainId(value).build());  }

    @Override
    public String get50CharactersDescription()
    {
        return "Role: " + this.getId();
    }


}