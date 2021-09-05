package acc.roadmap1.library.service;

import acc.roadmap1.library.dao.ReaderRepo;
import acc.roadmap1.library.model.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderServiceImpl implements ReaderService{

    private ReaderRepo readerRepo;

    @Autowired
    public ReaderServiceImpl(ReaderRepo readerRepo) {
        this.readerRepo = readerRepo;
    }

    @Override
    public List<Reader> findAll() {
        return readerRepo.findAll();
    }

    @Override
    public Reader findByName(String username) {
        List<Reader> readerList = this.findAll();
        Reader theReader = null;
        Optional<Reader> readerOptional = readerList.stream()
                .filter(reader -> reader.getName().equals(username))
                .findFirst();
        if(readerOptional.isPresent()){
            theReader = readerOptional.get();
        } else{
            throw new RuntimeException("Did not get reader - " + username);
        }
        return theReader;
    }

    @Override
    public void save(Reader reader) {
        readerRepo.save(reader);
    }
}
