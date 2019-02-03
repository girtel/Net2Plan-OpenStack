package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackGroup;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

public class AdvancedJTable_groups extends AdvancedJTable_networkElement<OpenStackGroup>
{
    public AdvancedJTable_groups(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.GROUPS , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackGroup>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackGroup>> res = new LinkedList<>();
       // res.add(new AjtColumnInfo<OpenStackGroup>(this, String.class, null, "ID", "Group ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGroup>(this, String.class, null, "Name", "Group name", null, n -> n.getGroupName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGroup>(this, String.class, null, "Description", "Group description", null, n -> n.getGroupDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGroup>(this, String.class, null, "Domain ID", "Group domain id", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getGroupDomainId()),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGroup>(this, List.class, null, "Links", "Group links",
                null, n -> n.getGroupLinks(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

    return res;

    }


}
