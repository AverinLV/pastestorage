CREATE TABLE service_user (
                              username VARCHAR(255) PRIMARY KEY,
                              password VARCHAR(255),
                              birth_date DATE,
                              user_role VARCHAR(255)
);

CREATE TABLE Paste (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       text_data VARCHAR,
                       expire_date timestamp,
                       created_at timestamp,
                       access_type VARCHAR(255),
                       created_by VARCHAR(255) REFERENCES service_user
);