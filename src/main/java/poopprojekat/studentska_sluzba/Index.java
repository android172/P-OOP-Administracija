package poopprojekat.studentska_sluzba;

import java.util.regex.Pattern;

// Student Index class
// INCLUDES:
// Constructor              - (number, year) / (String_index_representation)
// get(Number, Year, Index) - return number, year, string representation of Index
// set(Number, Year, index) - set ~
// equals()                 - two indexes are same if corresponding number and year ints are equivalent;
public class Index {
    
    // vars
    private int number;
    private int year;

    // constructors
    // // works both with int number representation of index, and with string representation of index
    public Index(int number, int year) throws Exception {
        if (number <= 0)
            throw new Exception("Index number must be bigger then 0.");
        if (year <= 1900 || year > 9999)
            throw new Exception("Invalid value for year given: " + year + " (must be > 1900)");
        this.number = number;
        this.year = year;
    }
    public Index(String index_str) throws Exception {
        if (!Pattern.matches("\\d+/\\d{4}$", index_str))
            throw new Exception("Input format invalid, required format: (Any number > 0) / (4 digit year)\nGiven: " + index_str);
        this.number = Integer.parseInt(index_str.split("/")[0]);
        this.year = Integer.parseInt(index_str.split("/")[1]);
    }

    // access functions
    // // get value
    public int getNumber() {
        return number;
    }
    public int getYear() {
        return year;
    }
    public String getIndex() { // return string representation
        return number + "/" + year;
    }
    public String toString() { // return string representation
        return number + "/" + year;
    }
    // // set value
    public void setNumber(int number) throws Exception {
        if (number <= 0)
            throw new Exception("Index number must be bigger then 0.");
        this.number = number;
    }
    public void setYear(int year) throws Exception {
        if (year <= 1900 || year > 9999)
            throw new Exception("Invalid value for year given: " + year + " (must be > 1900)");
        this.year = year;
    }
    public void setIndex(int number, int year) throws Exception {
        if (number <= 0)
            throw new Exception("Index number must be bigger then 0.");
        if (year <= 1900 || year > 9999)
            throw new Exception("Invalid value for year given: " + year + " (must be > 1900)");
        this.number = number;
        this.year = year;
    }
    public void setIndex(String index_str) throws Exception {
        if (!Pattern.matches("\\d+/\\d{4}$", index_str))
            throw new Exception("Input format invalid, required format: (Any number > 0) / (4 digit year)\nGiven: " + index_str);
        this.number = Integer.parseInt(index_str.split("/")[0]);
        this.year = Integer.parseInt(index_str.split("/")[1]);
    }

    // compare two Index objects
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Index other = (Index) obj;
        if (this.number != other.number || this.year != other.year)
            return false;
        return true;
    }
}