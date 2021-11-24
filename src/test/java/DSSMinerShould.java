import hec.heclib.dss.CondensedReference;
import hec.hecmath.DSS;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class DSSMinerShould {
    String pathToDSS = "src/test/resources/Existing_Conditions-Trinity.dss";
    @Test
    void getAllPathnamesForCollectionNumber() {
        CondensedReference[] actual = DSSMiner.GetAllPathnamesForCollectionNumber(pathToDSS,1);
        int expected = 44;
        assertEquals(expected,actual.length);
    }
    @Test
    void shiftDataForward() {
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        CondensedReference[] ref = DSSMiner.GetAllPathnamesForCollectionNumber(pathToDSS,1);
        DSSMiner.ShiftDataForward(pathToDSS,ref[0],ints,24);
    }

}