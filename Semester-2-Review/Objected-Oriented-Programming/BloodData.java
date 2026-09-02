import java.util.Scanner;

public class TestBloodData
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("ENTER YOUR BLOOD TYPE ( O / A / B / AB): ");
        String bloodType = input.nextLine();

        System.out.print("ENTER rH ( + / - ): ");
        String rh = input.nextLine();

        BloodData p1 = new BloodData();
        BloodData p2 = new BloodData(bloodType,rh);

        System.out.println("          OBJECT 1: \nBLOOD TYPE: "+ p1.getBloodType() + "\nrH: "+ p1.getrH());
        System.out.println("          OBJECT 2: \nBLOOD TYPE: "+ p2.getBloodType() + "\nrH: "+ p2.getrH());

        p1.setBloodType(bloodType);
        p1.setrH(rh);

        System.out.println("          OBJECT 1 AFTER CHANGING: \nBLOOD TYPE: "+ p1.getBloodType() + "\nrH: "+ p1.getrH());


    }

}
class BloodData
{
    private String bloodType;
    private String rH;

    public BloodData()
    {
        this.bloodType = "O";
        this.rH = "+";
    }
    public BloodData(String bloodType, String rH)
    {
        this.rH = rH;
        this.bloodType = bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    public void setrH(String rH)
    {
        this.rH = rH;
    }

    public String getBloodType() {
        return bloodType;
    }
    public String getrH()
    {
        return rH;
    }
}
