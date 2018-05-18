package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackGeneralInformation;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_informationProject extends AdvancedJTable_networkElement<OpenStackGeneralInformation>
{
public AdvancedJTable_informationProject(GUINetworkDesign callback)
        {
        super(callback, ViewEditTopologyTablesPane.AJTableType.INFORMATION , true);
        }

@Override
public List<AjtColumnInfo<OpenStackGeneralInformation>> getNonBasicUserDefinedColumnsVisibleOrNot()
        {

final List<AjtColumnInfo<OpenStackGeneralInformation>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackGeneralInformation>(this, String.class, null, "Type", "OpenStack", null, n -> n.getType(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGeneralInformation>(this, String.class, null, "Number", "Number of", null, n -> n.getNumber(), AGTYPE.NOAGGREGATION, null, null));
        return res;
        }


@Override
public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
        {
final List<AjtRcMenu> res = new ArrayList<>();

        return res;
        }


        }
