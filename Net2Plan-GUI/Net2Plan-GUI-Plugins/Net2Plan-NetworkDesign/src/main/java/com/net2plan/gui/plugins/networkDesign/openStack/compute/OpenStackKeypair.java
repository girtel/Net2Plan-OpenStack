package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.compute.Keypair;

import java.util.Date;
import java.util.List;

public class OpenStackKeypair  extends OpenStackNetworkElement
{


    private Integer keypairId;
    private String keypairName;
    private String keypairUserId;
    private Date keypairCreatedA;
    private boolean keypairDeleted;
    private Date keypairDeletedAt;
    private Date keypairUpdatedAt;
    private String keypairFingerprint;
    private String keypairPrivateKey;
    private String keypairPublicKey;
    private Keypair osKeypair;
    public static OpenStackKeypair createFromAddKeypair (OpenStackNet osn , Keypair keypair)
    {
        final OpenStackKeypair res = new OpenStackKeypair(osn,keypair);
        res.keypairId= keypair.getId();
        res.keypairName=keypair.getName();
        try {
            res.keypairUserId = keypair.getUserId();
            res.keypairCreatedA = keypair.getCreatedAt();
            res.keypairUpdatedAt = keypair.getUpdatedAt();
            res.keypairFingerprint = keypair.getFingerprint();
            res.keypairPrivateKey = keypair.getPrivateKey();
            res.keypairPublicKey = keypair.getPublicKey();
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
        return res;
    }

    private OpenStackKeypair (OpenStackNet osn,Keypair keypair )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackKeypairs);
        this.osKeypair = keypair;
    }

    @Override
    public String getId () { return this.keypairId.toString(); }
    public String getKeypairName () { return this.keypairName; }
    public String getKeypairUserId () { return this.keypairUserId; }
    public Date getKeypairCreatedA () { return this.keypairCreatedA; }
    public boolean isKeypairDeleted() { return this.keypairDeleted; }
    public Date getKeypairDeletedAt () { return this.keypairDeletedAt; }
    public Date getKeypairUpdatedAt () { return this.keypairUpdatedAt; }
    public String getKeypairFingerprint () { return this.keypairFingerprint; }
    public String getKeypairPrivateKey () { return this.keypairPrivateKey; }
    public String getKeypairPublicKey () { return this.keypairPublicKey; }



    @Override
    public String get50CharactersDescription()
    {
        return "Keypair: " + this.getId();
    }


}
