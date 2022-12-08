import java.util.ArrayList;

public class MainServer extends Server
{
    ArrayList<String> validPort = new ArrayList<String>();
    ArrayList<String> invalidePort = new ArrayList<String>();

    @Override
    public void OnConnect()
    {
        updateLoop = false;
    }

    @Override
    public void Get(String cmd)
    {
        // prend le port et l'enleve des ports valid
        String port = validPort.get(0);
        invalidePort.add(validPort.get(0));
        validPort.remove(0);

        m_RequestHost.print("port: " + port + "\r\n");
    }

    @Override
    public void Put(String cmd)
    {

    }

    public void AddValidPort(String port)
    {
        invalidePort.remove(port);
        validPort.add(port);
    }
}
