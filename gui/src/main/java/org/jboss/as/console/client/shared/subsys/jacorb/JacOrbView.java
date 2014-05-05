package org.jboss.as.console.client.shared.subsys.jacorb;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import com.google.inject.Inject;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.shared.subsys.jacorb.model.JacOrbSubsystem;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.EmbeddedPropertyView;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.FrameworkButton;
import org.jboss.as.console.client.shared.viewframework.FrameworkPresenter;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.shared.viewframework.SingleEntityToDmrBridgeImpl;
import org.jboss.as.console.client.shared.viewframework.SingleEntityView;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.dmr.client.dispatch.DispatchAsync;

public class JacOrbView extends AbstractEntityView<JacOrbSubsystem> implements JacOrbPresenter.MyView, FrameworkView {
    private final EntityToDmrBridge<JacOrbSubsystem> bridge;

    @Inject
    public JacOrbView(ApplicationMetaData applicationMetaData, DispatchAsync dispatcher) {
        super(JacOrbSubsystem.class, applicationMetaData, EnumSet.of(FrameworkButton.ADD, FrameworkButton.REMOVE));
        bridge = new SingleEntityToDmrBridgeImpl<JacOrbSubsystem>(applicationMetaData, JacOrbSubsystem.class, this, dispatcher);
        setDescription(Console.CONSTANTS.subsys_jacorb_desc());
    }

    @Override
    public EntityToDmrBridge<JacOrbSubsystem> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<JacOrbSubsystem> makeEntityTable() {
        DefaultCellTable<JacOrbSubsystem> table = new DefaultCellTable<JacOrbSubsystem>(5);
        table.setVisible(false);
        return table;
    }

    @Override
    public void refresh() {
        super.refresh();
        String lastEdited = bridge.getNameOfLastEdited();
        if (lastEdited != null && entityDetails != null) {
            JacOrbSubsystem entity = bridge.findEntity(lastEdited);
            if (entityDetails != null) {
                entityDetails.updatedEntity(entity);
            }
        }
    }

    @Override
    protected FormAdapter<JacOrbSubsystem> makeAddEntityForm() {
        return new Form<JacOrbSubsystem>(beanType);
    }

    @Override
    protected List<SingleEntityView<JacOrbSubsystem>> provideAdditionalTabs(Class<?> beanType, FormMetaData formMetaData,
        FrameworkPresenter presenter) {
        EmbeddedPropertyView<JacOrbSubsystem, JacOrbSubsystem> propertyView = new EmbeddedPropertyView<JacOrbSubsystem, JacOrbSubsystem>(presenter);
        return Collections.<SingleEntityView<JacOrbSubsystem>>singletonList(propertyView);
    }

    @Override
    protected String getEntityDisplayName() {
        return "JacORB";
    }
}
