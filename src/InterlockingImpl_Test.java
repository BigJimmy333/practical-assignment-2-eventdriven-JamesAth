import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InterlockingImpl_Test {
    
    @Test
    public void testGetSection(){
        InterlockingImpl interlockingImpl = new  InterlockingImpl();
        String trainName = interlockingImpl.getSection(1);
        assertNull(trainName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSectionException(){
        InterlockingImpl interlockingImpl = new  InterlockingImpl();
        interlockingImpl.getSection(33);
    }

}
