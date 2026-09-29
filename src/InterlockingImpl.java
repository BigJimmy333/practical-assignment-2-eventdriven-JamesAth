import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 
 * InterlockingImpl
 * This class simulates a petri net of trains entering a system from two different directions
 * and navigating it through different sections and types of track to get to there destination
 * and avoiding collisions 
 */
public class InterlockingImpl implements Interlocking {

    //Create a value for each place to track train positions
    private Map<Integer, String> places = new HashMap<>();
    //Keeps track of all the trains that have entered
    private List<String> trainsEntered = new ArrayList<>();
    //Keeps track of where a specific train can move
    private Map<String, List<Integer>> routes = new HashMap<>();

    //The paths a train can take
    private Map<Integer, List<Integer>> southPath = new HashMap<>();
    private Map<Integer, List<Integer>> northPath = new HashMap<>();

    
    /**
     * Constructs the sections that a train can take and 
     * the paths that a train heading north and south can take
     */
    public InterlockingImpl() {
        //11 because theres 11 places in the petri net
        for (int i = 1; i <= 11; i++){
            places.put(i, null);
        }

        //The south paths that can be taken 
        southPath.put(1, List.of(5));
        southPath.put(5, List.of(8,9));
        southPath.put(3, List.of(4,7));
        southPath.put(7, List.of(11));

        //The north paths that can be taken
        northPath.put(4, List.of(3));
        northPath.put(9, List.of(6));
        northPath.put(6, List.of(2));
        northPath.put(10, List.of(6));
        northPath.put(11, List.of(7));
        northPath.put(7, List.of(3));
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

        //Route the train should take
        List<Integer> trainRoute;
        //South entries
        if(entryTrackSection == 1 || entryTrackSection == 3){
            trainRoute = getPath(entryTrackSection, destinationTrackSection, southPath);
        }

        //North entries
        else if (entryTrackSection == 4 || entryTrackSection == 9 || entryTrackSection == 10 || entryTrackSection == 11) {
            trainRoute = getPath(entryTrackSection, destinationTrackSection, northPath);
        }
        else {
            throw new IllegalArgumentException("Place : " + entryTrackSection + " is not a valid starting track");
        }

        //If there is no valid path, throw an exception 
        if(trainRoute == null){
            throw new IllegalArgumentException("There is no path from: " + entryTrackSection + "To: " + destinationTrackSection);
        }

        //If no exception is thrown, add the train into the place and the trains entered list
        places.put(entryTrackSection, trainName);
        trainsEntered.add(trainName);
        routes.put(trainName, trainRoute);
        
    }

    @Override
    public int moveTrains(String[] trainNames) throws IllegalArgumentException {
        //Keep track of the amount of trains that have moved
        int trainsMoved = 0;
        //Keep track of where each train intends to go
        Map<String, Integer> trainsDestination = new HashMap<>();

        //Loop through the trains to move, if its not in the system throw error
        for(String name : trainNames){
            int section = getTrain(name);
            if(section == -1){
                throw new IllegalArgumentException(name + " is not in the system");
            }
            //If it is in the system get that trains route
            List<Integer> trainRoute = routes.get(name);
            //Get the last section of the route and see if its at the end
            int lastSection = trainRoute.get(trainRoute.size() -1);
            //If it is the end remove it from the place 
            //Removing its route is not necessary unless you use the same train name
            if(section == lastSection){
               trainsDestination.put(name, -1);
            }

            else {
                //Get where you are currently in the list index wise
                int currentIndex = trainRoute.indexOf(section);
                //Get the place where you need to go next
                int nextSection = trainRoute.get(currentIndex + 1);

                //Need to obtain if the junction is empty so passengers can go first
                //If a train is going from 3 to 4 or the opposite and 
                //If a train is either in section 1 or 6 its blocked
                boolean junctionBlocked = (section == 3 && nextSection == 4 || section == 4 && nextSection == 3)
                && (getSection(1) != null || getSection(6) != null);

                //If there is no train in the next section and the junction is not blocked
                if (!junctionBlocked){
                    trainsDestination.put(name, nextSection);
                }

                
            }
        }

        // === Get multiple destinations ===
        //Track if multiple trains want the same destination 
        Map<Integer, Integer> sectionCounter = new HashMap<>();
        for (Integer section : trainsDestination.values()){
            //If its -1 they can leave that does not matter
            if (section != -1){
                //If the section is already in the map add 1 to it
                if(sectionCounter.containsKey(section)){
                    sectionCounter.put(section, sectionCounter.get(section) + 1);
                //If not add the section and the counter 1
                } else {
                    sectionCounter.put(section, 1);
                }
            }
        }

        // === Remove trains with multiple destinations ===
        //The list of trains to remove because they have the same destination as another train
        List<String> removeTrains = new ArrayList<>(); 
        //If the section has more than 1 value (two or more trains going there)
        //Add it to the list to remove
        for(Map.Entry<String, Integer> entry : trainsDestination.entrySet()){
            int section = entry.getValue();
            if(section != -1 && sectionCounter.get(section) > 1){
                removeTrains.add(entry.getKey());
            }
        }

        //Remove trains heading to the same destination
        for(String train : removeTrains){
            trainsDestination.remove(train);
        }

        // === Move the trains ===
        //Loop through the trainNames
        for(String trainName : trainNames) {
            //If two or more trains are going to the same destination we do not move any
            if(trainsDestination.containsKey(trainName)){
                int currentSection = getTrain(trainName);
                int trainDestination = trainsDestination.get(trainName);

                //If leaving the system
                if (trainDestination == -1){
                    places.put(currentSection, null);
                    routes.remove(trainName);
                    trainsMoved++;
                }
                //If the destination is free
                else if(places.get(trainDestination) == null) {
                    places.put(currentSection, null);
                    places.put(trainDestination, trainName);
                    trainsMoved++;
                }
            }
        }

        return trainsMoved;
        
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

    /**
     * A bfs to find if a given entry section can make it to the destination
     * 
     * @param   entryTrackSection The id number of the track section that the train is entering into.
     * @param   destinationTrackSection The id number of the track section that the train should exit from.
     * @param   path The directional map of where the train is going, either north or south.
     * @return  The path that the train needs to go from the entry to the exit
     */
    private List<Integer> getPath(int entryTrackSection, int destinationTrackSection, Map<Integer, List<Integer>> path){
        Queue<Integer> queue = new LinkedList<Integer>();

        List<Integer> visited = new ArrayList<Integer>();

        //Keeps track of the path of the sections
        Map<Integer, Integer> route = new HashMap<>();

        queue.add(entryTrackSection);
        visited.add(entryTrackSection);

        //BFS loop
        while(!queue.isEmpty()){
            Integer section = queue.poll();

            //If a route is found create the path and return it
            if(section.equals(destinationTrackSection)){
                //Build the path in this variable
                ArrayList<Integer> entryToDestination = new ArrayList<>();
                //The destination, used to walk backwards to get to the start
                Integer destination = destinationTrackSection;

                while(destination != null){
                    //Add to the path
                    entryToDestination.add(destination);
                    //Use the key of where you are and get the value of where you can go
                    destination = route.get(destination);
                }

                //Reverse because its build backwards
                Collections.reverse(entryToDestination);
                return entryToDestination;
            }

            //If not keep going through the path to see if a destination can be found
            //Get the list of sections you can explore from the current section
            List<Integer> nextSections = path.get(section);
            //If it leads somewhere explore it
            if (nextSections != null){
                //If it leads multiple places go one at a time
                for(Integer adjacent : nextSections){
                    //If we have not explored it
                    if(!visited.contains(adjacent)){
                        //Add the section as seen
                        visited.add(adjacent);
                        //Add the section and where it could be visited from 
                        route.put(adjacent, section);
                        //Add the section to the queue
                        queue.add(adjacent);
                    }
                }
            }
        }
        return null;
    }
    
}
