#include <iostream>
using namespace std;

class Node
{
public:

    string name;
    Node* next;
};
int main()
{
    Node* head = new Node();
    Node* second = new Node();
    Node* third = new Node();
    Node* fourth = new Node();

    head->name = "ONKARABILE";
    head->next = second;
    second->name = "IGNATIOUS";
    second->next = third;
    third->name = "MIYA";
    third->next = fourth;
    fourth->name = "SKITS";
    fourth->next = NULL;

    //PROTECT THE HEAD NODE
    Node* temp = head;

   while(temp!=NULL)
    {
        cout <<temp->name<<" - "<<temp->next<< endl;
        temp = temp->next;
    }

    cout <<"THE VALUE OF THE HEAD NODE IS "<< head->name;



    return 0;
}
