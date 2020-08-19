package data;

/**
 * Class for the statistic of a certain graph.
 * @author tommy
 *
 */
public class Statistics {
	public int  totalCreated;	// number of created drawings
	public int  nonIsomorphic;	// number of non-isomorphic drawings
	public long time;			// time it took to create all non-isomorphic drawings
	public int  maxCrossings = -1;   // maximal number of crossings in a drawing
	public int  minCrossings = -1;   // minimal number of crossings in a drawing
}
