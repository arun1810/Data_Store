# Data_Store

A Java based helper class fore Storing, Reading and Deleting JSON files.

<details open="open">
  <summary><h2 style="display: inline-block">Contents:</h2></summary>
  <ol>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
        <li><a href="#Pre -request">Pre-request</a></li>
        <li><a href="#Compile-And-Run">Compile and Run</a></li>
        <ul>
        <li><a href="#to-compile">To Compile</a></li>
        <li><a href="#to-run">To Run</a></li>
        </ul>
        </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>
 

## Getting Started

DataStore is a `key-value pair` based Data Storing Helper class for `Java`. it Stores data given as `HashMap<String,String>` as `JSON`. It is a lightweight helper class which can be used with any kind of java projects which requries JSON data storing functionality. DataStore supports `Time-to-Live` functionality which enables you to specify the time in `Seconds` after which the file will be deleted. DataStore is `Thread-Safe` that is DataStore can be used in multiple Threads. the key size of DataStore is capped at `32 chars` and the size of the given value is capped at `16KB` and the OverAll file size is capped at `1 GB`.

### Pre-request
DataStore uses `Gson-2.2.2.jar` to convert HashMap to JSON and  JSON to HashMap. to compile and run the DataStore the Gson-2.2.2.jar should be included in the classpath.

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/arun1810/Data_Store.git
   ```
## Compile And Run
Put the DataStore.java and DataStoreSamples.java in same folder and run the following command in Command-prompt
### To Compile
```sh
javac -cp "gson-2.2.2.jar";"."; DataStoreSamples.java
```
### To Run
```sh
java -cp "gson-2.2.2.jar";"."; DataStoreSamples
```
## Usage
Copy the DataStore.java and CustomExceptions folder inside your project.

#### To create Instance
```sh
// This returns a Instance of DataStore in the default path
DataStore datastore = DataStore.getInstance(); 

// This returns a Instance of DataStore in the given "path"
DataStore datastore = DataStore.getInstance("path");
```
#### To Create Data
```sh
// your value to be Stored
HashMap<String,String> value = new HashMap<String,String>(); 

// Save the given value on the given key
datastore.Create("Youe Key",value); 

// Save the given data on the given key with Time-To-Live property in Seconds
datastore.Create("Your Key",value,100); 
```
#### To Read Data
```sh 
// Return the value stored on the given key as HashMap<String,String>
datastore.Read("Your Key"); 
```
#### To Delete data
```sh
// Deletes the key and the value stored on the given key
datastore.Delete("Your Key"); 
```
