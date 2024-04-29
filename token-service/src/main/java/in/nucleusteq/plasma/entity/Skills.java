package in.nucleusteq.plasma.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "skills")
public class Skills {
    /**
     * skillId.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private int skillId;
    /**
     * skillName.
     */
    @Column(name = "skill_name")
    private String skillName;
    /**
     * employeeSkills.
     */
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<UserSkill> employeeSkills;
    /**
     * skills
     */
    public Skills(String skillName) {
        super();
        this.skillName = skillName;
    }
}
