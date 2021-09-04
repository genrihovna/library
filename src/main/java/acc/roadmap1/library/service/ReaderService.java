package acc.roadmap1.library.service;

import acc.roadmap1.library.model.Reader;

import java.util.List;

public interface ReaderService {

    public List<Reader> findAll();
    public Reader findByName(String username);
    public void save(Reader reader);
    //public Reader findById(int readersId);

}
