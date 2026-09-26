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
        interlockingImpl.addTrain("Train 1", 3, 0);
        assertEquals("Train 1", interlockingImpl.getSection(3));
        assertEquals(3, interlockingImpl.getTrain("Train 1"));
    }

}
