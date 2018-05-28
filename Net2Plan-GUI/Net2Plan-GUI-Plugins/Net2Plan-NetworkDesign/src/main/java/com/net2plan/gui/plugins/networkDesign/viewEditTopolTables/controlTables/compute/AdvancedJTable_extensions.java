package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackExtension;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_extensions extends AdvancedJTable_networkElement<OpenStackExtension>
{
    public AdvancedJTable_extensions(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.EXTENSIONS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackExtension>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackExtension>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackExtension>(this, String.class, null, "ID", "Extension ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackExtension>(this, String.class, null, "Name", "Extension name", null, n -> n.getExtensionName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackExtension>(this, String.class, null, "Description", "Extension description", null, n -> n.getExtensionDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackExtension>(this, URI.class, null, "Namespace", "Extension URI",
                null, n -> n.getExtensionNamespace(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackExtension>(this, Date.class, null, "Update", "Date update",
                null, n -> n.getExtensionUpdated(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackExtension>(this, List.class, null, "Links", "Credentials links",
                null, n -> n.getExtensionLinks(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackExtension>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));


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