import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
public class ControlPanel extends JPanel {
    private JButton playButton;
    private JButton stopButton;
    public ControlPanel(ItemMediator itemMediator){
        class PlayListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMediator.play();
            }
        }
        class StopListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                itemMediator.stop();
            }
        } 
        playButton = new JButton("play");
        playButton.addActionListener(new PlayListener());
        stopButton = new JButton("stop");
        stopButton.addActionListener(new StopListener());
        add(playButton);
        add(stopButton);
        this.setSize(100,50);
        this.setVisible(true);
    }
}
