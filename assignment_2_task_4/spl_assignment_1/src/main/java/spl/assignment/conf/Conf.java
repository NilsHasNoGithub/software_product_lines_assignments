package spl.assignment.conf;

public class Conf{

    // Config variables
    public boolean color = false;
    public boolean encryption = false;
    public boolean logging = false;

    // Private constructor
    private Conf(){}

    // Single instance variable
    private static Conf single_instance = null;

    // Static method to create instance of Conf class
    public static Conf getInstance()
    {
        if (single_instance == null)
            single_instance = new Conf();
  
        return single_instance;
    }
}