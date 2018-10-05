package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackQuotas;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackQuotasUsage;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_quotasUsage extends AdvancedJTable_networkElement<OpenStackQuotasUsage> {
    public AdvancedJTable_quotasUsage(GUINetworkDesign callback, OpenStackClient openStackClient) {
        super(callback, ViewEditTopologyTablesPane.AJTableType.QUOTASUSAGE, true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackQuotasUsage>> getNonBasicUserDefinedColumnsVisibleOrNot() {

        final List<AjtColumnInfo<OpenStackQuotasUsage>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "OpenStack", "OpenStack ID", null, n -> n.getOpenStackClient().getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "Project ", " Project ID", null, n -> n.getProject_id(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "Cores", "Project Cores", null, n -> n.getVcpusUsage(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "Volumen", "Project Volumen GB", null, n -> n.getVolumenGbUsage(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "Ram", "Project Ram", null, n -> n.getRamUsage(), AGTYPE.NOAGGREGATION, null, null));
        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo() {
        final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Go to Glance", e -> getSelectedElements().forEach(n -> {


        }), (a, b) -> b == 1, null));

        return res;

    }
}





