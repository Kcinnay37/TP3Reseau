public class MainServer extends Server
{
    @Override
    public void GetPortValid(String cmd)
    {
        m_RequestHost.print("Port: 10001\r\n");
    }

    @Override
    public void Put(String cmd)
    {

    }
}
