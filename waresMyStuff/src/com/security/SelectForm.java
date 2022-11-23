package com.security;

import java.util.HashMap;

import com.db.SessionVars;
import com.forms.EndOfInputException;
import com.forms.EndOfInputRedoQueries;
import com.forms.FormsArray;
import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;
import com.forms.Utils;

public class SelectForm extends SmartForm {
SessionVars sVars = null;
	public SelectForm(FormsMatrixDynamic fm, SessionVars sVars) throws Exception {
		super(fm, sVars);
		this.sVars = sVars;
//		this.callBackVar = this;
		// save a pointer to the caller
//		this.caller = callBack;
		this.target = fm.getObject();

//		boundaryForm = target.getAttachToForms(fm);

//		fmd = fm;
	}

//	FormsMatrixDynamic fmd;
	protected MyObject target = null;
//	, belowTarget
//	aboveTarget 
//	= null;

//	protected SmartForm caller = null;
	protected final String CANCELBUTTON = Utils.getNextString();
	public String parentString = "";
//	AttachToForm boundaryForm = null;
	HashMap<String, Integer> childToId = new HashMap<String, Integer>();
	protected MyObject descendant = null;
	public String DESCENDANTBOX = Utils.getNextString();
	MyObject peer = null;
	String PEERBOX = Utils.getNextString();
	String INVENTORYBUTTON = Utils.getNextString();

	/**
	 * let the user know what would be deleted
	 */
	public final String DELETETESTBUTTON = Utils.getNextString();
	/**
	 * delete an object
	 */
	public final String DELETEBUTTON = Utils.getNextString();
//	public final String LINKPARENT = Utils.getNextString();
//	public final String UNLINKPARENT = Utils.getNextString();

	@Override
	public FormsArray getForm(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();

		if (!target.isLoaded())
			return ret;
		// throw an exception if there's no anchor set in the row
		// TODO reinstate?
//		fmd.getAnchor();

//		ret.addAll(fmd.editOrAddForm());
		ret.addAll(getFormSpare1());
//		ret.addAll(boundaryForm.getForm());
		if (target.isRecursive()) {
			ret.addAll(parentButton());
			ret.addAll(addKidsButtons());
			ret.addAll(addDescendant(sVars));
			ret.addAll(addPeer(sVars));
			if (target.hasInventoryField() && target.isLoaded()) {
				if (target.isOrphan(target)) {
					ret.rawText(target.getInstanceName() + " is an orphan or is at the top of its members");
					ret.newLine();
				} else
					ret.addAll(addInventoryButton());
			}
		}

		// dump the path to the top using the level above target as a parent type
		MyObjectsArray pathToTheTop = new MyObjectsArray();
		pathToTheTop.getSingleParentsToTop(fmd, sVars);
//			ret.rawText(target.getLogicalName() + ":" + target.getInstanceName());
//			ret.newLine();
		for (int i = pathToTheTop.size() - 1; i >= 0; i--) {
			ret.rawText(pathToTheTop.get(i).getLogicalName() + ":" + pathToTheTop.get(i).getInstanceName());
			ret.newLine();
		}
		ret.newLine();
//		ret.addAll(target.editOrAddForm(inState));
		if (target.isLoaded())
			ret.addAll(deleteCancelButtons());
//		ret.rawText("<br>end of SelectForm<br>");
		return ret;
	}

	protected FormsArray deleteCancelButtons() {
		FormsArray ret = new FormsArray();
		ret.submitButton("Cancel " + target.getInstanceName(), CANCELBUTTON);
		ret.submitButton("Test Delete " + target.getInstanceName(), DELETETESTBUTTON);
		ret.submitButton("Delete (No Undo) " + target.getInstanceName(), DELETEBUTTON);
		return ret;
	}

	protected FormsArray getFormSpare1() throws Exception {
		return new FormsArray();
	}

	@Override
	public FormsArray extractParams(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
		if (sVars.hasParameterKey(CANCELBUTTON)) {
			fmd.getObject().clear();
			fmd.resetAllIdAndStrings();
			throw new EndOfInputRedoQueries(fmd);
		}
		if (sVars.hasParameterKey(DELETETESTBUTTON)) {
			target.deleteTest();
			ret.errorToUser("No problems with deleting " + target.getInstanceName());
			throw new EndOfInputException(ret);
		}
		if (sVars.hasParameterKey(DELETEBUTTON)) {
			try {
				ret.errorToUser(target.getInstanceName());
				target.deleteUnconditionally();
				ret.errorToUser(" was deleted.");
				fmd.get(fmd.row).get(fmd.column).clear();
//				objs.clearChildren(index);
			} catch (Exception e) {
				ret.errorToUser(" was not deleted.");
				ret.errorToUser(e);
			}
			fmd.resetAllIdAndStrings();
			throw new EndOfInputRedoQueries(fmd);
		}
		if (sVars.hasParameterKey(INVENTORYBUTTON)) {
			if (!target.isOrphan(target)) {
				ret.addAll(target.markAsInventoried());
//				ret.addAll(updateTheCaller(sVars, target));
			} else {
				ret.errorToUser(target.getInstanceName() + " is an orphan or is at the top of its members");
			}
			throw new EndOfInputException(ret);
		}
//		Anchor anchor = fmd.getAnchor();
//		ret.addAll(fmd.extractEditAddFormParams(sVars));
		ret.addAll(super.extractParams(sVars));
		ret.addAll(extractSpare(sVars));
//		ret.addAll(boundaryForm.extractHandleLinkBetween(sVars, callBackVar, this, this, fmd));

		// if a select parent button was created and selected
		if (!parentString.isEmpty() && sVars.hasParameterKey(parentString)) {
			MyObject parent = target.getSingleParent(target);
//			ret.addAll(switchAndUpdate(sVars, parent));
			target.find(parent.id);
			fmd.resetAllIdAndStrings();
			throw new EndOfInputRedoQueries(fmd);
		}
		for (String thisKey : childToId.keySet()) {
			if (sVars.hasParameterKey(thisKey)) {
				MyObject tmp = target.getNew();
				tmp.find(childToId.get(thisKey));
				target.find(tmp.id);
				fmd.resetAllIdAndStrings();
				throw new EndOfInputRedoQueries(fmd);
			}
		}

		ret.addAll(processDescendant(sVars));
		ret.addAll(processPeer(sVars));
		return ret;
	}

	protected FormsArray extractSpare(SessionVars sVars) throws Exception {
		return new FormsArray();
	}

//	@Override
//	public FormsArray callBack(SessionVars sVars, FormsMatrixDynamic calledInfo) throws Exception {
//		FormsArray ret = new FormsArray();
////		
////				switch (calledInfo.action) {
////				case DELETE:
////					break;
////				case DELETETEST:
////					try {
////						target.find(tmp.getId());
////					} catch (Exception e) {
////						ret.errorToUser(e);
//////						ret.addAll(updateTheCaller(sVars, target));
////						throw new EndOfInputException(ret);
////					}
////					try {
////						target.deleteTest();
////						ret.errorToUser(target.getLogicalName() + ":" + target.getInstanceName() + " has no children.");
//////						ret.addAll(updateTheCaller(sVars, target));
////						throw new EndOfInputException(ret);
////					} catch (Exception e) {
////						// target.clear();
////						ret.errorToUser(e);
//////						ret.addAll(updateTheCaller(sVars, target));
//////						return ret;
////						throw new EndOfInputException(ret);
////					}
////				case SELECT:
////					target.find(tmp.getId());
////					objs.clearChildren(index);
//////					calledInfo.status = FormsArray.STATUS.SELECTEDEXISTING;
////					ret.addAll(caller.callBack(sVars, calledInfo));
////					return ret;
////				default:
////					break;
////
////				}
////			}
////			ret.errorToUser("undefined object in calledInfo " + calledInfo.toString());
////			throw new EndOfInputException(ret);
////		}
//		return ret;
//	}

//	public FormsArray updateTheCaller(SessionVars sVars, MyObject oc) throws Exception {
//		FormsArray ret = new FormsArray();
//		ret.addAll(caller.callBack(sVars, new CalledInfo(this, true, FormsArray.STATUS.TRYAGAIN, oc)));
//		return ret;
//	}

//	FormsArray tryAgain(SessionVars sVars, CalledInfo oc) throws Exception {
//		FormsArray ret = new FormsArray();
//		ret.addAll(caller.callBack(sVars, oc));
//		return ret;
//	}

//	@Override
//	public void resetForm(INSTATE inState) {
//		super.resetForm(INSTATE.STANDARDFORM);
//	}

	// add a button to select the parent of the currently selected object
	public FormsArray parentButton() {
		FormsArray ret = new FormsArray();
		MyObject parent = null;
		try {
			parent = target.getParents(target).iterator().next();
			parentString = Utils.getNextString();
			ret.rawText("parent of " + target.getInstanceName());
			ret.submitButton(parent.getInstanceName(), parentString);
			ret.newLine();
		} catch (Exception e) {
			parentString = "";
		}
		return ret;
	}

	public FormsArray addKidsButtons() {
		FormsArray ret = new FormsArray();
		boolean kidsFound = false;
		// add buttons to select the children of the currently selected object
		childToId = new HashMap<String, Integer>();
		// can not have kids if not selected
		if (!target.isLoaded())
			return ret;
		try {
			for (MyObject child : target.listChildren(target)) {
				if (!kidsFound) {
					kidsFound = true;
					ret.rawText("Select a child of " + target.getInstanceName());
					ret.newLine();
				}
				String idString = Utils.getNextString();
				ret.submitButton(child.getInstanceName(), idString);
				childToId.put(idString, child.id);
			}
		} catch (Exception e) {
			ret.errorToUser(e);
		}
		return ret;
	}

	public FormsArray addInventoryButton() throws Exception {
		FormsArray ret = new FormsArray();
		// get the target's parent
		MyObject parent = target.getSingleParent(target);
		ret.submitButton("Mark " + target.getInstanceName() + " as stored at " + parent.getInstanceName(),
				INVENTORYBUTTON);
		return ret;
	}

	protected FormsArray addDescendant(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
		if (target.isLoaded()) {
			// adding a sibling to target
			ret.newLine();
			ret.startTable();
			ret.rawText("Add a descendant to " + target.getInstanceName());
			descendant = target.getNew();
			ret.addAll(descendant.getNameForm(DESCENDANTBOX,
//					inState, 
					descendant.isLoaded()));
			ret.endTable();
		} else {
			descendant = null;
		}
		return ret;
	}

	protected FormsArray addPeer(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
		if (target.isLoaded()) {
			ret.startTable();
			ret.rawText("Add a peer to " + target.getInstanceName());
			peer = target.getNew();
			ret.addAll(peer.getNameForm(PEERBOX,
//					inState, 
					peer.isLoaded()));
			ret.endTable();
			// ret.submitButton("Add peer", "dgs");
		} else {
			peer = null;
		}
		return ret;
	}

	protected FormsArray processPeer(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
		if (peer != null) {
			if (peer.nameChanged(sVars, PEERBOX)) {
				peer.extractName(sVars, PEERBOX);
				peer.doSanityUpdateAddTryAgain(fmd);
				MyObject parent = null;
				try {
					parent = target.getParents(target).iterator().next();
				} catch (Exception e) {
					// if we don't find a parent, target must be at the top
					// of the heap
				}
				// if target has a parent
				if (parent != null)
					parent.addChild(peer);
				fmd.resetAllIdAndStrings();
				throw new EndOfInputException(ret);
			}
		}
		return ret;
	}

	// allow location to override to get a warehouse for an anchor
	protected FormsArray processDescendant(SessionVars sVars)
			throws Exception, EndOfInputRedoQueries, EndOfInputException {
		FormsArray ret = new FormsArray();
		if (descendant != null) {
			if (descendant.nameChanged(sVars, DESCENDANTBOX)) {
				descendant.extractName(sVars, DESCENDANTBOX);
				descendant.doSanityUpdateAddTryAgain(fmd);
				target.addChild(descendant);
				target.find(descendant.id);
				fmd.resetAllIdAndStrings();
				throw new EndOfInputRedoQueries(fmd);
			}
		}
		return ret;
	}

//	protected FormsArray switchAndUpdate(SessionVars sVars, MyObject oc) throws Exception {
//		FormsArray ret = new FormsArray();
//		target.find(oc.id);
//		ret.addAll(updateTheCaller(sVars, target));
//		return ret;
//	}
}
