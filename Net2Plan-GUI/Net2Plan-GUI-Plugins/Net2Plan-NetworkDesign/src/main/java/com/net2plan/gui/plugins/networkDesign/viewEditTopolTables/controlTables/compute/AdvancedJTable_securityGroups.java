package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackSecurityGroup;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.utils.GeneralForm;
import org.openstack4j.api.Builders;
import org.openstack4j.model.compute.IPProtocol;
import org.openstack4j.model.compute.SecGroupExtension;

import java.util.*;
import java.util.stream.Collectors;

public class AdvancedJTable_securityGroups extends AdvancedJTable_networkElement<OpenStackSecurityGroup>
{
    public AdvancedJTable_securityGroups(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.SECURITYGROUPS , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackSecurityGroup>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackSecurityGroup>> res = new LinkedList<>();
        //res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "ID", "Security group ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Name", "Security name", null, n -> n.getSecGroupExtensionName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, Object.class, null, "Rules", "Security rules",
                null, n -> n.getSecGroupExtensionRules().stream().map(x-> openStackClient.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(x.getId())).collect(Collectors.toList()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Description", "Security description", null, n -> n.getSecGroupExtensionDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSecurityGroup>(this, String.class, null, "Project ID", "Security tenant id",
                null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getSecGroupExtensionTenantId()), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();
        res.add(new AjtRcMenu("Add segurity group", e -> addSegurityGroup(), (a, b) -> b==b, null));
        res.add(new AjtRcMenu("Add rule", e -> addRule(this.getSelectedElements()), (a, b) -> b==1, null));

        return res;

    }

    public void addRule(List<OpenStackSecurityGroup> stackSecurityGroups){
        Map<String,String> headers = new HashMap<>();
        headers.put("Cidr", "0.0.0.0/0");
        headers.put("IP Protocol","Select");
        GeneralForm generalTableForm = new GeneralForm("Add rule",headers,this.ajtType,this.openStackClient,this,stackSecurityGroups.get(0));

       // this.openStackClient.getClient().compute().securityGroups().createRule(Builders.secGroupRule().cidr("cidr").groupId("id").protocol(IPProtocol.ICMP).build());
    }

    public void addSegurityGroup(){
        Map<String,String> headers = new HashMap<>();
        headers.put("Name", "");
        headers.put("Description","");
        GeneralForm generalTableForm = new GeneralForm("Add security group",headers,this.ajtType,this.openStackClient,this,null);

    }
}