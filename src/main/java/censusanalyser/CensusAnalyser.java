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

    public int loadIndiaCensusData(String... csvFilePath) throws CensusAnalyserException {
        censusList = new CensusLoader().loadCensusData(IndiaCensusCSV.class, csvFilePath);
        return censusList.size();
    }

    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        censusList = new CensusLoader().loadCensusData(USCensusCSV.class, csvFilePath);
        return censusList.size();
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
