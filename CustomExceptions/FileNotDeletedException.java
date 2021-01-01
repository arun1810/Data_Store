package CustomExceptions;
public class FileNotDeletedException extends Exception{
    public FileNotDeletedException(String s){
        super(s+" Coudn't be deleted");
     }
 }