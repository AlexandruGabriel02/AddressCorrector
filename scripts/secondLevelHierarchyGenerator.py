city_state_map = {}
state_country_map = {}
stateId_geonameId_map = {}
stateIdentifier_geonameId_map = {} #stateIdentifier is the countryCode.stateCode

def parse_admin1codes_file(file_name):
    with open(file_name, encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")
            levels = line[0].split(".")
            country_id = levels[0]
            state_id = levels[1]
            geonameId = line[3]
            stateIdentifier_geonameId_map[country_id + "." + state_id] = geonameId

def generate_hierarchy():
    output = open("data/secondLevelHierarchy.txt", "w", encoding="utf8")
    with open("data/allEuropeanCities.txt", encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")

            if(len(line) > 4):
                state_identifier = line[4] 
            geoname_city_id = line[0]
            if(state_identifier in stateIdentifier_geonameId_map):
                geoname_state_id = stateIdentifier_geonameId_map[state_identifier]

            output.write(geoname_state_id + "\t" + geoname_city_id + "\n")
    output.close()

parse_admin1codes_file("data/admin1CodesASCII.txt")
generate_hierarchy()
        