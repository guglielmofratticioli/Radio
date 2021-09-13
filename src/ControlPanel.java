import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolTip;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Interface for visual controls
public class 
ControlPanel extends JPanel 
{
    private JButton playButton;
    private JSlider tempoSlider;
    private JButton stopButton;

    public ControlPanel(ItemMediator itemMediator)
    {

        // Action/Changes Listeners to handle controls functions 
        class PlayListener implements ActionListener
        {           
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                itemMediator.play();
            }
        }
        class StopListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                itemMediator.stop();
            }
        } 
        class TempoListener implements ChangeListener
        {

            @Override
            public void stateChanged(ChangeEvent event) 
            {
               itemMediator.setBpm(tempoSlider.getValue());      // Bpm = beats per minute is a measure for music basic time interval
            }
        }
        playButton = new JButton("play");
        playButton.addActionListener(new PlayListener());
        stopButton = new JButton("stop");
        stopButton.setBackground(Color.WHITE);
        stopButton.addActionListener(new StopListener());
        tempoSlider = new JSlider(1, 800);
        tempoSlider.addChangeListener(new TempoListener());
        tempoSlider.setBackground(Color.decode("#2c2c2c"));
        tempoSlider.setForeground(Color.darkGray);


        
        add(playButton);
        add(stopButton);
        add(tempoSlider);

        this.setSize(100,50);
        this.setVisible(true);
    }
}
