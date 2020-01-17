package IndiaCensusDAO;

import censusanalyser.IndiaCensusCSV;
import censusanalyser.IndiaStateCodeCSV;
import censusanalyser.USCensusCSV;

public class CensusDAO {
    public String state;
    public String stateCode;
    public int population;
    public double populationDensity;
    public double totalArea;

    public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        totalArea = indiaCensusCSV.areaInSqKm;
        populationDensity = indiaCensusCSV.densityPerSqKm;
        population = indiaCensusCSV.population;
    }

    public CensusDAO(IndiaStateCodeCSV stateCSV) {
        state = stateCSV.State;
    }

    public CensusDAO(USCensusCSV censusCSV) {
        state = censusCSV.state;
        stateCode = censusCSV.stateId;
        population = censusCSV.population;
        populationDensity = censusCSV.populationDensity;
        totalArea = censusCSV.totalArea;
    }

    @Override
    public String toString() {
        return "CensusDAO{" +
                "state='" + state + '\'' +
                ", population=" + population +
                ", populationDensity=" + populationDensity +
                ", totalArea=" + totalArea +
                '}';
    }
}
