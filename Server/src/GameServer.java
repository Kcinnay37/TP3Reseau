import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends Server
{
    ServerSocket[] m_ServerHostPlayer = {null, null};

    Socket[] m_SocketHostPlayer = {null, null};

    InputStreamReader[] m_IsrHostPlayer = {null, null};

    BufferedReader[] m_ResponseHostPlayer = {null, null};

    PrintWriter[] m_RequestHostPlayer = {null, null};

    int[] ports = {0, 0};

    int currPlayer = 0;

    TicTacTo ticTacTo;

    public synchronized void Start()
    {
        try
        {
            ticTacTo = new TicTacTo();
            ticTacTo.ResetTicTacTo();

            // connection host --------------------------------------------------------------

            for(int i = 0; i < 2; i++)
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
            for(int i = 0; i < 2; i++)
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

                //va traiter la commande
                if(!line.equals(""))
                {
                    CmdReceive(line);
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

        if(ticTacTo.ChangeGridAt(sign, pos))
        {
            m_RequestHostPlayer[currPlayer].print("turn: nop\r\n");

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
            m_RequestHostPlayer[currPlayer].print("turn: yes\r\n");
            m_RequestHostPlayer[currPlayer].print(ticTacTo.GetGrid() + "\r\n");
            m_RequestHostPlayer[currPlayer].print("end\r\n");
            m_RequestHostPlayer[currPlayer].flush();

            if(currPlayer == 0)
            {
                m_RequestHostPlayer[1].print("turn: nop\r\n");
            }
            else
            {
                m_RequestHostPlayer[0].print("turn: nop\r\n");
            }
        }
    }
}
