package acc.roadmap1.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "reader",
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE, CascadeType.DETACH,
                    CascadeType.REFRESH})
    private List<Book> books;

    public User() {
    }

    public User(String username, String password, List<Book> books) {
        this.username = username;
        this.password = password;
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public String toString() {
        return "Reader{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", books=" + books.toString() +
                '}';
    }
}
