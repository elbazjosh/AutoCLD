package autocld_main;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang3.StringUtils;


import com.google.common.base.Function;
import com.google.common.base.Functions;

import anotherCycleImplement.ElementaryCyclesSearch;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
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
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.ParallelEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.GradientVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.Edge;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import edu.uci.ics.jung.visualization.util.Animator;
import autocld_main.EdgeLabel.MutableDirectionalEdgeValue;
import edu.uci.ics.jung.visualization.*;


public class AutoCLD {

	JFrame frame;


	private static Graph<String, String> g = new DirectedSparseGraph<String, String>();
    ObservableGraph<String,String> og = new ObservableGraph<String,String>(g);

    private AbstractLayout<String,Integer> layout = null;
    VisualizationViewer vv = new VisualizationViewer(new FRLayout(g));
    SatelliteVisualizationViewer satelliteView = new SatelliteVisualizationViewer(vv, new Dimension(100, 40));

	private static String originVariables[];
	private static int counter = 0;
	private static String inputArray[][] = new String[100][3];
    private static String column[]={"Origin Node","Relationship","End Node"}; 

	
	private static HashMap<String, LinkedHashSet<String>> inputHash = new HashMap<String, LinkedHashSet<String>>();
	
	private static DefaultListModel originModel = new DefaultListModel();
	private static DefaultListModel endModel = new DefaultListModel();
	private static DefaultListModel reinforcingModel = new DefaultListModel();
	private static DefaultListModel balancingModel = new DefaultListModel();

	
	private static JLabel selectRelationship;
    private static JPanel graphPanel = new JPanel();
    private static JSplitPane graphInputSplit;
    
    private JTextField originInput;
    private JTable outputTable;
    private JTable cycleTable;
    private JList originNode;
    private JList endNode;
    private JList reinforcingCycle;
    private JList balancingCycle;
    private JRadioButton direct;
    private JRadioButton indirect;
	public JPopupMenu editPopup;
	public JTextField editTextField;
	public BooleanChangeTest editFire = new BooleanChangeTest(false);
	public BooleanChangeTest endEditFire = new BooleanChangeTest(false);


	
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
	
	public boolean[][] matrixBuilder() {
		boolean adjacencyMatrix[][] = new boolean [g.getVertexCount()][g.getVertexCount()];
		
		int rowCount =0;
		int colCount =0;
		for (String vertex : g.getVertices()) {
			
			Collection<String> connectedVertex = g.getSuccessors(vertex);
			
			
			for (String vertex2: g.getVertices()) {
				
				if (connectedVertex.contains(vertex2) == true) {
					adjacencyMatrix[rowCount][colCount] = true;
					colCount++;
				}
				else if (connectedVertex.contains(vertex2) == false) {
					adjacencyMatrix[rowCount][colCount] = false;
					colCount++;

				}
			}
			
			colCount=0;
			rowCount++;
		}
		
		
		//System.out.println(Arrays.deepToString(adjacencyMatrix));
		
	
		return adjacencyMatrix;
	}
	
	public void updateCycleTable() {
		
		reinforcingModel.clear();  
		balancingModel.clear();  

		String[] vertices = g.getVertices().toArray(new String[0]);
		boolean matrix[][] = matrixBuilder();
		ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(matrix, vertices);
		List cycles = ecs.getElementaryCycles();
		
	
		System.out.println(Arrays.toString(vertices));
		System.out.println("vertex count: " + g.getVertexCount());
		System.out.println("Cycle counter: " + cycles.size());
		
		ArrayList<String> row = null;
		System.out.println("Row going into if: " + row);

		if (cycles.size() > 0) {
			for (int i = 0; i < cycles.size(); i++) {
				List cycle = (List) cycles.get(i);
				 row = new ArrayList<String>();

				
				for (int j = 0; j < cycle.size(); j++) {
					String node = (String) cycle.get(j);
					
					
					if (j == 0) {
						row.add(node);
					}
					else if (j > 0) {
						String originNode = (String) cycle.get(j-1);
						Iterator<String> it = inputHash.get(originNode).iterator();
					     while(it.hasNext()){
					        	String gotIt = it.next();

					        if (gotIt.contains(node) == true) {
					        	System.out.println("I entered the if");
					        	row.add(" -->");
					        	row.add(gotIt.substring(gotIt.length()-3, gotIt.length()));
					        	row.add(" " + node);
					        }
					     }

					}
				}
				System.out.println(row);
				StringBuffer sb = new StringBuffer();
			      
			      for (String s : row) {
			         sb.append(s);
			      }
			      String entry = sb.toString();
				int plusCount = StringUtils.countMatches(entry, "+");
			    if ( plusCount % 2 == 0 ) {
			    	reinforcingModel.addElement(entry);
			    }
			    else if ( plusCount == 0 ) {
			    	reinforcingModel.addElement(entry);
			    }
			    else if ( plusCount%2 != 0 ) {
			    	balancingModel.addElement(entry);
			    }

				row.clear();
				System.out.println("Row after clear: " + row);
				} 
			}
				
		}
		
		
		
	
	
	public void clearGraph(Graph<String, String> g2) {
		
		int vertexCount = g2.getVertexCount();
		int edgeCount = g2.getEdgeCount();
		
		
		
		while(g2.getVertexCount() != 0) {
			g2.removeVertex(g2.getVertices().iterator().next());
		}

		
		System.out.println("Vertex count at the end: " + g2.getVertexCount());
	
	}
	

	public void mapFromInput() {
		int counter = 0;
		 for (String origin : inputHash.keySet()) {
			  for (final String end : inputHash.get(origin)) {
				  String relationship = end.substring(end.length()-2, end.length()-1);
				  
				  final String edgeName = relationship + counter;
				
				g.addEdge(edgeName, origin, end.substring(0, end.length()-4));  
				
				counter++;
			  }
		 }
		 
		 
		 LabelAsShape<String,Number> vlasr = new LabelAsShape<String,Number>(vv.getRenderContext());
	      vv.getRenderContext().setVertexLabelTransformer(
	        		// this chains together Functions so that the html tags
	        		// are prepended to the toString method output
	        		Functions.<Object,String,String>compose(
	        				new Function<String,String>(){
								public String apply(String input) {
									return "<html><center>"+input;
								}}, new ToStringLabeller()));
	      
	 
	      final Function<String, String> edgeLabel = new Function<String, String>(){

			public String apply(String edge) {
				// TODO Auto-generated method stub
				if (edge.contains("+")){
	                  return "+";
	              }
	              else if (edge.contains("-")){
	                  return "-";
	              }
	              else
	              return "error";
	          }
			

	      };
	      
	        vv.getRenderContext().setEdgeLabelTransformer(edgeLabel);
	        vv.getRenderContext().setVertexShapeTransformer(vlasr);
	        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.black));
	        
	     
	        vv.getRenderContext().setEdgeDrawPaintTransformer(Functions.<Paint>constant(Color.black));
	        vv.getRenderContext().setEdgeStrokeTransformer(Functions.<Stroke>constant(new BasicStroke(1.5f)));
	        
	        // customize the renderer
	        vv.getRenderer().setVertexRenderer(new GradientVertex<String,Number>(Color.white, Color.white, true));
	        vv.getRenderer().setVertexLabelRenderer(vlasr);

	        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Number>(vv.getPickedEdgeState(), Color.black, Color.cyan));
	        vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<String>(vv.getPickedVertexState(), Color.black, Color.yellow));
	       


	        satelliteView.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Number>(satelliteView.getPickedEdgeState(), Color.black, Color.cyan));
	        satelliteView.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<String>(satelliteView.getPickedVertexState(), Color.red, Color.yellow));
	        
	      // ScalingControl vv2Scaler = new CrossoverScalingControl();
	       // satelliteView.scaleToLayout(vv2Scaler);
	      
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
	            Graph<String, String> constructorArgs = g;

				Class<? extends Layout<Integer,Number>> layoutC = 
	                (Class<? extends Layout<Integer,Number>>) jcb.getSelectedItem();
	            try
	            {
	                Constructor<? extends Layout<Integer, Number>> constructor = layoutC
	                        .getConstructor(new Class[] {Graph.class});
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
	  
	  class MutableDirectionalEdgeValue extends ConstantDirectionalEdgeValueTransformer<Integer,Number> {
	        BoundedRangeModel undirectedModel = new DefaultBoundedRangeModel(5,0,0,10);
	        BoundedRangeModel directedModel = new DefaultBoundedRangeModel(7,0,0,10);
	        
	        public MutableDirectionalEdgeValue(double undirected, double directed) {
	            super(undirected, directed);
	            undirectedModel.setValue((int)(undirected*10));
	            directedModel.setValue((int)(directed*10));
	            
	            undirectedModel.addChangeListener(new ChangeListener(){
	                public void stateChanged(ChangeEvent e) {
	                    setUndirectedValue(new Double(undirectedModel.getValue()/10f));
	                    vv.repaint();
	                }
	            });
	            directedModel.addChangeListener(new ChangeListener(){
	                public void stateChanged(ChangeEvent e) {
	                    setDirectedValue(new Double(directedModel.getValue()/10f));
	                    vv.repaint();
	                }
	            });
	        }
	        
	        /**
	         * @return Returns the directedModel.
	         */
	        public BoundedRangeModel getDirectedModel() {
	            return directedModel;
	        }

	        /**
	         * @return Returns the undirectedModel.
	         */
	        public BoundedRangeModel getUndirectedModel() {
	            return undirectedModel;
	        }
	    }
	  
	public AutoCLD() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width, screenSize.height);
		
		JButton compile = new JButton ("Compile");
		JButton addAnother = new JButton ("Add Another");
		
		frame.setLayout(new BorderLayout(8, 6));
		
		//ToolBar Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		topPanel.add(compile);
		topPanel.add(addAnother);
		topPanel.setBorder(BorderFactory.createTitledBorder("Toolbar"));
		
		MutableDirectionalEdgeValue mv = new MutableDirectionalEdgeValue(.5, .5);
        vv.getRenderContext().setEdgeLabelClosenessTransformer(mv);
        JSlider directedSlider = new JSlider(mv.getDirectedModel()) {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width /= 2;
                return d;
            }
        }; 
      
        
        JSlider edgeOffsetSlider = new JSlider(-50,50) {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width /= 2;
                return d;
            }
        };
        edgeOffsetSlider.addChangeListener(new ChangeListener() {
            @SuppressWarnings("rawtypes")
			public void stateChanged(ChangeEvent e) {
                JSlider s = (JSlider)e.getSource();
                Function<? super Number, Shape> edgeShapeFunction = vv.getRenderContext().getEdgeShapeTransformer();
                if (edgeShapeFunction instanceof ParallelEdgeShapeTransformer) {
                	((ParallelEdgeShapeTransformer)edgeShapeFunction).setControlOffsetIncrement(s.getValue());
                	vv.repaint();
                }
            }
        });
        
        JPanel sliders = new JPanel();
        sliders.add(directedSlider);
        sliders.add(edgeOffsetSlider);
        frame.add(sliders, BorderLayout.SOUTH);
        
       

		
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
	      
	      originInput.addMouseListener(new MouseAdapter() {
	    	  public void mouseClicked(MouseEvent e) {
	    		  originInput.setText("");
	    		 
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
	    				
	    			  }
	    		  
	    	  }
	      });
	      
	      
		  //Creating new End Nodes in JList
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
	    				  mapFromInput();
			    		  updateCycleTable();
			    		  
			    		  leftPanel.revalidate();
			    		  endInput.setText("");
	    				  endNodeListPanel.revalidate();


	    				
	    			  }
	    			  
	    			  else {
	    				  endModel.removeAllElements();
		    		  	  
			    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText() + " (+)");
			    		  
			    		  
			    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
			    			  endModel.addElement(end);
			    		  }
			    		  
			    		  updateTable(outputTable);


			    		 // process(endInput.getText(), (String) originNode.getSelectedValue());
	    				  mapFromInput();
			    		  endInput.setText("");
			    		  updateCycleTable();
			    		  leftPanel.revalidate();


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


	    				  mapFromInput();
			    		  updateCycleTable();
			    		  leftPanel.revalidate();
			    		  endInput.setText("");

	    			  }
	    			  
	    			  else {
		    		  endModel.removeAllElements();
		    		  	  
		    		  inputHash.get(originNode.getSelectedValue()).add(endInput.getText() + " (-)");
		    		  
		    		  
		    		  for (String end : inputHash.get(originNode.getSelectedValue())) {
		    			  endModel.addElement(end);
		    		  }
		    	 
		    		  updateTable(outputTable);


    				  mapFromInput();
		    		  updateCycleTable();
		    		  leftPanel.revalidate();

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
	  
	      //Creating new Origin Nodes in JList
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
		 
		
	      
	      JScrollPane endScroll = new JScrollPane(endNode);
	      endNodeListPanel.add(endScroll, BorderLayout.CENTER);
	      
	      ////////////		Key Bindings		/////////////////
	      
	      //key binding for deleting elements in end node list
	      InputMap deleteEnd = endNode.getInputMap();
		     deleteEnd.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_BACK_SPACE), "delete end");
		     ActionMap end = endNode.getActionMap();
		     end.put("delete end", new AbstractAction() {
		    	 public void actionPerformed (ActionEvent e) {
		    		 

		    		 
		    		 inputHash.get(originNode.getSelectedValue()).remove(endNode.getSelectedValue());
		    		 endModel.removeElement(endNode.getSelectedValue());
		    		 updateTable(outputTable);



		    		  clearGraph(g);
		    		  g.removeVertex((String) endNode.getSelectedValue());
	   				  mapFromInput();
		    		  updateCycleTable();
		    		  leftPanel.revalidate();


	   				  frame.repaint();

		    		 
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

			    		
			    		 
			    		  
			    		  clearGraph(g);
			    		  mapFromInput();
			    		  updateCycleTable();
			    		  leftPanel.revalidate();


			    		  frame.repaint();
			    	
			    	 }
			     });
		     
		     
		     ////		This section adds editing functionality to the lists, fully integrated with graph		////
		     
		     //Adding listeners to notify us when an edit has been made so that the graph is refreshed
		     BooleanChangeListener listener = new BooleanChangeListener() {
		            public void stateChanged(BooleanChangeEvent event) {
		                System.out.println("Detected change to: "
		                    + event.getDispatcher().getFlag()
		                    + " -- event: " + event);
		                
		                  clearGraph(g);
				    	  mapFromInput();
				    	  frame.repaint();
				    	  editFire.setFlag(false);
				    	  System.out.println("boolean changed");
		            }
		        };
		        
	        BooleanChangeListener endlistener = new BooleanChangeListener() {
	            public void stateChanged(BooleanChangeEvent event) {
	                System.out.println("Detected change to: "
	                    + event.getDispatcher().getFlag()
	                    + " -- event: " + event);
	                
	                clearGraph(g);
			    	  mapFromInput();
			    	  frame.repaint();
			    	  endEditFire.setFlag(false);
			    	  System.out.println("boolean changed");
	            }
	        };
		        
	 	  editFire.addBooleanChangeListener(listener);
	 	  endEditFire.addBooleanChangeListener(endlistener);

	 	  //adding the edit function to the JLists
	      Action editOrigin = new EditListAction(inputHash, outputTable, editFire, endNode);
	      Action editEnd = new EditEndNode(inputHash, originNode, outputTable, direct, indirect, endEditFire);
	      ListAction addOrigin = new ListAction(originNode, editOrigin);
	      ListAction addEnd = new ListAction(endNode, editEnd);
		      
		    
		      
	      
	      //borderPanel is the panel containing the lists and instructions
		  JPanel borderPanel = new JPanel();
		  borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.Y_AXIS));
		  borderPanel.add(listPanel);
		  borderPanel.add(instructionsPanel);
	      borderPanel.setBorder(BorderFactory.createTitledBorder("Input Variables"));
	      	      
	      JTabbedPane tabbedPanels = new JTabbedPane();
	 
	      reinforcingCycle = new JList(reinforcingModel);
	      balancingCycle = new JList(balancingModel);
	 

		  JPanel cyclePanel = new JPanel();
		  cyclePanel.setLayout(new BoxLayout(cyclePanel, BoxLayout.X_AXIS));
		  JPanel rePanel = new JPanel(new BorderLayout());
		  JPanel baPanel = new JPanel(new BorderLayout());

	      reinforcingCycle.setBorder(BorderFactory.createTitledBorder("Reinforcing Loops"));
	      balancingCycle.setBorder(BorderFactory.createTitledBorder("Balancing Loops"));

		  rePanel.add(reinforcingCycle, BorderLayout.CENTER);
		  baPanel.add(balancingCycle, BorderLayout.CENTER);
		  
		  JScrollPane reScroll = new JScrollPane(rePanel);
		  JScrollPane baScroll = new JScrollPane(baPanel);

		  reScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		  baScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		  JSplitPane loopSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, reScroll, baScroll);
		  loopSplit.setResizeWeight(0.5f);
	      tabbedPanels.add("All Links", tablePanel);
	      tabbedPanels.add("Loops", loopSplit);
	      
		  
	      leftPanel.add(borderPanel);
	      leftPanel.add(tabbedPanels);
	      
		  graphInputSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, graphPanel);

		  frame.add(graphInputSplit);
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.setVisible(true);
	


		//compile the inputs and generate a map
				compile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						System.out.println("I was pressed");
						boolean test[][] = matrixBuilder();
						for (boolean[] row1 : test)
							System.out.println(Arrays.toString(row1));
						
						
				
						
						String[] vertices = g.getVertices().toArray(new String[0]);
						
						ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(test, vertices);
						List cycles = ecs.getElementaryCycles();
						for (int i = 0; i < cycles.size(); i++) {
							List cycle = (List) cycles.get(i);
							for (int j = 0; j < cycle.size(); j++) {
								String node = (String) cycle.get(j);
								if (j < cycle.size() - 1) {
									System.out.print(node + " -> ");
								} else {
									System.out.print(node);
								}
							}
							System.out.print("\n");
						}
						
					}
					
				});			
	
		
		
	}
	
	

}


