package life.genny.datagenerator.data;

public class DatabaseSchema {
    public static final String CREATE_BASE_ENTITY = "create TABLE `baseentity` (\n" +
            "    `dtype` varchar(31) COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
            "    `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "    `created` datetime(6) DEFAULT NULL,\n" +
            "    `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,\n" +
            "    `realm` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
            "    `updated` datetime(6) DEFAULT NULL,\n" +
            "    `code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
            "    `status` int(11) DEFAULT NULL,\n" +
            "    PRIMARY KEY (`id`),\n" +
            "    UNIQUE KEY `UKb4u1syrco33nx6qj3a96xyihb` (`code`,`realm`),\n" +
            "    KEY `code_idx` (`code`,`realm`),\n" +
            "    KEY `r_s_c` (`realm`,`status`,`code`),\n" +
            "    KEY `r_s_n` (`realm`,`status`,`name`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=84597 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;\n";

    public static final String CREATE_BASE_ENTITY_ATTRIBUTE = "create TABLE `baseentity_attribute` (\n" +
            "    `attributeCode` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,\n" +
            "    `baseEntityCode` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,\n" +
            "    `created` datetime(6) DEFAULT NULL,\n" +
            "    `inferred` bit(1) DEFAULT NULL,\n" +
            "    `privacyFlag` bit(1) DEFAULT NULL,\n" +
            "    `readonly` bit(1) DEFAULT NULL,\n" +
            "    `realm` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,\n" +
            "    `updated` datetime(6) DEFAULT NULL,\n" +
            "    `valueBoolean` bit(1) DEFAULT NULL,\n" +
            "    `valueDate` date DEFAULT NULL,\n" +
            "    `valueDateRange` tinyblob DEFAULT NULL,\n" +
            "    `valueDateTime` datetime(6) DEFAULT NULL,\n" +
            "    `valueDouble` double DEFAULT NULL,\n" +
            "    `valueInteger` int(11) DEFAULT NULL,\n" +
            "    `valueLong` bigint(20) DEFAULT NULL,\n" +
            "    `money` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,\n" +
            "    `valueString` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,\n" +
            "    `valueTime` time DEFAULT NULL,\n" +
            "    `weight` double DEFAULT NULL,\n" +
            "    `ATTRIBUTE_ID` bigint(20) NOT NULL,\n" +
            "    `BASEENTITY_ID` bigint(20) NOT NULL,\n" +
            "    `icon` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,\n" +
            "    `confirmationFlag` bit(1) DEFAULT NULL,\n" +
            "    PRIMARY KEY (`ATTRIBUTE_ID`,`BASEENTITY_ID`),\n" +
            "    UNIQUE KEY `UKfhe6ytcnf3pqww35brvtadvta` (`attributeCode`,`baseEntityCode`,`realm`),\n" +
            "    KEY `FKmqrqcxsqu49b0cliy2tymjoae` (`BASEENTITY_ID`),\n" +
            "    KEY `id_search` (`baseEntityCode`,`attributeCode`,`valueString`(20)),\n" +
            "    KEY `ba_idx` (`attributeCode`,`baseEntityCode`,`realm`),\n" +
            "    KEY `rvsvb2` (`realm`,`valueString`(25),`valueBoolean`),\n" +
            "    KEY `beid_attrcode_valStr_valBool_idx` (`BASEENTITY_ID`,`attributeCode`,`valueString`(20),`valueBoolean`),\n" +
            "    CONSTRAINT `FKmqrqcxsqu49b0cliy2tymjoae` FOREIGN KEY (`BASEENTITY_ID`) REFERENCES `baseentity` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

}
