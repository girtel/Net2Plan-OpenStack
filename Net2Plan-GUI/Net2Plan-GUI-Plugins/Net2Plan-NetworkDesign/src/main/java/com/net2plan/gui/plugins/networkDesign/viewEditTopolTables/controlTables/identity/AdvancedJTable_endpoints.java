package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackEndpoint;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.openstack4j.api.types.Facing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_endpoints extends AdvancedJTable_networkElement<OpenStackEndpoint>
{
    public AdvancedJTable_endpoints(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.ENDPOINTS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackEndpoint>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackEndpoint>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "ID", "Endpoint ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "Name", "Endpoint name", null, n -> n.getEndpointName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "Description", "Endpoint description", null, n -> n.getEndpointDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, Facing.class, null, "IFace", "IFace endpoint",
                null, n -> n.getEndpointIface(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "Region", "Endpoint region",
                null, n -> n.getEndpointRegion(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "Region ID", "Endpoint region id",
                null, n -> n.getEndpointRegionId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "Service ID", "Endpoint service id",
                null, n -> n.getEndpointServiceId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "Type", "Endpoint type",
                null, n -> n.getEndpointType(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "URL", "Endpoint url",
                null, n -> n.getEndpointUrl(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, Boolean.class, null, "Enabled", "Endpoint enable",
                null, n -> n.isEndpointEnabled(), AGTYPE.NOAGGREGATION, null, null));


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


