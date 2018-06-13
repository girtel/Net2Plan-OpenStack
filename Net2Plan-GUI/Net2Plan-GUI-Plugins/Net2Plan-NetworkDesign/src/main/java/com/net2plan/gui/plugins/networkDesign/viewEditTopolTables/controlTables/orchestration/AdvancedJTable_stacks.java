package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.orchestration;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.orchestration.OpenStackStack;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_stacks extends AdvancedJTable_networkElement<OpenStackStack>
{
    public AdvancedJTable_stacks(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.STACKS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackStack>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackStack>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackStack>(this, Object.class, null, "ID", "Stack ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackStack>(this, String.class, null, "Name", " Stack Name", null, n -> n.getStackName(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();


        return res;

    }




}