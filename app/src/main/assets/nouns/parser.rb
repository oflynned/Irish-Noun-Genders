require "json"

APRIL_8TH = 1554674400000.freeze

def open_file(filename)
    file_data = File.open("./" + filename + ".json", "rb").read
    JSON.parse(file_data)
end

def write_file(filename, contents)
    File.open("./#{filename}.json","w") do |f|
        f.write(contents.to_json)
    end
end

def parse_data(filename)
    puts "loading file..."
    json_data = open_file(filename)
    puts "loaded file. starting parsing"

    ga_terms_without_hyphens = json_data.select do |term|
        # remove any unusable irish terms with organic chemistry names off the bat
        term["ga"].count("-") < 2
    end

    terms_without_ga_and_en_hyphens = ga_terms_without_hyphens.reject do |term|
        # now remove any english translations containing hyphens
        # remove completely
        term["en"].any? {|en| en.count("-") > 1 || en.count("(") > 0 || en.count(",") > 0}
    end

    output = terms_without_ga_and_en_hyphens.reject do |term|
        term["en"].length == 0
    end

    output.map {|t| puts t}

    write_file(filename + "-groomed", output)
    puts "completed!"
end

parse_data("chemistry")