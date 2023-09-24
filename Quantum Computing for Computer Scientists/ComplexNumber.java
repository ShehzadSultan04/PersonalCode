public class ComplexNumber {

    int real;
    int imaginary;

    public ComplexNumber(int r, int i) {
        real = r;
        imaginary = i;
    }

    public String getComplex() {
        return real + " + " + imaginary + "i";
    }

    public Integer getReal() {
        return real; 
    }

    public Integer getImaginary() {
        return imaginary;
    }

    public Integer getModulus() {
        return real^2 + imaginary^2;
    }

    public ComplexNumber add(ComplexNumber num2) {
        return new ComplexNumber(real + num2.real, imaginary+num2.imaginary);
    }

    public ComplexNumber subtract(ComplexNumber num2) {
        return new ComplexNumber(real - num2.real, imaginary - num2.imaginary);
    }

    public ComplexNumber getConjugate() {
        return new ComplexNumber(real, -imaginary);
    }
}
