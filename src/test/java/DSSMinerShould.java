import hec.hecmath.DSS;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class DSSMinerShould {
    String pathToDSS = "src/test/resources/Existing_Conditions-Trinity.dss";
    @Test
    void getAllPathnamesForCollectionNumber() {
        Vector<String> actual = DSSMiner.GetAllPathnamesForCollectionNumber(pathToDSS,1);
        System.out.print(actual);
    }
    @Test
    void shiftDataForward() {
    }

}