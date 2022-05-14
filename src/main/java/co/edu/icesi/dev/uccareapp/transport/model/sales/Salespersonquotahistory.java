package co.edu.icesi.dev.uccareapp.transport.model.sales;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.PositiveOrZero;

/**
 * The persistent class for the salespersonquotahistory database table.
 *
 */
@Entity
@NamedQuery(name = "Salespersonquotahistory.findAll", query = "SELECT s FROM Salespersonquotahistory s")
public class Salespersonquotahistory implements Serializable {
	private static final long serialVersionUID = 1L;

//	@EmbeddedId
//	private SalespersonquotahistoryPK id;
	@Id
	@SequenceGenerator(name = "SALESPERSONQUOTAHISTORY_ID_GENERATOR", allocationSize = 1, sequenceName = "SALESPERSONQUOTAHISTORY_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALESPERSONQUOTAHISTORY_ID_GENERATOR")
	private Integer id;
	
	private Timestamp modifieddate;

	private Integer rowguid;

	@NotNull
	@PositiveOrZero
	private BigDecimal salesquota;

	// bi-directional many-to-one association to Salesperson
	@NotNull
	@ManyToOne
	@JoinColumn(name = "businessentityid")
	private Salesperson salesperson;
		
		
	public Salesperson getSalesperson() {
		return salesperson;
	}

	public void setSalesperson(Salesperson salesperson) {
		this.salesperson = salesperson;
	}

	public Salespersonquotahistory() {
	}
	
	public Integer getId() {
		return this.id;
	}

	public Timestamp getModifieddate() {
		return this.modifieddate;
	}

	public Integer getRowguid() {
		return this.rowguid;
	}

	public BigDecimal getSalesquota() {
		return this.salesquota;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setModifieddate(Timestamp modifieddate) {
		this.modifieddate = modifieddate;
	}

	public void setRowguid(Integer rowguid) {
		this.rowguid = rowguid;
	}

	public void setSalesquota(BigDecimal salesquota) {
		this.salesquota = salesquota;
	}

}