
import sys
import operator

file = open(sys.argv[1], "r");

data = file.read()
lst = data.splitlines()

dictionary = {}

for line in lst:
	tokens = line.split("\t")
	dictionary[tokens[0]] = int(tokens[1])

count = 0
sorted_lst = sorted(dictionary.iteritems(), key=lambda (k,v): (v,k), reverse=True)
for key, value in sorted_lst:
    print "%s: %s" % (key, value)
    count += value

print count
file.close()
