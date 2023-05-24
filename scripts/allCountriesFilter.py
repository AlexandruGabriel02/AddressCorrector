european_country_codes = ["AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "MD"]
fileName = "data/allCountries.txt"

def parse_file():
    output = open("data/allEuropeanEntities.txt", "w", encoding="utf8")
    with open(fileName, encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")
           
            if line[8] not in european_country_codes:
                continue

            id = line[0]
            name = line[1]
            asciiname = line[2]
            alternatenames = line[3].strip()
            feature_class = line[6]
            admin2_code = line[11].strip()
            country_code = line[8].strip()
            admin1_code = line[10].strip()
            admin_code = country_code + "." + admin1_code
            feature_code = line[7].strip()
            
            if feature_code != "PCLI" and feature_code != "PCLIX":
                country_code = "-"

            output.write(id + "\t" + name + "\t" + asciiname  + "\t"+ alternatenames+ "\t" + country_code + "\n")
    output.close()

parse_file()