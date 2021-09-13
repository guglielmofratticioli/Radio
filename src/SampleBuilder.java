import java.awt.Color;
import java.io.File;
import java.util.Random;

import com.mxgraph.util.mxHtmlColor;

public class SampleBuilder 
{
    
    private GraphPanel graphPanel;
    private ItemMediator itemMediator;
    
    public SampleBuilder(GraphPanel graphPanel, ItemMediator itemMediator) 
    {
        this.graphPanel = graphPanel;
        this.itemMediator = itemMediator;
    }
    
    public Sample build(String name, File file, String color) // Build a Sample from file, connect with its observer and adds it to itemMedator's list
    {

        Sample sample = new Sample(name, file);
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        color = mxHtmlColor.getHexColorString(new Color(r,g,b));
        sample.setColor(color);
        
        Observer sampleCell = graphPanel.addSampleCell(sample);
        sample.addObserver(sampleCell);
        itemMediator.addSample(sample);
        graphPanel.getGraph().refresh();
        return sample;
    }
}
