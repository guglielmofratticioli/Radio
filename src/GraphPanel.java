import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mxgraph.io.mxCodecRegistry;
import com.mxgraph.io.mxObjectCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel.mxChildChange;
import com.mxgraph.model.mxGraphModel.mxGeometryChange;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

// in this panel will be displayed a customized mxGraph
public class GraphPanel extends JPanel 
{  

    GraphPanel thisPanel = this;                    // holds reference to itself, needed to forward a graphpanel reference inside mouseClicked Listener
    DropTarget dp;                                  // drag n drop functionality            
    ItemMediator itemMediator;                      
    
    private mxGraphOutline graphOutline;            // an graph is composed by : mxGraph <- Model , mxGraphComponent <- Swing View , mxGraphOutline <- perimeter
    private mxGraphComponent graphComponent;        
    private int mousePosX = 0;                      // stores mouse positions
    private int mousePosY = 0;  
    private mxRubberband rubberBand;                // rubber band selection support
    private boolean mouseScroll;                    // state to enable scrolling behaviour
    private mxGraph graph;                          // mxGraph model
    Map<String, Object> style;                      // here is the default mxGraph style

    public GraphPanel(ItemMediator itemMediator) 
    {
        super();
        this.itemMediator = itemMediator;

        mxCodecRegistry.register(new mxObjectCodec(	new Pattern()));        // needed to store Java Objects as mxCell values
        mxCodecRegistry.register(new mxObjectCodec(	new Sample()));
        mxCodecRegistry.register(new mxObjectCodec(	new Step()));
        
        this.graph = new mxGraph() {                                      // istantiate a customized mxGraph, with inline methods reimplementation

            public boolean isPort(Object cell) {                          // enables Input/Output port support for connecting cells
                mxGeometry geo = getCellGeometry(cell);     
                return (geo != null) ? geo.isRelative() : false;
            }
                                                                          // avoid cell folding
            public boolean isCellFoldable(Object cell, boolean collapse) {
                return false;
            }
                                                                          // empty string label over the cell when value is instance of Pattern/Sample
            public String convertValueToString(Object cell) {
                if (cell instanceof mxCell) {
                    Object value = ((mxCell) cell).getValue();
                    if (value instanceof Pattern || value instanceof Step || value instanceof Sample) {
                        return "";
                    }
                }

                return super.convertValueToString(cell);
            }

        };

        this.style = graph.getStylesheet().getDefaultEdgeStyle();
        this.style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.ElbowConnector);
        
        //Initialize Graph Here
        this.graph.getModel().beginUpdate();
        try {
        } finally {
            graph.getModel().endUpdate();
        }

        // Graph styling
        this.graph.setBorder(750);
        this.graph.setKeepEdgesInBackground(true);
        this.graphComponent = new mxGraphComponent(graph);
        Color c = new Color(100,10,100,0);
        this.graphComponent.getViewport().setBackground(c);
        this.graphComponent.getViewport().setOpaque(false);
        //this.graphComponent.getViewport().setAutoscrolls(true);
        this.graphComponent.setOpaque(false);
        this.graphComponent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.graphOutline = new mxGraphOutline(graphComponent);
        this.rubberBand = new mxRubberband(graphComponent);
        this.rubberBand.setEnabled(false);
        
        new mxKeyboardHandler(graphComponent);
        
        add(graphComponent);
        
        setBackground(Color.decode("#2c2c2c"));
        setOpaque(true);

        //graph.setMultigraph(false);
		graph.setAllowDanglingEdges(false);
        //this.setSize(300, 300);     
        this.installListeners();
    }
    @Override 
    protected void paintComponent(Graphics g) // Painting dot grid on the background
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(Color.decode("#5c5c5c"));
        for(int i = 0 ; i < 750; i+=25){
            for(int j = 0; j <750; j+=25){
                g2d.fillOval(i, j, 3, 3);
            }
        }
    }

    //Getter/Setter
    public mxGraph getGraph()
    {
        return this.graph;
    }
    //
    
    
    protected void installListeners() // install all event listener for the graph
    {
        //DragnDrop
        class DragListener implements DropTargetListener{

            SampleBuilder sampleBuilder;
            public DragListener(SampleBuilder sampleBuilder){
                this.sampleBuilder = sampleBuilder;
            }
            @Override
            public void dragEnter(DropTargetDragEvent arg0) {
               
                
            }
        
            @Override
            public void dragExit(DropTargetEvent arg0) {
               
                
            }
        
            @Override
            public void dragOver(DropTargetDragEvent arg0) {
               
                
            }
        
            @Override
            public void drop(DropTargetDropEvent event) {

            event.acceptDrop(DnDConstants.ACTION_COPY);
            Transferable t = event.getTransferable();
            DataFlavor[] df = t.getTransferDataFlavors();
    
            for(DataFlavor f : df){
                try{
                    if(f.isFlavorJavaFileListType()){
                        List<File> files = (List<File>) t.getTransferData(f);
                        for(File file : files) {
                            sampleBuilder.build(file.getName(), file, "red");
                        }
                    }
                }catch(Exception ex){
    
                }
            }
        
            }
            
        
            @Override
            public void dropActionChanged(DropTargetDragEvent arg0) {
               
                
            }
        
        }  
        
        SampleBuilder sb = new SampleBuilder(this,itemMediator);
        DragListener dragListener = new DragListener(sb);
        
        // wheel
        MouseWheelListener wheelTracker = new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getSource() instanceof mxGraphOutline || e.isControlDown()) {
                    GraphPanel.this.mouseWheelMoved(e);
                }
            }

        };
        graphOutline.addMouseWheelListener(wheelTracker);
        graphComponent.addMouseWheelListener(wheelTracker);

        // press
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (graphComponent.getCellAt(e.getX(), e.getY()) == null) {
                    mouseScroll = true;
                    dp = new DropTarget(graphComponent,dragListener);
                    graphComponent.setDragEnabled(true);
                }
                else {
                    graphComponent.setDragEnabled(false);
                    Object value = ((mxCell) graphComponent.getCellAt(e.getX(), e.getY())).getValue();
                    if (value instanceof Step) {
                        mxCell stepCell = ((mxCell) graphComponent.getCellAt(e.getX(), e.getY()));
                        Step step = (Step) stepCell.getValue();
                        if (step.getState() == StepState.ON) {
                            
                            try{System.out.println("on"); step.toggle();} catch(Exception ex) {ex.printStackTrace();}
                        } else {
                            System.out.println("off");
                            try{System.out.println("on"); step.toggle();} catch(Exception ex) {ex.printStackTrace();}
                        }
                        
                    } else if(value instanceof Sample){
                        Sample sample = (Sample) ((mxCell) graphComponent.getCellAt(e.getX(), e.getY())).getValue();
                        sample.play();
                    } else if ((String)value =="⊹"){
                        Pattern pattern = (Pattern) ((mxCell) graphComponent.getCellAt(e.getX(), e.getY())).getParent().getValue();
                        pattern.extend();
                    } else if((String) value =="←"){
                        Pattern pattern = (Pattern) ((mxCell) graphComponent.getCellAt(e.getX(), e.getY())).getParent().getValue();
                        pattern.reduce();
                    } else if((String) value =="+"){
                        Sample sample = (Sample) ((mxCell) graphComponent.getCellAt(e.getX(), e.getY())).getParent().getValue();
                        sample.volumeUp();
                    } else if((String) value =="-"){
                        Sample sample = (Sample) ((mxCell) graphComponent.getCellAt(e.getX(), e.getY())).getParent().getValue();
                        sample.volumeDown();
                    } 
                   
                }
               // graphComponent.refresh();

            }

            public void mouseReleased(MouseEvent e) {
                mouseScroll = false;
                graphComponent.setDragEnabled(true);
            }

            // new Pattern
            public void mouseClicked(MouseEvent e){
                if (graphComponent.getCellAt(e.getX(), e.getY()) == null){
                    if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                        PatternBuilder patternBuilder = new PatternBuilder(thisPanel,itemMediator);
                        patternBuilder.build("pattern", 4, "black");
                    }
                }
            }
        });

        // scroll
        graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {

                if (mouseScroll && !rubberBand.isEnabled()) {
                    graphComponent.getGraph().getView().scaleAndTranslate(
                            graphComponent.getGraph().getView().getScale(),
                            graphComponent.getGraph().getView().getTranslate().getX() - (mousePosX - e.getX()),
                            graphComponent.getGraph().getView().getTranslate().getY() - (mousePosY - e.getY()));
                    // graphComponent.getGraph().getView().scaleAndTranslate(graphComponent.getGraph().getView().getScale(),
                    // mousePosX, mousePosY);
                }
                mouseLocationChanged(e);
            }

            public void mouseMoved(MouseEvent e) {
                mouseDragged(e);
            }

        });

        // keys
        graphComponent.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 17) {
                    rubberBand.setEnabled(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                rubberBand.setEnabled(false);
            }

            @Override
            public void keyTyped(KeyEvent e) {
               

            }

        });

        // Graph Changed
        graphComponent.getGraph().getModel().addListener(mxEvent.CHANGE, new mxIEventListener() {

            @Override
            public void invoke(Object sender, mxEventObject evt) {

                System.out.println("-- -- --");
                for (Object change : (ArrayList<Object>) evt.getProperty("changes")) {

                    if (change instanceof mxGeometryChange)
                        System.out.println("geometry");
                    else if (change instanceof mxChildChange) {
                        mxCell alteredCell = ((mxCell) ((mxChildChange) change).getChild());
                        if (((mxChildChange) change).getIndex() == 0) {
                            if (alteredCell.isEdge())
                                {
                                ((Item)alteredCell.getSource().getValue()).removeChild((Item)alteredCell.getTarget().getValue());;
                                ((Item)alteredCell.getTarget().getValue()).removeFather((Item)alteredCell.getSource().getValue());
                            }
                            else
                                System.out.println("vertex");
                            System.out.println("deleted");
                        } else if (((mxChildChange) change).getPreviousIndex() == 0) {
                            if (alteredCell.isEdge()){
                                ((Item)alteredCell.getSource().getValue()).addChild((Item)alteredCell.getTarget().getValue());;
                                ((Item)alteredCell.getTarget().getValue()).addFather((Item)alteredCell.getSource().getValue());
                            }
                                
                            else
                                System.out.println("vertex");
  
                        }
                    }
                }
            }

        });

        
    }
    
    protected void mouseWheelMoved(MouseWheelEvent e) // zoom
    {
        if (e.getWheelRotation() < 0) {
            zoom(1.1);
        } else {
            zoom(1 / 1.1);
        }
    }
    
    protected void mouseLocationChanged(MouseEvent e) // translate
    {
        mousePosX = e.getX();
        mousePosY = e.getY();
    }

    public void zoom(double factor) 
    {
        mxGraphView view = graphComponent.getGraph().getView();
        double newScale = (double) ((int) (view.getScale() * 100 * factor)) / 100;

        if (newScale != view.getScale() && newScale > 0.04) {

            graphComponent.getGraph().getView().scaleAndTranslate(newScale, mousePosX, mousePosY);
        }
    }

    public Observer addPatternCell(Pattern pattern)
    {
        PatternCell patternCell =  new PatternCell(graph, pattern);
        return patternCell;
    }
    
    public Observer addSampleCell(Sample sample)
    {
        SampleCell sampleCell = new SampleCell(graph, sample);
        return sampleCell;
    }
    
    class PatternCell implements Observer // Pattern item cell  class
    {
        
        private Pattern pattern;
        private mxGraph graph;
        private mxCell patternCell;
        private mxCell inPort;
        private mxCell outPort;
        private List<mxCell> buttons =  new ArrayList<mxCell>();
        private mxGeometry patternCellGeometry;
        private mxGeometry inPortGeometry;
        private mxGeometry outPortGeometry;
        private mxCell add;
        private mxCell remove;
        private int PORT_DIAMETER = 20;                 // sizes for pattern buttons
        final int PORT_RADIUS = PORT_DIAMETER / 2;
        
        
        public PatternCell(mxGraph graph, Pattern pattern) // building cell body and ports
        {

            this.pattern = pattern;
            this.graph = graph;

            patternCell = (mxCell) graph.insertVertex(graph.getDefaultParent(), null, pattern, mousePosX, mousePosY, 25 * pattern.getPattern().length, 30,
            "shape=rectangle;perimeter=rectanglePerimeter;align=center;resizable=0;rounded=true;arcSize=10;strokeColor=white;fillColor=white;editable=false"); 
            patternCell.setConnectable(false);

            mxGeometry inPortGeometry = new mxGeometry(0, 0, 60, 30); 
            inPortGeometry.setOffset(new mxPoint(-15, 0)); inPortGeometry.setRelative(true);
            
            mxCell inPort = new mxCell(null, inPortGeometry,
            "rounded=true;arcSize=10;shape=rectangle;perimeter=rectanglePerimeter;strokeColor=white;fillColor=white;"); 
            inPort.setVertex(true);
            
            mxGeometry outPortGeometry = new mxGeometry(1, 0.5, PORT_DIAMETER / 1.8, PORT_DIAMETER);
            outPortGeometry.setOffset(new mxPoint(1, -PORT_RADIUS)); outPortGeometry.setRelative(true);
            
            mxCell outPort = new mxCell(null, outPortGeometry,
            "shape=triangle;strokeOpacity=0;strokeColor=" + pattern.getColor() + ";fillColor=" + pattern.getColor() +";"); 
            outPort.setVertex(true);

            mxGeometry addGeometry = new mxGeometry(1, 1, PORT_DIAMETER, PORT_DIAMETER);
            
            addGeometry.setOffset(new mxPoint(1, -PORT_RADIUS)); addGeometry.setRelative(true);

            add = new mxCell("⊹" , addGeometry,"shape=trinagle;strokeOpacity=1.0;fillOpacity=1.0;fontColor=ffffff;fontOpacity=50;fontSize=10;");
            add.setVertex(true);

            mxGeometry removeGeometry = new mxGeometry(0, 0.98, PORT_DIAMETER, PORT_DIAMETER);
            removeGeometry.setOffset(new mxPoint(1, -PORT_RADIUS)); removeGeometry.setRelative(true);

            remove = new mxCell("←" , removeGeometry,"shape=trinagle;strokeOpacity=1.0;fillOpacity=1.0;fontColor=5c5c5c;fontStyle=1;fontSize=10;");
            remove.setVertex(true);
          
            graph.addCell(inPort, patternCell); 
            graph.addCell(outPort, patternCell);
            graph.addCell(add,patternCell);
            graph.addCell(remove,patternCell);
            
            

            update();

        }
        
        @Override
        public void update() // updating whole view on Pattern properties changes
        {
            //update length
            mxGeometry old = patternCell.getGeometry();
            patternCell.setGeometry(new mxGeometry( old.getX(), old.getY() , 25 * pattern.getPattern().length, 30));

            //update steps
            for(mxCell button : buttons){
                button.removeFromParent();
            }
            buttons.clear();
            for (int i = 0; i < pattern.getPattern().length; i++) { 
                
                mxGeometry buttonGeometry = new mxGeometry(i / ((double) pattern.getPattern().length), 0.483, PORT_DIAMETER / 4, PORT_DIAMETER / 4); 
                buttonGeometry.setOffset(new mxPoint(-PORT_RADIUS / 4, -PORT_RADIUS / 4));
                buttonGeometry.setRelative(true); 
                mxCell button = new mxCell(pattern.getPattern()[i], buttonGeometry,null); 
                if(pattern.getPattern()[i].getState() == StepState.OFF){
                    button.setStyle("shape=rectangle;perimeter=rectanglePerimeter;strokeColor=white;fillColor=black;"); 
                }
                else{
                    mxGeometry oldB = buttonGeometry;
                    mxGeometry neo = new mxGeometry(oldB.getX(), oldB.getY(), oldB.getWidth() * 2,oldB.getHeight() * 2);
                    neo.setOffset(new mxPoint(-oldB.getWidth(), -oldB.getWidth()));
                    neo.setRelative(true);
                    button.setGeometry(neo);
                    button.setStyle("shape=ellipse;perimter=ellipsePerimeter;strokeColor=white;fillColor="+ pattern.getColor() + ";");
                }
                button.setVertex(true); 
                button.setConnectable(false);
                graph.addCell(button, patternCell); 
                buttons.add(button);
                System.out.println(i);
            }
            graph.refresh();
            
        }
        
        @Override
        public void update(Object object) // updating only a singular step 
        {
            
            if( object instanceof Step ){
                Step step = (Step)object;
                if (step.getState() == StepState.OFF) {
                    mxGeometry old = buttons.get(step.getIndex()).getGeometry();
                    mxGeometry neo = new mxGeometry(old.getX(), old.getY(), old.getWidth() / 2,old.getHeight() / 2);
                    neo.setOffset(new mxPoint(-old.getWidth() / 4, -old.getWidth() / 4));
                    neo.setRelative(true);
                    buttons.get(step.getIndex()).setGeometry(neo);
                    buttons.get(step.getIndex()).setStyle("shape=rectangle;perimeter=rectanglePerimeter;strokeColor=white;fillColor=black;");
                    
                } else {
                    mxGeometry old = buttons.get(step.getIndex()).getGeometry();
                    mxGeometry neo = new mxGeometry(old.getX(), old.getY(), old.getWidth() * 2,old.getHeight() * 2);
                    neo.setOffset(new mxPoint(-old.getWidth(), -old.getWidth()));
                    neo.setRelative(true);
                    buttons.get(step.getIndex()).setGeometry(neo);
                    buttons.get(step.getIndex()).setStyle("shape=ellipse;perimter=ellipsePerimeter;strokeColor=white;fillColor="+ pattern.getColor() + ";");
                }
                graph.refresh();

            }
            
        }
        
        // Getter/Setter
        public mxCell getInPort() {
            return inPort;
        }

        public void setInPort(mxCell inPort) {
            this.inPort = inPort;
        }

        public mxCell getOutPort() {
            return outPort;
        }

        public void setOutPort(mxCell outPort) {
            this.outPort = outPort;
        }

        public mxGeometry getPatternCellGeometry() {
            return patternCellGeometry;
        }

        public void setPatternCellGeometry(mxGeometry patternCellGeometry) {
            this.patternCellGeometry = patternCellGeometry;
        }

        public mxGeometry getInPortGeometry() {
            return inPortGeometry;
        }

        public void setInPortGeometry(mxGeometry inPortGeometry) {
            this.inPortGeometry = inPortGeometry;
        }

        public mxGeometry getOutPortGeometry() {
            return outPortGeometry;
        }

        public void setOutPortGeometry(mxGeometry outPortGeometry) {
            this.outPortGeometry = outPortGeometry;
        }
        //

    }
    
    class SampleCell implements Observer  // Sample item cell class
    {
        private Sample sample;
        private mxCell sampleCell;
        private mxCell volumeUp;
        private mxCell volumeDown;
        private mxCell nameCell;

        // TODO waveform PNG visualizer
        
        public SampleCell(mxGraph graph, Sample sample) // building a colored Square, name label and voume buttons
        {
            this.sample = sample;

            sampleCell = (mxCell) graph.insertVertex(graph.getDefaultParent(), null, sample, mousePosX, mousePosY, 50, 50,
            "resizable=0;rounded=false;sfontColor=white;strokeColor="+sample.getColor()+";fillColor="+sample.getColor()+";editable=false;");
            
            mxGeometry volumeUpGeometry = new mxGeometry(0.8, 1, 25, 15);  
            volumeUpGeometry.setOffset(new mxPoint(-15, 0)); 
            volumeUpGeometry.setRelative(true);
            
            volumeUp = new mxCell("+",volumeUpGeometry, "shape=rectangle;verticalAlign=center;editable=false;");
            volumeUp.setVertex(true);
            volumeUp.setConnectable(false);
            graph.addCell(volumeUp,sampleCell);

            mxGeometry volumeDownGeometry = new mxGeometry(0.3, 1, 25, 15);  
            volumeDownGeometry.setOffset(new mxPoint(-15, 0)); 
            volumeDownGeometry.setRelative(true);
            volumeDown = new mxCell("-",volumeDownGeometry, "shape=rectangle;verticalAlign=center;editable=false;");
            volumeDown.setVertex(true);
            volumeDown.setConnectable(false);
            graph.addCell(volumeDown,sampleCell);
            
            mxGeometry nameGeometry = new mxGeometry(0, -0.3 ,50, 15);
            //nameGeometry.setRelative(true);  
            nameCell = new mxCell("",nameGeometry, "resizable=0;fillColor=ffffff; fontColor="+sample.getColor()+";shape=rectangle;verticalAlign=center;editable=true;");
            nameCell.setVertex(true);
            nameCell.setConnectable(false);
            
            graph.addCell(nameCell,sampleCell);
            
        }
       
        @Override
        public void update()   // update Color opacity based on sample volume
        {
            sampleCell.setStyle("resizable=0;rounded=false;strokeColor="+sample.getColor()+";fillColor="+sample.getColor()+";editable=false");
            System.out.println(((Gain)sample.getPlayer().getProcessor().getModuleAt(0)).getGain());
            sampleCell.setStyle("opacity="+((Gain)sample.getPlayer().getProcessor().getModuleAt(0)).getGain()+";resizable=0;rounded=false;strokeColor="+sample.getColor()+";fillColor="+sample.getColor()+";editable=false");
        }

        @Override
        public void update(Object o) {
            sampleCell.setStyle("resizable=0;rounded=false;strokeColor="+sample.getColor()+";fillColor=ffffff;editable=false");
        }
        
    }

}
