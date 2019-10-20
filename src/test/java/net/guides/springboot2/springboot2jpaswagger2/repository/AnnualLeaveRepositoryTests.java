package net.guides.springboot2.springboot2jpaswagger2.repository;

import net.guides.springboot2.springboot2swagger2.Springboot2Swagger2Application;
import net.guides.springboot2.springboot2swagger2.enums.EnumAnnualLeaveStatus;
import net.guides.springboot2.springboot2swagger2.model.AnnualLeave;
import net.guides.springboot2.springboot2swagger2.model.Employee;
import net.guides.springboot2.springboot2swagger2.repository.AnnualLeaveRepository;
import net.guides.springboot2.springboot2swagger2.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Springboot2Swagger2Application.class})
@DataJpaTest
public class AnnualLeaveRepositoryTests {
	
	@Autowired
	private AnnualLeaveRepository annualLeaveRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testSaveAnnualLeave() {
		Employee employeeTest = employeeRepository.findByFirstName("admin");

		AnnualLeave annualLeave = new AnnualLeave(new Date(),new Date(), 0L,EnumAnnualLeaveStatus.WAITING,employeeTest);
		Long id = entityManager.persistAndGetId(annualLeave, Long.class);
		assertNotNull(id);

		List<AnnualLeave> annualLeave2 = annualLeaveRepository.findAnnualLeavesByEmployeeId(employeeTest.getId());
		assertNotNull(annualLeave);
		assertNotNull(annualLeave2);
	}

	@Test
	public void testDeleteAnnualLeave() {
		Employee employeeTest = employeeRepository.findByFirstName("admin");

		AnnualLeave annualLeave = new AnnualLeave(new Date(),new Date(), 0L,EnumAnnualLeaveStatus.WAITING,employeeTest);

		annualLeaveRepository.save(annualLeave);
		annualLeaveRepository.delete(annualLeave);
	}
}
