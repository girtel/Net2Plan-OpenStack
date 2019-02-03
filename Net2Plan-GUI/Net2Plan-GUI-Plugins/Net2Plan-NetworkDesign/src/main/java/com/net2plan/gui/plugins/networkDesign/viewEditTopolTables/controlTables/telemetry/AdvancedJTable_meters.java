package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackMeter;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_meters extends AdvancedJTable_networkElement<OpenStackMeter>
{
    public AdvancedJTable_meters(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.METERS , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackMeter>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackMeter>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackMeter>(this, Object.class, null, "ID", "Meter ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackMeter>(this, String.class, null, "Name", "Meter Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackMeter>(this, String.class, null, "Project ID", "Project ID", null, n -> n.getMeter_project_id(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackMeter>(this, String.class, null, "Resource ID", "Meter Resource ID",
                null, n -> n.getMeter_resource_id(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackMeter>(this, String.class, null, "Type", "Meter type",
                null, n -> n.getMeter_type(), AGTYPE.NOAGGREGATION, null, null));
         res.add(new AjtColumnInfo<OpenStackMeter>(this, String.class, null, "Unit", "Meter unit", null, n -> n.getMeter_unit(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Refresh", e ->updateTab(), (a, b) -> b >=0, null));
        res.add(new AjtRcMenu("Get measures of metric", e -> getSelectedElements().forEach(n -> {

            //System.out.println("GETTING MEASURES" + n.getId());
            //n.getOpenStackClient().updateMeasuresList(n,n.getId());

        }), (a, b) -> b == 1, null));


        return res;

    }

    public void addUser() {

    }




}