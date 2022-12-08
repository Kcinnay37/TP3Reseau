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
}
