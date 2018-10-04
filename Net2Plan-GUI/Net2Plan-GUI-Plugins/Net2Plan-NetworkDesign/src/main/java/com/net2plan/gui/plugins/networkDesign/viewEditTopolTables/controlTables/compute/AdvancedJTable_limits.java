package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackImage;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackLimits;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import org.openstack4j.model.compute.Image;

import java.util.*;

public class AdvancedJTable_limits extends AdvancedJTable_networkElement<OpenStackLimits> {
    public AdvancedJTable_limits(GUINetworkDesign callback, OpenStackClient openStackClient) {
        super(callback, ViewEditTopologyTablesPane.AJTableType.LIMITS, true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackLimits>> getNonBasicUserDefinedColumnsVisibleOrNot() {

        final List<AjtColumnInfo<OpenStackLimits>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackLimits>(this, String.class, null, "OpenStack", "OpenStack ID", null, n -> openStackClient.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, String.class, null, " Total Cores", " Total Cores", null, n -> n.getLimitCores(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Used Cores", "Used Cores", null, n -> n.getLimitCoresUsed(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Total Ram", "Total Ram", null, n -> n.getLimitRam(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Used Ram", "Used ram", null, n -> n.getLimitRamUsed(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Total instances", "Total instances", null, n -> n.getLimitInstances(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Used instances", "Used instances", null, n -> n.getLimitInstancesUsed(), AGTYPE.NOAGGREGATION, null, null));

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




