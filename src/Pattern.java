import java.io.Serializable;
public class Pattern extends Observable implements Item, Serializable {

    private Item child = null;
    private Item father = null;
    
    private Step[] pattern;
    private int currentPosition = 0;
    private int beatCount = 0;
    private String name;
    private String color;

    public Pattern(String name, Step[] pattern) {
        this.pattern = pattern;
        this.name = name;
    }
    public Pattern(String name, int length){
        this.pattern = new Step[length];
        for(int i = 0; i < pattern.length; i++)  pattern[i] = new Step(i,this,StepState.OFF);
        this.name = name;
    }
    public Pattern() {
    }

    @Override
    public Object play() {
        if(child != null){
            Object ret = new Object();
            if(pattern[currentPosition].getState() == StepState.ON) 
                ret = child.play();
            else if(child instanceof Pattern)
                ret = child.stop();
            
            beatCount++;
            if(beatCount%getBeatUnit() == 0) currentPosition = (currentPosition + 1) % pattern.length;
            return ret;
            }
            
        return null;
    }
    @Override
    public Object stop() {
        currentPosition = 0;
        return child.stop();
    }
    @Override
    public Item getChild() {
        return child;
    }
    @Override 
    public Item getFather(){
        return this.father;
    }
    @Override
    public void setFather(Item father){
        this.father = father;
    }
    @Override
    public void setChild(Item child){
        this.child = child;
    }

    public Object pause() {
        return child.stop();
    }

    public int getBeatUnit() {
        if (this.child instanceof Sample)
            return 1;
        else {
            return ((Pattern)child).getPattern().length*((Pattern)child).getBeatUnit();
        }
    }

    public void extend() {
        Step[] extended = new Step[pattern.length + 4];
        for(int i = 0 ; i< extended.length; i++){
            extended[i] = new Step(i, this, StepState.OFF);
        }
        for(int i = 0 ; i < pattern.length; i++){
            extended[i].setState(pattern[i].getState());
        }
        this.pattern = extended;
        for(Step step : pattern){
            for(Observer observer : observers)
                step.addObserver(observer);
        }
        notifyChanges();
    }
    
    public void reduce() {
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
    public Step[] getPattern() {
        return this.pattern;
    }

    public void setPattern(Step[] pattern) {
        this.pattern = pattern;
        notifyChanges();
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyChanges();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChanges();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifyChanges();
    }
    //

}
