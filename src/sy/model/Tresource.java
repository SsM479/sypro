package sy.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Tresource entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TRESOURCE", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Tresource implements java.io.Serializable {

	// Fields

	private String id;
	private Tresource tresource;
	private String text;
	private String url;
	private BigDecimal seq;
	private Set<Tresource> tresources = new HashSet<Tresource>(0);
	private Set<TroleTresource> troleTresources = new HashSet<TroleTresource>(0);

	// Constructors

	/** default constructor */
	public Tresource() {
	}

	/** minimal constructor */
	public Tresource(String id, String text) {
		this.id = id;
		this.text = text;
	}

	/** full constructor */
	public Tresource(String id, Tresource tresource, String text, String url, BigDecimal seq, Set<Tresource> tresources, Set<TroleTresource> troleTresources) {
		this.id = id;
		this.tresource = tresource;
		this.text = text;
		this.url = url;
		this.seq = seq;
		this.tresources = tresources;
		this.troleTresources = troleTresources;
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
	@JoinColumn(name = "PID")
	public Tresource getTresource() {
		return this.tresource;
	}

	public void setTresource(Tresource tresource) {
		this.tresource = tresource;
	}

	@Column(name = "TEXT", nullable = false, length = 100)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "URL", length = 200)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "SEQ", precision = 22, scale = 0)
	public BigDecimal getSeq() {
		return this.seq;
	}

	public void setSeq(BigDecimal seq) {
		this.seq = seq;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tresource")
	public Set<Tresource> getTresources() {
		return this.tresources;
	}

	public void setTresources(Set<Tresource> tresources) {
		this.tresources = tresources;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tresource")
	public Set<TroleTresource> getTroleTresources() {
		return this.troleTresources;
	}

	public void setTroleTresources(Set<TroleTresource> troleTresources) {
		this.troleTresources = troleTresources;
	}

}