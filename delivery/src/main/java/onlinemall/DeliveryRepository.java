package onlinemall;

import java.util.Optional;

//import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;




@RepositoryRestResource(collectionResourceRel="deliveries", path="deliveries")
//public interface DeliveryRepository extends PagingAndSortingRepository<Delivery, Long>{

    public interface DeliveryRepository extends MongoRepository<Delivery, Long>{

}
