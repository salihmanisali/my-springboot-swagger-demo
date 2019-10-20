package net.guides.springboot2.springboot2swagger2.repository;

import net.guides.springboot2.springboot2swagger2.model.AnnualLeave;
import net.guides.springboot2.springboot2swagger2.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnnualLeaveRepository extends JpaRepository<AnnualLeave, Long>{

    @Query(value = "SELECT * FROM Annual_leaves WHERE status = 'COMMIT' and :date between start_Date and end_Date;", nativeQuery = true)
    List<AnnualLeave> findAnnualLeavesByDate(@Param("date") Date date);

    @Query(value = "SELECT x FROM AnnualLeave  x WHERE x.status = 'COMMIT' and x.employee.id = :id")
    List<AnnualLeave> findAnnualLeavesByEmployeeId(@Param("id") Long id);

    @Query(value = "SELECT x FROM AnnualLeave  x WHERE x.status = 'WAITING'")
    List<AnnualLeave> findAllWaitingAnnualLeaves();


    @Query(value = "SELECT coalesce(sum(days),0)  FROM Annual_leaves WHERE status = 'COMMIT' and YEAR(SYSDATE()) = YEAR(start_Date) and id_employees = :EmployeID", nativeQuery = true)
    Integer getSumAnnualDaysForThisYearByEmployeeID(@Param("EmployeID") Long EmployeID);

    @Query(value = "SELECT count(*) FROM Annual_leaves WHERE (:startDate between  start_Date and end_Date or :endDate between  start_Date and end_Date ) and id_employees = :EmployeID", nativeQuery = true)
    Integer areDatesConflicted(@Param("startDate") Date startDate, @Param("endDate") Date endDate,@Param("EmployeID") Long EmployeID);
}
