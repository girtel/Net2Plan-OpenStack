package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackPolicy;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_policies extends AdvancedJTable_networkElement<OpenStackPolicy>
{
    public AdvancedJTable_policies(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.POLICIES , true);
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

            generalTableUpdate("User ID",n);

        }), (a, b) -> b ==1, null));


        return res;

    }

    public void addPolicy(){

        List<String> newList = new ArrayList<>();
        newList.add("User ID");
        newList.add("Project ID");
        newList.add("Type");
        newList.add("Blob");
        generalTableForm("Add policy",newList);
    }
    public void removePolicy(OpenStackPolicy policy){

        callback.getOpenStackNet().getOpenStackNetDelete().deleteOpenStackPolicy(policy.getId());
        updateTab();
    }


}
