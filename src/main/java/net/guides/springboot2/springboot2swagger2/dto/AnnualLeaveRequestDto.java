package net.guides.springboot2.springboot2swagger2.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(description="All details about the Annual Leave ")
public class AnnualLeaveRequestDto {

    @ApiModelProperty(notes = "The database generated employee ID")
    @NotNull
    private long employeeId;

    @ApiModelProperty(notes = "Start Date of the Annual Leave")
    @NotNull
    private Date startDate;

    @ApiModelProperty(notes = "End Date of the Annual Leave")
    @NotNull
    private Date endDate;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
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

    @Override
    public String toString() {
        return "AnualLeaveRequest{" +
                "employeeId=" + employeeId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
