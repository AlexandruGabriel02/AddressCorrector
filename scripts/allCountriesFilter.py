european_country_codes = ["AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "ES", "SE"]
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
            alternatenames = line[3]
            feature_class = line[6]
            
            if feature_class != "P" and feature_class != "A":
                continue

            output.write(id + "\t" + name + "\t" + asciiname + "\t" + alternatenames + "\n")
    output.close()

parse_file()