import csv
from collections import defaultdict


with open('covidTrain.csv', 'r') as f:
    reader = csv.reader(f)
    data = list(reader)

    latitudePairs = defaultdict(list)
    longitudePairs = defaultdict(list)
    
    numberOfLatitudes = 0
    numberOfLongitudes = 0

    for row in data:
        if row[0] == 'ID': # Skip header
            continue

        if '-' in row[1]:
            #split the string into a list of strings, separated by '-'
            ageRange = row[1].split('-')
            data[data.index(row)][1] = round((int(ageRange[0]) + int(ageRange[1])) / 2)


        #date colums are 8, 9, 10
        dateSegements = row[8].split('.')
        #swap the day and month
        temp = dateSegements[0]
        dateSegements[0] = dateSegements[1]
        dateSegements[1] = temp
        #join the list of strings into a single string
        data[data.index(row)][8] = '.'.join(dateSegements)

        dateSegements = row[9].split('.')
        temp = dateSegements[0]
        dateSegements[0] = dateSegements[1]
        dateSegements[1] = temp
        data[data.index(row)][9] = '.'.join(dateSegements)

        dateSegements = row[10].split('.')
        temp = dateSegements[0]
        dateSegements[0] = dateSegements[1]
        dateSegements[1] = temp
        data[data.index(row)][10] = '.'.join(dateSegements)

        
        if row[6] != 'NaN':
            latitudePairs[row[4]].append(float(row[6]))
            numberOfLatitudes += 1

        if row[7] != 'NaN':
            longitudePairs[row[4]].append(float(row[7]))
            numberOfLongitudes += 1

    for row in data:
        if row[0] == 'ID': # Skip header
            continue

        if row[6] == 'NaN':
            row[6] = round(sum(latitudePairs[row[4]]) / len(latitudePairs[row[4]]), 2)
            # print(row[6])

        if row[7] == 'NaN':
            row[7] = round(sum(longitudePairs[row[4]]) / len(longitudePairs[row[4]]), 2)
            # print(row[7])

    provinceCityCounts = defaultdict(lambda: defaultdict(int))
    for row in data:
        if row[0] == 'ID': # Skip header
            continue
        if row[3] != 'NaN':
            provinceCityCounts[row[4]][row[3]] += 1

    for row in data:
        if row[0] == 'ID': # Skip header
            continue
        if row[3] == 'NaN':
            province = row[4]
            cityCounts = provinceCityCounts[province]
            mostCommonCity = min(cityCounts, key=lambda x: (-cityCounts[x], x))
            row[3] = mostCommonCity
            # print(row[3])

    symptomCounts = defaultdict(lambda: defaultdict(int))
    for row in data:
        if row[0] == 'ID': # Skip header
            continue
        if row[11] != 'NaN':
            symptoms = row[11].split(';')
            for symptom in symptoms:
                symptomCounts[row[4]][symptom.strip()] += 1

    for row in data:
        if row[0] == 'ID': # Skip header
            continue
        if row[11] == 'NaN':
            province = row[4]
            provinceSymptoms = symptomCounts[province]
            mostCommonSymptom = min(provinceSymptoms, key=lambda x: (-provinceSymptoms[x], x))
            row[11] = mostCommonSymptom.strip()
            print(row[11])

    with open('covidResult.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerows(data)