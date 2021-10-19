package acc.roadmap1.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "author")
    private String author;

    @Column(name = "date")
    private int published;

    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH})
    @JoinColumn(name = "reader")
    private Reader reader;

    @Transient
    private BookStatus status;

    public Book(String author, int published, String title) {
        this.author = author;
        this.published = published;
        this.title = title;
    }

    public Optional<Reader> getReader() {
        return Optional.ofNullable(reader);
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public String toString() {
        return author +
                ", " + published +
                ", " + title + "//";
    }
}
