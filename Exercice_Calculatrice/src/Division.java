public class Division implements IOperation
{
    @Override
    public double Apply(double a, double b)
    {
        if (b == 0)
        {
            throw new ArithmeticException("Division par zéro");
        }
        return a / b;
    }

}
