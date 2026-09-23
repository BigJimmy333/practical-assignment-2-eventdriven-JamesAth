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

}
