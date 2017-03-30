# Spreadsheet-Calculator

A text-based spreadsheet calculator to determine numeric values at each cell or to detect cyclic dependencies.

#Inputs
The first line of input gives the number of columns and rows in the spreadsheet. The further inputs include numerical values (Integers) or post-fix expressions including references to other cells (A1), Mathematical operations (+, -, *, /, ++, --), and numbers(Integers)

#Expected Output
Numeric value of each cell after calculations. If cyclic dependencies are found, it prints 'Error: Cyclic Dependencies'.

#Sample test case

Inputs:

3 2  
A2  
4 5 *  
A1  
A1 B2 / 2 +  
3  
39 B1 B2 * /  

Representaion in Tabular form:

| row   | 1            | 2     | 3            |
| ----- | ------------ | ----- | ------------ |
| A     | A2           | 4 5 * | A1           |
| B     | A1 B2 / 2 +  |  3    | 39 B1 B2 * / |

Results:

20.00000  
20.00000  
20.00000  
8.66667  
3.00000  
1.50000  
