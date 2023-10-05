package com.example.demo.math;

import com.example.demo.exceptions.UnsupportedMathOperationException;

public class SimpleMath {
	
	public Double sum(Double numberOne,Double numberTwo) {
			return numberOne + numberTwo;
		}
		
		public Double sub( Double numberOne, Double numberTwo) {
			return numberOne - numberTwo;
		}
		
		public Double mult(Double numberOne, Double numberTwo) {
			return numberOne * numberTwo;
		}
		
		public Double div(Double numberOne, Double numberTwo) throws Exception {

			if (numberTwo.equals(0D)) {
				throw new UnsupportedMathOperationException("Error division by zero!");
			}
			
			return numberOne / numberTwo;
		}
		
		public Double sqrt(Double numberOne) throws Exception {
			if (numberOne < 0) {
				throw new UnsupportedMathOperationException("Error calculating the square root, negative numbers not allowed !");
			}
			
			return Math.sqrt(numberOne);
		}
		
		public Double avg(Double numberOne, Double numberTwo) {
			return (numberOne + numberTwo) / 2;
		}

}
