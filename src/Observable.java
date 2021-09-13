import java.util.ArrayList;
import java.util.List;
public class Observable 
{

    protected List<Observer> observers = new ArrayList<Observer>();

    public void addObserver(Observer observer)
    {
        this.observers.add(observer);
    }
    
    protected void notifyChanges()
    {
        for(Observer observer : this.observers) {
            observer.update();
            System.out.println("callback done");
        }
    }

    protected void notifyChanges(Object object)
    {
        for( Observer observer : this.observers) 
            observer.update(object);
    }

}
