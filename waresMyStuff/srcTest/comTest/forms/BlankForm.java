package comTest.forms;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;

public class BlankForm extends SmartForm {

	protected BlankForm(FormsMatrixDynamic fmd) throws Exception {
		super(fmd, new SessionVars());
	}

	public BlankForm() {
		super(null, null);
	}
}
