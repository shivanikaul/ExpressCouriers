package expressService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressService {

    private static final int maximumNumberOfParcels = 3;
    private static final String AFTERNOON = "AFTERNOON";
    private static final String NIGHT = "NIGHT";

    public Map<States, Double> getAvailableCapacityByStates() {
        return availableCapacityByStates;
    }
    private Map<States,Double> availableCapacityByStates = new HashMap<States, Double>()
    {{
            put(States.BANGALORE,5000d);
            put(States.CHANDIGARH,5000d);
            put(States.DELHI,5000d);
        }};


    private Map<String,Map<States,Double>> availableCapavityByStatesByTimeSlot =
            new HashMap<String, Map<States, Double>>()
            {{
                put(AFTERNOON,availableCapacityByStates);
                put(NIGHT,new HashMap<States, Double>(availableCapacityByStates));
             }
            };


    private Map<States,Double> priceByStates = new HashMap<States, Double>()
    {{
        put(States.BANGALORE,30d);
        put(States.DELHI,35d);
        put(States.CHANDIGARH,35d);

    }};

    public ExpressService(Map<States,Double> availableCapacityByStates,
                          Map<States,Double> priceByStates)
    {
        this.priceByStates = priceByStates;
        this.availableCapacityByStates = availableCapacityByStates;
    }


    public ExpressService(Map<States,Double> availableCapacityByStates)
    {
        this.availableCapacityByStates = availableCapacityByStates;
    }

    public ExpressService(){ }

    boolean acceptTheParcel(List<Parcel> parcels, Map<States, Double> availableCapacityByStates1) throws Exception {

        if(parcels.size() > maximumNumberOfParcels)
            throw new Exception("Cannot Accept more than 3 parcels");
        for (Parcel parcel : parcels) {
            Double availableCapacity = availableCapacityByStates1.get(parcel.getState());
            if(parcel.getWeight() <= availableCapacity)
            {
                availableCapacityByStates1.put(parcel.getState(), availableCapacity - parcel.getWeight());
            }
            else
            {
                throw new Exception("Parcel Weight is over and Above the availble Capacity");
            }
        }
        return true;

    }

    boolean acceptTheParcelByTimeSlot(List<Parcel> parcels, Map<States,Double> availableCapacityMap) throws Exception {
        return acceptTheParcel(parcels, availableCapacityMap);
    }

    public Map<States,Double> courierTransction(List<Parcel> parcels,boolean before3) throws Exception {
        Map<States, Double> availabilityMap = before3 ? availableCapavityByStatesByTimeSlot.get(AFTERNOON) : availableCapavityByStatesByTimeSlot.get(NIGHT);
        try {
            acceptTheParcelByTimeSlot(parcels, availabilityMap);
        }
        catch(Exception e)
        {
            if(before3 && e.getMessage().equalsIgnoreCase("Parcel Weight is over and Above the availble Capacity"))
            {
                before3 = false;
                acceptTheParcelByTimeSlot(parcels,availableCapavityByStatesByTimeSlot.get(NIGHT));
            }
            else
                throw e;
        }

        for (Parcel parcel : parcels) {
            calaculateCharge(parcel,before3);
        }
        return availabilityMap;
    }
     double calaculateCharge(Parcel parcel,boolean before3) {
        Double pricePer100Gram = priceByStates.get(parcel.getState());
        if(parcel.isFragile())
        {
            pricePer100Gram += 5d;
        }
        if(!before3) pricePer100Gram += pricePer100Gram*0.20;
        int parcelWiightModulo = (int)(parcel.getWeight() % 100);
        int parcelDivisor = (int) parcel.getWeight() / 100;
        int parcelWeightIncrement = (parcel.getWeight()>=100? (parcelWiightModulo==0?parcelDivisor:
                parcelDivisor+1):1);
        double totalPrice = (parcel.getWeight()>=100?(parcelWeightIncrement%100):1) * pricePer100Gram;
        return totalPrice;
    }
}
