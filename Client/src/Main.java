import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] argv)
    {
        //cree tout les clients -----------------------------------------
        Client client = new Client();
        client.SetAddressPort("192.168.0.186", 10000);
        //-------------------------------------------------------------------------

        //les associ a des thread -----------------------------------------------------------------------------
        Thread threadClient = new Thread(client, "threadClient");
        // ----------------------------------------------------------------------------------------------------

        // Lance tout les thread ------------------------------------------------------------------------------
        threadClient.start();
        //-----------------------------------------------------------------------------------------------------
    }
}