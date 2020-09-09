package play.Scrap;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.collections.Transformer;
import org.w3c.dom.Entity;

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
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent.Edge;
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



public class AllPaths {

    private static List<List<Edge>> allPaths;

    public static List<List<Edge>> getAllPathsBetweenNodes(DirectedGraph<Entity, Edge> graph, Entity startNode, Entity endNode) {
        allPaths = new ArrayList<List<Edge>>();

        List<Edge> currentPath = new ArrayList<Edge>();

        findAllPaths(startNode, startNode, endNode, currentPath, graph);

        return allPaths;
    }

    private static void findAllPaths(Entity currentNode, Entity startNode, Entity endNode, List<Edge> currentPath, DirectedGraph<Entity, Edge> graph) {
        Collection<Edge> outgoingEdges = graph.getOutEdges(currentNode);

        for (Edge outEdge : outgoingEdges) {
            Entity outNode = outEdge.getSuperType();

            if (outNode.equals(startNode)) {
                List<Edge> cyclePath = new ArrayList<Edge>(currentPath);
                cyclePath.add(outEdge);
                System.out.println("Found cycle provoked by path " + cyclePath);
                continue;
            }

            List<Edge> newPath = new ArrayList<Edge>(currentPath);
            newPath.add(outEdge);

            if (outNode.equals(endNode)) {
                allPaths.add(newPath);
                continue;
            }

            findAllPaths(outNode, startNode, endNode, newPath, graph);
        }
    }
}