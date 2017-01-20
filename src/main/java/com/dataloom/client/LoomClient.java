package com.dataloom.client;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dataloom.authorization.PermissionsApi;
import com.dataloom.client.RetrofitFactory.Environment;
import com.dataloom.client.serialization.SerializableSupplier;
import com.dataloom.data.DataApi;
import com.dataloom.edm.EdmApi;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import retrofit2.Retrofit;

public class LoomClient implements ApiFactoryFactory {

    private static final long                        serialVersionUID = -5757911484718872922L;

    private static final Logger                      logger           = LoggerFactory
            .getLogger( LoomClient.class );

    private transient ApiFactory                     restAdapter      = null;
    private transient LoadingCache<Class<?>, Object> apiCache         = null;
    private transient boolean                        initialized      = false;
    private final ApiFactoryFactory                  retrofitSupplier;

    public LoomClient( Environment environment, SerializableSupplier<String> jwtToken ) {
        this( () -> {
            final Retrofit retrofit = RetrofitFactory.newClient( environment, jwtToken );
            // ApiFactory f = (ApiFactory)retrofit::create;
            return (ApiFactory) clazz -> retrofit.create( clazz );
        } );
    }

    public LoomClient( SerializableSupplier<String> jwtToken ) {
        this( () -> {
            final Retrofit retrofit = RetrofitFactory.newClient( jwtToken );
            // ApiFactory f = (ApiFactory)retrofit::create;
            return (ApiFactory) clazz -> retrofit.create( clazz );
        } );
    }

    public LoomClient( ApiFactoryFactory retrofitSupplier ) {
        this.retrofitSupplier = retrofitSupplier;
        logger.info( "Loom client ready!" );
    }

    public DataApi getDataApi() throws ExecutionException {
        ensureInitialized();
        return (DataApi) apiCache.get( DataApi.class );
    }

    public PermissionsApi getPermissionsApi() throws ExecutionException {
        ensureInitialized();
        return (PermissionsApi) apiCache.get( PermissionsApi.class );
    }

    public EdmApi getEdmApi() throws ExecutionException {
        ensureInitialized();
        return (EdmApi) apiCache.get( EdmApi.class );
    }

    private void ensureInitialized() {

        // quick exit after initialization without any locking.
        if ( initialized ) {
            return;
        }

        synchronized ( this ) {

            // this handles threads that attempted, but weren't selected for initialization
            if ( initialized ) {
                return;
            }

            restAdapter = retrofitSupplier.get();
            apiCache = CacheBuilder.newBuilder()
                    .maximumSize( 100 )
                    .initialCapacity( 10 )
                    .build( new CacheLoader<Class<?>, Object>() {
                        @Override
                        public Object load( Class<?> key ) throws Exception {
                            return restAdapter.create( key );
                        }
                    } );

            initialized = true;
        }
    }

    public static SerializableSupplier<LoomClient> getSerializableSupplier( SerializableSupplier<String> jwtToken ) {
        return () -> new LoomClient( jwtToken );
    }

    public static SerializableSupplier<LoomClient> getSerializableSupplier(
            Environment environment,
            SerializableSupplier<String> jwtToken ) {
        return () -> new LoomClient( environment, jwtToken );
    }

    public ApiFactory get() {
        ensureInitialized();
        return (ApiFactory) clazz -> {
            try {
                return apiCache.get( clazz );
            } catch ( ExecutionException e ) {
                logger.error( "Unable to instantiate API: {} ", clazz.getCanonicalName() );
                throw new IllegalStateException( "Unable to instantiate API " + clazz.getCanonicalName() );
            }
        };
    }
}
