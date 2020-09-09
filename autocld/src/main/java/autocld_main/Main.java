package autocld_main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.collections.Transformer;

import com.google.common.base.Function;
import com.google.common.base.Functions;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.GradientVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import edu.uci.ics.jung.visualization.util.Animator;
import edu.uci.ics.jung.visualization.*;


public class Main {

	private JFrame frame;


	private static Graph<String, Integer> g = new DirectedSparseGraph<String, Integer>();
    ObservableGraph<String,Integer> og = new ObservableGraph<String,Integer>(g);

    private VisualizationViewer vv = new VisualizationViewer(new FRLayout(g));
    SatelliteVisualizationViewer satelliteView = new SatelliteVisualizationViewer(vv, new Dimension(100, 40));

	private static String originVariables[];
	private static int counter = 0;
	private static String inputArray[][] = new String[100][3];
    private static String column[]={"Origin Node","Relationship","End Node"}; 

	
	private static HashMap<String, LinkedHashSet<String>> inputHash = new HashMap<String, LinkedHashSet<String>>();
	
	private static DefaultListModel originModel = new DefaultListModel();
	private static DefaultListModel endModel = new DefaultListModel();
	
	private static JLabel selectRelationship;
    private static JPanel graphPanel = new JPanel();
    private static JSplitPane graphInputSplit;
    
    private JTextField originInput;
    private JTable outputTable;
    private JList originNode;
    private JList endNode;
    private JRadioButton direct;
    private JRadioButton indirect;
	public JPopupMenu editPopup;
	public JTextField editTextField;


	
	private int edgeCounter = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test happy = new test();
					happy.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public void updateTable(JTable table) {

		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		  model.setRowCount(0);
		  
		  for (String origin : inputHash.keySet()) {
			  for (String end : inputHash.get(origin)) {
				  
				if (end.indexOf('+') != -1) {
				model.addRow(new Object[] {origin, "+", end.substring(0, end.length()-4)});
			  }
				else if (end.indexOf('-') != -1) {
					model.addRow(new Object[] {origin, "-", end.substring(0, end.length()-4)});
				  }
				
		  }
		  }
	}
	
	public void clearGraph(Graph<String, Integer> g2) {
		
		int vertexCount = g2.getVertexCount();
		int edgeCount = g2.getEdgeCount();
		
		
		for (int count = g2.getEdgeCount()-1; count >= 0; count--) {
			System.out.println("VertexCount: " + g2.getVertexCount());
			System.out.println("Count: " + count);
			//System.out.println("EdgeCount: " + g2.getEdgeCount());
			//System.out.println("Destination: " + g2.getDest(count-1));
			//System.out.println("Source: " + g2.getSource(count-1));

			//g2.removeVertex(g2.getDest(count-1));
			//System.out.println(g2.getVertexCount());
			//g2.removeVertex(g2.getSource(count-1));

			g2.removeVertex(g2.getDest(count));
			g2.removeVertex(g2.getSource(count));
			g2.removeEdge(count);

			if(g2.getVertexCount() == 1) {
				ArrayList<String> graphArray = new ArrayList(g2.getVertices());
				g2.removeVertex(graphArray.get(0));

			}
			

		} 
		

	
	}
	
	static class MyRenderer implements Renderer.Vertex<String, Number> {
	    public void paintVertex(RenderContext<String, Number> rc,
	        Layout<String, Number> layout, String vertex) {
	     GraphicsDecorator graphicsContext = rc.getGraphicsContext();
	  
	      graphicsContext.setPaint(Color.white);
	      graphicsContext.fill(new Rectangle(100,100));
	    }
	  }
	
	public void mapFromInput() {
		
		
		 for (String origin : inputHash.keySet()) {
			  for (String end : inputHash.get(origin)) {
				  
				  g.addEdge(g.getEdgeCount(), origin, end.substring(0, end.length()-4));
				  
			      LabelAsShape<String,Number> vlasr = new LabelAsShape<String,Number>(vv.getRenderContext());
			      vv.getRenderContext().setVertexLabelTransformer(
			        		// this chains together Functions so that the html tags
			        		// are prepended to the toString method output
			        		Functions.<Object,String,String>compose(
			        				new Function<String,String>(){
										public String apply(String input) {
											return "<html><center>"+input;
										}}, new ToStringLabeller()));
			        vv.getRenderContext().setVertexShapeTransformer(vlasr);
			        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.black));


			        vv.getRenderContext().setEdgeDrawPaintTransformer(Functions.<Paint>constant(Color.black));
			        vv.getRenderContext().setEdgeStrokeTransformer(Functions.<Stroke>constant(new BasicStroke(1.5f)));
			        
			        // customize the renderer
			        vv.getRenderer().setVertexRenderer(new GradientVertex<String,Number>(Color.white, Color.white, true));
			        vv.getRenderer().setVertexLabelRenderer(vlasr);


				  
				  /*
				   // vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Integer>(vv.getPickedVertexState(), Color.red, Color.yellow));
			        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Number>(vv.getPickedEdgeState(), Color.black, Color.cyan));
			        //vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<String>(vv.getPickedVertexState(), Color.white, Color.yellow));
			        vv.getRenderContext().setVertexRenderer(MyRenderer);
			      
			        //vv.getRenderer().setVertexRenderer(new GradientVertexRenderer<String,Number>(Color.red, Color.white, true));
			        //vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
			        //vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
			        */
			       

			        satelliteView.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Number>(satelliteView.getPickedEdgeState(), Color.black, Color.cyan));
			        satelliteView.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<String>(satelliteView.getPickedVertexState(), Color.red, Color.yellow));
			        
			      // ScalingControl vv2Scaler = new CrossoverScalingControl();
			       // satelliteView.scaleToLayout(vv2Scaler);
			  }
		 }
			        vv.setBackground(Color.white);
			        final DefaultModalGraphMouse<Integer,Number> graphMouse = new DefaultModalGraphMouse<Integer,Number>();
			        graphMouse.setMode(Mode.PICKING);
			        vv.setGraphMouse(graphMouse);
			        	        
					GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
					graphPanel.setLayout(new BorderLayout());
					graphPanel.removeAll();
					graphPanel.add(gzsp, BorderLayout.CENTER);
					graphPanel.revalidate();
				
			        frame.pack();
			        frame.repaint();
			        frame.setVisible(true);
			  
		  }
	
	
	
	
    public void process(String newNode, String originN) {

    	vv.getRenderContext().getPickedVertexState().clear();
    	vv.getRenderContext().getPickedEdgeState().clear();
        try {

        	 og.addEdge(edgeCounter, originN, newNode);
			  edgeCounter++;
                //add a vertex
        		/*
                g.addVertex(input);
                vv.getRenderContext().getPickedVertexState().pick(input, true);

                // wire it to some edges
                if (v_prev != null) {
                	Integer edge = g.getEdgeCount();
                	vv.getRenderContext().getPickedEdgeState().pick(edge, true);
                    g.addEdge(edge, v_prev, v1);
                    // let's connect to a random vertex, too!
                    int rand = (int) (Math.random() * g.getVertexCount());
                    edge = g.getEdgeCount();
                	vv.getRenderContext().getPickedEdgeState().pick(edge, true);
                   g.addEdge(edge, v1, rand);
                } */

               // v_prev = v1;

                //layout.initialize();
        		Relaxer relaxer = new VisRunner((IterativeContext)vv.getLayout());
        		relaxer.stop();
        		relaxer.prerelax();
        		StaticLayout<String,Integer> staticLayout = new StaticLayout<String,Integer>(g);
				LayoutTransition<String,Integer> lt =
					new LayoutTransition<String,Integer>(vv, vv.getGraphLayout(),
							staticLayout);
				Animator animator = new Animator(lt);
				animator.start();
//				vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
				vv.repaint();

           

        } catch (Exception e) {
            System.out.println(e);

        }
    } 
	

	  private static Class<? extends Layout>[] getCombos()
	    {
	        List<Class<? extends Layout>> layouts = new ArrayList<Class<? extends Layout>>();
	        layouts.add(KKLayout.class);
	        layouts.add(FRLayout.class);
	        layouts.add(CircleLayout.class);
	        layouts.add(SpringLayout.class);
	        layouts.add(SpringLayout2.class);
	        layouts.add(ISOMLayout.class);
	        return layouts.toArray(new Class[0]);
	    }
	  
	  private static final class LayoutChooser implements ActionListener
	    {
	        private final JComboBox<?> jcb;
	        private final VisualizationViewer<Integer,Number> vv;

	        private LayoutChooser(JComboBox<?> jcb, VisualizationViewer<Integer,Number> vv)
	        {
	            super();
	            this.jcb = jcb;
	            this.vv = vv;
	        }

	        @SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0)
	        {
	            Graph<String, Integer> constructorArgs = g;

				Class<? extends Layout<Integer,Number>> layoutC = (Class<? extends Layout<Integer,Number>>) jcb.getSelectedItem();
	            try
	            {
	                Constructor<? extends Layout<Integer, Number>> constructor = layoutC.getConstructor(new Class[] {Graph.class});
	                Object o = constructor.newInstance(constructorArgs);
	                Layout<Integer,Number> l = (Layout<Integer,Number>) o;
	                l.setInitializer(vv.getGraphLayout());
	                l.setSize(vv.getSize());
	                
					LayoutTransition<Integer,Number> lt =
						new LayoutTransition<Integer,Number>(vv, vv.getGraphLayout(), l);
					Animator animator = new Animator(lt);
					animator.start();
					vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
					vv.repaint();
	                
	            }
	            catch (Exception e)
	            {
	                e.printStackTrace();
	            }
	        }
	    }
	  
	public Main() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 720);
		
		JButton compile = new JButton ("Compile");
		JButton addAnother = new JButton ("Add Another");
		
		frame.setLayout(new BorderLayout(8, 6));
		
		//ToolBar Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		topPanel.add(compile);
		topPanel.add(addAnother);
		topPanel.setBorder(BorderFactory.createTitledBorder("Toolbar"));
		
		Class[] combos = getCombos();
        final JComboBox jcb = new JComboBox(combos);
        // use a renderer to shorten the layout name presentation
        jcb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String valueString = value.toString();
                valueString = valueString.substring(valueString.lastIndexOf('.')+1);
                return super.getListCellRendererComponent(list, valueString, index, isSelected,
                        cellHasFocus);
            }
        });
        
        jcb.addActionListener(new LayoutChooser(jcb, vv));
        jcb.setSelectedItem(FRLayout.class);
        topPanel.add(jcb);
		topPanel.add(satelliteView);

		frame.add(topPanel, BorderLayout.NORTH);
		
		

		
		//Left panel with variable list and graph display
		final JPanel leftPanel = new JPanel();							///LeftPanel = the parent panel
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		//leftPanel.setBorder(BorderFactory.createTitledBorder("Input Variables"));
		
		
		//Code for the table and its containing panel
	      final JPanel tablePanel = new JPanel();
	      JPanel listPanel = new JPanel();
	      listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
	      
	      final DefaultTableModel outputModel = new DefaultTableModel();
	      outputModel.addColumn("Origin Node");
	      outputModel.addColumn("Relationship");
	      outputModel.addColumn("End Node");
		  outputTable = new JTable();
		  outputTable.setModel(outputModel);
		  outputTable.setEnabled(false);
		  JScrollPane tableScroll = new JScrollPane(outputTable);
		  
		  tablePanel.setLayout(new BorderLayout());
		  tablePanel.add(tableScroll, BorderLayout.CENTER);
		  
		 
		  //Creation of the origin node panel, the origin List, the input textfield and action listeners
	      JPanel originNodeListPanel = new JPanel();
	      originNodeListPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
	      listPanel.add(originNodeListPanel);
	      originNodeListPanel.setLayout(new BorderLayout(0, 0));

	      
	      originNode = new JList(originModel);
	      
	      originNode.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
	      originInput = new JTextField("Input origin node");
	    
	   
	      
	     
	      
	
		     
		  JScrollPane originScroll = new JScrollPane(originNode);
	      originNodeListPanel.add(originInput, BorderLayout.NORTH);
	      originNodeListPanel.add(originScroll, BorderLayout.CENTER);
	      
	      
	      //panel for the instructions label
	      JPanel instructionsPanel = new JPanel();
	      instructionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	      JLabel instructions = new JLabel("<html><br>*Type origin node and hit ENTER to submit<br>**Select node and hit BACKSPACE to delete</html>");
	      instructionsPanel.add(instructions);

	      
	      //panel for the buttons, and creation of the buttons
	      JPanel buttonPanel = new JPanel();
	      buttonPanel.setBorder(BorderFactory.createTitledBorder("Select"));
	      listPanel.add(buttonPanel);
	      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
	      
	      ButtonGroup relationship = new ButtonGroup();

	  	  direct = new JRadioButton("+");
		  direct.setVerticalTextPosition(direct.TOP);
		  direct.setHorizontalTextPosition(direct.CENTER);
		  
		  indirect = new JRadioButton("-");
		  indirect.setVerticalTextPosition(indirect.TOP);
		  indirect.setHorizontalTextPosition(indirect.CENTER);
		  
		  relationship.add(direct);
		  relationship.add(indirect);
		  buttonPanel.add(direct);
		  buttonPanel.add(indirect);
	 

		  //Creation of the end node panel, the origin List, the input textfield and action listeners
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
	      
	      originNode.addMouseListener(new MouseAdapter() {
	    	  public void mouseClicked(MouseEvent e) {
	    		   if (e.getClickCount() == 2) {
	    			   	endNode.clearSelection();
	    				  originEditPopup();
	    				
	    			  }
	    		  
	    	  }
	      });
	      
	      
	      //key binding for deleting elements in the origin node list
	      InputMap deleteOrigin = originNode.getInputMap();
		     deleteOrigin.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_BACK_SPACE), "delete origin");
		     ActionMap ap = originNode.getActionMap();
		     ap.put("delete origin", new AbstractAction() {
		    	 public void actionPerformed (ActionEvent e) {
		    		 
		
		    		inputHash.remove(originNode.getSelectedValue());
		    		originModel.removeElement(originNode.getSelectedValue());
		    		
		    		updateTable(outputTable);
		    		
		    		 
		    		  leftPanel.revalidate();
		    		  
		    		  clearGraph(g);
		    		  mapFromInput();
		    		  frame.repaint();
		    	
		    	 }
		     });
		 
		  //end node textfield action listener
	      endInput.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent event) {
	    		  
	    		  
	    		  if (direct.isSelected() == true) {
	    			  

	    			  if (selectRelationship != null) {
		    			  System.out.println("im direct");

	    				  endNodeListPanel.remove(selectRelationship);
	    				  endModel.removeAllElements();
	    				  		    		  	  
			    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText() + " (+)");
			    		  
			    		  
			    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
			    			  endModel.addElement(end);
			    		  }
			    		  
			    		  updateTable(outputTable);
			    		  
			    		  leftPanel.revalidate();
			    		  endInput.setText("");
	    				  endNodeListPanel.revalidate();
	    				  
	    				  mapFromInput();

	    				
	    			  }
	    			  
	    			  else {
	    				  endModel.removeAllElements();
		    		  	  
			    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText() + " (+)");
			    		  
			    		  
			    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
			    			  endModel.addElement(end);
			    		  }
			    		  
			    		  updateTable(outputTable);

			    		  leftPanel.revalidate();
			    		 // process(endInput.getText(), (String) originNode.getSelectedValue());
	    				  mapFromInput();
			    		  endInput.setText("");
	    			  }
	    		  
	    		  }
	    		  
	    		  else if (indirect.isSelected() == true) {
	    			  
	    			  System.out.println("im indirect");
	    			  if (selectRelationship != null) {
	    				  endNodeListPanel.remove(selectRelationship);
	    				  endNodeListPanel.revalidate();
	    				  
	    				  endModel.removeAllElements();
		    		  	  
			    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText() + " (-)");
			    		  
			    		  
			    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
			    			  endModel.addElement(end);
			    		  }
			    		  
			    		
			    		  updateTable(outputTable);

			    		  leftPanel.revalidate();
	    				  mapFromInput();
	    				
			    		  endInput.setText("");

	    			  }
	    			  
	    			  else {
		    		  endModel.removeAllElements();
		    		  	  
		    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText() + " (-)");
		    		  
		    		  
		    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
		    			  endModel.addElement(end);
		    		  }
		    	 
		    		  updateTable(outputTable);

		    		  leftPanel.revalidate();
    				  mapFromInput();

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
	      endNode = new JList(endModel);
	      endNode.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
	      
	      endNode.addListSelectionListener(new ListSelectionListener() {
	    	  public void valueChanged(ListSelectionEvent e) {
	    		  System.out.println("selected");
	    		  
	    	  }
	      });
	      
	      endNode.addMouseListener(new MouseAdapter() {
	    	  public void mouseClicked(MouseEvent e) {
	    		   if (e.getClickCount() == 2) {
	    				  endEditPopup();
	    				
	    			  }
	    		  
	    	  }
	      });
	   
	      
	      JScrollPane endScroll = new JScrollPane(endNode);
	      endNodeListPanel.add(endScroll, BorderLayout.CENTER);
	      
	      //key binding for deleting elements in end node list
	      InputMap deleteEnd = endNode.getInputMap();
		     deleteEnd.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_BACK_SPACE), "delete end");
		     ActionMap end = endNode.getActionMap();
		     end.put("delete end", new AbstractAction() {
		    	 public void actionPerformed (ActionEvent e) {
		    		 

		    		 
		    		 inputHash.get(originNode.getSelectedValue()).remove(endNode.getSelectedValue());
		    		 endModel.removeElement(endNode.getSelectedValue());
		    		 updateTable(outputTable);

		    		  leftPanel.revalidate();

		    		  clearGraph(g);
		    		  g.removeVertex((String) endNode.getSelectedValue());
	   				  mapFromInput();
	   				  frame.repaint();

		    		 
		    	 }
		     }); 
	      
		
	     
	      
	   actionListeners();
	      
	      
	      
	      //borderPanel is the panel containing the lists and instructions
		  JPanel borderPanel = new JPanel();
		  borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.Y_AXIS));
		  borderPanel.add(listPanel);
		  borderPanel.add(instructionsPanel);
	      borderPanel.setBorder(BorderFactory.createTitledBorder("Input Variables"));
	      
	      tablePanel.setBorder(BorderFactory.createTitledBorder("All Links"));

	     leftPanel.add(borderPanel);
	     leftPanel.add(tablePanel);
	      

	
			 graphInputSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, graphPanel);

			frame.add(graphInputSplit);
			
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.setVisible(true);
	


		//compile the inputs and generate a map
				compile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						System.out.println("I was pressed");
					
					 
						
						
						
						if (counter > 1){
							
							
							//update additional links to array
							for (int row=1; row < counter; row++) {
								
							
									
							}
							
						
						
						}
						
						mapFromInput();
						
											
					}
				});			
	
		
		
	}
	
	public void originEditPopup() {

		//  Do a lazy creation of the popup editor

		if (editPopup == null)
    		originEditBackend();

		//  Position the popup editor over top of the selected row

		int row = originNode.getSelectedIndex();
		Rectangle r = originNode.getCellBounds(row, row);

		editPopup.setPreferredSize(new Dimension(r.width, r.height));
		editPopup.show(originNode, r.x, r.y);

		//  Prepare the text field for editing

		String selectedValue = originNode.getSelectedValue().toString();
		editTextField.setText(selectedValue);
		editTextField.selectAll();
		editTextField.requestFocusInWindow();
	}

	public void originEditBackend()
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
				
				System.out.println("Before hashmap" + inputHash);

				inputHash.put(value, inputHash.get(originNode.getSelectedValue()));
				inputHash.remove(originNode.getSelectedValue());
				System.out.println("After hashmap" + inputHash);
				
				originModel.removeAllElements();
				
				 for (String key : inputHash.keySet()) {
	    			  originModel.addElement(key);
	    		  }

				
	    		updateTable(outputTable);
	    		  clearGraph(g);
   				  mapFromInput();
   				  
   				  frame.repaint();
	    		 
	    		 
				//applyValueToModel(value, model, row);
				editPopup.setVisible(false);
			}
		});

		//  Add the editor to the popup

	    editPopup = new JPopupMenu();
		editPopup.setBorder( new EmptyBorder(0, 0, 0, 0) );
    	editPopup.add(editTextField);
    	
	}
	
	public void endEditPopup() {

		//  Do a lazy creation of the popup editor

		if (editPopup == null)
    		endEditBackend();

		//  Position the popup editor over top of the selected row

		int row = endNode.getSelectedIndex();
		Rectangle r = endNode.getCellBounds(row, row);

		editPopup.setPreferredSize(new Dimension(r.width, r.height));
		editPopup.show(endNode, r.x, r.y);

		//  Prepare the text field for editing

		
		String selectedValue = endNode.getSelectedValue().toString();
		System.out.println(selectedValue);
		editTextField.setText( selectedValue.substring(0, selectedValue.length()-4));
		editTextField.selectAll();
		editTextField.requestFocusInWindow();
	}

	public void endEditBackend()
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
				
	    		inputHash.get(originNode.getSelectedValue()).remove(endNode.getSelectedValue());
	    		System.out.println("Removal to key" + inputHash.get(originNode.getSelectedValue()));
	   
	    		if (direct.isSelected() == true) {
		    		inputHash.get(originNode.getSelectedValue()).add(value + " (+)");
		    		System.out.println("Addition to key" + inputHash.get(originNode.getSelectedValue()));


	    		}
	    		else if (indirect.isSelected() == true) {
		    		inputHash.get(originNode.getSelectedValue()).add(value + " (-)");
		    		System.out.println("Addition to key" + inputHash.get(originNode.getSelectedValue()));


	    		}
				endModel.removeAllElements();

	    		
	    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
			    		System.out.println("For loop" + end);

	    			  ((DefaultListModel) endModel).addElement(end);
	    		  }
	    		  
	    		  System.out.println("Hashmap at the end" + inputHash);
				

	    		updateTable(outputTable);
	    		  clearGraph(g);
   				  mapFromInput();
   				  frame.repaint();
	    		 
	    		 
				//applyValueToModel(value, model, row);
				editPopup.setVisible(false);
			}
		});

		//  Add the editor to the popup

	    editPopup = new JPopupMenu();
		editPopup.setBorder( new EmptyBorder(0, 0, 0, 0) );
    	editPopup.add(editTextField);
    	
	}
	 public void actionListeners() {
		 
		 
		 /*
		  //functionality for editing the end node list and the origin node list
	      Action editOrigin = new EditListAction(inputHash, outputTable);
	      Action editEnd = new EditEndNode(inputHash, originNode, outputTable, direct, indirect);
	      ListAction addOrigin = new ListAction(originNode, editOrigin);
	      ListAction addEnd = new ListAction(endNode, editEnd);
	      
	      */
	      
	      
	      
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
		 
		   originInput.addMouseListener(new MouseAdapter() {
		    	  public void mouseClicked(MouseEvent e) {
		    		  originInput.setText("");
		    		  
		    		  
		    	  }
		      });
	 }
}


