package acc.roadmap1.library.controller.dto;

import lombok.Data;

@Data
public class BookDTO {

    private long id;

    private String author;

    private short published;

    private String title;
}
