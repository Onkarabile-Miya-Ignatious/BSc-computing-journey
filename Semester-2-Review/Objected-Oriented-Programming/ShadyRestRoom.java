import java.util.Scanner;

public class ShadyRestRoom {

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int choice;
        String message;
       double price;

        System.out.print("      MENU \n 1. QUEEN BED \n 2. KING BED \n 3. KING AND A PULLOUT COUCH \n ENTER OPTION: ");
        choice = input.nextInt();

        if(choice == 1)
        {
            message = "QUEEN BED";
            price = 125;

        }
        else if(choice == 2)
        {
            message = "KING BED";
            price = 139;
        }
        else if(choice == 3)
        {
            message = "SUITE WITH A KING AND A PULLOUT COUCH";
            price = 165;
        }
        else {
            message = "INVALID CHOICE";
            price = 0;
        }
        System.out.println(message + " - R"+ price);
    }
}
