/*
 * Copyright (c) 2007, the JUNG Project and the Regents of the University of
 * California All rights reserved. This software is open-source under the BSD
 * license; see either "license.txt" or http://jung.sourceforge.net/license.txt
 * for a description.
 */
package play.Scrap;

import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.utils.UserData;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * <p>Unit tests for <code>CycleDetector</code></p>
 * 
 * @author Paul Stepowski (PGS)
 */
public class CycleDetectorTest
{
	// simple undirected graph with no cycles
	@Test
	public void test01 ()
	{
		// create graph
		SparseGraph g = new SparseGraph ();

		// create vertices
		SparseVertex v1 = new SparseVertex ();
		v1.setUserDatum ( "name", "v1", UserData.SHARED );
		SparseVertex v2 = new SparseVertex ();
		v2.setUserDatum ( "name", "v2", UserData.SHARED );
		SparseVertex v3 = new SparseVertex ();
		v3.setUserDatum ( "name", "v3", UserData.SHARED );

		// add vertices to graph
		g.addVertex ( v1 );
		g.addVertex ( v2 );
		g.addVertex ( v3 );

		// connect vertices with edges
		UndirectedSparseEdge e1 = new UndirectedSparseEdge ( v1, v2 );
		UndirectedSparseEdge e2 = new UndirectedSparseEdge ( v2, v3 );

		// add edges to graph
		g.addEdge ( e1 );
		g.addEdge ( e2 );

		// detect cycles
		CycleDetector dcd = new CycleDetector ( g );
		boolean b = dcd.detectCycle ();
		assertTrue ( !b );
	} // end test01()

	// simple undirected graph with cycle  
	@Test
	public void test02 ()
	{
		// create graph
		SparseGraph g = new SparseGraph ();

		// create vertices
		SparseVertex v1 = new SparseVertex ();
		v1.setUserDatum ( "name", "v1", UserData.SHARED );
		SparseVertex v2 = new SparseVertex ();
		v2.setUserDatum ( "name", "v2", UserData.SHARED );
		SparseVertex v3 = new SparseVertex ();
		v3.setUserDatum ( "name", "v3", UserData.SHARED );

		// add vertices to graph
		g.addVertex ( v1 );
		g.addVertex ( v2 );
		g.addVertex ( v3 );

		// connect vertices with edges
		UndirectedSparseEdge e1 = new UndirectedSparseEdge ( v1, v2 );
		UndirectedSparseEdge e2 = new UndirectedSparseEdge ( v2, v3 );
		UndirectedSparseEdge e3 = new UndirectedSparseEdge ( v3, v1 );

		// add edges to graph
		g.addEdge ( e1 );
		g.addEdge ( e2 );
		g.addEdge ( e3 );

		// detect cycles
		CycleDetector dcd = new CycleDetector ( g );
		boolean b = dcd.detectCycle ();
		assertTrue ( b );
	} // end test02()

	// simple directed graph with no cycles  
	@Test
	public void test03 ()
	{
		// create graph
		DirectedSparseGraph g = new DirectedSparseGraph ();

		// create vertices
		SparseVertex v1 = new SparseVertex ();
		v1.setUserDatum ( "name", "v1", UserData.SHARED );
		SparseVertex v2 = new SparseVertex ();
		v2.setUserDatum ( "name", "v2", UserData.SHARED );
		SparseVertex v3 = new SparseVertex ();
		v3.setUserDatum ( "name", "v3", UserData.SHARED );

		// add vertices to graph
		g.addVertex ( v1 );
		g.addVertex ( v2 );
		g.addVertex ( v3 );

		// connect vertices with edges
		DirectedSparseEdge e1 = new DirectedSparseEdge ( v1, v2 );
		DirectedSparseEdge e2 = new DirectedSparseEdge ( v2, v3 );
		DirectedSparseEdge e3 = new DirectedSparseEdge ( v1, v3 );

		// add edges to graph
		g.addEdge ( e1 );
		g.addEdge ( e2 );
		g.addEdge ( e3 );

		// detect cycles
		CycleDetector dcd = new CycleDetector ( g );
		boolean b = dcd.detectCycle ();
		assertTrue ( !b );
	} // end test03()

	// simple directed graph with cycles  
	@Test
	public void test04 ()
	{
		// create graph
		DirectedSparseGraph g = new DirectedSparseGraph ();

		// create vertices
		SparseVertex v1 = new SparseVertex ();
		v1.setUserDatum ( "name", "v1", UserData.SHARED );
		SparseVertex v2 = new SparseVertex ();
		v2.setUserDatum ( "name", "v2", UserData.SHARED );
		SparseVertex v3 = new SparseVertex ();
		v3.setUserDatum ( "name", "v3", UserData.SHARED );

		// add vertices to graph
		g.addVertex ( v1 );
		g.addVertex ( v2 );
		g.addVertex ( v3 );

		// connect vertices with edges
		DirectedSparseEdge e1 = new DirectedSparseEdge ( v1, v2 );
		DirectedSparseEdge e2 = new DirectedSparseEdge ( v2, v3 );
		DirectedSparseEdge e3 = new DirectedSparseEdge ( v3, v1 );

		// add edges to graph
		g.addEdge ( e1 );
		g.addEdge ( e2 );
		g.addEdge ( e3 );

		// detect cycles
		CycleDetector dcd = new CycleDetector ( g );
		boolean b = dcd.detectCycle ();
		assertTrue ( b );
	} // end test04()

	// more complex undirected graph with no cycles
	@Test
	public void test05 ()
	{
		// create graph
		SparseGraph g = new SparseGraph ();

		// create vertices
		SparseVertex v1 = new SparseVertex ();
		v1.setUserDatum ( "name", "v1", UserData.SHARED );
		SparseVertex v2 = new SparseVertex ();
		v2.setUserDatum ( "name", "v2", UserData.SHARED );
		SparseVertex v3 = new SparseVertex ();
		v3.setUserDatum ( "name", "v3", UserData.SHARED );
		SparseVertex v4 = new SparseVertex ();
		v4.setUserDatum ( "name", "v4", UserData.SHARED );
		SparseVertex v5 = new SparseVertex ();
		v5.setUserDatum ( "name", "v5", UserData.SHARED );
		SparseVertex v6 = new SparseVertex ();
		v6.setUserDatum ( "name", "v6", UserData.SHARED );
		SparseVertex v7 = new SparseVertex ();
		v7.setUserDatum ( "name", "v7", UserData.SHARED );
		SparseVertex v8 = new SparseVertex ();
		v8.setUserDatum ( "name", "v8", UserData.SHARED );

		// add vertices to graph
		g.addVertex ( v1 );
		g.addVertex ( v2 );
		g.addVertex ( v3 );
		g.addVertex ( v4 );
		g.addVertex ( v5 );
		g.addVertex ( v6 );
		g.addVertex ( v7 );
		g.addVertex ( v8 );

		// add edges to
		UndirectedSparseEdge e1 = new UndirectedSparseEdge ( v1, v2 );
		UndirectedSparseEdge e2 = new UndirectedSparseEdge ( v1, v3 );
		UndirectedSparseEdge e3 = new UndirectedSparseEdge ( v1, v4 );
		UndirectedSparseEdge e4 = new UndirectedSparseEdge ( v2, v5 );
		UndirectedSparseEdge e5 = new UndirectedSparseEdge ( v2, v6 );
		UndirectedSparseEdge e6 = new UndirectedSparseEdge ( v4, v7 );
		UndirectedSparseEdge e7 = new UndirectedSparseEdge ( v4, v8 );

		// add edges to graph
		g.addEdge ( e1 );
		g.addEdge ( e2 );
		g.addEdge ( e3 );
		g.addEdge ( e4 );
		g.addEdge ( e5 );
		g.addEdge ( e6 );
		g.addEdge ( e7 );

		// detect cycles
		CycleDetector dcd = new CycleDetector ( g );
		boolean b = dcd.detectCycle ();
		assertTrue ( !b );
	} // end test05()

	// more complex undirected graph with cycles
	@Test
	public void test06 ()
	{
		// create graph
		SparseGraph g = new SparseGraph ();

		// create vertices
		SparseVertex v1 = new SparseVertex ();
		v1.setUserDatum ( "name", "v1", UserData.SHARED );
		SparseVertex v2 = new SparseVertex ();
		v2.setUserDatum ( "name", "v2", UserData.SHARED );
		SparseVertex v3 = new SparseVertex ();
		v3.setUserDatum ( "name", "v3", UserData.SHARED );
		SparseVertex v4 = new SparseVertex ();
		v4.setUserDatum ( "name", "v4", UserData.SHARED );
		SparseVertex v5 = new SparseVertex ();
		v5.setUserDatum ( "name", "v5", UserData.SHARED );
		SparseVertex v6 = new SparseVertex ();
		v6.setUserDatum ( "name", "v6", UserData.SHARED );
		SparseVertex v7 = new SparseVertex ();
		v7.setUserDatum ( "name", "v7", UserData.SHARED );
		SparseVertex v8 = new SparseVertex ();
		v8.setUserDatum ( "name", "v8", UserData.SHARED );

		// add vertices to graph
		g.addVertex ( v1 );
		g.addVertex ( v2 );
		g.addVertex ( v3 );
		g.addVertex ( v4 );
		g.addVertex ( v5 );
		g.addVertex ( v6 );
		g.addVertex ( v7 );
		g.addVertex ( v8 );

		// add edges to
		UndirectedSparseEdge e1 = new UndirectedSparseEdge ( v1, v2 );
		UndirectedSparseEdge e2 = new UndirectedSparseEdge ( v1, v3 );
		UndirectedSparseEdge e3 = new UndirectedSparseEdge ( v1, v4 );
		UndirectedSparseEdge e4 = new UndirectedSparseEdge ( v2, v5 );
		UndirectedSparseEdge e5 = new UndirectedSparseEdge ( v2, v6 );
		UndirectedSparseEdge e6 = new UndirectedSparseEdge ( v4, v7 );
		UndirectedSparseEdge e7 = new UndirectedSparseEdge ( v4, v8 );
		UndirectedSparseEdge e8 = new UndirectedSparseEdge ( v6, v7 );

		// add edges to graph
		g.addEdge ( e1 );
		g.addEdge ( e2 );
		g.addEdge ( e3 );
		g.addEdge ( e4 );
		g.addEdge ( e5 );
		g.addEdge ( e6 );
		g.addEdge ( e7 );
		g.addEdge ( e8 );

		// detect cycles
		CycleDetector dcd = new CycleDetector ( g );
		boolean b = dcd.detectCycle ();
		assertTrue ( b );
	} // end test06()

	// more complex directed graph with no cycles
	@Test
	public void test07 ()
	{
		// create graph
		SparseGraph g = new SparseGraph ();

		// create vertices
		SparseVertex v1 = new SparseVertex ();
		v1.setUserDatum ( "name", "v1", UserData.SHARED );
		SparseVertex v2 = new SparseVertex ();
		v2.setUserDatum ( "name", "v2", UserData.SHARED );
		SparseVertex v3 = new SparseVertex ();
		v3.setUserDatum ( "name", "v3", UserData.SHARED );
		SparseVertex v4 = new SparseVertex ();
		v4.setUserDatum ( "name", "v4", UserData.SHARED );
		SparseVertex v5 = new SparseVertex ();
		v5.setUserDatum ( "name", "v5", UserData.SHARED );
		SparseVertex v6 = new SparseVertex ();
		v6.setUserDatum ( "name", "v6", UserData.SHARED );
		SparseVertex v7 = new SparseVertex ();
		v7.setUserDatum ( "name", "v7", UserData.SHARED );
		SparseVertex v8 = new SparseVertex ();
		v8.setUserDatum ( "name", "v8", UserData.SHARED );

		// add vertices to graph
		g.addVertex ( v1 );
		g.addVertex ( v2 );
		g.addVertex ( v3 );
		g.addVertex ( v4 );
		g.addVertex ( v5 );
		g.addVertex ( v6 );
		g.addVertex ( v7 );
		g.addVertex ( v8 );

		// add edges to
		DirectedSparseEdge e1 = new DirectedSparseEdge ( v1, v2 );
		DirectedSparseEdge e2 = new DirectedSparseEdge ( v1, v3 );
		DirectedSparseEdge e3 = new DirectedSparseEdge ( v1, v4 );
		DirectedSparseEdge e4 = new DirectedSparseEdge ( v2, v5 );
		DirectedSparseEdge e5 = new DirectedSparseEdge ( v2, v6 );
		DirectedSparseEdge e6 = new DirectedSparseEdge ( v4, v7 );
		DirectedSparseEdge e7 = new DirectedSparseEdge ( v4, v8 );
		DirectedSparseEdge e8 = new DirectedSparseEdge ( v6, v7 );

		// add edges to graph
		g.addEdge ( e1 );
		g.addEdge ( e2 );
		g.addEdge ( e3 );
		g.addEdge ( e4 );
		g.addEdge ( e5 );
		g.addEdge ( e6 );
		g.addEdge ( e7 );
		g.addEdge ( e8 );

		// detect cycles
		CycleDetector dcd = new CycleDetector ( g );
		boolean b = dcd.detectCycle ();
		assertTrue ( !b );
	} // end test07()

	// more complex directed graph with cycles
	@Test
	public void test08 ()
	{
		// create graph
		SparseGraph g = new SparseGraph ();

		// create vertices
		SparseVertex v1 = new SparseVertex ();
		v1.setUserDatum ( "name", "v1", UserData.SHARED );
		SparseVertex v2 = new SparseVertex ();
		v2.setUserDatum ( "name", "v2", UserData.SHARED );
		SparseVertex v3 = new SparseVertex ();
		v3.setUserDatum ( "name", "v3", UserData.SHARED );
		SparseVertex v4 = new SparseVertex ();
		v4.setUserDatum ( "name", "v4", UserData.SHARED );
		SparseVertex v5 = new SparseVertex ();
		v5.setUserDatum ( "name", "v5", UserData.SHARED );
		SparseVertex v6 = new SparseVertex ();
		v6.setUserDatum ( "name", "v6", UserData.SHARED );
		SparseVertex v7 = new SparseVertex ();
		v7.setUserDatum ( "name", "v7", UserData.SHARED );
		SparseVertex v8 = new SparseVertex ();
		v8.setUserDatum ( "name", "v8", UserData.SHARED );

		// add vertices to graph
		g.addVertex ( v1 );
		g.addVertex ( v2 );
		g.addVertex ( v3 );
		g.addVertex ( v4 );
		g.addVertex ( v5 );
		g.addVertex ( v6 );
		g.addVertex ( v7 );
		g.addVertex ( v8 );

		// add edges to
		DirectedSparseEdge e1 = new DirectedSparseEdge ( v1, v2 );
		DirectedSparseEdge e3 = new DirectedSparseEdge ( v1, v4 );
		DirectedSparseEdge e4 = new DirectedSparseEdge ( v2, v5 );
		DirectedSparseEdge e5 = new DirectedSparseEdge ( v2, v6 );
		DirectedSparseEdge e2 = new DirectedSparseEdge ( v3, v1 );
		DirectedSparseEdge e6 = new DirectedSparseEdge ( v4, v7 );
		DirectedSparseEdge e7 = new DirectedSparseEdge ( v4, v8 );
		DirectedSparseEdge e8 = new DirectedSparseEdge ( v6, v7 );
		DirectedSparseEdge e9 = new DirectedSparseEdge ( v7, v3 );

		// add edges to graph
		g.addEdge ( e1 );
		g.addEdge ( e2 );
		g.addEdge ( e3 );
		g.addEdge ( e4 );
		g.addEdge ( e5 );
		g.addEdge ( e6 );
		g.addEdge ( e7 );
		g.addEdge ( e8 );
		g.addEdge ( e9 );

		// detect cycles
		CycleDetector dcd = new CycleDetector ( g );
		boolean b = dcd.detectCycle ();
		assertTrue ( b );
	} // end test08()

	// more complex directed graph with cycles
	@Test
	public void test09 ()
	{
		// create graph
		SparseGraph g = new SparseGraph ();

		// create vertices
		SparseVertex v1 = new SparseVertex ();
		v1.setUserDatum ( "name", "v1", UserData.SHARED );
		SparseVertex v2 = new SparseVertex ();
		v2.setUserDatum ( "name", "v2", UserData.SHARED );
		SparseVertex v3 = new SparseVertex ();
		v3.setUserDatum ( "name", "v3", UserData.SHARED );
		SparseVertex v4 = new SparseVertex ();
		v4.setUserDatum ( "name", "v4", UserData.SHARED );
		SparseVertex v5 = new SparseVertex ();
		v5.setUserDatum ( "name", "v5", UserData.SHARED );
		SparseVertex v6 = new SparseVertex ();
		v6.setUserDatum ( "name", "v6", UserData.SHARED );
		SparseVertex v7 = new SparseVertex ();
		v7.setUserDatum ( "name", "v7", UserData.SHARED );
		SparseVertex v8 = new SparseVertex ();
		v8.setUserDatum ( "name", "v8", UserData.SHARED );

		// add vertices to graph
		g.addVertex ( v1 );
		g.addVertex ( v2 );
		g.addVertex ( v3 );
		g.addVertex ( v4 );
		g.addVertex ( v5 );
		g.addVertex ( v6 );
		g.addVertex ( v7 );
		g.addVertex ( v8 );

		// add edges to
		DirectedSparseEdge e1 = new DirectedSparseEdge ( v1, v2 );
		DirectedSparseEdge e3 = new DirectedSparseEdge ( v1, v4 );
		DirectedSparseEdge e4 = new DirectedSparseEdge ( v2, v5 );
		DirectedSparseEdge e5 = new DirectedSparseEdge ( v2, v6 );
		DirectedSparseEdge e2 = new DirectedSparseEdge ( v3, v1 );
		DirectedSparseEdge e6 = new DirectedSparseEdge ( v4, v7 );
		DirectedSparseEdge e7 = new DirectedSparseEdge ( v4, v8 );
		DirectedSparseEdge e8 = new DirectedSparseEdge ( v6, v7 );
		DirectedSparseEdge e9 = new DirectedSparseEdge ( v3, v7 );

		// add edges to graph
		g.addEdge ( e1 );
		g.addEdge ( e2 );
		g.addEdge ( e3 );
		g.addEdge ( e4 );
		g.addEdge ( e5 );
		g.addEdge ( e6 );
		g.addEdge ( e7 );
		g.addEdge ( e8 );
		g.addEdge ( e9 );

		// detect cycles
		CycleDetector dcd = new CycleDetector ( g );
		boolean b = dcd.detectCycle ();
		assertTrue ( !b );
	} // end test09()

} // end class
