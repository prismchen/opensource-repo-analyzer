
import sys
import operator

file = open(sys.argv[1], "r");

data = file.read()
lst = data.splitlines()

dictionary = {}

for line in lst:
	tokens = line.split("\t")
	dictionary[tokens[0]] = int(tokens[1])

for key, value in sorted(dictionary.iteritems(), key=lambda (k,v): (v,k)):
    print "%s: %s" % (key, value)

file.close()