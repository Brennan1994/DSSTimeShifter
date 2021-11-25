import hec.heclib.dss.CondensedReference;
import hec.heclib.dss.HecDSSUtilities;
import hec.heclib.dss.HecDataManager;

import java.util.Arrays;
import java.util.Vector;

public class TestData {


    public static void main(String[] args){
        String _oldDss = "\\\\NRC-Ctrl01\\D$\\WG_Grid1\\NRCPilotStudy_L01G01.dss";
        String _newDss = "C:\\Temp\\test.dss";
        String CNasString = String.format("%06d",1);
        String pathWithWildChars = "/*/*/*/*/*/C:"+ CNasString+"*/";


        HecDSSUtilities utils = new HecDSSUtilities();
        utils.setDSSFileName(_oldDss);
        HecDataManager oldDSSManager = new HecDataManager();
        oldDSSManager.setDSSFileName(_oldDss);
        String[] pathnames = oldDSSManager.getCatalog(false,pathWithWildChars);
        Vector<String> vector = new Vector<>(Arrays.asList(pathnames));
        utils.copyRecordsFrom(_newDss,vector);
        }
    }
