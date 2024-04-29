package in.nucleusteq.plasma.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_address")
public class UserAddress {
    /**
     * id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * address1.
     */
    @Column(name = "address1")
    private String address1;
    /**
     * address2.
     */
    @Column(name = "address2")
    private String address2;
    /**
     * city.
     */
    @Column(name = "city")
    private String city;
    /**
     * state.
     */
    @Column(name = "state")
    private String state;
    /**
     * country.
     */
    @Column(name = "country")
    private String country;
    /**
     * zipCode.
     */
    @Column(name = "zip_code")
    private int zipCode;
    /**
     * userPersonalDetails.
     */
    @OneToOne(mappedBy = "userAddress")
    @JsonBackReference(value = "address_details")
    private UserPersonalDetail userPersonalDetail;
	public UserAddress(String address1, String address2, String city, String state, String country, int zipCode) {
		super();
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
	}
}
