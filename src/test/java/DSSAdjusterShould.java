import hec.heclib.dss.CondensedReference;
import hec.heclib.dss.HecTimeSeries;
import hec.io.TimeSeriesContainer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DSSAdjusterShould {
    String pathToDSS = "src/test/resources/SDI_Data.dss";
    @Test
    void getAllPathnamesForCollectionNumber() {
        CondensedReference[] actual = DSSAdjuster.GetAllPathnamesForCollectionNumber(pathToDSS,1);
        CondensedReference[] actual2 = DSSAdjuster.GetAllPathnamesForCollectionNumber(pathToDSS,0);
        int expected = 5;
        int expected2 = 0;
        assertEquals(expected,actual.length);
        assertEquals(expected2,actual2.length);
    }

    @Test
    void shiftDataForward() {
        int hoursToShift = 5;
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        CondensedReference[] ref = DSSAdjuster.GetAllPathnamesForCollectionNumber(pathToDSS,1);
        TimeSeriesContainer tsc = new TimeSeriesContainer();
        tsc.setName(ref[1].getNominalPathname());
        HecTimeSeries dssTimeSeriesRead = new HecTimeSeries();
        dssTimeSeriesRead.setDSSFileName(pathToDSS);
        dssTimeSeriesRead.read(tsc, false);
        dssTimeSeriesRead.done();

        TimeSeriesContainer tscShifted = null;
        try {
            tscShifted = DSSAdjuster.ShiftDataForward(tsc,ints,hoursToShift);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(tsc.getValue(0),tscShifted.getValue(hoursToShift)); //First value of the event matches shifted first value
        assertEquals(tsc.getValue(tsc.values.length-1),tscShifted.getValue(tscShifted.values.length-1)); // Last value of the lifecycle still matches
        assertEquals(0.0,tscShifted.getValue(0)); //First values of shifted are getting filled with zeroes
        assertEquals(tsc.getValue(8783-hoursToShift),tscShifted.getValue(8753)); //end of the year should have been deleted, now ends 5 days sooner by values
    }

}