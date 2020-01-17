package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import IndiaCensusDAO.CensusDAO;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    Map<String, CensusDAO> censusList = null;
    private List<IndiaStateCodeCSV> stateCodeList;

    public CensusAnalyser() {
        this.censusList = new HashMap<>();
        this.stateCodeList = new ArrayList<>();
    }

    public int loadIndiaStateCodeData(String csvFilePath) throws CensusAnalyserException {
        try( Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            while(csvFileIterator.hasNext()){
                IndiaStateCodeCSV censusCSV = csvFileIterator.next();
                this.censusList.put(censusCSV.StateCode, new CensusDAO(censusCSV));
            }
            return this.censusList.size();
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

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return this.loadCensusData(csvFilePath, IndiaCensusCSV.class);
    }

    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        return this.loadCensusData(csvFilePath, USCensusCSV.class);
    }

    private <E> int loadCensusData(String csvFilePath, Class<E> censusCSVClass) throws CensusAnalyserException {
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
            return censusList.size();
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

    private <E> int getCount(Iterator<E> iterator) {
        Iterable<E> csvIterable = () -> iterator;
        int numOfEnteries = (int) StreamSupport.stream(csvIterable.spliterator(),false).count();
        return numOfEnteries;
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if(censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census data", CensusAnalyserException.ExceptionType.No_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.state);
//        this.sort(censusComparator);
        String sortedStateCensusJson = new Gson().toJson(this.censusList);
        return sortedStateCensusJson;
    }

    public String getSortedPopulationState() {
        List list = censusList.values().stream()
                .sorted((data1,data2)->data1.population - data2.population > 0 ? -1 : 1)
                .collect(Collectors.toList());
        String sortedStateCensusJson = new Gson().toJson(list);
        return sortedStateCensusJson;
    }

    public String getSortedPopulationDensity() {
        List list = censusList.values().stream()
                .sorted((data1,data2)->data1.populationDensity - data2.populationDensity > 0 ? -1 : 1)
                .collect(Collectors.toList());
        String sortedStateCensusJson = new Gson().toJson(list);
        return sortedStateCensusJson;
    }

    public String getSortedStateByArea() {
        //Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.totalArea);
        List list = censusList.values().stream()
                .sorted((data1,data2)->data1.totalArea - data2.totalArea > 0 ? -1 : 1)
                .collect(Collectors.toList());
        String sortedStateCensusJson = new Gson().toJson(list);
        return sortedStateCensusJson;
    }

//    private void sort(Comparator<CensusDAO> censusComparator) {
//        for(int i=0;i<censusList.size()-1;i++){
//            for(int j=0;j<censusList.size()-i-1;j++){
//                CensusDAO census1 = censusList.get(j);
//                CensusDAO census2 = censusList.get(j+1);
//                if(censusComparator.compare(census1,census2) > 0){
//                    censusList.set(j, census2);
//                    censusList.set(j+1, census1);
//                }
//            }
//        }
//    }
}
