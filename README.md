# 15 Puzzle

This project compares the efficacy of different sorting algorithms using the classic 15 puzzle game. Goal states are selected from two available options, while start states are entered manually. Start states are not validated. So, impossible setups will never reach solutions. After goal and start states have been chosen, the program will attempt to solve the puzzle using 6 different algorithms:

- depth first

- breadth first

- uniform cost

- iterative depth first

- greedy

- A star

After each algorithm runs, the program will print relevant statistics.

# Instructions to run

#### Compile the program

`$ make`

#### Start the program

`$ make play`

#### Select a goal state from the options provided

`A` or `B`

#### Input the start state
`( (1 2 3 4) (5 0 6 7) (8 9 10 11) (12 13 14 15) (1 1) )`

or

`1 2 3 4 5 0 6 7 8 9 10 11 12 13 14 15`
