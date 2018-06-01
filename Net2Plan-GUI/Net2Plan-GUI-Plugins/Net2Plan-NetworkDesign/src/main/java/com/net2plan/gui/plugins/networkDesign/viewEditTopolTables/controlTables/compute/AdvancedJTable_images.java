package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackImage;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import org.openstack4j.model.compute.Image;

import java.util.*;

public class AdvancedJTable_images extends AdvancedJTable_networkElement<OpenStackImage>
{
    public AdvancedJTable_images(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.IMAGES , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackImage>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackImage>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackImage>(this, String.class, null, "ID", "Image ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, String.class, null, "Name", "Image name", null, n -> n.getImageName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, Image.Status.class, null, "Status", "Image status", null, n -> n.getImageStatus(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, Long.class, null, "Size", "Image size",
                null, n -> n.getImageSize(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, Integer.class, null, "Progress", "Image progress",
                null, n -> n.getImageProgress(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, Date.class, null, "Created", "Date created image",
                null, n -> n.getImageCreated(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, Integer.class, null, "Updated", "Date Updated image",
                null, n -> n.getImageUpdated(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, Map.class, null, "Metadata", "Image Metadata",
                null, n -> n.getImageMetaData(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, Integer.class, null, "MinDisk", "Image MinDisk",
                null, n -> n.getImageMinDisk(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, Integer.class, null, "MinRam", " Image MinRam",
                null, n -> n.getImageMinRam(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, Boolean.class, null, "Snapshot", "Snapshot enable",
                null, n -> n.isImageSnapshot(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImage>(this, List.class, null, "Links", "Image links",
                null, n -> n.getImageLinks(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Change the images's description", e -> getSelectedElements().forEach(n -> {


        }), (a, b) -> b ==1, null));

        return res;

    }



}