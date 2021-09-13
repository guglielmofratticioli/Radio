import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Sample extends Observable implements Item, Serializable 
{

    private final Item children = null;
    private ArrayList<Item> fathers = new ArrayList<Item>();
    private String name;
    private String color;
    private AudioPlayer player;
    private File file;

    public Sample() //init
    {

    }
    public Sample(String name, File file) // new Audioplayer from File 
    {
        this.name = name;
        this.player = new AudioPlayer(file);
    }


    @Override
    public void play() // Stops the Audioplayer if it is playing before playing
    {
        if(this.player.isRunning()) this.player.stop();
        this.player.play();       
    }
    @Override
    public void stop() 
    {
        this.player.stop();
    }
    @Override
    public void addFather(Item father) 
    {
        fathers.add(father);
    }
    @Override
    public void addChild(Item child) 
    {
        return;
    }
    @Override
    public void removeFather(Item father) 
    {
        fathers.remove(father);
    }
    @Override
    public void removeChild(Item child) 
    {
        return;
    }
    

    public void volumeUp() // raise volume by 10%
    {
        Gain gain = (Gain) player.getProcessor().getModuleAt(0);
        int level = gain.getGain() + 10;
        if(level > 100)
            level = 100;
        gain.setGain(level);
        notifyChanges();
    }
    public void volumeDown() // lower volume by 10%
    {
        Gain gain = (Gain) player.getProcessor().getModuleAt(0);
        int level = gain.getGain() - 10;
        if(level < 0)
            level = 0;
        gain.setGain(level);
        notifyChanges();

    }

    // Getter/Setter

    public String getName() 
    {
        return this.name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public AudioPlayer getPlayer() 
    {
        return this.player;
    }

    public void setPlayer(AudioPlayer player) 
    {
        this.player = player;
    }
    public String getColor() 
    {
        return color;
    }

    public void setColor(String color) 
    {
        this.color = color;
    }

    public File getFile() 
    {
        return this.file;
    }

    public void setFile(File file) 
    {
        this.file = file;
    }
    //

}
