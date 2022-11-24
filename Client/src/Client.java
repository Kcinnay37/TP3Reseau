import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Client extends MyRunnable
{
    private ServerSocket m_ServerSocket = null;
    private Socket m_Socket = null;

    private InputStreamReader isr = null;
    private DataInputStream in = null;

    private DataOutputStream m_Out = null;
    private BufferedReader m_Response = null;
    private PrintWriter m_Request = null;

    String address = "";
    int port = 0;

    String responseLine = "";
    String cmd = "";

    Boolean getInput = false;

    Boolean update = true;

    public synchronized void Start()
    {
        try
        {
            System.out.println("test");
            // Connection au server --------------------------------------------------------
            m_Socket = new Socket(address, port);
            System.out.println("Client Connecter");

            isr = new InputStreamReader(this.m_Socket.getInputStream());
            m_Response = new BufferedReader(this.isr);
            m_Request = new PrintWriter(this.m_Socket.getOutputStream());
            // ----------------------------------------------------------------------------

            update = true;
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public synchronized boolean Update()
    {
        try
        {
            // prend les input du client -------------------------------------------
            if(getInput)
            {
                cmd = "";
                System.out.print("entrer le type de commande : ");
                if(this.in == null)
                    this.in = new DataInputStream(System.in);
                cmd = this.in.readLine();
            }
            else
            {
                cmd = "GetGamePort";
                getInput = true;
                update = false;
            }
            // ---------------------------------------------------------------------

            //envoie la commande au server connecter
            CmdSend(cmd);

            // reponse -------------------------------------------------------------
            String responseLine = "";
            String currLine = "";
            while(!(currLine = m_Response.readLine()).equals("end")) {
                responseLine += currLine;
            }
            // ---------------------------------------------------------------------

            CmdReceive(responseLine);

        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        return update;
    }

    public synchronized void End()
    {
        try
        {
            m_Socket.close();
            if(this.in != null)
                this.in.close();
            isr.close();
            m_Response.close();
            m_Request.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public void CmdSend(String cmd)
    {
        switch (cmd)
        {
            case "GetGamePort":
                m_Request.print("GetPortValid\r\n");
                break;
        }

        m_Request.print("end\r\n");
        m_Request.flush();
    }

    public void CmdReceive(String cmd)
    {
        if(cmd.contains("Port:"))
        {
            String port = cmd.substring(6, cmd.length());
            System.out.println(port);
            SetAddressPort(address, Integer.parseInt(port));
        }
    }

    public void SetAddressPort(String address, int port)
    {
        this.address = address;
        this.port = port;
    }
}
