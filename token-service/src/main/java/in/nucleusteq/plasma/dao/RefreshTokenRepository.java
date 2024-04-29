package in.nucleusteq.plasma.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.nucleusteq.plasma.entity.RefreshToken;

/**
 * Repository interface for managing RefreshToken entities.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    /**
     * Finds a refresh token in the repository based on the provided token.
     *
     * @param token The refresh token to search for.
     * @return An Optional containing the RefreshToken if found, otherwise an empty
     *         Optional.
     */
    Optional<RefreshToken> findByRefreshToken(String token);
}
