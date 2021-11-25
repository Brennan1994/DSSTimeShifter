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

        //gonna do this for each event that needs to shift. Event numbers relate back to the year.
        // Event one occurs between 2000-2001, event two between 2001-2002 and so on.
        // First step is to get the index that relates to the first ordinate, and final ordinate of the event.
        for (Integer eventNum : eventsToShift) {
            HecTime startOfYear = new HecTime("01Jan20" + String.format("%02d",eventNum), "0100");
            HecTime endOfYear = new HecTime("31Dec20" + String.format("%02d",eventNum), "2400");
            int startOfYearIndex = times.index(startOfYear);
            int endOfYearIndex = times.index(endOfYear);

            //now I want to make a copy of those ordinates so I can shift them around. We could do without this and work
            //directly with values, but this extra bit of memory makes it more clear to me what I'm doing.
            double[] valuesForEvent = Arrays.copyOfRange(values,startOfYearIndex,endOfYearIndex);
            //This next variable will be the new shifted event.
            double[] ShiftedValuesForEvent = new double[valuesForEvent.length];
            //Java defaults new double[] to values of 0.0, so the starting 0s are already set.

            //now the real data
            for(int i = 0; i< valuesForEvent.length - hoursToShift; i++){
                ShiftedValuesForEvent[i+hoursToShift] = valuesForEvent[i];
            }
            //now copy this back into the full 50yr array
            for(int i = 0; i< ShiftedValuesForEvent.length; i++) {
                values[i + startOfYearIndex] = ShiftedValuesForEvent[i];
            }
            //We're good! do it for the next event in this lifecycle
        }
        //set the new array and save the adjusted record out
        tsc.set(values,times);
        return tsc;
    }
}
