import csv
from collections import defaultdict, Counter

with open('pokemonTrain.csv', 'r') as f:
    reader = csv.reader(f)
    data = list(reader)

    weakness_type_counter = defaultdict(Counter)
    total_fire = 0
    fire_level_40 = 0

    level_threshold = 40

    sumOverThreshAtk = 0
    sumUnderThreshAtk = 0
    countOverThreshAtk = 0
    countUnderThreshAtk = 0

    sumOverThreshDef = 0
    sumUnderThreshDef = 0
    countOverThreshDef = 0
    countUnderThreshDef = 0

    sumOverThreshHp = 0
    sumUnderThreshHp = 0
    countOverThreshHp = 0
    countUnderThreshHP = 0

    hpStageThreeSum = 0

    for row in data:
        if row[0] == 'id': # Skip header
            continue

        if row[4] != 'NaN':
            weakness_type_counter[row[5]][row[4]] += 1
            if row[4] == 'fire':
                total_fire += 1
                if float(row[2]) >= 40:
                    fire_level_40 += 1

        if row[6] != 'NaN':
            if float(row[2]) > level_threshold:
                sumOverThreshAtk += float(row[6])
                countOverThreshAtk += 1

            else:
                sumUnderThreshAtk += float(row[6])
                countUnderThreshAtk += 1
        
        if row[7] != 'NaN':
            if float(row[2]) > level_threshold:
                sumOverThreshDef += float(row[7])
                countOverThreshDef += 1

            else:
                sumUnderThreshDef += float(row[7])
                countUnderThreshDef += 1

        if row[8] != 'NaN':
            if float(row[2]) > level_threshold:
                sumOverThreshHp += float(row[8])
                countOverThreshHp += 1

            else:
                sumUnderThreshHp += float(row[8])
                countUnderThreshHP += 1

    weakness_type_mapping = {weakness: sorted(types.items(), key=lambda x: (-x[1], x[0]))[0][0] for weakness, types in weakness_type_counter.items()}

    if countOverThreshAtk > 0:
        overThreshAverageAtk = round(sumOverThreshAtk / countOverThreshAtk, 1)

    if countUnderThreshAtk - 1> 0:
        underThreshAverageAtk = round(sumUnderThreshAtk / countUnderThreshAtk, 1)
    
    if countOverThreshDef > 0:
        overThreshAverageDef = round(sumOverThreshDef / countOverThreshDef, 1)
    
    if countUnderThreshDef - 1 > 0:
        underThreshAverageDef = round(sumUnderThreshDef / countUnderThreshDef, 1)

    if countOverThreshHp > 0:
        overThreshAverageHp = round(sumOverThreshHp / countOverThreshHp, 1)
    
    if countOverThreshHp - 1 > 0:
        underThreshAverageHp = round(sumUnderThreshHp / countUnderThreshHP, 1)

    for row in data:
        if row[0] == 'id': # Skip header
            continue
        
        if row[4] == 'NaN':
            row[4] = weakness_type_mapping[row[5]]
        
        if row[6] == 'NaN':
            if float(row[2]) > level_threshold:
                row[6] = overThreshAverageAtk
            else:
                row[6] = underThreshAverageAtk
        
        if row[7] == 'NaN':
            if float(row[2]) > level_threshold:
                row[7] = overThreshAverageDef
            else:
                row[7] = underThreshAverageDef

        if row[8] == 'NaN':
            if float(row[2]) > level_threshold:
                row[8] = overThreshAverageHp
            else:
                row[8] = underThreshAverageHp

    percentage = round((fire_level_40 / total_fire) * 100)

with open('pokemon1.txt', 'w') as f:
    f.write(f"Percentage of fire type Pokemons at or above level 40 = {percentage}")

with open('pokemonResult.csv', 'w') as f:
    writer = csv.writer(f)
    writer.writerows(data)

with open("pokemonResult.csv", "r") as f, open('pokemon4.txt', 'w') as f2, open('pokemon5.txt', 'w') as f3: 
    reader = csv.reader(f)
    data = list(reader)

    writer = csv.writer(f2)

    numStageThree = 0

    personality_mapping = defaultdict(set)
    for row in data:
        if row[0] == 'id': # Skip header
            continue
        personality_mapping[row[4]].add(row[3])

        if row[9] == '3.0':
            hpStageThreeSum += float(row[8])
            numStageThree += 1

    # Sort the keys alphabetically
    personality_mapping = {k: sorted(v) for k, v in sorted(personality_mapping.items())}

    writer.writerows(personality_mapping.items())

    print(f"Average hit point for Pokemons of stage 3.0 = {round(hpStageThreeSum / numStageThree)}", file=f3)