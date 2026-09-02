import java.time.LocalDate;

import java.util.Scanner;

public class TestFitnessTracker
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("ENTER THE NAME OF THE ACTIVITY: ");
        String name = input.nextLine();

        System.out.print("ENTER MINUTES SPENT: ");
        int min = input.nextInt();

        System.out.print("ENTER THE CURRENT YEAR: ");
        int year = input.nextInt();

        System.out.print("ENTER THE MONTH (1-12): ");
        int month = input.nextInt();

        System.out.print("ENTER THE DAY (1-31): ");
        int day = input.nextInt();
        LocalDate date = LocalDate.of(year, month, day);

        FitnessTracker f1 = new FitnessTracker(name,min, date);
        FitnessTracker f2 = new FitnessTracker();

        System.out.println("                   OBJECT 1: \nNAME OF ACTIVITY: "+ f1.getNameOfActivity() + "\nMINUTES SPENT: "+ f1.getMinutes()+ "\nDATE: "+ f1.getDate());
        System.out.println();

        System.out.println("                   OBJECT 2: \nNAME OF ACTIVITY: "+ f2.getNameOfActivity() + "\nMINUTES SPENT: "+ f2.getMinutes()+ "\nDATE: "+ f2.getDate());



    }

}
class FitnessTracker
{
    private final String nameOfActivity;
    private final int minutes;

    private final LocalDate date;

    public FitnessTracker()
    {
        this.nameOfActivity = "running";
        this.minutes = 0;
        int currentYear = LocalDate.now().getYear();
        this.date = LocalDate.of(currentYear,1,1);
    }
    
    public FitnessTracker(String nameOfActivity, int minutes, LocalDate date)
    {
        this.nameOfActivity = nameOfActivity;
        this.minutes = minutes;
        this.date = date;

    }

    public String getNameOfActivity() {
        return nameOfActivity;
    }

    public int getMinutes() {
        return minutes;
    }

    public LocalDate getDate() {
        return date;
    }
}
