package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackMeter;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackResource;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_resources extends AdvancedJTable_networkElement<OpenStackResource>
{
    public AdvancedJTable_resources(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.RESOURCES , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackResource>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackResource>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackResource>(this, Object.class, null, "ID", "Resource ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, String.class, null, "Source", "Resource Name", null, n -> n.getSource(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, String.class, null, "Project ID", "Resource Project ID", null, n -> n.getResource_project_id(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, String.class, null, "User ID", "Resource Project ID", null, n -> n.getResource_user_id(),
                AGTYPE.NOAGGREGATION, null, null));
        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Get metrics of resource", e -> getSelectedElements().forEach(n -> {

            System.out.println(n);
         n.getOpenStackClient().updateMeterList(n.getSourceId());

        }), (a, b) -> b == 1, null));
        res.add(new AjtRcMenu("Refresh", e ->updateTab(), (a, b) -> b >=0, null));


        return res;

    }

    public void refrescaMetrics(ArrayList<OpenStackResource> openStackResources) {

    }




}