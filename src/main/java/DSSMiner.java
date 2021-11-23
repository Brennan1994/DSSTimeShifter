import hec.heclib.dss.HecDataManager;
import hec.heclib.dss.HecTimeSeries;
import hec.heclib.util.HecTime;
import hec.heclib.util.HecTimeArray;
import hec.io.TimeSeriesContainer;

import java.util.*;

public class DSSMiner {
    public static Vector<String> GetOutputVariablePathnames(String _dssFile, int collectionNumber) {
        String pathWithWildChars = "/*/*/*/*/*/*/*/*"+ collectionNumber +"/";
        HecDataManager dssManager = new HecDataManager();
        dssManager.setDSSFileName(_dssFile);
        String[] pathnames = dssManager.getCatalog(false, pathWithWildChars);
        HecDataManager.closeAllFiles();
        Vector<String> vector = new Vector<>(Arrays.asList(pathnames));
        return vector;
    }
    public static void ShiftDataForward(String _dssFile, String pathname, Set<Integer> yearsToShift){
        TimeSeriesContainer tscRead = new TimeSeriesContainer();
        tscRead.setName(pathname);
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
        for(int i = 0; i< sortedYears.size(); i++){
            if(i>0 && sortedYears.get(i) - sortedYears.get(i-1) == 1){
                return;
            }
        }

//        //actually shift things
//        for(Integer year: sortedYears){
//            HecTime startOfYear = new HecTime();
//            startOfYear.add
//            hTimes.index()

        }
    }
