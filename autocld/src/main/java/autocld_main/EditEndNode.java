package autocld_main;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;

/*
 *	A simple popup editor for a JList that allows you to change
 *  the value in the selected row.
 *
 *  The default implementation has a few limitations:
 *
 *  a) the JList must be using the DefaultListModel
 *  b) the data in the model is replaced with a String object
 *
 *  If you which to use a different model or different data then you must
 *  extend this class and:
 *
 *  a) invoke the setModelClass(...) method to specify the ListModel you need
 *  b) override the applyValueToModel(...) method to update the model
 */
public class EditEndNode extends AbstractAction
{
	public JList list;

	public JPopupMenu editPopup;
	public JTextField editTextField;
	public Class<?> modelClass;
	public final HashMap<String, LinkedHashSet<String>> hash;
	public final JList originSelect;
	public final JTable table;
	public final JRadioButton direct;
	public final JRadioButton indirect;
	private BooleanChangeTest fired;


	
	public void updateTable(JTable table) {
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		  model.setRowCount(0);
		  
		  for (String origin : hash.keySet()) {
			  for (String end : hash.get(origin)) {
				  
				if (end.indexOf('+') != -1) {
				model.addRow(new Object[] {origin, "+", end.substring(0, end.length()-4)});
			  }
				else if (end.indexOf('-') != -1) {
					model.addRow(new Object[] {origin, "-", end.substring(0, end.length()-4)});
				  }
				
		  }
		  }
	}
	

	public boolean eventInitiated() {
		return true;
	}

	public EditEndNode(final HashMap<String, LinkedHashSet<String>> hash, final JList originSelect, final JTable table, final JRadioButton direct, final JRadioButton indirect, BooleanChangeTest fired)
	{
		
		setModelClass(DefaultListModel.class);
		this.hash = hash;
		this.originSelect = originSelect;
		this.table = table;
		this.direct = direct;
		this.indirect = indirect;
		this.fired = fired;
	}

	public void setModelClass(Class modelClass)
	{
		this.modelClass = modelClass;
	}

	public void applyValueToModel(String value, ListModel model, int row)
	{
		DefaultListModel dlm = (DefaultListModel)model;
		dlm.set(row, value);
	}

	/*
	 *	Display the popup editor when requested
	 */
	public void actionPerformed(ActionEvent e)
	{
		list = (JList)e.getSource();
		ListModel model = list.getModel();

		if (! modelClass.isAssignableFrom(model.getClass())) return;

		//  Do a lazy creation of the popup editor

		if (editPopup == null)
    		createEditPopup();

		//  Position the popup editor over top of the selected row

		int row = list.getSelectedIndex();
		Rectangle r = list.getCellBounds(row, row);

		editPopup.setPreferredSize(new Dimension(r.width, r.height));
		editPopup.show(list, r.x, r.y);

		//  Prepare the text field for editing

		String selectedValue = list.getSelectedValue().toString();
		editTextField.setText( selectedValue.substring(0, selectedValue.length()-4));
		editTextField.selectAll();
		editTextField.requestFocusInWindow();
	}

	/*
	 *  Create the popup editor
	 */
	public boolean createEditPopup()
	{
		//  Use a text field as the editor

		editTextField = new JTextField();
		Border border = UIManager.getBorder("List.focusCellHighlightBorder");
		editTextField.setBorder( border );

		//  Add an Action to the text field to save the new value to the model

		editTextField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String value = editTextField.getText();
				ListModel model = list.getModel();
				int row = list.getSelectedIndex();
				
	    		hash.get(originSelect.getSelectedValue()).remove(list.getSelectedValue());
	   
	    		if (direct.isSelected() == true) {
		    		hash.get(originSelect.getSelectedValue()).add(value + " (+)");

	    		}
	    		else if (indirect.isSelected() == true) {
		    		hash.get(originSelect.getSelectedValue()).add(value + " (-)");

	    		}
				((DefaultListModel) model).removeAllElements();

	    		
	    		  for (String end : hash.get(originSelect.getSelectedValue())) {
	    			  ((DefaultListModel) model).addElement(end);
	    		  }
				

	    		updateTable(table);
	    		 fired.setFlag(true);
	    		 
				//applyValueToModel(value, model, row);
				editPopup.setVisible(false);
			}
		});

		//  Add the editor to the popup

	    editPopup = new JPopupMenu();
		editPopup.setBorder( new EmptyBorder(0, 0, 0, 0) );
    	editPopup.add(editTextField);
    	
    	return true;
	}
}