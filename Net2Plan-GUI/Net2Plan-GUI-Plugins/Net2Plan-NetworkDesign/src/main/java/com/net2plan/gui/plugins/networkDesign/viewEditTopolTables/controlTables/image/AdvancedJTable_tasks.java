package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.image;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackTask;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_tasks extends AdvancedJTable_networkElement<OpenStackTask>
{
    public AdvancedJTable_tasks(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.TASKS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackTask>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackTask>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackTask>(this, Object.class, null, "ID", "Task ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackTask>(this, String.class, null, "Name", " Task Name", null, n -> n.getTaskMessage(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();


        return res;

    }




}