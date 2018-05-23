package com.net2plan.gui.plugins.networkDesign.openStack.network;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.api.Builders;
import org.openstack4j.model.network.*;

import java.util.List;

public class OpenStackNetwork extends OpenStackNetworkElement
{
    private String networkId ;
    private String networkName ;
    private String networkProviderPhyNet;
    private String networkProviderSegID ;
    private String networkTenantId ;
    private State networkState;
    private NetworkType networkType;
    private List<? extends Subnet> networkNeutronSubnets;
    private List<String> networkSubnets;
    private boolean networkIsAdminStateUp;
    private boolean networkIsRouterExternal;
    private boolean networkIsShared;
    private Integer networkMTU;

    private Network osNetwork;

   public static OpenStackNetwork createFromAddNetwork (OpenStackNet osn , String networkId, String networkName, State networkState, NetworkType networkType, List<? extends Subnet> networkNeutronSubnets, String networkProviderPhyNet, String networkProviderSegID, List <String> networkSubnets, String networkTenantId, boolean networkIsAdminStateUp, boolean networkIsRouterExternal, boolean networkIsShared,Integer networkMTU)
    {
        final OpenStackNetwork res = new OpenStackNetwork(osn);
        res.networkId= networkId;
        res.networkName=networkName;
        res.networkProviderPhyNet=networkProviderPhyNet;
        res.networkProviderSegID=networkProviderSegID;
        res.networkTenantId=networkTenantId;
        res.networkState= networkState;
        res.networkType=networkType;
        res.networkNeutronSubnets=networkNeutronSubnets;
        res.networkSubnets=networkSubnets;
        res.networkIsAdminStateUp=networkIsAdminStateUp;
        res.networkIsRouterExternal= networkIsRouterExternal;
        res.networkIsShared=networkIsShared;
        res.networkMTU=networkMTU;

        return res;
    }

    public OpenStackNetwork(OpenStackNet osn)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.list_osNetworks);
        if (this.networkId != null)
            this.osNetwork = this.osn.getOs().networking().network().get(this.networkId);

    }


    @Override
    public String getId () { return this.networkId; }
    public String getName () { return this.networkName; }
    public String getNetworkProviderPhyNet () { return this.networkProviderPhyNet; }
    public String getNetworkProviderSegID () { return this.networkProviderSegID; }
    public String getNetworkTenantId () { return this.networkTenantId; }
    public State  getNetworkState () { return this.networkState; }
    public NetworkType getNetworkType () { return this.networkType; }
    public List<? extends Subnet> getNetworkNeutronSubnets () { return this.networkNeutronSubnets; }
    public List<String> getNetworkSubnets () { return this.networkSubnets; }
    public boolean isNetworkIsAdminStateUp () { return this.networkIsAdminStateUp; }
    public boolean isNetworkIsRouterExternal () { return this.networkIsRouterExternal; }
    public boolean isNetworkIsShared () { return this.networkIsShared; }
    public Integer getNetworkMTU(){return this.networkMTU;}

    @Override
    public String get50CharactersDescription()
    {
        return "Network"+this.getId();
    }

    public void setName (String value) {  this.osn.getOs().networking().network().update(this.networkId, Builders.networkUpdate().name(value).build()); }
    public void isNetworkIsAdminStateUp (boolean value) {  this.osn.getOs().networking().network().update(this.networkId, Builders.networkUpdate().adminStateUp(value).build());  }
    public void isNetworkIsShared (boolean value) {  this.osn.getOs().networking().network().update(this.networkId, Builders.networkUpdate().shared(value).build()); }
}
