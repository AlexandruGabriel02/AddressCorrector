european_country_codes = ["AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "MD"]
fileName = "data/allCountries.txt"

def parse_file():
    output = open("data/allEuropeanCities.txt", "w", encoding="utf8")
    with open(fileName, encoding="utf8") as file:
        for line in file:
            line = line.strip()
            line = line.split("\t")
            if(line[0].strip() == '2598303'):
                print('here')
                for i in range(0, len(line)):
                    print(line[i] + " ")
           
            if line[8] not in european_country_codes:
                continue

            id = line[0]
            name = line[1]
            asciiname = line[2]
            alternatenames = line[3].strip()
            feature_class = line[6]
            admin2_code = line[11].strip()
            if feature_class != "P":
                continue
            country_code = line[8].strip()
            admin1_code = line[10].strip()
            admin_code = country_code + "." + admin1_code
            output.write(id + "\t" + name + "\t" + asciiname  + "\t"+ alternatenames+ "\t" + admin_code + "\t\n")
    output.close()

parse_file()