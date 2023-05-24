country_code_map = {}

def create_country_code_mapping():
    with open("data/allEuropeanEntities.txt", "r", encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")

            country_id = line[0]
            country_code = line[4]    
            
            if country_code == "-":
                continue
            
            print(country_code + " " + country_id)
            country_code_map[country_code] = country_id


def map_state_to_country():
    output = open("data/secondLevelHierarchy.txt", "a", encoding="utf8")
    with open("data/admin1CodesASCII.txt", encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")
            levels = line[0].split(".")
            country_code = levels[0]
            state_id = line[3]
            if country_code in country_code_map:
                output.write(country_code_map[country_code]  + "\t" + state_id + "\n")
    output.close()

create_country_code_mapping()
map_state_to_country()