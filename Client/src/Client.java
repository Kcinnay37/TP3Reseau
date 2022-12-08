import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Set;

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

    Boolean playerTurn = false;

    String currGrid = "000000000";

    public synchronized void Start()
    {
        try
        {
            // Connection au server --------------------------------------------------------
            m_Socket = new Socket(address, port);

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
                DisplayGrid();
                if(playerTurn)
                {
                    System.out.print("entrer le movement x,y : ");

                    // s'assure que le inputStream n'est pas null
                    if(this.in == null)
                        this.in = new DataInputStream(System.in);
                    cmd = this.in.readLine();
                    cmd = "Put:" + cmd;
                }
                else
                {
                    System.out.println("en attente de l'autre joueur");
                }
            }
            else
            {
                cmd = "Get";
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
        if(cmd.contains("Get"))
        {
            m_Request.print("Get\r\n");
        }
        if(cmd.contains("Put:"))
        {
            m_Request.print(cmd + "\r\n");
            playerTurn = false;
        }

        m_Request.print("end\r\n");
        m_Request.flush();
    }

    public void CmdReceive(String cmd)
    {
        if(cmd.contains("port:"))
        {
            String port = cmd.substring(6, cmd.length());
            SetAddressPort(address, Integer.parseInt(port));
        }
        else if(cmd.contains("turn: "))
        {
            String turn = cmd.substring(6, 9);
            if(turn.equals("yes"))
            {
                playerTurn = true;
            }
            else
            {
                playerTurn = false;
            }
            currGrid = cmd.substring(cmd.length() - 9);
        }
    }

    public void DisplayGrid()
    {
        int step = 2;
        for(int i = 0; i < 9; i++)
        {
            if(i % 3 == 0 && i != 0)
            {
                step += 3;
                System.out.println("");
                for(int j = 0; j < 3; j++)
                {
                    System.out.print("─");
                    if(j != 2)
                    {
                        System.out.print("┼");
                    }
                }
                System.out.println("");
            }
            char currChar = currGrid.charAt(i);
            if(currChar != '0')
            {
                System.out.print(currChar);
            }
            else
            {
                System.out.print(" ");
            }
            if(i % step != 0 || i == 0)
            {
                System.out.print('│');
            }
        }
        System.out.println("");
    }

    public void SetAddressPort(String address, int port)
    {
        this.address = address;
        this.port = port;
    }
}
