package acc.roadmap1.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
    private short published;

    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH})
    @JoinColumn(name = "reader")
    private Reader reader;

    @Transient
    private boolean status;

    public Book(String author, short published, String title) {
        this.author = author;
        this.published = published;
        this.title = title;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return reader != null;
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
