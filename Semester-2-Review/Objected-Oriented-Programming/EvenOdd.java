import java.util.Scanner;

public class EvenOdd
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        System.out.print("ENTER AN INTEGER: ");
        int number = input.nextInt();
        boolean results =checker(number);

        if (results)
        {
            System.out.println(number + " IS EVEN");
        }
        else
        {
            System.out.println(number+" IS ODD");
        }

    }
    public static boolean checker(int num)
    {
        return (num%2==0);
    }
}
