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
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Користувач з таким ім'ям вже існує");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Користувач з таким email вже існує");
        }

        return save(user);
    }

    /**
     * Пошук користувача за ім'ям
     *
     * @return користувач
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено"));

    }

    /**
     * Отримання користувача за ім'ям
     * <p>
     * Потрібен для Spring Security
     *
     * @return користувач
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Отримання поточного користувача
     *
     * @return поточний користувач
     */
    public User getCurrentUser() {
        //Отримання ім'я з контексту Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    /**
     * Отримання ролі ADMIN поточним користувачем
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}