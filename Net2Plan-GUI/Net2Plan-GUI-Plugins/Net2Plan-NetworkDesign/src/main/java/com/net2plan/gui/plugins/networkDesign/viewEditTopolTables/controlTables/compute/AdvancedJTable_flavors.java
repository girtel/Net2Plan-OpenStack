package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackFlavor;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_flavors extends AdvancedJTable_networkElement<OpenStackFlavor>
{
    public AdvancedJTable_flavors(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.FLAVORS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackFlavor>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackFlavor>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, String.class, null, "ID", "Flavor ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, String.class, null, "Name", "Flavor name", null, n -> n.getFlavorName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, Integer.class, null, "Disk", "Flavor disk", null, n -> n.getFlavorDisk(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, Integer.class, null, "Ephemeral", "Flavor ephemeral",
                null, n -> n.getFlavorEphemeral(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, Integer.class, null, "Ram", "Flavor ram",
                null, n -> n.getFlavorRam(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, Integer.class, null, "RxTxCap", "Flavor RxTx Capacity",
                null, n -> n.getFlavorRxtxCap(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, Integer.class, null, "RxTxFactor", "Credentials links",
                null, n -> n.getFlavorRxtxFactor(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, Integer.class, null, "RxTxQuota", "Credential blob",
                null, n -> n.getFlavorRxtxQuota(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, Integer.class, null, "Swsp", "Flavor swap",
                null, n -> n.getFlavorSwap(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackFlavor>(this, Integer.class, null, "CPUs", "Flavor cpus",
                null, n -> n.getFlavorVcpus(), AGTYPE.NOAGGREGATION, null, null));

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