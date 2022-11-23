package com.security;

import java.util.ArrayList;
import java.util.Iterator;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.SearchTargets;

public class MyObjectsArray extends ArrayList<MyObject> {

	private static final long serialVersionUID = 3099532864055480496L;

	public int findObjectUsingClassName(MyObject me) throws Exception {
		for (int i = 0; i < size(); i++)
			if (get(i).getMyFileName().equals(me.getMyFileName()))
				return i;
		return -1;
	}

	/**
	 * assuming there are 2 instances of the same class, find the one that's not me
	 * 
	 * @param me
	 * @return
	 * @throws Exception
	 */
	public int findOtherObjectUsingClassName(MyObject me) throws Exception {
		for (int i = 0; i < size(); i++)
			if (get(i).getMyFileName().equals(me.getMyFileName()) && get(i).id != me.id)
				return i;
		throw new Exception(me.getLogicalName() + " is not in stack");
	}

	public boolean canHaveChildren(MyObject me) throws Exception {
		return findObjectUsingClassName(me) < size() - 1;
	}

	public boolean canHaveParent(MyObject me) throws Exception {
		return findObjectUsingClassName(me) > 0;
	}

	/**
	 * select the parents of everyone above me, if there's only one parent
	 * 
	 * @param me
	 * @throws Exception
	 */
//	public void selectParents(int myIndex) throws Exception {
//		if (!get(myIndex).isLoaded())
//			throw new Exception("not loaded");
//		// int meIndex = findMe(me);
//		for (int i = myIndex; i > 0; i--) {
//			MyObject aboveMe = get(i - 1);
//			MyObject thatsMe = get(i);
//			MyObjects myParents = thatsMe.getParents(aboveMe);
//			if (myParents.size() == 1)
//				// set the parent
//				aboveMe.find(myParents.iterator().next().id);
//			else
//				break;
//		}
//	}

	/**
	 * clear everyone below me
	 * 
	 * @param me
	 * @throws Exception
	 */
	public void clearChildren(int myIndex) throws Exception {
		for (int i = myIndex + 1; i < size(); i++)
			get(i).clear();
	}

	public MyObjects getParents(int indexOfChild, SessionVars sVars) throws Exception {
		if (indexOfChild > 0)
			return new MyObjects().getParents(get(indexOfChild - 1), get(indexOfChild), sVars);
		else
			return new MyObjects();
	}

	/**
	 * return a list of MyObjects that are above this. If this is at the top of the
	 * tree, return an empty list.
	 * 
	 * @return
	 * @throws Exception
	 */
	public MyObjectsArray getRecursiveParents(MyObject startHere, SessionVars sVars) throws Exception {
		if (!startHere.isLoaded())
			throw new Exception(startHere.getAName() + " is not loaded. " + new Exception().getStackTrace()[0]);
		if (!startHere.isRecursive())
			throw new Exception(
					startHere.getInstanceName() + " is not recursive. " + new Exception().getStackTrace()[0]);
		MyObjects tmp = new MyObjects();
		MyObject clone = startHere.getNew().find(startHere.id);
		while (true) {
			tmp.clear();
			tmp.addAll(new MyLinkObject(clone, clone, sVars).listParentsOfChild());
			if (tmp.isEmpty())
				return this;
			if (tmp.size() > 1)
				throw new Exception("multiple parents of " + clone.getInstanceName());
			clone = tmp.iterator().next();
			add(clone);
		}
	}

	/**
	 * get the index of the lowest (highest Index) loaded object. return -1 if none
	 * are loaded
	 * 
	 * @return
	 */
	public int getLowestLoadedIndex() {
		for (int myIndex = size() - 1; myIndex >= 0; myIndex--) {
			if (get(myIndex).isLoaded())
				return myIndex;
		}
		return -1;
	}

	/**
	 * get the index of the highest (lowest Index) loaded object. return -1 if none
	 * are loaded
	 * 
	 * @return
	 */
	public int getHighestLoadedIndex() {
		for (int i = 0; i < size(); i++)
			if (get(i).isLoaded())
				return i;
		return -1;
	}

	/**
	 * return MyOjectsArray of the single parents above the object at index of the
	 * stack. Stop at the top or an unloaded layer
	 * 
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public MyObjectsArray getSingleParentsToTop(FormsMatrixDynamic fmd, SessionVars sVars) throws Exception {
		SearchTargets stack = fmd.getRow();
		for (int i = fmd.column; i >= 0; i--) {
			if (!stack.get(i).obj.isLoaded())
				return this;
			if (stack.get(i).obj.isRecursive())
				getRecursiveParents(stack.get(i).obj, sVars);
			else
				add(stack.get(i).obj);
		}
		return this;
	}
	
	public boolean containsMyObject(MyObject obj) throws Exception {
		Iterator<MyObject> itr = iterator();
		while (itr.hasNext()) {
			if (itr.next().equals(obj))
				return true;
		}
		return false;
	}
	

}
