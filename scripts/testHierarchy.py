entity_map = {}

def create_mapping():
    with open("data/allEuropeanEntities.txt", "r", encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")
            entity_map[line[0]] = line[1]

def print_hierarchy():
    output = open("data/testHierarchy.txt", "w", encoding="utf8")
    with open("data/europeanHierarchy.txt", "r", encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")
            output.write(entity_map[line[0]] + " -> " + entity_map[line[1]] + "\n")
    output.close()

create_mapping()
print_hierarchy()