import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DCInvestigatorReader {
    Set<FailedEvent> _failedEvents = new HashSet<>();

    public DCInvestigatorReader(String _xmlfilePath){
        try{
            SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(new File(_xmlfilePath));
            Element rootNode = doc.getRootElement();
            List<Element> list = rootNode.getChildren("Event");
            for(Element event: list){
                int eventNum = Integer.parseInt(event.getChildText("Event"));
                int lifecyleNum= Integer.parseInt(event.getChildText("Lifecycle"));
                int realizationNum = Integer.parseInt(event.getChildText("Realization"));
                _failedEvents.add(new FailedEvent(realizationNum,lifecyleNum,eventNum));
            }
        }
        catch(IOException | JDOMException e){
            e.printStackTrace();
        }
    }
    public Set<FailedEvent> GetFailedEvents() {
        return _failedEvents;
    }
    public Set<Integer> GetBadLifecycles(){
        Set<Integer> badLifecycles = new HashSet<>();
        for(FailedEvent fail: _failedEvents){
            badLifecycles.add(fail.getLifecycle());
        }
        return badLifecycles;
    }
    public Set<Integer> GetBadEventsPerLifecycle(int lifecycle){
        Set<Integer> badEvents = new HashSet<>();
        for(FailedEvent fail: _failedEvents){
            if( fail.getLifecycle() == lifecycle){
                badEvents.add(fail.getEvent());
            }
        }
        return badEvents;
    }
}
