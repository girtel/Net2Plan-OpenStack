package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.interfaces.networkDesign.Resource;
import java.util.List;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.model.network.State;
import org.openstack4j.model.network.Subnet;

/**
 *
 * @author Manuel
 */
public class OpenStackNetwork extends OpenStackNetworkElement
{
    private String networkId = "";
    private String networkName = "";
    private String networkProviderPhyNet = "";
    private String networkProviderSegID ="";
    private String networkTenantId = "";
    private State networkState;
    private NetworkType networkType;
    private List<? extends Subnet> networkNeutronSubnets;
    private List <String> networkSubnets;
    private boolean networkIsAdminStateUp;
    private boolean networkIsRouterExternal;
    private boolean networkIsShared;

    static OpenStackNetwork createFromAddNetwork (OpenStackNet osn ,String networkId,String networkName,State networkState,NetworkType networkType,List<? extends Subnet> networkNeutronSubnets,String networkProviderPhyNet,String networkProviderSegID,List <String> networkSubnets,String networkTenantId,boolean networkIsAdminStateUp,boolean networkIsRouterExternal, boolean networkIsShared)
    {
        final OpenStackNetwork res = new OpenStackNetwork(osn,null);
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

        return res;
    }

    public OpenStackNetwork(OpenStackNet osn, Resource npDummyResource)
    {
        super (osn , npDummyResource , (List<OpenStackNetworkElement>) (List<?>) osn.list_osNetworks);
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

    OpenStackNetwork getOpenStackNetwork () { return this; }

    @Override
    public String get50CharactersDescription()
    {
        return this.getId();
    }
}
