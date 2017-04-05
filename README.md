# Fill Zone Solver
First Artificial Intelligence System Project.

## Getting Started

These instructions will install the system in your local machine.
 
### Prerequisites
 
1. Install Maven, if you haven't yet
    #### Mac OS X
    ```
    brew install maven
    ```
    
    #### Ubuntu
    ```
    sudo apt-get install maven
    ```
    
    #### Other OSes
    Check https://maven.apache.org/install.html.

2. Clone the repository or download source code:
```
git clone https://github.com/juanmbellini/fill_zone.git
```    

### Installing

1. Change working directory to project root (i.e where pom.xml is located):
    ```
    cd <project-root>
    ```

2. Let maven resolve dependencies:
    ```
    mvn dependency:resolve
    ```

3. Create jar file
    ```
    mvn clean package [-D dir=<destination-directory>]
    ```
    Note: If no destination was indicated, jar file will be under ``` <project-root>/target ```

## Usage
The application can be executed running ```java -jar <path-to-jar>```.
It can be configured in order to execute in different modes. This is done including parameters. The following sections will describe these modes. 

### Displaying usage message
You can display the usage message by setting the ```-h``` or the ```--help``` parameters.
Example of usage:
```
java -jar <path-to-jar> -h
```


### Indicating a searching strategy
To indicate the searching strategy, you must include the ```-S``` or the ```--strategy``` parameters, together with the selected strategy.
Options are:
* ```dfs```
* ```bfs```
* ```iddfs```
* ```iddfs-border```
* ```greedy```
* ```astar``` (Default option)

Example of usage:
```
java -jar <path-to-jar> --strategy greedy
```

### Indicating an heuristic
In case ```greedy``` or ```astar``` are selected, an heuristic must be specified. You can do this with the ```-H``` or the ```--heuristic``` parameters, together with the selected strategy.
Options are:
* ```remaining-groups```
* ```remaining-lockers```
* ```remaining-colors``` (Default option)
* ```two-colors```
* ```combined```
* ```max-admissible```

Example of usage:
```
java -jar <path-to-jar> --strategy astar -H two-colors 
```

### Indicating a board to solve
There are three ways of indicating the type of board to be solved.

#### Random board
In case you want to create a random board, you must specify the ```-rB``` or the ```--random-board``` parameters, indicating rows, columns and colors with the following parameters:

* Rows: ```-r``` or ```--rows``` (Default: 8)
* Columns: ```-c``` or ```--columns``` (Default: 8)
* Colors: ```-x``` or ```--colors``` (Default: 8)

This is the default option.


Example of usage:
```
java -jar <path-to-jar> --strategy dfs -random-board -r 7 -c 8 -x 10
```


#### Board file
In case you already have a board, and you want the system to load and solve it, you can specify the ```-fB``` or the ```--file-board``` parameters, indicating the path of the file with the ```-i``` or the ```--input``` parameters.
The default value is "Board.brd" saved in the current working directory.

Example of usage:
```
java -jar <path-to-jar> --strategy dfs -fB -i <path-of-file>
```

Note: The file must be a text file with the following format:
line 1: [amount-of-rows]
line 2: [amount-of-columns]
line 3: [amount-of-colors]
line 4: [blank-line]
line 5: [row #1]
line 6: [row #2]
...
line 5 + [amount-of-rows] - 1 : [row #[amount-of-rows]]


#### Preloaded boards
The system already brings built-in boards that can be selected. This is done with the ```-sB``` or the ```--stored-board``` parameters, indicating the number of board with the ```-b``` or the ```--board-number``` parameters.
The default value is 6.
The amount of stored boards is 7.

Example of usage:
```
java -jar <path-to-jar> --strategy bfs -sB --board-number 7
```

### Indicating an output
The application implements to ways of giving the solution output.


#### Print to console
You can print the solution directly to standard output (i.e the console). In order to do this, include the ```-cO``` or the ```--console``` parameters.
This is the default option.
If 10 or less colors are selected, the output will include real colors when printing the boards. Otherwise, just numbers will be printed.

Example of usage:
```
java -jar <path-to-jar> --strategy bfs --random-board -r 7 -c 8 -x 10 --console
```


#### Save to file
In case you want to save the solution into a file, you must include the  ```-fO``` or the ```--save``` parameters, indicating the output path with the ```-o``` or the ```--output``` parameters.
The default value is a file called "solution.txt" saved in the current working directory.

Example of usage:
```
java -jar <path-to-jar> --strategy bfs --sB -b 5 --save -o <path-of-solution-file>
```


## Authors
* Juan Marcos Bellini
* Natalia Navas
* Francisco Bartolomé

## Acknowledgments
* General Problem Solver Engine is based on https://github.com/apierri/GeneralProblemSolver project.