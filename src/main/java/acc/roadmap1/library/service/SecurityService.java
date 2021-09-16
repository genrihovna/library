package acc.roadmap1.library.service;

import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.User;
import acc.roadmap1.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findFirstByUsername(username);

        if (user.isPresent()) {
            return new ApplicationUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("User with login " + username + " not found");
        }
    }
}
