package autocld_main;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

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
public class EditListAction extends AbstractAction
{
	private JList list;

	private JPopupMenu editPopup;
	private JTextField editTextField;
	private Class<?> modelClass;
	private final HashMap<String, LinkedHashSet<String>> hash;
	private final JTable table;
	private final JList endList;
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

	public EditListAction(final HashMap<String, LinkedHashSet<String>> hash, final JTable table, final BooleanChangeTest fired, final JList endList)
	{
		setModelClass(DefaultListModel.class);
		this.hash = hash;
		this.table = table;
		this.fired = fired;
		this.endList = endList;
	}

	protected void setModelClass(Class modelClass)
	{
		this.modelClass = modelClass;
	}

	protected void applyValueToModel(String value, ListModel model, int row)
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

		editTextField.setText( list.getSelectedValue().toString() );
		editTextField.selectAll();
		editTextField.requestFocusInWindow();
	}

	/*
	 *  Create the popup editor
	 */
	private void createEditPopup()
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
				DefaultListModel editEnd = (DefaultListModel) endList.getModel(); 

				
				hash.put(value, hash.get(list.getSelectedValue()));
				hash.remove(list.getSelectedValue());
				
				for (String key : hash.keySet()) {
					for (String endnode : hash.get(key)) {
						if (endnode.contains((CharSequence) list.getSelectedValue()) == true) {
							hash.get(key).remove(endnode);
							//String listselect = (String) list.getSelectedValue();
							hash.get(key).add(value + endnode.substring(endnode.length()-4, endnode.length()));
						}
					}
				}
				
				((DefaultListModel) model).removeAllElements();
				
				 for (String key : hash.keySet()) {
	    			  ((DefaultListModel) model).addElement(key);
	    		  }
				 
				 editEnd.removeAllElements();
				 for (String endnode : hash.get(value)) {
					 editEnd.addElement(endnode);
				 }

				 updateTable(table);
				 fired.setFlag(true);
				 System.out.println("i fired");
				 System.out.println(hash);

				editPopup.setVisible(false);
			}
		});

		//  Add the editor to the popup

	    editPopup = new JPopupMenu();
		editPopup.setBorder( new EmptyBorder(0, 0, 0, 0) );
    	editPopup.add(editTextField);
	}
}