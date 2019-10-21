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
package org.tinyuml.umldraw.structure;

import java.util.HashMap;
import java.util.Map;
import org.tinyuml.model.RelationType;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.Relation;
import org.tinyuml.model.UmlClass;
import org.tinyuml.model.UmlComponent;
import org.tinyuml.model.UmlPackage;
import org.tinyuml.draw.LineConnectMethod;
import org.tinyuml.model.UmlRelation;
import org.tinyuml.umldraw.shared.NoteConnection;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.shared.UmlConnection;
import org.tinyuml.umldraw.shared.UmlDiagramElement;
import org.tinyuml.umldraw.shared.UmlNode;
import org.tinyuml.umldraw.structure.Association.AssociationType;

/**
 * Implementation of the DiagramElementFactory interface. A
 * DiagramElementFactory instance belongs to a particular UmlDiagram instance,
 * so it can automatically associate elements to the diagram they belong to.
 *
 * @author Wei-ju Wu
 * @version 1.0
 */
public class DiagramElementFactoryImpl implements DiagramElementFactory {

  private Map<ElementType, UmlDiagramElement> elementPrototypes =
    new HashMap<ElementType, UmlDiagramElement>();
  private Map<RelationType, UmlConnection> connectionPrototypes =
    new HashMap<RelationType, UmlConnection>();
  private StructureDiagram diagram;

  /**
   * Constructor.
   * @param aDiagram the diagram this factory belongs to
   */
  public DiagramElementFactoryImpl(StructureDiagram aDiagram) {
    diagram = aDiagram;
    setupElementPrototypeMap();
    setupConnectionPrototypeMap();
  }

  /**
   * Initializes the element map with the element prototypes.
   */
  private void setupElementPrototypeMap() {
    NoteElement notePrototype = (NoteElement)
      NoteElement.getPrototype().clone();
    elementPrototypes.put(ElementType.NOTE, notePrototype);

    // Add package prototype
    UmlPackage pkg = (UmlPackage) UmlPackage.getPrototype().clone();
    PackageElement pkgPrototype = (PackageElement)
      PackageElement.getPrototype().clone();
    pkg.setName("Package 1");
    pkgPrototype.setUmlPackage(pkg);
    elementPrototypes.put(ElementType.PACKAGE, pkgPrototype);

    // add component prototype
    UmlComponent comp = (UmlComponent) UmlComponent.getPrototype().clone();
    comp.setName("Component 1");
    ComponentElement compElem = (ComponentElement)
      ComponentElement.getPrototype().clone();
    compElem.setModelElement(comp);
    compElem.addNodeChangeListener(diagram);
    elementPrototypes.put(ElementType.COMPONENT, compElem);

    // Add class prototype
    UmlClass clss = (UmlClass) UmlClass.getPrototype().clone();
    clss.setName("Class 1");
    ClassElement classElem = (ClassElement) ClassElement.getPrototype().clone();
    classElem.setModelElement(clss);
    classElem.addNodeChangeListener(diagram);
    elementPrototypes.put(ElementType.CLASS, classElem);
  }

  /**
   * Initializes the map with the connection prototypes.
   */
  private void setupConnectionPrototypeMap() {
    UmlRelation notnavigable = new UmlRelation();
    notnavigable.setCanSetElement1Navigability(false);
    notnavigable.setCanSetElement2Navigability(false);
    UmlRelation fullnavigable = new UmlRelation();
    fullnavigable.setCanSetElement1Navigability(true);
    fullnavigable.setCanSetElement2Navigability(true);
    UmlRelation targetnavigable = new UmlRelation();
    targetnavigable.setCanSetElement1Navigability(false);
    targetnavigable.setCanSetElement2Navigability(true);

    Dependency depPrototype = (Dependency) Dependency.getPrototype().clone();
    depPrototype.setRelation((Relation) notnavigable.clone());
    connectionPrototypes.put(RelationType.DEPENDENCY, depPrototype);

    Association assocPrototype = (Association)
      Association.getPrototype().clone();
    assocPrototype.setRelation((Relation) fullnavigable.clone());
    connectionPrototypes.put(RelationType.ASSOCIATION, assocPrototype);

    Association compPrototype = (Association)
      Association.getPrototype().clone();
    compPrototype.setAssociationType(AssociationType.COMPOSITION);
    compPrototype.setRelation((Relation) targetnavigable.clone());
    connectionPrototypes.put(RelationType.COMPOSITION, compPrototype);

    Association aggrPrototype = (Association)
      Association.getPrototype().clone();
    aggrPrototype.setAssociationType(AssociationType.AGGREGATION);
    aggrPrototype.setRelation((Relation) targetnavigable.clone());
    connectionPrototypes.put(RelationType.AGGREGATION, aggrPrototype);

    Inheritance inheritPrototype = (Inheritance)
      Inheritance.getPrototype().clone();
    inheritPrototype.setRelation((Relation) notnavigable.clone());
    connectionPrototypes.put(RelationType.INHERITANCE, inheritPrototype);

    Inheritance interfRealPrototype = (Inheritance)
      Inheritance.getPrototype().clone();
    interfRealPrototype.setRelation((Relation) notnavigable.clone());
    interfRealPrototype.setIsDashed(true);
    connectionPrototypes.put(RelationType.INTERFACE_REALIZATION,
      interfRealPrototype);

    connectionPrototypes.put(RelationType.NOTE_CONNECTOR,
      NoteConnection.getPrototype());
  }

  /**
   * {@inheritDoc}
   */
  public UmlNode createNode(ElementType elementType) {
    UmlNode umlnode = (UmlNode) elementPrototypes.get(elementType).clone();
    umlnode.addNodeChangeListener(diagram);
    return umlnode;
  }

  /**
   * {@inheritDoc}
   */
  public UmlConnection createConnection(RelationType relationType,
    UmlNode node1, UmlNode node2) {
    UmlConnection prototype = connectionPrototypes.get(relationType);
    UmlConnection conn = null;
    if (prototype != null) {
      conn = (UmlConnection) prototype.clone();
      bindConnection(conn, node1, node2);
    }
    return conn;
  }

  /**
   * {@inheritDoc}
   */
  public LineConnectMethod getConnectMethod(RelationType relationType) {
    UmlConnection conn = connectionPrototypes.get(relationType);
    return (conn == null) ? null : conn.getConnectMethod();
  }

  /**
   * Binds the UmlConnection to the nodes.
   * @param conn the Connection
   * @param node1 the Node 1
   * @param node2 the Node 2
   */
  private void bindConnection(UmlConnection conn, UmlNode node1,
    UmlNode node2) {
    conn.setNode1(node1);
    conn.setNode2(node2);
    node1.addConnection(conn);
    node2.addConnection(conn);
    Relation relation = (Relation) conn.getModelElement();
    if (relation != null) {
      relation.setElement1(node1.getModelElement());
      relation.setElement2(node2.getModelElement());
    }
  }
}
