import java.util.*;
public class DataStoreSamples{

    DataStoreSamples(){

        DataStore dataStore = DataStore.getInstance(); //returns a dataStore instance with default path
        
        DataStore dataStore2 = DataStore.getInstance("c:/"); //returns a dataStore instance with userdefines path,

        HashMap<String , String> value = new HashMap<>(); //data are given in hashmap

        value.put("a","apple is a fruit but tomato is a berry");
        value.put("b","orange is a fruit but strawberry is a berry");
        value.put("c","pineapple is a fruit but banana is a berry");
        value.put("d","apple is not a berry but tomato is a berry");
        value.put("e","orange is a not a berry but tomato is a berry");

        try{
            dataStore.Create("key", value); // creates a file named as "Key" and stores the 'value(parameter)'. If the specified key already present throws error.
            dataStore.Create("key2", value,100); //creates a file named as "Key2" and stores the 'value(parameter)' with Time-to-Live property in Seconds.

            HashMap<String,String> Rtnvalue =   dataStore.Read("key") ; //Searches for file name as "key" returns the content in it as Hasmap. if the specified key doesnt present throws error.
    
            dataStore.Delete("key15"); // Deletes the specified "Key" entry,if the specified key doesnt present throws error.
     
        }catch(Exception e){
         e.printStackTrace();

        }

         // DataStore is Thread-Safe !!

         new Thread(){
             public void run() {

                 try{
                     dataStore2.Create("facts", value);
                    }catch(Exception e){e.printStackTrace();};
             }
         }.start();

         new Thread(){
            public void run() {

                try{
                    dataStore2.Create("facts v2", value);
                    HashMap<String,String> data = dataStore2.Read("facts");
                   }catch(Exception e){e.printStackTrace();};
            }
        }.start();
     
            




    }

    public static void main(String[] args) {
        new DataStoreSamples();
    }
    
}
