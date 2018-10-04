package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackRole;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

public class AdvancedJTable_roles extends AdvancedJTable_networkElement<OpenStackRole>
{
    public AdvancedJTable_roles(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.ROLES , true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackRole>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackRole>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackRole>(this, String.class, null, "ID", "Role ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRole>(this, String.class, null, "Name", "Role name", null, n -> n.getRoleName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRole>(this, String.class, null, "Domain ID", "Role domain id", null, n -> n.getRoleDomainId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRole>(this, List.class, null, "Links", "Role links",
                null, n -> n.getRoleLinks(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Add role", e -> addRole(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove role", e -> getSelectedElements().forEach(n -> {

            removeRole(n);

        }), (a, b) -> b == 1, null));
        res.add(new AjtRcMenu("Change role's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n,"");

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change role's domain ID", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Domain ID",n,"Select");

        }), (a, b) -> b ==1, null));
        return res;

    }

    public void addRole(){
        Map<String,String> newList = new HashMap<>();
        newList.put("Name","");
        newList.put("Domain ID","Select");

        generalTableForm("Add role",newList);

    }
    public void removeRole(OpenStackRole role){

        openStackClient.getOpenStackNetDelete().deleteOpenStackRole(role.getId());
        updateTab();
    }
}

