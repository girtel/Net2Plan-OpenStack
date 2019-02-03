package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackSecurityGroup;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

public class AdvancedJTable_securityGroups extends AdvancedJTable_networkElement<OpenStackSecurityGroup>
{
    public AdvancedJTable_securityGroups(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.SECURITYGROUPS , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackSecurityGroup>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackSecurityGroup>> res = new LinkedList<>();
        //res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "ID", "Security group ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Name", "Security name", null, n -> n.getSecGroupExtensionName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Rules", "Security rules",
                null, n -> n.getSecGroupExtensionRules(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Description", "Security description", null, n -> n.getSecGroupExtensionDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Project ID", "Security tenant id",
                null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getSecGroupExtensionTenantId()), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        return res;

    }



}