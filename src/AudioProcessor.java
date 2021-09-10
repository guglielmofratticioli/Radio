import java.io.Serializable;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
public class AudioProcessor implements Serializable {
    private Vector<AudioProcessorModule> modules;

    public AudioProcessor(boolean volume) {
        this.modules = new Vector<AudioProcessorModule>();

        if (volume) {
            Gain gain = new Gain();
            modules.add(gain);
        }

    }

    public void process(byte[] samples, AudioFormat format) {
        for (AudioProcessorModule module : modules) {
            module.process(samples, format);
        }
    }

    public AudioProcessorModule getModuleAt( int index ){
        return modules.elementAt(index);
    }
}
