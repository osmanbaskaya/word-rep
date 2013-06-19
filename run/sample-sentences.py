#! /usr/bin/python
# -*- coding: utf-8 -*-

__author__ = "Osman Baskaya"

from itertools import izip
from random import seed, sample
import sys
import gzip

seed(int(sys.argv[1]))
k = int(sys.argv[2])
n = -1

for line0, line1, line2 in izip(gzip.open(sys.argv[3]), gzip.open(sys.argv[4]), gzip.open(sys.argv[5])):
    if line0 == '' or line1 == '' or line2 == '': continue
    n += 1
    if len(tokens) < k:
        tokens.append(line0)
        pos.append(line1)
        lemma.append(line2)
    else:
        r = randint(0, n)
        if r < k:
            tokens[r] = line0
            pos[r] = line1
            lemma[r] = line2

f0 = gzip.open('sampled.' + sys.argv[3], 'w')
f1 = gzip.open('sampled.' + sys.argv[4], 'w')
f2 = gzip.open('sampled.' + sys.argv[5], 'w')
for line0, line1, line2 in izip(tokens, pos, lemma):
    f0.write(line0)
    f1.write(line1)
    f2.write(line2)


def main():
    pass

if __name__ == '__main__':
    main()

