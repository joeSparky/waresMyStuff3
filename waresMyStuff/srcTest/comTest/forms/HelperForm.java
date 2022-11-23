package comTest.forms;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;

public class HelperForm extends SmartForm{

	public HelperForm(FormsMatrixDynamic callBack) throws Exception {
		super(callBack, new SessionVars());
	}
}
