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

package org.jboss.as.console.client.core;

/**
 * @author Heiko Braun
 * @date 2/4/11
 */
public class NameTokens {

    public static final String mainLayout = "main";

    public static final String Batch = "batch";
    public static final String IO = "io";
    public static final String HomepagePresenter = "home";
    public static final String BoundedQueueThreadPoolPresenter = "threads";
    public static final String ConfigAdminPresenter = "configadmin";
    public static final String DataSourcePresenter = "datasources";
    public static final String DebugToolsPresenter = "debug-tools";
    public static final String DistributedCachePresenter = "distributed-cache";
    public static final String EJB3Presenter = "ejb3";
    public static final String HostInterfacesPresenter = "host-interfaces";
    public static final String HostJVMPresenter = "host-jvms";
    public static final String HostPropertiesPresenter = "host-properties";
    public static final String Infinispan = "infinispan";
    public static final String InterfacePresenter = "interfaces";
    public static final String InvalidationCachePresenter = "invalidation-cache";
    public static final String CacheContainerPresenter = "infinispan";
    public static final String JacOrbPresenter = "jacorb";
    public static final String JMSPresenter = "jms";
    public static final String JndiPresenter = "naming";
    public static final String LocalCachePresenter = "local-cache";
    public static final String Logger = "logging";
    public static final String LogViewer = "logviewer";
    public static final String LogFiles = "logfiles";
    public static final String LogHandler = "log-handler";
    public static final String ScannerPresenter = "deployment-scanner";
    public static final String MessagingPresenter = "messaging";
    public static final String MetricsPresenter = "invocation-metrics";
    public static final String ModelBrowserPresenter = "model-browser";
    public static final String OSGiConfigurationPresenter = "osgi-configuration";
    public static final String OSGiRuntimePresenter = "osgi-runtime";
    public static final String PropertiesPresenter = "properties";
    public static final String ResourceAdapterPresenter ="resource-adapters";
    public static final String Remoting = "remoting";
    public static final String ReplicatedCachePresenter = "replicated-cache";
    public static final String SecurityDomainsPresenter = "security-domains";
    public static final String SecuritySubsystemPresenter = "security";
    public static final String SettingsPresenter = "settings";
    public static final String SocketBindingPresenter = "socket-bindings";
    public static final String StandaloneServerPresenter = "server-overview";
    public static final String WebPresenter = "web";
    public static final String WebServicePresenter = "webservices";
    public static final String VirtualMachine = "vm";
    public static final String HostVMMetricPresenter = "host-vm";
    public static final String TransactionPresenter = "transactions";
    public static final String StandaloneRuntimePresenter = "standalone-runtime";
    public static final String DomainRuntimePresenter = "domain-runtime";
    public static final String PatchingPresenter = "patching";

    public static final String TXMetrics = "tx-metrics";
    public static final String TXLogs = "tx-logs";
    public static final String JpaPresenter = "jpa";
    public static final String MailPresenter = "mail";
    public static final String JMXPresenter = "jmx";
    public static final String EEPresenter = "ee";
    public static final String JcaPresenter = "jca";
    public static final String WebMetricPresenter = "web-metrics";
    public static final String JmsMetricPresenter = "jms-metrics";
    public static final String DataSourceMetricPresenter  = "ds-metrics";
    public static final String EnvironmentPresenter = "environment";
    public static final String ExtensionsPresenter = "extension";
    public static final String JPAMetricPresenter = "jpa-metrics";
    public static final String WebServiceRuntimePresenter = "webservice-runtime";
    public static final String JGroupsPresenter = "jgroups";
    public static final String ModclusterPresenter = "modcluster";
    public static final String MsgConnectionsPresenter = "messaging-connections";
    public static final String MsgClusteringPresenter  =  "messaging-cluster";
    public static final String DMRBrowser = "browser";
    public static final String ToolsPresenter = "tools";
    public static final String PathManagementPresenter = "path" ;
    public static final String UndertowHTTP = "undertow-http";
    public static final String UndertowServlet = "undertow-servlet";
    public static final String UndertowCore = "undertow-core";
    public static final String DialogPresenter = "mbui";
    public static final String DomainPresenter = "domain";
    public static final String NoServer = "no-server";
    public static final String CSP = "csp";
    public static final String HttpPresenter = "http";
    public static final String ServletPresenter = "servlet";
    public static final String  UndertowPresenter = "undertow";
    public static final String MailFinder = "mail-sessions";
    public static final String HttpMetrics = "http-metrics";

    public static String getMainLayout() {
        return mainLayout;
    }

    public static final String signInPage = "login";
    public static String getSignInPage() {
        return signInPage;
    }

    public static final String errorPage = "err";
    public static String getErrorPage() {
        return errorPage;
    }

    public static final String ServerProfile = "profile";
    public static String getServerProfile() {
        return ServerProfile;
    }

    public static final String StandloneDeployments = "server-deployments";
    public static String getStandloneDeployments() {
        return StandloneDeployments;
    }

    public static final String systemApp = "system";
    public static String getSystemApp() {
        return systemApp;
    }

    public final static String InterfaceToolPresenter = "server-interfaces";
    public static String getInterfaceToolPresenter() {
        return InterfaceToolPresenter;
    }

    public final static String PathToolPresenter = "server-paths";
    public static String getPathToolPresenter() {
        return PathToolPresenter;
    }

    public final static String SubsystemToolPresenter = "subsys";
    public static String getSubsystemToolPresenter() {
        return SubsystemToolPresenter;
    }

    public static final String ThreadManagementPresenter = "threading";
    public static String getThreadManagementPresenter() {
        return ThreadManagementPresenter;
    }


    // ------------------------------------------------------
    // domain tokens below

    public static final String ProfileMgmtPresenter = "profiles";
    public static String getProfileMgmtPresenter() {
        return ProfileMgmtPresenter;
    }

    public static final String Topology = "topology";
    public static String getTopology() {
        return Topology;
    }

    public static final String ServerGroupPresenter = "server-groups";
    public static String getServerGroupPresenter() {
        return ServerGroupPresenter;
    }

    public static final String DeploymentsPresenter  = "domain-deployments";
    public static String getDeploymentsPresenter() {
        return DeploymentsPresenter;
    }

    public static final String HostMgmtPresenter = "hosts";
    public static String getHostMgmtPresenter() {
        return HostMgmtPresenter;
    }

    public final static String ServerPresenter = "server-config";
    public static String getServerPresenter() {
        return ServerPresenter;
    }

    public static final String DeploymentBrowserPresenter = "deployments";
    public static String getDeploymentBrowserPresenter() {
        return DeploymentBrowserPresenter;
    }


    // ------------------------------------------------------
    // administration tokens below

    public static final String AdministrationPresenter = "administration";
    public static String getAdministrationPresenter() {
        return AdministrationPresenter;
    }

    public static final String RoleAssignmentPresenter = "role-assignment";
    public static String getRoleAssignmentPresenter() {
        return RoleAssignmentPresenter;
    }

    public static final String AuditLogPresenter = "audit-log";
    public static String getAuditLogPresenter() {
        return AuditLogPresenter;
    }
}

