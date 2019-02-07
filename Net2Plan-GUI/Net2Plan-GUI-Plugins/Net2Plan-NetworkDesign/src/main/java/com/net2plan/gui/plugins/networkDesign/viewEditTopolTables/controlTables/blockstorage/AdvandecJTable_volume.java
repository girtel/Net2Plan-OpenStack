package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.blockstorage;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.blockstorage.OpenStackVolume;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvandecJTable_volume extends AdvancedJTable_networkElement<OpenStackVolume>
{
public AdvandecJTable_volume(GUINetworkDesign callback, OpenStackClient openStackClient)
        {
        super(callback, ViewEditTopologyTablesPane.AJTableType.VOLUMES , true, openStackClient);
        }

    @Override
    public List<AjtColumnInfo<OpenStackVolume>> getNonBasicUserDefinedColumnsVisibleOrNot()
            {

            final List<AjtColumnInfo<OpenStackVolume>> res = new LinkedList<>();
            //res.add(new AjtColumnInfo<OpenStackUser>(this, Object.class, null, "ID", "User ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
            res.add(new AjtColumnInfo<OpenStackVolume>(this, String.class, null, "Name", "Volume Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
            res.add(new AjtColumnInfo<OpenStackVolume>(this, String.class, null, "Type", "Volume Type", null, n -> n.getVolumeType(), AGTYPE.NOAGGREGATION, null, null));


        return res;
            }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        return res;

    }
}
