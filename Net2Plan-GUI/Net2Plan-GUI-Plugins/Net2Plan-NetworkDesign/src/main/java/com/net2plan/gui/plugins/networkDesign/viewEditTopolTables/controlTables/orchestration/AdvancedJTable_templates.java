package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.orchestration;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.orchestration.OpenStackTemplate;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_templates extends AdvancedJTable_networkElement<OpenStackTemplate>
{
    public AdvancedJTable_templates(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.TEMPLATES , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackTemplate>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackTemplate>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackTemplate>(this, Object.class, null, "ID", "Template ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackTemplate>(this, String.class, null, "Name", " Template Name", null, n -> n.getStackName(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();


        return res;

    }




}