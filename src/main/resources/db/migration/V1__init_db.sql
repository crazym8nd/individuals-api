CREATE TABLE IF NOT EXISTS persons (
                                       id UUID PRIMARY KEY,
                                       name VARCHAR(256) NOT NULL,
                                       password VARCHAR(256) NOT NULL,
                                       email VARCHAR(256) NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(256) NOT NULL,
    updated_by VARCHAR(256),
    status VARCHAR(256) NOT NULL
    );