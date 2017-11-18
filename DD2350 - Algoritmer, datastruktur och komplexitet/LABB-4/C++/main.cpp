#include <iostream>
#include <vector>
#include <sstream>

using namespace std;

class Edge {
private:
    int from, to;
public:
    int getFrom()
    {
        return this->from;
    }
    int getTo()
    {
        return this->to;
    }

    Edge(int from, int to)
    {
        this->from = from;
        this->to = to;
    }
};



int main() {
    int vertices, edges, colors;
    int scenes, actors, roles;

    cin >> vertices >> edges >> colors;

    if (colors > vertices)
        colors = vertices;

    scenes = edges + 2;
    actors = colors + 3;

    int refactor[vertices + 1] = {};
    vector<Edge> edgeList;

    int from, to;
    roles = 3;

    for (int i = 0; i < edges; i++)
    {
        cin >> from >> to;

        if (refactor[from] == 0)
        {
            roles++;
            refactor[from] = roles;
        }
        if (refactor[to] == 0)
        {
            roles++;
            refactor[to] = roles;
        }

        edgeList.push_back(Edge(refactor[from], refactor[to]));
    }

    cout << roles << endl << scenes << endl << actors << endl;
    cout << "1 1" << endl;
    cout << "1 2" << endl;
    cout << "1 3" << endl;

    stringstream ss;
    ss << actors - 3;
    for (int i = 4; i <= actors; i++)
        ss << " " << i;

    string str = ss.str();
    for (int i = 4; i <= roles; i++)
        cout << str << endl;

    cout << "2 1 3" << endl;
    cout << "2 2 3" << endl;

    for (int i = 0; i < edges; i++) {
        cout << 2 << " " << edgeList[i].getFrom() << " " << edgeList[i].getTo() << endl;
    }

    return 0;
}