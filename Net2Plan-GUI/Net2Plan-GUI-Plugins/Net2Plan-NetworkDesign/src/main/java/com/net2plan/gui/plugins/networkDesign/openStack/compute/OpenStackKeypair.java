package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.compute.Keypair;

import java.util.Date;
import java.util.List;

public class OpenStackKeypair  extends OpenStackNetworkElement
{


    private String keypairId;
    private String keypairName;
    private String keypairUserId;
    private String keypairCreatedA;
    private String keypairDeleted;
    private String keypairDeletedAt;
    private String keypairUpdatedAt;
    private String keypairFingerprint;
    private String keypairPrivateKey;
    private String keypairPublicKey;
    private Keypair osKeypair;
    public static OpenStackKeypair createFromAddKeypair (OpenStackNet osn , Keypair keypair,OpenStackClient openStackClient)
    {
        final OpenStackKeypair res = new OpenStackKeypair(osn,keypair,openStackClient);
        res.keypairId= keypair.getName();
       // System.out.println("KEYPAIR"+keypair);
        res.keypairName=keypair.getName();
        try {
            res.keypairUserId = keypair.getName();
            res.keypairCreatedA = keypair.getName();
            res.keypairUpdatedAt = keypair.getName();
            res.keypairFingerprint = keypair.getFingerprint();
            res.keypairPrivateKey = keypair.getName();
            res.keypairPublicKey = keypair.getPublicKey();
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
        return res;
    }

    private OpenStackKeypair (OpenStackNet osn, Keypair keypair, OpenStackClient openStackClient)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackKeypairs,openStackClient);
        this.osKeypair = keypair;
    }

    @Override
    public String getId () { return this.keypairId.toString(); }
    public String getKeypairName () { return this.keypairName; }
    public String getName () { return this.keypairName; }
    public String getKeypairUserId () { return this.keypairUserId; }
    public String getKeypairCreatedA () { return this.keypairCreatedA; }
    public String isKeypairDeleted() { return this.keypairDeleted; }
    public String getKeypairDeletedAt () { return this.keypairDeletedAt; }
    public String getKeypairUpdatedAt () { return this.keypairUpdatedAt; }
    public String getKeypairFingerprint () { return this.keypairFingerprint; }
    public String getKeypairPrivateKey () { return this.keypairPrivateKey; }
    public String getKeypairPublicKey () { return this.keypairPublicKey; }



    @Override
    public String get50CharactersDescription()
    {
        String description = "Keypair: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getKeypairName() +
                this.NEWLINE + "Figerprint " + this.getKeypairFingerprint() +
                this.NEWLINE + "Private key " + this.getKeypairPrivateKey() +
                this.NEWLINE + "Public key " + this.getKeypairPublicKey() +
                this.NEWLINE + "User ID " + this.getKeypairUserId() +
                this.NEWLINE + "Created at " + this.getKeypairCreatedA() +
                this.NEWLINE + "Delete at " + this.getKeypairDeletedAt() +
                this.NEWLINE + "Update at " + this.getKeypairUpdatedAt()+
                this.NEWLINE + "Is deleted " + this.isKeypairDeleted();


        return description;
    }


}
