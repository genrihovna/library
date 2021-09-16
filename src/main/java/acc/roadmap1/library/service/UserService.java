package acc.roadmap1.library.service;

import acc.roadmap1.library.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findByName(String username);

    void save(User user);

}
