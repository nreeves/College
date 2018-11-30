#include <iostream>
#include <vector>

using namespace std;

int function() { 
    int i; 
    int n = 5;
    float num[100], sum=0.0, average;
    
    //vector<int> grades (5);
        //grades.insert (grades.begin() +1, i);

    while (n > 100 || n <= 0)
    {
        cout << "Error! number should in range of (1 to 100)." << endl;
        cout << "Enter the number again: ";
        cin >> n;
    }
    for(i = 0; i < n; ++i)
    {
        cout << i + 1 << ". Enter grade(s): ";
        cin >> num[i];
        sum += num[i];
    }
    

    average = sum / n;
    cout << "Average = " << average;
    return 0;
}


int main()
{
    int call;
    call = function();
    cout << call;

    return 0;
}
