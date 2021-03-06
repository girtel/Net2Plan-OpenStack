package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane.AJTableType;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.Ipv6AddressMode;
import org.openstack4j.model.network.Ipv6RaMode;

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

        res.add(new AjtColumnInfo<OpenStackSubnet>(this, List.class, null, "Allocation pool", "Subnet allocation pool", null, n -> n.getSubnetAllocationPools(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, List.class, null, "DNS", "Subnet dns list", null, n -> n.getSubnetDnsNames(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, List.class, null, "Host routes", "Subnet host routes", null, n -> n.getSubnetHostRoutes(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, IPVersionType.class, null, "Ip version type", "Subnet ip version type", null, n -> n.getSubnetIpVersion(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Tenant ID", "Subnet tenant ID", null, n -> n.getSubnetTenantId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, Boolean.class, null, "DHCP", "Subnet dhcp", null, n -> n.isSubnetIsDHCPEnabled(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, Ipv6AddressMode.class, null, "IPv6 address mode", "Subnet ipv6 address mode", null, n -> n.getSubnetIpv6AddressMode(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, Ipv6RaMode.class, null, "IPv6 Ra mode", "Subnet ipv6 ra mode", null, n -> n.getSubnetIpv6RaMode(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add subnet", e -> addSubnet(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove subnet", e -> getSelectedElements().forEach(n -> {

            removeSubnet(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change subnet's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n);

        }), (a, b) -> b ==1, null));


        return res;

    }

    public void addSubnet(){

        List<String> headers = new ArrayList<>();
        headers.add("Name");
        headers.add("Network ID");
        headers.add("IP version");
        headers.add("Cidr");
        generalTableForm("Add subnet",headers);

    }
    public void removeSubnet(OpenStackSubnet subnet){

        callback.getOpenStackNet().getOpenStackNetDelete().deleteOpenStackSubnet(subnet.getId());
        updateTab();
    }


}
