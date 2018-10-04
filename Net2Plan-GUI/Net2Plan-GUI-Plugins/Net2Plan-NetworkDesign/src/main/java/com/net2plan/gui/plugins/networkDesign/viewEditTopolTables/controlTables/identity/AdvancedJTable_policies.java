package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackPolicy;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

public class AdvancedJTable_policies extends AdvancedJTable_networkElement<OpenStackPolicy>
{
    public AdvancedJTable_policies(GUINetworkDesign callback,OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.POLICIES , true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackPolicy>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackPolicy>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "ID", "Policy ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "User ID", "Policy user id", null, n -> n.getPolicyUserId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "Project ID", "Policy project id", null, n -> n.getPolicyProjectId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "Type", "Policy type", null, n -> n.getPolicyType(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "Blob", "Policy blob", null, n -> n.getPolicyBlob(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, List.class, null, "Links", "Policy links",
                null, n -> n.getPolicyLinks(), AGTYPE.NOAGGREGATION, null, null));



        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add policy", e -> addPolicy(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove policy", e -> getSelectedElements().forEach(n -> {

            removePolicy(n);

        }), (a, b) -> b == 1, null));


        res.add(new AjtRcMenu("Change policy's user id", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("User ID",n,"Select");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change policy's tenant/project id", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Tenant ID",n,"Select");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change policy's type", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Type",n,"");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change policy's blob", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Blob",n,"");

        }), (a, b) -> b ==1, null));
        return res;

    }

    public void addPolicy(){

        Map<String,String> newList = new HashMap<>();
        newList.put("User ID","Select");
        newList.put("Tenant ID","Select");
        newList.put("Blob","Select");
        newList.put("Type","Select");
        generalTableForm("Add policy",newList);
    }
    public void removePolicy(OpenStackPolicy policy){

        openStackClient.getOpenStackNetDelete().deleteOpenStackPolicy(policy.getId());
        updateTab();
    }


}
