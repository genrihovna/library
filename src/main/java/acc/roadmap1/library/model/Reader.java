package acc.roadmap1.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class Reader{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private int id;

    @Id
    @Column(name = "username")
    private String name;

    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "reader",
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE, CascadeType.DETACH,
                    CascadeType.REFRESH})
    private List<Book> books;

    public Reader() {
    }

    public Reader(String name, String password, List<Book> books) {
        this.name = name;
        this.password = password;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void add(Book tempBook){
        if(books==null)
            books = new ArrayList<>();
        books.add(tempBook);
        tempBook.setReader(this);
    }

    public void handOver(Book tempBook){
        if(!books.contains(tempBook)){
            throw new RuntimeException("Book " + tempBook.toString() + " is not found");
        } else {
            books.remove(tempBook);
            tempBook.setReader(null);
        }
    }
}
