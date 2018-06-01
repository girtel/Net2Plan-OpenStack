package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackSecurityGroup;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_securityGroups extends AdvancedJTable_networkElement<OpenStackSecurityGroup>
{
    public AdvancedJTable_securityGroups(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.SECURITYGROUPS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackSecurityGroup>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackSecurityGroup>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "ID", "Security group ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Name", "Security name", null, n -> n.getSecGroupExtensionName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Description", "Security description", null, n -> n.getSecGroupExtensionDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Rules", "Security rules",
                null, n -> n.getSecGroupExtensionRules(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Tenant ID", "Security tenant id",
                null, n -> n.getSecGroupExtensionTenantId(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Add security group", e -> addSecurityGroup(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove security group", e -> getSelectedElements().forEach(n -> {

            removeSecurityGroup(n);

        }), (a, b) -> b == 1, null));
        return res;

    }

    public void addSecurityGroup(){

        List<String> newList = new ArrayList<>();
        newList.add("Name");
        newList.add("Description");
        generalTableForm("Add security group",newList);


    }
    public void removeSecurityGroup(OpenStackSecurityGroup securityGroup){

        callback.getOpenStackNet().getOpenStackNetDelete().deleteOpenStackSecurityGroup(securityGroup.getId());
        updateTab();
    }


}