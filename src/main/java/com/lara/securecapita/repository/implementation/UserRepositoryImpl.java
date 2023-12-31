package com.lara.securecapita.repository.implementation;

import com.lara.securecapita.domain.Role;
import com.lara.securecapita.domain.User;
import com.lara.securecapita.domain.UserPrincipal;
import com.lara.securecapita.exception.ApiException;
import com.lara.securecapita.repository.RoleRepository;
import com.lara.securecapita.repository.UserRepository;
import com.lara.securecapita.rowmapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static com.lara.securecapita.enumeration.RoleType.ROLE_USER;
import static com.lara.securecapita.enumeration.VerificationType.ACCOUNT;
import static com.lara.securecapita.query.UserQuery.COUNT_USER_EMAIL_QUERY;
import static com.lara.securecapita.query.UserQuery.INSERT_ACCOUNT_VERIFICATION_URL_QUERY;
import static com.lara.securecapita.query.UserQuery.INSERT_USER_QUERY;
import static com.lara.securecapita.query.UserQuery.SELECT_USER_BY_EMAIL_QUERY;
import static java.util.Objects.requireNonNull;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {
    public static final String EMAIL = "email";
    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public User create(User user) {
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) {
            throw new ApiException("Email already in use. Please use a different email and try again");
        }
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(requireNonNull(holder.getKey()).longValue());
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            String verificationrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationrl));
            // TODO: Send email to user with verification url
            //emailService.sendVerification(user.getFirstName(), user.getEmail(), verificationrl, ACCOUNT.getType());
            user.setEnabled(false);
            user.setIsNotLocked(true);
            return user;
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again");
        }
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return false;
    }

    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of(EMAIL, email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue(EMAIL, user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if (user == null) {
            log.info("User not found in the database: {}", email);
            throw new UsernameNotFoundException("User not found in the database");
        }

        log.info("User found in the database: {}", email);
        return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()).getPermission());
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of(EMAIL, email), new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No user found by email: " + email);
        } catch (Exception exception) {
            log.error("Error occurred while trying to fetch user by email: {}", email);
            throw new ApiException("An error occurred. Please try again");
        }
    }
}
