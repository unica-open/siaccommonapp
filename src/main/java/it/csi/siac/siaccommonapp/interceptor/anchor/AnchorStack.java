/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.interceptor.anchor;

import java.io.Serializable;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Stack;

/**
 * Defines the stack for the Anchors.
 *  
 * @author Alessandro Marchino
 * @version 1.0.0 04/10/2013
 *
 */
public class AnchorStack implements Serializable {
	
	private static final long serialVersionUID = 5592331058631417009L;

	/** The stack */
	private Stack<Anchor> anchorStack = new Stack<Anchor>();
	
	/** Default empty constructor */
	public AnchorStack() {
		super();
	}
	
	/**
	 * @return the anchorStack
	 */
	public Stack<Anchor> getAnchorStack() {
		return anchorStack == null ? new Stack<Anchor>() : anchorStack;
	}
	
	/**
	 * @param anchorStack the anchorStack to set
	 */
	public void setAnchorStack(Stack<Anchor> anchorStack) {
		this.anchorStack = anchorStack;
	}
	
	/**
	 * Rewinds the stack to the point given by the index.
	 * 
	 * @param index the index to rewind the stack at
	 */
	public void rewindAt(int index) {
		for (int i = index + 1, size = anchorStack.size(); i < size; i++) {
			this.anchorStack.remove(index + 1);
		}
	}
	
	/**
	 * Gives the index of the anchor, based on the {@link AnchorComparator#compare(Anchor, Anchor)} method.
	 * 
	 * @param anchor the anchor to find in the stack
	 * 
	 * @return the index of the element, if present; -1 otherwise
	 */
	public int indexOf(Anchor anchor) {
	    ListIterator<Anchor> iterator = anchorStack.listIterator();
	    Comparator<Anchor> comparator = new AnchorComparator();

	    while (iterator.hasNext()) {
	      Anchor a = iterator.next();
	      if (comparator.compare(a, anchor) == 0) {
	        return iterator.previousIndex();
	      }
	    }
	    // Fallback in case of not having found the anchor in the stack
	    return -1;
	  }
	
	/**
	 * Gives the last index of the anchor, based on the {@link AnchorComparator#compare(Anchor, Anchor)} method.
	 * 
	 * @param anchor the anchor to find in the stack
	 * 
	 * @return the index of the element, if present; -1 otherwise
	 */
	public int lastIndexOf(Anchor anchor) {
	    ListIterator<Anchor> iterator = anchorStack.listIterator(anchorStack.size());
	    Comparator<Anchor> comparator = new AnchorComparator();

	    while (iterator.hasPrevious()) {
	      Anchor a = iterator.previous();
	      if (comparator.compare(a, anchor) == 0) {
	        return iterator.nextIndex();
	      }
	    }
	    // Fallback in case of not having found the anchor in the stack
	    return -1;
	  }
	
}
