import java.io.Serializable;

enum StepState // States for steps
{
    ON, OFF
}

public class Step extends Observable implements Serializable
{

    private int index;
    private Pattern father;
    private StepState state;
    public Step(){

    }
    public Step(int index, Pattern father, StepState state) // a Step has an index, belongs to a pattern and holds a state
    {
        this.index = index;
        this.father = father;
        this.state = state;
    }
    
    //Getter/Setter
    public int getIndex() 
    {
        return this.index;
    }

    public void setIndex(int index) 
    {
        this.index = index;
    }

    public Pattern getFather() 
    {
        return this.father;
    }

    public void setFather(Pattern father) 
    {
        this.father = father;
    }

    public StepState getState() {
        return this.state;
    }

    public void setState(StepState state) 
    {
        this.state = state;
        notifyChanges(this);
    }
    //
    // action performed when a step cell is pressed on a pattern
    public StepState toggle() throws UnvalidStateException 
    {
        switch (state) {
            case ON:{
                state = StepState.OFF;
                notifyChanges();
                break;
            }
                
            case OFF:{
                state = StepState.ON;
                notifyChanges();
                break;
            }
                
            default:
                throw new UnvalidStateException("Unvalid Step State of step " + Integer.toString(index)
                        + " children of Pattern" + getFather().getName());
        }
        return state;
        
    }
    
    class UnvalidStateException extends Exception 
    {
        public UnvalidStateException(String message) {
            super(message);
        }

        public UnvalidStateException(String message, Throwable cause) 
        {
            super(message, cause);
        }
    }

}
