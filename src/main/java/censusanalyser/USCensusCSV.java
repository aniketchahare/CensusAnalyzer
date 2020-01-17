package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {

    @CsvBindByName(column = "State", required = true)
    public String state;

    @CsvBindByName(column = "State Id", required = true)
    public String stateId;

    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "Total Area", required = true)
    public double totalArea;

    @CsvBindByName(column = "Population Density", required = true)
    public double populationDensity;

    public USCensusCSV(String state, String stateCode, int population, double populationDensity, double totalArea) {
    }

    @Override
    public String toString() {
        return "IndiaCensusCSV{" +
                "State='" + state + '\'' +
                "State Id='" + stateId + '\'' +
                ", Population='" + population + '\'' +
                ", Total Area='" + totalArea + '\'' +
                ", Population Density='" + populationDensity + '\'' +
                '}';
    }
}
