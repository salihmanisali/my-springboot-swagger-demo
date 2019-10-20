package net.guides.springboot2.springboot2jpaswagger2.controller;


import net.guides.springboot2.springboot2swagger2.Springboot2Swagger2Application;
import net.guides.springboot2.springboot2swagger2.enums.EnumAnnualLeaveStatus;
import net.guides.springboot2.springboot2swagger2.model.AnnualLeave;
import net.guides.springboot2.springboot2swagger2.model.Employee;
import net.guides.springboot2.springboot2swagger2.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Springboot2Swagger2Application.class})
public class AnnualLeaveControllerTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EmployeeRepository employeeRepository;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void testGetAllAnnualLeaves() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/annualLeaves",
				HttpMethod.GET, entity, String.class);

		assertNotNull(response.getBody());
	}

	@Test
	public void testGetAnnualLeaveById() {
		AnnualLeave annualLeave = restTemplate.getForObject(getRootUrl() + "/annualLeaves/1", AnnualLeave.class);
		System.out.println(annualLeave.getId());
		assertNotNull(annualLeave);
	}

	@Test
	public void testCreateAnnualLeave() {
		Employee employeeTest = employeeRepository.findByFirstName("admin");

		AnnualLeave annualLeave = new AnnualLeave();
		annualLeave.setDays(1L);
		annualLeave.setStartDate(new Date());
		annualLeave.setEndDate(new Date());
		annualLeave.setStatus(EnumAnnualLeaveStatus.WAITING);
		annualLeave.setEmployee(employeeTest);


		ResponseEntity<AnnualLeave> postResponse = restTemplate.postForEntity(getRootUrl() + "/annualLeaves", annualLeave, AnnualLeave.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdateAnnualLeave() {
		int id = 1;
		AnnualLeave annualLeave = restTemplate.getForObject(getRootUrl() + "/annualLeaves/" + id, AnnualLeave.class);
		annualLeave.setStartDate(new Date());

		restTemplate.put(getRootUrl() + "/annualLeaves/" + id, annualLeave);

		AnnualLeave updatedAnnualLeave = restTemplate.getForObject(getRootUrl() + "/annualLeaves/" + id, AnnualLeave.class);
		assertNotNull(updatedAnnualLeave);
	}

	@Test
	public void testDeleteAnnualLeave() {
		int id = 2;
		AnnualLeave annualLeave = restTemplate.getForObject(getRootUrl() + "/annualLeaves/" + id, AnnualLeave.class);
		assertNotNull(annualLeave);

		restTemplate.delete(getRootUrl() + "/annualLeaves/" + id);

		try {
			annualLeave = restTemplate.getForObject(getRootUrl() + "/annualLeaves/" + id, AnnualLeave.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
