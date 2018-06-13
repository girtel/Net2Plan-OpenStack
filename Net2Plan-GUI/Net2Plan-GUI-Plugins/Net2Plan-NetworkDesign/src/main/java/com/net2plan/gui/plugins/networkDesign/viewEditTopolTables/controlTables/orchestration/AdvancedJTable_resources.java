package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.orchestration;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.orchestration.OpenStackResource;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_resources extends AdvancedJTable_networkElement<OpenStackResource>
{
    public AdvancedJTable_resources(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.RESOURCES , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackResource>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackResource>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackResource>(this, Object.class, null, "ID", "Resource ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, String.class, null, "Name", " Resource Name", null, n -> n.getResourceName(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();


        return res;

    }




}

