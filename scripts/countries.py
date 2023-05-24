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

create_country_code_mapping()