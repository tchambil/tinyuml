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
package org.tinyuml.ui;

import java.util.HashMap;
import java.util.Map;
import org.tinyuml.draw.Scaling;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.RelationEndType;
import org.tinyuml.model.RelationType;
import org.tinyuml.util.AppCommandListener;
import org.tinyuml.ui.diagram.DiagramEditor;
import org.tinyuml.util.MethodCall;

/**
 * This class receives Editor related AppCommands and dispatches them to
 * the right places. This offloads editor related commands from the
 * AppFrame object, while AppFrame handles commands on a global level,
 * EditorCommandDispatcher handles it on the level of the current editor.
 *
 * @author Wei-ju Wu
 * @version 1.0
 */
public class EditorCommandDispatcher implements AppCommandListener {

  private AppFrame frame;
  private Map<String, MethodCall> selectorMap =
    new HashMap<String, MethodCall>();

  /**
   * Constructor.
   * @param aFrame the application frame
   */
  public EditorCommandDispatcher(AppFrame aFrame) {
    this.frame = aFrame;
    initSelectorMap();
  }

  /**
   * Initializes the selector map.
   */
  private void initSelectorMap() {
    try {
      selectorMap.put("SELECT_MODE", new MethodCall(
        DiagramEditor.class.getMethod("setSelectionMode")));
      selectorMap.put("REDO", new MethodCall(
        DiagramEditor.class.getMethod("redo")));
      selectorMap.put("UNDO", new MethodCall(
        DiagramEditor.class.getMethod("undo")));
      selectorMap.put("REDRAW", new MethodCall(
        DiagramEditor.class.getMethod("redraw")));
      selectorMap.put("ZOOM_50", new MethodCall(
        DiagramEditor.class.getMethod("setScaling", Scaling.class),
          Scaling.SCALING_50));
      selectorMap.put("ZOOM_75", new MethodCall(
        DiagramEditor.class.getMethod("setScaling", Scaling.class),
          Scaling.SCALING_75));
      selectorMap.put("ZOOM_100", new MethodCall(
        DiagramEditor.class.getMethod("setScaling", Scaling.class),
          Scaling.SCALING_100));
      selectorMap.put("ZOOM_150", new MethodCall(
        DiagramEditor.class.getMethod("setScaling", Scaling.class),
          Scaling.SCALING_150));
      selectorMap.put("BRING_TO_FRONT", new MethodCall(
        DiagramEditor.class.getMethod("bringToFront")));
      selectorMap.put("PUT_TO_BACK", new MethodCall(
        DiagramEditor.class.getMethod("putToBack")));
      selectorMap.put("EDIT_PROPERTIES", new MethodCall(
        DiagramEditor.class.getMethod("editProperties")));

      selectorMap.put("CREATE_PACKAGE", new MethodCall(
        DiagramEditor.class.getMethod("setCreationMode", ElementType.class),
          ElementType.PACKAGE));
      selectorMap.put("CREATE_COMPONENT", new MethodCall(
        DiagramEditor.class.getMethod("setCreationMode", ElementType.class),
          ElementType.COMPONENT));
      selectorMap.put("CREATE_CLASS", new MethodCall(
        DiagramEditor.class.getMethod("setCreationMode", ElementType.class),
          ElementType.CLASS));
      selectorMap.put("CREATE_NOTE", new MethodCall(
        DiagramEditor.class.getMethod("setCreationMode", ElementType.class),
          ElementType.NOTE));
      selectorMap.put("CREATE_DEPENDENCY", new MethodCall(
        DiagramEditor.class.getMethod("setCreateConnectionMode",
        RelationType.class), RelationType.DEPENDENCY));
      selectorMap.put("CREATE_NOTE_CONNECTION", new MethodCall(
        DiagramEditor.class.getMethod("setCreateConnectionMode",
        RelationType.class), RelationType.NOTE_CONNECTOR));
      selectorMap.put("CREATE_ASSOCIATION", new MethodCall(
        DiagramEditor.class.getMethod("setCreateConnectionMode",
        RelationType.class), RelationType.ASSOCIATION));
      selectorMap.put("CREATE_COMPOSITION", new MethodCall(
        DiagramEditor.class.getMethod("setCreateConnectionMode",
        RelationType.class), RelationType.COMPOSITION));
      selectorMap.put("CREATE_AGGREGATION", new MethodCall(
        DiagramEditor.class.getMethod("setCreateConnectionMode",
        RelationType.class), RelationType.AGGREGATION));
      selectorMap.put("CREATE_INHERITANCE", new MethodCall(
        DiagramEditor.class.getMethod("setCreateConnectionMode",
        RelationType.class), RelationType.INHERITANCE));
      selectorMap.put("CREATE_INTERFACE_REALIZATION", new MethodCall(
        DiagramEditor.class.getMethod("setCreateConnectionMode",
        RelationType.class), RelationType.INTERFACE_REALIZATION));
      selectorMap.put("RESET_POINTS", new MethodCall(
        DiagramEditor.class.getMethod("resetConnectionPoints")));
      selectorMap.put("RECT_TO_DIRECT", new MethodCall(
        DiagramEditor.class.getMethod("rectilinearToDirect")));
      selectorMap.put("DIRECT_TO_RECT", new MethodCall(
        DiagramEditor.class.getMethod("directToRectilinear")));
      selectorMap.put("NAVIGABLE_TO_SOURCE", new MethodCall(
        DiagramEditor.class.getMethod("setNavigability", RelationEndType.class),
        RelationEndType.SOURCE));
      selectorMap.put("NAVIGABLE_TO_TARGET", new MethodCall(
        DiagramEditor.class.getMethod("setNavigability", RelationEndType.class),
        RelationEndType.TARGET));

      // Self-calls
      selectorMap.put("SHOW_GRID", new MethodCall(
        getClass().getMethod("showGrid")));
      selectorMap.put("SNAP_TO_GRID", new MethodCall(
        getClass().getMethod("snapToGrid")));
    } catch (NoSuchMethodException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Displays the grid depending on the selection state of the menu item.
   */
  public void showGrid() {
    getCurrentEditor().showGrid(
      getMenuManager().isSelected("SHOW_GRID"));
    getCurrentEditor().redraw();
  }

  /**
   * Activates snapping depending on the selection state of the menu item.
   */
  public void snapToGrid() {
    getCurrentEditor().snapToGrid(getMenuManager().isSelected("SNAP_TO_GRID"));
  }

  /**
   * {@inheritDoc}
   */
  public void handleCommand(String command) {
    MethodCall methodcall = selectorMap.get(command);
    if (methodcall != null) {
      Object target = getCurrentEditor();
      // in order to catch the self calling methods
      if (methodcall.getMethod().getDeclaringClass()
        == EditorCommandDispatcher.class) {
        target = this;
      }
      methodcall.call(target);
    } else {
      System.out.println("not handled: " + command);
    }
  }

  /**
   * Returns the currently selected editor.
   * @return the current editor
   */
  private DiagramEditor getCurrentEditor() {
    return frame.getCurrentEditor();
  }

  /**
   * Returns the application's menu manager.
   * @return the menu manager
   */
  private MenuManager getMenuManager() {
    return frame.getMenuManager();
  }
}
