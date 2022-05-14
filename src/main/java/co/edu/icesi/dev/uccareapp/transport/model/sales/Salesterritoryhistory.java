package co.edu.icesi.dev.uccareapp.transport.model.sales;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * The persistent class for the salesterritoryhistory database table.
 *
 */
@Entity
@NamedQuery(name = "Salesterritoryhistory.findAll", query = "SELECT s FROM Salesterritoryhistory s")
public class Salesterritoryhistory implements Serializable {
	private static final long serialVersionUID = 1L;

//	@EmbeddedId
//	private SalesterritoryhistoryPK id;
	@Id
	@SequenceGenerator(name = "SALESTERRITORYHISTORY_ID_GENERATOR", allocationSize = 1, sequenceName = "SALESTERRITORYHISTORY_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALESTERRITORYHISTORY_ID_GENERATOR")
	private Integer id;

	private Timestamp enddate;

	private Timestamp modifieddate;
	
	private Timestamp startdate;

	private Integer rowguid;

	// bi-directional many-to-one association to Salesperson
	@NotNull
	@ManyToOne
	@JoinColumn(name = "businessentityid")
	private Salesperson salesPerson;

	// bi-directional many-to-one association to salesTerritory
	@NotNull
	@ManyToOne
	@JoinColumn(name = "territoryid")
	private Salesterritory salesTerritory;

	public Salesterritoryhistory() {
	}

	public Timestamp getEnddate() {
		return this.enddate;
	}

//	public SalesterritoryhistoryPK getId() {
//		return this.id;
//	}
	
	public Integer getId() {
		return this.id;
	}

	public Timestamp getModifieddate() {
		return this.modifieddate;
	}

	public Integer getRowguid() {
		return this.rowguid;
	}


	public Salesterritory getSalesTerritory() {
		return this.salesTerritory;
	}

	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}

//	public void setId(SalesterritoryhistoryPK id) {
//		this.id = id;
//	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setModifieddate(Timestamp modifieddate) {
		this.modifieddate = modifieddate;
	}

	public void setRowguid(Integer rowguid) {
		this.rowguid = rowguid;
	}

	public Salesperson getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(Salesperson salesPerson) {
		this.salesPerson = salesPerson;
	}

	public void setSalesTerritory(Salesterritory salesTerritory) {
		this.salesTerritory = salesTerritory;
	}

	public Timestamp getStartdate() {
		return startdate;
	}

	public void setStartdate(Timestamp startdate) {
		this.startdate = startdate;
	}

}