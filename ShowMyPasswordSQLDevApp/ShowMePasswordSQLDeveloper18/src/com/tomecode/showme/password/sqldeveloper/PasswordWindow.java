package com.tomecode.showme.password.sqldeveloper;

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
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */
public class PasswordWindow extends JFrame {

	private static final long serialVersionUID = -2286919092357050433L;

	private final String[] cols = new String[] { "Connection Name", "Host", "SID", "Service Name", "User Name", "Saved Password" };
	private final JTable table;
	private final TableRowSorter<TableModel> sorter;
	private final JTextField txtFilter;

	public PasswordWindow(final List<DBLogin> dbLogins) {
		setAlwaysOnTop(false);
		setTitle("Show Me Passwords SQL Developer 4! :) version: 2.0");
		setBounds(100, 100, 600, 378);
		setMinimumSize(new Dimension(350, 378));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		txtFilter = new JTextField();
		txtFilter.addKeyListener(new KeyAdapter() {
			public final void keyReleased(KeyEvent paramKeyEvent) {
				if (txtFilter.getText().trim().length() == 0) {
					sorter.setRowFilter(null);
				} else {
					try {
						sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txtFilter.getText(), 0, 1, 2, 3, 4, 5));
						// sorter.setRowFilter(RowFilter.regexFilter(txtFilter.getText(),
						// Pattern.CASE_INSENSITIVE));
					} catch (Exception e) {
						//
					}
				}
			}
		});
		JPanel panelFilter = new JPanel(new BorderLayout());
		panelFilter.setBorder(new TitledBorder("Filter by Connection Name:"));
		panelFilter.add(txtFilter, BorderLayout.CENTER);
		getContentPane().add(panelFilter, BorderLayout.NORTH);

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

				if (this.getRowCount() > 0) {
					Class<?> returnValue;
					if ((column >= 0) && (column < getColumnCount())) {
						returnValue = getValueAt(0, column).getClass();
					} else {
						returnValue = Object.class;
					}
					return returnValue;
				}

				return String.class;
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
		// by default sort on connection_name asc
		List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
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

	public static final void showMe(final List<DBLogin> dbLogins) {
		new PasswordWindow(dbLogins).setVisible(true);
	}

}
