package in.nucleusteq.plasma.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class Employee {
	@Id
	@Column(name = "user_id", unique = true)
	private String userId;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_personal_id")
	@JsonManagedReference(value = "personal_details")
	private UserPersonalDetail userPersonalDetail;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_work_id")
	@JsonManagedReference(value = "user_work_details")
	private UserWorkDetail userWorkDetail;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "finance_id")
	@JsonManagedReference(value = "financial_details")
	private FinancialDetail financialDetail;

	@OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
	private List<UserWorkDetail> recruited;

	@OneToMany(mappedBy = "recruiterManager", cascade = CascadeType.ALL)
	private List<UserWorkDetail> recruiterManaged;

	@OneToMany(mappedBy = "employeeId", fetch = FetchType.LAZY)
	private List<AssetAllocation> assetsAllocation;
}
