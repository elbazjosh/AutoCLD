/*
 * Copyright (c) 2007, the JUNG Project and the Regents of the University of
 * California All rights reserved. This software is open-source under the BSD
 * license; see either "license.txt" or http://jung.sourceforge.net/license.txt
 * for a description.
 */
package play.Scrap;

import edu.uci.ics.jung.graph.ArchetypeGraph;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.impl.SimpleSparseVertex;
import edu.uci.ics.jung.utils.UserData;

import java.util.Iterator;

/**
 * <p>Determines whether the specified graph contains any cycles.
 * The algorithm is based on a depth first search combined with
 * marking of visited nodes.</p>
 * 
 * @author Paul Stepowski (PGS)
 */
public class CycleDetector
{
	// graph to be searched for cycles
	protected ArchetypeGraph graph;

	// working copy of the graph to be searched for cycles
	protected ArchetypeGraph graphCopy;

	/**
	 * <p>Creates an instance of <code>CycleDetector</code> for the
	 * specified graph.</p>
	 * 
	 * @param g the graph which we want to search for cycles
	 */
	public CycleDetector ( ArchetypeGraph g )
	{
		this.graph = g;
		this.graphCopy = (ArchetypeGraph)this.graph.copy ();
	} // end constructor

	/**
	 * <p>Returns true if any cycle(s) were found, otherwise false.</p>
	 * <p>NOTE: the cycle detection process is not necessarily deterministic
	 * even when applied to graphs that are exactly the same.  This is
	 * because the search is started at an arbitrary vertex in the graph.
	 * Different starting vertexes cause non deterministic search
	 * times.</p>
	 */
	public boolean detectCycle ()
	{
		return ( detectCycleDFS () );
	} // end detectCycle()

	/**
	 * <p>Depth first search based cycle detection algorithm.  Returns 
	 * true if any cycle(s) were found, otherwise false.</p> 
	 */
	protected boolean detectCycleDFS ()
	{
		Iterator i = this.graphCopy.getVertices ().iterator ();
		ArchetypeVertex root = null;
		while ( i.hasNext () )
		{
			ArchetypeVertex v = (ArchetypeVertex)i.next ();
			v.setUserDatum ( "mark", "white", UserData.CLONE );
			if ( root == null )
			{
				root = v; // save the root vertex for later
			}
		}

		i = this.graphCopy.getVertices ().iterator ();
		{
			ArchetypeVertex v = (ArchetypeVertex)i.next ();
			if ( v.getUserDatum ( "mark" ).equals ( "white" ) )
			{
				if ( visitDFS ( v ) )
				{
					return ( true );
				}
			}
		}
		return ( false );
	} // end detectCycleDFS()

	/**
	 * <p>Visits the specified vertex and marks the vertex gray.  Then,
	 * uses a depth first search to determine if the vertex completes a cycle,
	 * If so, returns true.  Otherwise, marks the vertex as black and returns
	 * false. </p>
	 * 
	 * @param v vertex to visit
	 */
	protected boolean visitDFS ( ArchetypeVertex v )
	{
		v.setUserDatum ( "mark", "gray", UserData.CLONE );

		Iterator i = ( (SimpleSparseVertex)v ).getPredecessors ().iterator ();
		while ( i.hasNext () )
		{
			ArchetypeVertex u = (ArchetypeVertex)i.next ();
			if ( ( u.getUserDatum ( "mark" ).equals ( "gray" ) )
					&& ( !v.getUserDatum ( "predecessor" ).equals ( u ) ) )
			{
				return ( true );
			}
			else if ( u.getUserDatum ( "mark" ).equals ( "white" ) )
			{
				u.setUserDatum ( "predecessor", v, UserData.CLONE );
				return ( visitDFS ( u ) );
			}
		}

		v.setUserDatum ( "mark", "black", UserData.CLONE );
		return ( false );
	} // end visit()

} // end class
