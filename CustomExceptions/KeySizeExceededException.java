package CustomExceptions;
public class KeySizeExceededException extends Exception{

   public KeySizeExceededException(String s){
    super("Key "+'"'+s+'"'+"exceeds limit(32 chars)");
   }
    
}