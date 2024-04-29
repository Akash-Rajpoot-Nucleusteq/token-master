package in.nucleusteq.plasma.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.nucleusteq.plasma.entity.Employee;

/**
 * Repository interface for managing Employee entities.
 */
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    /**
     * Finds an employee by their user ID.
     *
     * @param employeeId The ID of the employee.
     * @return An optional containing the employee if found, otherwise empty.
     */
    Optional<Employee> findByUserId(String employeeId);

    /**
     * Finds an employee by their email.
     *
     * @param userName The email of the employee.
     * @return The employee with the specified email.
     */
    Employee getByEmail(String userName);
}
