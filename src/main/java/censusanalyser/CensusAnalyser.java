package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import IndiaCensusDAO.IndiaCensusDAO;
import com.google.gson.Gson;

import javax.naming.ldap.LdapName;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    Map<String,IndiaCensusDAO> censusList = null;
    private List<IndiaStateCodeCSV> stateCodeList;

    public CensusAnalyser() {
        this.censusList = new HashMap<>();
        this.stateCodeList = new ArrayList<>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try( Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            while(csvFileIterator.hasNext()){
                IndiaCensusCSV censusCSV = csvFileIterator.next();
                this.censusList.put(censusCSV.state, new IndiaCensusDAO(censusCSV));
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

    public int loadIndiaStateCodeData(String csvFilePath) throws CensusAnalyserException {
        try( Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> stateCSVIterator = csvBuilder.getCSVFileIterator(reader,IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> stateCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .forEach(data -> stateCodeList.add(data));
            return stateCodeList.size();
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
        Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.state);
//        this.sort(censusComparator);
        String sortedStateCensusJson = new Gson().toJson(this.censusList);
        return sortedStateCensusJson;
    }

    public String getSortedPopulationState() {
        Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.population);
        List list = censusList.values().stream()
                .sorted(censusComparator)
                .collect(Collectors.toList());
        String sortedStateCensusJson = new Gson().toJson(list);
        return sortedStateCensusJson;
    }

    public String getSortedPopulationDensity() {
        Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.densityPerSqkm);
        List list = censusList.values().stream()
                .sorted(censusComparator)
                .collect(Collectors.toList());
        String sortedStateCensusJson = new Gson().toJson(list);
        return sortedStateCensusJson;
    }

//    private void sort(Comparator<IndiaCensusDAO> censusComparator) {
//        for(int i=0;i<censusList.size()-1;i++){
//            for(int j=0;j<censusList.size()-i-1;j++){
//                IndiaCensusDAO census1 = censusList.get(j);
//                IndiaCensusDAO census2 = censusList.get(j+1);
//                if(censusComparator.compare(census1,census2) > 0){
//                    censusList.set(j, census2);
//                    censusList.set(j+1, census1);
//                }
//            }
//        }
//    }
}
