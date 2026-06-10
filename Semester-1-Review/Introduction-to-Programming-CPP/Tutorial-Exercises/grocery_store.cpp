#include <iostream>

using namespace std;



int main()

{

    // DECLARE VARIABLE CHOICE

    int option;



    // OUTPUT THE MENU

    cout << "--------- MIYA GROCEERY STORE ----------"<< endl;



    do

    {



    cout << " MENU: "<< endl;

    cout << " 1. CHEESE \n 2. BACON\n 3. EGGS\n 4. CASHEW\n 5. EXIT \n ";



    // PROMPT USER TO CHOOSE A PRODUCT

    cout << " CHOOSE OPTION (1 - 5): ";

    cin >> option;

    cout << endl;



    switch(option)

    {

    case 1:

        cout << " CHEESE - R45.99\n"<<endl;

        break;

    case 2:

        cout << " BACON - R109.99\n"<<endl;

        break;

    case 3:

        cout << " EGGS - R109.10\n"<< endl;

        break;

    case 4:

        cout << " CASHEW - R150.39\n"<<endl;

        break;

    case 5:

        cout << " THANK YOU FOR VISITING MIYA GROCERY STORE, GOODBYE!"<<endl;

        break;



    default:

        cout << " INVALID OPTION!!! TRY AGAIN"<<endl;

    }

    }

    while (option != 5);

    return 0;

}
