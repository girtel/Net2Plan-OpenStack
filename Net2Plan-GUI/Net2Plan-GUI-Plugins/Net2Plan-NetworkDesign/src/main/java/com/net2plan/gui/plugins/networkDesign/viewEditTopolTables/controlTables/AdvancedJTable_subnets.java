package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane.AJTableType;

/**
 */
@SuppressWarnings("unchecked")
public class AdvancedJTable_subnets extends AdvancedJTable_networkElement<OpenStackSubnet>
{
    public AdvancedJTable_subnets(GUINetworkDesign callback)
    {
        super(callback, AJTableType.SUBNETS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackSubnet>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackSubnet>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "ID", "Subnet ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Name", "Subnet Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Cidr", "Subnet Cidr", null, n -> n.getSubnetCidr(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Gateway", "Subnet Gateway",
                null, n -> n.getSubnetGateway(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Network ID", "Subnet network ID",
                null, n -> n.getSubnetNetworkId(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        return res;
    }


}
