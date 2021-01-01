import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.*;
import CustomExceptions.*;


public class DataStore{

    private static DataStore INSTANCE;
    private String path;
    private static final String datastore = "DataStore",property = "Property.json",value = "value.json";
    private File parent,parentProperty;
    private static final int valueLimit =  16000,keyLimit = 32;
    private static final double HeaderLimit = 1e+9;
    Gson g ;
    
    private DataStore(){
       g  = new Gson();
    }

    public static DataStore getInstance(String path){

        INSTANCE = InstanceCreater(path);
         return INSTANCE;
        
    }

    public static DataStore getInstance(){
       String path = System.getProperty("user.dir");
       INSTANCE =  InstanceCreater(path);
        return INSTANCE;
        
    }

    private synchronized static DataStore InstanceCreater(String path){
        
        synchronized(DataStore.class){
         if(INSTANCE==null){
            INSTANCE = new DataStore();
            
         }
         else if(INSTANCE.path != path){
             INSTANCE = new DataStore();
         }
         
        }
         INSTANCE.path = path;
         INSTANCE.parent = new File(path+"/"+datastore);
         if(!INSTANCE.parent.exists()){
            INSTANCE.parent.mkdir();
        }
        INSTANCE.parentProperty = new File(INSTANCE.parent.getAbsolutePath()+"/"+property);

        try{INSTANCE.HeaderPropertyWriter();}catch(IOException e){e.printStackTrace();}
         return INSTANCE;

    }

    public void Create(String key,HashMap<String,String> value)throws Exception{


        int TTL = -1;
        CreateFile(key, value, TTL);

    }

    public void Create(String key,HashMap<String,String> value,int TTL)throws Exception{
       CreateFile(key, value, TTL);

    }

    

    private void CreateFile(String key,HashMap<String,String> mapValue,int TTL) throws Exception{
       
        if(key.length() > keyLimit){
            throw new KeySizeExceededException(key);
        }
        
        File KeyFile = new File(parent.getAbsolutePath()+"/"+key);
        File propertiesFile = new File(KeyFile.getAbsolutePath()+"/"+property);
        File ValueFile = new File(KeyFile.getAbsolutePath()+"/"+value);
        
        if(KeyFile.exists()){
           
            if(propertiesFile.exists()){


                HashMap<String,String> prop = FileToMap(propertiesFile);
               
                double TTLretrived =Double.parseDouble(prop.get("TTL"));
                double CreatedAtRetreived = Double.parseDouble(prop.get("CreatedAt"));
              
              if(TTLretrived!=-1 && (TTLretrived+CreatedAtRetreived)<=System.currentTimeMillis()){
                    Delete(key);
                    FileCreater(mapValue, TTL, KeyFile, propertiesFile, g);
              }
              else{
                throw new KeyAlreadyExistsException(key);
                
              }
            }
            else{
                if(ValueFile.exists()){
                    
                    HashMap<String,String> oldVal = FileToMap(ValueFile);
                    FileCreater(oldVal, TTL, KeyFile, propertiesFile, g);
                }
                else{
                    FileCreater(mapValue, TTL, KeyFile, propertiesFile, g);
                }
                
            }

            
        }
        else{
          FileCreater(mapValue, TTL, KeyFile, propertiesFile, g);
           
        }
        
        
    }

    public HashMap<String , String> Read(String key) throws Exception{
       

        File ValueFile = new File(parent.getAbsolutePath()+"/"+key+"/"+value);
        HashMap<String,String> val;

        if(!ValueFile.exists()){
            throw new KeyNotExistsException(key);
            
        }
        else{
           val = FileToMap(ValueFile);
            return val;
        } 
    }
    
    
    public synchronized void Delete(String key) throws Exception{
        File KeyFile = new File(parent.getAbsolutePath()+"/"+key);
        int TotalLen = 0;
        if(parentProperty.exists()){
                HashMap<String,String> parentprop =FileToMap(parentProperty);

                long count = Long.parseLong(parentprop.get("count").trim());
                long size = Long.parseLong(parentprop.get("size").trim());

                if(!KeyFile.exists()){
                    throw new KeyNotExistsException(key);
                    
                }
                else{
                    File[] files = KeyFile.listFiles();
                    for(File file : files){
                        TotalLen+=file.length();
                        if(!file.delete()){
                           throw new FileNotDeletedException(file.getName());
                        }
                    }

                    if(KeyFile.delete()){
                        
                        DecreaseHeader(TotalLen, count, size, parentprop);
                    }
                    else{
                        throw new FileNotDeletedException(KeyFile.getName());
                    }
                }
                
        
            }
            
            else{
                HeaderPropertyWriter();
                Delete(key);
            }

        
    }

    private synchronized void FileCreater(HashMap<String,String> mapValue,int TTL,File KeyFile,File propertiesFile,Gson g)throws Exception{

        long TotalLen = 0;

        String valueStr = g.toJson(mapValue);
        double valueLength = g.toJson(mapValue).getBytes().length;

        if(parentProperty.exists()){
            
            HashMap<String,String> parentprop = FileToMap(parentProperty);

            long count = Long.parseLong(parentprop.get("count").trim());
            long size = Long.parseLong(parentprop.get("size").trim());

            if(count!=(parent.listFiles().length-1)){
                HeaderPropertyWriter();
                FileCreater(mapValue, TTL, KeyFile, propertiesFile, g);
            }
            else if((size+valueLength) >= HeaderLimit){
                    throw new SizeLimitExcededException("Overall data size exceeds limit(1GB)");
            }

            else{
               
        KeyFile.mkdir();
        HashMap<String , String> property = new HashMap<>();
        if(TTL == -1){
            property.put("TTL", String.valueOf(-1));
        }
        else{
            property.put("TTL", String.valueOf(TTL*1000));
        }
        property.put("CreatedAt", String.valueOf(System.currentTimeMillis()) );
      if(valueLength>=valueLimit){
          throw new SizeLimitExcededException("given data size exceeds limit(16KB)");
          
      }
      String propertyStr = g.toJson(property);
      File valuefile = new File(KeyFile.getAbsolutePath()+"/"+value);
      FileWrite(valuefile, valueStr);
      FileWrite(propertiesFile, propertyStr);
      TotalLen = valuefile.length()+propertiesFile.length();
     IncreaseHeader(TotalLen, count, size, parentprop);
            }
        }
        else{
            HeaderPropertyWriter();
        }
       
    }

private String FileRead(File f) throws IOException{
    String data=null;
    FileReader reader = new FileReader(f);
    BufferedReader propReader = new BufferedReader(reader);
               data = propReader.readLine();
               reader.close();
               propReader.close();
    return data;
}
private void FileWrite(File f,String value) throws IOException{
    FileWriter writer = new FileWriter(f);
    writer.write(value);
    writer.close();
}

private void HeaderPropertyWriter() throws IOException{

    if(!parentProperty.exists()){
        parentProperty.createNewFile();
    }
        HashMap<String,String> prop = new HashMap<>();
        prop.put("count",HeaderCount(parent));
        prop.put("size",HeaderSize(parent,0));
      String value =   g.toJson(prop);
      try{FileWrite(parentProperty, value);}catch(Exception e){e.printStackTrace();}

}

private String HeaderCount(File header){
    String count = null;
    count = String.valueOf(header.listFiles().length-1);

    return count;
}

private String HeaderSize(File header,long s){
    String size = null;
    File[] files = header.listFiles();
    for(File f : files){
        if(f.isDirectory()){
         s=Long.parseLong(HeaderSize(f,s));//Resursive call
        }
        else{s+=f.length();} 
    }
    size = String.valueOf(s);
    return size;
}

private void IncreaseHeader(long TotalLen,long count,long size,HashMap<String,String> parentprop) throws Exception{

    count++;
    size+=TotalLen;
    parentprop.put("count", String.valueOf(count));
    parentprop.put("size", String.valueOf(size));
    String ParentPropUpdated = g.toJson(parentprop);
    FileWrite(parentProperty, ParentPropUpdated);

}
private void DecreaseHeader(long TotalLen,long count,long size,HashMap<String,String> parentprop) throws Exception{

    count--;
    size-=TotalLen;
    parentprop.put("count", String.valueOf(count));
    parentprop.put("size", String.valueOf(size));
    String ParentPropUpdated = g.toJson(parentprop);
    FileWrite(parentProperty, ParentPropUpdated);

}

private HashMap<String,String> FileToMap(File f) throws IOException{

    String data = FileRead(f);       
    HashMap<String,String> prop = g.fromJson(data, new TypeToken<HashMap<String,String>>(){}.getType());
    return prop;
}

}





