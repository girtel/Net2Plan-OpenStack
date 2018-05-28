package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackService;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_abstractElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

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
        res.add(new AjtColumnInfo<OpenStackService>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));



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

            createTableForUpdate("Name",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change service's description", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Tenant id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change service's type", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Device id",n);

        }), (a, b) -> b ==1, null));




        return res;

    }

    public void createTableForUpdate(String key, OpenStackService service) {
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
                        service.setServiceName(os_text_change.getText());
                        break;
                    case "Description":
                        service.setServiceDescription(os_text_change.getText());
                        break;
                    case "Type":
                        service.setServiceType(os_text_change.getText());
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
    public void addService(){

        callback.getOpenStackNet().getOsnc().createOpenStackService("name","description","type");
    }
    public void removeService(OpenStackService service){

        callback.getOpenStackNet().getOsnd().deleteOpenStackService(service.getId());
    }


}

