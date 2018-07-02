package com.mermaid.framework.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpConnection;

import java.net.URI;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/1 13:59
 */
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
