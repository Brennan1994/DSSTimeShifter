import hec.heclib.dss.CondensedReference;
import hec.heclib.dss.HecDataManager;
import hec.heclib.dss.HecTimeSeries;
import hec.heclib.util.HecTime;
import hec.heclib.util.HecTimeArray;
import hec.io.TimeSeriesContainer;
import java.util.*;

public class DSSMiner {
    public static CondensedReference[] GetAllPathnamesForCollectionNumber(String _dssFile, int collectionNumber) {
        String CNasString = String.format("%06d",collectionNumber);
        String pathWithWildChars = "/*/*/*/*/*/C:" + CNasString + "*/";
        HecDataManager dssManager = new HecDataManager();
        dssManager.setDSSFileName(_dssFile);
        CondensedReference[] pathnames = dssManager.getCondensedCatalog(pathWithWildChars);
        HecDataManager.closeAllFiles();
        return pathnames;
    }

    public static TimeSeriesContainer ShiftDataForward(TimeSeriesContainer tsc, Collection<Integer> eventsToShift, int hoursToShift) {
        double[] values = tsc.getValues();
        HecTimeArray times = tsc.getTimes();

        for (Integer eventNum : eventsToShift) {
            HecTime startOfYear = new HecTime("01Jan20" + String.format("%02d",eventNum), "0100");
            HecTime endOfYear = new HecTime("31Dec20" + String.format("%02d",eventNum), "2400");
            int startOfYearIndex = times.index(startOfYear);
            int endOfYearIndex = times.index(endOfYear);

            double[] valuesForEvent = Arrays.copyOfRange(values,startOfYearIndex,endOfYearIndex);
            double[] ShiftedValuesForEvent = new double[valuesForEvent.length];

            System.arraycopy(valuesForEvent, 0, ShiftedValuesForEvent, hoursToShift, valuesForEvent.length - hoursToShift);
            System.arraycopy(ShiftedValuesForEvent, 0, values, startOfYearIndex, ShiftedValuesForEvent.length);
        }
        //set the new array and save the adjusted record out
        TimeSeriesContainer shiftedTSC = (TimeSeriesContainer) tsc.clone();
        shiftedTSC.set(values,times);
        return shiftedTSC;
    }
}
