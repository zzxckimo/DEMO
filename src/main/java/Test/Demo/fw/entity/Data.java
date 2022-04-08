package Test.Demo.fw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "data")
public class Data {
	@Id
	@Column(name="CODE")
    private String code;
	@Column(name="SYMBOL")
    private String symbol;
	@Column(name="RATE")
    private String rate;
	@Column(name="DESCRIPTION")
    private String description;
	@Column(name="RATE_FLOAT")
    private double rate_float;
	@Column(name="CNAME")
    private String cname;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public double getRate_float() {
		return rate_float;
	}

	public void setRate_float(double rate_float) {
		this.rate_float = rate_float;
	}

	public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

}