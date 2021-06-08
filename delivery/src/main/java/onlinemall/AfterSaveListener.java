package onlinemall;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

public class AfterSaveListener extends AbstractMongoEventListener<Delivery> {
    @Override
    public void onAfterSave(AfterSaveEvent<Delivery> event) {
      //... does some auditing manipulation, set timestamps, whatever ...
      System.out.println("*********************************************************event");
    }
}
