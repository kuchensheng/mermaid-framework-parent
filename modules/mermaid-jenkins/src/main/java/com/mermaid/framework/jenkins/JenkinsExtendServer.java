package com.mermaid.framework.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpConnection;

import java.net.URI;


public class JenkinsExtendServer extends JenkinsServer {

    public JenkinsExtendServer(URI serverUri) {
        super(serverUri);
    }

    public JenkinsExtendServer(URI serverUri, String username, String passwordOrToken) {
        super(serverUri, username, passwordOrToken);
    }

    public JenkinsExtendServer(JenkinsHttpConnection client) {
        super(client);
    }
}
