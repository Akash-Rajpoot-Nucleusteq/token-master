package in.nucleusteq.plasma.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "document")
public class Document {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "document_id")
	private int documentId;
	@Column(name = "document_name")
	private String documentName;
	@Column(name = "document_path")
	private String documentPath;
	@ManyToOne
	@JoinColumn(name = "user_personal_detail_id")
	private UserPersonalDetail userPersonalDetail;
	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
	private List<DocumentAttribute> documentAttributes;

}
