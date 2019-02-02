package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackEndpoint;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import org.openstack4j.api.types.Facing;

import java.net.URL;
import java.util.*;

public class AdvancedJTable_endpoints extends AdvancedJTable_networkElement<OpenStackEndpoint>
{
    public AdvancedJTable_endpoints(GUINetworkDesign callback,OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.ENDPOINTS , true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackEndpoint>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackEndpoint>> res = new LinkedList<>();
        /*res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "ID", "Endpoint ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "Name", "Endpoint name", null, n -> n.getEndpointName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "Description", "Endpoint description", null, n -> n.getEndpointDescription(),
                AGTYPE.NOAGGREGATION, null, null));*/

        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, Object.class, null, "Region ID", "Endpoint region id",
                null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getEndpointRegionId()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, Object.class, null, "Service ID", "Endpoint service id",
                null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getEndpointServiceId()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, Boolean.class, null, "Enabled", "Endpoint enable",
                null, n -> n.isEndpointEnabled(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, Facing.class, null, "IFace", "IFace endpoint",
                null, n -> n.getEndpointIface(), AGTYPE.NOAGGREGATION, null, null));
        /*res.add(new AjtColumnInfo<OpenStackEndpoint>(this, String.class, null, "Type", "Endpoint type",
                null, n -> n.getEndpointType(), AGTYPE.NOAGGREGATION, null, null));*/
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, URL.class, null, "URL", "Endpoint url",
                null, n -> n.getEndpointUrl(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }

    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

      /*  res.add(new AjtRcMenu("Add endpoint", e -> addEndpoint(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove endpoint", e -> getSelectedElements().forEach(n -> {

            removeEndpoint(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change endpoint's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n,"");

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change endpoint's description", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Description",n,"");

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change endpoint's region", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Region ID",n,"Select");

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change endpoint's facing", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Facing",n,"Select");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change endpoint's service ID", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Service ID",n,"Select");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change endpoint's type", e -> getSelectedElements().forEach(n -> {

        generalTableUpdate("Type",n,"");

    }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change endpoint's url", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("URL",n,"");

        }), (a, b) -> b ==1, null));
*/
         return res;

    }

    public void addEndpoint(){

        Map<String,String> newList = new HashMap<>();
        newList.put("Name","");
        newList.put("Facing","Select");
        newList.put("Service ID","Select");
        newList.put("URL","");

        //generalTableForm("Add endpoint",newList);
    }
    public void removeEndpoint(OpenStackEndpoint endpoint){

        openStackClient.getOpenStackNetDelete().deleteOpenStackEndpoint(endpoint.getId());
        updateTab();
    }

}

