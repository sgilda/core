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

package org.jboss.as.console.client.domain.hosts;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.BootstrapContext;
import org.jboss.as.console.client.core.Header;
import org.jboss.as.console.client.core.MainLayoutPresenter;
import org.jboss.as.console.client.core.NameTokens;
import org.jboss.as.console.client.domain.model.Host;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.rbac.HostManagementGatekeeper;
import org.jboss.as.console.client.rbac.UnauthorisedPresenter;
import org.jboss.as.console.client.shared.state.DomainEntityManager;
import org.jboss.as.console.client.shared.state.HostList;
import org.jboss.as.console.client.shared.state.PerspectivePresenter;
import org.jboss.as.console.client.v3.stores.domain.HostStore;
import org.jboss.as.console.client.v3.stores.domain.actions.RefreshHosts;
import org.jboss.ballroom.client.layout.LHSHighlightEvent;
import org.jboss.gwt.circuit.Dispatcher;
import org.jboss.gwt.circuit.PropagatesChange;

import java.util.List;

/**
 * @author Heiko Braun
 */
public class HostMgmtPresenter extends PerspectivePresenter<HostMgmtPresenter.MyView, HostMgmtPresenter.MyProxy> {

    @ContentSlot
    public static final GwtEvent.Type<RevealContentHandler<?>> TYPE_MainContent = new GwtEvent.Type<RevealContentHandler<?>>();
    private final Dispatcher circuit;

    private BootstrapContext bootstrap;
    private final HostStore hostStore;

    @ProxyCodeSplit
    @NameToken(NameTokens.HostMgmtPresenter)
    @UseGatekeeper(HostManagementGatekeeper.class)
    public interface MyProxy extends Proxy<HostMgmtPresenter>, Place {
    }

    public interface MyView extends View {
        void setPresenter(HostMgmtPresenter presenter);

        void updateHosts(String selectedHost, List<Host> hostModel);
    }

    @Inject
    public HostMgmtPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager,
            BootstrapContext bootstrap, Header header, HostStore hostStore, Dispatcher circuit,
            UnauthorisedPresenter unauthorisedPresenter) {

        super(eventBus, view, proxy, placeManager, header, NameTokens.HostMgmtPresenter, unauthorisedPresenter,
                TYPE_MainContent);

        this.bootstrap = bootstrap;
        this.hostStore = hostStore;
        this.circuit = circuit;
    }

    @Override
    protected void onBind() {
        super.onBind();
        getView().setPresenter(this);
        hostStore.addChangeHandler(new PropagatesChange.Handler() {
            @Override
            public void onChange(Class<?> source) {
                getView().updateHosts(hostStore.getSelectedHost(), hostStore.getHostModel());
            }
        });
    }

    @Override
    protected void onReset() {
        clearInitialPlace();
        circuit.dispatch(new RefreshHosts());
        HostMgmtPresenter.super.onReset();
    }

    @Override
    protected void onFirstReveal(final PlaceRequest placeRequest, PlaceManager placeManager, boolean revealDefault) {
        if(revealDefault)
        {
            placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.ServerPresenter).build());
        }
    }

    private void clearInitialPlace() {
        if (bootstrap.getInitialPlace() != null) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    Console.getEventBus().fireEvent(
                            new LHSHighlightEvent(bootstrap.getInitialPlace())
                    );
                    bootstrap.setInitialPlace(null);
                }
            });
        }
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainLayoutPresenter.TYPE_MainContent, this);
    }
}
