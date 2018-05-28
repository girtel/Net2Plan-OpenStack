package com.net2plan.gui.plugins.networkDesign.openStack.network;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackUser;
import com.net2plan.interfaces.networkDesign.Link;
import org.openstack4j.api.Builders;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.network.AllowedAddressPair;
import org.openstack4j.model.network.IP;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.State;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class OpenStackPort extends OpenStackNetworkElement
{

    private State portState;
    private boolean isAdminStateUp;
    private String portNetworkId;
    private String portDeviceId;
    private String portDeviceOwner;
    private Set<? extends IP> portFixedIps;
    private Set<? extends AllowedAddressPair> portAllowedAddressPair;
    private String portMacAddress;
    private List<String>  portSecurityGroups;
    private Map<String,Object> portProfile;
    private boolean portSecurityEnable;
    private String portName;
    private String portId;
    private String portTenantId;
    private String portHostId;
    private Port osPort;

    public static OpenStackPort createFromAddPort (OpenStackNet osn , String portId, String portName,  String portTenantId, Set<? extends AllowedAddressPair> portAllowedAddressPair,String portDeviceId, String portDeviceOwner, Set<? extends IP> portFixedIps,String portHostId,String portMacAddress,String portNetworkId,Map<String,Object> portProfile, List<String> portSecurityGroups,State portState,boolean isAdminStateUp,boolean portSecurityEnable)
    {
        final OpenStackPort res = new OpenStackPort(osn);
        res.portId= portId;
        res.portName=portName;
        res.portTenantId=portTenantId;
        res.portAllowedAddressPair=portAllowedAddressPair;
        res.portDeviceId=portDeviceId;
        res.portDeviceOwner= portDeviceOwner;
        res.portFixedIps=portFixedIps;
        res.portHostId=portHostId;
        res.portMacAddress=portMacAddress;
        res.portNetworkId=portNetworkId;
        res.portProfile= portProfile;
        res.portSecurityGroups=portSecurityGroups;
        res.portState=portState;
        res.isAdminStateUp=isAdminStateUp;
        res.portSecurityEnable=portSecurityEnable;
        return res;
    }

    private OpenStackPort (OpenStackNet osn)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.list_osPorts);
        if(this.portId != null)
        this.osPort = this.osn.getOs().networking().port().get(this.portId);
    }

    @Override
    public String getId () { return this.portId; }
    public String getName () { return this.portName; }
    public String getPortTenantId () { return this.portTenantId; }
    public Set<? extends AllowedAddressPair> getPortAllowedAddressPair () { return this.portAllowedAddressPair; }
    public Set<? extends IP> getPortFixedIps () { return this.portFixedIps; }
    public String getPortDeviceId () { return this.portDeviceId; }
    public String getPortDeviceOwner () { return this.portDeviceOwner; }
    public String getPortHostId () { return this.portHostId; }
    public String getPortMacAddress () { return this.portMacAddress; }
    public String getPortNetworkId () { return this.portNetworkId; }
    public Map<String,Object> getPortProfile () { return this.portProfile; }
    public List<String>  getPortSecurityGroups () { return this.portSecurityGroups; }
    public State getPortState () { return this.portState; }
    public boolean isAdminStateUp () { return this.isAdminStateUp; }
    public boolean isPortSecurityEnable () { return this.portSecurityEnable; }

    @Override
    public String get50CharactersDescription()
    {
        return "Port" + this.getId();
    }

    public void setName (String value) { this.osn.getOs().networking().port().update(osPort.toBuilder().name(value).build()); }
    public void setPortTenantId (String value) { this.osn.getOs().networking().port().update(osPort.toBuilder().tenantId(value).build());}
    public void setPortDeviceId (String value) {  this.osn.getOs().networking().port().update(osPort.toBuilder().deviceId(value).build());}
    public void setPortDeviceOwner (String value) {  this.osn.getOs().networking().port().update(osPort.toBuilder().deviceOwner(value).build()); }
    public void setPortHostId (String value) {  this.osn.getOs().networking().port().update(osPort.toBuilder().hostId(value).build()); }
    public void setPortMacAddress (String value) { this.osn.getOs().networking().port().update(osPort.toBuilder().macAddress(value).build()); }
    public void setPortNetworkId (String value) { this.osn.getOs().networking().port().update(osPort.toBuilder().networkId(value).build()); }
    public void setPortState (State value) { this.osn.getOs().networking().port().update(osPort.toBuilder().state(value).build()); }
    public void isAdminStateUp (boolean value) { this.osn.getOs().networking().port().update(osPort.toBuilder().adminState(value).build()); }
    public void isPortSecurityEnable (boolean value) { this.osn.getOs().networking().port().update(osPort.toBuilder().portSecurityEnabled(value).build()); }
    public void setPortVifType (String value) {this.osn.getOs().networking().port().update(osPort.toBuilder().vifType(value).build()); }
    public void setPortvNicType (String value) { this.osn.getOs().networking().port().update(osPort.toBuilder().vNicType(value).build()); }


}


