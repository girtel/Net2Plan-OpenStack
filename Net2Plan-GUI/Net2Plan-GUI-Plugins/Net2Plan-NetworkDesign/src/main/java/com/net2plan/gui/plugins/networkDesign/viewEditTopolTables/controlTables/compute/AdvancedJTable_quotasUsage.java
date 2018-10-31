package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackQuotas;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackQuotasUsage;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackProject;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import org.openstack4j.model.compute.QuotaSet;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_quotasUsage extends AdvancedJTable_networkElement<OpenStackQuotasUsage> {
    public AdvancedJTable_quotasUsage(GUINetworkDesign callback, OpenStackClient openStackClient) {
        super(callback, ViewEditTopologyTablesPane.AJTableType.QUOTASUSAGE, true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackQuotasUsage>> getNonBasicUserDefinedColumnsVisibleOrNot() {

        final List<AjtColumnInfo<OpenStackQuotasUsage>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "OpenStack", "OpenStack ID", null, n -> n.getOpenStackClient().getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "Project ", " Project name", null, n -> n.getProject().getProjectName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "Cores", "Project Cores", null, n -> n.getVcpusUsage(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "Volumen", "Project Volumen GB", null, n -> n.getVolumenGbUsage(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotasUsage>(this, String.class, null, "Ram", "Project Ram", null, n -> n.getRamUsage(), AGTYPE.NOAGGREGATION, null, null));
        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo() {
        final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Get percentage of use", e -> getSelectedElements().forEach(n -> {

       adjustPercentage(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Refresh", e ->updateTab(), (a, b) -> b >=0, null));

        return res;

    }

    public void adjustPercentage(OpenStackQuotasUsage openStackQuotasUsage) {

        OpenStackClient openStackClientSelected = openStackQuotasUsage.getOpenStackClient();
        String openStackProjectId = openStackQuotasUsage.getProject_id();
        OpenStackProject openStackProject = (OpenStackProject) callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(openStackProjectId);

        if (openStackQuotasUsage.getVcpusUsage() == null) {
            JOptionPane.showMessageDialog(null, "NULL VALUES");
        } else {
            int vcpusUsage = openStackQuotasUsage.getVcpusUsage().intValue();
            int ramUsage = openStackQuotasUsage.getRamUsage().intValue();
            int volumenGbUsage = openStackQuotasUsage.getVolumenGbUsage().intValue();


            openStackClientSelected.updateClient();

            QuotaSet quotaSet = openStackClientSelected.getClient().compute().quotaSets().get(openStackProjectId);

            int ram = quotaSet.getRam();
            int cores = quotaSet.getCores();
            int volGb = quotaSet.getVolumes();

            int porcentajeRam = 0;
            int porcentajeVcpu = 0;
            int porcentajeVolGb = 0;

            if (ramUsage != 0)
                porcentajeRam = (ramUsage / ram) * 100;
            if (vcpusUsage != 0)
                porcentajeVcpu = (vcpusUsage / cores) * 100;
            if (volumenGbUsage != 0)
                porcentajeVolGb = (volumenGbUsage / volGb) * 100;

            JOptionPane.showMessageDialog(null, "Percentaje of use for project " +
                    openStackProject.getProjectName() +
                    "  RAM: " + porcentajeRam + "%" +
                    "Vcpu: " + porcentajeVcpu + "%" +
                    "VolGB: " + porcentajeVolGb + "%");

        }
    }
}





