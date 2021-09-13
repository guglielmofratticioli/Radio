import java.io.Serializable;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;

public class AudioProcessor implements Serializable  // audioProcessor process audio datas with DSP modules 
{
    private Vector<AudioProcessorModule> modules;

    public AudioProcessor(boolean volume) 
    {
        this.modules = new Vector<AudioProcessorModule>();
        if (volume) {
            Gain gain = new Gain();     // this module changes audio volume
            modules.add(gain);
        }

    }

    public void process(byte[] samples, AudioFormat format) 
    {
        for (AudioProcessorModule module : modules) {
            module.process(samples, format);
        }
    }

    public AudioProcessorModule getModuleAt( int index )
    {
        return modules.elementAt(index);
    }
}
