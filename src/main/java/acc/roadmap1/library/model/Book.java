package acc.roadmap1.library.model;

import lombok.AllArgsConstructor;
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
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    public void setReader(Reader reader) {
        this.reader = reader;
    }
}
