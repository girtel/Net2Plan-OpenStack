package com.net2plan.gui.plugins.networkDesign.openStack.network;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
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

    public static OpenStackPort createFromAddPort (OpenStackNet osn ,Port port)
    {
        final OpenStackPort res = new OpenStackPort(osn,port);
        res.portId= port.getId();
        res.portName=port.getName();
        res.portTenantId=port.getTenantId();
        res.portAllowedAddressPair=port.getAllowedAddressPairs();
        res.portDeviceId=port.getDeviceId();
        res.portDeviceOwner= port.getDeviceOwner();
        res.portFixedIps=port.getFixedIps();
        res.portHostId=port.getHostId();
        res.portMacAddress=port.getMacAddress();
        res.portNetworkId=port.getNetworkId();
        res.portProfile= port.getProfile();
        res.portSecurityGroups=port.getSecurityGroups();
        res.portState=port.getState();
        res.isAdminStateUp=port.isAdminStateUp();
        res.portSecurityEnable=port.isPortSecurityEnabled();
        return res;
    }

    private OpenStackPort (OpenStackNet osn,Port port)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackPorts);
        this.osPort = port;
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
        String description = "Port: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getName() +
                this.NEWLINE + "Router/Device ID " + this.getPortDeviceId() +
                this.NEWLINE + "Router/Device owner " + this.getPortDeviceOwner() +
                this.NEWLINE + "Host ID " + this.getPortHostId() +
                this.NEWLINE + "MAC " + this.getPortMacAddress() +
                this.NEWLINE + "Network ID " + this.getPortNetworkId() +
                this.NEWLINE + "Project/Tenant ID " + this.getPortTenantId() +
                this.NEWLINE + "State " + this.getPortState() +
                this.NEWLINE + "Admin state up " + this.isAdminStateUp() +
                this.NEWLINE + "Secrutiy Enable " + this.isPortSecurityEnable() ;

                description+=this.NEWLINE + "Allowed Address Pair" + this.NEWLINE;
                for(AllowedAddressPair allowedAddressPair : this.getPortAllowedAddressPair()) {
                    description += allowedAddressPair + " " + NEWLINE;
                }

        description+=this.NEWLINE + "Fixep IPs" + this.NEWLINE;
        for(IP ip : this.getPortFixedIps()) {
            description += ip + " " + NEWLINE;
        }

        description+=this.NEWLINE + "Profile" + this.NEWLINE;
        for(String key : this.getPortProfile().keySet()) {
            description += key + " " + this.getPortProfile().get(key) + NEWLINE;
        }

        description+=this.NEWLINE + "Security groups" + this.NEWLINE;
        for(String securityGroup : this.getPortSecurityGroups()) {
            description += securityGroup + " " + NEWLINE;
        }
        return description;
    }

    public void setName (String value) {
        try{
        this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().name(value).build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setPortTenantId (String value) { this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().tenantId(value).build());}
    public void setPortDeviceId (String value) {  this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().deviceId(value).build());}
    public void setPortDeviceOwner (String value) {  this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().deviceOwner(value).build()); }
    public void setPortHostId (String value) {  this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().hostId(value).build()); }
    public void setPortMacAddress (String value) { this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().macAddress(value).build()); }
    public void setPortNetworkId (String value) { this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().networkId(value).build()); }
    public void setPortState (State value) { this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().state(value).build()); }
    public void isAdminStateUp (boolean value) {
        try{
        this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().adminState(value).build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void isPortSecurityEnable (boolean value) {
        try{
        this.osn.getOSClientV3().networking().port().update(osPort.toBuilder().portSecurityEnabled(value).build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }


}
