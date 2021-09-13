import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import utils.Utils;

// this class uses the javax.sound to playback audio data
// AudioProcessor let you implements some DSP on the audioStram 

public class AudioPlayer implements Serializable 
{

    private transient AudioFormat audioFormat;                  // holds the format of audio File
    private transient AudioInputStream audioInputStream;        // a dataStream where the audio File data will be placed
    private transient SourceDataLine sourceDataLine;            // reference to a line of the System Sound mixer
    private boolean running;                                    // if is playing audio
    private boolean close;                                      // if the thread is closed
    private File file;
    private transient PlayThread playThread;                    // playback function is handled by a thread
    private AudioProcessor processor;                           // can process audio data

    public AudioPlayer(File file) 
    {
        load(file);
        this.playThread = new PlayThread(); 
        playThread.start();
    }

    public void load(File file) 
    {
        try {
            this.running = false;                               // stopping on init
            this.close = false;
            this.file = file;
            this.processor = new AudioProcessor(true);          
            audioInputStream = AudioSystem.getAudioInputStream(file);                // get an audio Stream from file format
            audioInputStream.mark(Integer.MAX_VALUE);                                // required to rewind the position AudioStream to start   
            audioFormat = audioInputStream.getFormat();                              // new  audioFormat
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);  
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);     //a dataline from dataline_info from audioformat
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            //System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void play() 
    {
        this.playThread.triggerPlayBack();
    }

    public void stop() 
    {
        this.playThread.triggerStop();
    }

    // Getter/Setter
    public AudioFormat getAudioFormat() 
    {
        return this.audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat) 
    {
        this.audioFormat = audioFormat;
    }

    public AudioInputStream getAudioInputStream() 
    {
        return this.audioInputStream;
    }

    public void setAudioInputStream(AudioInputStream audioInputStream) 
    {
        this.audioInputStream = audioInputStream;
    }

    public SourceDataLine getSourceDataLine() 
    {
        return this.sourceDataLine;
    }

    public void setSourceDataLine(SourceDataLine sourceDataLine) 
    {
        this.sourceDataLine = sourceDataLine;
    }

    public boolean isRunning() 
    {
        return this.running;
    }

    public boolean isClose() 
    {
        return this.close;
    }

    public File getFile() 
    {
        return this.file;
    }

    public PlayThread getPlayThread() 
    {
        return this.playThread;
    }

    public AudioProcessor getProcessor() 
    {
        return this.processor;
    }

    public void setProcessor(AudioProcessor processor) 
    {
        this.processor = processor;
    }
    //

    class PlayThread extends Thread  // a thread which handles the playback of the AudioStream
    {

        private byte tempBuffer[] = new byte[10000];        // audio will be extracted in chunks from the AudioStream to this buffer

        public PlayThread() 
        {
            try {
                sourceDataLine.open(audioFormat);           // opens the mixer line
                sourceDataLine.start();
                setPriority(10);                            // maximum thread priority
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void run() 
        {
            while (!close)                          // while thread is not closed
            {                                  
                while (!running) {                  // wait until audio should be played 
                    Utils.handleProcedure(this::wait, false);
                }
                int nread = 0;
                // read nread bytes in tempBuffer from audioStream until it reads -1 bytes ( the whole stream has been read )
                try 
                {
                    while ((nread = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1 && running) {
                        if (nread > 0) {
                            processor.process(tempBuffer, audioFormat);         // Processing Buffer
                            sourceDataLine.write(tempBuffer, 0, nread);         // play the processed audio 
                        }
                    }

                    running = false;   // go to waiting state on playback end

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        public synchronized void triggerPlayBack() 
        {
            running = false;                // stop if it is playing
            sourceDataLine.flush();         // flush previous audio data in dataline queue 
            rewind();                       // rewind to 0
            running = true;
            notify();                       // restarting playback
        }

        public void triggerStop() 
        {
            running = false;                // stop & flush
            sourceDataLine.flush();
        }

        public void close() 
        {
            close = true;                   // should be called on thread close
        }

        public void rewind() 
        {
            try {
                audioInputStream.reset();   // reset to marker
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
