package net.guides.springboot2.springboot2swagger2.service;

import net.guides.springboot2.springboot2swagger2.dto.AnnualLeaveRequestDto;
import net.guides.springboot2.springboot2swagger2.enums.EnumAnnualLeaveStatus;
import net.guides.springboot2.springboot2swagger2.exception.AnnualLeaveNotAvailableException;
import net.guides.springboot2.springboot2swagger2.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2swagger2.model.AnnualLeave;
import net.guides.springboot2.springboot2swagger2.model.Employee;
import net.guides.springboot2.springboot2swagger2.repository.AnnualLeaveRepository;
import net.guides.springboot2.springboot2swagger2.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Locale;
import static java.util.Calendar.*;
import java.util.Date;

import static java.util.Calendar.YEAR;

@Service
public class AnnualLeaveRequestDtoService {

    @Autowired
    AnnualLeaveRepository annualLeaveRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public AnnualLeave saveAnnualLeave(AnnualLeaveRequestDto dto)  throws ResourceNotFoundException,AnnualLeaveNotAvailableException{
        validateAnnualLeaveRequestDto(dto);

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + dto.getEmployeeId()));

        AnnualLeave annualLeave = new AnnualLeave();
        annualLeave.setStartDate(truncate(dto.getStartDate()));
        annualLeave.setEndDate(truncate(dto.getEndDate()));
        annualLeave.setEmployee(employee);
        annualLeave.setDays(new Long(getWorkingDaysBetweenTwoDates(annualLeave.getStartDate(),annualLeave.getEndDate())));
        annualLeave.setStatus(EnumAnnualLeaveStatus.WAITING);

        final AnnualLeave updatedAnnualLeave = annualLeaveRepository.save(annualLeave);
        return updatedAnnualLeave;
    }

    private void validateAnnualLeaveRequestDto(AnnualLeaveRequestDto dto) throws AnnualLeaveNotAvailableException, ResourceNotFoundException {

        availableDaysControl(dto);

        areDatesConflicted(dto);

    }

    private void availableDaysControl(AnnualLeaveRequestDto dto) throws AnnualLeaveNotAvailableException,ResourceNotFoundException {

        Integer sumOfUsedDays = annualLeaveRepository.getSumAnnualDaysForThisYearByEmployeeID(dto.getEmployeeId());

        Integer claimDays = getAnnualLeaveClaimByEmployeeId(dto.getEmployeeId());

        Integer availableDays = claimDays - sumOfUsedDays;

        Integer days = getWorkingDaysBetweenTwoDates(dto.getStartDate(), dto.getEndDate());

        if (days > availableDays)
            throw new AnnualLeaveNotAvailableException("EmployeeId: " + dto.getEmployeeId() + " has " + availableDays + " days for Annual Leave");

    }

    private void areDatesConflicted(AnnualLeaveRequestDto dto) throws AnnualLeaveNotAvailableException {
            Integer result =  annualLeaveRepository.areDatesConflicted(truncate(dto.getStartDate()),truncate(dto.getEndDate()),dto.getEmployeeId());
            if(result > 0) throw new AnnualLeaveNotAvailableException("Dates conflict with persisted Annual Leave for EmployeeID: "+ dto.getEmployeeId());
    }

    public static Date truncate(Date date) {
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    private Integer getAnnualLeaveClaimByEmployeeId(Long EmployeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(EmployeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Annual Leave not found for this id :: " + EmployeeId));

        int yearsOfWork = getDiffYears(employee.getEmploymentDate(), new Date());

        if (yearsOfWork == 0) return 5;
        if (yearsOfWork > 0 && yearsOfWork < 6) return 15;
        if (yearsOfWork > 5 && yearsOfWork < 16) return 18;
        if (yearsOfWork > 15) return 24;
        return 0;
    }

    public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        startDate=truncate(startDate);
        endDate = truncate(endDate);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 1;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays + 1;
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

}
