import java.io.Serializable;

import javax.sound.sampled.AudioFormat;
public interface AudioProcessorModule extends Serializable{

    public void process(byte[] samples, AudioFormat format);

    class FormatNotSupportedException extends RuntimeException {
        FormatNotSupportedException(String error) {
            super(error);
        }
    }
}

/*
    * class RateShifter { public File fromFile( File file ) { File outFile = new
    * File("alter.wav"); AudioInputStream inputStream; try { inputStream =
    * AudioSystem.getAudioInputStream(file); AudioFormat inputFormat =
    * inputStream.getFormat(); AudioFormat outputFormat = new AudioFormat(new
    * Encoding("PCM_SIGNED"), 96000, inputFormat.getSampleSizeInBits(),
    * inputFormat.getChannels(), inputFormat.getFrameSize(), 1024,
    * inputFormat.isBigEndian());
    * 
    * AudioInputStream out = AudioSystem.getAudioInputStream(outputFormat,
    * inputStream); AudioSystem.write(out, AudioFileFormat.Type.WAVE, outFile); }
    * catch (UnsupportedAudioFileException e) { // TODO Auto-generated catch block
    * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
    * block e.printStackTrace(); }
    * 
    * return outFile; } }
    */
