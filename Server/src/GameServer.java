import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends Server
{
    MainServer m_MainServer = null;

    ServerSocket[] m_ServerHostPlayer = {null, null};

    Socket[] m_SocketHostPlayer = {null, null};

    InputStreamReader[] m_IsrHostPlayer = {null, null};

    BufferedReader[] m_ResponseHostPlayer = {null, null};

    PrintWriter[] m_RequestHostPlayer = {null, null};

    int[] ports = {0, 0};

    int currPlayer = 0;

    TicTacTo ticTacTo;

    int nbPlayer = 2;

    public synchronized void Start()
    {
        try
        {
            ticTacTo = new TicTacTo();
            ticTacTo.ResetTicTacTo();


            // connection 2 player --------------------------------------------------------------
            for(int i = 0; i < nbPlayer; i++)
            {
                m_ServerHostPlayer[i] = new ServerSocket(ports[i]);

                m_SocketHostPlayer[i] = m_ServerHostPlayer[i].accept();

                m_IsrHostPlayer[i] = new InputStreamReader(m_SocketHostPlayer[i].getInputStream());
                m_ResponseHostPlayer[i] = new BufferedReader(m_IsrHostPlayer[i]);
                m_RequestHostPlayer[i] = new PrintWriter(m_SocketHostPlayer[i].getOutputStream());
            }
            // -----------------------------------------------------------------------------

            OnConnect();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public synchronized boolean Update()
    {
        try
        {
            for(int i = 0; i < nbPlayer; i++)
            {
                currPlayer = i;

                // prend la commande qu'il recois -------------------------------------------
                line = "";
                String currLine = "";
                while(!(currLine = m_ResponseHostPlayer[i].readLine()).equals("end"))
                {
                    line += currLine;
                }
                // -------------------------------------------------------------------------

                // si il a recu une commende va traiter la commande
                if(!line.equals(""))
                {
                    CmdReceive(line);
                }
                if(updateLoop == false)
                {
                    break;
                }
            }
        }
        catch (IOException i)
        {
            System.out.println(i);
        }
        return updateLoop;
    }

    public synchronized void End()
    {
        SetPortActive();
        try
        {
            for(int i = 0; i < 2; i++)
            {
                m_IsrHostPlayer[i].close();
                m_RequestHostPlayer[i].close();
                m_ResponseHostPlayer[i].close();
                m_SocketHostPlayer[i].close();
                m_ServerHostPlayer[i].close();
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void CmdReceive(String cmd) {

        if (cmd.contains("Get"))
        {
            Get(cmd);
        }
        if (cmd.contains("Put"))
        {
            Put(cmd);
        }
        m_RequestHostPlayer[currPlayer].print(ticTacTo.GetGrid() + "\r\n");
        m_RequestHostPlayer[currPlayer].print("end\r\n");
        m_RequestHostPlayer[currPlayer].flush();
    }

    public void SetPortsHost(int port1, int port2)
    {
        ports[0] = port1;
        ports[1] = port2;
    }

    @Override
    public void OnConnect()
    {
        updateLoop = true;

        m_RequestHostPlayer[currPlayer].print("turn: yes\r\n");
        m_RequestHostPlayer[currPlayer].print(ticTacTo.GetGrid() + "\r\n");
        m_RequestHostPlayer[currPlayer].print("end\r\n");
        m_RequestHostPlayer[currPlayer].flush();
    }

    @Override
    public void Get(String cmd)
    {

    }

    @Override
    public void Put(String cmd)
    {
        String pos = cmd.substring(cmd.length() - 3);

        String sign = "X";
        if(currPlayer == 1)
        {
            sign = "O";
        }

        // si le joueur peux jouer le coup
        if(ticTacTo.ChangeGridAt(sign, pos))
        {
            if(ticTacTo.CheckGameIsEnd())
            {
                for(int i = 0; i < 2; i++)
                {
                    m_RequestHostPlayer[i].print("gameIsEnd\r\n");
                    if(i != currPlayer)
                    {
                        m_RequestHostPlayer[i].print(ticTacTo.GetGrid() + "\r\n");
                        m_RequestHostPlayer[i].print("end\r\n");
                        m_RequestHostPlayer[i].flush();
                    }
                }
                updateLoop = false;
                return;
            }

            //dit qu'il peut pas jouer
            m_RequestHostPlayer[currPlayer].print("turn: nop\r\n");

            // dit a l'autre joueur que c'est sont tour
            if(currPlayer == 0)
            {
                m_RequestHostPlayer[1].print("turn: yes\r\n");
                m_RequestHostPlayer[1].print(ticTacTo.GetGrid() + "\r\n");
                m_RequestHostPlayer[1].print("end\r\n");
                m_RequestHostPlayer[1].flush();
            }
            else
            {
                m_RequestHostPlayer[0].print("turn: yes\r\n");
                m_RequestHostPlayer[0].print(ticTacTo.GetGrid() + "\r\n");
                m_RequestHostPlayer[0].print("end\r\n");
                m_RequestHostPlayer[0].flush();
            }
        }
        else
        {
            // redit au joueur de jouer
            m_RequestHostPlayer[currPlayer].print("turn: yes\r\n");

            // dit a l'autre joueur de ne pas rejouer
            if(currPlayer == 0)
            {
                m_RequestHostPlayer[1].print("turn: nop\r\n");
                m_RequestHostPlayer[1].print(ticTacTo.GetGrid() + "\r\n");
                m_RequestHostPlayer[1].print("end\r\n");
                m_RequestHostPlayer[1].flush();
            }
            else
            {
                m_RequestHostPlayer[0].print("turn: nop\r\n");
                m_RequestHostPlayer[0].print(ticTacTo.GetGrid() + "\r\n");
                m_RequestHostPlayer[0].print("end\r\n");
                m_RequestHostPlayer[0].flush();
            }
        }
    }

    // va remettre les port actif dans le mainServer
    public void SetPortActive()
    {
        String port1 = String.valueOf(ports[0]);
        String port2 = String.valueOf(ports[1]);
        m_MainServer.AddValidPort(port1, port2);
    }

    public void SetMainServer(MainServer mainServer)
    {
        this.m_MainServer = mainServer;
    }
}
