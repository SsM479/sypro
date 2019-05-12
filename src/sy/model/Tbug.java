package sy.model;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tbug entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TBUG", schema = "")
public class Tbug implements java.io.Serializable {

	public Tbug(String id, String name, Date createdatetime) {
		this.id = id;
		this.name = name;
		this.createdatetime = createdatetime;
	}

	// Fields

	private String id;
	private String name;
	private Clob note;
	private Date createdatetime;

	// Constructors

	/** default constructor */
	public Tbug() {
	}

	/** minimal constructor */
	public Tbug(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public Tbug(String id, String name, Clob note, Date createdatetime) {
		this.id = id;
		this.name = name;
		this.note = note;
		this.createdatetime = createdatetime;
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

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "NOTE")
	public Clob getNote() {
		return this.note;
	}

	public void setNote(Clob note) {
		this.note = note;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATETIME", length = 7)
	public Date getCreatedatetime() {
		return this.createdatetime;
	}

	public void setCreatedatetime(Date createdatetime) {
		this.createdatetime = createdatetime;
	}

}