import java.util.Scanner;
import java.util.function.ObjIntConsumer;

public class add_multiply_complex {

    //Programming Drill 1.1.1
    public static void main(String[] args){
        ComplexNumber number1; 
        ComplexNumber number2;

        ComplexNumber sum; 
        ComplexNumber product;

        int modulus1; 
        int modulus2; 

        ComplexNumber difference; 
        ComplexNumber quotient;

        ComplexNumber conjugate1;
        ComplexNumber conjugate2; 

        Scanner myScanner = new Scanner(System.in);  // Create a Scanner object

        System.out.println("First Complex Number Real: a");
        int firstComplexReal = myScanner.nextInt();  // Read user input

        System.out.println("First Complex Number Imaginary: b");
        int firstComplexImaginary = myScanner.nextInt();  // Read user input

        System.out.println("Second Complex Number Real : a");
        int secondComplexReal = myScanner.nextInt();  // Read user input

        System.out.println("Second Complex Number Real : b");
        int secondComplexImaginary = myScanner.nextInt();  // Read user input

        number1 = new ComplexNumber(firstComplexReal, firstComplexImaginary);
        number2 = new ComplexNumber(secondComplexReal, secondComplexImaginary);

        sum = new ComplexNumber(number1.getReal() + number2.getReal(), number1.getImaginary() + number2.getImaginary());
        product = new ComplexNumber(number1.getReal()*number2.getReal() -1*number1.getImaginary()*number2.getImaginary(), number1.getReal()*number2.getImaginary() + number1.getImaginary()*number2.getReal());

        modulus1 = number1.getModulus();
        modulus2 = number2.getModulus();

        difference = new ComplexNumber(number1.getReal() - number2.getReal(), number1.getImaginary() - number2.getImaginary());
        quotient = new ComplexNumber((number1.getReal()*number2.getReal() + number1.getImaginary()*number2.getImaginary())/modulus2, (number2.getReal()*number1.getImaginary()-number1.getReal()*number2.getImaginary())/modulus2);

        conjugate1 = number1.getConjugate();
        conjugate2 = number2.getConjugate(); 


        System.out.println();

        System.out.println("Sum: " + sum.getComplex());
        System.out.println("Product: " + product.getComplex());

        System.out.println("Difference: " + difference.getComplex());
        System.out.println("Quotient: " + quotient.getComplex());

        myScanner.close();
        
    }

}