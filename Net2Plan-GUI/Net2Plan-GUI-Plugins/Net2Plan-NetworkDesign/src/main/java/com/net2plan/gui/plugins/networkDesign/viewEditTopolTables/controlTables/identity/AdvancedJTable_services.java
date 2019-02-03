package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackService;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_abstractElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

public class AdvancedJTable_services extends AdvancedJTable_networkElement<OpenStackService>
{
    public AdvancedJTable_services(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.SERVICES , true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackService>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackService>> res = new LinkedList<>();
        //res.add(new AjtColumnInfo<OpenStackService>(this, String.class, null, "ID", "Service ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, String.class, null, "Name", "Service name", null, n -> n.getServiceName(), AGTYPE.NOAGGREGATION, null, null));
          res.add(new AjtColumnInfo<OpenStackService>(this, String.class, null, "Type", "Service type", null, n -> n.getServiceType(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, Boolean.class, null, "Enabled", "Service enable", (n,v)-> n.setServiceEnabled((Boolean)v), n -> n.isServiceEnabled(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, List.class, null, "Endpoints", "Service endpoints",
                null, n -> n.getServiceEndpoints(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, String.class, null, "Description", "Service description", null, n -> n.getServiceDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, List.class, null, "Links", "Service links",
                null, n -> n.getServiceLinks(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();


        return res;

    }


}

