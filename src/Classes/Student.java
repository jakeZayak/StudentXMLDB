package Classes;

import java.util.Comparator;

public class Student {
    public int ID;
    public String fName;
    public String lName;   
    
public static Comparator<Student> getCompByName()
{   
 Comparator comp = new Comparator<Student>(){
     @Override
     public int compare(Student s1, Student s2)
     {
        return s1.ID - s2.ID;
     }        
 };
 return comp;  
}
}

