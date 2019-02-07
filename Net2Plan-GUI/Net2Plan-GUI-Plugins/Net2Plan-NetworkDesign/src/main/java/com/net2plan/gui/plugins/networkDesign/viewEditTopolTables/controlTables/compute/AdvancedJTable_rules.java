package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackRule;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackSecurityGroup;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackUser;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.utils.GeneralForm;

import java.util.*;

public class AdvancedJTable_rules extends AdvancedJTable_networkElement<OpenStackRule>
{
    public AdvancedJTable_rules(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.RULES , true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackRule>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackRule>> res = new LinkedList<>();
        //res.add(new AjtColumnInfo<OpenStackRule>(this, Object.class, null, "ID", "User ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
       // res.add(new AjtColumnInfo<OpenStackRule>(this, String.class, null, "Name", "Rule Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRule>(this, String.class, null, "Group ID", "Parent Group ID", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getRuleParentGroupId()),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRule>(this, String.class, null, "IpRange", "Cidr", null, n -> n.getRuleRangeIP().getCidr(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRule>(this, String.class, null, "IPProtocol", "IPProtocol",
                null, n -> n.getRuleIPProtocol(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRule>(this, String.class, null, "From Port", "From Port",
                null, n -> n.getRuleFromPort(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRule>(this, String.class, null, "To Port", "To Port", null, n -> n.getRuleToPort(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add rule", e -> addRule(), (a, b) -> b==b, null));



        return res;

    }


    public void addRule(){
        Map<String,String> headers = new HashMap<>();
        headers.put("Cidr", "0.0.0.0/0");
        headers.put("IP Protocol","Select");
        headers.put("Security group ID","Select");
        GeneralForm generalTableForm = new GeneralForm("Add rule",headers,this.ajtType,this.openStackClient,this,null);

        // this.openStackClient.getClient().compute().securityGroups().createRule(Builders.secGroupRule().cidr("cidr").groupId("id").protocol(IPProtocol.ICMP).build());
    }

}

