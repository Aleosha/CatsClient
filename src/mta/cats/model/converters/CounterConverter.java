package mta.cats.model.converters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mta.cats.model.Counter;
import mta.cats.model.Feeder;

public class CounterConverter implements IConverter<Counter> {

	static CounterConverter instance;
	
	private CounterConverter(){
	}
	
	public static IConverter<Counter> getInstance() {
		if (instance == null) {
			instance = new CounterConverter();
		}
		return instance;
	}
	
	@Override
	/**
	 * The result must have fields NAME and CNT
	 */
	public List<Counter> convert(ResultSet rs) {
		List<Counter> counters = new ArrayList<Counter>();
		if (rs != null){
			try {
				while (rs.next()) {
					Counter counter = new Counter();
					
					counter.setName(rs.getString("NAME"));
					counter.setCount(rs.getLong("CNT"));
					
					counters.add(counter);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<Counter>();
			}
		}
		
		return counters;
	}

}
