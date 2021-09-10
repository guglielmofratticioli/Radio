
import javax.swing.JFrame;

public class App extends JFrame {

    public App() {
        //this.setSize(500, 500);
        //this.setResizable(false);
        //this.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        AppBuilder appBuilder = new AppBuilder(args);
        appBuilder.build();
        System.out.println("app");
    }

}

