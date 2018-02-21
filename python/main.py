#!/usr/bin/env python
import sys
import os
import re
from collections import defaultdict

def get_csv_files(directory):
    csv_files = []
    for p, d, f in os.walk(directory):
        for csv_file in (os.path.join(p, x) for x in f if x.endswith('.csv')):
            csv_files.append(csv_file)
    return csv_files

def avg_col_count(files):
    num_cols = 0
    num_files = len(files)
    for csv_file in files:
        with open(csv_file, 'rb') as f:
            num_cols += next(f).decode('utf8').count(',') + 1
    return int(num_cols / num_files)

def line_count(files):
    lines = 0
    for csv_file in files:
        with open(csv_file, 'rb') as f1:
            lines += len(f1.readlines())
    return lines

def word_count(files):
    coll = defaultdict(int)
    for csv_file in files:
        with open(csv_file, 'rb') as f1:
            for line in f1.readlines():
                line = line.decode('utf8', errors='ignore')
                line = line.replace('"', '')
                line = re.split('[\s+,]', line)
                for word in line:
                    coll[word] += 1
    return coll


if __name__ == '__main__':
    target = sys.argv[1]
    files = get_csv_files(target)

    col_ct = avg_col_count(files)
    print("Average Columns:", col_ct)

    line_ct = line_count(files)
    print("Total Number Of Rows:", line_ct)

    print("Word Counts:")
    word_ct = word_count(files)
    for k,v in word_ct.items():
        print("{},{}".format(k, v))
