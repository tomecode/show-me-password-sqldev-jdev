package com.tomecode.jdev.extension12g.showmepassword;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


import java.awt.Color;

import java.util.regex.Pattern;

import javax.swing.ListSelectionModel;

/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */
public final class PasswordWindow extends JFrame {

        private static final long serialVersionUID = -1298316341843197099L;

        private final String[] cols = new String[] { "Connection Type","Connection Name","User Name", "Saved Password" };
        private final JTable table;
        private final TableRowSorter<TableModel> sorter;


        public PasswordWindow(final List<Login> dbLogins) {
                setAlwaysOnTop(false);
                setTitle("Show Me Passwords JDeveloper! :) version: 1.0");
                setBounds(100, 100, 600, 378);
                setMinimumSize(new Dimension(350, 378));
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panelTable = new JPanel(new BorderLayout());
                panelTable.setBorder(new TitledBorder("Connections, SIDs, Users and Saved passwords..."));
                getContentPane().add(panelTable, BorderLayout.CENTER);

                String[][] rows = new String[0][0];
                if (!dbLogins.isEmpty()) {
                        rows = new String[dbLogins.size()][1];
                        for (int i = 0; i <= rows.length - 1; i++) {
                                rows[i] = dbLogins.get(i).toArray();
                        }
                }

                TableModel model = new DefaultTableModel(rows, cols) {

                        private static final long serialVersionUID = 673146279179020293L;

                        public final Class<?> getColumnClass(int column) {
                                Class<?> returnValue;
                                if ((column >= 0) && (column < getColumnCount())) {
                                        returnValue = getValueAt(0, column).getClass();
                                } else {
                                        returnValue = Object.class;
                                }
                                return returnValue;
                        }

                        public final boolean isCellEditable(int p1, int p2) {
                                return false;
                        }
                };

                table = new JTable(model);

    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setShowGrid(true);
    table.setShowHorizontalLines(true);


                sorter = new TableRowSorter<TableModel>(model);
                table.setRowSorter(sorter);

                panelTable.add(new JScrollPane(table), BorderLayout.CENTER);

                JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                getContentPane().add(panelButtons, BorderLayout.SOUTH);

                JButton btnExit = new JButton("Close");
                panelButtons.add(btnExit);
                btnExit.addActionListener(new ActionListener() {
                        public final void actionPerformed(ActionEvent e) {
                                setVisible(false);
                                dispose();
                        }
                });
        }

        public static final void showMe(final List<Login> dbLogins) throws Exception {
                new PasswordWindow(dbLogins).setVisible(true);
        }


}

