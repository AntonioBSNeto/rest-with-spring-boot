package com.example.demo.controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.converts.NumberConverter;
import com.example.demo.exceptions.UnsupportedMathOperationException;
import com.example.demo.math.SimpleMath;

@RestController
public class MathController {

	private final AtomicLong id =  new AtomicLong();
	
	private SimpleMath math = new SimpleMath();

	@GetMapping("sum/{numberOne}/{numberTwo}")
	public Double sum(
		@PathVariable(value = "numberOne") String numberOne,
		@PathVariable(value = "numberTwo") String numberTwo
	) throws Exception {
		if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		return math.sum(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
	}
	
	@GetMapping("sub/{numberOne}/{numberTwo}")
	public Double sub(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo
			) throws Exception {
		if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		return math.sub(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
	}
	
	@GetMapping("mult/{numberOne}/{numberTwo}")
	public Double mult(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo
			) throws Exception {
		if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		return math.mult(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
	}
	
	@GetMapping("div/{numberOne}/{numberTwo}")
	public Double div(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo
			) throws Exception {
		if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		if (NumberConverter.convertToDouble(numberTwo).equals(0D)) {
			throw new UnsupportedMathOperationException("Error division by zero!");
		}
		
		return math.div(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
	}
	
	@GetMapping("sqrt/{numberOne}")
	public Double sqrt(
			@PathVariable(value = "numberOne") String numberOne
			) throws Exception {
		if (!NumberConverter.isNumeric(numberOne)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		if (NumberConverter.convertToDouble(numberOne) < 0) {
			throw new UnsupportedMathOperationException("Error calculating the square root, negative numbers not allowed !");
		}
		
		return math.sqrt(NumberConverter.convertToDouble(numberOne));
	}
	
	@GetMapping("avg/{numberOne}/{numberTwo}")
	public Double avg(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo
			) throws Exception {
		if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		
		return math.avg(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
	}
	
}
