package sy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * TroleTresource entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TROLE_TRESOURCE", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
public class TroleTresource implements java.io.Serializable {

	// Fields

	private String id;
	private Trole trole;
	private Tresource tresource;

	// Constructors

	/** default constructor */
	public TroleTresource() {
	}

	/** full constructor */
	public TroleTresource(String id, Trole trole, Tresource tresource) {
		this.id = id;
		this.trole = trole;
		this.tresource = tresource;
	}

	// Property accessors
	@Id
	@Column(name = "ID", nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID", nullable = false)
	public Trole getTrole() {
		return this.trole;
	}

	public void setTrole(Trole trole) {
		this.trole = trole;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOURCE_ID", nullable = false)
	public Tresource getTresource() {
		return this.tresource;
	}

	public void setTresource(Tresource tresource) {
		this.tresource = tresource;
	}

}