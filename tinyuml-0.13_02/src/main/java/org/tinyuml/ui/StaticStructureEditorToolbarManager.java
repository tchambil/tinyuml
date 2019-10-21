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

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import org.tinyuml.util.AppCommandListener;
import org.tinyuml.util.ApplicationResources;
import org.tinyuml.util.IconLoader;

/**
 * This class manages the a toolbar for a static structure dialog.
 *
 * @author Wei-ju Wu
 * @version 1.0
 */
public class StaticStructureEditorToolbarManager implements ActionListener {
  private JToolBar toolbar = new JToolBar();
  private List<AppCommandListener> listeners =
    new ArrayList<AppCommandListener>();
  private Map<String, AbstractButton> buttonMap =
    new HashMap<String, AbstractButton>();
  private ButtonGroup buttongroup;

  /**
   * Constructor.
   */
  public StaticStructureEditorToolbarManager() {
    buttongroup = new ButtonGroup();
    toolbar.add(createToggleButton(buttongroup, "select"));
    toolbar.add(createToggleButton(buttongroup, "package"));
    toolbar.add(createToggleButton(buttongroup, "class"));
    toolbar.add(createToggleButton(buttongroup, "component"));
    toolbar.addSeparator(new Dimension(10, 10));
    toolbar.add(createToggleButton(buttongroup, "dependency"));
    toolbar.add(createToggleButton(buttongroup, "association"));
    toolbar.add(createToggleButton(buttongroup, "aggregation"));
    toolbar.add(createToggleButton(buttongroup, "composition"));
    toolbar.add(createToggleButton(buttongroup, "inheritance"));
    toolbar.add(createToggleButton(buttongroup, "interfreal"));
    toolbar.addSeparator(new Dimension(10, 10));
    toolbar.add(createToggleButton(buttongroup, "note"));
    toolbar.add(createToggleButton(buttongroup, "noteconnector"));
    doClick("SELECT_MODE");
  }

  /**
   * Adds an AppCommandListener.
   * @param l the AppCommandListener to add
   */
  public void addCommandListener(AppCommandListener l) {
    listeners.add(l);
  }

  /**
   * Removes an AppCommandListener.
   * @param l the AppCommandListener to remove
   */
  public void removeCommandListener(AppCommandListener l) {
    listeners.remove(l);
  }

  /**
   * Returns the toolbar component.
   * @return the toolbar component
   */
  public JToolBar getToolbar() { return toolbar; }

  /**
   * {@inheritDoc}
   */
  public void actionPerformed(ActionEvent e) {
    for (AppCommandListener l : listeners) {
      l.handleCommand(e.getActionCommand());
    }
  }

  /**
   * Enables the specified button.
   * @param actionCommand the action command string that is tied to the button
   * @param flag true for enabling, false for disabling
   */
  public void setEnabled(String actionCommand, boolean flag) {
    buttonMap.get(actionCommand).setEnabled(flag);
  }

  /**
   * Selects the specified button.
   * @param actionCommand the action command string that is tied to the button
   */
  public void doClick(String actionCommand) {
    buttonMap.get(actionCommand).requestFocusInWindow();
    buttonMap.get(actionCommand).doClick();
  }

  /**
   * Creates the specified toggle button.
   * @param aButtonGroup an optional ButtonGroup to add to
   * @param name the toggle button name
   * @return the toggle button
   */
  private JToggleButton createToggleButton(ButtonGroup aButtonGroup,
    String name) {
    String prefix = "statictoolbar." + name;
    JToggleButton button = new JToggleButton(
      IconLoader.getInstance().getIcon(getResourceString(prefix + ".icon")));
    button.setMargin(new Insets(1, 1, 1, 1));
    String actionCommand = getResourceString(prefix + ".command");
    button.setActionCommand(actionCommand);
    button.addActionListener(this);
    toolbar.add(button);
    button.setToolTipText(getResourceString(prefix + ".tooltip"));
    buttonMap.put(actionCommand, button);
    if (aButtonGroup != null) {
      aButtonGroup.add(button);
    }
    return button;
  }

  /**
   * Returns the specified resource as a String object.
   * @param property the property name
   * @return the property value or null if not found
   */
  private String getResourceString(String property) {
    return ApplicationResources.getInstance().getString(property);
  }
}
