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
     * Finds a user by login (username).
     *
     * @deprecated Deprecated because the application no longer uses login/email as the username
     * in the authentication context. Instead, JWT contains only the user ID,
     * which is used as the principal name in the security context.
     * <p>
     * Use {findByLogin(Long)} instead to look up users by ID,
     * or rely on {@code @AuthenticationPrincipal} to access the current user.
     */
    @Deprecated
    public UserEntity getByUsername(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Отримання поточного користувача
     *
     * @return поточний користувач
     */
    public UserEntity getCurrentUser() {
        String userIDStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userID = Long.parseLong(userIDStr);

        return repository.findById(userID).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserEntity createOAuthUser(UserEntity login) {
        if (!repository.existsByLogin(login.getEmail())) {
            repository.save(login);
        }
        return login;
    }
}