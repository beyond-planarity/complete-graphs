package drawing;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import data.Constant;
import data.Edge;
import data.Embedding;
import data.EnumColor;
import data.Vertex;
import io.safeLoad.SafeLoad;

/**
 * Class for showing drawings calculated with the DFS-like approach in a window.
 * @author tommy
 *
 */
public class DrawerDFS implements ActionListener  {

	private String   folderName;
	private String   windowTitle;
	private String[] fileNames;
	
	private Frame      mainFrame;
	private ScrollPane scrollPane;
	private Panel      mainPanel;
	
	private Label crossingsColLabel;
	private Label twoPartitionColLabel;
	private Label numberLabel;
	private Label crossingLabel;
	private Panel controlPanel;
	private Panel drawingPanel;
	
	private MyCanvas canvasDrawing;

	private Button nextDrawing;
	private Button prevDrawing;

	private Embedding drawing;
	private int       fileNo;
	private int       crossingNo;
	
	
	/**
	 * Creates a new <code>DrawerDFS</code>.
	 * @param folderName name of the folder that contains the drawings
	 */
	public DrawerDFS(String folderName) {
		this.windowTitle  = folderName;
		this.folderName   = folderName;
		this.fileNames    = SafeLoad.getEmbeddingFiles(folderName);
		this.fileNo    = 0;
				
		loadEmbedding();
		prepareGUI();
	}
	
	/**
	 * Loads the current embedding.
	 */
	private void loadEmbedding() {
		this.drawing          = SafeLoad.loadEmbedding(folderName, fileNames[fileNo]);
		this.crossingNo       = drawing.getCrossingNumber();
	}
	
	/**
	 * Prepares for showing the current drawing.
	 */
	private void loadDrawing() {
		loadEmbedding();
		canvasDrawing.setDrawing(drawing);
	}
	
	/**
	 * Shows the next drawing.
	 */
	private void nextEmbedding() {
		fileNo++;
		if (fileNo >= fileNames.length) {
			fileNo = 0;
		}
		loadDrawing();
		updatePainting();
	}
	/**
	 * Shows the previous drawing.
	 */
	private void previousEmbedding() {
		fileNo--;
		if (fileNo < 0) {
			fileNo = fileNames.length - 1;
		}
		loadDrawing();
		updatePainting();
	}
	
	/**
	 * Updates the window.
	 */
	private void updatePainting() {
		setLabels();
		mainFrame.repaint();
		canvasDrawing.repaint();
	}

	/**
	 * Initializes the window.
	 */
	private void prepareGUI() {
		mainFrame = new Frame(windowTitle);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				mainFrame.dispose();
			}        
		});
		mainFrame.setSize(Constant.WINDOW_WIDTH,Constant.WINDOW_HEIGHT);
		mainFrame.setLayout(new FlowLayout());
		
		scrollPane = new ScrollPane();
		scrollPane.setSize(Constant.WINDOW_WIDTH-60, Constant.WINDOW_HEIGHT-60);
		mainFrame.add(scrollPane);
		
		mainPanel = new Panel();
		mainPanel.setLayout(new FlowLayout());
		scrollPane.add(mainPanel);
		
		
		crossingsColLabel = new Label();
		crossingsColLabel.setAlignment(Label.LEFT);
		crossingsColLabel.setText("Crossings are red.");
		twoPartitionColLabel = new Label();
		twoPartitionColLabel.setAlignment(Label.LEFT);
		twoPartitionColLabel.setText("Nodes of the 2-set are blue.");
		numberLabel = new Label();
		numberLabel.setAlignment(Label.LEFT);
		crossingLabel = new Label();
		crossingLabel.setAlignment(Label.LEFT);
		
		nextDrawing = new Button("NEXT");
		nextDrawing.addActionListener(this);
		prevDrawing = new Button("PREV");
		prevDrawing.addActionListener(this);


		controlPanel = new Panel();
		controlPanel.setLayout(new GridLayout(0,1,0,5));
		controlPanel.add(crossingsColLabel);
		controlPanel.add(twoPartitionColLabel);
		controlPanel.add(new Label(""));
		controlPanel.add(numberLabel);
		controlPanel.add(crossingLabel);
		controlPanel.add(new Label(""));
		controlPanel.add(nextDrawing);
		controlPanel.add(prevDrawing);
		controlPanel.add(new Label(""));
		
		drawingPanel = new Panel();
		drawingPanel.setLayout(new FlowLayout());

		mainPanel.add(controlPanel);
		mainPanel.add(drawingPanel);
		mainFrame.setVisible(true);  
	}
	
	/**
	 * Sets the information for the current drawings. 
	 */
	private void setLabels() {
		numberLabel.setText("Number: " + (fileNo+1) + " of " + fileNames.length);
		crossingLabel.setText("Crossings: " + crossingNo);
	}

	/**
	 * Shows the window.
	 */
	public void show(){
		setLabels();
		
		canvasDrawing = new MyCanvas();
		canvasDrawing.setDrawing(drawing);
		drawingPanel.add(canvasDrawing);
		
		drawingPanel.add(new Label(""));
		
		mainFrame.setVisible(true);  
	} 


	/**
	 * Class for showing the current drawing.
	 * @author tommy
	 *
	 */
	@SuppressWarnings("serial")
	class MyCanvas extends Canvas {
		
		private Embedding emb;
		private boolean   areVerticesMapped = false;

		/**
		 * Creates a new <code>MyCanvas</code>.
		 */
		public MyCanvas () {
			setBackground(EnumColor.DRAWING_BACK.getColor());
			setSize(Constant.CANVAS_WIDTH, Constant.CANVAS_HEIGHT);
		}
		
		/**
		 * Show the mapping of all vertices.
		 * @param show	true, if mapping should be shown 
		 */
		public void showMapping(boolean show) {
			areVerticesMapped = show;
		}
		
		/**
		 * Sets the current drawing.
		 * @param drawing the new drawing to show
		 */
		public void setDrawing(Embedding drawing) {
			this.emb = drawing;
		}

		public void paint(Graphics g) {
			Graphics2D g2;
			g2 = (Graphics2D) g;
			int offset = Constant.NODE_SIZE/2;

			// draw edges
			for (Edge e : emb.getEdges().values()) {
				if (!e.isToDraw()) {
					continue;
				}
				
				g2.setColor(EnumColor.EDGE.getColor());
								
				Vertex source = e.getSource();
				Vertex target = e.getTarget();

				int x1 = source.getStrechedX();
				int y1 = source.getStrechedY();
				int x2 = target.getStrechedX();
				int y2 = target.getStrechedY();
				g2.drawLine(x1, y1, x2, y2);
				
				// write source to the edges
				g2.setColor(Color.WHITE);
				g2.drawString(areVerticesMapped ? e.getMappedName() : e.getName(), (x1+x2)/2, (y1+y2)/2);
			}
			
			// draw vertices
			int textXOffset = 0;
			int textYOffset = Constant.NODE_SIZE - 6;
			for (Vertex v : emb.getVertices().values()) {
				g2.setColor(v.getColor());
				int x = v.getStrechedX() - offset;
				int y = v.getStrechedY() - offset;
				
				g2.fillRoundRect(x, y, Constant.NODE_SIZE, Constant.NODE_SIZE, 2, 2);
				g2.setColor(EnumColor.VERTEX_TEXT.getColor());
				g2.drawString(areVerticesMapped ? v.getMappedName() : v.getName(), x + textXOffset, y + textYOffset);
			}
		}
	}
	
	


	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(nextDrawing)) {
			nextEmbedding();
		}
		else if (e.getSource().equals(prevDrawing)) {
			previousEmbedding();
		}
	}
}
