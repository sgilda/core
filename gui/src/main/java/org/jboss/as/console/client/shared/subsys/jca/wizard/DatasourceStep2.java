/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jboss.as.console.client.shared.subsys.jca.wizard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.shared.subsys.jca.model.JDBCDriver;
import org.jboss.as.console.client.widgets.ContentDescription;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormValidation;
import org.jboss.ballroom.client.widgets.forms.NumberBoxItem;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tables.DefaultPager;
import org.jboss.ballroom.client.widgets.window.DialogueOptions;
import org.jboss.ballroom.client.widgets.window.WindowContentBuilder;

import java.util.List;

/**
 * @author Heiko Braun
 */
public class DatasourceStep2 {

    private final NewDatasourceWizard wizard;
    private Form<JDBCDriver> form;
    private DriverNameItem name;
    private DriverModuleNameItem moduleName;
    private TextBoxItem driverClass;
    private NumberBoxItem major;
    private NumberBoxItem minor;
    private SingleSelectionModel<JDBCDriver> selectionModel;
    private CellTable<JDBCDriver> table;
    private boolean isStandalone;
    private int selectedTab;

    public DatasourceStep2(NewDatasourceWizard wizard) {
        this.wizard = wizard;
        this.isStandalone = wizard.getBootstrap().isStandalone();
    }

    @SuppressWarnings("unchecked")
    Widget asWidget() {
        VerticalPanel layout = new VerticalPanel();
        layout.getElement().setAttribute("style", "margin:10px; vertical-align:center;width:95%");

        HTML desc = new HTML("<h3>" + Console.CONSTANTS.subsys_jca_dataSource_step2() + "</h3>");
        desc.getElement().setAttribute("style", "padding-bottom:10px;");

        layout.add(desc);
        layout.add(new ContentDescription("Select one of the installed JDBC driver. Don't see your driver? Please make sure it's deployed as a module and properly registered."));

        // -- First tab: Define new JDBC Driver
        form = new Form<JDBCDriver>(JDBCDriver.class);
        name = new DriverNameItem(wizard.getDrivers());
        moduleName = new DriverModuleNameItem(wizard.getDrivers());
        driverClass = new TextBoxItem("driverClass", "Driver Class", false);
        major = new NumberBoxItem("majorVersion", "Major Version") {
            {
                setRequired(false);
            }
        };
        minor = new NumberBoxItem("minorVersion", "Minor Version") {
            {
                setRequired(false);
            }
        };
        form.setFields(name, moduleName, driverClass, major, minor);

        // -- Second tab: Select existing JDBC driver
        table = new DefaultCellTable<JDBCDriver>(5);

        TextColumn<JDBCDriver> nameColumn = new TextColumn<JDBCDriver>() {
            @Override
            public String getValue(JDBCDriver record) {
                return record.getName();
            }
        };
        TextColumn<JDBCDriver> moduleColumn = new TextColumn<JDBCDriver>() {
            @Override
            public String getValue(JDBCDriver record) {
                return record.getDriverModuleName();
            }
        };

        table.addColumn(nameColumn, "Name");
        if (!isStandalone) {
            table.addColumn(moduleColumn, "Module");
        }

        selectionModel = new SingleSelectionModel<JDBCDriver>();
        table.setSelectionModel(selectionModel);
        provisionTable(table);

        DefaultPager pager = new DefaultPager();
        pager.setDisplay(table);

        VerticalPanel driverPanel = new VerticalPanel();
        driverPanel.add(table);
        driverPanel.add(pager);

        // --
        TabPanel tabs = new TabPanel();
        tabs.setStyleName("default-tabpanel");
        tabs.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                selectedTab = event.getSelectedItem();
            }
        });

        tabs.add(form.asWidget(), "Specify Driver");
        tabs.add(driverPanel, "Detected Driver");

        layout.add(tabs);
        tabs.selectTab(0);

        // --
        ClickHandler submitHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                JDBCDriver driver = null;
                if (selectedTab == 0) {
                    FormValidation formValidation = form.validate();
                    if (!formValidation.hasErrors()) {
                        driver = form.getUpdatedEntity();
                    }
                } else {
                    // clear state
                    form.clearValues();
                    SingleSelectionModel<JDBCDriver> selection =
                            (SingleSelectionModel<JDBCDriver>) table.getSelectionModel();
                    driver = selection.getSelectedObject();
                }

                if (driver != null) { // force selected driver
                    wizard.onConfigureDriver(driver);
                } else {
                    Console.warning("A driver needs to be specified or chosen!");
                }
            }
        };

        ClickHandler cancelHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                wizard.getPresenter().closeDialogue();
            }
        };
        DialogueOptions options = new DialogueOptions(
                Console.CONSTANTS.common_label_next(), submitHandler,
                Console.CONSTANTS.common_label_cancel(), cancelHandler
        );
        return new WindowContentBuilder(layout, options).build();
    }

    private void provisionTable(final CellTable<JDBCDriver> table) {
        List<JDBCDriver> drivers = wizard.getDrivers();
        table.setRowCount(drivers.size(), true);
        table.setRowData(drivers);

        // clear selection
        JDBCDriver selectedDriver = selectionModel.getSelectedObject();
        if (selectedDriver != null) {
            selectionModel.setSelected(selectedDriver, false);
        }

        // new default selection
        if (drivers.size() > 0) {
            selectionModel.setSelected(drivers.get(0), true);
        }
    }

    void edit(JDBCDriver driver) {
        form.edit(driver);
        name.setModified(true);
        moduleName.setModified(true);
        driverClass.setModified(true);
        major.setModified(true);
        minor.setModified(true);
        selectionModel.clear();
    }
}
