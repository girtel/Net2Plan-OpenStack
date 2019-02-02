package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackFloatingIp;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.utils.GeneralForm;

import java.util.*;

public class AdvancedJTable_floatingIp extends AdvancedJTable_networkElement<OpenStackFloatingIp>
{
    public AdvancedJTable_floatingIp(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.FLOATINGIPS , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackFloatingIp>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackFloatingIp>> res = new LinkedList<>();
        //res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "ID", "Floating IP ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "Address", "Floating IP address",
                null, n -> n.getFloatingIPFloatingIpAddress(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "Pool", "Pool floating ID", null, n -> n.getFloatingIPPool(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "Instance ID", "Instance ID floatingip", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getFloatingIPInstanceId()),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFloatingIp>(this, String.class, null, "Fixed Address", "Floating IP Fixed Ip address",
                null, n -> n.getFloatingIPFixedIpAddress(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add floating ip", e -> addFloatingIp(this.getSelectedElements()), (a, b) -> b==b, null));

        res.add(new AjtRcMenu("Remove floating ip", e -> getSelectedElements().forEach(n -> {

            removeFloatingIp(n);

        }), (a, b) -> b >= 1, null));


        return res;

    }

    public void addFloatingIp(ArrayList<OpenStackFloatingIp> openStackFloatingIps){

        Map<String,String> headers = new HashMap<>();
        headers.put("Server ID","Select");
        headers.put("Pool Name","Select");
        GeneralForm generalTableForm = new GeneralForm("Add floating ip",headers,this.ajtType,this.openStackClient,this,openStackFloatingIps.get(0));

    }
    public void removeFloatingIp(OpenStackFloatingIp floatingIp){

        openStackClient.getOpenStackNetDelete().deleteOpenStackFloatingIp(floatingIp.getId());
        updateThisTab();
    }

}