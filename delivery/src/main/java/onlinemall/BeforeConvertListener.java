package onlinemall;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;


public class BeforeConvertListener extends AbstractMongoEventListener<Delivery> {
	  @Override
	  public void onBeforeConvert(BeforeConvertEvent<Delivery> event) {
	    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ event");
	  }
	}
