package org.jboss.as.console.client.shared.subsys.undertow;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.NameTokens;
import org.jboss.as.console.client.layout.MultipleToOneLayout;
import org.jboss.as.console.client.shared.subsys.messaging.MessagingProviderEditor;
import org.jboss.as.console.client.widgets.tables.ViewLinkCell;
import org.jboss.as.console.mbui.dmr.ResourceAddress;
import org.jboss.as.console.mbui.dmr.ResourceDefinition;
import org.jboss.as.console.mbui.widgets.ModelDrivenWidget;
import org.jboss.as.console.mbui.widgets.ModelNodeFormBuilder;
import org.jboss.ballroom.client.rbac.SecurityContext;
import org.jboss.ballroom.client.widgets.forms.FormCallback;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tools.ToolButton;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;
import org.jboss.ballroom.client.widgets.window.Feedback;
import org.jboss.dmr.client.Property;

import java.util.List;
import java.util.Map;

/**
 * @author Heiko Braun
 * @date 1/17/12
 */
public class ContainerList extends ModelDrivenWidget {

    private static final String RESOURCE_ADDRESS = "{selected.profile}/subsystem=undertow/servlet-container=*";
    private ServletPresenter presenter;
    private DefaultCellTable table;
    private ListDataProvider<Property> dataProvider;
    private MessagingProviderEditor providerEditor;

    public ContainerList(ServletPresenter presenter) {
        super(RESOURCE_ADDRESS);
        this.presenter = presenter;
        this.table = new DefaultCellTable(5);
        this.dataProvider = new ListDataProvider<Property>();
        this.dataProvider.addDataDisplay(table);
        this.table.setSelectionModel(new SingleSelectionModel<Property>());
    }

    @Override
    public Widget buildWidget(ResourceAddress address, ResourceDefinition definition) {

        TextColumn<Property> nameColumn = new TextColumn<Property>() {
            @Override
            public String getValue(Property node) {
                return node.getName();
            }
        };

        Column<Property, String> option = new Column<Property, String>(
                new ViewLinkCell<String>(Console.CONSTANTS.common_label_view(), new ActionCell.Delegate<String>() {
                    @Override
                    public void execute(String selection) {
                        presenter.getPlaceManager().revealPlace(
                                new PlaceRequest(presenter.getProxy().getNameToken()).with("name", selection)
                        );
                    }
                })
        ) {
            @Override
            public String getValue(Property node) {
                return node.getName();
            }
        };

        table.addColumn(nameColumn, "Name");
        table.addColumn(option, "Option");

        ToolStrip tools = new ToolStrip();
        tools.addToolButtonRight(new ToolButton(Console.CONSTANTS.common_label_add(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO
            }
        }));
        tools.addToolButtonRight(new ToolButton(Console.CONSTANTS.common_label_delete(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Feedback.confirm(Console.MESSAGES.deleteTitle("Container"),
                        Console.MESSAGES.deleteConfirm("Container '" + getCurrentSelection().getName() + "'"),
                        new Feedback.ConfirmationHandler() {
                            @Override
                            public void onConfirmation(boolean isConfirmed) {
                                if (isConfirmed) {
                                    // TODO
                                }
                            }
                        });
            }
        }));

        SecurityContext securityContext = Console.MODULES.getSecurityFramework().getSecurityContext(presenter.getProxy().getNameToken());

        final ModelNodeFormBuilder.FormAssets formAssets = new ModelNodeFormBuilder()
                .setConfigOnly()
                .setResourceDescription(definition)
                .setSecurityContext(securityContext).build();


        formAssets.getForm().setToolsCallback(new FormCallback() {
            @Override
            public void onSave(Map changeset) {
                presenter.onSaveResource(
                        RESOURCE_ADDRESS, getCurrentSelection().getName(), changeset
                );
            }

            @Override
            public void onCancel(Object entity) {
                formAssets.getForm().cancel();
            }
        });

        VerticalPanel formPanel = new VerticalPanel();
        formPanel.setStyleName("fill-layout-width");
        formPanel.add(formAssets.getHelp().asWidget());
        formPanel.add(formAssets.getForm().asWidget());

        // ----
        MultipleToOneLayout layoutBuilder = new MultipleToOneLayout()
                .setPlain(true)
                .setHeadline("Servlet Container")
                .setDescription("Please chose a container below for further settings.")
                        //.setMasterTools(tools) // TODO: implement add/remove ops
                .setMaster(Console.MESSAGES.available("Servlet Container"), table)
                .addDetail("Attributes", formPanel);


        final SingleSelectionModel<Property> selectionModel = new SingleSelectionModel<Property>();
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Property server = selectionModel.getSelectedObject();
                if(server!=null)
                {
                    formAssets.getForm().edit(server.getValue());
                }
                else
                {
                    formAssets.getForm().clearValues();
                }
            }
        });
        table.setSelectionModel(selectionModel);
        return layoutBuilder.build();
    }


    private Property getCurrentSelection() {
        Property selection = ((SingleSelectionModel<Property>) table.getSelectionModel()).getSelectedObject();
        return selection;
    }

    public void setServer(List<Property> provider) {
        dataProvider.setList(provider);
        table.selectDefaultEntity();

    }
}
