package com.outsera.goldenraspberryawards.core.helper;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Log4j2
public class CSVHelper {

    public static List<Map<String,Object>> readCSV(String filePath, String delimiter, Character quote, Set<String> headers) throws IOException {

        List<Map<String,Object>> records = new ArrayList<>();

        try {
            Reader in = new FileReader(filePath);
            Iterable<CSVRecord> rows = CSVFormat.RFC4180.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setDelimiter(delimiter)
                    .setQuote(quote)
                    .setIgnoreEmptyLines(true)
                    .build()
                    .parse(in);
            for (CSVRecord row : rows) {

                Map<String, Object> recordMap = new HashMap<>();
                headers.forEach(header -> {
                    if (!row.isSet(header)) {
                        log.warn("Row {}: Header {} not found in CSV file", header, row.getRecordNumber());
                        recordMap.put(header, null);
                    } else {

                        String words = row.get(header);
                        try {
                            recordMap.put(header, Integer.valueOf(words));
                        } catch (NumberFormatException e) {
                            recordMap.put(header, words);
                        }
                    }
                });

                records.add(recordMap);

            }
        } catch (FileNotFoundException e) {
            log.error("File not found: {} - the data stay empty", filePath);
        }

        return records;
    }

    public static List<Map<String, Set<Object>>> parseData(String cvsFilePath) {

        List<Map<String,Object>> dataFromCsv = new ArrayList<>();

        if(isNull(cvsFilePath)) {
            log.error("CSV file path not found in application.properties");
            return null;
        }

        try {
            dataFromCsv = CSVHelper.readCSV(cvsFilePath, ";", null,
                    Set.of("year", "title", "studios", "producers", "winner"));
        } catch (IOException e) {
            log.error("Error reading CSV file: {}", e.getMessage());
        }

        List<Map<String, Set<Object>>> parsedData = parseData(dataFromCsv, Map.of(
                        "studios", ",|,and\\s|\\sand\\s",
                        "producers", ",|,and\\s|\\sand\\s"
                )
        );

        return parsedData;

    }

    public static List<Map<String, Set<Object>>> parseData(List<Map<String, Object>> records, Map<String, String> headersToParse) {
        return records.stream().map(record -> {
            Map<String, Set<Object>> resultMap = new HashMap<>();

            record.forEach((key, value) -> {
                Set<Object> parsedValues = new HashSet<>();

                if (headersToParse.containsKey(key)) {

                    String splitRegex = headersToParse.get(key);

                    String[] fieldsValue = value.toString().split(splitRegex);

                    for (String fieldValue : fieldsValue) {
                        if (!fieldValue.trim().isEmpty()) {
                            parsedValues.add(fieldValue.trim());
                        }
                    }

                } else {
                    if (value instanceof String) {
                        parsedValues.add(value.toString().trim());
                    } else {
                        parsedValues.add(value);
                    }
                }

                resultMap.put(key, parsedValues);
            });

            return resultMap;
        }).collect(Collectors.toList());
    }

}
