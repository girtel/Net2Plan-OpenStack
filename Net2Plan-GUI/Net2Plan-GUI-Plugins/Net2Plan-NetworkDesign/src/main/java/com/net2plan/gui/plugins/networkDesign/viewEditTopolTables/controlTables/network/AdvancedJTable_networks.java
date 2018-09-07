package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network;


import java.util.*;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane.AJTableType;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.model.network.State;

/**
 */
@SuppressWarnings("unchecked")
public class AdvancedJTable_networks extends AdvancedJTable_networkElement<OpenStackNetwork>
{
    public AdvancedJTable_networks(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, AJTableType.NETWORKS , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackNetwork>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackNetwork>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "ID", "Network ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "Name", "Network Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "Provider", "Network Provider PhyNet", null, n -> n.getNetworkProviderPhyNet(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "Provider ID", "Network Provider SegID",
                null, n -> n.getNetworkProviderSegID(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "Tenant ID", "NetworkTenantId",
                null, n -> n.getNetworkTenantId(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackNetwork>(this, State.class, null, "State", "Network state", null, n -> n.getNetworkState(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, NetworkType.class, null, "Type", "Network type", null, n -> n.getNetworkType(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, List.class, null, "Neutron", "Network neutron subnets", null, n -> n.getNetworkNeutronSubnets(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, List.class, null, "Subnets", "Network subnets",
                null, n -> n.getNetworkSubnets(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, Boolean.class, null, "Admin State", "Network admin state",
                null, n -> n.isNetworkIsAdminStateUp(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackNetwork>(this, Boolean.class, null, "Router external", "Network router external", null, n -> n.isNetworkIsRouterExternal(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, Boolean.class, null, "Shared", "Network shared", null, n -> n.isNetworkIsShared(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, Integer.class, null, "MTU", "Network MTU", null, n -> n.getNetworkMTU(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }

    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add network", e -> addNetwork(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove network", e -> getSelectedElements().forEach(n -> {

            removeNetwork(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change network's name", e -> getSelectedElements().forEach(n -> {

            Map<String,String> headers = new HashMap<>();
            headers.put("Name","");
            generalTableFormUpdate("Change name",headers,"Name",n);

        }), (a, b) -> b ==1, null));


        return res;

    }

    public void addNetwork(){

        Map<String,String> headers = new HashMap<>();
        headers.put("Name","");
        headers.put("Tenant ID","Select");
        headers.put("Network type","Select");
        headers.put("IsExternal","Boolean");
        headers.put("Provider ID","");
        generalTableForm("Add network",headers);



    }
    public void removeNetwork(OpenStackNetwork network){

        openStackClient.getOpenStackNetDelete().deleteOpenStackNetwork(network.getId());
        updateTab();
    }


}
