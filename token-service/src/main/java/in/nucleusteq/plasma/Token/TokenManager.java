package in.nucleusteq.plasma.Token;

import in.nucleusteq.plasma.exception.UnauthorizedAccessException;
import in.nucleusteq.plasma.entity.EmployeeDetails;
import in.nucleusteq.plasma.entity.TokenData;

public interface TokenManager {
    /**
     * @param employeeDetails - details of user.
     * @return it return generated token.
     */
    String generateToken(EmployeeDetails employeeDetails);

    /** This function is used to fetch userEmail from token.
     * @param token - token type.
     * @return - TokenData - contains data related to token.
     * @throws in.nucleusteq.plasma.exception.UnauthorizedAccessException
     */
    TokenData getDataFromToken(String token) throws UnauthorizedAccessException;
}
