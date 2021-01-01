package CustomExceptions;
public class SizeLimitExcededException extends Exception{
    public SizeLimitExcededException(String s){
        super(s);
    }
}