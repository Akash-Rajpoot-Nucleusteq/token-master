package in.nucleusteq.plasma.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenData {
    private String employeeId;
    /**
     * user first name.
     */
    private String firstName;
    /**
     * user last name.
     */
    private String lastName;
    /**
     * unique email.
     */
    private String email;
    /**
     * user phone number.
     */
    private String mobileNo;
    /**
     * job name to which user is associated.
     */
    private String Designation;
    /**
     * user role in the system.
     */
    private String role;
    /**
     * user password.
     */
    private char[] password;
}
