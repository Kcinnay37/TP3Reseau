public class Main
{
    public static void main(String[] argv)
    {
        //cree tout les servers -----------------------------------------
        Server mainServer = new MainServer();
        mainServer.SetPortHost(10000);

        Server gameServer1 = new GameServer();
        gameServer1.SetPortHost(10001);
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
