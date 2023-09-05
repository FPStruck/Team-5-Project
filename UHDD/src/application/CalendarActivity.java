package application;

import java.time.ZonedDateTime;

/**
 * This is used for the Calendar Controller and Activity Creation Controller
 * Just a simple class for the activities with getters and setters 
 * @author - Team 5
 * @deprecated - used for the new calendar
 */

public class CalendarActivity {
    private ZonedDateTime date;
    private String clientName;
    private String doctorName;
    private Integer patiantId;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    
    /**
     * This will get the patient id
     * @return - the patient's id is an integer
     */
    public Integer getPatiantId() {
		return patiantId;
	}

    /**
     * This will set the patient id
     * @param - the patiantId is an integer
     */
	public void setPatiantId(Integer patiantId) {
		this.patiantId = patiantId;
	}
	
	/**
	 * This will get the year
	 * @return - the year is an integer
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * This will set the year
	 * @param - the year is an integer
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * This will get the month
	 * @return - the month is an integer
	 */
	public Integer getMonth() {
		return month;
	}

	/**
	 * This will set the month
	 * @param - the month is an integer
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * This will get the day
	 * @return - the day is an integer
	 */
	public Integer getDay() {
		return day;
	}

	/**
	 * This will set the day
	 * @param - the day is an integer
	 */
	public void setDay(Integer day) {
		this.day = day;
	}

	/**
	 * This will get the hour
	 * @return - the hour is an integer
	 */
	public Integer getHour() {
		return hour;
	}

	/**
	 * This will set the hour
	 * @param - the hour is an integer
	 */
	public void setHour(Integer hour) {
		this.hour = hour;
	}

	/**
	 * This will get the minute
	 * @return - the minute is an integer
	 */
	public Integer getMinute() {
		return minute;
	}

	/**
	 * This will set the minute
	 * @param - the minute is an integer
	 */
	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	// Constructors
	
	/**
	 * This constructor will create a calendar activity with three parameters
	 * @param - the date is a zone date time
	 * @param - the clientName is a string
	 * @param - the patientNo is an integer
	 */
	public CalendarActivity(ZonedDateTime date, String clientName, Integer patientNo) {
        this.date = date;
        this.clientName = clientName;
        this.patiantId = patientNo;
    }
    
	/**
	 * This is a constructor will create a calendar activity with four parameters
	 * @param - the string(date) is a zone data time
	 * @param - the clientName is a string
	 * @param - the doctorName is a string
	 * @param - the i(patient id) is an integer
	 */
    public CalendarActivity(ZonedDateTime string, String clientName, String doctorName, Integer i) {
    	this.date = string;
        this.clientName = clientName;
        this.doctorName = doctorName;
        this.patiantId = i;
    }

    /**
     * default constructor
     */
    public CalendarActivity() {
		// TODO Auto-generated constructor stub
	}
    
    /**
     * This will get the date
     * @return - this will return the date in a zone date time class
     */
	public ZonedDateTime getDate() {
        return date;
    }

	/**
	 * This will set the date
	 * @param - the date is a zone date time
	 */
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    /**
     * This will get the client name
     * @return - the client name is a string
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * This will set the client name
     * @param - the clientName is a string
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * This will get the patient number
     * @return - the patient number is an integer
     */
    public Integer getPatientNo() {
        return patiantId;
    }

    /**
     * This will set the patient number
     * @param - the patientNo is an integer
     */
    public void setPatientNo(Integer patientNo) {
        this.patiantId = patientNo;
    }

    /**
     * This will return the class as a string with variables 
     * It will override the default to string function 
     * @return - the date, client name and service number 
     */
    @Override
    public String toString() {
        return "CalenderActivity{" +
                "date=" + date +
                ", clientName='" + clientName + '\'' +
                ", serviceNo=" + patiantId +
                '}';
    }

    /**
     * This will get the doctor's name
     * @return - the doctor name is a string
     */
	public String getDoctorName() {
		return doctorName;
	}

	/**
	 * This will set the doctor name
	 * @param - the doctor's name is a string
	 */
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
}
