package acc.roadmap1.library.service;

import acc.roadmap1.library.model.User;
import acc.roadmap1.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByName(String username) {
        List<User> userList = this.findAll();
        User theUser = null;
        Optional<User> readerOptional = userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
        if (readerOptional.isPresent()) {
            theUser = readerOptional.get();
        } else {
            throw new RuntimeException("Did not get reader - " + username);
        }
        return theUser;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
