package censusanalyser;

import com.opencsv.bean.CsvBindByName;

// SrNo,StateName,TIN,StateCode
public class IndiaStateCodeCSV {
    @CsvBindByName(column = "SrNo", required = true)
    public int SrNo;

    @CsvBindByName(column = "StateName", required = true)
    public String  State;

    @CsvBindByName(column = "TIN", required = true)
    public int TIN;

    @CsvBindByName(column = "StateCode", required = true)
    public String StateCode;

    @Override
    public String toString() {
        return "IndiaStateCode{" +
                "SrNo=" + SrNo +
                ", StateName='" + State + '\'' +
                ", TIN=" + TIN +
                ", StateCode='" + StateCode + '\'' +
                '}';
    }

    public String getStateCode() {
        return StateCode;
    }
}