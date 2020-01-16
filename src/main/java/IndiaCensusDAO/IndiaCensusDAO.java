package IndiaCensusDAO;

import censusanalyser.IndiaCensusCSV;

public class IndiaCensusDAO {
    public String state;
    public int population;
    public int densityPerSqkm;
    public int areaInSqKm;

    public IndiaCensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        densityPerSqkm = indiaCensusCSV.densityPerSqKm;
        population = indiaCensusCSV.population;
    }
}
