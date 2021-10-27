package acc.roadmap1.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "readers")
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reader",
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE, CascadeType.DETACH,
                    CascadeType.REFRESH})
    private Set<Book> books;

    public Reader(String name, Account account) {
        this.name = name;
        this.account = account;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
