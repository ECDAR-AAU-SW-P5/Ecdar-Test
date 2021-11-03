# Ecdar-test
The test framework for automatic test case generation for Ecdar engines.

Requires a configuration file with information for each engine such as: 
```json
[
   {
     "name": "Reveaal",
     "executablePath": "path/to/Reveaal.exe",
     "parameterExpression" : "-p {address}",
     "ip": "127.0.0.1",
     "port" : 5000,
     "processes" : 8
   }
 ]
```
