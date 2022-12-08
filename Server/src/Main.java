public class Main
{
    public static void main(String[] argv)
    {
        //cree tout les servers -----------------------------------------
        MainServer mainServer = new MainServer();
        mainServer.SetPortHost(10000);
        mainServer.AddValidPort("10001");
        mainServer.AddValidPort("10002");

        GameServer gameServer1 = new GameServer();
        gameServer1.SetPortsHost(10001, 10002);
        //--------------------------------------------------------------

        //les associ a des thread -----------------------------------------------------------------------------
        Thread threadMainServer = new Thread(mainServer, "threadMainServer");
        Thread threadGameServer1 = new Thread(gameServer1, "threadGameServer1");
        // ----------------------------------------------------------------------------------------------------

        // Lance tout les thread ------------------------------------------------------------------------------
        threadMainServer.start();
        threadGameServer1.start();
        //-----------------------------------------------------------------------------------------------------
    }
}
