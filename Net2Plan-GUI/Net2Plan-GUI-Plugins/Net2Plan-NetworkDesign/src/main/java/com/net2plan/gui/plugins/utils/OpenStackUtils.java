package com.net2plan.gui.plugins.utils;


import com.net2plan.utils.Pair;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.network.Subnet;

import java.util.List;
import java.util.Map;


/**
 *
 * This class provides different functions to work with OpenStack
 *
 * @author Cesar San-Nicolas-Martinez
 */
public class OpenStackUtils {

    private OpenStackUtils()
    {}

    /**
     * Checks if an instance belongs to a specific network
     * @param instance instance to check
     * @param network network to check
     * @return true if instance belongs to network, false if not
     */
    public static boolean belongsToThisNetwork(Server instance, Subnet network)
    {
        String networkIP = network.getCidr();
        String instanceIP = OpenStackUtils.getFixedAndFloatingIPsFromInstance(instance).getFirst();
        IPAddressComparator ipAddressMatcher = new IPAddressComparator(networkIP);
        return ipAddressMatcher.belongsToThisNetwork(instanceIP);
    }

    /**
     * Obtains fixed and floating IP address from an instance
     * @param instance instance to get both IPs (fixed and floating)
     * @return Pair where first variable is fixed IP and second is floating IP
     */
    public static Pair<String, String> getFixedAndFloatingIPsFromInstance(Server instance)
    {
        String this_instancesIPs = "";
        String fixedIPAddress;
        String floatingIPAddress = null;
        Map<String, List<? extends Address>> addresses = instance.getAddresses().getAddresses();
        for (Map.Entry<String, List<? extends Address>> entry : addresses.entrySet())
        {
            List<Address> IPs = (List<Address>)entry.getValue();
            for(Address ip : IPs)
                this_instancesIPs += ip.getAddr()+" ";
        }

        String [] this_instanceIPsSplitted = this_instancesIPs.split(" ");
        if(this_instanceIPsSplitted.length == 1)
        {
            fixedIPAddress = this_instanceIPsSplitted[0];
        }
        else
        {
            floatingIPAddress = this_instanceIPsSplitted[1];
            fixedIPAddress = this_instanceIPsSplitted[0];
        }

        return Pair.unmodifiableOf(fixedIPAddress, floatingIPAddress);
    }

    public static void openStackLogDialog (){

    }
}
