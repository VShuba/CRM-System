package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.shpp.repository.UserRepository;
import ua.shpp.entity.User;
import ua.shpp.utils.Role;

//Ticket Scrum-33
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * Збереження користувача
     *
     * @return збереження користувача
     */
    public User save(User user) {
        return repository.save(user);
    }


    /**
     * Створення користувача
     *
     * @return створення користувача
     */
    public User create(User user) {
        if (repository.existsByLogin(user.getEmail())) {
            throw new RuntimeException("A user with this email already exists.");
        }

        return repository.save(user);
    }

    public User getByUsername(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    public User getByLogin(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Отримання поточного користувача
     *
     * @return поточний користувач
     */
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public User createOAuthUser(User login) {
        if (!repository.existsByLogin(login.getUsername())) {
            repository.save(login);
        }
        return login;
    }
}