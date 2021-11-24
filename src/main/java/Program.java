import hec.heclib.dss.HecDSSUtilities;
import hec.heclib.dss.HecDataManager;
import hec.heclib.dss.HecPairedData;
import hec.heclib.dss.HecTimeSeries;
import hec.io.PairedDataContainer;
import hec.io.TimeSeriesContainer;

import java.util.Arrays;
import java.util.Set;
import java.util.Vector;

public class Program {
    public static void main(String[] args) {
        //User Inputs
        String _dssFile = "TheWeatherGeneratorData.dss";
        String _xmlFile = "TheDCInvestigatorReport.xml";
        int _forwardShiftInHours = 48;

        //Define the events that need adjustment
        DCInvestigatorReader reader = new DCInvestigatorReader(_xmlFile);

        //Figure out the record that needs adjusting
        for (int collectionNumber : reader.GetBadLifecycles()) {
            Set<Integer> yearsToAdjust = reader.GetBadEventsPerLifecycle(collectionNumber);
            Vector<String> recordsToAdjust = DSSMiner.GetAllPathnamesForCollectionNumber(_dssFile, collectionNumber);
            //Perform the Shift
            for (String pathname : recordsToAdjust) {
                DSSMiner.ShiftDataForward(_dssFile, pathname, yearsToAdjust, _forwardShiftInHours);
            }
        }
    }
}
