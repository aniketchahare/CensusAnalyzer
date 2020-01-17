package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import IndiaCensusDAO.CensusDAO;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class CensusAdapter {
    public abstract Map<String, CensusDAO> loadCensusData(String... csvFilePath) throws CensusAnalyserException;

    public <E> Map loadCensusData(Class<E> censusCSVClass, String csvFilePath) throws CensusAnalyserException {
        Map<String, CensusDAO> censusList = new HashMap<>();
        try( Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvIterator = csvBuilder.getCSVFileIterator(reader,censusCSVClass);
            Iterable<E> csvIterable = () -> csvIterator;
            if(censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map((IndiaCensusCSV.class::cast))
                        .forEach(censusCSV -> censusList.put(censusCSV.state, new CensusDAO(censusCSV)));
            } else if(censusCSVClass.getName().equals("censusanalyser.USCensusCSV")){
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map((USCensusCSV.class::cast))
                        .forEach(censusCSV -> censusList.put(censusCSV.state, new CensusDAO(censusCSV)));
            }
            return censusList;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.INCORRECT_FILE_DATA);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(), e.type.name());
        }
    }
}