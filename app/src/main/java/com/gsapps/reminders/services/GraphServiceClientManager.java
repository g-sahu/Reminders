package com.gsapps.reminders.services;

import android.util.Log;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;

import java.util.Optional;

import static com.microsoft.graph.logger.LoggerLevel.DEBUG;

public class GraphServiceClientManager implements IAuthenticationProvider {
    private IGraphServiceClient mGraphServiceClient;
    private static GraphServiceClientManager graphServiceClientManager;
    //private final MSAuthManager msAuthManager;

    @Override
    public void authenticateRequest(IHttpRequest request)  {
        //request.addHeader("Authorization", "Bearer " + msAuthManager.getAccessToken());
        Log.i("Connect", "Request: " + request);
    }

    static synchronized GraphServiceClientManager getGraphServiceClientManager() {
        return Optional.ofNullable(graphServiceClientManager)
                       .orElse(new GraphServiceClientManager());
    }

    synchronized IGraphServiceClient getGraphServiceClient() {
        return getGraphServiceClient(this);
    }

    private synchronized IGraphServiceClient getGraphServiceClient(IAuthenticationProvider authenticationProvider) {
        if (mGraphServiceClient == null) {
            mGraphServiceClient = GraphServiceClient.builder()
                                                    .authenticationProvider(authenticationProvider)
                                                    .buildClient();

            mGraphServiceClient.getLogger().setLoggingLevel(DEBUG);
        }

        return mGraphServiceClient;
    }
}
