import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.swing.Timer;
public class ItemMediator{
    private final class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            //System.out.println("tick");
            for(Pattern p : patterns) {
                if( p.getFather() == null ){
                    p.play();
                }
            }
        }
    }
    private List<Pattern> patterns;
    private Vector<Sample> samples;
    private Timer timer;
    private int bpm;
    ActionListener timerListener = new TimerListener();

    public ItemMediator(){
        this.bpm = 60;
        patterns = new Vector<Pattern>();
        samples = new Vector<Sample>();
        timer = new Timer(bpm*500/120,timerListener);
        timer.start();
    }
    
    public void connect( Item father, Item child ) {
        father.setChild(child);
        child.setFather(father);
    }
    
    public Sample addSample(Sample sample){
        samples.add(sample);
        return sample;
    }
    public Sample addSample(String name,File file){
        Sample sample = new Sample(name,file);
        samples.add(sample);
        return sample;
    }
    
    public Pattern addPattern(Pattern pattern){
        patterns.add(pattern);
        return pattern;
    }
    public Pattern addPattern(String name,int length){
        Pattern pattern = new Pattern(name,length);
        patterns.add(pattern);
        
        return pattern;
    }
    public void play(){
        timer.start();
    }
    public void stop(){
        timer.stop();
        for(Sample sample : samples){
            sample.stop();
        }
        for(Pattern pattern : patterns){
            pattern.setCurrentPosition(0);
        }
    }

    public void setBpm(int bpm){
        this.bpm = bpm;
        timer.setDelay(bpm*1000/60);   
    }
}
