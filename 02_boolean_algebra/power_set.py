import sys

from itertools import combinations

def powerset(set):
    n = len(set)
    for i in range(0,n+1):
        for element in combinations(set,i):
            print(''.join(element))


set = sys.argv[1]

powerset(set.split(" "))