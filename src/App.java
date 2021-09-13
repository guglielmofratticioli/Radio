
import javax.swing.JFrame;

public class App extends JFrame // Swing based Application
{

    public App() 
    {

    }

    public static void main(String[] args) throws Exception 
    {
        // Building App
        AppBuilder appBuilder = new AppBuilder(args);
        appBuilder.build();
    }

}

