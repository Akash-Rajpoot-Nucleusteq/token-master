package in.nucleusteq.plasma.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "assets_allocation")
public class AssetAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assets_allocation_id")
    private int assetsAllocationId;
    @Column(name = "allocation_date")
    private Date allocationDate;
    @Column(name = "deallocation_date")
    private Date deallocationDate;
    @OneToOne
    @JoinColumn(name = "assets_id")
    private Asset assetsId;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employeeId;

}
