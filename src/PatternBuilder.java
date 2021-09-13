import java.awt.Color;
import java.util.Random;

import com.mxgraph.util.mxHtmlColor;

public class PatternBuilder 
{

    private GraphPanel graphPanel;
    private ItemMediator itemMediator;
    
    public PatternBuilder(GraphPanel graphPanel , ItemMediator itemMediator ) // init
    {
        this.graphPanel = graphPanel;
        this.itemMediator = itemMediator;
    }
    
    public Pattern build(String name, int length, String color) // builds the pattern , add pattern Cell, connects observer/observable
    {
        
        Pattern pattern = new Pattern(name,length);
        
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        color = mxHtmlColor.getHexColorString(new Color(r,g,b));
        pattern.setColor(color);

        Observer patternCell = graphPanel.addPatternCell(pattern);
        pattern.addObserver(patternCell);
        for(Step step : pattern.getPattern()){
            step.addObserver(patternCell);
        }
        itemMediator.addPattern(pattern);
        graphPanel.getGraph().refresh();
        return pattern;
        
    }

}
