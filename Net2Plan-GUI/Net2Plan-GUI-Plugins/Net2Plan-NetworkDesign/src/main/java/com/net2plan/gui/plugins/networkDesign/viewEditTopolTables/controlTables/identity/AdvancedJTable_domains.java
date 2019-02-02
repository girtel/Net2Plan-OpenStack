package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackDomain;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.*;

public class AdvancedJTable_domains extends AdvancedJTable_networkElement<OpenStackDomain>
{
    public AdvancedJTable_domains(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.DOMAINS , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackDomain>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackDomain>> res = new LinkedList<>();
        //res.add(new AjtColumnInfo<OpenStackDomain>(this, String.class, null, "ID", "Domain ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackDomain>(this, String.class, null, "Name", "Domain name", null, n -> n.getDomainName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackDomain>(this, Boolean.class, null, "Enabled", "Domain enable",
                null, n -> n.isDomainEnabled(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackDomain>(this, String.class, null, "Description", "Domain description", null, n -> n.getDomainDescription(),
                AGTYPE.NOAGGREGATION, null, null));
           res.add(new AjtColumnInfo<OpenStackDomain>(this, String.class, null, "Links", "Domain links",
                null, n -> n.getDomainLinks(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

       /* res.add(new AjtRcMenu("Add domain", e -> addDomain(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove domain", e -> getSelectedElements().forEach(n -> {

            removeDomain(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change domain's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n,"");

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change domain's description", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Description",n,"");

        }), (a, b) -> b ==1, null));
        */

        return res;

    }


    public void addDomain(){

        Map<String,String> newList = new HashMap<>();
        newList.put("Name","");
        newList.put("Enable","Boolean");

        //generalTableForm("Add domain",newList);
    }
    public void removeDomain(OpenStackDomain domain){
        System.out.println("Deleting");
        openStackClient.getOpenStackNetDelete().deleteOpenStackDomain(domain.getId());
        updateTab();
    }

}
