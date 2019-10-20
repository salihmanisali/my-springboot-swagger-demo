package net.guides.springboot2.springboot2swagger2.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Entity
@Table(name = "employees")
@ApiModel(description="All details about the Employee. ")
public class Employee {

	@ApiModelProperty(notes = "The database generated employee ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ApiModelProperty(notes = "The employee first name")
	@Column
	@NotNull
	private String firstName;

	@ApiModelProperty(notes = "The employee last name")
	@Column
	@NotNull
	private String lastName;

	@ApiModelProperty(notes = "The Start Date of The employee")
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date employmentDate;

	public Employee() {

	}

	public Employee(String firstName, String lastName, Date employmentDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.employmentDate = employmentDate;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getEmploymentDate() {
		return employmentDate;
	}

	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", employmentDate=" + employmentDate +
				'}';
	}
}
