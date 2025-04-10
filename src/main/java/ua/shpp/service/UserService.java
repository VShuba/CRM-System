package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.shpp.entity.UserEntity;
import ua.shpp.repository.UserRepository;

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
    public UserEntity save(UserEntity userEntity) {
        return repository.save(userEntity);
    }


    /**
     * Створення користувача
     *
     * @return створення користувача
     */
    public UserEntity create(UserEntity userEntity) {
        if (repository.existsByLogin(userEntity.getEmail())) {
            throw new RuntimeException("A user with this email already exists.");
        }

        return repository.save(userEntity);
    }

    public UserEntity getByUsername(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    public UserEntity getByLogin(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Отримання поточного користувача
     *
     * @return поточний користувач
     */
    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public UserEntity createOAuthUser(UserEntity login) {
        if (!repository.existsByLogin(login.getUsername())) {
            repository.save(login);
        }
        return login;
    }
}