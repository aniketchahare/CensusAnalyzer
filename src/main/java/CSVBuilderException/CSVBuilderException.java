package CSVBuilderException;

import censusanalyser.CensusAnalyserException;

public class CSVBuilderException extends Exception {

    public enum ExceptionType {
        CENSUS_FILE_PROBLEM,UNABLE_TO_PARSE,INCORRECT_FILE_DATA
    }

    public ExceptionType type;

    public CSVBuilderException(String message, ExceptionType Type) {
        super(message);
        this.type = type;
    }
}
