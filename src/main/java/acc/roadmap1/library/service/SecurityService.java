package acc.roadmap1.library.service;

import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Privilege;
import acc.roadmap1.library.model.Privileges;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.model.Role;
import acc.roadmap1.library.model.RoleNames;
import acc.roadmap1.library.repository.AccountRepository;
import acc.roadmap1.library.repository.PrivilegeRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import acc.roadmap1.library.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SecurityService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final ReaderRepository readerRepository;

    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public SecurityService(AccountRepository accountRepository, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, ReaderRepository readerRepository,
                           PrivilegeRepository privilegeRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.readerRepository = readerRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findAccountByUsername(username)
                .map(account -> new ApplicationUserDetails(account.getUsername(), account.getPassword(),
                        getGrantedAuthoritiesFromRoles(account.getRoles()), account))
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + username + " not found"));
    }

    public void createLibrarianAccount(String username, String password, String name) {
        Role adminRole = getAdminRole();

        Account account = new Account(username, passwordEncoder.encode(password), Set.of(adminRole));
        account = accountRepository.save(account);

        Reader reader = new Reader(name, account);
        readerRepository.save(reader);
    }

    public void createReaderAccount(String username, String password, String name) {
        Role readerRole = getReaderRole();

        Account account = new Account(username, passwordEncoder.encode(password), Set.of(readerRole));
        account = accountRepository.save(account);

        Reader reader = new Reader(name, account);
        readerRepository.save(reader);
    }

    private Role getReaderRole() {
        Optional<Role> role = roleRepository.findRoleByName(RoleNames.READER.name());
        if (role.isEmpty()){
            role = Optional.of(roleRepository.save(new Role(RoleNames.READER.name(), getReaderPrivileges())));
        }
        return role.orElseThrow();
    }

    private Role getAdminRole() {
        Optional<Role> role = roleRepository.findRoleByName(RoleNames.ADMIN.name());

        if (role.isEmpty()) {
            role = Optional.of(roleRepository.save(new Role(RoleNames.ADMIN.name(), getDefaultAdminPrivileges())));
        }

        return role.orElseThrow();
    }

    private Set<Privilege> getDefaultAdminPrivileges() {
        List<String> defaultAdminPrevileges = List.of(
                Privileges.MANAGE_BOOKS.name(),
                Privileges.MANAGE_ACCOUNTS.name()
        );

        return getPrivileges(defaultAdminPrevileges);
    }

    private Set<Privilege> getPrivileges(List<String> defaultPrevileges) {
        Set<Privilege> privileges = privilegeRepository.findPrivilegeByNameIn(defaultPrevileges);

        List<String> foundedPrivilegeNames = privileges.stream().map(Privilege::getName).collect(Collectors.toList());

        List<Privilege> differentPrivileges = defaultPrevileges.stream()
                .filter(privilegeName -> !foundedPrivilegeNames.contains(privilegeName))
                .map(Privilege::new)
                .collect(Collectors.toList());

        privilegeRepository.saveAll(differentPrivileges);

        return privilegeRepository.findPrivilegeByNameIn(defaultPrevileges);
    }

    private Set<Privilege> getReaderPrivileges() {
        List<String> defaultReaderPrivileges = List.of(
                Privileges.MANAGE_BOOKS.name()
        );
        return getPrivileges(defaultReaderPrivileges);
    }

    private List<GrantedAuthority> getGrantedAuthoritiesFromRoles(Set<Role> roles) {
        return roles.stream().flatMap(role -> role.getPrivileges().stream())
                .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
                .collect(Collectors.toList());
    }
}
