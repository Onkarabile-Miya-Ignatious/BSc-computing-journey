import java.util.Scanner;

public class Fibonacci {

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.print(" ENTER THE NUMBER OF FIBONACCI TERMS YOU WANT TO SEE: ");
        int number = input.nextInt();
        for (int i = 0; i < number; i++)
        {
            System.out.print(fibonacci(i) + " ");
        }
    }
    public static int fibonacci(int n)
    {
        if (n ==0)
        {
            return 0;
        }
        else if ( n == 1)
        {
            return 1;
        }
        return fibonacci(n-1) + fibonacci(n-2);
    }
}