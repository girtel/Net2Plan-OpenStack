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
        //res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "ID", "Policy ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "User ID", "Policy user id", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getPolicyUserId()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "Project ID", "Policy project id", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getPolicyProjectId()),
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
    {
        final List<AjtRcMenu> res = new ArrayList<>();

         return res;

    }


}
