import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterlockingImpl implements Interlocking {

    //Create a value for each place to track train positions
    private Map<Integer, String> places = new HashMap<>();
    //Keeps track of all the trains that have entered
    private List<String> trainsEntered = new ArrayList<>();

    public InterlockingImpl() {
        //11 because theres 11 places in the petri net
        for (int i = 1; i <= 11; i++){
            places.put(i, null);
        }
    }

    @Override
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection) throws IllegalArgumentException, IllegalStateException {
        //If a name is in use it can't be used again
        //May need to be changed if the same train can come back after leaving
        if(trainsEntered.contains(trainName)){
            throw new IllegalArgumentException("Train name: " + trainName + " Has already entered the system");
        }

        //Get the train and check if its occupying a place
        String train = places.get(entryTrackSection);

        if(train != null){
            throw new IllegalStateException("Place: " + entryTrackSection + " Is already occupied");
        }

        //If no exception is thrown, add the train into the place and the trains entered list
        places.put(entryTrackSection, trainName);
        trainsEntered.add(trainName);
        
    }

    @Override
    public int moveTrains(String[] trainNames) throws IllegalArgumentException {
        return 0;
        
    }

    //Returns the trainName
    @Override
    public String getSection(int trackSection) throws IllegalArgumentException {

        //If the place is not in the map then throw an error
        if (!places.containsKey(trackSection)) {
            throw new IllegalArgumentException("Track section " + trackSection + " does not exist.");
        }

        //Otherwise return the train name
        String trainName = places.get(trackSection);
        return trainName;
    }

    //Returns the place 
    @Override
    public int getTrain(String trainName) throws IllegalArgumentException {
        
        //If the train has never entered the system throw an error
        if(!trainsEntered.contains(trainName)){
            throw new IllegalArgumentException("Train name:  " + trainName + " has not entered the railway.");
        }
        
        //If the train is in the system return where it is
        //If not return -1 because the last case is the train has entered and left

        //Key = place
        //Value = train name
        for (Map.Entry<Integer, String> entry : places.entrySet()) {
            if(trainName.equals(entry.getValue())){
                return entry.getKey();
            }
        }

        return -1;
    }
    
}
