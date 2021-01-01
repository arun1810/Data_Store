package CustomExceptions;
public class KeyNotExistsException extends Exception{
  public  KeyNotExistsException(String s){
        super("Key "+'"'+s+'"'+"doesn't exists ");
    }
}