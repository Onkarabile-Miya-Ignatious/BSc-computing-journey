import java.util.Scanner;

public class TestLease {
    public static void main(String[] args)
    {
        Lease lease1, lease2, lease3, lease4;
        System.out.println("DATA FOR LEASE 1 OBJECT");
        lease1 = getData();

        System.out.println("\nDATA FOR LEASE 2 OBJECT");
        lease2 = getData();

        System.out.println("\nDATA FOR LEASE 3 OBJECT");
        lease3 = getData();

        System.out.println("DATA FOR LEASE 4 OBJECT");
        lease4 = new Lease();

        System.out.println("\nLEASE 1: ");

        showValues(lease1);
        lease1.addPetFee();
        System.out.println("\nLEASE 1 AFTER THE ADDITION OF THE PET POLICY FEE:");
        showValues(lease1);

        System.out.println("\nLEASE 2: ");

        showValues(lease2);

        System.out.println("\nLEASE 3: ");

        showValues(lease3);

        System.out.println("\nLEASE 4: ");

        showValues(lease4);





    }
    public static Lease getData()
    {
        Scanner input = new Scanner(System.in);
        Lease tempLease = new Lease();

        System.out.print("ENTER APARTMENT NAME: ");
        tempLease.setAptName(input.nextLine());

        System.out.print("ENTER APARTMENT NUMBER: ");
        tempLease.setAptNr(input.nextInt());

        System.out.print("ENTER MONTHLY RENT: ");
        tempLease.setRent(input.nextDouble());

        System.out.print("ENTER TERM IN MONTHS: ");
        tempLease.setTerm(input.nextInt());
        input.nextLine();

        return tempLease;
    }
    public static void showValues(Lease l)
    {
        System.out.println("APARTMENT NAME: "+ l.getAptName()+ "\nAPARTMENT NUMBER: "+l.getAptNr()+ "\nMONTHLY RENT: "+l.getRent()+ "\nTERM: "+ l.getTerm());
    }

}
class Lease
{
    private String aptName;
    private int aptNr;
    private double rent;
    private  int term;

    Lease()
    {
        this.aptName = "XXX";
        this.aptNr = 0;
        this.rent = 1000;
        this.term = 12;
    }

    public void setAptName(String aptName) {
        this.aptName = aptName;
    }

    public void setAptNr(int aptNr) {
        this.aptNr = aptNr;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getAptName() {
        return aptName;
    }

    public int getAptNr() {
        return aptNr;
    }

    public double getRent() {
        return rent;
    }

    public int getTerm() {
        return term;
    }
    public void addPetFee()
    {
        this.rent +=10;
        explainPetPolicy();
    }
    public static void explainPetPolicy()
    {
        System.out.println("\nA PET POLICY FEE OF $10 HAS BEEN ADDED");
    }

}

