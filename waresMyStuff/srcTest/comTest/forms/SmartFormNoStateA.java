package comTest.forms;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;

public class SmartFormNoStateA extends SmartForm {

	protected SmartFormNoStateA(FormsMatrixDynamic callBack) throws Exception {
		super(callBack, new SessionVars());
	}
}
