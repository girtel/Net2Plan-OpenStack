package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackFloatingIp;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_floatingIp extends AdvancedJTable_networkElement<OpenStackFloatingIp>
{
    public AdvancedJTable_floatingIp(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.FLOATINGIPS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackFloatingIp>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackFloatingIp>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "ID", "Floating IP ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "Address", "Floating IP address",
                null, n -> n.getFloatingIPFloatingIpAddress(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "Pool", "Pool floating ID", null, n -> n.getFloatingIPPool(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "Instance ID", "Instance ID floatingip", null, n -> n.getFloatingIPInstanceId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "Fixed Address", "Floating IP Fixed Ip address",
                null, n -> n.getFloatingIPFixedIpAddress(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "Address", "Floating IP address",
                null, n -> n.getFloatingIPFloatingIpAddress(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add floating ip", e -> addFloatingIp(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove floating ip", e -> getSelectedElements().forEach(n -> {

            removeFloatingIp(n);

        }), (a, b) -> b == 1, null));


        return res;

    }

    public void addFloatingIp(){

        callback.getOpenStackNet().getOsnc().createOpenStackFloatingIp("serverip","floatingip");
    }
    public void removeFloatingIp(OpenStackFloatingIp floatingIp){

        callback.getOpenStackNet().getOsnd().deleteOpenStackFloatingIp(floatingIp.getId());
    }

}