package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackGnocchiMeasure;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackResource;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_measures extends AdvancedJTable_networkElement<OpenStackGnocchiMeasure> {
    public AdvancedJTable_measures(GUINetworkDesign callback, OpenStackClient openStackClient) {
        super(callback, ViewEditTopologyTablesPane.AJTableType.MEASURES, true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackGnocchiMeasure>> getNonBasicUserDefinedColumnsVisibleOrNot() {

        final List<AjtColumnInfo<OpenStackGnocchiMeasure>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackGnocchiMeasure>(this, Object.class, null, "ID", "Measure ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGnocchiMeasure>(this, String.class, null, "Timestamp", "Measure Timestamp", null, n -> n.getTimestamp(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGnocchiMeasure>(this, String.class, null, "Granurality", "Granurality Value", null, n -> n.getGranularity(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGnocchiMeasure>(this, String.class, null, "Value", "Measure Value", null, n -> n.getValue(),
                AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo() {
        final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add new user", e -> addUser(), (a, b) -> true, null));


        return res;

    }

    public void addUser() {

    }
}




