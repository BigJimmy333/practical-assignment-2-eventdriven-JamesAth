import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InterlockingImpl_Test {
    
    // === GET SECTION TESTS ===
    //Test if a train is in a section, should be null because no train has been added
    @Test
    public void testGetSection(){
        InterlockingImpl interlockingImpl = new  InterlockingImpl();
        String trainName = interlockingImpl.getSection(1);
        assertNull(trainName);
    }

    //Test if a section 33 exists, it should not because theres only 11 sections
    @Test(expected = IllegalArgumentException.class)
    public void testGetSectionException(){
        InterlockingImpl interlockingImpl = new  InterlockingImpl();
        interlockingImpl.getSection(33);
    }

    // === GET TRAIN TESTS ===
    //No trains have been added yet so should throw
    @Test(expected = IllegalArgumentException.class)
    public void testGetTrainEmptyList(){
        InterlockingImpl interlockingImpl = new  InterlockingImpl();
        interlockingImpl.getTrain("Train 33");
    }

    // === ADD TRAIN TESTS ===
    //Test when adding a train, it returns its place and its name 
    @Test
    public void testAddTrain(){
        InterlockingImpl interlockingImpl = new  InterlockingImpl();
        interlockingImpl.addTrain("Train 1", 3, 11);
        assertEquals("Train 1", interlockingImpl.getSection(3));
        assertEquals(3, interlockingImpl.getTrain("Train 1"));
    }

    //Test if adding the same train twice throws an exception 
    @Test(expected = IllegalArgumentException.class)
    public void testAddSameTrain(){
        InterlockingImpl interlockingImpl = new  InterlockingImpl();
        interlockingImpl.addTrain("Train 1", 3, 11);
        interlockingImpl.addTrain("Train 1", 3, 11);
    }

    //Test if adding a train to an already occupied section throws an exception 
    @Test(expected = IllegalStateException.class)
    public void testAddTrainToSamePlace(){
        InterlockingImpl interlockingImpl = new  InterlockingImpl();
        interlockingImpl.addTrain("Train 1", 3, 11);
        interlockingImpl.addTrain("Train 2", 3, 11);
    }

    //Test if having an incorrect entry track throws an exception
    @Test(expected = IllegalArgumentException.class)
    public void testWrongEntryTrack(){
        InterlockingImpl interlockingImpl = new  InterlockingImpl();
        interlockingImpl.addTrain("Train 1", 5, 11);
    }

    //Test if a correct start and destination throws an error if they don't connect
    @Test(expected = IllegalArgumentException.class)
    public void testWrongPath(){
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("Train 1", 1, 11);
    }

    // === MOVE TRAIN TESTS ===
    //Tests the flow of the move train function from start to finish
    @Test 
    public void moveTrain(){
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("Train 1", 3, 11);

        String[] trainNames = {"Train 1"};

        //After one move should go 3-7
        int moves = interlockingImpl.moveTrains(trainNames);
        assertEquals(7, interlockingImpl.getTrain("Train 1"));
        //One move should occur
        assertEquals(1, moves);

        //After two moves should go 7=11
        moves = interlockingImpl.moveTrains(trainNames);
        assertEquals(11, interlockingImpl.getTrain("Train 1"));
        //One move should occur
        assertEquals(1, moves);

        //After three moves should leave
        moves = interlockingImpl.moveTrains(trainNames);
        assertEquals(-1, interlockingImpl.getTrain("Train 1"));
        //One move should occur
        assertEquals(1, moves);
    }

    //Test if a train can move into an occupied section 
    @Test 
    public void moveBlockedTrain(){
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("Train 1", 3, 11);

        String[] train1 = {"Train 1"};
        int moves = interlockingImpl.moveTrains(train1);
        assertEquals(7, interlockingImpl.getTrain("Train 1"));
        assertEquals(1, moves);

        interlockingImpl.addTrain("Train 2", 3, 11);
        String[] train2 = {"Train 2"};
        moves = interlockingImpl.moveTrains(train2);
        assertEquals(0, moves);

        assertEquals(7, interlockingImpl.getTrain("Train 1"));
        assertEquals(3, interlockingImpl.getTrain("Train 2"));
    }

    //Priority testing
    //A freight train should stay at 4 when there is a train in 1
    @Test
    public void testJunction(){
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("Train 1", 4, 3);
        interlockingImpl.addTrain("Train 2", 1, 8);
        
        String[] train1 = {"Train 1"};
        int moves = interlockingImpl.moveTrains(train1);
        assertEquals(4, interlockingImpl.getTrain("Train 1"));
        assertEquals(0, moves);
    }

    //Priority testing
    //If sections 1 and 6 are empty, a freight train should be able to move
    @Test 
    public void testFreightTrainMove(){
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("Train 1", 4, 3);

        String[] train1 = {"Train 1"};
        int moves = interlockingImpl.moveTrains(train1);
        assertEquals(3, interlockingImpl.getTrain("Train 1"));
        assertEquals(1, moves);
    }


}
