package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.information;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.information.OpenStackSummary;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_summary extends AdvancedJTable_networkElement<OpenStackSummary>
{
    public AdvancedJTable_summary(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.SUMMARY , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackSummary>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackSummary>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackSummary>(this, String.class, null, "ID", "Project ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, Integer.class, null, "Projects", "Number of projects", null, n -> n.getNumberOfProjects(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, Integer.class, null, "Users", "Number of users", null, n -> n.getNumberOfUsers(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, Integer.class, null, "Networks", "Number of networks", null, n -> n.getNumberOfNetworks(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, Integer.class, null, "Routers", "Number of routers", null, n -> n.getNumberOfRouters(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        return res;

    }



}
