import java.io.File;

public class TicTacTo
{
    String gridTicTacTo = "000000000";

    public String GetGrid()
    {
        return gridTicTacTo;
    }

    public boolean ChangeGridAt(String sign, String move)
    {
        int x = move.charAt(0) - 48;
        int y = move.charAt(2) - 48;

        int index = (y * 3) + x;

        if(index < 0 || index >= 9)
        {
            return false;
        }

        char charAt = gridTicTacTo.charAt(index);

        if(charAt == '0')
        {
            gridTicTacTo = gridTicTacTo.substring(0, index) + sign + gridTicTacTo.substring(index + 1, gridTicTacTo.length());
            return true;
        }
        else
        {
            return false;
        }

    }

    public void ResetTicTacTo()
    {
        gridTicTacTo = "000000000";
    }

    public boolean CheckGameIsEnd()
    {
        // regarde si la grid est full -----------------------------------------------------------------------
        Boolean gridIsFull = false;
        for(int i = 0; i < gridTicTacTo.length(); i++)
        {
            if(gridTicTacTo.charAt(i) == '0')
            {
                break;
            }
            if(i == gridTicTacTo.length() - 1)
            {
                gridIsFull = true;
            }
        }
        // ---------------------------------------------------------------------------------------------------

        // regarde si il a une ligne -------------------------------------------------------------------------
        Boolean hasALine = false;

        // regarde si il a une ligne de gauche a droit
        for(int y = 0; y < 3; y++)
        {
            char firstChar = gridTicTacTo.charAt(3 * y);
            for(int x = 0; x < 3; x++)
            {
                int index = (3 * y) + x;
                if(gridTicTacTo.charAt(index) == '0' || gridTicTacTo.charAt(index) != firstChar)
                {
                    break;
                }
                if(x == 2)
                {
                    hasALine = true;
                }
            }
            if(hasALine)
            {
                break;
            }
        }

        // regarde si il a une colonne
        for(int x = 0; x < 3; x++)
        {
            char firstChar = gridTicTacTo.charAt(x);
            for(int y = 0; y < 3; y++)
            {
                int index = (3 * y) + x;
                if(gridTicTacTo.charAt(index) == '0' || gridTicTacTo.charAt(index) != firstChar)
                {
                    break;
                }
                if(y == 2)
                {
                    hasALine = true;
                }
            }
            if(hasALine)
            {
                break;
            }
        }

        // regarde si il a une ligne croiser
        if(gridTicTacTo.charAt(0) != '0' && gridTicTacTo.charAt(0) == gridTicTacTo.charAt(4) && gridTicTacTo.charAt(0) == gridTicTacTo.charAt(8))
        {
            hasALine = true;
        }
        if(gridTicTacTo.charAt(2) != '0' && gridTicTacTo.charAt(2) == gridTicTacTo.charAt(4) && gridTicTacTo.charAt(2) == gridTicTacTo.charAt(6))
        {
            hasALine = true;
        }

        if(gridIsFull || hasALine)
        {
            return true;
        }
        return false;
    }
}
