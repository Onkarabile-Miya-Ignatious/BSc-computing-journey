#include <iostream>
#include <string>
#include <iomanip>
#include <algorithm>


using namespace std;

// GLOBAL VARIABLES
const int MAX_PATIENTS = 15;
string patientNames[MAX_PATIENTS];     // stores patient names
string assignedDoctors[MAX_PATIENTS];  //store assigned doctor's name
int patientDetails[MAX_PATIENTS][3];  // Column 0 = age , column 1 = priority score, column 2 = id
int currentPatientCount = 0;           // Keeps track of how many people are in the hospital

// FUNCTION DECLARATIONS
void showMenu();
void addPatient();
void checkSymptomsAndAssign();
void loginRecetionist();
void loginManager();
void ManagerMenu();
void showWaitingList();
void sortWaitingList();    // BUBBLE SORT
int calculateWaitTime(int place);


// MAIN PROGRAM

int main()
{
    int loginSystem;

    while (true)
    {
        cout << "\n======================================================\n";
        cout << "\n                 WELCOME TO THE                   \n";
        cout << "          INTELLIGENT HOSPITAL WAITING SYSTEM         \n";
        cout << " \n            BY: NEXTGEN MEDICAL                \n";
        cout << "======================================================\n";
        cout << "1. login as the receptionist\n";
        cout << "2. login as manager\n";
        cout << "3. Exit system\n";
        cout << "choose an option (1-3): ";
cin>>loginSystem;
        if (cin.fail())
        {
            cin.clear();
            cin.ignore(1000, '\n');
            cout << "\nInvalid input!!!\n";
            break;
        }

        switch(loginSystem)
        {
        case 1:
            loginRecetionist();
            break;
        case 2:
            loginManager();
            break;


        default:
            cout<< "exiting the system . program terminated"<<endl;
            break;
        }
        if(loginSystem==3)
        {

            break;
        }
    }

    return 0;
}

// FUNCTIONS

void showMenu()

 {

    cout << "\n======================================================\n";
    cout << "          INTELLIGENT HOSPITAL WAITING SYSTEM         \n";
    cout << " \n            BY: NEXTGEN MEDICAL                \n";
    cout << "======================================================\n";
    cout << "1. Add a New Patient\n";
    cout << "2. Check Symptoms & Assign Doctor\n";
    cout << "3. Look at the Waiting List\n";
    cout << "4. Predict Wait Time\n";
    cout << "5. Exit System\n\n";
    cout << "Choose an option (1-5): ";
}

void addPatient()

{

    cout << "\n--- ADD A NEW PATIENT ---\n";

    if (currentPatientCount >= MAX_PATIENTS) {
        cout << " Hospital is full right now!\n";
        return;
    }

    string name;
    cout << " Enter Patient's Full Name: ";
cin.clear();
cin.ignore(1000, '\n');
getline(cin, name);



    while (name.length() < 3)
        {
        cout << " Name too short. Please enter full name: ";

     getline(cin, name);
    }

    int age;
    cout << " ENTER PATIENT AGE: ";
    cin >> age;
while (cin.fail())
{
    cout << "Enter integer only: ";
    cin.clear();
    cin.ignore(1000, '\n');
    cin >> age;
}
    int ID;

    cout << " ENTER PATIENT ID: ";
    cin >> ID;

while (cin.fail())
{
    cout << "Enter integer only: ";
    cin.clear();
    cin.ignore(1000, '\n');
    cin >> ID;
}



    for (int i = 0; i < name.length(); i++)
    {
        name[i] = toupper(name[i]);
    }
    string secureID = name;
    reverse(secureID.begin(), secureID.end());

    patientNames[currentPatientCount] = name;
    assignedDoctors[currentPatientCount] = "Not Assigned Yet";
    patientDetails[currentPatientCount][0] = age; //age
    patientDetails[currentPatientCount][2] = ID; // id
    patientDetails[currentPatientCount][1]= 0; // Priority Score

    currentPatientCount++;

    cout << " Patient " << name << " has been added.\n";
    cout << " PATIENT ID: "<< ID<<endl;
    cout << " Secure File ID Generated: " << secureID << " - Patient " << currentPatientCount << endl;
}

int calculateUrgency(string symptoms) {
    int score = 5;
    if (symptoms.find("CHEST") != string::npos) score += 50;
    if (symptoms.find("BREATHING") != string::npos) score += 40;
    if (symptoms.find("BLEEDING") != string::npos) score += 30;
    if (symptoms.find("STROKE") != string::npos) score += 50;
    if (symptoms.find("UNCONSCIOUS") != string::npos) score += 50;
    if (symptoms.find("FEVER") != string::npos) score += 15;
    if (symptoms.find("HEADACHE") != string::npos) score += 10;
    return score;
}

// BUBBLE SORT

void sortWaitingList()
 {
    for (int i = 0; i < currentPatientCount - 1; i++)
    {
        for (int j = 0; j < currentPatientCount - i - 1; j++)
         {
            //we check scores here of the first patient and the one next to them
            if (patientDetails[j][1] < patientDetails[j + 1][1])
            {
                swap(patientDetails[j][1], patientDetails[j + 1][1]); // Swap Score
                swap(patientDetails[j][0], patientDetails[j + 1][0]); // Swap Age
                swap(patientNames[j], patientNames[j + 1]);           // Swap Name
                swap(assignedDoctors[j], assignedDoctors[j + 1]);     // Swap Doctor
                swap(patientDetails[j][2], patientDetails[j + 1][2]); // swap id
            }
        }
    }
}

void showWaitingList()
{
    cout << "\n--- CURRENT WAITING LIST ---\n";

    if (currentPatientCount == 0)
    {
        cout << " No patients are waiting right now.\n";
        return;
    }

    cout << left << setw(5) << "No."
         << setw(20) << "Patient Name"
         << setw(10) << "Age"
         << setw(5)  << "ID"
         << setw(35) << "Assigned Doctor"
         << setw(10) << "Score" << endl;

    cout << "--------------------------------------------------------------------------------\n";

    for (int i = 0; i < currentPatientCount; i++)
    {
        cout << setw(5) << (i + 1)
             << setw(20) << patientNames[i]
             << setw(10) << patientDetails[i][0]   // Prints age
             << setw(5)  << patientDetails[i][2] // prints id
             << setw(35) << assignedDoctors[i]
             << setw(10) << patientDetails[i][1] << endl;  // prints score
    }
}

void checkSymptomsAndAssign()
{
    cout << "\n--- CHECK SYMPTOMS & ASSIGN DOCTOR ---\n";

    if (currentPatientCount == 0)
    {
        cout << " [!] No patients are in the hospital yet.\n";
        return;
    }

    string searchName;
    int searchID;
    cout << " Enter Patient's Full Name to assess: ";

    cin.clear();
    cin.ignore(1000, '\n');
    getline(cin, searchName);



    for (int i = 0; i < searchName.length(); i++)
    {
        searchName[i] = toupper(searchName[i]);
    }
    cout << " ENTER PATIENT'S ID TO ASSESS: ";
cin>>searchID;
   cin.clear();
    cin.ignore(1000, '\n');







    for (int i = 0; i < currentPatientCount; i++)
    {
        if (patientNames[i] == searchName && patientDetails[i][2] == searchID)
        {

            string symptoms;
            string emergencyStatus;

           cout << "\n Patient Found: " << patientNames[i] << " (Age: " << patientDetails[i][0] << ")\n";


            cout << " SYSTEM KEYWORDS: Chest| Breathing| Bleeding| Stroke| Unconscious| Fever| Headache\n";
            cout << " Describe Symptoms: ";

            getline(cin, symptoms);

            for (int j = 0; j < symptoms.length(); j++)
            {
                symptoms[j] = toupper(symptoms[j]);
            }

            int urgencyScore = calculateUrgency(symptoms);

            string assignedDoc = "";
            if (symptoms.find("CHEST") != string::npos)
            {
                emergencyStatus = "CRITICAL EMERGENCY";
                assignedDoc = "Dr. Nongqunga (Cardiologist)";
            }
            else if (symptoms.find("BREATHING") != string::npos)
            {
                emergencyStatus = "EMERGENCY";
                assignedDoc = "Dr. Mkhwanazi (Pulmonologist)";
            }
            else if (symptoms.find("BLEEDING") != string::npos)
            {
                emergencyStatus = "EMERGENCY";
                assignedDoc = "Dr. Kesiyane (Trauma Surgeon)";
            }
            else if (symptoms.find("STROKE") != string::npos || symptoms.find("UNCONSCIOUS") != string::npos)
            {
                emergencyStatus = "CRITICAL EMERGENCY";
                assignedDoc = "Dr. Magabe (Neurologist)";
            }
            else if (symptoms.find("FEVER") != string::npos)
            {
                emergencyStatus = "MODERATE";
                assignedDoc = "Dr. Motaung (General Practitioner)";
            }
            else
            {
                emergencyStatus = "LOW / NORMAL";
                assignedDoc = "Dr. Moloi (General Practitioner)";
            }

            patientDetails[i][1]= urgencyScore;
            assignedDoctors[i] = assignedDoc;

            cout << "\n            REPORT          \n";
            cout << " Symptoms Logged: " << symptoms << "\n";
            cout << " Priority Score:  " << urgencyScore << "\n";
            cout << " Patient Status:  " << emergencyStatus << "\n";
            cout << " Assigned Doctor: " << assignedDoc << "\n";

            // TRIGGER THE SORT
            sortWaitingList();
            cout << "\n Queue sorted. Critical patients moved to the front.\n";

            return;
        }
    }


   cout << " Patient record not found. Please check spelling.\n";


}



int calculateWaitTime(int place)
{
    if (place == 1)
        return 15;  // Base case

    return 15 + calculateWaitTime(place - 1);  // Recursive case
}

void loginRecetionist()
{
    const string PASSWORD = "NEXTGEN15";
    string userpassword;
    int passwordCOUNT = 0;

    cout << "\n======================================================\n";
    cout << "    SECURE LOGIN: AUTHORIZED MEDICAL RECETIONIST ONLY   \n";
    cout << "======================================================\n";
    cout << "Enter Staff Password to Access Terminal: ";
    cin >> userpassword;

    while (userpassword != PASSWORD)
    {
        cout << " ACCESS DENIED, UNAUTHORIZED USER!!!\n\n";
        passwordCOUNT++;




        if (passwordCOUNT == 3)
        {
            cout << " You have used all 3 attempts. Program terminated.\n";
         return;
        }

        cout << " Attempts used: " << passwordCOUNT << "/3\n";
        cout << "Enter Staff Password to Access Terminal: ";
        cin >> userpassword;

    }



    cout << " ACCESS GRANTED. WELCOME TO THE SYSTEM\n";

    int choice;

    do {
        showMenu();


           cin >> choice;

if(cin.fail())
{
    cout<< "invalid option choose between 1-5";
    cin.clear();
    cin.ignore(1000, '\n');
    continue;
}





        switch(choice)
        {
            case 1:
                addPatient();
                break;
            case 2:
                checkSymptomsAndAssign();
                break;
            case 3:
                showWaitingList();
                break;
            case 4:
                if (currentPatientCount == 0)
                {
                    cout << "\n The hospital waiting room is empty.\n";
                }
                else
                {
                    int position;
                    cout << "\n Enter patient's position (1 to " << currentPatientCount << "): ";
                    cin >> position;

                    if (position > 0 && position <= currentPatientCount)
                    {
                        cout << " ESTIMATED WAIT TIME: "
                             << calculateWaitTime(position) << " minutes\n";
                    }
                    else
                    {
                        cout << " Invalid position.\n";
                    }
                }
                break;
            case 5:
                cout << "\n loging of..... returning to main menu!\n";
                break;
            default:
                cout << "\n Invalid choice!!! Please enter 1 to 5.\n";
        }

    } while (choice != 5);
}

void loginManager()
{
    const string PASWORD = "MANAGER123";
    string Managerpassword;
    int paswordCOUNT = 0;

    cout << "\n======================================================\n";
    cout << "    SECURE LOGIN: AUTHORIZED MEDICAL MANAGER ONLY   \n";
    cout << "======================================================\n";
    cout << "Enter Staff Password to Access Terminal: ";
    cin >> Managerpassword;

    while (Managerpassword != PASWORD)
    {
        cout << " ACCESS DENIED, UNAUTHORIZED USER!!!\n\n";
        paswordCOUNT++;




        if (paswordCOUNT == 3)
        {
            cout << " You have used all 3 attempts. Program terminated.\n";
         return;
        }

        cout << " Attempts used: " << paswordCOUNT << "/3\n";
        cout << "Enter Staff Password to Access Terminal: ";
        cin >> Managerpassword;

    }



    cout << " ACCESS GRANTED. WELCOME TO THE SYSTEM\n";

    int option;

    do {
        ManagerMenu();

cin >> option;

if (cin.fail())
{
    cin.clear();
    cin.ignore(1000, '\n');
    cout << "\n Invalid input!!! Please enter 1 or 2.\n";
    continue;
}




        switch(option)
        {

            case 1:
                showWaitingList();
                break;
            case 2:
                if (currentPatientCount == 0)
                {
                    cout << "\n The hospital waiting room is empty.\n";
                }
                else
                {
                    int position;
                    cout << "\n Enter patient's position (1 to " << currentPatientCount << "): ";
                    cin >> position;

                    if (position > 0 && position <= currentPatientCount)
                    {
                        cout << " ESTIMATED WAIT TIME: "
                             << calculateWaitTime(position) << " minutes\n";
                    }
                    else
                    {
                        cout << " Invalid position.\n";
                    }
                }
                break;
            case 3:
                cout << "\n loging of..... returning to main menu!\n";
                break;
            default:
                cout << "\n Invalid choice!!! Please enter 1 to 5.\n";
        }


    } while (option != 3);
}
void ManagerMenu()
{
     cout << "\n======================================================\n";
    cout << "          INTELLIGENT HOSPITAL WAITING SYSTEM         \n";
    cout << " \n            BY: NEXTGEN MEDICAL                \n";
    cout << "======================================================\n";
    cout << "1. Look at the Waiting List\n";
    cout << "2. Predict Wait Time\n";
    cout << "3. Exit System\n\n";
    cout << "Choose an option (1-3): ";
}
