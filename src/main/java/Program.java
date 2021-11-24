import hec.heclib.dss.*;
import hec.io.PairedDataContainer;
import hec.io.TimeSeriesContainer;

import java.util.Arrays;
import java.util.List;
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
            CondensedReference[] recordsToAdjust = DSSMiner.GetAllPathnamesForCollectionNumber(_dssFile, collectionNumber);
            //Perform the Shift
            for (CondensedReference pathname : recordsToAdjust) {
                DSSMiner.ShiftDataForward(_dssFile, pathname, (List<Integer>) yearsToAdjust, _forwardShiftInHours);
            }
        }
    }
}
