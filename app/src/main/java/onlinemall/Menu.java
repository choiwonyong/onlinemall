package onlinemall;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Menu_table")
public class Menu {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

}
