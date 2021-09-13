import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.swing.Timer;

public class ItemMediator // ItemMediator is responsable to syncronize playbac and store items
{
    private final class TimerListener implements ActionListener // Listener for Timer Events
    {
        public void actionPerformed(ActionEvent ev)             // for each pattern, Play if it is a root pattern (with no father)
        {
            //System.out.println("tick");
            for(Pattern p : patterns) {
                if( p.getFathers().isEmpty() ){
                    p.play();
                }
            }
        }
    }
    private List<Pattern> patterns;
    private Vector<Sample> samples;
    private Timer timer;
    private int bpm;                                            // beats per minute is a measure for a time interval
    ActionListener timerListener = new TimerListener();

    public ItemMediator() // init 
    {
        this.bpm = 240;
        patterns = new Vector<Pattern>();
        samples = new Vector<Sample>();
        timer = new Timer(60*1000/bpm,timerListener);
        timer.start();
    }
    
    public static void connect( Item father, Item child ) // this method performs a connections between items
    {
        father.addChild(child);
        child.addFather(father);
    }
    
    public Sample addSample(Sample sample)
    {
        samples.add(sample);
        return sample;
    }
    
    public Sample addSample(String name,File file)
    {
        Sample sample = new Sample(name,file);
        samples.add(sample);
        return sample;
    }
    
    public Pattern addPattern(Pattern pattern)
    {
        patterns.add(pattern);
        return pattern;
    }
    
    public Pattern addPattern(String name,int length)
    {
        Pattern pattern = new Pattern(name,length);
        patterns.add(pattern);
        
        return pattern;
    }
    
    public void play() // Starting Timer
    {
        timer.start();
    }
    
    public void stop() // Stopping Timer, stopping all playback, rewind all patterns to 0
    {
        timer.stop();
        for(Sample sample : samples){
            sample.stop();
        }
        for(Pattern pattern : patterns){
            pattern.setCurrentPosition(0);
        }
    }

    public void setBpm(int bpm) // Setting Timer delay
    {
        this.bpm = bpm;
        timer.setDelay(60*1000/bpm);   
    }
}
