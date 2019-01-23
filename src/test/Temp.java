import esn.entities.secondary.StoredFile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

public class Temp {
    @Entity
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue
        private int id;


        @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "owner")
        private Set<StoredFile> storedFiles = new HashSet<>();

        //.........//

    }
}