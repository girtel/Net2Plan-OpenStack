package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackFloatingIp;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackServer;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackProject;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackService;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackUser;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackImageV2;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.utils.MyRunnable;
import com.net2plan.gui.plugins.utils.OpenStackUtils;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.network.AttachInterfaceType;
import org.openstack4j.model.network.RouterInterface;
import org.openstack4j.model.network.State;
import org.openstack4j.openstack.networking.domain.NeutronIP;

import javax.swing.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static edu.emory.mathcs.utils.ConcurrencyUtils.prevPow2;
import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

public class OpenStackNetDelete {

    private OpenStackClient openStackClient;

    public OpenStackNetDelete(OpenStackClient openStackClient){
         this.openStackClient = openStackClient;
    }

    public void deleteOpenStackNetworkElement(OpenStackNetworkElement openStackNetworkElement){

        ActionResponse actionResponse = null;

        try {
            if (openStackNetworkElement instanceof OpenStackUser) {

                actionResponse = this.openStackClient.getClient().identity().users().delete(openStackNetworkElement.getId());

            } else if (openStackNetworkElement instanceof OpenStackProject) {

                actionResponse = this.openStackClient.getClient().identity().projects().delete(openStackNetworkElement.getId());

            } else if (openStackNetworkElement instanceof OpenStackNetwork) {

                actionResponse = this.openStackClient.getClient().networking().network().delete(openStackNetworkElement.getId());

            } else if (openStackNetworkElement instanceof OpenStackSubnet) {

                actionResponse = this.openStackClient.getClient().networking().subnet().delete(openStackNetworkElement.getId());

            } else if (openStackNetworkElement instanceof OpenStackRouter) {

                actionResponse = this.openStackClient.getClient().networking().router().delete(openStackNetworkElement.getId());

            } else if (openStackNetworkElement instanceof OpenStackPort) {

                OpenStackPort openStackPort = (OpenStackPort) openStackNetworkElement;
                OpenStackRouter openStackRouter = (OpenStackRouter)openStackClient.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(openStackPort.getPortDeviceId());
                //OpenStackSubnet openStackSubnet = (OpenStackSubnet)openStackClient.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(openStackPort.getPortFixedIps().stream().map(n->(((NeutronIP)n).getSubnetId())).collect(Collectors.toList()).get(0));

               // this.openStackClient.getClient().networking().port().update(openStackPort.osPort.toBuilder().state(State.DOWN).build());
                RouterInterface deleteInterface = this.openStackClient.getClient().networking().router().detachInterface(openStackRouter.getId(),null,openStackPort.getId());

                //actionResponse = this.openStackClient.getClient().networking().port().delete(openStackNetworkElement.getId());

            }else if (openStackNetworkElement instanceof OpenStackServer) {

                actionResponse = this.openStackClient.getClient().compute().servers().delete(openStackNetworkElement.getId());

            }else if (openStackNetworkElement instanceof OpenStackFloatingIp) {

                actionResponse= this.openStackClient.getClient().compute().floatingIps().deallocateIP(openStackNetworkElement.getId());

            }else if (openStackNetworkElement instanceof OpenStackImageV2) {

                actionResponse = this.openStackClient.getClient().imagesV2().delete(openStackNetworkElement.getId());

            }


            if (!actionResponse.isSuccess()) {
                OpenStackUtils.openStackLogDialog(actionResponse.getFault());
            }

        }catch (Exception ex){

            OpenStackUtils.openStackLogDialog(ex.getMessage());
            ex.printStackTrace();
        }
    }


}
