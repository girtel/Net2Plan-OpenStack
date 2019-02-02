package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackMeter;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackResource;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.utils.AnalisisFrame;
import com.net2plan.gui.plugins.utils.Graficos;
import javafx.scene.control.Spinner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
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
        //res.add(new AjtColumnInfo<OpenStackResource>(this, Object.class, null, "ID", "Resource ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, String.class, null, "Type", "Resource Type", null, n -> n.getType(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, Object.class, null, "Source", "Resource Source", null, n -> {
            if(callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getSourceId())!=null)
                return callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getSourceId()) ;

            return n.getSourceId();
             }, AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, Object.class, null, "Project", "Resource Project", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getResource_project_id()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, Map.class, null, "Metrics", "Resource Metrics", null, n -> n.getMetrics(), AGTYPE.NOAGGREGATION, null, null));
        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        /*res.add(new AjtRcMenu("Get metrics of resource", e -> getSelectedElements().forEach(n -> {

            System.out.println(n);
         n.getOpenStackClient().updateMeterList(n.getSourceId());

        }), (a, b) -> b == 1, null));*/

        res.add(new AjtRcMenu("Show analysis window", e -> getSelectedElements().forEach(n -> {

            AnalisisFrame analisisFrame = new AnalisisFrame(callback,n);

        }), (a, b) -> b == 1, null));

        return res;

    }




}