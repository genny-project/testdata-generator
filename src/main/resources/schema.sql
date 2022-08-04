create TABLE `baseentity` (
    `dtype` varchar(31) COLLATE utf8mb4_unicode_ci NOT NULL,
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `created` datetime(6) DEFAULT NULL,
    `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `realm` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `updated` datetime(6) DEFAULT NULL,
    `code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `status` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKb4u1syrco33nx6qj3a96xyihb` (`code`,`realm`),
    KEY `code_idx` (`code`,`realm`),
    KEY `r_s_c` (`realm`,`status`,`code`),
    KEY `r_s_n` (`realm`,`status`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=84597 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


create TABLE `baseentity_attribute` (
    `attributeCode` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `baseEntityCode` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created` datetime(6) DEFAULT NULL,
    `inferred` bit(1) DEFAULT NULL,
    `privacyFlag` bit(1) DEFAULT NULL,
    `readonly` bit(1) DEFAULT NULL,
    `realm` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `updated` datetime(6) DEFAULT NULL,
    `valueBoolean` bit(1) DEFAULT NULL,
    `valueDate` date DEFAULT NULL,
    `valueDateRange` tinyblob DEFAULT NULL,
    `valueDateTime` datetime(6) DEFAULT NULL,
    `valueDouble` double DEFAULT NULL,
    `valueInteger` int(11) DEFAULT NULL,
    `valueLong` bigint(20) DEFAULT NULL,
    `money` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `valueString` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `valueTime` time DEFAULT NULL,
    `weight` double DEFAULT NULL,
    `ATTRIBUTE_ID` bigint(20) NOT NULL,
    `BASEENTITY_ID` bigint(20) NOT NULL,
    `icon` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `confirmationFlag` bit(1) DEFAULT NULL,
    PRIMARY KEY (`ATTRIBUTE_ID`,`BASEENTITY_ID`),
    UNIQUE KEY `UKfhe6ytcnf3pqww35brvtadvta` (`attributeCode`,`baseEntityCode`,`realm`),
    KEY `FKmqrqcxsqu49b0cliy2tymjoae` (`BASEENTITY_ID`),
    KEY `id_search` (`baseEntityCode`,`attributeCode`,`valueString`(20)),
    KEY `ba_idx` (`attributeCode`,`baseEntityCode`,`realm`),
    KEY `rvsvb2` (`realm`,`valueString`(25),`valueBoolean`),
    KEY `beid_attrcode_valStr_valBool_idx` (`BASEENTITY_ID`,`attributeCode`,`valueString`(20),`valueBoolean`),
    CONSTRAINT `FKaedpn6csuwk6uwm5kqh73tiwd` FOREIGN KEY (`ATTRIBUTE_ID`) REFERENCES `attribute` (`id`),
    CONSTRAINT `FKmqrqcxsqu49b0cliy2tymjoae` FOREIGN KEY (`BASEENTITY_ID`) REFERENCES `baseentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `address` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `json_data` longtext NOT NULL,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;