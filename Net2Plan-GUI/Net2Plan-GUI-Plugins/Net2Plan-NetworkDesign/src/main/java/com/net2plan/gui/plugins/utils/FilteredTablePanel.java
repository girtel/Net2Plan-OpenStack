package com.net2plan.gui.plugins.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITableRowFilter;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane.AJTableType;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;

public class FilteredTablePanel extends JPanel
{
    private final GUINetworkDesign callback;

    private final JPanel headerPanel;
    private JLabel numEntriesLabel;
    private JButton resetTableRowFilters;

    private OpenStackClient openStackClient;
    private final JTable table;

    public FilteredTablePanel(GUINetworkDesign callback, JScrollPane scrollPane, OpenStackClient openStackClient)
    {
        if (callback == null) throw new IllegalArgumentException();
        if (scrollPane == null) throw new IllegalArgumentException();

        this.callback = callback;
        this.openStackClient =openStackClient;

        try
        {
            final JTable table = (JTable) scrollPane.getViewport().getView();

            if (table == null) throw new ClassCastException();

            this.table = table;
            this.headerPanel = this.buildHeader();

            this.setLayout(new BorderLayout());

            this.add(headerPanel, BorderLayout.NORTH);
            this.add(scrollPane, BorderLayout.CENTER);
        } catch (ClassCastException ex)
        {
            throw new IllegalArgumentException("ScrollPane component can only contain a JTable");
        }
    }

    /*public FilteredTablePanel(GUINetworkDesign callback, JTable table)
    {
        this(callback, new JScrollPane(table), new);
    }*/

    private JPanel buildHeader()
    {
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BorderLayout());
        labelsPanel.setBackground(new Color(0, 94, 184));

        this.numEntriesLabel = new JLabel("Number of entries: " + table.getRowCount());
        this.numEntriesLabel.setForeground(Color.WHITE);
        this.numEntriesLabel.setFont(new Font(numEntriesLabel.getFont().getName(), Font.BOLD, numEntriesLabel.getFont().getSize()));
        labelsPanel.add(numEntriesLabel, BorderLayout.CENTER);

        final JPanel buttonsPanel = new JPanel();
        this.resetTableRowFilters = new JButton("Reset VFs");
        this.resetTableRowFilters.addActionListener(e ->
        {
            callback.updateVisualizationJustTables();
            callback.resetPickedStateAndUpdateView();

        });
        buttonsPanel.add(resetTableRowFilters, BorderLayout.EAST);

        buttonsPanel.setOpaque(false);

        labelsPanel.add(buttonsPanel, BorderLayout.EAST);
        labelsPanel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        return labelsPanel;
    }

    public void updateHeader()
    {
        final int numEntries = table.getModel().getRowCount() - 1; // last columns is for the aggregation
        if (callback.getVisualizationState().getTableRowFilter() != null && table instanceof AdvancedJTable_networkElement)
        {
            final AJTableType ajType = ((AdvancedJTable_networkElement) table).getAjType();
            final int numUnfilteredElements = ITableRowFilter.getAllElements(openStackClient, ajType).size();
            numEntriesLabel.setText("Number of entries: " + numEntries + " / " + numUnfilteredElements + ", FILTERED VIEW: " + callback.getVisualizationState().getTableRowFilter().getDescription());
        }
        else
            numEntriesLabel.setText("Number of entries: " + numEntries);
    }

    public void updateTableSelection(String type,Object value){
        if(type.equals("String")){
            String openStackNetworkElementId = ((String) value);
            if(openStackNetworkElementId!=null){
                for(int i = 0; i< table.getModel().getRowCount();i++) {
                    System.out.println(openStackNetworkElementId + "= " + table.getModel().getValueAt(i, 0));
                    if((openStackNetworkElementId).equals(table.getModel().getValueAt(i, 0))){
                        table.getSelectionModel().addSelectionInterval(i, i);
                    }
                }
            }
        }
        if(type.equals("ArrayList")) {
            List<String> openStackNetworkElementId = (ArrayList<String>) value;
            if(openStackNetworkElementId!=null){
                for (int i = 0; i < table.getModel().getRowCount(); i++) {
                    if ((openStackNetworkElementId).contains(table.getModel().getValueAt(i, 0))) {
                        table.getSelectionModel().addSelectionInterval(i, i);
                    }
                }
            }
        }
    }
}