package data;

/**
 * Class to save some global values.
 * @author tommy
 *
 */
public class Constant {
	// interval to print progress of inserting a vertex
	public static final int INSERTING_PROGRESS = 20;
	// interval to print progress when comparing drawings in main memory
	public static final int INTERNAL_COMPARING_PROGRESS = 500;
	// interval to print progress whenn comparing drawings on hard disc
	public static final int EXTERNAL_COMPARING_PROGRESS = 100;
	// interval to print progress when calculating positions and labels of the drawnigs
	public static final int DRAWING_PROGRESS = 500;
	// interval to print progress when migrating after changing file-structure
	public static final int MIGRATION_PROGRESS = 500;
	// interval to print progress when counting something
	public static final int COUNTING_PROGRESS = 1000;
	
	// when inserting a new vertex, first save new drawings in memory, until this limit is passed
	public static final int EMBEDDING_RAM_LIST_SIZE = 25000;
	
	// interval in milliseconds to print state of DFS-like search
	public static final long DFS_PRINT = 10000;
	

	// window
	public static final int WINDOW_WIDTH  = 1920; // 1200
	public static final int WINDOW_HEIGHT = 1050;
	// canvas, where the drawing is shown (calculate positions for the drawings again after changing this!!!)
	public static final int CANVAS_WIDTH  = 1600;  // 800
	public static final int CANVAS_HEIGHT = 940;  // 800
	// the size (higth and width) of a node in the window (calculate positions for the drawings again after changing this!!!)
	public static final int NODE_SIZE     = 18;
	// text on the information bar
	public static final String CROSSING_COLOR = "Crossings are lilac.";
	public static final String SETS_COLOR     = "Nodes of the bip. sets are blue and red.";
	public static final String NEW_COLOR      = "Currently added nodes are green.";
}
