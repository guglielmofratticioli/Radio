import java.awt.Color;
import javax.swing.JLayeredPane;

public class AppBuilder{
    
    private String[] args;
    private ItemMediator itemMediator;
    private App app;
    private ControlPanel controlPanel;
    private GraphPanel graphPanel;
    private JLayeredPane jLayeredPane;


    public AppBuilder(String[] args) { 
        this.setArgs(args);
    }

    public void build(){

        this.itemMediator = new ItemMediator();
        this.itemMediator.stop();
       
        this.app = new App();
        this.controlPanel = new ControlPanel(itemMediator);
        this.graphPanel= new GraphPanel(itemMediator);
        this.jLayeredPane = new JLayeredPane();

        jLayeredPane.add(graphPanel);
        graphPanel.setBounds(0,-6,500,500);
        jLayeredPane.setLayer(graphPanel, 1);
        jLayeredPane.add(controlPanel);
        controlPanel.setBackground(new Color(0,0,0,0));
        controlPanel.setBounds(150, 400, 200, 100);
        jLayeredPane.setLayer(controlPanel,2);
        
        app.add(jLayeredPane);
        app.setSize(500,500);
        app.setResizable(false);
        app.setVisible(true);
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

}

