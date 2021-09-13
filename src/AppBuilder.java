import java.awt.Color;
import javax.swing.JLayeredPane;

// Builder pattern for Application interface 

public class AppBuilder
{
    
    private String[] args;              //  cli arguments
    private ItemMediator itemMediator;  //  Mediator manages playback and item relations
    private App app;                    
    private ControlPanel controlPanel;  //  play/pause/slider buttons
    private GraphPanel graphPanel;      //  panel containing mxgraph
    private JLayeredPane jLayeredPane;  //  layered pane to float buttons over the mxGraph

    public AppBuilder(String[] args) 
    { 
        this.setArgs(args);
    }

    public void build()
    {

        this.itemMediator = new ItemMediator();
        this.itemMediator.stop();       //stopping in init
       
        this.app = new App();
        this.controlPanel = new ControlPanel(itemMediator);     // ControlPanel reference item Mediator to control playback
        this.jLayeredPane = new JLayeredPane();                 // GraphPanel reference item Mediator to control connections
        this.graphPanel = new GraphPanel(itemMediator);
        jLayeredPane.add(graphPanel);                          // Stack the Layers
        graphPanel.setBounds(0,-6,750,750);
        jLayeredPane.setLayer(graphPanel, 1);
        jLayeredPane.add(controlPanel);
        controlPanel.setBackground(new Color(0,0,0,0));
        controlPanel.setBounds(275, 650, 200, 100);
        jLayeredPane.setLayer(controlPanel,2);
        
        app.add(jLayeredPane);
        app.setSize(750,750);
        app.setResizable(false);
        app.setVisible(true);
    }

    public String[] getArgs() 
    {
        return args;
    }

    public void setArgs(String[] args) 
    {
        this.args = args;
    }

}

