public class Driver {
    public static void main(String[] args)
    {
        System.out.println("Start");
        Pascal myInterface;

        // Bara byt kommentars-rad på den du vill köra, varför göra det svårare än det behöver vara?
        // Reversed ändras i Pascal.java

        //myInterface = new RecursivePascal();
        myInterface = new IterativePascal();

        myInterface.printPascal(1000);
        System.out.println();
        System.out.println("End");
    }
}
