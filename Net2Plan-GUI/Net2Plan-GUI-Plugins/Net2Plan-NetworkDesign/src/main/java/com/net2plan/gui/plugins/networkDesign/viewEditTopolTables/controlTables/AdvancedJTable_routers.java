package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane.AJTableType;

/**
 */
@SuppressWarnings("unchecked")
public class AdvancedJTable_routers extends AdvancedJTable_networkElement<OpenStackRouter>
{
    public AdvancedJTable_routers(GUINetworkDesign callback)
    {
        super(callback, AJTableType.ROUTERS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackRouter>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackRouter>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackRouter>(this, String.class, null, "ID", "Router ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        return res;
    }


}
