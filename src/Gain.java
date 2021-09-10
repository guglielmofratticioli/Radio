import javax.sound.sampled.AudioFormat;

import utils.Utils;
public class Gain implements AudioProcessorModule {

    private int gain;

    public Gain() {
        this.gain = 100;
    }

    public void process(byte[] samples, AudioFormat format) throws FormatNotSupportedException {
        if (format.getSampleSizeInBits() == 8) {
            throw new FormatNotSupportedException("File Format is 8 bit");

        }
        int nchannels = format.getChannels();
        int frameSize = format.getFrameSize();
        // System.out.println(frameSize);
        // System.out.println(nchannels);
        for (int frame = 0; frame < samples.length; frame += frameSize) {
            if (nchannels == 1) {
                short val = Utils.ByteToShort(samples[frame], samples[frame + 1], !format.isBigEndian(), true);
                val = (short) (val * gain / 100);
                byte[] bytes = (Utils.ShortToByte(val));
                samples[frame] = bytes[0];
                samples[frame + 1] = bytes[1];
            } else if (nchannels == 2) {
                short lval = Utils.ByteToShort(samples[frame], samples[frame + 1], !format.isBigEndian(), true);
                short rval = Utils.ByteToShort(samples[frame + 2], samples[frame + 3], !format.isBigEndian(), true);
                lval = (short) (lval * gain / 100);
                rval = (short) (rval * gain / 100);

                byte[] lbytes = (Utils.ShortToByte(lval));
                samples[frame] = lbytes[1];
                samples[frame + 1] = lbytes[0];

                byte[] rbytes = (Utils.ShortToByte(rval));
                samples[frame + 2] = rbytes[1];
                samples[frame + 3] = rbytes[0];
            } else
                throw new FormatNotSupportedException("more than 2 audio chanels");
        }

    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public int getGain() {
        return this.gain;
    }

}