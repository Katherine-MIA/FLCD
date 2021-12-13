import os

from grammar_utils import read_grammar_from_file
from recursive_descent_alg import recursive_descent

path_to_grammar = os.path.join(os.getcwd(), 'language_grammar.txt')

grammar = read_grammar_from_file(path_to_grammar)
print(grammar)
f = open('fip.in', 'r')
inp = f.readline().strip().split(" ")
print(inp)
good, prods = recursive_descent(grammar, inp)
print(good, prods)
