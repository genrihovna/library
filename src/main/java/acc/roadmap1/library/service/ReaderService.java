package acc.roadmap1.library.service;

import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Reader;

import java.util.List;

public interface ReaderService {

    List<Reader> findAll();

    Reader findByName(String username);

    void save(Reader reader);

    Reader takeABook(ApplicationUserDetails userDetails, long bookId);

    void returnABook(ApplicationUserDetails userDetails, long bookId);

}
