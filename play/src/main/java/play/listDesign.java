package play;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultEditorKit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

public class listDesign {
	
	private static String originVariables[];
	private static int counter = 0;
	private static String inputArray[][] = new String[100][3];


	private static LinkedHashSet<String> originHash = new LinkedHashSet<String>();
	private static LinkedHashSet<String> endHash = new LinkedHashSet<String>();
	
	private static HashMap<String, LinkedHashSet<String>> inputHash = new HashMap<String, LinkedHashSet<String>>();
	
	private static DefaultListModel originModel = new DefaultListModel();
	private static DefaultListModel endModel = new DefaultListModel();
	
	private static JLabel selectRelationship;

	
	public static void main(String[] args) {
		

		

		final JFrame frame = new JFrame();
		
		      frame.setTitle("JTextfieldToJList Test");
		      
		      
		      final JPanel parentPanel = new JPanel();
		      parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
		      final JPanel tablePanel = new JPanel();
		      JPanel listPanel = new JPanel();
		      //tabbedPane_2.addTab("New tab", null, listPanel, null);
		      listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
		      
		      String column[]={"Origin Node","Relationship","End Node"};         
			  JTable jt=new JTable(inputArray,column); 
			  JScrollPane tableScroll = new JScrollPane(jt);
			  
			  tablePanel.setLayout(new BorderLayout());
			  tablePanel.add(tableScroll, BorderLayout.CENTER);

		      JPanel originNodeListPanel = new JPanel();
		      originNodeListPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
		      listPanel.add(originNodeListPanel);
		      originNodeListPanel.setLayout(new BorderLayout(0, 0));

		      final JList originNode = new JList(originModel);
		      
		      originNode.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		      final JTextField originInput = new JTextField("Input origin node");
		    
		      originInput.addMouseListener(new MouseAdapter() {
		    	  public void mouseClicked(MouseEvent e) {
		    		  originInput.setText("");
		    	  }
		      });
		      
		      originInput.addActionListener(new ActionListener() {
		    	  public void actionPerformed(ActionEvent event) {
		    		  originModel.removeAllElements();
		    		   
		    		  LinkedHashSet<String> newHashSet = new LinkedHashSet<String>();
		    		  inputHash.put(originInput.getText(), newHashSet);
		    		  
		    		  for (String key : inputHash.keySet()) {
		    			  originModel.addElement(key);
		    		  }
		    		
		    		  originInput.setText("");

		    	  }
		      });
		      
		
			     
			  JScrollPane originScroll = new JScrollPane(originNode);
		      originNodeListPanel.add(originInput, BorderLayout.NORTH);
		      originNodeListPanel.add(originScroll, BorderLayout.CENTER);
		      
		      JPanel instructionsPanel = new JPanel();
		      instructionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		      JLabel instructions = new JLabel("<html><br>*Type origin node and hit ENTER to submit<br>**Select node and hit BACKSPACE to delete</html>");
		      instructionsPanel.add(instructions);

		      JPanel buttonPanel = new JPanel();
		      buttonPanel.setBorder(BorderFactory.createTitledBorder("Select"));
		      listPanel.add(buttonPanel);
		      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		      
		      ButtonGroup relationship = new ButtonGroup();

		  	  final AbstractButton direct = new JRadioButton("+");
			  direct.setVerticalTextPosition(direct.TOP);
			  direct.setHorizontalTextPosition(direct.CENTER);
			  
			  final AbstractButton indirect = new JRadioButton("-");
			  indirect.setVerticalTextPosition(indirect.TOP);
			  indirect.setHorizontalTextPosition(indirect.CENTER);
			  
			  relationship.add(direct);
			  relationship.add(indirect);
			  buttonPanel.add(direct);
			  buttonPanel.add(indirect);
		 

		      final JPanel endNodeListPanel = new JPanel();
		      endNodeListPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
		      final JTextField endInput = new JTextField("Input end node");
		      
		      endInput.addMouseListener(new MouseAdapter() {
		    	  public void mouseClicked(MouseEvent e) {
		    		  endInput.setText("");
		    	  }
		      });
		      
		      endInput.setEnabled(false);

		      
		      originNode.addListSelectionListener (new ListSelectionListener() {
		    	  public void valueChanged(ListSelectionEvent e) {
		    		  
		    		  endInput.setEnabled(true);
		    		  
		    		  if (originNode.getSelectedIndex() >= 0) {
		    			  
			    		  if (originNode.getSelectedValue() == null) {
			    		
			    			  originNode.setSelectedValue(inputHash.keySet().iterator().next(), true);
			    			  
			    		  }
			    		  
			    		  endModel.removeAllElements();
			    		  
					    	
			    		  for (String end: inputHash.get(originNode.getSelectedValue())) {
			    			  endModel.addElement(end);
			    		  } 	
		    		 
		    		  }
		    		  
		    		  else if (originNode.getSelectedIndex() == -1) {
		    			  endModel.removeAllElements();
		    		  }
		    		  
		    	
		    	  }
		      }); 
		      
		      
		      
		      InputMap deleteOrigin = originNode.getInputMap();
			     deleteOrigin.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_BACK_SPACE), "delete origin");
			     ActionMap ap = originNode.getActionMap();
			     ap.put("delete origin", new AbstractAction() {
			    	 public void actionPerformed (ActionEvent e) {
			    		 
				    		
			    		inputHash.remove(originNode.getSelectedValue());
			    	
			    		originModel.removeElement(originNode.getSelectedValue());
			    		
			    		for (int row =0; row < inputArray.length; row++) {
			    			if (inputArray[row][0].equals(originNode.getSelectedValue())) {
			    				
			    			}
			    		}
			    	
			    	 }
			     });
			 
			     
		      endInput.addActionListener(new ActionListener() {
		    	  public void actionPerformed(ActionEvent event) {
		    		  
		    		  
		    		  if (direct.isSelected() == true) {
		    			  

		    			  if (selectRelationship != null) {
			    			  System.out.println("im direct");

		    				  endNodeListPanel.remove(selectRelationship);
		    				  endModel.removeAllElements();
			    		  	  
				    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText());
				    		  
				    		  
				    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
				    			  endModel.addElement(end);
				    		  }
				    		  
				    		  inputArray[counter][0] = (String) originNode.getSelectedValue();
				    		  inputArray[counter][1] = "+";
				    		  inputArray[counter][2] = endInput.getText();
				    		  counter++;
				    		  
				    		  parentPanel.revalidate();
				    		  
				    		  endInput.setText("");
		    				  endNodeListPanel.revalidate();
		    				  frame.repaint();
		    			  }
		    			  
		    			  else {
		    				  endModel.removeAllElements();
			    		  	  
				    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText());
				    		  
				    		  
				    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
				    			  endModel.addElement(end);
				    		  }
				    		  
				    		  inputArray[counter][0] = (String) originNode.getSelectedValue();
				    		  inputArray[counter][1] = "+";
				    		  inputArray[counter][2] = endInput.getText();
				    		  counter++;
				    		  
				    		  parentPanel.revalidate();
				    		  frame.repaint();
				    		  
				    		  endInput.setText("");
		    			  }
		    		  
		    		  }
		    		  
		    		  else if (indirect.isSelected() == true) {
		    			  
		    			  System.out.println("im indirect");
		    			  if (selectRelationship != null) {
		    				  endNodeListPanel.remove(selectRelationship);
		    				  endNodeListPanel.revalidate();
		    				  
		    				  endModel.removeAllElements();
			    		  	  
				    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText());
				    		  
				    		  
				    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
				    			  endModel.addElement(end);
				    		  }
				    		  
				    		  inputArray[counter][0] = (String) originNode.getSelectedValue();
				    		  inputArray[counter][1] = "-";
				    		  inputArray[counter][2] = endInput.getText();
				    		  counter++;
				    		  
				    		  parentPanel.revalidate();
				    		  frame.repaint();
				    		 
				    		  endInput.setText("");

		    			  }
		    			  
		    			  else {
			    		  endModel.removeAllElements();
			    		  	  
			    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText());
			    		  
			    		  
			    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
			    			  endModel.addElement(end);
			    		  }
			    		  
			    		  inputArray[counter][0] = (String) originNode.getSelectedValue();
			    		  inputArray[counter][1] = "-";
			    		  inputArray[counter][2] = endInput.getText();
			    		  counter++;
			    		  
			    		  parentPanel.revalidate();
			    		  frame.repaint();
			    		 
			    		  endInput.setText("");
		    			  }
		    			  
		    		  }
		    		  
		    		  else {
		    			  
		    			  selectRelationship = new JLabel("Select a relationship");
		    			  selectRelationship.setForeground(new Color(255, 0, 0));
		    			  endNodeListPanel.add(selectRelationship, BorderLayout.SOUTH);
		    			  endNodeListPanel.revalidate();
		    			  
		    		  }

		    	  }
		      });
			  
		
		      
		      listPanel.add(endNodeListPanel);
		      endNodeListPanel.setLayout(new BorderLayout(0, 0));
		      endNodeListPanel.add(endInput, BorderLayout.NORTH);
		      final JList endNode = new JList(endModel);
		      endNode.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		      
		      JScrollPane endScroll = new JScrollPane(endNode);
		      endNodeListPanel.add(endScroll, BorderLayout.CENTER);
		      
		      
		      InputMap deleteEnd = endNode.getInputMap();
			     deleteEnd.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_BACK_SPACE), "delete end");
			     ActionMap end = endNode.getActionMap();
			     end.put("delete end", new AbstractAction() {
			    	 public void actionPerformed (ActionEvent e) {
			    		 endHash.remove(endNode.getSelectedValue());
			    		 endModel.removeElement(endNode.getSelectedValue());
			    	 }
			     }); 
		      
			     /*
		      Action editOrigin = new EditListAction(inputHash, outputTable);
		      Action editEnd = new EditEndNode(inputHash, originNode, outputTable);
		      ListAction addOrigin = new ListAction(originNode, editOrigin);
		      ListAction addEnd = new ListAction(endNode, editEnd); */
		      
		      
			  JPanel borderPanel = new JPanel();
			  borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.Y_AXIS));
			  borderPanel.add(listPanel);
			  borderPanel.add(instructionsPanel);
		      borderPanel.setBorder(BorderFactory.createTitledBorder("Input Variables"));
		      
		      tablePanel.setBorder(BorderFactory.createTitledBorder("Link Table"));

		      parentPanel.add(borderPanel);
		      parentPanel.add(tablePanel);
		      
		      frame.add(parentPanel);
		      frame.setSize(375, 250);
		      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		      frame.setLocationRelativeTo(null);
		      frame.setVisible(true);
		
	
	}
}
