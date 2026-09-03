import java.util.Scanner;

public class Order
{
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("ENTER INTEGER 1: ");
        int num1 = input.nextInt();

        System.out.print("ENTER INTEGER 2: ");
        int num2 = input.nextInt();

        System.out.print("ENTER INTEGER 3: ");
        int num3 = input.nextInt();

        if (num1 >num2)
        {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }

        if (num2 >num3)
        {
            int temp = num2;
            num2 = num3;
            num3 = temp;
        }
        if (num1 >num2)
        {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }
        System.out.println("ASCENDING ORDER: "+ num1 +" " + num2 + " "+ num3);
        System.out.println("DESCENDING ORDER: "+ num3 +" " + num2 + " "+ num1);


    }
}
