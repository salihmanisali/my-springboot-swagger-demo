package net.guides.springboot2.springboot2swagger2.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.guides.springboot2.springboot2swagger2.enums.EnumAnnualLeaveStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "annual_leaves")
@ApiModel(description="All details about the annual leave. ")
public class AnnualLeave {

	@ApiModelProperty(notes = "The database generated annual leave ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ApiModelProperty(notes = "The start date of the annual leave")
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date startDate;

	@ApiModelProperty(notes = "The end date of the annual leave")
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date endDate;

	@ApiModelProperty(notes = "Days of the annual leave")
	@Column
	@NotNull
	private Long days;

	@ApiModelProperty(notes = "Annual leave status")
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	@NotNull
	private EnumAnnualLeaveStatus status;

	@ApiModelProperty(notes = "Annual leave of the Employe")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idEmployees")
	@NotNull
	private Employee employee;


	public AnnualLeave() {

	}

	public AnnualLeave(@NotNull Date startDate, @NotNull Date endDate, Long days, EnumAnnualLeaveStatus status, Employee employee) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.days = days;
		this.status = status;
		this.employee = employee;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getDays() {
		return days;
	}

	public void setDays(Long days) {
		this.days = days;
	}

	public EnumAnnualLeaveStatus getStatus() {
		return status;
	}

	public void setStatus(EnumAnnualLeaveStatus status) {
		this.status = status;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "AnnualLeave{" +
				"id=" + id +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", days=" + days +
				", status=" + status +
				", employee=" + employee +
				'}';
	}
}
