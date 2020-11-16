public class Complex {

    private double real, imag;

    public Complex(double _real, double _imag) {
        real = _real;
        imag = _imag;
    }



    public void add(Complex other) {
        real = real + other.real;
        imag = imag + other.imag;
    }

    public void sub(Complex other) {
        real = real - other.real;
        imag = imag - other.imag;
    }

    public void mult(Complex other) {
//        z 1 ⋅z 2 := (x 1 ⋅x 2 – y 1 ⋅y 2 | x 1 ⋅y 2 + x 2 ⋅y 1 )
        double newreal = real * other.real - imag * other.imag;
        double newimag = real * other.imag + other.real * imag;
        setReal(newreal);
        setImag(newimag);

    }

    public double abs() {
        return Math.sqrt(real * real + imag * imag);
    }


    //Standard GETTER/SETTER

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImag() {
        return imag;
    }

    public void setImag(double imag) {
        this.imag = imag;
    }
}
