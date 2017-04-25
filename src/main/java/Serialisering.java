import java.io.Serializable;

/**
 * Created by LarsMyrup on 24/04/2017.
 */
public class Serialisering implements Serializable {

    int counter = 0;

    public int getCounter() {
        counter++;
        return counter;
    }
}
