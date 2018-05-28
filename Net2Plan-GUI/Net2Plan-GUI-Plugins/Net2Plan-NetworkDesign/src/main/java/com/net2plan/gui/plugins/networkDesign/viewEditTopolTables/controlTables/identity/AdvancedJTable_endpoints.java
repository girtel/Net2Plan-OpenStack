package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackEndpoint;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.collections15.BidiMap;
import org.openstack4j.api.types.Facing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
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
        res.add(new AjtColumnInfo<OpenStackEndpoint>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));



        return res;
    }

    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add endpoint", e -> addEndpoint(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove endpoint", e -> getSelectedElements().forEach(n -> {

            removeEndpoint(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change endpoint's name", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Name",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change endpoint's region id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Region id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change endpoint's service id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Service id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change endpoint's type", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Type",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change endpoint's description", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Description",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change endpoint's region name", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Region name",n);

        }), (a, b) -> b ==1, null));


        return res;

    }

    public void createTableForUpdate(String key, OpenStackEndpoint endpoint) {
        JFrame jfM = new JFrame(key);
        jfM.setLayout(null);

        JButton jbP1 = new JButton("Enter");

        JPanel jp1 = new JPanel(new GridLayout(6, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas
        JLabel l6 = new JLabel(key, SwingConstants.LEFT);
        jp1.add(l6);
        JTextField os_text_change = new JTextField();
        jp1.add(os_text_change);


        jp1.setVisible(true);
        jp1.setBounds(10, 10, 200, 200);
        jbP1.setBounds(75, 90, 90, 25);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch(key){
                    case "Name":
                        endpoint.setEndpointName(os_text_change.getText());
                        break;
                    case "Region id":
                        endpoint.setEndpointRegionId(os_text_change.getText());
                        break;
                    case "Service id":
                        endpoint.setEndpointServiceId(os_text_change.getText());
                        break;
                    case "Type":
                        endpoint.setEndpointType(os_text_change.getText());
                        break;
                    case "Description":
                        endpoint.setEndpointDescription(os_text_change.getText());
                        break;
                    case "Region name":
                        endpoint.setEndpointRegion(os_text_change.getText());
                        break;

                }
                callback.getOpenStackNet().updateAllTables();
                final VisualizationState vs = callback.getVisualizationState();
                Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                        vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
                vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());
                callback.updateVisualizationAfterNewTopology();
                callback.addNetPlanChange();
                jfM.dispose();
            }});

        jfM.add(jbP1);
        jfM.add(jp1);

        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        jfM.setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        jfM.setSize(250, 160);
        jfM.setLocation(dim.width/2-jfM.getSize().width/2, dim.height/2-jfM.getSize().height/2);

        jfM.setResizable(false);
        jfM.setVisible(true);
        jfM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
    public void addEndpoint(){

        callback.getOpenStackNet().getOsnc().createOpenStackEndpoint("name","description","regionid","serviceid","type");
    }
    public void removeEndpoint(OpenStackEndpoint endpoint){

        callback.getOpenStackNet().getOsnd().deleteOpenStackEndpoint(endpoint.getId());
    }

}


