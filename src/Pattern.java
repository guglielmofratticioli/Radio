import java.io.Serializable;
import java.util.ArrayList;
public class Pattern extends Observable implements Item, Serializable 
{

    private ArrayList<Item> children = new ArrayList<Item>();    // List of items this pattern is pointing to
    private ArrayList<Item> fathers = new ArrayList<Item>();     // List of fathers for this pattern
    
    private Step[] pattern;                   // vector of pattern's atomic steps 
    private int currentPosition = 0;          // current position
    private int beatCount = 0;                // lenght in beats of a singular pattern step
    private String name;
    private String color;

    public Pattern(String name, Step[] pattern) 
    {
        this.pattern = pattern;
        this.name = name;
    }
    
    public Pattern(String name, int length)
    {
        this.pattern = new Step[length];
        for(int i = 0; i < pattern.length; i++)  pattern[i] = new Step(i,this,StepState.OFF);
        this.name = name;
    }
    
    public Pattern() 
    {
    }

    @Override
    public void play() // for each children play if current position is in state ON , update current position by 1 every beat Unit 
    {
        if(!children.isEmpty())
        {   
            for(Item child: children)
            {
                if(pattern[currentPosition].getState() == StepState.ON) 
                    child.play();
            }
            beatCount++;
            if(getBeatUnit()>0 && beatCount%getBeatUnit() == 0) currentPosition = (currentPosition + 1) % pattern.length;
        }
    }

    @Override
    public void stop() // stop and rewind position
    {
        currentPosition = 0;
        for(Item child : children)
            child.stop();
    }

    @Override
    public void addFather(Item father) 
    {
        fathers.add(father);
    }
    @Override
    public void addChild(Item child) 
    {
        children.add(child);
    }
    @Override
    public void removeFather(Item father) 
    {
        fathers.remove(father);
    }
    @Override
    public void removeChild(Item child) 
    {
        children.remove(child);
    }
    

    public void pause() // unused method, just stop without rewind
    {
        for(Item child : children) child.stop();
    }

    public int getBeatUnit() // recoursively get beat unit from children
    {
        if(!children.isEmpty())
        {
            if (this.children.get(0) instanceof Sample)
                return 1;
            else 
            {
                return ((Pattern)children.get(0)).getPattern().length*((Pattern)children.get(0)).getBeatUnit();
            }
        } return 0;
    }

    public void extend() // extend lenght by 4 step 
    {

        Step[] extended = new Step[pattern.length + 4];       
        for(int i = 0 ; i< extended.length; i++) {
            extended[i] = new Step(i, this, StepState.OFF);
        }
        for(int i = 0 ; i < pattern.length; i++) {
            extended[i].setState(pattern[i].getState());
        }
        this.pattern = extended;
        for(Step step : pattern){
            for(Observer observer : observers)
                step.addObserver(observer);
        }
        notifyChanges();
    }
    
    public void reduce() // reduce length by 1 step
    {
        Step[] reduced = new Step[pattern.length-1];
        for( int i = 0 ; i <reduced.length; i++){
            reduced[i] = pattern[i];
        }
        this.pattern = reduced;
        for(Step step : pattern){
            for(Observer observer : observers)
                step.addObserver(observer);
        }
        notifyChanges();
    }
   
    // Getter/Setter
    public ArrayList<Item> getFathers()
    {
        return this.fathers;
    }
    
    public Step[] getPattern() 
    {
        return this.pattern;
    }

    public void setPattern(Step[] pattern) 
    {
        this.pattern = pattern;
        notifyChanges();
    }

    public int getCurrentPosition()
     {
        return this.currentPosition;
    }

    public void setCurrentPosition(int currentPosition) 
    {
        this.currentPosition = currentPosition;
        notifyChanges();
    }

    public String getName()
     {
        return this.name;
    }

    public void setName(String name) 
    {
        this.name = name;
        notifyChanges();
    }

    public String getColor() 
    {
        return color;
    }

    public void setColor(String color) 
    {
        this.color = color;
        notifyChanges();
    }

}
