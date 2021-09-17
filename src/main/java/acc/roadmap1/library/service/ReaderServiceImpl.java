package acc.roadmap1.library.service;

import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.repository.AccountRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public ReaderServiceImpl(ReaderRepository readerRepository, AccountRepository accountRepository) {
        this.readerRepository = readerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Reader> findAll() {
        return readerRepository.findAll();
    }

    @Override
    public Reader findByName(String username) {
        return accountRepository.findAccountByUsername(username)
                .map(Account::getReader)
                .orElseThrow(() -> new RuntimeException("Did not get reader - " + username));
    }

    @Override
    public void save(Reader reader) {
        readerRepository.save(reader);
    }
}
