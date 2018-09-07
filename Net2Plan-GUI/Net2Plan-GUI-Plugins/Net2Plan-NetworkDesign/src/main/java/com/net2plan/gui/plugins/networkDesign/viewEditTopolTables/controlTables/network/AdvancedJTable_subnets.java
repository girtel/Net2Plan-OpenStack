package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network;


import java.util.*;


import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
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
    public AdvancedJTable_subnets(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, AJTableType.SUBNETS , true,openStackClient);
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

            Map<String,String> headers = new HashMap<>();
            headers.put("Name","");
            generalTableFormUpdate("Change name",headers,"Name",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Add subnet's DNS", e -> getSelectedElements().forEach(n -> {

            Map<String,String> headers = new HashMap<>();
            headers.put("DNS","Special-ipv4");
            generalTableFormUpdate("Add dns",headers,"DNS",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Add subnet's pool", e -> getSelectedElements().forEach(n -> {

            Map<String,String> headers = new HashMap<>();
            headers.put("Start","Special-ipv4");
            headers.put("End","Special-ipv4");
            generalTableFormUpdate("Add pool",headers,"Pool",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change subnet's cidr", e -> getSelectedElements().forEach(n -> {

            Map<String,String> headers = new HashMap<>();
            headers.put("CIDR","Special-ipv4masc");
            generalTableFormUpdate("Change CIDR",headers,"CIDR",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Add subnet's route", e -> getSelectedElements().forEach(n -> {

            Map<String,String> headers = new HashMap<>();
            headers.put("Destination","Special-ipv4masc");
            headers.put("Next hop","Special-ipv4masc");
            generalTableFormUpdate("Add route",headers,"Route",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Subnet no gateway", e -> getSelectedElements().forEach(n -> {

           n.subnetNoGateway();
updateTab();
        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change subnet's gateway", e -> getSelectedElements().forEach(n -> {

            Map<String,String> headers = new HashMap<>();
            headers.put("Gateway","Special-ipv4");
            generalTableFormUpdate("Change Gateway",headers,"Gateway",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change subnet's network ID", e -> getSelectedElements().forEach(n -> {

            Map<String,String> headers = new HashMap<>();
            headers.put("Network ID","Select");
            generalTableFormUpdate("Change network ID",headers,"Network ID",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change subnet's Tenant ID", e -> getSelectedElements().forEach(n -> {

            Map<String,String> headers = new HashMap<>();
            headers.put("Tenant ID","Select");
            generalTableFormUpdate("Change Tenant ID",headers,"Tenant ID",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change subnet's IP version", e -> getSelectedElements().forEach(n -> {

            Map<String,String> headers = new HashMap<>();
            headers.put("IP version","Select");
            generalTableFormUpdate("Change IP version",headers,"IP version",n);

        }), (a, b) -> b ==1, null));


        return res;

    }

    public void addSubnet(){

        Map<String,String> headers = new HashMap<>();
        headers.put("Name","");
        headers.put("Network ID","Select");
        headers.put("IP version","Select");
        headers.put("Cidr","Special-ipv4masc");
        headers.put("Tenant ID","Select");
        generalTableForm("Add subnet",headers);

    }
    public void removeSubnet(OpenStackSubnet subnet){

        openStackClient.getOpenStackNetDelete().deleteOpenStackSubnet(subnet.getId());
        updateTab();
    }


}
