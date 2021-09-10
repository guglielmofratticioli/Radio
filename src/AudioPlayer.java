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
public class AudioPlayer implements Serializable {

    private transient AudioFormat audioFormat;
    private transient AudioInputStream audioInputStream;
    private transient SourceDataLine sourceDataLine;
    private boolean running;
    private boolean close;
    private File file;
    private transient PlayThread playThread;
    private AudioProcessor processor;

    public AudioPlayer(File file) {
        load(file);
        this.playThread = new PlayThread(); // play as a Thread to be stopped
        playThread.start();
    }

    public void load(File file) {
        try {
            this.running = false;
            this.close = false;
            this.file = file;
            this.processor = new AudioProcessor(true);
            audioInputStream = AudioSystem.getAudioInputStream(file); // adapting to file format
            audioInputStream.mark(Integer.MAX_VALUE);
            audioFormat = audioInputStream.getFormat(); // getting a SourceDataLine
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
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

    public void play() {
        this.playThread.triggerPlayBack();
    }

    public void stop() {
        this.playThread.triggerStop();
    }

    // Getter/Setter
    public AudioFormat getAudioFormat() {
        return this.audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    public AudioInputStream getAudioInputStream() {
        return this.audioInputStream;
    }

    public void setAudioInputStream(AudioInputStream audioInputStream) {
        this.audioInputStream = audioInputStream;
    }

    public SourceDataLine getSourceDataLine() {
        return this.sourceDataLine;
    }

    public void setSourceDataLine(SourceDataLine sourceDataLine) {
        this.sourceDataLine = sourceDataLine;
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean getRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isClose() {
        return this.close;
    }

    public boolean getClose() {
        return this.close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public File getFile() {
        return this.file;
    }

    public PlayThread getPlayThread() {
        return this.playThread;
    }

    public AudioProcessor getProcessor() {
        return this.processor;
    }

    public void setProcessor(AudioProcessor processor) {
        this.processor = processor;
    }
    //

    class PlayThread extends Thread {

        private byte tempBuffer[] = new byte[10000];

        public PlayThread() {
            try {
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();
                setPriority(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void run() {
            while (!close) {
                while (!running) {
                    Utils.handleProcedure(this::wait, false);
                }

                int nread;

                try {
                    while ((nread = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1 && running) {
                        if (nread > 0) {
                            // Processing Buffer
                            processor.process(tempBuffer, audioFormat);
                            sourceDataLine.write(tempBuffer, 0, nread);
                        }
                    }

                    running = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        public synchronized void triggerPlayBack() {
            running = false;
            sourceDataLine.flush();
            rewind();
            running = true;
            notify();
        }

        public void triggerStop() {
            running = false;
            sourceDataLine.flush();
        }

        public void close() {
            close = true;
        }

        public void rewind() {
            try {
                audioInputStream.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
