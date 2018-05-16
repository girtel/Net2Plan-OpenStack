package com.net2plan.gui.plugins.utils;


import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.net2plan.gui.utils.FileChooserConfirmOverwrite;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.internal.Constants.DialogType;
import com.net2plan.internal.Constants.IOFeature;
import com.net2plan.internal.plugins.IOFilter;
import com.net2plan.internal.plugins.PluginSystem;
import com.net2plan.io.IOBrite;
import com.net2plan.io.IOMatPlanWDM_design;
import com.net2plan.io.IOMatPlanWDM_trafficMatrix;
import com.net2plan.io.IONet2Plan;
import com.net2plan.io.IOSNDLibNative;
import com.net2plan.io.IOTrafficMatrix2DFile;
import com.net2plan.io.IOVisum;

/**
 * Extends {@link FileChooserConfirmOverwrite FileChooserConfirmOverwrite}
 * to support custom IO filters for Net2Plan.
 *
 * @author Pablo Pavon-Marino, Jose-Luis Izquierdo-Zaragoza
 * @since 0.2.2
 */
public class FileChooserNetworkDesign extends FileChooserConfirmOverwrite
{
    private boolean finishConfiguration;
    private Set<IOFilter> loadFilters;
    private Set<IOFilter> saveFilters;
    private IOFilter lastLoadFilter, lastSaveFilter;

    static
    {
        PluginSystem.addPlugin(IOFilter.class, IONet2Plan.class);
        PluginSystem.addPlugin(IOFilter.class, IOSNDLibNative.class);
        PluginSystem.addPlugin(IOFilter.class, IOBrite.class);
        PluginSystem.addPlugin(IOFilter.class, IOVisum.class);
        PluginSystem.addPlugin(IOFilter.class, IOMatPlanWDM_design.class);
        PluginSystem.addPlugin(IOFilter.class, IOMatPlanWDM_trafficMatrix.class);
        PluginSystem.addPlugin(IOFilter.class, IOTrafficMatrix2DFile.class);
    }

    /**
     * Default constructor.
     *
     * @param currentDirectory Current directory
     * @param type             Dialog type
     * @since 0.3.0
     */
    public FileChooserNetworkDesign(File currentDirectory, DialogType type)
    {
        super(currentDirectory);

        loadFilters = new LinkedHashSet<IOFilter>();
        saveFilters = new LinkedHashSet<IOFilter>();

        switch (type)
        {
            case NETWORK_DESIGN:
                loadFilters.addAll(IOFilter.getIOFiltersByFeatures(EnumSet.of(IOFeature.LOAD_DESIGN)));
                saveFilters.addAll(IOFilter.getIOFiltersByFeatures(EnumSet.of(IOFeature.SAVE_DESIGN)));
                break;

           /* case COMARCH_DB:
                loadFilters.addAll(IOFilter.getIOFiltersByFeatures(EnumSet.of(IOFeature.LOAD_DEMANDS)));
                saveFilters.addAll(IOFilter.getIOFiltersByFeatures(EnumSet.of(IOFeature.SAVE_DEMANDS)));
                break;
                */
        }

        lastLoadFilter = null;
        lastSaveFilter = null;

        finishConfiguration = false;

        setAcceptAllFileFilterUsed(false);
        setFileSelectionMode(JFileChooser.FILES_ONLY);

        finishConfiguration = true;
    }

    @Override
    public void addChoosableFileFilter(FileFilter filter)
    {
        if (finishConfiguration)
        {
            throw new UnsupportedOperationException("Unsupported operation");
        } else
        {
            super.addChoosableFileFilter(filter);
        }
    }

    @Override
    public void setAcceptAllFileFilterUsed(boolean b)
    {
        if (finishConfiguration)
        {
            throw new UnsupportedOperationException("Unsupported operation");
        } else
        {
            super.setAcceptAllFileFilterUsed(b);
        }
    }

    @Override
    public void setFileSelectionMode(int mode)
    {
        if (finishConfiguration) throw new UnsupportedOperationException("Unsupported operation");
        else super.setFileSelectionMode(mode);
    }

    @Override
    public int showOpenDialog(Component parent) throws HeadlessException
    {
        return showDialog(parent, true);
    }

    @Override
    public int showSaveDialog(Component parent) throws HeadlessException
    {
        return showDialog(parent, false);
    }

    private IOFilter getCurrentIOFilter()
    {
        FileFilter currentFilter = getFileFilter();
        if (!(currentFilter instanceof IOFilter))
            throw new RuntimeException("Bad filter");

        return (IOFilter) currentFilter;
    }

    /** Load the Comarch database
     * @return
     */
    public NetPlan readComarchDb()
    {
    	/*final NetPlan netPlanToImport = new NetPlanCreator(getSelectedFile()).getNetPlan();
		final MultilayerAlgorithmImporter importer = new MultilayerAlgorithmImporter(netPlanToImport);
        return importer.getImportedNetPlan();*/
        return null;
    }

    /**
     * Loads a network design from the selected file.
     *
     * @return Network design
     * @since 0.2.2
     */
    public NetPlan readNetPlan()
    {
        IOFilter importer = getCurrentIOFilter();
        NetPlan netPlan = importer.readFromFile(getSelectedFile());
        return netPlan;
    }

    /**
     * Loads a network design from the selected file.
     *
     * @return Network design
     * @since 0.2.2
     */
    public Collection<NetPlan> readNetPlans()
    {
        List<NetPlan> netPlans = new LinkedList<NetPlan>();
        IOFilter importer = getCurrentIOFilter();
        File[] files = getSelectedFiles();
        for (File file : files) netPlans.add(importer.readFromFile(file));

        return netPlans;
    }

//    /**
//     * Saves the demands from the given network design to the selected file.
//     *
//     * @param mtn Network design
//     * @since 0.3.0
//     */
//    public void saveDemands(Mtn mtn)
//    {
//        IOFilter exporter = getCurrentIOFilter();
//        File file = getSelectedFile();
//        exporter.saveDemandSetToFile(mtn, file);
//    }

//    /**
//     * Saves the demands from the given network designs to the selected file.
//     *
//     * @param netPlans Collection of network designs to be saved
//     * @since 0.3.0
//     */
//    public void saveDemands(Collection<NetPlan> netPlans)
//    {
//        if (netPlans.size() == 1)
//        {
//            saveDemands(netPlans.iterator().next());
//            return;
//        }
//
//        IOFilter exporter = getCurrentIOFilter();
//        File file = getSelectedFile();
//
//        String path = SystemUtils.getPath(file);
//        String pathSeparator = SystemUtils.getDirectorySeparator();
//        String fileName = SystemUtils.getFilename(file);
//        String extension = SystemUtils.getExtension(file);
//
//        int i = 0;
//        Iterator<NetPlan> it = netPlans.iterator();
//        while (it.hasNext())
//        {
//            NetPlan netPlan = it.next();
//
//            if (extension.isEmpty())
//                exporter.saveDemandSetToFile(netPlan, new File(path + pathSeparator + fileName + "_tm" + i));
//            else
//                exporter.saveDemandSetToFile(netPlan, new File(path + pathSeparator + fileName + "_tm" + i + "." + extension));
//
//            i++;
//        }
//    }

    /**
     * Saves the given network design to the selected file.
     *
     * @param netPlan Network design
     * @since 0.2.2
     */
    public void saveNetPlan(NetPlan netPlan)
    {
        IOFilter exporter = getCurrentIOFilter();
        File file = getSelectedFile();
        exporter.saveToFile(netPlan, file);
    }

    private int showDialog(Component parent, boolean isOpenDialog) throws HeadlessException
    {
        finishConfiguration = false;
        for (FileFilter existingFilter : super.getChoosableFileFilters())
            super.removeChoosableFileFilter(existingFilter);

        for (IOFilter ioFilter : isOpenDialog ? loadFilters : saveFilters)
        {
            addChoosableFileFilter(ioFilter);

            if ((isOpenDialog && ioFilter.equals(lastLoadFilter)) || (!isOpenDialog && ioFilter.equals(lastSaveFilter)))
                super.setFileFilter(ioFilter);
        }

        finishConfiguration = true;

        int out = isOpenDialog ? super.showOpenDialog(parent) : super.showSaveDialog(parent);
        if (out == JFileChooser.APPROVE_OPTION)
        {
            IOFilter ioFilter = getCurrentIOFilter();

            if (loadFilters.contains(ioFilter)) lastLoadFilter = ioFilter;
            if (saveFilters.contains(ioFilter)) lastSaveFilter = ioFilter;
        }

        return out;
    }
}
