package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.shpp.entity.UserEntity;
import ua.shpp.exception.UserAlreadyExistsException;
import ua.shpp.repository.UserRepository;

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
            throw new UserAlreadyExistsException("A user with this email already exists.");
        }

        return repository.save(userEntity);
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
        Long userID = getCurrentUserId();

        return repository.findById(userID).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Retrieves the current user's ID from the security context.
     *
     * @return ID of the currently authenticated user
     * @throws NumberFormatException if the authentication name is not a valid Long
     */
    public Long getCurrentUserId() {
        String userIDStr = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(userIDStr);
    }

    public UserEntity createOAuthUser(UserEntity user) {
        return repository.findByLogin(
                        user.getLogin())
                .orElseGet(() -> repository.save(user));

    }
}