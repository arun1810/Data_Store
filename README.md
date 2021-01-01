# Data_Store

A Java based helper class fore Storing, Reading and Deleting JSON files.

<details open="open">
  <summary><h2 style="display: inline-block">Contents:</h2></summary>
  <ol>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
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

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/arun1810/Data_Store.git
   ```
## Compile And Run
Put the DataStore.java and DataStoreSamples.java in same folder and run the following command in Command-prompt
### To Compile
```
javac -cp "gson-2.2.2.jar";"."; DataStoreSamples.java
```
### To Run
```
java -cp "gson-2.2.2.jar";"."; DataStoreSamples
```
## Usage
Copy the DataStore.java and CustomExceptions folder inside your project.

# To create Instance
```
DataStore datastore = DataStore.getInstance(); // This returns a Instance of DataStore in the default path.

DataStore datastore = DataStore.getInstance("path");// This returns a Instance of DataStore in the given "path".
```
# To Create
```
HashMap<String,String> value = new HashMap<String,String>(); // your value to be Stored.

datastore.Create("Youe Key",value); // Save the given value on the given key.

datastore.Create("Your Key",value,100); // Save the given data on the given key with Time-To-Live property in Seconds
```
# To Read
```
datastore.Read("Your Key"); // Return the value stored on the given key as HashMap<String,String>
```
# To Delete
```
datastore.Delete("Your Key"); // Deletes the key and the value stored on the given key.
```
