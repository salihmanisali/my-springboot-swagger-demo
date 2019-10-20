package net.guides.springboot2.springboot2swagger2.controller;

import io.swagger.annotations.*;
import net.guides.springboot2.springboot2swagger2.dto.AnnualLeaveRequestDto;
import net.guides.springboot2.springboot2swagger2.enums.EnumAnnualLeaveStatus;
import net.guides.springboot2.springboot2swagger2.exception.AnnualLeaveNotAvailableException;
import net.guides.springboot2.springboot2swagger2.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2swagger2.model.AnnualLeave;
import net.guides.springboot2.springboot2swagger2.repository.AnnualLeaveRepository;
import net.guides.springboot2.springboot2swagger2.repository.EmployeeRepository;
import net.guides.springboot2.springboot2swagger2.service.AnnualLeaveRequestDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Api(value="Annual Leave Management System", description="Operations pertaining to Annual Leave in AnnualLeave Management System")
public class AnnualLeaveController {

	@Autowired
	private AnnualLeaveRepository annualLeaveRepository;

	@Autowired
	private AnnualLeaveRequestDtoService annualLeaveRequestDtoService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@ApiOperation(value = "View a list of available annual leaves", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	@GetMapping("/annualLeaves")
	public List<AnnualLeave> getAllAnnualLeaves() {
		return annualLeaveRepository.findAll();
	}

	@ApiOperation(value = "Get an Annual Leave by Id")
	@GetMapping("/annualLeaves/{id}")
	public ResponseEntity<AnnualLeave> getAnnualLeaveById(
			@ApiParam(value = "Annual Leave id", required = true)
			@PathVariable(value = "id") Long annualLeaveId)
			throws ResourceNotFoundException {
		AnnualLeave annualLeave = annualLeaveRepository.findById(annualLeaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Annual Leave not found for this id :: " + annualLeaveId));
		return ResponseEntity.ok().body(annualLeave);
	}

	@ApiOperation(value = "Add an Annual Leave")
	@PostMapping("/annualLeaves")
	public AnnualLeave createAnnualLeave(
			@ApiParam(value = "Annual Leave object to store in database table", required = true)
			@Valid @RequestBody AnnualLeave annualLeave) {
		return annualLeaveRepository.save(annualLeave);
	}

	@ApiOperation(value = "Update an Annual Leave")
	@PutMapping("/annualLeaves/{id}")
	public ResponseEntity<AnnualLeave> updateAnnualLeave(
			@ApiParam(value = "Annual Leave Id to update Annual Leave object", required = true)
			@PathVariable(value = "id") Long annualLeaveId,

			@ApiParam(value = "Update Annual Leave object", required = true)
			@Valid @RequestBody AnnualLeave annualLeaveDetails
	) throws ResourceNotFoundException {
		AnnualLeave annualLeave = annualLeaveRepository.findById(annualLeaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Annual Leave not found for this id :: " + annualLeaveId));

		annualLeave.setStartDate(annualLeaveDetails.getStartDate());
		annualLeave.setEndDate(annualLeaveDetails.getEndDate());
		annualLeave.setEmployee(annualLeaveDetails.getEmployee());
		annualLeave.setStatus(annualLeaveDetails.getStatus());

		final AnnualLeave updatedAnnualLeave = annualLeaveRepository.save(annualLeave);
		return ResponseEntity.ok(updatedAnnualLeave);
	}

	@ApiOperation(value = "Reject an Annual Leave")
	@PutMapping("/rejectAnnualLeave/{id}")
	public ResponseEntity<AnnualLeave> rejectAnnualLeave(
			@ApiParam(value = "Annual Leave Id to reject", required = true)
			@PathVariable(value = "id") Long annualLeaveId
	) throws ResourceNotFoundException {
		AnnualLeave annualLeave = annualLeaveRepository.findById(annualLeaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Annual Leave not found for this id :: " + annualLeaveId));

		annualLeave.setStatus(EnumAnnualLeaveStatus.REJECT);

		final AnnualLeave updatedAnnualLeave = annualLeaveRepository.save(annualLeave);
		return ResponseEntity.ok(updatedAnnualLeave);
	}

	@ApiOperation(value = "Commit an Annual Leave")
	@PutMapping("/commitAnnualLeave/{id}")
	public ResponseEntity<AnnualLeave> commitAnnualLeave(
			@ApiParam(value = "Annual Leave Id to commit", required = true)
			@PathVariable(value = "id") Long annualLeaveId
	) throws ResourceNotFoundException {
		AnnualLeave annualLeave = annualLeaveRepository.findById(annualLeaveId)
				.orElseThrow(() -> new ResourceNotFoundException("AnnualLeave not found for this id :: " + annualLeaveId));

		annualLeave.setStatus(EnumAnnualLeaveStatus.COMMIT);

		final AnnualLeave updatedAnnualLeave = annualLeaveRepository.save(annualLeave);
		return ResponseEntity.ok(updatedAnnualLeave);
	}

	@ApiOperation(value = "Find All Annual Leaves for date between Start date and End Date")
	@PutMapping("/findAllByDate")
	public ResponseEntity<List<AnnualLeave>> findAllByDate(
			@ApiParam(value = "Date to query between Start date and End Date ", required = true)
			@Valid @RequestBody Date date
	)  throws ResourceNotFoundException {
		List<AnnualLeave> list = annualLeaveRepository.findAnnualLeavesByDate(date);
		if(list==null || list.size()==0) throw new ResourceNotFoundException("No Annual Leaves found for this Date");

		return ResponseEntity.ok(list);
	}

	@ApiOperation(value = "Find All Commited Annual Leaves  By Employee Id")
	@PutMapping("/findAllByEmployeeId")
	public ResponseEntity<List<AnnualLeave>> findAllByEmployeeId(
			@ApiParam(value = "Employee Id", required = true)
			@Valid @RequestBody Long id
	) throws ResourceNotFoundException {
		List<AnnualLeave> list = annualLeaveRepository.findAnnualLeavesByEmployeeId(id);

		if(list==null || list.size()==0) throw new ResourceNotFoundException("No Annual Leaves found for this Employee Id :: " + id);

		return ResponseEntity.ok(list);
	}

	@ApiOperation(value = "Find All waiting Annual Leaves")
	@PutMapping("/findAllWaitingAnnualLeaves")
	public ResponseEntity<List<AnnualLeave>> findAllWaitingAnnualLeaves() throws ResourceNotFoundException  {
		List<AnnualLeave> list = annualLeaveRepository.findAllWaitingAnnualLeaves();
		if(list==null || list.size()==0) throw new ResourceNotFoundException("No Waiting Annual Leaves");

		return ResponseEntity.ok(list);
	}

	@ApiOperation(value = "Request an Annual Leave")
	@PutMapping("/requestAnnualLeave")
	public ResponseEntity<AnnualLeave> requestAnnualLeave(
			@ApiParam(value = "Request an Annual Leave", required = true)
			@Valid @RequestBody AnnualLeaveRequestDto dto
	) throws ResourceNotFoundException, AnnualLeaveNotAvailableException {

		AnnualLeave annualLeave = annualLeaveRequestDtoService.saveAnnualLeave(dto);
		return ResponseEntity.ok(annualLeave);
	}


	@ApiOperation(value = "Delete an Annual Leave")
	@DeleteMapping("/annualLeaves/{id}")
	public Map<String, Boolean> deleteAnnualLeave(
			@ApiParam(value = "AnnualLeave Id from which Annual Leave object will delete from database table", required = true)
			@PathVariable(value = "id") Long annualLeaveId)
			throws ResourceNotFoundException {
		AnnualLeave annualLeave = annualLeaveRepository.findById(annualLeaveId)
				.orElseThrow(() -> new ResourceNotFoundException("AnnualLeave not found for this id :: " + annualLeaveId));

		annualLeaveRepository.delete(annualLeave);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
