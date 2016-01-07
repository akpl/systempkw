package pkw;

/**
 * Created by Elimas on 2016-01-07.
 */
public class Counter
{
    private int i = -1;

    public int getCurrent() {
        return i;
    }

    public int getNext() {
        i++;
        return i;
    }
}
