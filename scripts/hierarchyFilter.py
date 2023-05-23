id_set = set()

def parse_file(file_name):
    with open(file_name, encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")
            id_set.add(line[0])

def filter_hierarchy(hierarchy_file_name):
    output = open("data/europeanHierarchy.txt", "w", encoding="utf8")
    with open(hierarchy_file_name, encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")
            if len(line) != 3:
                continue
            if line[0] not in id_set or line[1] not in id_set or line[2] != "ADM":
                continue
            
            output.write(line[0] + "\t" + line[1] + "\n")
    output.close()

parse_file("data/allEuropeanEntities.txt")
filter_hierarchy("data/hierarchy.txt")
