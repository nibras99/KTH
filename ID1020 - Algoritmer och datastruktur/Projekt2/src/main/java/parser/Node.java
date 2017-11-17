package parser;

import java.util.ArrayList;

public class Node
{
    // Variablar som behövs
    public NodeType nodeType;
    public ArrayList<Node> children;
    public Node parent;
    public Operator operator;
    public String word;

    // En del constructors för olika scenarios
    public Node()
    {
        this.nodeType = NodeType.E;
        this.parent = null;
        this.operator = null;
        this.children = new ArrayList<Node>(0);
        this.word = null;
    }

    public Node(NodeType nt, Node parent, Operator operator)
    {
        this.nodeType = nt;
        this.parent = parent;
        this.operator = operator;
        this.children = new ArrayList<Node>(0);
        this.word = null;
    }

    public Node(NodeType nt, Node parent, String word)
    {
        this.nodeType = nt;
        this.parent = parent;
        this.word = word;
        this.children = null;
        this.operator = null;
    }

    public void createNodeTree(String[] queryParts)
    {
        // Variablar som kommer behövas
        Node top = this;
        Node operationDive = this;
        Node temp;

        // Special case
        // Exempel: nightmare
        if (queryParts.length == 1)
        {
            temp = new Node(NodeType.T, operationDive, queryParts[0]);
            operationDive.children.add(temp);
            return;
        }

        // Special case
        // Exempel: nightmare orderby relevance asc
        else if (!isOperator(queryParts[0]))
        {
            temp = new Node(NodeType.T, operationDive, queryParts[0]);
            operationDive.children.add(temp);

            addNodeWords(queryParts, operationDive, top);
            return;
        }

        // Loopar igenom fram tills vi börjar komma in på orden
        int operationsRemoved = 0;
        for (int i = 0; i < queryParts.length; i++)
        {
            // Om i är en operator och även nästa är det så skapar vi en nod och går ner i noden
            if (!queryParts[i].toUpperCase().equals("ORDERBY") && isOperator(queryParts[i]) && isOperator(queryParts[i + 1]))
            {
                temp = new Node(NodeType.T, operationDive, getOperator(queryParts[i]));
                operationDive.children.add(temp);
                operationDive = operationDive.children.get(0);
                operationsRemoved++;
            }
            // Annars är vi klar med detta och "breakar" loopen
            else
                i = queryParts.length;
        }

        // Skickar med det icke hanterade queryt
        String[] newQuery = new String[queryParts.length - operationsRemoved];
        for (int i = 0; i < newQuery.length; i++)
        {
            newQuery[i] = queryParts[i + operationsRemoved];
        }

        addNodeWords(newQuery, operationDive, top);
    }

    public void addNodeWords(String[] queryParts, Node current, Node top)
    {
        // Current == djupaste noden vi skapat
        // Top == E noden
        // Variablar som behövs
        Node tempParentNode;
        Node tempNode;
        // För varje ord
        // Kortfattat, skapar parent node, sätter in "barn" i parent noden med de operationer och ord som behövs,
        // sätter in parent i den tidigare skapade noden,
        // om den tidigare skapade noden nu har två barn så går vi upp ett steg till nästa tidigare skapade nod
        for (int i = 0; i < queryParts.length; i++)
        {
            // Exempel: + hello index
            if (isOperator(queryParts[i]) && !queryParts[i].toUpperCase().equals("ORDERBY"))
            {
                tempParentNode = new Node(NodeType.T, current, getOperator(queryParts[i]));

                tempNode = new Node(NodeType.T, tempParentNode, queryParts[i + 1]);
                tempParentNode.children.add(tempNode);

                tempNode = new Node(NodeType.T, tempParentNode, queryParts[i + 2]);
                tempParentNode.children.add(tempNode);

                current.children.add(tempParentNode);

                if (current.children.size() == 2)
                    current = current.parent;
                i = i + 2;
            }
            // Exempel: + hello index test <--- sista ordet
            else if (!isOperator(queryParts[i]) && !queryParts[1].toUpperCase().equals("ORDERBY"))
            {
                tempNode = new Node(NodeType.T, current, queryParts[i]);
                current.children.add(tempNode);

                if (current.children.size() == 2)
                    current = current.parent;
            }
            // När vi nått till ORDERBY, om det finns.
            else if (isOperator(queryParts[i]) && queryParts[i].toUpperCase().equals("ORDERBY"))
            {
                if (queryParts.length == i + 3)
                {
                    top.operator = Operator.orderby;

                    tempParentNode = new Node(NodeType.PROPERTY, top, getOperator(queryParts[i + 1]));
                    top.children.add(tempParentNode);

                    tempParentNode = new Node(NodeType.DIRECTION, top, getOperator(queryParts[i + 2]));
                    top.children.add(tempParentNode);
                }
                i = queryParts.length;
            }
        }
    }

    // Förklarar sig själv
    public boolean isOperator(String s)
    {
        return (s.equals("+") || s.equals("-") || s.equals("|") || s.equals("orderby"));
    }

    // Förklarar sig själv
    public Operator getOperator(String s)
    {
        if (s.equals("+"))
            return Operator.plus;
        else if (s.equals("-"))
            return Operator.minus;
        else if (s.equals("|"))
            return Operator.union;
        else if (s.equals("orderby"))
            return Operator.orderby;
        else if (s.equals("popularity"))
            return Operator.popularity;
        else if (s.equals("relevance"))
            return Operator.relevance;
        else if (s.equals("asc"))
            return Operator.asc;
        else if (s.equals("desc"))
            return Operator.desc;
        else
        {
            return null;
        }
    }

    // Hämtar djupaste noden, eller snarare näst djupaste
    public Node getDeepest()
    {
        Node current = this;
        while (current.children != null)
            current = current.children.get(0);
        current = current.parent;
        return current;
    }
}