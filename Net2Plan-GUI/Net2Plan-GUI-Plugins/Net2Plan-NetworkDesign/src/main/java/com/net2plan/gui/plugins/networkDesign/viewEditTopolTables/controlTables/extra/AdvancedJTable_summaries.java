package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.extra;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.extra.OpenStackSummary;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_summaries extends AdvancedJTable_networkElement<OpenStackSummary> {
    public AdvancedJTable_summaries(GUINetworkDesign callback, OpenStackClient openStackClient) {
        super(callback, ViewEditTopologyTablesPane.AJTableType.SUMMARY, true, openStackClient);
        System.out.println("Creating advancedjtable sumarry");
    }

    @Override
    public List<AjtColumnInfo<OpenStackSummary>> getNonBasicUserDefinedColumnsVisibleOrNot() {

        final List<AjtColumnInfo<OpenStackSummary>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackSummary>(this, String.class, null, "Metric", "Metric ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, String.class, null, "Max", " Total Cores", null, n -> n.getMax(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, String.class, null, "Min", "Used Cores", null, n -> n.getMin(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, String.class, null, "Media", "Total Ram", null, n -> n.getMed(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, String.class, null, "Mediana", "Used ram", null, n -> n.getMediana(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, String.class, null, "Moda", "Total instances", null, n -> n.getModa(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSummary>(this, String.class, null, "Desviación tipica", "Used instances", null, n -> n.getDesv(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo() {
        final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Adjust percentage of quotas", e -> getSelectedElements().forEach(n -> {



        }), (a, b) -> b == 1, null));

        return res;

    }
}

