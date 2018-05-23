package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackDomain;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_domains extends AdvancedJTable_networkElement<OpenStackDomain>
{
    public AdvancedJTable_domains(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.DOMAINS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackDomain>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackDomain>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackDomain>(this, String.class, null, "ID", "Domain ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackDomain>(this, String.class, null, "Name", "Domain name", null, n -> n.getDomainName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackDomain>(this, String.class, null, "Description", "Domain description", null, n -> n.getDomainDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackDomain>(this, Boolean.class, null, "Enabled", "Domain enable",
                null, n -> n.isDomainEnabled(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackDomain>(this, String.class, null, "Links", "Domain links",
                null, n -> n.getDomainLinks(), AGTYPE.NOAGGREGATION, null, null));

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

