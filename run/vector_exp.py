#! /usr/bin/python
# -*- coding: utf-8 -*-
__author__ = "Osman Baskaya"

import gzip
import numpy as np
import sys


def file_open(filename):
    if filename.endswith('.gz'):
        return gzip.open(filename)
    else:
        return open(filename)

def get_arr(filename):

    lines = file_open(filename).readlines()
    values = [line.split()[2:] for line in lines]
    words = [line.split()[0] for line in lines]
    keys = dict(zip(range(len(words)), words))
    mat = np.matrix(values, dtype=float)
    arr = np.array(values, dtype=float)
    return keys, dict(zip(words, arr)), mat


def find_nearest_word(word):
    v = arr[word]
    res = np.argsort(v * mat.T).tolist()[0][::-1]
    return [keys[ind] for ind in res][:10]


def find_nearest_vector(v):
    res = np.argsort(v * mat.T).tolist()[0][::-1]
    return [keys[ind] for ind in res][:20]

filename = 'scode-y.gz'
if len(sys.argv) > 1:
    filename = sys.argv[1]

keys, arr, mat = get_arr(filename)
word = 'Pierre'

