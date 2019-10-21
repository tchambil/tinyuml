/**
 * Copyright 2007 Wei-ju Wu
 *
 * This file is part of TinyUML.
 *
 * TinyUML is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * TinyUML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TinyUML; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.tinyuml.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is the default implementation of the UmlModel interface.
 *
 * @author Wei-ju Wu
 * @version 1.0
 */
public class UmlModelImpl implements UmlModel {

  private static final long serialVersionUID = -3440413376365267032L;
  // The list of main elements. Top-level elements go here. A top-level element
  // is an element without a parent namespace (package).
  private Set<UmlModelElement> mainElements = new HashSet<UmlModelElement>();
  private List<UmlDiagram> diagrams = new ArrayList<UmlDiagram>();

  /**
   * Constructor.
   */
  public UmlModelImpl() { }

  /**
   * {@inheritDoc}
   */
  public void addElement(UmlModelElement anElement) {
    mainElements.add(anElement);
  }

  /**
   * {@inheritDoc}
   */
  public boolean contains(UmlModelElement anElement) {
    return mainElements.contains(anElement);
  }

  /**
   * {@inheritDoc}
   */
  public Set<? extends NamedElement> getElements() {
    return mainElements;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "UML model";
  }

  /**
   * {@inheritDoc}
   */
  public void addDiagram(UmlDiagram diagram) {
    diagrams.add(diagram);
  }

  /**
   * {@inheritDoc}
   */
  public List<? extends UmlDiagram> getDiagrams() {
    return diagrams;
  }
}
