package acc.roadmap1.library.service;

import acc.roadmap1.library.model.*;
import acc.roadmap1.library.repository.AccountRepository;
import acc.roadmap1.library.repository.PrivilegeRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import acc.roadmap1.library.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SecurityServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private ReaderRepository readerRepository;

    @MockBean
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private SecurityService service;

    @Test
    void createLibrarianAccount() {
        String username = "username";
        String password = "password";
        String name = "name";
//        when(accountRepository.findAccountByUsername(username)).thenReturn(
//                new Account("", "", Arrays.asList(new Role("", Arrays.asList(
//                        new Privilege(1, Privileges.MANAGE_ACCOUNTS.name()),
//                        new Privilege(Privileges.MANAGE_BOOKS.name()))))));
        Reader tempReader = new Reader(name, accountRepository.findAccountByUsername(username).get());
        readerRepository.save(tempReader);
        assertThat(tempReader.getName()).isEqualTo(username);
    }

    @Test
    void createReaderAccount() {
    }
}