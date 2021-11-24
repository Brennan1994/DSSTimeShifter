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
        String pathWithWildChars = "/*/*/*/*/*/C:" + CNasString + "|EXISTING C:TRINITY:STOCHHYDRO-TRINITY SDI_VALIDATION/";
        HecDataManager dssManager = new HecDataManager();
        dssManager.setDSSFileName(_dssFile);
        CondensedReference[] pathnames = dssManager.getCondensedCatalog(pathWithWildChars);
        HecDataManager.closeAllFiles();
        return pathnames;
    }

    public static void ShiftDataForward(String _dssFile, CondensedReference pathname, List<Integer> yearsToShift, int hoursToShift) {
        TimeSeriesContainer tscRead = new TimeSeriesContainer();
        tscRead.setName(String.valueOf(pathname));
        HecTimeSeries dssTimeSeriesRead = new HecTimeSeries();
        dssTimeSeriesRead.setDSSFileName(_dssFile);
        dssTimeSeriesRead.read(tscRead, true);
        dssTimeSeriesRead.done();

        double[] vals = tscRead.getValues();
        HecTimeArray hTimes = tscRead.getTimes();

        //sort in descending order. Want to shift the
        List<Integer> sortedYears = new ArrayList<>(yearsToShift);
        Collections.sort(sortedYears);
        Collections.reverse(sortedYears);

        //Check that the events aren't consecutive. This throws off our whole plan
        for (int i = 0; i < sortedYears.size(); i++) {
            if (i > 0 && sortedYears.get(i) - sortedYears.get(i - 1) == 1) {
                return;
            }
        }
        //actually shift things
        for (Integer year : sortedYears) {
            HecTime startOfYear = new HecTime("01Jan20" + year, "0100");
            HecTime endOfYear = new HecTime("31Dec20" + year, "2400");
            int startOfYearIndex = hTimes.index(startOfYear);
            int endOfYearIndex = hTimes.index(endOfYear);

            double[] valsForEvent = Arrays.copyOfRange(vals,startOfYearIndex,endOfYearIndex);
            double[] shiftedValsForEvent = new double[valsForEvent.length];

            //add the zeroes for the beginning
            for(int i = 0; i < hoursToShift; i++){
                shiftedValsForEvent[i]=0.0;
            }
            //now the real data
            for(int i = 0; i< valsForEvent.length; i++){
                shiftedValsForEvent[i+hoursToShift] = valsForEvent[i];
            }
            //now copy this back into the full 50yr array
            for(int i = 0; i< shiftedValsForEvent.length; i++) {
                vals[i + startOfYearIndex] = shiftedValsForEvent[i];
            }
            //We're good! do it for the next event in this lifecycle
        }
        //set the new array and save the adjusted record out
        tscRead.set(vals,hTimes);
//        dssTimeSeriesRead.write(tscRead);
    }
}
