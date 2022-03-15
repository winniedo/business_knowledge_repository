import pyeda
from pyeda.inter import *
import sys

s = sys.argv[1]

bs = expr(s)

expression = bs.to_dnf()

strExpr = str(expression)
strExpr = strExpr.replace("Or", "").replace("And", "#").replace("(", "").replace(")", "").replace(", ", " ")
for e in strExpr.split("#"):
    val = e.strip("*")
    if val != "":
        print(val)