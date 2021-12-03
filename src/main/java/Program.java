import hec.heclib.dss.CondensedReference;
import hec.heclib.dss.HecTimeSeries;
import hec.io.TimeSeriesContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

//This class is intended to be used to perform that actual adjustments to the SDI data. Shifting the desired pathnames in place.
public class Program {
    public static void main(String[] args) {
        //User Inputs
        String _dssFile = "\\\\NRC-Ctrl01\\D$\\WG_Grid1\\NRCPilotStudy_L01G01.dss";
        String _xmlFile = "C:\\Temp\\DCInvestigatorReport.xml";
        int _forwardShiftInHours = 48;
        ArrayList<Integer> skipList = new ArrayList<>(Arrays.asList(0)); //if for some reason you need to skip lifecycles for debug

        //Define the events that need adjustment
        DCInvestigatorReader reader = new DCInvestigatorReader(_xmlFile);

        //Figure out the record that needs adjusting
        for (int collectionNumber : reader.GetBadLifecycles()) {
            if(skipList.contains(collectionNumber)){
                continue;
            }
            Set<Integer> eventsToAdjust = reader.GetBadEventsPerLifecycle(collectionNumber);
            CondensedReference[] recordsToAdjust = DSSAdjuster.GetAllPathnamesForCollectionNumber(_dssFile, collectionNumber);
            //Perform the Shift
            for (CondensedReference pathname : recordsToAdjust) {
                TimeSeriesContainer tsc = new TimeSeriesContainer();
                tsc.setName(pathname.getNominalPathname());
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
