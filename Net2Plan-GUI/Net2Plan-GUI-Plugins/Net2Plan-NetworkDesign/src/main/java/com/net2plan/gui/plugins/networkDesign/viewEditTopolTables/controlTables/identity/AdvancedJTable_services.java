package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackService;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_abstractElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

public class AdvancedJTable_services extends AdvancedJTable_networkElement<OpenStackService>
{
    public AdvancedJTable_services(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.SERVICES , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackService>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackService>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackService>(this, String.class, null, "ID", "Service ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, String.class, null, "Name", "Service name", null, n -> n.getServiceName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, String.class, null, "Description", "Service description", null, n -> n.getServiceDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, String.class, null, "Type", "Service type", null, n -> n.getServiceType(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, String.class, null, "Version", "Service version", null, n -> n.getServiceVersion(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackService>(this, Boolean.class, null, "Enabled", "Service enable", null, n -> n.isServiceEnabled(),
                AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackService>(this, List.class, null, "Endpoints", "Service endpoints",
                null, n -> n.getServiceEndpoints(), AdvancedJTable_abstractElement.AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackService>(this, List.class, null, "Links", "Service links",
                null, n -> n.getServiceLinks(), AdvancedJTable_abstractElement.AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add service", e -> addService(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove service", e -> getSelectedElements().forEach(n -> {

            removeService(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change service's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n);

        }), (a, b) -> b ==1, null));


        return res;

    }

    public void addService(){
        Map<String,String> newList = new HashMap<>();
        newList.put("Name","");
        newList.put("Service type","Select");

        generalTableForm("Add service",newList);

    }
    public void removeService(OpenStackService service){

        callback.getOpenStackNet().getOpenStackNetDelete().deleteOpenStackService(service.getId());
        updateTab();
    }


}

