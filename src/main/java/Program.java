import hec.heclib.dss.CondensedReference;
import hec.heclib.dss.HecTimeSeries;
import hec.io.TimeSeriesContainer;

import java.util.Set;

//This class is intended to be used to perform that actual adjustments to the SDI data. Shifting the desired pathnames in place.
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
            Set<Integer> eventsToAdjust = reader.GetBadEventsPerLifecycle(collectionNumber);
            CondensedReference[] recordsToAdjust = DSSAdjuster.GetAllPathnamesForCollectionNumber(_dssFile, collectionNumber);
            //Perform the Shift
            for (CondensedReference pathname : recordsToAdjust) {
                TimeSeriesContainer tsc = new TimeSeriesContainer();
                tsc.setName(String.valueOf(pathname));
                HecTimeSeries dssTimeSeriesRead = new HecTimeSeries();
                dssTimeSeriesRead.setDSSFileName(_dssFile);
                dssTimeSeriesRead.read(tsc, false);
                dssTimeSeriesRead.done();

                TimeSeriesContainer ShiftedTSC = DSSAdjuster.ShiftDataForward(tsc, eventsToAdjust, _forwardShiftInHours);
                dssTimeSeriesRead.write(ShiftedTSC);

            }
        }
    }
}
