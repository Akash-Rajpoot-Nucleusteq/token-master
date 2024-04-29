package in.nucleusteq.plasma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * the email id of user.
     */
    @Column(name = "email")
    private String email;

    /**
     * The refresh token.
     */
    @Column(name = "refresh_token")
    private String refreshToken;

    /**
     * The refresh token date.
     */
    @Column(name = "refresh_token_dt")
    private Instant refreshTokenDt;

    /**
     * The invalidated token.
     */
    @Column(name = "invalidated_token")
    private String invalidatedToken;
}
