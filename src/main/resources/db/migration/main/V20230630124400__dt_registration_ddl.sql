CREATE TABLE egov_dt_registration
(
    id               character varying(64) NOT NULL,
    tenantid         character varying(50) NOT NULL,
    applicationno    character varying(64) NOT NULL,
    firstname        character varying(200),
    lastname         character varying(200),
    placeofdeath     character varying(1000),
    timeofdeath      bigint,
    createdtime      bigint,
    createdby        character varying(64),
    lastmodifiedtime bigint,
    lastmodifiedby   character varying(64),
    CONSTRAINT eg_death_regn_pkey PRIMARY KEY (id),
    CONSTRAINT eg_death_regn_ukey1 UNIQUE (applicationno, tenantid)
);


CREATE TABLE egov_dt_address
(
    id               character varying(64) NOT NULL,
    tenantid         character varying(50) NOT NULL,
    registrationid   character varying(64),
    addressid        character varying(64),
    latitude         bigint,
    longitude        bigint,
    addressNumber    character varying(64),
    addressline1     character varying(1000),
    addressline2     character varying(1000),
    landmark         character varying(1000),
    city             character varying(100),
    pincode          character varying(100),
    detail           character varying(1000),
    createdby        character varying(64),
    createdtime      bigint,
    lastmodifiedby   character varying(64),
    lastmodifiedtime bigint,
    CONSTRAINT eg_death_addr_pkey PRIMARY KEY (id),
    CONSTRAINT eg_death_addr_fkey1 FOREIGN KEY (registrationid)
        REFERENCES public.egov_dt_registration (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);