package CustomExceptions;
public class KeyAlreadyExistsException extends Exception{

    public KeyAlreadyExistsException(String s){
        super("Key"+'"'+s+'"'+"already Exists");
    }
    
}