# Ecdar-test
The test framework for automatic test case generation for Ecdar engines.

Clone with `git clone --recurse-submodules https://github.com/Ecdar/Ecdar-Test.git` or if you have already cloned use `git submodule update --init --recursive`

Runs on JDK 11 Temurin

Requires a configuration file `configuration.json` with information for each engine executable: (Also see [full config](#full-configuration-file))
```json
[
  {
    "name": "Reveaal",
    "version": "Main",
    "executablePath": "path/to/Reveaal/target/release/reveaal",
    "parameters" : ["serve","{ip}:{port}"],
    "ip": "127.0.0.1",
    "port" : 7000,
    "processes" : 8,
    "enabled" : true,
    "verbose": true,
    "testTimeout": 500,
    "testCount" : 4000,
    "testSorting": "Random",
    "queryComplexity": [0, 1000],
    "gRPCSettings": {
      "disable-clock-reduction": true
    }
  }
]
```
## Run Tests for Engine
Run all tests on enabled engines from `main()` in [Main.kt](src/main/kotlin/Main.kt) *(Click me in your IDE)*.

Test results are stored in `results/ENGINE_NAME/ENGINE_VERSION/RUN_NUMBER`. Run numbering is used so new results on same engine and version do not override previous results.
```
Found 5730 tests

Running 5730 tests on engine "Reveaal"
| 100% [5730/5730]
4372/5730 tests succeeded (76%) in 78 seconds
```

## Print Results From The Latest Run

Pretty print results of the latest run from `main()` in [Results.kt](src/main/kotlin/Results.kt).

This further provides the arguments for the engine to rerun any failed query.
```
Printing results for file "results\Reveaal\Main\0.json"
4372/5730 tests succeeded
1358 tests failed
0 failed due to exceptions

Expected SATISFIED, but was UNSATISFIED in consistency: (((A || G) // G) && ((A || Q) // Q) && (Q // (A || Q))); refinement: A <= (((A || G) // G) && ((A || Q) // Q) && (Q // (A || Q)))
Rerun with arguments: "consistency: (((A || G) // G) && ((A || Q) // Q) && (Q // (A || Q))); refinement: A <= (((A || G) // G) && ((A || Q) // Q) && (Q // (A || Q)))" -i samples/json/AG 

Expected SATISFIED, but was UNSATISFIED in consistency: ((G // (A || G)) && (G // (A || Q))); refinement: A <= ((G // (A || G)) && (G // (A || Q)))
Rerun with arguments: "consistency: ((G // (A || G)) && (G // (A || Q))); refinement: A <= ((G // (A || G)) && (G // (A || Q)))" -i samples/json/AG 
...
```
## Generate Interactive Plots Comparing Engines and Versions 
Line and density plots can be generated from `main()` in [Plotting.kt](src/main/kotlin/Plotting.kt). 

Plots can be generated comparing all engines or all versions of a specific engine. 

Plots can optionally have log scale axes, as the ones seen below.

### Line Plots
![Line Plot](https://i.imgur.com/dsKycFL.png "Line Plot")
### Density Plots
![Density Plot](https://i.imgur.com/PAl3BdX.png "Density Plot")

### Full configuration file
If an `executablePath` or `parameterExpression` is omitted, the engine is expected to be hosted externally. An example of this is the `External` engine in the full configuration in the bottom. 

* Engines can optionally be marked `verbose` to print failed queries while the tests are run from [Run Tests for Engine](#run-tests-for-engine)
* `testTimeout`, `testCount`, `testSorting`, `queryComplexity`, and `testsSavePath` are all optional attributes.
* `testTimeout` sets the time limit in seconds for the duration of a test (default=30).
* `testCount` limits the number of tests to execute (default=all).
* `testSorting` determines how to sort the tests if `testCount` is set. There are four different sortings:
* `Random` (default) - Takes `testCount` generated tests randomly
* `FILO` - Takes the last `testCount` generated tests
* `FIFO` - Takes the first `testCount` generated tests
* `Split` - Takes an equal split of each test-sort by RoundRobin, summing up to no more than `testCount` tests
* `testsSavePath` determines if and where in the filesystem to save the text-file with the queries being generated. If not set, the queries will not be saved on disk.
* `gRPCSettings` is the settings that are sent to the engine through gRPC. The settings can be found in the [protobuf](https://github.com/Ecdar/Ecdar-ProtoBuf).
* `queryComplexity` determines the complexity of the queries in the tests (the number of operators).

Both the upper and lower bound can be set.

If only one element is in the array the upper bound will be set to that.

If more than two elements are defined then only the first two will be used where the first value is the lower bound and second value is the upper bound.

If the array is empty, no bound is set.

```json
[
    {
        "name": "Reveaal",
        "version": "Main",
        "executablePath": "path/to/Reveaal.exe",
        "parameters" : ["serve","{ip}:{port}"],
        "ip": "127.0.0.1",
        "port" : 7000,
        "processes" : 8,
        "enabled" : true,
        "verbose": true,
        "testTimeout": 30,
        "testCount" : 4000,        
        "testSorting": "Random",
        "queryComplexity": [0, 1000],
        "testsSavePath": "/path/to/file",
        "gRPCSettings": {
          "disable-clock-reduction": true
        }
    },
    {
        "name": "J-Ecdar",
        "version": "UCDD",
        "executablePath": "path/to/j-Ecdar.bat",
        "parameters" : ["-p={ip}:{port}"],
        "ip": "127.0.0.1",
        "port" : 8000,
        "processes" : 8,
        "enabled" : false
    },
    {
        "name": "External",
        "version": "v1.0",
        "ip": "127.0.0.1",
        "port" : 9000,
        "processes" : 1,
        "enabled" : false
    }
 ]
```