package play.Scrap;
/*
 * Copyright (c) 2008, The JUNG Authors
 * All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or https://github.com/jrtom/jung/blob/master/LICENSE for a description.
 */


import javax.swing.JFrame;

import com.google.common.base.Supplier;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A class that shows the minimal work necessary to load and visualize a graph.
 */
public class SimpleGraph 
{

	public static void main(String[] args)
    {
        JFrame jf = new JFrame();
		Graph<String, String> g = new DirectedSparseGraph<String, String>();
		g.addEdge("family", "josh", "elbaz");
        VisualizationViewer vv = new VisualizationViewer(new FRLayout(g));
        jf.getContentPane().add(vv);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

}


