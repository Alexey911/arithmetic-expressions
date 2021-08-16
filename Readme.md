# Math expressions

[![Build Status](https://travis-ci.com/Alexey911/arithmetic-expressions.png?branch=master)](https://travis-ci.com/Alexey911/arithmetic-expressions)
[![Coverage Status](https://coveralls.io/repos/github/Alexey911/arithmetic-expressions/badge.svg?branch=master)](https://coveralls.io/github/Alexey911/arithmetic-expressions?branch=master)

### Features
- expression generation
- expression evaluation
- expression simplification
- expression equivalence testing
- expression significance testing

### Examples

### How does it work?
The library is built around a brute-force algorithm that generates all possible expressions, 
and the filtration that removes equivalent expressions and expressions which have insignificant sub-expressions.
In order to get all valid expressions that consist of K-variables out of N available variables
the algorithm generates K-size variable combinations, after that for each combination
it generates all permutations, and then for each generated permutation it builds expressions and keeps only valid ones.
Further the filtration part takes each of the generated valid expressions and 
eliminate each variable/sub-expression in them and tests it for the significance.
Finally, the rest of valid expressions are normalized via multiple AST-transformations, after that 
equivalent expressions are removed.

### Complexity & Performance

### How it started?
I was working on an integration with [Yahoo! Finance](https://finance.yahoo.com/) 
where I tried to calculate different financial ratios (e.g. *Total Debt/Equity*, *Profit Margin*). 
The problem was I couldn't get even close to [the numbers](https://finance.yahoo.com/quote/TSLA/key-statistics?p=TSLA) 
and after several days of trying to manually figure out actual formulas 
I decided to write this library to solve such a problem. **PUT RESULT HERE**

### Mathematics problems
- [Getting the number of ways of associating N-applications of a binary operator](https://en.wikipedia.org/wiki/Catalan_number#Applications_in_combinatorics)

### TODO

### Related projects
https://github.com/scireum/parsii
