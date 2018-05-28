package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;

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

    public static OpenStackKeypair createFromAddKeypair (OpenStackNet osn , Integer keypairId, String keypairName, String keypairUserId, Date keypairCreatedA, boolean keypairDeleted, Date keypairDeletedAt, Date keypairUpdatedAt, String keypairFingerprint, String keypairPrivateKey, String keypairPublicKey)
    {
        final OpenStackKeypair res = new OpenStackKeypair(osn);
        res.keypairId= keypairId;
        res.keypairName=keypairName;
        res.keypairUserId=keypairUserId;
        res.keypairCreatedA=keypairCreatedA;
        res.keypairDeleted=keypairDeleted;
        res.keypairDeletedAt=keypairDeletedAt;
        res.keypairUpdatedAt=keypairUpdatedAt;
        res.keypairFingerprint=keypairFingerprint;
        res.keypairPrivateKey=keypairPrivateKey;
        res.keypairPublicKey=keypairPublicKey;
        return res;
    }

    private OpenStackKeypair (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osKeypairs);
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
        return "Keypair" + this.getId();
    }


}
