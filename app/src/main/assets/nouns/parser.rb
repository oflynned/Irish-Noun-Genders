require "json"

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
        term["en"].any? {|en| en.count("-") > 1 || en.count("(") > 0 || en.count(",") > 0}
    end

    output = terms_without_ga_and_en_hyphens.reject do |term|
        # remove any items without any equivalent english terms due to hyphen removal, leaving just {en: []}
        term["en"].length == 0
    end

    write_file(filename + "-groomed", output)
    puts "completed!"
end

parse_data("chemistry")