package censusanalyser;

import IndiaCensusDAO.CensusDAO;
import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    public enum Country { INDIA, US };
    private Country country;
    Map<String, CensusDAO> censusList = null;
    private List<IndiaStateCodeCSV> stateCodeList;

    public CensusAnalyser(Country country) {
        this.country = country;
    }

    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        censusList = CensusAdapterFactory.getCensusData(country, csvFilePath);
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
        ArrayList censusDTOS = censusList.values().stream().
                sorted(censusComparator).
                map(censusDAO -> censusDAO.getCensusDTO(country)).
                collect(Collectors.toCollection(ArrayList::new));
        String sortedStateCensusJson = new Gson().toJson(censusDTOS);
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
}
