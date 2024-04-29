package in.nucleusteq.plasma.dao;

import in.nucleusteq.plasma.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken findByEmail(String email);
//    RefreshToken checkTokenForInvalidation(String token, String email);
}
