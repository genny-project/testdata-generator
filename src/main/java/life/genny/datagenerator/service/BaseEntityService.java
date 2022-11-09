package life.genny.datagenerator.service;

import life.genny.datagenerator.configs.MySQLCatalogueConfig;
import life.genny.datagenerator.data.schemas.*;
import life.genny.datagenerator.data.serialization.SchemaInitializerImpl;
import life.genny.datagenerator.model.BaseEntityAttributeModel;
import life.genny.datagenerator.model.BaseEntityModel;
import life.genny.datagenerator.utils.CacheUtils;
import life.genny.datagenerator.utils.DatabaseUtils;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.infinispan.commons.util.FileLookupFactory;
import org.infinispan.commons.util.Util;
import org.infinispan.protostream.SerializationContextInitializer;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

@ApplicationScoped
public class BaseEntityService {
    private static final Logger LOGGER = Logger.getLogger(BaseEntityService.class);
    private static final String HOTROD_CLIENT_PROPERTIES = "hotrod-client.properties";
    private static final String ATTRIBUTE_CACHE_NAME = "baseentity_attribute";
    private static final String BASEENTITY_CACHE_NAME = "baseentity";

    private final DatabaseUtils dbUtils = new DatabaseUtils();

    private RemoteCacheManager remoteCacheManager;
    private RemoteCache<String, String> beCache;
    private RemoteCache<String, String> beAttrCache;

    @Inject
    CacheUtils cacheUtils;

    @Inject
    private MySQLCatalogueConfig mysqlConfig;

    public void onStart () {
        onSchemaBuild();
        onCacheBuild();
    }

    private void onSchemaBuild() {
        LOGGER.debug("onSchemaBuild");
        initRemoteCacheManager();
    }

    private void onCacheBuild(){
        LOGGER.debug("onCacheBuild");
        URL tableStoreCacheConfig = CacheUtils.class.getClassLoader().getResource("META-INF/protobuf/tableStore.xml");
        cacheUtils.setTableStoreCacheConfig(tableStoreCacheConfig);
        beCache = getBECache();
        beAttrCache = getBEAttrCache();
    }

    private void initRemoteCacheManager() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        builder.classLoader(cl);

        // load infinispan properties
        InputStream stream = FileLookupFactory.newInstance().lookupFile(HOTROD_CLIENT_PROPERTIES, cl);

        if (stream == null) {
            LOGGER.error("Could not find infinispan hotrod client properties file: " + HOTROD_CLIENT_PROPERTIES);
            return;
        }

        try {
            builder.withProperties(loadFromStream(stream));
        } finally {
            Util.close(stream);
        }

        // create cache manager
        getAllSerializationContextInitializers().stream().forEach(builder::addContextInitializer);
        Configuration config = builder.build();
        remoteCacheManager = new RemoteCacheManager(config);
        remoteCacheManager.getConfiguration().marshallerClass();
    }

    private List<SerializationContextInitializer> getAllSerializationContextInitializers() {
        List<SerializationContextInitializer> serCtxInitList = new LinkedList<>();
        SerializationContextInitializer schemaInitializer = new SchemaInitializerImpl();
        serCtxInitList.add(schemaInitializer);
        return serCtxInitList;
    }

    private Properties loadFromStream(InputStream stream) {
        Properties properties = new Properties();
        try {
            properties.load(stream);
        } catch (Exception e) {
            throw new HotRodClientException("Issues configuring from client hotrod-client.properties", e);
        }
        return properties;
    }

    private RemoteCache<String, String> getBECache() {
        return cacheUtils.createCache(BASEENTITY_CACHE_NAME, "baseentity", BaseEntityKey.class, BaseEntity.class.getSimpleName());
    }

    private RemoteCache<String, String> getBEAttrCache() {
        return cacheUtils.createCache(ATTRIBUTE_CACHE_NAME, "baseentity_attribute", BaseEntityAttributeKey.class,
                BaseEntityAttribute.class.getSimpleName());
    }

    public int count() {
        int total = 1;
        try {
            int totalTemp = dbUtils.initConnection(mysqlConfig).getCountFromMysql("baseentity");
            if (totalTemp > 1) total = totalTemp;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return total;
    }

    public void save(BaseEntityModel model) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<BaseEntityAttributeModel> beAttrs = model.getAttributes();
        cacheUtils.putEntityIntoCache(BASEENTITY_CACHE_NAME, model.getMessageKey(), model.toEntity());
        for (int i =0; i < beAttrs.size(); i++) {
            BaseEntityAttributeModel beAttr = beAttrs.get(i);
            cacheUtils.putEntityIntoCache(ATTRIBUTE_CACHE_NAME, beAttr.getMessageKey(), beAttr.toEntity());
        }
    }

    public void saveAll(List<BaseEntityModel> models) {
        Map<MessageKey, MessageEntity> entities = new HashMap<>();
        Map<MessageKey, MessageEntity> entityAttributes = new HashMap<>();
        for (BaseEntityModel model: models) {
            entities.put(model.getMessageKey(), model.toEntity());
            for (BaseEntityAttributeModel attrModel: model.getAttributes())
                entityAttributes.put(attrModel.getMessageKey(), attrModel.toEntity());
        }

        cacheUtils.putEntitiesIntoCache(BASEENTITY_CACHE_NAME, entities);
        cacheUtils.putEntitiesIntoCache(ATTRIBUTE_CACHE_NAME, entityAttributes);
    }
}
