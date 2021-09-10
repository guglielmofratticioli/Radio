import java.io.File;
import java.io.Serializable;
public class Sample extends Observable implements Item, Serializable {

    private final Item child = null;
    private Item father = null;
    private String name;
    private String color;
    private AudioPlayer player;
    private File file;

    public Sample(){

    }
    public Sample(String name, File file) {
        this.name = name;
        this.player = new AudioPlayer(file);
    }


    @Override
    public Object play() {
        if(this.player.isRunning()) this.player.stop();
        this.player.play();       
        return this.player;
    }
    @Override
    public Object stop() {
        this.player.stop();
        return this.player;
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
        System.out.println("Sample Item does must not have child Item !!");
    }

    public void volumeUp(){
        Gain gain = (Gain) player.getProcessor().getModuleAt(0);
        int level = gain.getGain() + 10;
        if(level > 100)
            level = 100;
        gain.setGain(level);
        notifyChanges();
    }
    public void volumeDown(){
        Gain gain = (Gain) player.getProcessor().getModuleAt(0);
        int level = gain.getGain() - 10;
        if(level < 0)
            level = 0;
        gain.setGain(level);
        notifyChanges();

    }

    // Getter/Setter

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AudioPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(AudioPlayer player) {
        this.player = player;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    //
}
