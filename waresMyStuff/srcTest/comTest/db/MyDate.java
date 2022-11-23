package comTest.db;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

//import java.sql.Date;

import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.Strings;

import comTest.security.Level1;

public class MyDate extends Level1 {

	public MyDate(SessionVars sVars) throws Exception {
		super(sVars);
	}

	private static final String INVENTORYDATE = "inventoryDate";
	boolean justInventoried = false;
	Date inventoryDate = null;

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add("`" + INVENTORYDATE + "` DATE NOT NULL");
		return ret;
	}

	@Override
	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		inventoryDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String stringDate = formatter.format(inventoryDate);
		ret.add(new DoubleString(INVENTORYDATE, stringDate));		
		return ret;
	}

	@Override
	public DoubleStrings extendUpdate() {
		if (justInventoried)
			// insert the current date in the inventory field
			return extendAdd();
		else
			// don't update the inventory field
			return new DoubleStrings();
	}

	protected MyDate extractInfo(ResultSet rs) throws Exception {
		inventoryDate = new Date(0);
		inventoryDate = rs.getDate(INVENTORYDATE);
		super.extractInfo(rs);
		return this;
	}

	public void inventoried() {
		justInventoried = true;
	}

	public String getInventoryDate() throws Exception {
		if (inventoryDate == null)
			throw new Exception(this.getInstanceName() + " has not been inventoried.");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		return formatter.format(inventoryDate);
	}

}
