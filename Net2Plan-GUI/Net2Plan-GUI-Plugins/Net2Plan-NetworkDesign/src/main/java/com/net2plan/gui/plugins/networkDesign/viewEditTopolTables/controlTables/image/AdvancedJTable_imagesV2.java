package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.image;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackImageV2;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_imagesV2 extends AdvancedJTable_networkElement<OpenStackImageV2>
{
    public AdvancedJTable_imagesV2(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.IMAGESV2 , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackImageV2>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackImageV2>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackImageV2>(this, Object.class, null, "ID", "Image ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImageV2>(this, String.class, null, "Name", " Image Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();


        return res;

    }




}