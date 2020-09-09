package play.Scrap;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.collections15.Transformer;

import com.google.common.base.Function;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.shortestpath.BFSDistanceLabeler;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;

/**
 * Demonstrates use of the shortest path algorithm and visualization of the
 * results.
 * 
 * @author danyelf
 */
public class PathDemo extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 7526217664458188502L;

    /**
     * Starting vertex
     */
    private String mFrom;

    /**
     * Ending vertex
     */
    private String mTo;
    private Graph<String, String> mGraph;
    private Set<String> mPred;

    private BFSDistanceLabeler<String, String> bdl;

    private Graph<String, String> path;

    protected boolean full = true;

    private JLabel label;
    
    public void SCCFinder(){
    	int index = 0;
    	ArrayList<AbstractNode> stack = new ArrayList<AbstractNode>();
    	ArrayList<ArrayList<AbstractNode>> SCC = new
    ArrayList<ArrayList<AbstractNode>>();

    	HashMap<AbstractNode, Integer> indexHash = new HashMap<AbstractNode,
    Integer>();
    	HashMap<AbstractNode, Integer> lowlinkHash = new HashMap<AbstractNode,
    Integer>();
    		
    	private ArrayList<ArrayList<AbstractNode>> tarjan(AbstractNode v,
    DirectedGraph<AbstractNode, CommunicationPairEdge> source) {
    		for (AbstractNode node : source.getVertices()) {
    			indexHash.put(node, -1);
    			lowlinkHash.put(node, -1);
    		}
    		return tarjan_internal(v, source);
    	}

    	private ArrayList<ArrayList<AbstractNode>> tarjan_internal(AbstractNode v,
    DirectedGraph<AbstractNode, CommunicationPairEdge> source) {
    		indexHash.put(v, index);
    		lowlinkHash.put(v, index);
    		index++;
    		stack.add(0, v);

    		for (AbstractNode n : source.getSuccessors(v)) {
    			if (indexHash.get(n) == -1) {
    				tarjan_internal(n, source);
    				lowlinkHash.put(v, Math.min(lowlinkHash.get(v), lowlinkHash.get(n)));
    			} else if (stack.contains(n)) {
    				lowlinkHash.put(v, Math.min(lowlinkHash.get(v), indexHash.get(n)));
    			}
    		}
    		if (lowlinkHash.get(v).equals(indexHash.get(v))) {
    			AbstractNode n;
    			ArrayList<AbstractNode> component = new ArrayList<AbstractNode>();
    			do {
    				n = stack.remove(0);
    				component.add(n);
    			} while (n != v);
    			SCC.add(component);
    		}
    		return SCC;
    	}

    public PathDemo() {

        this.mGraph = getGraph();
        setBackground(Color.WHITE);
        // show graph
        final Layout<String, String> layout = new FRLayout<String, String>(
                mGraph);
        final VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(
                layout);
        vv.setBackground(Color.WHITE);

        /*
        vv.getRenderContext().setVertexDrawPaintTransformer(
              new  MyVertexDrawPaintFunction<String>());
        vv.getRenderContext().setVertexFillPaintTransformer(
               new MyVertexFillPaintFunction<String>());
        vv.getRenderContext().setEdgeDrawPaintTransformer(
               new MyEdgePaintFunction());
        vv.getRenderContext().setEdgeStrokeTransformer(
                new MyEdgeStrokeFunction());
        vv.getRenderContext().setVertexLabelTransformer(
                new ToStringLabeller()); */
        
        vv.setGraphMouse(new DefaultModalGraphMouse<String, String>());
        vv.addPostRenderPaintable(new VisualizationViewer.Paintable() {

            public boolean useTransform() {
                return true;
            }

            public void paint(Graphics g) {
                if (mPred == null)
                    return;

                // for all edges, paint edges that are in shortest path
                for (String e : layout.getGraph().getEdges()) {

                    if (isBlessed(e)) {
                        String v1 = mGraph.getEndpoints(e).getFirst();
                        String v2 = mGraph.getEndpoints(e).getSecond();
                        Point2D p1 = (Point2D) ((MyVertexDrawPaintFunction<java.lang.String>) layout).transform(v1);
                        Point2D p2 = (Point2D) ((MyVertexDrawPaintFunction<java.lang.String>) layout).transform(v2);
                        p1 = vv.getRenderContext().getMultiLayerTransformer()
                                .transform(Layer.LAYOUT, p1);
                        p2 = vv.getRenderContext().getMultiLayerTransformer()
                                .transform(Layer.LAYOUT, p2);
                        Renderer<String, String> renderer = vv.getRenderer();
                        renderer.renderEdge(vv.getRenderContext(), layout, e);
                    }
                }
            }
        });

        setLayout(new BorderLayout());
        add(vv, BorderLayout.CENTER);
        // set up controls
        add(setUpControls(), BorderLayout.SOUTH);
    }

    boolean isBlessed(String e) {
        Iterator<String> it = path.getEdges().iterator();
        while (it.hasNext()) {
            String edge = it.next();
            if (edge.equalsIgnoreCase(e.toString()))
                return true;
        }
        return false;
    }

    /**
     * @author danyelf
     */
    public class MyEdgePaintFunction implements Transformer<String, Paint> {

        public Paint transform(String e) {
            if (path == null || path.getEdges().size() == 0)
                return Color.BLACK;
            if (isBlessed(e)) {
                return new Color(0.0f, 0.0f, 1.0f, 0.5f);// Color.BLUE;
            } else {
                return Color.LIGHT_GRAY;
            }
        }
    }

    public class MyEdgeStrokeFunction implements Transformer<String, Stroke> {
        protected final Stroke THIN = new BasicStroke(1);
        protected final Stroke THICK = new BasicStroke(2);

        public Stroke transform(String e) {
            if (path == null || path.getEdges().size() == 0)
                return THIN;
            if (isBlessed(e)) {
                return THICK;
            } else
                return THIN;
        }

    }

    /**
     * @author danyelf
     */
    public class MyVertexDrawPaintFunction<V> implements Transformer<V, Paint> {

        public Paint transform(V v) {
            return Color.black;
        }

    }

    public class MyVertexFillPaintFunction<String> implements
            Transformer<String, Paint> {

        public Paint transform(String v) {
            if (v.equals(mFrom)) {
                return Color.BLUE;
            }
            if (v.equals(mTo)) {
                return Color.BLUE;
            }
            if (path == null) {
                return Color.LIGHT_GRAY;
            } else {
                if (mGraph.getVertices().contains(v)) {
                    return Color.RED;
                } else {
                    return Color.LIGHT_GRAY;
                }
            }
        }

    }

    /**
     *  
     */
    private JPanel setUpControls() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        label = new JLabel(
                "Select a pair of vertices for which all possible paths will be displayed");
        panel.add(label);
        JPanel jpPathType = new JPanel();
        jpPathType.add(new JLabel("Path type", SwingConstants.LEFT));
        jpPathType.add(getPathBox());

        JPanel jpFrom = new JPanel();
        jpFrom.add(new JLabel("vertex from", SwingConstants.LEFT));
        jpFrom.add(getSelectionBox(true));
        JPanel jpTo = new JPanel();
        jpTo.add(new JLabel("vertex to", SwingConstants.LEFT));
        jpTo.add(getSelectionBox(false));
        panel.add(jpPathType);
        panel.add(jpFrom);
        panel.add(jpTo);
        return panel;
    }

    public Component getPathBox() {
        Set<String> str = new HashSet<String>();
        str.add("Shortest");
        str.add("Full");
        final JComboBox path = new JComboBox(str.toArray());
        path.setSelectedItem("Full");
        path.setBackground(Color.WHITE);
        path.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String option = (String) path.getSelectedItem();
                if (option.equals("Full")) {
                    full = true;
                    label.setText("Select a pair of vertices for which all possible paths will be displayed");
                } else {
                    full = false;
                    label.setText("Select a pair of vertices for which a shortest path will be displayed");
                }
                drawPath();
                repaint();
            }
        });
        return path;
    }

    protected void drawPath() {
        if (mFrom == null || mTo == null) {
            return;
        }
        path = getNewGraph();
        if (full) {
            path = getPaths();
        } else {
            path = drawShortest();
        }
    }

    private Component getSelectionBox(final boolean from) {

        Set<String> s = new TreeSet<String>();

        for (String v : mGraph.getVertices()) {
            s.add(v);
        }
        final JComboBox choices = new JComboBox(s.toArray());
        choices.setSelectedIndex(-1);
        choices.setBackground(Color.WHITE);
        choices.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String v = (String) choices.getSelectedItem();

                if (from) {
                    mFrom = v;
                } else {
                    mTo = v;
                }
                drawPath();
                repaint();
            }
        });
        return choices;
    }

    /**
     * @return
     * 
     */
    protected Graph<String, String> getPaths() {
        path = getNewGraph();

        Set<String> visited = new HashSet<String>();
        LinkedList<String> currpath = new LinkedList<String>();

        findAllPaths(mFrom, visited, path, currpath);

        return path;

    }

    private void findAllPaths(String start, Set<String> visited,
            Graph<String, String> path, LinkedList<String> currpath) {

        if (visited.contains(start)) {
            return;
        }

        visited.add(start);

        currpath.addLast(start);

        if (start.equals(mTo)) {

            String pred = null;

            for (String l : currpath) {
                if (pred != null) {
                    path.addVertex(l);
                    path.addVertex(pred);
                    path.addEdge((pred + l), pred, l);
                }
                pred = l;
            }
            currpath.removeLast();
            visited.remove(start);
            return;
        }

        for (String edge : mGraph.getOutEdges(start)) {
            String succ = mGraph.getDest(edge);
            findAllPaths(succ, visited, path, currpath);
        }

        visited.remove(start);
        currpath.removeLast();
    }

    protected Graph<String, String> drawShortest() {
        path = getNewGraph();
        LinkedList<String> currpath = new LinkedList<String>();
        DijkstraShortestPath<String, String> alg = new DijkstraShortestPath(
                mGraph);
        List<String> l = alg.getPath(mFrom, mTo);
        Iterator<String> itrCon = l.iterator();
        while (itrCon.hasNext()) {
            String c = (String) itrCon.next();
            String source = mGraph.getSource(c);
            String dest = mGraph.getDest(c);
            path.addEdge(source + dest, source, dest);
        }
        return path;
    }

    public static void main(String[] s) {
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().add(new PathDemo());
        jf.pack();
        jf.setVisible(true);
    }

    /**
     * @return the graph for this demo
     */
    Graph<String, String> getGraph() {

        // Graph<V, E> where V is the type of the vertices and E is the type of
        // the edges
        Graph<String, String> g = new DirectedSparseGraph<String, String>();
        // Add some vertices. From above we defined these to be type Integer.
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        // Note that the default is for undirected edges, our Edges are Strings.
        g.addEdge("AB", "A", "B"); // Note that Java 1.5 auto-boxes primitives
        g.addEdge("BC", "B", "C");
        g.addEdge("AC", "A", "C");
        g.addEdge("BA", "B", "A");
        g.addEdge("CB", "C", "B");
        g.addEdge("CA", "C", "A");
        g.addEdge("AD", "A", "D");
        g.addEdge("CD", "C", "D");

        /*
         * g.addEdge("AB", "A", "B", EdgeType.DIRECTED); // Note that Java 1.5
         * auto-boxes primitives g.addEdge("BC", "B", "C", EdgeType.DIRECTED);
         * g.addEdge("AC", "A", "C", EdgeType.DIRECTED);
         */

        return g;
    }

    Graph<String, String> getNewGraph() {
        return new DirectedSparseGraph<String, String>();
    }
}