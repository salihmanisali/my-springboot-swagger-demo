package net.guides.springboot2.springboot2jpaswagger2.repository;

import net.guides.springboot2.springboot2swagger2.Springboot2Swagger2Application;
import net.guides.springboot2.springboot2swagger2.model.Employee;
import net.guides.springboot2.springboot2swagger2.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Springboot2Swagger2Application.class})
@DataJpaTest
public class EmployeeRepositoryTests {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testSaveEmployee() {

		Employee employee = new Employee("admin", "admin", new Date());
		Long id = entityManager.persistAndGetId(employee, Long.class);
		assertNotNull(id);
		Employee employee2 = employeeRepository.findByFirstName("admin");
		assertNotNull(employee);
		assertEquals(employee2.getFirstName(), employee.getFirstName());
		assertEquals(employee2.getLastName(), employee.getLastName());
	}



	@Test
	public void testGetEmployee() {

		Employee employee = new Employee("admin", "admin", new Date());
		employeeRepository.save(employee);
		Employee employee2 = employeeRepository.findByFirstName("admin");
		assertNotNull(employee);
		assertEquals(employee2.getFirstName(), employee.getFirstName());
		assertEquals(employee2.getLastName(), employee.getLastName());
	}

	@Test
	public void testDeleteEmployee() {
		Employee employee = new Employee("admin", "admin", new Date());
		employeeRepository.save(employee);
		employeeRepository.delete(employee);
	}
}
