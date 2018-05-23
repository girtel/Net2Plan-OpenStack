package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import org.openstack4j.model.network.ExternalGateway;
import org.openstack4j.model.network.State;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_routers extends AdvancedJTable_networkElement<OpenStackRouter>
{
    public AdvancedJTable_routers(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.ROUTERS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackRouter>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackRouter>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackRouter>(this, String.class, null, "ID", "Router ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, String.class, null, "Name", "Router name", null, n -> n.getRouterName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, String.class, null, "Tenant ID", "Router tenant id", null, n -> n.getRouterTenantId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, State.class, null, "State", "Router state", null, n -> n.getRouterState(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, Boolean.class, null, "AdminStateUp", "Router admin state", null, n -> n.isRouterIsAdminStateUp(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, Boolean.class, null, "Distributed", "Router distributed", null, n -> n.isRouterIsDistributed(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, List.class, null, "Routes", "Router routes", null, n -> n.getRouterRoutes(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, ExternalGateway.class, null, "Gateway info", "Router external gateway info", null, n -> n.getRouterExternalGatewayInfo(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        return res;
    }


}
