import java.io.IOException;
import java.util.ArrayList;

public class MainServer extends Server
{
    ArrayList<String> validPort = new ArrayList<String>();
    ArrayList<String> invalidePort = new ArrayList<String>();

    Boolean alreadyUse = false;

    @Override
    public void OnConnect()
    {
        updateLoop = false;
    }

    @Override
    public void Get(String cmd)
    {
        // prend un port valide le met invalide et le retourn
        String port = validPort.get(0);
        invalidePort.add(validPort.get(0));
        validPort.remove(0);

        m_RequestHost.print("port: " + port + "\r\n");
    }

    @Override
    public void Put(String cmd)
    {

    }

    public void AddValidPort(String port1, String port2)
    {
        while(alreadyUse)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }
        alreadyUse = true;
        invalidePort.remove(port1);
        invalidePort.remove(port2);
        validPort.add(port1);
        validPort.add(port2);
        alreadyUse = false;
    }
}
