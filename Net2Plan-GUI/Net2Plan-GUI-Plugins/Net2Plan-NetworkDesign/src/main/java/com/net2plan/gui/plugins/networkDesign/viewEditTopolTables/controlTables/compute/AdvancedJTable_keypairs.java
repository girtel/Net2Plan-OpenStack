package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackKeypair;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

public class AdvancedJTable_keypairs extends AdvancedJTable_networkElement<OpenStackKeypair>
{
    public AdvancedJTable_keypairs(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.KEYPAIRS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackKeypair>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackKeypair>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackKeypair>(this, String.class, null, "ID", "Keypair ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackKeypair>(this, String.class, null, "Name", "Keypair name", null, n -> n.getKeypairName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackKeypair>(this, String.class, null, "User ID", "Keypair user id", null, n -> n.getKeypairUserId(),
                AGTYPE.NOAGGREGATION, null, null));
           res.add(new AjtColumnInfo<OpenStackKeypair>(this, String.class, null, "Fingerprint", "Keypair Fingerprint",
                null, n -> n.getKeypairFingerprint(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackKeypair>(this, String.class, null, "Private key", "Keypair Private key",
                null, n -> n.getKeypairPrivateKey(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackKeypair>(this, String.class, null, "Public key", "Keypair Public key",
                null, n -> n.getKeypairPublicKey(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add keypair", e -> addKeypair(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove keypair", e -> getSelectedElements().forEach(n -> {

            removeKeypair(n);

        }), (a, b) -> b == 1, null));


        return res;

    }
    public void addKeypair(){
        Map<String,String> newList = new HashMap<>();
        newList.put("Name","");
        generalTableForm("Add keypair",newList);
    }
    public void removeKeypair(OpenStackKeypair keypair){

       callback.getOpenStackNet().getOpenStackNetDelete().deleteOpenStackKeypair(keypair.getId());
       updateTab();
    }


}