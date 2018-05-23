package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackRegion;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackRole;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_roles extends AdvancedJTable_networkElement<OpenStackRole>
{
    public AdvancedJTable_roles(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.ROLES , true);
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


        res.add(new AjtRcMenu("Change the user's description", e -> getSelectedElements().forEach(n -> {


        }), (a, b) -> b ==1, null));

        return res;

    }



}

