package com.net2plan.gui.plugins.networkDesign.openStack.network;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.interfaces.networkDesign.Resource;
import java.util.List;

import org.openstack4j.api.Builders;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.model.network.State;
import org.openstack4j.model.network.Subnet;
import sun.nio.ch.Net;

/**
 *
 * @author Manuel
 */


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

        public static OpenStackNetwork createFromAddNetwork (OpenStackNet osn ,Network network)
        {
            final OpenStackNetwork res = new OpenStackNetwork(osn,network);
            res.networkId= network.getId();
            res.networkName=network.getName();
            res.networkProviderPhyNet=network.getProviderPhyNet();
            res.networkProviderSegID=network.getProviderSegID();
            res.networkTenantId=network.getTenantId();
            res.networkState= network.getStatus();
            res.networkType=network.getNetworkType();
            res.networkNeutronSubnets=network.getNeutronSubnets();
            res.networkSubnets=network.getSubnets();
            res.networkIsAdminStateUp=network.isAdminStateUp();
            res.networkIsRouterExternal= network.isRouterExternal();
            res.networkIsShared=network.isShared();
            res.networkMTU=network.getMTU();


            return res;
        }

        public OpenStackNetwork(OpenStackNet osn, Network network)
        {
            super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackNetworks);

                this.osNetwork = network;

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
            String description = "Policy: " +
                    this.NEWLINE + "ID " + this.getId() +
                    this.NEWLINE + "Name " + this.getName() +
                    this.NEWLINE + "Provider physical net " + this.getNetworkProviderPhyNet() +
                    this.NEWLINE + "Provider segment ID " + this.getNetworkProviderSegID() +
                    this.NEWLINE + "Tenant/Project ID " + this.getNetworkTenantId() +
                    this.NEWLINE + "MTU " + this.getNetworkMTU() +
                    this.NEWLINE + "State " + this.getNetworkState() +
                    this.NEWLINE + "Type " + this.getNetworkType() +
                    this.NEWLINE + "Admin state up " + this.isNetworkIsAdminStateUp() +
                    this.NEWLINE + "Router external " + this.isNetworkIsRouterExternal() +
                    this.NEWLINE + "Shared " + this.isNetworkIsShared();
            if(this.getNetworkNeutronSubnets()!=null) {
                description += this.NEWLINE + "Neutron subnets" + this.NEWLINE;
                for (Subnet subnet : this.getNetworkNeutronSubnets()) {
                    description += subnet + " " + NEWLINE;
                }
            }
            if(this.getNetworkSubnets()!=null) {
                description += this.NEWLINE + "Subnets" + this.NEWLINE;
                for (String subnet : this.getNetworkSubnets()) {
                    description += subnet + " " + NEWLINE;
                }
            }
            return description;
        }

        public void setName (String value) {
            try{
            this.osn.getOSClientV3().networking().network().update(this.networkId, Builders.networkUpdate().name(value).build());

            }catch(Exception ex){

                logPanel();
                System.out.println(ex.toString());

            }
            }
        public void isNetworkIsAdminStateUp (boolean value) {
            try{
            this.osn.getOSClientV3().networking().network().update(this.networkId, Builders.networkUpdate().adminStateUp(value).build());

            }catch(Exception ex){

                logPanel();
                System.out.println(ex.toString());

            }
            }
        public void isNetworkIsShared (boolean value) {
            try{

                this.osn.getOSClientV3().networking().network().update(this.networkId, Builders.networkUpdate().shared(value).build());

            }catch(Exception ex){

                logPanel();
                System.out.println(ex.toString());

            }
            }
    }
