package robot;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReserveTest {
    private Reserve re;
    @Before
    public void setUp(){
        re=new Reserve(25);
    }

    @Test
    public void getReserveActuelleTest() {
        assertEquals(0, re.getReserveActuelle(0));
        assertEquals(10,re.getReserveActuelle(10));
        assertEquals(12,re.getReserveActuelle(2));
        assertEquals( 25, re.getReserveActuelle(60));
        re.viderReserve();
        assertEquals(0, re.getReserveActuelle());
    }
}