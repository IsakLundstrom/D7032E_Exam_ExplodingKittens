package test;

import static org.junit.Assert.*;

import org.junit.*;

public class TestRequirements {

    @Test
    public void testTest() {
        assertTrue(true);
    }

    @Before
    public void init() {
        /*
        calendar = (CalendarImpl2016) getCalendar("ltu.CalendarImpl2016");
        payment = new PaymentImpl(calendar, "21343hjkh2");
        */
    }

    @Test
    public void req101_19AndBelow() {
        //assertEquals(0, payment.getMonthlyAmount("19970101-1111", 0,100,100));
    }


}
