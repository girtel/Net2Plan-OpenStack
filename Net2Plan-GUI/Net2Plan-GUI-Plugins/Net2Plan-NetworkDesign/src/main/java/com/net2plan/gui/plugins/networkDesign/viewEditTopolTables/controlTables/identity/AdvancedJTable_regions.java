package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackRegion;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_regions extends AdvancedJTable_networkElement<OpenStackRegion>
{
    public AdvancedJTable_regions(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.REGIONS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackRegion>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackRegion>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackRegion>(this, String.class, null, "ID", "Region ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRegion>(this, String.class, null, "Description", "Region Description", null, n -> n.getRegionDescription(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRegion>(this, String.class, null, "Parent ID", "Region parent id", null, n -> n.getRegionParentRegionId(),
                AGTYPE.NOAGGREGATION, null, null));



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

