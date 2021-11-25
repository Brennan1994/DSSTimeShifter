import hec.heclib.dss.CondensedReference;
import hec.heclib.dss.HecTimeSeries;
import hec.io.TimeSeriesContainer;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DSSMinerShould {
    String pathToDSS = "src/test/resources/SDI_Data.dss";
    @Test
    void getAllPathnamesForCollectionNumber() {
        CondensedReference[] actual = DSSMiner.GetAllPathnamesForCollectionNumber(pathToDSS,1);
        CondensedReference[] actual2 = DSSMiner.GetAllPathnamesForCollectionNumber(pathToDSS,0);
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
        CondensedReference[] ref = DSSMiner.GetAllPathnamesForCollectionNumber(pathToDSS,1);
        TimeSeriesContainer tsc = new TimeSeriesContainer();
        tsc.setName(String.valueOf(ref[1]));
        HecTimeSeries dssTimeSeriesRead = new HecTimeSeries();
        dssTimeSeriesRead.setDSSFileName(pathToDSS);
        dssTimeSeriesRead.read(tsc, false);
        dssTimeSeriesRead.done();

        TimeSeriesContainer tscShifted = DSSMiner.ShiftDataForward(tsc,ints,hoursToShift);
        assertEquals(tsc.getValue(0),tscShifted.getValue(hoursToShift));
        assertEquals(tsc.getValue(tsc.values.length-1-hoursToShift),tscShifted.getValue(tscShifted.values.length));
        assertEquals(0.0,tscShifted.getValue(0));
    }

}