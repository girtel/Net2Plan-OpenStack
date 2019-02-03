package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackFlavor;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackHostResource;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

public class AdvancedJTable_hostResources extends AdvancedJTable_networkElement<OpenStackHostResource>
{
    public AdvancedJTable_hostResources(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.HOSTRESOURCES , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackHostResource>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackHostResource>> res = new LinkedList<>();
        //res.add(new AjtColumnInfo<OpenStackHostResource>(this, String.class, null, "Host", "Host", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackHostResource>(this, String.class, null, "Name", "Host name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackHostResource>(this, Integer.class, null, "Disk", "Host dis in Gb", null, n -> n.getHostDisk(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackHostResource>(this, Integer.class, null, "CPUs", "Host CPUs",
                null, n -> n.getHostCpu(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackHostResource>(this, Integer.class, null, "Ram", "Host ram in Mb",
                null, n -> n.getHostMemory(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackHostResource>(this, Integer.class, null, "Project", "Host Project",
                null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getHostProject()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackHostResource>(this, Integer.class, null, "Service", "Host Service",
                null, n -> n.getHostService(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackHostResource>(this, Integer.class, null, "Zone", "Host Zone",
                null, n -> n.getHostZone(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

       /* res.add(new AjtRcMenu("Add flavor", e -> addFlavor(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove flavor", e -> getSelectedElements().forEach(n -> {

            removeFlavor(n);

        }), (a, b) -> b == 1, null));

*/


        return res;

    }


}
