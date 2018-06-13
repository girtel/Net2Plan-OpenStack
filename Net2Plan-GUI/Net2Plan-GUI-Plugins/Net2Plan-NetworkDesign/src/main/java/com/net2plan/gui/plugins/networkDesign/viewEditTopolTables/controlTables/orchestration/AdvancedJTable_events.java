package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.orchestration;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.orchestration.OpenStackEvent;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_events extends AdvancedJTable_networkElement<OpenStackEvent>
{
    public AdvancedJTable_events(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.EVENTS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackEvent>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackEvent>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackEvent>(this, Object.class, null, "ID", "Event ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEvent>(this, String.class, null, "Resource Name", "Event Resource Name", null, n -> n.getEventResourceName(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();


        return res;

    }




}
