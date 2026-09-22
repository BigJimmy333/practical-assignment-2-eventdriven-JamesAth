import java.util.HashMap;
import java.util.Map;

public class InterlockingImpl implements Interlocking {

    //Create a value for each place to track train positions
    private Map<Integer, String> places = new HashMap<>();

    public InterlockingImpl() {
        //11 because theres 11 places in the petri net
        for (int i = 1; i <= 11; i++){
            places.put(i, null);
        }
    }

    @Override
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection) throws IllegalArgumentException, IllegalStateException {
        
    }

    @Override
    public int moveTrains(String[] trainNames) throws IllegalArgumentException {
        return 0;
        
    }

    @Override
    public String getSection(int trackSection) throws IllegalArgumentException {
        return null;
       
    }

    @Override
    public int getTrain(String trainName) throws IllegalArgumentException {
        return 0;
        
    }
    
}
