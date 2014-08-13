package expressService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ExpressTest {


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private ExpressService expressService = new ExpressService();
    private boolean isbrittle;
    private boolean before3 = true;
    @Test
    public void shouldNotAcceptTheParcelAboveTheCourierLimit() throws Exception {
        isbrittle = false;
        Parcel parcel = new Parcel(isbrittle,5100,States.BANGALORE);
        Parcel parcel1 = new Parcel(isbrittle,500,States.DELHI);
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Parcel Weight is over and Above the availble Capacity");
        expressService.courierTransction(Arrays.asList(parcel, parcel1), before3);
    }

    @Test
    public void shouldAcceptTheParcelUptoTheCourierLimit() throws Exception {
        isbrittle = false;

        Parcel parcel = new Parcel(isbrittle,5000,States.BANGALORE);
        Parcel parcel1 = new Parcel(isbrittle,500,States.BANGALORE);

        assertTrue(expressService.acceptTheParcel(Arrays.asList(parcel), expressService.getAvailableCapacityByStates()));
    }

    @Test
    public void shouldCalculateTheChargeOFTheParcelBefore3() throws Exception {
        isbrittle = false;
        Parcel parcel = new Parcel(isbrittle,4900,States.BANGALORE);
        boolean before3 = true;
        Assert.assertEquals(1470, expressService.calaculateCharge(parcel, before3),0);
    }

    @Test
    public void shouldCalculateTheChargeOFTheParcelAfter3() throws Exception {
        isbrittle = false;
        Parcel parcel = new Parcel(isbrittle,100,States.BANGALORE);
        before3 = false;
        Assert.assertEquals(36, expressService.calaculateCharge(parcel,before3),0);
    }

    @Test
    public void shouldCalculateTheChargeOFTheParcelBefore3LessThan100Grams() throws Exception {
        isbrittle = false;
        Parcel parcel = new Parcel(isbrittle,60,States.BANGALORE);
        Assert.assertEquals(30, expressService.calaculateCharge(parcel,before3),0);


    }

    @Test
    public void shouldCalculateTheChargeOFTheBrittleParcelBefore3() throws Exception {
        isbrittle = true;
        Parcel parcel = new Parcel(isbrittle,240,States.BANGALORE);
        Assert.assertEquals(105, expressService.calaculateCharge(parcel, before3),0);
    }

    @Test
    public void shouldNotAcceptThe3Parcels() throws Exception {
        isbrittle = false;
        Parcel parcel = new Parcel(isbrittle,500,States.BANGALORE);
        Parcel parcel1 = new Parcel(isbrittle,500,States.DELHI);
        Parcel parcel2 = new Parcel(isbrittle,500,States.DELHI);
        Parcel parcel3 = new Parcel(isbrittle,500,States.DELHI);
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Cannot Accept more than 3 parcels");
        expressService.courierTransction(Arrays.asList(parcel, parcel1, parcel2, parcel3), before3);
    }

    @Test
    public void shouldNotAcceptTheParcelsAltgetherEvenIfOneIsnotAccepted() throws Exception {
        isbrittle = false;
        Parcel parcel = new Parcel(isbrittle,4500,States.DELHI);
        Parcel parcel1 = new Parcel(isbrittle,500,States.DELHI);
        Parcel parcel2 = new Parcel(isbrittle,500,States.DELHI);
        Map<States,Double> availableCapacityByStates = new HashMap<States, Double>()
        {{
                put(States.BANGALORE,5000d);
                put(States.CHANDIGARH,5000d);
                put(States.DELHI,5000d);
            }};

        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Parcel Weight is over and Above the availble Capacity");
        expressService = new ExpressService(availableCapacityByStates);
        expressService.acceptTheParcel(Arrays.asList(parcel, parcel1,parcel2), expressService.getAvailableCapacityByStates());
        assertTrue(5000d == (Double) expressService.getAvailableCapacityByStates().get(States.DELHI).doubleValue());
    }

    @Test
    public void shouldCalculateTheChargeOFTheParcelLessThan100Grams() throws Exception {
        isbrittle = false;
        Parcel parcel = new Parcel(isbrittle,60,States.BANGALORE);
        Map<States, Double> availableMap = expressService.courierTransction(Arrays.asList(parcel), before3);
        Assert.assertEquals(4940, availableMap.get(States.BANGALORE),0);
    }


}
