package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackPolice;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_policies extends AdvancedJTable_networkElement<OpenStackPolice>
{
    public AdvancedJTable_policies(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.GROUPS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackPolice>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackPolice>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackPolice>(this, String.class, null, "ID", "Policy ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolice>(this, String.class, null, "User ID", "Policy user id", null, n -> n.getPolicyUserId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolice>(this, String.class, null, "Project ID", "Policy project id", null, n -> n.getPolicyProjectId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolice>(this, String.class, null, "Type", "Policy type", null, n -> n.getPolicyType(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolice>(this, String.class, null, "Blob", "Policy blob", null, n -> n.getPolicyBlob(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolice>(this, List.class, null, "Links", "Policy links",
                null, n -> n.getPolicyLinks(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Change the user's description", e -> getSelectedElements().forEach(n -> {


        }), (a, b) -> b ==1, null));

        return res;

    }



}
