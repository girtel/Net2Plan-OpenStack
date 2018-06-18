package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackRegion;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

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

        res.add(new AjtRcMenu("Add region", e -> addRegion(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove region", e -> getSelectedElements().forEach(n -> {

            removeRegion(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change region's description", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Description",n);

        }), (a, b) -> b ==1, null));




        return res;

    }



    public void addRegion(){

        Map<String,String> newList = new HashMap<>();
        newList.put("Region ID","");
        newList.put("Description","");
        generalTableForm("Add region",newList);
    }
    public void removeRegion(OpenStackRegion region){

        callback.getOpenStackNet().getOpenStackNetDelete().deleteOpenStackRegion(region.getId());
        updateTab();
    }


}

