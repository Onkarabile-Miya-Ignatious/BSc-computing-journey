import java.util.Scanner;

public class TestTeam
{
    public static void main(String[] args)
    {
        System.out.println("TEAM 1: ");
       Team team1 = setTeamData();

        System.out.println("\nTEAM 2: ");
        Team team2 = setTeamData();

        System.out.println("\nTEAM 3: ");
        Team team3 = setTeamData();

        System.out.println("\nUNIVERSAL MOTTO: "+ Team.MOTTO);

        System.out.println("\n         DATA FOR TEAM 1: \nSCHOOL NAME: "+ team1.getSchoolName()+"\nSPORT NAME: "+ team1.getSport()+ "\nTEAM NAME: "+team1.getTeamName());
        System.out.println("\n         DATA FOR TEAM 2: \nSCHOOL NAME: "+ team2.getSchoolName()+"\nSPORT NAME: "+ team2.getSport()+ "\nTEAM NAME: "+team2.getTeamName());

        System.out.println("\n         DATA FOR TEAM 3: \nSCHOOL NAME: "+ team3.getSchoolName()+"\nSPORT NAME: "+ team3.getSport()+ "\nTEAM NAME: "+team3.getTeamName());


    }
    public static Team setTeamData()
    {

        Scanner input = new Scanner(System.in);

        System.out.print("ENTER SCHOOL NAME: ");
        String name = input.nextLine();



        System.out.print("ENTER SPORT NAME: ");
        String sportName = input.nextLine();

        System.out.print("ENTER TEAM NAME: ");
        String teamName = input.nextLine();

        Team team1 = new Team(name, sportName, teamName);
        return team1;
    }
}
class Team
{
    public final static String MOTTO = "Sportsmanship!";
    private final String schoolName;
    private final String sport;
    private final String teamName;

    Team(String schoolName,String sport, String teamName)
    {
        this.schoolName = schoolName;
        this.teamName = teamName;
        this.sport = sport;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getTeamName() {
        return teamName;
    }
    public String getSport()
    {
        return sport;
    }
}
