package net.guides.springboot2.springboot2swagger2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AnnualLeaveNotAvailableException extends Exception{

	private static final long serialVersionUID = 1L;

	public AnnualLeaveNotAvailableException(String message){
    	super(message);
    }
}
