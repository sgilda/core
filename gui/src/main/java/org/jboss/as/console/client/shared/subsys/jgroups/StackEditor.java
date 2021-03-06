package org.jboss.as.console.client.shared.subsys.jgroups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.layout.FormLayout;
import org.jboss.as.console.client.layout.MultipleToOneLayout;
import org.jboss.as.console.client.shared.help.FormHelpPanel;
import org.jboss.as.console.client.shared.properties.PropertyEditor;
import org.jboss.as.console.client.shared.properties.PropertyRecord;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.widgets.forms.FormToolStrip;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;
import org.jboss.ballroom.client.widgets.forms.TextItem;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tools.ToolButton;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;
import org.jboss.ballroom.client.widgets.window.Feedback;
import org.jboss.dmr.client.ModelNode;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Heiko Braun
 * @date 2/16/12
 */
public class StackEditor {

    private JGroupsPresenter presenter  ;

    private Form<JGroupsProtocol> form;
    private ListDataProvider<JGroupsProtocol> dataProvider;
    private DefaultCellTable<JGroupsProtocol> table ;
    private HTML headline;
    private PropertyEditor propertyEditor;
    private JGroupsStack selectedStack;

    public StackEditor(JGroupsPresenter presenter) {
        this.presenter = presenter;
    }

    @SuppressWarnings("unchecked")
    Widget asWidget() {
        table = new DefaultCellTable<>(6, new ProvidesKey<JGroupsProtocol>() {
            @Override
            public Object getKey(JGroupsProtocol item) {
                return item.getName();
            }
        });
        dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(table);

        TextColumn<JGroupsProtocol> name = new TextColumn<JGroupsProtocol>() {
            @Override
            public String getValue(JGroupsProtocol record) {
                return record.getName();
            }
        };

        table.addColumn(name, "Name");

        ToolStrip toolstrip = new ToolStrip();

        ToolButton addBtn = new ToolButton(Console.CONSTANTS.common_label_append(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                presenter.launchNewProtocolWizard();
            }
        });
        toolstrip.addToolButtonRight(addBtn);

        ToolButton removeBtn = new ToolButton(Console.CONSTANTS.common_label_delete(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Feedback.confirm(
                        Console.MESSAGES.deleteTitle("Protocol"),
                        Console.MESSAGES.deleteConfirm("Protocol"),
                        new Feedback.ConfirmationHandler() {
                            @Override
                            public void onConfirmation(boolean isConfirmed) {
                                if (isConfirmed)
                                    presenter.onDeleteProtocol(form.getEditedEntity());
                            }
                        });
            }
        });

        toolstrip.addToolButtonRight(removeBtn);

        // ------

        this.propertyEditor = new PropertyEditor(presenter, true);

        // ------

        form = new Form<>(JGroupsProtocol.class);
        form.setNumColumns(2);

        TextItem nameField = new TextItem("name", "Name");
        TextBoxItem socket = new TextBoxItem("socketBinding", "Socket Binding");

        form.setFields(nameField, socket);
        form.setEnabled(false);


        FormHelpPanel helpPanel = new FormHelpPanel(new FormHelpPanel.AddressCallback() {
            @Override
            public ModelNode getAddress() {
                ModelNode address = Baseadress.get();
                address.add("subsystem", "jgroups");
                address.add("stack", "*");
                address.add("protocol", "*");
                return address;
            }
        }, form);

        FormToolStrip<JGroupsProtocol> formToolStrip = new FormToolStrip<>(
                form, new FormToolStrip.FormCallback<JGroupsProtocol>() {
            @Override
            public void onSave(Map<String, Object> changeset) {
                presenter.onSaveProtocol(form.getEditedEntity(), changeset);
            }

            @Override
            public void onDelete(JGroupsProtocol entity) {

            }
        });

        Widget detail = new FormLayout()
                .setForm(form)
                .setHelp(helpPanel)
                .setTools(formToolStrip).build();

        headline = new HTML();
        headline.setStyleName("content-header-label");

        Widget panel = new MultipleToOneLayout()
                .setPlain(true)
                .setTitle("JGroups")
                .setHeadlineWidget(headline)
                .setDescription(SafeHtmlUtils.fromTrustedString(Console.CONSTANTS.subsys_jgroups_protocol_desc()))
                .setMaster(Console.MESSAGES.available("Protocols"), table)
                .setMasterTools(toolstrip.asWidget())
                .addDetail("Attributes", detail)
                .addDetail("Properties", propertyEditor.asWidget())
                .build();

        form.bind(table);

        table.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                JGroupsProtocol currentSelection = getCurrentSelection();
                List<PropertyRecord> properties = currentSelection.getProperties();
                if(properties!=null)
                    propertyEditor.setProperties(selectedStack.getName() + "_#_" + currentSelection.getName(), properties);
                else
                    propertyEditor.setProperties(selectedStack.getName() + "_#_" + currentSelection.getName(), Collections.EMPTY_LIST);
            }
        });
        return panel;

    }

    @SuppressWarnings("unchecked")
    private JGroupsProtocol getCurrentSelection() {
        SingleSelectionModel<JGroupsProtocol> selectionModel = (SingleSelectionModel<JGroupsProtocol>) table.getSelectionModel();
        return selectionModel.getSelectedObject();

    }
    public void setStack(JGroupsStack stack) {
        this.selectedStack = stack;

        headline.setText("Protocols: Stack "+stack.getName());

        dataProvider.setList(stack.getProtocols());
        table.selectDefaultEntity();

    }

}
