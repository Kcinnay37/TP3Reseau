public class Main
{
    public static void main(String[] argv)
    {
        //cree tout les servers -----------------------------------------
        MainServer mainServer = new MainServer();
        mainServer.SetPortHost(10000);
        mainServer.AddValidPort("10001", "10002");
        mainServer.AddValidPort("10003", "10004");
        mainServer.AddValidPort("10005", "10006");
        mainServer.AddValidPort("10007", "10008");

        GameServer gameServer1 = new GameServer();
        gameServer1.SetPortsHost(10001, 10002);
        gameServer1.SetMainServer(mainServer);

        GameServer gameServer2 = new GameServer();
        gameServer2.SetPortsHost(10003, 10004);
        gameServer2.SetMainServer(mainServer);

        GameServer gameServer3 = new GameServer();
        gameServer3.SetPortsHost(10005, 10006);
        gameServer3.SetMainServer(mainServer);

        GameServer gameServer4 = new GameServer();
        gameServer4.SetPortsHost(10007, 10008);
        gameServer4.SetMainServer(mainServer);
        //--------------------------------------------------------------

        //les associ a des thread -----------------------------------------------------------------------------
        Thread threadMainServer = new Thread(mainServer, "threadMainServer");
        Thread threadGameServer1 = new Thread(gameServer1, "threadGameServer1");
        Thread threadGameServer2 = new Thread(gameServer2, "threadGameServer2");
        Thread threadGameServer3 = new Thread(gameServer3, "threadGameServer3");
        Thread threadGameServer4 = new Thread(gameServer4, "threadGameServer4");
        // ----------------------------------------------------------------------------------------------------

        // Lance tout les thread ------------------------------------------------------------------------------
        threadMainServer.start();
        threadGameServer1.start();
        threadGameServer2.start();
        threadGameServer3.start();
        threadGameServer4.start();
        //-----------------------------------------------------------------------------------------------------
    }
}
