package com.gsapps.reminders.services;

import android.util.Log;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.core.IClientConfig;
import com.microsoft.graph.extensions.GraphServiceClient;
import com.microsoft.graph.extensions.IGraphServiceClient;
import com.microsoft.graph.http.IHttpRequest;

import static com.gsapps.reminders.listeners.MSAuthCallbackListener.getAccessToken;
import static com.microsoft.graph.core.DefaultClientConfig.createWithAuthenticationProvider;
import static com.microsoft.graph.logger.LoggerLevel.Debug;

public class GraphServiceClientManager implements IAuthenticationProvider {
    private IGraphServiceClient mGraphServiceClient;
    private static GraphServiceClientManager INSTANCE;

    @Override
    public void authenticateRequest(IHttpRequest request)  {
        request.addHeader("Authorization", "Bearer " + getAccessToken());
        Log.i("Connect", "Request: " + request.toString());
    }

    public static synchronized GraphServiceClientManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GraphServiceClientManager();
        }
        return INSTANCE;
    }

    public synchronized IGraphServiceClient getGraphServiceClient() {
        return getGraphServiceClient(this);
    }

    private synchronized IGraphServiceClient getGraphServiceClient(IAuthenticationProvider authenticationProvider) {
        if (mGraphServiceClient == null) {
            IClientConfig clientConfig = createWithAuthenticationProvider(authenticationProvider);
            mGraphServiceClient = new GraphServiceClient.Builder().fromConfig(clientConfig).buildClient();
            mGraphServiceClient.getLogger().setLoggingLevel(Debug);
        }

        return mGraphServiceClient;
    }
}
