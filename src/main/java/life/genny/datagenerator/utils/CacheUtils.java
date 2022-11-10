package life.genny.datagenerator.utils;

import life.genny.datagenerator.configs.InfinispanCatalogueConfig;
import life.genny.datagenerator.data.schemas.MessageEntity;
import life.genny.datagenerator.data.schemas.MessageKey;
import org.apache.commons.io.IOUtils;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.api.CacheContainerAdmin;
import org.infinispan.commons.configuration.XMLStringConfiguration;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class CacheUtils {

    private static final Logger LOGGER = Logger.getLogger(CacheUtils.class);

    @Inject
    private RemoteCacheManager cacheManager;

    @Inject
    private InfinispanCatalogueConfig infinispanConfig;

    private URL tableStoreCacheConfig;

    public void setTableStoreCacheConfig(URL tableStoreCacheConfig) {
        this.tableStoreCacheConfig = tableStoreCacheConfig;
    }

    public RemoteCache<String, String> createCache(String cacheName, String table, Class<? extends MessageKey> key, String message) {
        RemoteCache<String, String> entityCache = null;

        try {
            String cacheConfig = replaceDBConnectionConfiguration(tableStoreCacheConfig, table, key, message, message + "_" + cacheName);
            entityCache = cacheManager.administration().withFlags(CacheContainerAdmin.AdminFlag.VOLATILE)
                    .getOrCreateCache(cacheName, new XMLStringConfiguration(cacheConfig));
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }

        return entityCache;
    }

    private String replaceDBConnectionConfiguration(URL cacheConfig, String table, Class<? extends MessageKey> key, String message, String indexPath) throws IOException {
        String config = IOUtils.toString(cacheConfig, StandardCharsets.UTF_8)
                .replace("TABLE_NAME", table)
                .replace("MESSAGE_KEY", key.getSimpleName())
                .replace("MESSAGE_NAME", message)
                .replace("PACKAGE", infinispanConfig.packageName())
                .replace("CONNECTION_URL", infinispanConfig.connectionUrl())
                .replace("USERNAME", infinispanConfig.username())
                .replace("PASSWORD", infinispanConfig.password())
                .replace("DIALECT", infinispanConfig.dialect())
                .replace("DRIVER", infinispanConfig.driver())
                .replace("INDEXED_PATH", indexPath);
        LOGGER.debug(config);
        return config;
    }

    private RemoteCache<MessageKey, MessageEntity> findCache(String cacheName) {
        RemoteCache<MessageKey, MessageEntity> cache = cacheManager.getCache(cacheName);
        if (cache == null) throw new NullPointerException("Could not find a cache called " + cacheName);
        return cache;
    }

    public MessageEntity getEntityFromCache(String cacheName, MessageKey key) {
        RemoteCache<MessageKey, MessageEntity> cache = findCache(cacheName);
        MessageEntity entity = cache.get(key);
        if (entity == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return getEntityFromCache(cacheName, key);
        }
        return entity;
    }

    public boolean putEntityIntoCache(String cacheName, MessageKey key, MessageEntity value) {
        RemoteCache<MessageKey, MessageEntity> cache = findCache(cacheName);

        try {
            if (value != null) {
                cache.put(key, value);
                Thread.sleep(20);
            } else {
                LOGGER.warn("[" + cacheName + "]: Value for " + key.getKeyString() + " is null, nothing to be added.");
            }
        } catch (Exception e) {
            LOGGER.error("Exception when inserting entity into cache: " + e.getMessage());
            LOGGER.error(e.getStackTrace());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean putEntitiesIntoCache(String cacheName, Map<MessageKey, MessageEntity> entities) {
        RemoteCache<MessageKey, MessageEntity> cache = findCache(cacheName);

        try {
            if (entities.size() > 0) {
                cache.putAll(entities);
                Thread.sleep(20);
            } else {
                LOGGER.warn("[" + cacheName + "]: entities size is " + entities.size() + ", nothing to be added.");
            }
        } catch (Exception e) {
            LOGGER.error("Exception when inserting entity into cache: " + e.getMessage());
            LOGGER.error(e.getStackTrace());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
