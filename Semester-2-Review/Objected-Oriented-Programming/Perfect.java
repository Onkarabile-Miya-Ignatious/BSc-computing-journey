import java.util.Scanner;

public class Perfect
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        System.out.print(" ENTER AN INTEGER LIMIT: ");
        int limit = input.nextInt();
        System.out.print("PERFECT NUMBERS: ");


        for (int i = 2; i <= limit; i++)
        {
            int sum =0;
            for (int j = 1; j <= i/2; j++)
            {
                if (i % j == 0)
                {
                    sum+=j;
                }
            }
            if(sum == i)
            {
                System.out.print(i + " ");
            }
        }
    }
}
