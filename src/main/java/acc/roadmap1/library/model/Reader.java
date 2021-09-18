package acc.roadmap1.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "reader",
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE, CascadeType.DETACH,
                    CascadeType.REFRESH})
    private List<Book> books;

    public Reader(String name, Account account) {
        this.name = name;
        this.account = account;
    }

    public void add(Book tempBook) {
        if (books == null)
            books = new ArrayList<>();
        books.add(tempBook);
        tempBook.setReader(this);
    }

    public void handOver(Book tempBook) {
        if (!books.contains(tempBook)) {
            throw new RuntimeException("Book " + tempBook.toString() + " is not found");
        } else {
            books.remove(tempBook);
            tempBook.setReader(null);
        }
    }
}
